package com.hoangtuan.translatechinesehwriting.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoangtuan.translatechinesehwriting.Model.GoiYModel;
import com.hoangtuan.translatechinesehwriting.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by atbic on 2/4/2018.
 */

public class GoiYAdapter extends RecyclerView.Adapter<GoiYAdapter.GoiYHolder> {
    private Context context;
    private List<GoiYModel> goiYModels;

    public GoiYAdapter() {
    }

    public GoiYAdapter(Context context) {
        this.context = context;
       goiYModels = Collections.EMPTY_LIST;
    }
    public void setCandidates(List<GoiYModel> candidates) {
        goiYModels = candidates;
        notifyDataSetChanged();
    }
    @Override
    public GoiYHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goiy, parent, false);
        return new GoiYHolder(view);
    }

    @Override
    public void onBindViewHolder(GoiYHolder holder, int position) {
        GoiYModel goiYModel = goiYModels.get(position);
        holder.txtGoiY.setText(goiYModel.getText());

    }

    @Override
    public int getItemCount() {
        return goiYModels.size();
    }

    public class GoiYHolder extends RecyclerView.ViewHolder {
        TextView txtGoiY;


        public GoiYHolder(View itemView) {
            super(itemView);
            txtGoiY = (TextView) itemView.findViewById(R.id.txtGoiY);


        }
    }
}
