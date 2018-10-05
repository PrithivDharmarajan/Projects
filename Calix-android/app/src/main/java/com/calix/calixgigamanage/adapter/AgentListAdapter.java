package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.output.model.AlexaAppIdEntity;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigamanage.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.Holder> {

    private ArrayList<AlexaAppIdEntity> mAgentListArrList;
    private InterfaceEdtBtnCallback mInterfaceEdtBtnCallback;
    private Context mContext;

    public AgentListAdapter(ArrayList<AlexaAppIdEntity> agentListArrList, InterfaceEdtBtnCallback interfaceEdtBtnCallback, Context context) {
        mAgentListArrList = agentListArrList;
        mInterfaceEdtBtnCallback = interfaceEdtBtnCallback;
        mContext = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_spinner_equ_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {

        holder.mAgentListTxt.setText(mAgentListArrList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogManager.getInstance().showOptionPopup(mContext, mContext.getString(R.string.uninstall_msg), mContext.getString(R.string.yes), mContext.getString(R.string.no), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onPositiveClick() {
                        mInterfaceEdtBtnCallback.onPositiveClick(mAgentListArrList.get(holder.getAdapterPosition()).getName());
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAgentListArrList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.spinner_txt)
        TextView mAgentListTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
