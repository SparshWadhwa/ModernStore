package com.sparshwadhwa.modernstore;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class adim_status extends Fragment {

    private Button check_status_button,change_status_button,get_genreitem_button;
    private TextView status_view,testview;
    private String genre_status_selected="" , genreitem_status_selected="";

    private FirebaseDatabase mfirebasedatabase;
    final private String genreval="";


    public adim_status() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View MyView =  inflater.inflate(R.layout.fragment_adim_status, container, false);
        mfirebasedatabase =FirebaseDatabase.getInstance();

        //----------------------------------------STATUS OPTIONS--------------------------------------------------------------

        //------------------------genre select-------------------
        final List<String> genre_list = new ArrayList<String>();


        mfirebasedatabase.getReference().child("genre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren()){

                    genre_list.add(String.valueOf(dsp.getValue()));


                }
                final Spinner genre_spinner3 =(Spinner)MyView.findViewById(R.id.genre_available);

                ArrayAdapter<String> genre_selector= new ArrayAdapter<String>(getActivity(),android.
                        R.layout.simple_spinner_dropdown_item ,genre_list);
                genre_selector.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                genre_spinner3.setAdapter(genre_selector);
                genre_spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // Get select item
                        genre_status_selected = parent.getItemAtPosition(position).toString();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        get_genreitem_button = MyView.findViewById(R.id.get_genreitem_buttn);
        final List<String> genreitem_list = new ArrayList<String>();
        testview = MyView.findViewById(R.id.test);

        get_genreitem_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mfirebasedatabase.getReference().child("genre_items").child(genre_status_selected).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                          String test="1";

                        for(DataSnapshot dsp : dataSnapshot.getChildren()){

                            genreitem_list.add(String.valueOf(dsp.getKey()));
                            test = test+String.valueOf(dsp.getKey());


                        }
                        testview.setText(test);

                        final Spinner genre_spinner4 =(Spinner)MyView.findViewById(R.id.genre_item_available);

                        ArrayAdapter<String> genreitem_selector= new ArrayAdapter<String>(getActivity(),android.
                                R.layout.simple_spinner_dropdown_item ,genreitem_list);
                        genreitem_selector.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                        genre_spinner4.setAdapter(genreitem_selector);
                        genre_spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {
                                // Get select item
                                genreitem_status_selected = parent.getItemAtPosition(position).toString();

                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });




        check_status_button = (Button) MyView.findViewById(R.id.check_status_buttn);
        status_view = (TextView) MyView.findViewById(R.id.status_check);
        check_status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfirebasedatabase.getReference().child("genre_items").child(genre_status_selected).child(genreitem_status_selected).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        testview.setText(genreitem_status_selected);
                        if(dataSnapshot.child("status").getValue()!=null)
                            status_view.setText(dataSnapshot.child("status").getValue().toString());
                        else
                        {status_view.setText("null");
                            mfirebasedatabase.getReference().child("genre_items").child(genre_status_selected).child(genreitem_status_selected).child("status").setValue("hello2");}

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        change_status_button = (Button) MyView.findViewById(R.id.change_status);
        change_status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfirebasedatabase.getReference().child("genre_items").child(genre_status_selected).child(genreitem_status_selected).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(genre_status_selected!="" && genreitem_status_selected!="" && dataSnapshot.child("status").getValue().equals("available"))
                        {
                            mfirebasedatabase.getReference().child("genre_items").child(genre_status_selected).child(genreitem_status_selected).child("status").setValue("unavailable");
                            genre_status_selected="";
                            genreitem_status_selected="";
                            genreitem_list.clear();
                            Toast.makeText(getActivity(),"Succesfully chnaged status to unavailable", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(getActivity(), MainActivity.class);
                            //add data to the Intent object
                            //start the second activity
                            startActivity(intent);

                        }
                        else if(genre_status_selected!="" && genreitem_status_selected!="" && dataSnapshot.child("status").getValue().equals("unavailable"))
                        {
                            mfirebasedatabase.getReference().child("genre_items").child(genre_status_selected).child(genreitem_status_selected).child("status").setValue("available");
                            genre_status_selected = "";
                            genreitem_status_selected = "";
                            genreitem_list.clear();
                            Toast.makeText(getActivity(),"Succesfully chnaged status to available", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(getActivity(), MainActivity.class);
                            //add data to the Intent object
                            //start the second activity
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return MyView;
    }

}
