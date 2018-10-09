package com.fautus.fautusapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.model.CardDeatilsResponse;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 26-May-17.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.Holder> {

    private Context mContext;
    private ArrayList<CardDeatilsResponse> mCardListRes;
    private String mDefaultCardStr,mStripeCusIdStr;
    private BaseFragment mBaseFragment;


    public CardListAdapter(Context context, ArrayList<CardDeatilsResponse> cardEntityListRes, String defaultCardStr, String stripeCusIdStr, BaseFragment currentFrag) {
        mContext = context;
        mCardListRes = cardEntityListRes;
        mDefaultCardStr = defaultCardStr;
        mStripeCusIdStr = stripeCusIdStr;
        mBaseFragment=currentFrag;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_card_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        holder.mCardNumTxt.setText(mCardListRes.get(position).getBrand() + " " + mContext.getString(R.string.ending_in) + " " + mCardListRes.get(position).getLast4());
        holder.mSelectedImgLay.setVisibility(mDefaultCardStr.equalsIgnoreCase(mCardListRes.get(position).getId()) ? View.VISIBLE : View.INVISIBLE);

        /*Make a default card selection*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCardDetails(mContext, mCardListRes.get(holder.getAdapterPosition()).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCardListRes.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_num_txt)
        TextView mCardNumTxt;

        @BindView(R.id.selected_img_lay)
        RelativeLayout mSelectedImgLay;

        Holder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

    private void saveCardDetails(final Context context, String tokenIDStr) {
        DialogManager.getInstance().showProgress(context);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put(ParseAPIConstants.source, tokenIDStr);

        ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_saveStripeCustomerDefaultPayment, params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                DialogManager.getInstance().hideProgress();
                if (e != null) {
                    try {
                        JSONObject errorJsonObj = new JSONObject(e.getMessage());
                        String messageStr = errorJsonObj.getString(context.getString(R.string.message));
                        DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), new InterfaceBtnCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });

                    } catch (Exception ex) {
                        DialogManager.getInstance().showAlertPopup(context, context.getString(R.string.app_name), e.getMessage(), new InterfaceBtnCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                } else {
                    APIRequestHandler.getInstance().stripCardAPICall(mStripeCusIdStr, mBaseFragment);
                }
            }
        });

    }
}

