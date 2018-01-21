package com.sparshwadhwa.modernstore;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static com.sparshwadhwa.modernstore.MainActivity.cartTotal;


/**
 * A simple {@link Fragment} subclass.
 */
public class cart extends Fragment {

private TextView cusName , addressLine1 , addressDetails , totalCost;
private DatabaseReference userFirebasedatabaseRef;
private FirebaseAuth mFirebaseAuth;
private String uid="";
private RelativeLayout cartLayout;
private RelativeLayout displayLayout;
private ListView cartItemList;
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
        cartItemList = myView.findViewById(R.id.listView);
        mFirebaseAuth = FirebaseAuth.getInstance();
       // final LottieAnimationView animationView = (LottieAnimationView) myView.findViewById(R.id.cart_view);
        if(mFirebaseAuth.getCurrentUser()!=null)
        {
            uid = mFirebaseAuth.getCurrentUser().getUid();
        }
        userFirebasedatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        final List<String> cartList = new ArrayList<>();
        final List<String> cartListKey = new ArrayList<>();
        userFirebasedatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("cart").getValue() == null) {
                    displayLayout.setVisibility(View.GONE);

                } else {
                    cartLayout.setVisibility(View.GONE);
                    displayLayout.setVisibility(View.VISIBLE);
                    cusName.setText(dataSnapshot.child("name").getValue().toString());
                    addressLine1.setText(dataSnapshot.child("address line1").getValue().toString());
                    String details = dataSnapshot.child("address line2").getValue().toString() + " " + dataSnapshot.child("cityandstate").getValue().toString();
                    addressDetails.setText(details);

                    for (DataSnapshot dsp : dataSnapshot.child("cart").getChildren()) {

                      //  Toast.makeText(getContext(),dsp.getValue().toString() ,Toast.LENGTH_SHORT).show();
                        cartList.add(String.valueOf(dsp.getValue()));
                        cartListKey.add(String.valueOf(dsp.getKey()));



                    }
                    cartItemAdapter adapter = new cartItemAdapter(getLayoutInflater() , getContext(),cartList ,cartListKey);
                    cartItemList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        totalCost = myView.findViewById(R.id.totalcost);
        totalCost.setText(cartTotal);

        return myView;
    }

}
