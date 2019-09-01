package com.elior.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {

    private List<Model> list_data;
    private Context context;
    private final LayoutInflater mInflater;
    private List<Model> contactListFiltered;

    public MyAdapter(List<Model> list_data, Context context) {
        this.list_data = list_data;
        this.context = context;
        this.contactListFiltered = list_data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.data_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Model listData = contactListFiltered.get(position);
        Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                + listData.getPhoto_reference() +
                "&key=AIzaSyDW58Amxy2klMcesFN2OT6XzvZZAj3Zyp0").into(holder.mPhoto);
        holder.textName.setText(listData.getName());
        holder.textVicinity.setText(listData.getVicinity());
        holder.textLat.setText(String.valueOf(listData.getLat()));
        holder.textLng.setText(String.valueOf(listData.getLng()));
        holder.textId.setText(listData.getId());

        holder.text_types.setText(" ");
        for (int i = 0; i < listData.getTypes().length; i++) {
            holder.text_types.append(String.valueOf(listData.getTypes()[i] + " "));
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), AddPlace.class);
                mInflater.getContext().startActivity(intent);
            }
        });

        Collections.sort(list_data, new Comparator<Model>() {
            public int compare(Model obj1, Model obj2) {
                return obj1.getName().compareToIgnoreCase(obj2.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mPhoto;
        private TextView textName, textVicinity, textLat, textLng, textId, text_types;
        private RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mPhoto = itemView.findViewById(R.id.image_view);
            textName = itemView.findViewById(R.id.text_name);
            textVicinity = itemView.findViewById(R.id.text_vicinity);
            textLat = itemView.findViewById(R.id.text_lat);
            textLng = itemView.findViewById(R.id.text_lng);
            textId = itemView.findViewById(R.id.text_id);
            text_types = itemView.findViewById(R.id.text_types);

            relativeLayout = itemView.findViewById(R.id.relative1);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = list_data;
                } else {
                    List<Model> filteredList = new ArrayList<>();
                    for (Model row : list_data) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    contactListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
