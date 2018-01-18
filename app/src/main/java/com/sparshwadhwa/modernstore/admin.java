package com.sparshwadhwa.modernstore;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class admin extends Fragment {

    private Button genre_save_buttn ,genre_item_save_buttn;
    private EditText new_genre_name , new_genre_item_name , price;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference mfirebasedatabaserefrence;

    private String genre_selected="" , price_type_selected ="" ;




    public admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final   View MyView =  inflater.inflate(R.layout.fragment_admin, container, false);
        mfirebasedatabase = FirebaseDatabase.getInstance();
        mfirebasedatabaserefrence = mfirebasedatabase.getReference().child("genre");

        new_genre_name = (EditText) MyView.findViewById(R.id.addgenre_field);
        new_genre_item_name = (EditText)MyView.findViewById(R.id.genre_item);
        price = (EditText) MyView.findViewById(R.id.item_price);

        genre_save_buttn = (Button) MyView.findViewById(R.id.genre_save);
        genre_item_save_buttn = (Button)MyView.findViewById(R.id.genre_item_save);
        genre_save_buttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfirebasedatabase.getReference().child("genre").push().setValue(new_genre_name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),new_genre_name.toString() +"added", Toast.LENGTH_SHORT).show();

                        new_genre_name.setText("");
                        //make intent to explore frag=========================>>>>>>>>>>>>>>
                    }
                });

                // new_genre_name.getText().toString()
            }
        });

//---------------------genre selector--------------------------------------

        final List<String> genre_list = new ArrayList<String>();

        mfirebasedatabase.getReference().child("genre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){

                    genre_list.add(String.valueOf(dsp.getValue()));


                }
                final Spinner genre_spinner1 =(Spinner)MyView.findViewById(R.id.genre_spinner);

                ArrayAdapter<String> genreselector= new ArrayAdapter<String>(getActivity(),android.
                        R.layout.simple_spinner_dropdown_item ,genre_list);
                genreselector.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                genre_spinner1.setAdapter(genreselector);
                genre_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // Get select item
                        genre_selected = parent.getItemAtPosition(position).toString();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });
//                genre_list.clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//------------------------------price type selector------------------------------------------
        final String[] weight_type={
                "kg","per unit","Litre"
        };
        final Spinner genreitem_weight_spinner =(Spinner)MyView.findViewById(R.id.genre_item_weight);

        ArrayAdapter<String> item_weighttype= new ArrayAdapter<String>(getActivity(),android.
                R.layout.simple_spinner_dropdown_item ,weight_type);
        item_weighttype.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        genreitem_weight_spinner.setAdapter(item_weighttype);
        genreitem_weight_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                price_type_selected = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        genre_item_save_buttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new_genre_item_name.getText()!=null || price.getText()!=null)
                {
                    mfirebasedatabase.getReference().child("genre_items").child(genre_selected).child(new_genre_item_name.getText().toString()).child("price").setValue(price.getText().toString());
                    mfirebasedatabase.getReference().child("genre_items").child(genre_selected).child(new_genre_item_name.getText().toString()).child("price_type").setValue(price_type_selected);
                    mfirebasedatabase.getReference().child("genre_items").child(genre_selected).child(new_genre_item_name.getText().toString()).child("status").setValue("available");

                    Toast.makeText(getActivity(),new_genre_item_name.toString()+"in genre" + genre_selected +"added", Toast.LENGTH_SHORT).show();
                    new_genre_item_name.setText("");
                    price.setText("");
                    //make intent to explore frag=========================>>>>>>>>>>>>>>

                }
            }
        });



        return MyView;
    }

}