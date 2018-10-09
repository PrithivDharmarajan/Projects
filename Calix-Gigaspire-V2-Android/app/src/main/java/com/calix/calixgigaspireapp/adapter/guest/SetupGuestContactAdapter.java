package com.calix.calixgigaspireapp.adapter.guest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AbdulRahim(SmaatApps) on 1/11/2018.
 */

public class SetupGuestContactAdapter extends RecyclerView.Adapter<SetupGuestContactAdapter.ControlHolder> {

    private Context mContext;
    private String[] mKeys;

    HashMap<String, Boolean> mContactsHashMap = new HashMap<>();


    public SetupGuestContactAdapter(Context context, HashMap<String, Boolean> contactsHashMap) {
        mContext = context;
        mContactsHashMap = contactsHashMap;
        mKeys = mContactsHashMap.keySet().toArray(new String[contactsHashMap.size()]);

    }


    @NonNull
    @Override
    public SetupGuestContactAdapter.ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SetupGuestContactAdapter.ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setup_guset_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SetupGuestContactAdapter.ControlHolder holder, int position) {

        if (mKeys != null) {
            holder.mGuestNetworkContactTxt.setText(mKeys[position]);
            holder.mContactCheckBox.setChecked(mContactsHashMap.get((mKeys[position])));
        }

    }

    @Override
    public int getItemCount() {
        return mContactsHashMap.size();
    }

    class ControlHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.guest_network_contact_txt)
        TextView mGuestNetworkContactTxt;

        @BindView(R.id.adapter_setup_guest_contact_checkbox)
        CheckBox mContactCheckBox;

        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

