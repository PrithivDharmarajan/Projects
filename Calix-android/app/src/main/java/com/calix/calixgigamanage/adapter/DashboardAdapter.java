package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.CategoriesEntity;
import com.calix.calixgigamanage.ui.dashboard.DeviceDetailsList;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.ImageUtil;
import com.calix.calixgigamanage.utils.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.Holder> {

    private Context mContext;
    private ArrayList<CategoriesEntity> mCategoriesRes;

    public DashboardAdapter(ArrayList<CategoriesEntity> categoriesRes, Context context) {
        mContext = context;
        mCategoriesRes = categoriesRes;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_dashboard_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {

        CategoriesEntity categoriesRes = mCategoriesRes.get(position);
        holder.mDeviceCountTxt.setText(categoriesRes.getCount());
        holder.mDeviceNameTxt.setText(TextUtil.getInstance().capitalizeStr(categoriesRes.getName()));
        holder.mDeviceIssueCountTxt.setText(categoriesRes.getIssueCount());
//        holder.mDeviceIssueCountTxt.setVisibility(Integer.valueOf(categoriesRes.getIssueCount()) > 0 ? View.VISIBLE : View.INVISIBLE);
        holder.mDeviceImg.setImageResource(ImageUtil.getInstance().deviceImg(categoriesRes.getType()));

        /*Item onclick*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstants.IOT_DEVICE_CATEGORIES = mCategoriesRes.get(holder.getAdapterPosition());
                ((BaseActivity) mContext).nextScreen(DeviceDetailsList.class);

            }
        });

    }




    @Override
    public int getItemCount() {
        return mCategoriesRes.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_count_txt)
        TextView mDeviceCountTxt;

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.device_issue_count_txt)
        TextView mDeviceIssueCountTxt;

        @BindView(R.id.device_img)
        ImageView mDeviceImg;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
