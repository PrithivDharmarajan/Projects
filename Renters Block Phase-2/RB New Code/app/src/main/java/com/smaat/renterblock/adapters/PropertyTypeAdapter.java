package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyDialogEntity;
import com.smaat.renterblock.utils.AppConstants;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PropertyTypeAdapter extends RecyclerView.Adapter<PropertyTypeAdapter.Holder> {

    private ArrayList<PropertyDialogEntity> mPropertyList = new ArrayList<>();
    private ArrayList<String> mResultList = new ArrayList<>();
    private Context mContext;


    public PropertyTypeAdapter(Context context, ArrayList<PropertyDialogEntity> propertyList) {
        mPropertyList = propertyList;
        mContext = context;

    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_property_type, parent, false);

        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {


        final PropertyDialogEntity mEntity = mPropertyList.get(position);

        holder.mTypeTxt.setText(mEntity.getType());
        holder.mTypeImg.setImageResource(mEntity.is_selected() ? R.drawable.tick_on : R.drawable.tick_off);
        if (mEntity.is_selected()) {
            mResultList.add(mEntity.getType());
        }

        AppConstants.PROPERTY_TYPE = TextUtils.join(",", mResultList);

        holder.mTypeLay.setTag(holder.getAdapterPosition());
        holder.mTypeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if (pos == 0) {
                    if (mPropertyList.get(pos).is_selected()) {
                        mPropertyList.get(pos).setIs_selected(false);
                        mResultList.remove(mPropertyList.get(pos).getType());
                    } else {
                        mPropertyList.get(pos).setIs_selected(true);
                        for (int i = 1; i < mPropertyList.size(); i++) {
                            mPropertyList.get(i).setIs_selected(false);
                        }
                        mResultList.clear();
                        mResultList.add(mPropertyList.get(pos).getType());
                    }

                } else {
                    if (mPropertyList.get(pos).is_selected()) {
                        mPropertyList.get(pos).setIs_selected(false);
                        mResultList.remove(mPropertyList.get(pos).getType());
                    } else {
                        mPropertyList.get(pos).setIs_selected(true);
                        mPropertyList.get(0).setIs_selected(false);
                        mResultList.add(mPropertyList.get(pos).getType());
                        mResultList.remove(mPropertyList.get(0).getType());
                    }
                }
                mResultList.clear();
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPropertyList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.type_txt)
        TextView mTypeTxt;

        @BindView(R.id.type_img)
        ImageView mTypeImg;

        @BindView(R.id.types_lay)
        RelativeLayout mTypeLay;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
