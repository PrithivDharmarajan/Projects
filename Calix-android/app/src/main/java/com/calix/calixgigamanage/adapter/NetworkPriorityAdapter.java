package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.support.dragdrop.ItemTouchHelperAdapter;
import com.calix.calixgigamanage.support.dragdrop.ItemTouchHelperViewHolder;
import com.calix.calixgigamanage.support.dragdrop.OnStartDragListener;
import com.calix.calixgigamanage.ui.network.NetworkUsage;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;



public class NetworkPriorityAdapter extends RecyclerView.Adapter<NetworkPriorityAdapter.ControlHolder>
        implements ItemTouchHelperAdapter {

    private Context mContext;

    private OnStartDragListener mDragStartListener;
    ArrayList<String> dummyList;

    public NetworkPriorityAdapter(Context context, ArrayList<String> dummyList, OnStartDragListener dragStartListener) {
        mContext = context;
        mDragStartListener = dragStartListener;
        this.dummyList = dummyList;
    }


    @Override
    public ControlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_network_priority, parent, false));
    }

    @Override
    public void onBindViewHolder(final ControlHolder holder, final int position) {
        holder.mDeviceNameTxt.setText(dummyList.get(position));


        holder.mDeviceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) mContext).nextScreen(NetworkUsage.class);
            }
        });


        holder.mItemRowLay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {

                    mDragStartListener.onStartDrag(holder);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        holder.mUpImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int toPosition = position != 0 ? position - 1 : 0;
                Collections.swap(dummyList, position, toPosition);
                //notifyItemMoved(position, toPosition);
                notifyDataSetChanged();


            }
        });

        holder.mDownImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int toPosition = position != dummyList.size() ? position + 1 : dummyList.size();
                Collections.swap(dummyList, position, toPosition);
                //notifyItemMoved(position, toPosition);
                notifyDataSetChanged();
            }
        });


    }


    @Override
    public int getItemCount() {
        return dummyList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(dummyList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    @Override
    public void onItemDismiss(int position) {

    }

    class ControlHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        @BindView(R.id.adapter_control_device_img)
        ImageView mDeviceImg;

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.wifi_connect_status_txt)
        TextView mWifiPointTxt;

        @BindView(R.id.adapter_network_row_lay)
        RelativeLayout mItemRowLay;

        @BindView(R.id.up_img_lay)
        RelativeLayout mUpImgLay;

        @BindView(R.id.down_img_lay)
        RelativeLayout mDownImgLay;

        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
