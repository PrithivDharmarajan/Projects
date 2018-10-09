package com.smaat.ipharma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.database.DatabaseUtil;
import com.smaat.ipharma.db.model.PillTimerResponse;
import com.smaat.ipharma.fragment.AddNewReminderFragment;
import com.smaat.ipharma.ui.HomeScreen;

import java.util.ArrayList;

import static com.smaat.ipharma.fragment.PillReminderFragment.mPillResDBRes;


/**
 * Created by admin on 9/19/2016.
 */

public class PillReminderAdapter extends RecyclerView.Adapter<PillReminderAdapter.Holder> {

    private Context mContext;
    private ArrayList<PillTimerResponse> mPillTimerRes;
    private DatabaseUtil db = new DatabaseUtil();

    public PillReminderAdapter(Context context, ArrayList<PillTimerResponse> mPillrRes) {
        this.mContext = context;
        this.mPillTimerRes = mPillrRes;

    }

    @Override
    public PillReminderAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pil_reminder_adapter, parent,
                false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView mTabletNameTxt, mTabletDateTxt, mTabletTimeTxt;
        Button mEditBtn, mDeleteBtn;

        public Holder(View view) {
            super(view);
            mTabletNameTxt = (TextView) view.findViewById(R.id.tablet_name_txt);
            mTabletDateTxt = (TextView) view.findViewById(R.id.tablet_date_txt);
            mTabletTimeTxt = (TextView) view.findViewById(R.id.tablet_time_txt);

            mEditBtn = (Button) view.findViewById(R.id.edit_btn);
            mDeleteBtn = (Button) view.findViewById(R.id.delete_btn);
        }
    }

    @Override
    public void onBindViewHolder(PillReminderAdapter.Holder holder, final int position) {
        holder.mTabletNameTxt.setText(mPillTimerRes.get(position).getTablet_name());
        holder.mTabletDateTxt.setText(mPillTimerRes.get(position).getDuration_type());
        holder.mTabletTimeTxt.setText(mPillTimerRes.get(position).getDuration_time());


        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewReminderFragment.mReminderID = mPillTimerRes.get(position).getId();
                HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                HomeScreen.mHeaderLeft
                        .setBackgroundResource(R.drawable.back_butto);
                ((HomeScreen) mContext)
                        .replaceFragment(new AddNewReminderFragment(), true);


            }
        });

        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deletePillReminderData(mContext, mPillTimerRes.get(position).getId());
                mPillTimerRes.remove(position);
                notifyItemRemoved(position);
                mPillResDBRes.clear();
                mPillResDBRes.addAll(mPillTimerRes);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPillTimerRes.size();
    }
}
