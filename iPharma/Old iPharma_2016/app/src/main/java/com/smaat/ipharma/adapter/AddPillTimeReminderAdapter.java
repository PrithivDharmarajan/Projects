package com.smaat.ipharma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.ipharma.R;

import java.util.ArrayList;

import static com.smaat.ipharma.fragment.AddNewReminderFragment.mLocTimeList;

/**
 * Created by admin on 9/10/2016.
 */

public class AddPillTimeReminderAdapter extends RecyclerView.Adapter<AddPillTimeReminderAdapter
        .Holder> {
    private ArrayList<String> mTimeList;
    private Context mContext;

    public AddPillTimeReminderAdapter(Context context, ArrayList<String> mList) {
        this.mTimeList = mList;
        this.mContext = context;
    }


    public class Holder extends RecyclerView.ViewHolder {

        TextView mTimeTxt;
        Button mDeleteBtn;

        public Holder(View view) {
            super(view);
            mTimeTxt = (TextView) view.findViewById(R.id.time_txt);
            mDeleteBtn = (Button) view.findViewById(R.id.delete_btn);
        }
    }

    @Override
    public AddPillTimeReminderAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.add_time_adapter,
                parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(AddPillTimeReminderAdapter.Holder holder, final int position) {
        holder.mTimeTxt.setText(mTimeList.get(position));
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeList.remove(position);
                notifyItemRemoved(position);
                mLocTimeList.clear();
                mLocTimeList.addAll(mTimeList);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeList.size();
    }


}
