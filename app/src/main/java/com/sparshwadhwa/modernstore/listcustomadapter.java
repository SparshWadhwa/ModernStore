package com.sparshwadhwa.modernstore;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

import static com.sparshwadhwa.modernstore.MainActivity.cartTotal;

/**
 * Created by spars on 12-01-2018.
 */

public class listcustomadapter extends BaseAdapter {
    private LayoutInflater inf;
    private List<String> valuesItems , valuesItemDetails;
    private TextView item_Name , item_price ;
    private Button addButoon;
    private String genre_Name;
    private DatabaseReference cartDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
   listcustomadapter(LayoutInflater inflater ,List<String> myDataset , List<String> myDatasetDetails ,String genreName ){
        this.inf=inflater;
        valuesItems = myDataset;
        valuesItemDetails = myDatasetDetails;
        genre_Name = genreName;
    }
    @Override
    public int getCount() {
//       i++;
//        return i;
        return valuesItems.size();
    }

    @Override
    public Object getItem(int i) {



        return valuesItems.get(i);
    }

    public Object getItemDetails(int i) {



        return valuesItemDetails.get(i);
    }


    public void add(int i, String item) {
        valuesItems.add(i, item);
//        notifyItemInserted(i);
    }

    public void remove(int i) {
        valuesItems.remove(i);
//        notifyItemRemoved(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=inf.inflate(R.layout.listcustomadapter,viewGroup,false);
        }
        item_Name = (TextView) view.findViewById(R.id.item_name);
        addButoon = view.findViewById(R.id.add_buttn);
        final String itemName = getItem(i).toString();
        item_Name.setText(itemName);
        item_price = view.findViewById(R.id.item_price);

        String price_type = "", status = "", unit = "", price = "", quantityType ,priceMRP = "";

        if(getItemDetails(i)!="-") {
            final String details = getItemDetails(i).toString();
            String priceTypenPrice = "";
            for (int j = 0; j < details.length(); j++) {
                if (details.charAt(j) == ',') {
                    status = details.substring(1, j );
                    //item_price.setText(price);
                    priceTypenPrice = details.substring(j + 1, details.length() - 1);
                    break;
                }
            }

            for (int k = 0; k < priceTypenPrice.length(); k++) {
                if (priceTypenPrice.charAt(k) == ',') {
                    price_type = priceTypenPrice.substring(0, k );    //it contains in the format price_type=kg
                    price = priceTypenPrice.substring(k + 1, priceTypenPrice.length());  //it contains in the format price = available
                    break;
                }
            }
            for (int j=0;j<price.length();j++)
            {
                if(price.charAt(j)=='=')
                {
                    priceMRP = price.substring(j+1,price.length());
                }
            }

            for (int j = 0; j < price_type.length(); j++) {
                if (price_type.charAt(j) == '=') {
                    unit = price_type.substring(j + 1, price_type.length());
                    quantityType = "price : "+ priceMRP+ " per " + price_type.substring(j + 1, price_type.length());
                    if(status.equals("status=unavailable"))
                    {
                        item_price.setText("Sorry , Currently this item is not available.");
                        addButoon.setEnabled(false);
                    }
                    else
                    {item_price.setText(quantityType);}
                    break;
                }
            }

        }
        else
        {
            item_price.setVisibility(view.GONE);
            addButoon.setVisibility(view.GONE);
        }
        final String priceForDialogBox = priceMRP , unitForDialogBox = unit;
        String uid ="";
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser()!=null)
        {
             uid = mFirebaseAuth.getCurrentUser().getUid();
        }
        final String userUID = uid;

        addButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // LayoutInflater layoutInflaterAndroid = LayoutInflater.from(mContext);

                View mView = inf.inflate(R.layout.user_input_dialog_box, null);

                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(inf.getContext());

                alertDialogBuilderUserInput.setView(mView);

                final TextView itemNameTextView =  mView.findViewById(R.id.dialogTitle);
                itemNameTextView.setText(itemName);

                final TextView item_priceDialogBox = mView.findViewById(R.id.itemPrice);
                String pricePerUnit = priceForDialogBox+ " per "+unitForDialogBox;
                    item_priceDialogBox.setText(pricePerUnit);
                final TextView unitDialogBox = mView.findViewById(R.id.itemUnit);
                    unitDialogBox.setText(unitForDialogBox);

                final EditText userInputDialogEditText =  mView.findViewById(R.id.userInputDialog);

                alertDialogBuilderUserInput

                        .setCancelable(false)

                        .setPositiveButton("ADD to Cart", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialogBox, int id) {

                                if(TextUtils.isEmpty(userInputDialogEditText.getText())) {
                                    Toast.makeText(inf.getContext(),"ENTER AMOUNT",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    cartDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
                                    cartDatabaseRef.child("cart").push().setValue(itemName+ "@" + userInputDialogEditText.getText() +"#"+unitForDialogBox+ "$" +priceForDialogBox);
                                    // ToDo get user input here
                                    Float temp = Float.parseFloat(cartTotal) + Float.parseFloat(userInputDialogEditText.getText().toString())*Float.parseFloat(priceForDialogBox);
                                    cartDatabaseRef.child("carttotal").setValue(Float.parseFloat(Float.toString(temp)));
                                    Toast.makeText(inf.getContext(),"Added to cart",Toast.LENGTH_SHORT).show();
                                    dialogBox.dismiss();

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

        return view;
    }
}
