package com.ccec.dexterservice.managers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccec.dexterservice.R;

import java.util.ArrayList;

public class QueryviewAdapter extends RecyclerView.Adapter<QueryviewAdapter.ViewHolder> {
    private ArrayList<String> countries;
    private Context ctx;

    public QueryviewAdapter(ArrayList<String> countries, Context ctx) {
        this.countries = countries;
        this.ctx = ctx;
    }

    @Override
    public QueryviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_query_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QueryviewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv_country.setText(countries.get(i));
        viewHolder.tv_country.setTypeface(FontsManager.getRegularTypeface(ctx));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_country;

        public ViewHolder(View view) {
            super(view);

            tv_country = (TextView) view.findViewById(R.id.tv_country);
        }
    }
}
