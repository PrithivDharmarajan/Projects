package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.ui.media.MediaFileList;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceEdtBtnCallback;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyMediaDeviceListAdapter extends RecyclerView.Adapter<MyMediaDeviceListAdapter.Holder> {

    private Context mContext;

    public MyMediaDeviceListAdapter(Context context) {
        mContext = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_my_media_device_list, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.mMediaEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogManager.getInstance().showEdtDeviceNamePopup(mContext, "", new InterfaceEdtBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick(String edtStr) {
                        APIRequestHandler.getInstance().deviceRenameAPICalll("1234", edtStr, "12345", ((BaseActivity) mContext));

                    }
                });

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((BaseActivity) mContext).nextScreen(MediaFileList.class);

            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.media_edit_img)
        ImageView mMediaEditImg;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
