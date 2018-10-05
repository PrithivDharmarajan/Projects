package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.ui.media.MediaDeviceList;

public class MyMediaAdapter extends RecyclerView.Adapter<MyMediaAdapter.Holder> {

    private Context mContext;

    public MyMediaAdapter(Context context) {
        mContext = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_my_media, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((BaseActivity)mContext).nextScreen(MediaDeviceList.class);

            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
