package com.sparshwadhwa.modernstore;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PrivateKey;

import static com.sparshwadhwa.modernstore.cart.line1FromCart;
import static com.sparshwadhwa.modernstore.cart.line2FromCart;
import static com.sparshwadhwa.modernstore.cart.nameFromCart;
import static com.sparshwadhwa.modernstore.cart.uid;

public class addAddressActivity extends AppCompatActivity {

    private EditText name , addressLine1 , addrssLine2, city , State , alternativePhnNo;
    private Button saveAddressButton;
    private DatabaseReference mFirebaseDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        name = findViewById(R.id.enter_name);
        addressLine1 = findViewById(R.id.enter_address_line1);
        addrssLine2 = findViewById(R.id.enter_address_line2);
        city = findViewById(R.id.enter_city);
        State = findViewById(R.id.enter_state);
        alternativePhnNo = findViewById(R.id.enter_alternative_PhnNo);
        saveAddressButton = findViewById(R.id.save_addressButton);

        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        if(!TextUtils.isEmpty(nameFromCart))
        {
            name.setText(nameFromCart);
        }
        if(!TextUtils.isEmpty(line1FromCart))
        {
            addressLine1.setText(line1FromCart);
        }
        if(!TextUtils.isEmpty(line2FromCart))
        {
            addrssLine2.setText(line2FromCart);
        }

        saveAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(name.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"if1",Toast.LENGTH_SHORT).show();

                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(2)
                            .playOn(findViewById(R.id.enter_name));
                }
                else  if(TextUtils.isEmpty(addressLine1.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"if2",Toast.LENGTH_SHORT).show();

                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(2)
                            .playOn(findViewById(R.id.enter_address_line1));
                }
                else  if(TextUtils.isEmpty(addrssLine2.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"if3",Toast.LENGTH_SHORT).show();

                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(2)
                            .playOn(findViewById(R.id.enter_address_line2));
                }
                else  if(TextUtils.isEmpty(city.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"if4",Toast.LENGTH_SHORT).show();

                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(2)
                            .playOn(findViewById(R.id.enter_city));
                }
                else if(TextUtils.isEmpty(State.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"if5",Toast.LENGTH_SHORT).show();

                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(2)
                            .playOn(findViewById(R.id.enter_state));
                }
                else  if(alternativePhnNo.getText().toString().length()!=0&&alternativePhnNo.getText().toString().length()!=10)
                {
                    Toast.makeText(getApplicationContext(),"if6",Toast.LENGTH_SHORT).show();

                    if(alternativePhnNo.getText().toString().length()<10) {
                        YoYo.with(Techniques.Shake)
                                .duration(700)
                                .repeat(2)
                                .playOn(findViewById(R.id.enter_alternative_PhnNo));
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                    mFirebaseDatabaseRef.child("name").setValue(name.getText().toString());
                    mFirebaseDatabaseRef.child("address line1").setValue(addressLine1.getText().toString());
                    mFirebaseDatabaseRef.child("address line2").setValue(addrssLine2.getText().toString());
                    mFirebaseDatabaseRef.child("cityandstate").setValue(city.getText().toString() + " " + State.getText().toString());
                    if(!TextUtils.isEmpty(alternativePhnNo.getText().toString()))
                        mFirebaseDatabaseRef.child("alternativePhnNo").setValue(alternativePhnNo.getText().toString());
                    //=============eithr close this act or make intent to cart frag
finish();


                }


            }
        });

    }
}
