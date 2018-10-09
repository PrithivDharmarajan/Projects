package com.smaat.renterblock.findagent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.model.FriendsRecentsResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.BaseActivity.GsonTransformer;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FindAgentAdapter extends BaseAdapter implements DialogMangerCallback {

    private Context mContext;
    private ArrayList<AgentFilterResultEntity> mAgentFilterResult;
    private String mFriendUserID, UserID;
    Holder holder = null;
    AQuery aq1;

    public FindAgentAdapter(Context context, ArrayList<AgentFilterResultEntity> mResult) {
        mContext = context;
        mAgentFilterResult = mResult;
        UserID = GlobalMethods.getUserID(mContext);
    }

    class Holder {

        private TextView mUserName, mAgentWith, mInPlace, mSoldCount, mSoldTxt, mActiveListCount, mActiveListTxt,
                mPhotosCount, mReviewsCount, mFriendsCount, mSponsorTxt;
        private Button mFriendRequest, message_icon, chat_icon;
        private LinearLayout mSponsorLay;
        private RatingBar mUserRatingBar;
        private ImageView mUser_images;
        private RelativeLayout find_an_agent_lay;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        holder = new Holder();

        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mLayoutInflater.inflate(R.layout.adapter_find_agent, null);
        ViewGroup root = (ViewGroup) convertView.findViewById(R.id.parent_layout);
        Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(mContext);
        Typeface mTypefaceBold = TypefaceSingleton.getInstance().getHelveticaBold(mContext);
        ((BaseActivity) mContext).setFont(root, mTypefaceBold);

        if (mAgentFilterResult.size() != 0) {
            AgentFilterResultEntity mAgentResultEntity = mAgentFilterResult.get(position);
            aq1 = new AQuery(mContext).recycle(convertView);

            final String UserID = GlobalMethods.getUserID(mContext);

            holder.mUserName = (TextView) convertView.findViewById(R.id.user_name_txt);
            holder.mAgentWith = (TextView) convertView.findViewById(R.id.agent_with_txt);
            holder.mInPlace = (TextView) convertView.findViewById(R.id.in_place_txt);
            holder.mSoldCount = (TextView) convertView.findViewById(R.id.sold_homes_count_txt);
            holder.mSoldTxt = (TextView) convertView.findViewById(R.id.sold_homes_txt);
            holder.mActiveListCount = (TextView) convertView.findViewById(R.id.active_listing_count_txt);
            holder.mActiveListTxt = (TextView) convertView.findViewById(R.id.active_listing_txt);

            holder.mFriendsCount = (TextView) convertView.findViewById(R.id.friends_count);
            holder.mPhotosCount = (TextView) convertView.findViewById(R.id.photos_count);
            holder.mReviewsCount = (TextView) convertView.findViewById(R.id.reviews_count);
            holder.mSponsorTxt = (TextView) convertView.findViewById(R.id.sponsor_txt);

            holder.mSponsorLay = (LinearLayout) convertView.findViewById(R.id.sponsor_lay);

            holder.mUserRatingBar = (RatingBar) convertView.findViewById(R.id.user_ratingbar);

            holder.mFriendRequest = (Button) convertView.findViewById(R.id.friend_request_btn);
            holder.mFriendRequest.setTag(position);

            holder.mUser_images = (ImageView) convertView.findViewById(R.id.user_image);

            holder.mUser_images.setTag(position);

            holder.message_icon = (Button) convertView.findViewById(R.id.message_icon);

            holder.message_icon.setTag(position);

            holder.chat_icon = (Button) convertView.findViewById(R.id.chat_icon);
            holder.chat_icon.setTag(position);

            if (mAgentFilterResult.get(position).getRb_user().equalsIgnoreCase("1")) {
                holder.chat_icon.setVisibility(View.VISIBLE);
                holder.message_icon.setVisibility(View.GONE);
                // holder.message_icon.setBackgroundResource(R.drawable.chat_icon);
            } else {
                holder.chat_icon.setVisibility(View.GONE);
                holder.message_icon.setVisibility(View.VISIBLE);
                // holder.message_icon
                // .setBackgroundResource(R.drawable.message_icon);
            }

            holder.find_an_agent_lay = (RelativeLayout) convertView.findViewById(R.id.find_an_agent_lay);

            holder.find_an_agent_lay.setTag(position);

            holder.find_an_agent_lay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    Intent intent = new Intent(mContext, FindAgentDetailsActivity.class);
                    intent.putExtra("mUserID", mAgentFilterResult.get(pos).getUser_id());
                    intent.putExtra("mEnhanced_profile", mAgentFilterResult.get(pos).getEnhanced_profile());
                    intent.putExtra("is_friend", mAgentFilterResult.get(pos).getIs_friend());
                    intent.putExtra("mfr_Username", mAgentFilterResult.get(pos).getUser_name());
                    intent.putExtra("rb_user", mAgentFilterResult.get(pos).getRb_user());
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            holder.chat_icon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    callGroupIdService(mAgentFilterResult.get(pos).getUser_id(), UserID, pos);
                }
            });

            holder.message_icon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    FindAgentActivity.showMessageMailer(mAgentFilterResult.get(pos).getEmail_id());
                }
            });

            holder.mUser_images.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // DialogManager.showToast(mContext, "clicked");
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    Intent profile = new Intent(mContext, ProfileScreen.class);
                    profile.putExtra("user_id", mAgentFilterResult.get(pos).getUser_id());
                    profile.putExtra("call_from", "Agent");
                    mContext.startActivity(profile);
                }
            });
            if (mAgentResultEntity.getRb_user().equalsIgnoreCase("1")) {
                holder.mUserName.setText(mAgentResultEntity.getUser_name());
            } else {
                holder.mUserName.setText(mAgentResultEntity.getFirst_name() + " " + mAgentResultEntity.getLast_name());
            }
            holder.mInPlace.setText("In " + mAgentResultEntity.getCity());
            holder.mSoldCount.setText(mAgentResultEntity.getSoldhomes());
            holder.mActiveListCount.setText(mAgentResultEntity.getActivelisting());
            holder.mAgentWith
                    .setText(mAgentResultEntity.getUser_type() + " with " + mAgentResultEntity.getBusiness_name());
            holder.mFriendsCount.setText(mAgentResultEntity.getFriends_count());
            holder.mPhotosCount.setText(mAgentResultEntity.getPhotos_count());
            holder.mReviewsCount.setText(mAgentResultEntity.getReviews_count());

            if (mAgentResultEntity.getIs_friend().equals("0") && mAgentResultEntity.getRb_user().equals("1")) {
                holder.mFriendRequest.setVisibility(View.VISIBLE);
            } else {
                holder.mFriendRequest.setVisibility(View.INVISIBLE);
            }
            if (mAgentResultEntity.getEnhanced_profile().equals("0")) {
                holder.mSponsorLay.setVisibility(View.INVISIBLE);
            } else {
                holder.mSponsorLay.setVisibility(View.VISIBLE);
            }

            aq1.id(R.id.user_image).image(mAgentResultEntity.getUser_pic(), false, false, 200, R.drawable.profile_pic,
                    null, 0);

            float rating = Float.parseFloat(mAgentResultEntity.getUser_avg_rating());
            // mFriendUserID = mAgentResultEntity.getUser_id();
            holder.mUserRatingBar.setRating(rating);
            holder.mFriendRequest.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    holder.mFriendRequest.setVisibility(View.INVISIBLE);
                    sendFriendRequest(mAgentFilterResult.get(pos).getUser_id().toString());

                }
            });
            holder.mSponsorTxt.setTypeface(mTypeface);
            holder.mAgentWith.setTypeface(mTypeface);
            holder.mSoldTxt.setTypeface(mTypeface);
            holder.mActiveListTxt.setTypeface(mTypeface);
            holder.mFriendsCount.setTypeface(mTypeface);
            holder.mPhotosCount.setTypeface(mTypeface);
            holder.mReviewsCount.setTypeface(mTypeface);
        }
        return convertView;
    }

    private void callGroupIdService(String Friend_UserID, String user_id, final int pos) {
        String Url = AppConstants.BASE_URL + "group";
        GsonTransformer t = new GsonTransformer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("owner_id", user_id);
        params.put("users_id", Friend_UserID);
        params.put("name", "");
        aq1.transformer(t).progress(DialogManager.getProgressDialog(mContext)).ajax(Url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {

                        if (json != null) {
                            ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
                            // onSuccessRequest(obj);
                            callChatService(obj.result, pos);
                        } else {
                            // statusErrorCode(status);
                            DialogManager.showCustomAlertDialog(mContext, FindAgentAdapter.this,
                                    mContext.getString(R.string.server_unreachable));
                        }
                    }
                });
    }

    private void callChatService(String group_id, int pos) {
        Intent intent = new Intent(mContext, FriendChatScreen.class);
        intent.putExtra("ids", mAgentFilterResult.get(pos).getUser_id());
        intent.putExtra("names", mAgentFilterResult.get(pos).getUser_name());
        intent.putExtra("groupId", group_id);
        intent.putExtra("type", "group");
        intent.putExtra("enhanced_profile_ids", mAgentFilterResult.get(pos).getEnhanced_profile());
        if (mAgentFilterResult.get(pos).getIs_friend().equalsIgnoreCase("1")) {
            intent.putExtra("from_call", "");
        } else {
            intent.putExtra("from_call", "hotleads");
        }
        mContext.startActivity(intent);
    }

    private void sendFriendRequest(String friend_id) {
        String Url = AppConstants.BASE_URL + "friend/sendfrindrequest";
        GsonTransformer t = new GsonTransformer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", UserID);
        params.put("friend_user_id", friend_id);
        ((BaseActivity) mContext).aq().transformer(t).progress(DialogManager.getProgressDialog(mContext)).ajax(Url,
                params, JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {
                        if (json != null) {
                            FriendsRecentsResponse mResponse = new Gson().fromJson(json.toString(),
                                    FriendsRecentsResponse.class);

                            DialogManager.showCustomAlertDialog(mContext, FindAgentAdapter.this, mResponse.getMsg());
                        } else {
                            ((BaseActivity) mContext).statusErrorCode(status);
                        }
                    }

                });
    }

    @Override
    public int getCount() {
        return mAgentFilterResult.size();
    }

    @Override
    public Object getItem(int pos) {
        return mAgentFilterResult.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public void onItemclick(String SelctedItem, int pos) {

    }

    @Override
    public void onOkclick() {
        holder.mFriendRequest.setVisibility(View.INVISIBLE);

    }
}
