package com.sparshwadhwa.modernstore;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spars on 12-01-2018.
 */

public class cardAdapter extends RecyclerView.Adapter<cardAdapter.MyViewHolder> {
    ListView listView;
    LayoutInflater laf;
    List list;
    private List<String> values;
    private  ClickListener clicklistener = null;
    private Context mContext;
    private DatabaseReference genreItemDatabaseRef;

    CardView card;
    public cardAdapter(Context context,LayoutInflater laf,List<String> myDataset){
        this.laf=laf;
        values = myDataset;
        mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private LinearLayout main;
        private TextView genreName;
        private ImageView arrow;
        private ListView listView;

       private boolean flag=true;
        public MyViewHolder(final View parent) {
            super(parent);

            genreName = (TextView) parent.findViewById(R.id.genre_name);
            card = (CardView) parent.findViewById(R.id.cardview);
            listView= (ListView) parent.findViewById(R.id.item_list);

            // final LinearLayout listView1= (LinearLayout) parent.findViewById(R.id.item_listt);


            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();

//
//                    listItem.add("item1");
//                    listItem.add("item2");
//                    listItem.add("item3");
//                    listItem.add("item4");
//                    listItem.add("item5");
                    genreItemDatabaseRef = FirebaseDatabase.getInstance().getReference().child("genre_items").child(values.get(getAdapterPosition()));
                    final List<String> listItem = new ArrayList<>();
                    final List<String> listItemDetails = new ArrayList<>();
                    genreItemDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {


                                listItem.add(String.valueOf(dsp.getKey()));
                                listItemDetails.add(String.valueOf(dsp.getValue()));

                            }
                            if(listItem.size()==0)
                            {
                                listItem.add("NO ITEMS");
                                listItemDetails.add("-");
                            }
                            listcustomadapter custom=new listcustomadapter(laf,listItem , listItemDetails ,values.get(getAdapterPosition()));
                            listView.setAdapter(custom);
                            custom.notifyDataSetChanged();
                            if(listItem.size() > 1){
                                View item = custom.getView(0, null, listView);
                                item.measure(0, 0);
                                ViewGroup.LayoutParams params = listView.getLayoutParams();
                                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                params.height = listItem.size() * item.getMeasuredHeight();
                                listView.setLayoutParams(params);


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    if(clicklistener !=null){
                        clicklistener.itemClicked(v,getAdapterPosition());
                    }

                    if(flag) {
                        listView.setVisibility(View.VISIBLE);
                        Toast.makeText(parent.getContext(),"visibile",Toast.LENGTH_SHORT).show();
                        flag=false;
                    }
                    else if(!flag){
                        listView.setVisibility(View.GONE);
                        flag=true;
                    }

                }
            });


        }
    }
    public void setClickListener(ClickListener clickListener){
        this.clicklistener = clickListener;
    }
    @Override
    public cardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewrecy,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {

        final String genreName =  values.get(position);
        holder.genreName.setText(genreName);





    }
    @Override
    public int getItemCount() {
        return values.size();
    }
    public interface ClickListener {
        public void itemClicked(View view, int position);
    }
}
