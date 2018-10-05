package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.calix.calixgigamanage.R;

import butterknife.ButterKnife;

/**
 * Created by AbdulRahim(SmaatApps) on 1/10/2018.
 */

public class ParentalControlFilterAdatper extends RecyclerView.Adapter<ParentalControlFilterAdatper.ControlHolder> {

    private Context mContext;

    public ParentalControlFilterAdatper(Context context) {
        mContext = context;

    }


    @Override
    public ControlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pc_filter_content, parent, false));
    }

    @Override
    public void onBindViewHolder(final ControlHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ControlHolder extends RecyclerView.ViewHolder {


        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


