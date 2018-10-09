package com.smaat.ipharma.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.LocalTimeEntitiy;
import com.smaat.ipharma.entity.ShowReviewMessageEntity;
import com.smaat.ipharma.fragments.AddReminderScreen;
import com.smaat.ipharma.fragments.PillReminderListFragment;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Smaat on 1/30/2017.
 */

public class ReminderAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private Holder mHolder;
    private ArrayList<LocalTimeEntitiy> mLocalTimeList = new ArrayList<>();
    PillReminderListFragment pfgmt;

    public ReminderAdapter(FragmentActivity context,PillReminderListFragment pillfgmt, ArrayList<LocalTimeEntitiy> localTimeEntitiys) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(this.mContext);
        mLocalTimeList = localTimeEntitiys;
        pfgmt = pillfgmt;
    }

    @Override
    public int getCount() {
        return mLocalTimeList.size();
    }

    @Override
    public Object getItem(int i) {
        return mLocalTimeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    class Holder {

        @BindView(R.id.edit_btn)
        ImageView mEditBtn;

        @BindView(R.id.delete_btn)
        ImageView mDeleteBtn;

        @BindView(R.id.title_reminder)
        TextView mTitlereminder;
        @BindView(R.id.time_txt)
        TextView mTimetxt;


        Holder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        mHolder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.adapt_reminder, parent, false);
            mHolder = new Holder(view);
            view.setTag(mHolder);
        } else {
            mHolder = (Holder) view.getTag();
        }
        final LocalTimeEntitiy mLocalTimeEntitiy = mLocalTimeList.get(pos);
        ArrayList<String> mTotlatime = new ArrayList<>();
        if(mLocalTimeEntitiy.getMorningtime()!=null)
            mTotlatime.add(mLocalTimeEntitiy.getMorningtime());
        if(mLocalTimeEntitiy.getAfternoontime()!=null)
            mTotlatime.add(mLocalTimeEntitiy.getAfternoontime());
        if(mLocalTimeEntitiy.getEveningtime()!=null)
            mTotlatime.add(mLocalTimeEntitiy.getEveningtime());
        if(mLocalTimeEntitiy.getNighttime()!=null)
            mTotlatime.add(mLocalTimeEntitiy.getNighttime());


        mHolder.mTitlereminder.setText(mLocalTimeEntitiy.getTitle());
        mHolder.mTimetxt.setText(mTotlatime.toString().replace("[","").replace("]",""));

        mHolder.mEditBtn.setTag(pos);
        mHolder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                AppConstants.EDITLOCALTIMEENTITIY = mLocalTimeEntitiy;
                AppConstants.EDITPOS = pos;
                ((HomeScreen)mContext).pushFragment(new AddReminderScreen());
            }
        });
        mHolder.mDeleteBtn.setTag(pos);
        mHolder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                showDelConfirm(mContext,"",mContext.getString(R.string.del_confirmation),pos);


            }
        });

        return view;
    }



    public void showDelConfirm(final Context mContext,String title,String msg,final int position) {
        final Dialog mDialog = DialogManager.getDialog(mContext, R.layout.popup_msg_layout);

        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        TextView mTitte=(TextView)mDialog.findViewById(R.id.title_text);
        mTitte.setText(msg);

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                pfgmt.deleteReminder(position);
                mDialog.dismiss();
            }
        });

        m_btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();


            }
        });

        mDialog.show();
    }


}
