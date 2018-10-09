package com.calix.calixgigaspireapp.adapter.guest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.output.model.ContactResponse;
import com.calix.calixgigaspireapp.utils.AppConstants;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AbdulRahim(SmaatApps) on 1/11/2018.
 */

public class GuestContactAdapter extends RecyclerView.Adapter<GuestContactAdapter.Holder> {

    private ArrayList<ContactResponse> mContactArrListRes;
    private SwitchCompat mContactSwitchCompat;
    private CompoundButton.OnCheckedChangeListener mSelectAllContactChangeListener;
    private TextView mContactTxt;
    private Context mContext;


    public GuestContactAdapter(ArrayList<ContactResponse> contactArrListRes, SwitchCompat contactSwitchCompat, CompoundButton.OnCheckedChangeListener selectAllContactChangeListener, TextView contactTxt, Context context) {
        mSelectAllContactChangeListener = selectAllContactChangeListener;
        mContactArrListRes = contactArrListRes;
        mContactSwitchCompat = contactSwitchCompat;
        mContactTxt = contactTxt;
        mContext = context;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_setup_guset_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {

        holder.mGuestNetworkContactTxt.setText(mContactArrListRes.get(position).getName());
        holder.mContactCheckBox.setChecked(mContactArrListRes.get(position).isChecked());

        if (mContactArrListRes.get(position).isChecked()) {
            AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.put(mContactArrListRes.get(position).getNumber(), mContactArrListRes.get(position).getNumber());
        }

        setContactTxt(AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.size());
        holder.mContactCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mContactArrListRes.get(holder.getAdapterPosition()).setChecked(isChecked);
                if (isChecked && AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.get(mContactArrListRes.get(holder.getAdapterPosition()).getNumber()) == null) {
                    AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.put(mContactArrListRes.get(holder.getAdapterPosition()).getNumber(), mContactArrListRes.get(holder.getAdapterPosition()).getNumber());
                } else if (!isChecked) {
                    AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.remove(mContactArrListRes.get(holder.getAdapterPosition()).getNumber());
                }
                mContactSwitchCompat.setOnCheckedChangeListener(null);
                mContactSwitchCompat.setChecked(mContactArrListRes.size() == AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.size());
                mContactSwitchCompat.setOnCheckedChangeListener(mSelectAllContactChangeListener);
                setContactTxt(AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.size());
            }
        });

    }

    private void setContactTxt(int contactSizeInt) {
        if (contactSizeInt > 0) {
            mContactTxt.setText(String.format(mContext.getString(contactSizeInt > 1 ? R.string.contacts_selected : R.string.contact_selected), contactSizeInt));
        }
        mContactTxt.setVisibility(contactSizeInt > 0 ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return mContactArrListRes.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.guest_network_contact_txt)
        TextView mGuestNetworkContactTxt;

        @BindView(R.id.adapter_setup_guest_contact_checkbox)
        CheckBox mContactCheckBox;

        private Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.setIsRecyclable(false);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

