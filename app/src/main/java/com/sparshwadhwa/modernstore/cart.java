package com.sparshwadhwa.modernstore;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//import static com.sparshwadhwa.modernstore.MainActivity.cartTotal;


/**
 * A simple {@link Fragment} subclass.
 */
public class cart extends Fragment {

private TextView cusName , addressLine1 , addressDetails , totalCost;
private DatabaseReference userFirebasedatabaseRef;
private FirebaseAuth mFirebaseAuth;
public static String uid="";
private RelativeLayout cartLayout;
private RelativeLayout displayLayout , addressLayout;
private ListView cartItemList;
private ImageView addAddressButton;
//private String cartTotal="";
public static String nameFromCart , line1FromCart ,cityFromCart ,stateFromCart ,line2FromCart ;
    public cart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_cart, container, false);
        cusName = myView.findViewById(R.id.cus_name);
        addressLine1 = myView.findViewById(R.id.add_line1);
        addressDetails = myView.findViewById(R.id.add_details);
        cartLayout = myView.findViewById(R.id.cart_layout);
        displayLayout = myView.findViewById(R.id.display_view);
        addressLayout = myView.findViewById(R.id.middle_one);
        cartItemList = myView.findViewById(R.id.listView);
        totalCost = myView.findViewById(R.id.totalcost);
        mFirebaseAuth = FirebaseAuth.getInstance();
       // final LottieAnimationView animationView = (LottieAnimationView) myView.findViewById(R.id.cart_view);
        if(mFirebaseAuth.getCurrentUser()!=null)
        {
            uid = mFirebaseAuth.getCurrentUser().getUid();
        }
        userFirebasedatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userFirebasedatabaseRef.keepSynced(true);
        final List<String> cartList = new ArrayList<>();
        final List<String> cartListKey = new ArrayList<>();

        userFirebasedatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("cart").getValue() == null) {
                    displayLayout.setVisibility(View.GONE);
                    addressLayout.setVisibility(View.GONE);

                } else {
                    cartLayout.setVisibility(View.GONE);
                    displayLayout.setVisibility(View.VISIBLE);
                    addressLayout.setVisibility(View.VISIBLE);
                    if(dataSnapshot.child("name").getValue()!=null) {
                        cusName.setText(dataSnapshot.child("name").getValue().toString());
                        nameFromCart = dataSnapshot.child("name").getValue().toString();
                        addressLine1.setText(dataSnapshot.child("address line1").getValue().toString());
                        line1FromCart = dataSnapshot.child("address line1").getValue().toString();
                        String details = dataSnapshot.child("address line2").getValue().toString() + " " + dataSnapshot.child("cityandstate").getValue().toString();
                        addressDetails.setText(details);
                        line2FromCart = dataSnapshot.child("address line2").getValue().toString();
                    }
                    else {
                        cusName.setText("No Name");
                        addressLine1.setText("ADD ADDRESS");
                        addressDetails.setText("");
                    }
                  final String  cartTotal = dataSnapshot.child("carttotal").getValue().toString();
                    totalCost.setText(cartTotal);


                    for (DataSnapshot dsp : dataSnapshot.child("cart").getChildren()) {

                      //  Toast.makeText(getContext(),dsp.getValue().toString() ,Toast.LENGTH_SHORT).show();
                        cartList.add(String.valueOf(dsp.getValue()));
                        cartListKey.add(String.valueOf(dsp.getKey()));



                    }
                    cartItemAdapter adapter = new cartItemAdapter(getLayoutInflater() , getContext(),cartList ,cartListKey);
                    cartItemList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                    Fragment frg = null;
//                    frg = getChildFragmentManager().findFragmentByTag("cart");
//                    final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//                    ft.detach(frg);
//                    ft.attach(frg);
//                    ft.commit();
                    if(cartList.size() > 1){
                        View item = adapter.getView(0, null, cartItemList);
                        item.measure(0, 0);
                        ViewGroup.LayoutParams params = cartItemList.getLayoutParams();
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        params.height = cartList.size() * item.getMeasuredHeight();
                        cartItemList.setLayoutParams(params);


                    }
                }
                //========================
               // getChildFragmentManager().beginTransaction().replace(R.id.container_cart,new cart()).commit();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        addAddressButton = myView.findViewById(R.id.add_address);
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), addAddressActivity.class);
                startActivity(intent);
            }
        });

        return myView;
    }

}
