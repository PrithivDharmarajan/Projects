package com.fautus.fautusapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.fragment.MomentDetailsFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DateUtil;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 21-Apr-17.
 */

public class MomentListAdapter extends RecyclerView.Adapter<MomentListAdapter.Holder> implements RecyclerView.OnItemTouchListener {
    private Context mContext;
    private ArrayList<MomentPhotoEntity> mMomentArrList;
    private SimpleDateFormat mServerDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
    private SimpleDateFormat mLocalDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public MomentListAdapter(Context context, ArrayList<MomentPhotoEntity> momentList) {
        this.mContext = context;
        this.mMomentArrList = momentList;
    }

    private MomentListAdapter(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_moment_details_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        MomentPhotoEntity momentRes = mMomentArrList.get(position);

        String photosCountStr = momentRes.getPhoto().size() + " " + (momentRes.getPhoto().size() > 1 ? mContext.getResources().getString(R.string.photos) : mContext.getResources().getString(R.string.photo));
        holder.mPhotoAddressTxt.setText(photosCountStr + " " + mContext.getResources().getString(R.string.hyphen_sym) + " " + momentRes.getMoment().getString(ParseAPIConstants.locationCity) + mContext.getResources().getString(R.string.comma_sym) + " " + momentRes.getMoment().getString(ParseAPIConstants.locationState));
        holder.mDateTxt.setText(DateUtil.getCustomDateFormat(DateUtil.getStringFromDate(momentRes.getMoment().getDate(ParseAPIConstants.momentDate), mServerDateFormat), mServerDateFormat, mLocalDateFormat));
        holder.mPhotoPurchaseTxt.setText(momentRes.getPurchasedPhotosCount() + " " + (momentRes.getPurchasedPhotosCount().equalsIgnoreCase(AppConstants.SUCCESS_CODE) || momentRes.getPurchasedPhotosCount().equalsIgnoreCase(AppConstants.FAILURE_CODE) ? mContext.getResources().getString(R.string.photo) : mContext.getResources().getString(R.string.photos)) + " " + mContext.getResources().getString(R.string.purchased));

        holder.mPhotoHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.mPhotoHorizontalRecyclerView.setAdapter(new MomentPhotoAdapter(mContext, momentRes.getPhoto()));
        holder.mPhotoHorizontalRecyclerView.setNestedScrollingEnabled(true);

        holder.mPhotoHorizontalRecyclerView.addOnItemTouchListener(
                new MomentListAdapter(mContext, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        momentListDetails(mContext, holder.getAdapterPosition());
                    }
                })
        );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                momentListDetails(mContext, holder.getAdapterPosition());
            }
        });

    }


    private void momentListDetails(final Context context, final int adapterPosInt) {
        if (NetworkUtil.isNetworkAvailable(context)) {
            DialogManager.getInstance().showProgress(context);
            ParseQuery<ParseObject> photoCountQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
            photoCountQuery.whereEqualTo(ParseAPIConstants.purchased, true);
            photoCountQuery.whereEqualTo(ParseAPIConstants.moment, mMomentArrList.get(adapterPosInt).getMoment());
            photoCountQuery.countInBackground(new CountCallback() {
                @Override
                public void done(final int count, ParseException e) {
                    DialogManager.getInstance().hideProgress();
                    if (e == null) {
                        AppConstants.MOMENT_PHOTO_ENTITY = new MomentPhotoEntity();
                        AppConstants.MOMENT_PHOTO_ENTITY.setPurchasedPhotosCount(count + "");
                        AppConstants.MOMENT_ALREADY_BOUGHT = (mMomentArrList.get(adapterPosInt).getPhoto().size() == count ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE);
                        AppConstants.MOMENT_PHOTO_ENTITY.setMoment(mMomentArrList.get(adapterPosInt).getMoment());
                        AppConstants.MOMENT_PHOTO_ENTITY.setPhoto(mMomentArrList.get(adapterPosInt).getPhoto());
                        AppConstants.MOMENT_FROM_LIST = AppConstants.SUCCESS_CODE;
                        ((HomeScreen) context).addFragment(new MomentDetailsFragment());
                    } else {
                        DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.app_name), e.getMessage(), new InterfaceBtnCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
                    }
                }
            });
        } else {
                    /*Alert message will be appeared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.app_name), context.getString(R.string.no_internet), new InterfaceBtnCallback() {
                @Override
                public void onOkClick() {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMomentArrList.size();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_address_txt)
        TextView mPhotoAddressTxt;

        @BindView(R.id.date_txt)
        TextView mDateTxt;

        @BindView(R.id.photo_purchase_txt)
        TextView mPhotoPurchaseTxt;

        @BindView(R.id.photo_horizontal_recycler_view)
        RecyclerView mPhotoHorizontalRecyclerView;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    private interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

}
