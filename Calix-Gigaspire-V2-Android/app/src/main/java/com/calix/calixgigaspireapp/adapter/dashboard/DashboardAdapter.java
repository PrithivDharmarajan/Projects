package com.calix.calixgigaspireapp.adapter.dashboard;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.CategoryEntity;
import com.calix.calixgigaspireapp.ui.devices.Devices;
import com.calix.calixgigaspireapp.ui.devices.DevicesList;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.CircularLayout;
import com.calix.calixgigaspireapp.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends PagerAdapter {

    private ArrayList<List<CategoryEntity>> mCategoriesArrList;
    private Context mContext;
    private int mTotalDevicesCountInt;

    public DashboardAdapter(Context context, ArrayList<List<CategoryEntity>> categoriesArrList, int totalDevicesCountInt) {
        this.mContext = context;
        this.mCategoriesArrList = categoriesArrList;
        this.mTotalDevicesCountInt = totalDevicesCountInt;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {
    Log.d("mycheccccc",""+ position);
        View dashBoardCircularView = LayoutInflater.from(collection.getContext()).inflate(R.layout.adapter_dashboard_view, collection, false);
        CircularLayout circularLayout = dashBoardCircularView.findViewById(R.id.circular_lay);
        ImageView deviceHexagonalImg = dashBoardCircularView.findViewById(R.id.device_hexagonal_img);
        TextView deviceCountTxt = dashBoardCircularView.findViewById(R.id.device_count_txt);

        if (mTotalDevicesCountInt > 0) {
            deviceHexagonalImg.setColorFilter(mContext.getResources().getColor(R.color.sky_blue));
            deviceCountTxt.setVisibility(View.VISIBLE);
            deviceCountTxt.setText(String.valueOf(mTotalDevicesCountInt));

            deviceHexagonalImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).nextScreen(DevicesList.class);
                }
            });
        } else {
            deviceCountTxt.setVisibility(View.GONE);
        }

        circularLayout.setCapacity(8);

        for (int childViewPosInt = 0; childViewPosInt < mCategoriesArrList.get(position).size(); childViewPosInt++) {

            View dashBoardChildView = LayoutInflater.from(collection.getContext()).inflate(R.layout.adapter_dashboard_child_view, collection, false);
            ImageView deviceChildHexagonalImg = dashBoardChildView.findViewById(R.id.device_child_hexagonal_img);
            ImageView deviceChildImg = dashBoardChildView.findViewById(R.id.device_child_img);
            TextView deviceChildNameTxt = dashBoardChildView.findViewById(R.id.device_child_name_txt);

            deviceChildImg.setBackground(mContext.getResources().getDrawable(deviceListImg(mCategoriesArrList.get(position).get(childViewPosInt).getType())));
            deviceChildNameTxt.setText(TextUtil.getInstance().capitalizeStr(mCategoriesArrList.get(position).get(childViewPosInt).getName()));

            TextView deviceCountTxtView = dashBoardChildView.findViewById(R.id.device_count_txt);
            if (mCategoriesArrList.get(position).get(childViewPosInt).getCount() > 0) {

                deviceChildHexagonalImg.setColorFilter(mContext.getResources().getColor(R.color.sky_blue));
                deviceCountTxtView.setText(String.valueOf(mCategoriesArrList.get(position).get(childViewPosInt).getCount()));

                final int fixedChildViewPosInt = childViewPosInt;
                deviceChildHexagonalImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppConstants.CATEGORY_ENTITY = mCategoriesArrList.get(position).get(fixedChildViewPosInt);
                        ((BaseActivity) mContext).nextScreen(Devices.class);
                    }
                });
            } else {
                //Hide Device count if 0
                dashBoardChildView.setVisibility(View.INVISIBLE);
                deviceCountTxtView.setVisibility(View.INVISIBLE);
            }

            circularLayout.addView(dashBoardChildView);
        }

        collection.addView(dashBoardCircularView, 0);
        return dashBoardCircularView;
    }

    /*find the IOT device Image*/
    private int deviceListImg(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 1:
                return R.drawable.ic_phone;
            case 2:
                return R.drawable.ic_computer;
            case 3:
                return R.drawable.ic_console;
            case 4:
                return R.drawable.ic_storage;
            case 5:
                return R.drawable.ic_printer;
            case 6:
                return R.drawable.ic_television;
            case 7:
                return R.drawable.ic_iot_device;
            case 8:
                return R.drawable.ic_camera;
            default:
                return R.mipmap.push_launcher;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public int getCount() {
        return mCategoriesArrList.size();
    }
}