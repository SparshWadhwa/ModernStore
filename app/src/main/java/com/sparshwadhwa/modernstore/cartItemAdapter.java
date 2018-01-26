package com.sparshwadhwa.modernstore;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

import static com.sparshwadhwa.modernstore.MainActivity.cartTotal;

/**
 * Created by spars on 21-01-2018.
 */

public class cartItemAdapter extends BaseAdapter {
    private List<String> valuesItems , getValuesItemsKey;
    private Context mContext;
    private LayoutInflater inf;

    private TextView cartItemName , cartItemQuantity , cartItemPrice , cartItemCost;
    private String itemCost="";
    private ImageView editCartItem , delCartItem;

    private DatabaseReference cartDatabaseRef;
    private FirebaseAuth mFirebaseAuth;

    cartItemAdapter(LayoutInflater inflater,Context context, List<String> myDataset ,List<String> myDatasetKey){
        valuesItems = myDataset;
        mContext = context;
        this.inf = inflater;
        getValuesItemsKey = myDatasetKey;

    }

    @Override
    public int getCount() {
        return valuesItems.size();
    }

    @Override
    public String getItem(int i) {
        return valuesItems.get(i);
    }

    public String getItemKey(int i) {
        return getValuesItemsKey.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view==null){
            view=inf.inflate(R.layout.cartadapteritems,viewGroup,false);
        }
        cartItemName = view.findViewById(R.id.item_name);
        cartItemPrice = view.findViewById(R.id.pricePerItem);
        cartItemQuantity = view.findViewById(R.id.quantity);
        cartItemCost = view.findViewById(R.id.total_cost);
       // String name , quantity ="", quantityUnit , price="";

         String itemDetail = getItem(i);
        //"item1@5#kg$50"

        int a = itemDetail.indexOf('@');
        int b = itemDetail.indexOf('#');
        int c = itemDetail.indexOf('$');
        final String name = itemDetail.substring(0,a);
        final String quantity = itemDetail.substring(a+1,b);
        final String quantityUnit = itemDetail.substring(b+1,c);
        final String price = itemDetail.substring(c+1,itemDetail.length());
        cartItemName.setText(name);
        cartItemQuantity.setText(quantity);

        String temp = price + " / " + quantityUnit;
        cartItemPrice.setText(temp);
        String temp2 = quantity + " x " + price +" = Rs " + Float.parseFloat(quantity)*Integer.parseInt(price);
        cartItemCost.setText(temp2);

        editCartItem = view.findViewById(R.id.edit_cart_Iten);
        delCartItem = view.findViewById(R.id.del_cart_item);

        String uid ="";
        final String key = getItemKey(i);
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser()!=null)
        {
            uid = mFirebaseAuth.getCurrentUser().getUid();
        }
        final String userUID = uid;
        editCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = inf.inflate(R.layout.user_input_dialog_box, null);

                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(inf.getContext());

                alertDialogBuilderUserInput.setView(mView);

                final TextView itemNameTextView =  mView.findViewById(R.id.dialogTitle);
                itemNameTextView.setText(name);

                final TextView item_priceDialogBox = mView.findViewById(R.id.itemPrice);
                String pricePerUnit = price+ " per "+quantityUnit;
                item_priceDialogBox.setText(pricePerUnit);
                final TextView unitDialogBox = mView.findViewById(R.id.itemUnit);
                unitDialogBox.setText(quantityUnit);

                final EditText userInputDialogEditText =  mView.findViewById(R.id.userInputDialog);

                alertDialogBuilderUserInput

                        .setCancelable(false)

                        .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogBox, int id) {

                                if(TextUtils.isEmpty(userInputDialogEditText.getText())) {
                                    Toast.makeText(inf.getContext(),"ENTER AMOUNT",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    cartDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
                                    cartDatabaseRef.child("cart").push().setValue(name+ "@" + userInputDialogEditText.getText() +"#"+quantityUnit+ "$" +price);
                                    // ToDo get user input here
                                    cartDatabaseRef.child("cart").child(key).removeValue();
                                    Float temp = Float.parseFloat(cartTotal) - Float.parseFloat(quantity)*Integer.parseInt(price) + Float.parseFloat(userInputDialogEditText.getText().toString())*Float.parseFloat(price);
                                    cartDatabaseRef.child("carttotal").setValue(Float.parseFloat(Float.toString(temp)));
                                    Toast.makeText(inf.getContext(),"Changes Made",Toast.LENGTH_SHORT).show();
                                    dialogBox.dismiss();
                                    Intent intent = new Intent(mContext,MainActivity.class);
                                    intent.putExtra("fromCart" , true);
                                    mContext.startActivity(intent);
                                    //==========================make intent to cart fragment

                                }


                            }

                        })



                        .setNegativeButton("Cancel",

                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialogBox, int id) {

                                        dialogBox.cancel();

                                    }

                                });



                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

                alertDialogAndroid.show();
            }
        });

        delCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
                cartDatabaseRef.child("cart").child(key).removeValue();
                Float temp = Float.parseFloat(cartTotal) - Float.parseFloat(quantity)*Integer.parseInt(price);
                cartDatabaseRef.child("carttotal").setValue(Float.parseFloat(Float.toString(temp)));
                //==================make intent here=================
               // getSupportFragmentManager().beginTransaction().replace(R.id.layout_main,new cart()).commit();
                valuesItems.remove(i);
                notifyDataSetChanged();
                Intent intent = new Intent(mContext,MainActivity.class);
                intent.putExtra("fromCart" , true);
                mContext.startActivity(intent);


            }
        });

        return view;
    }
}
