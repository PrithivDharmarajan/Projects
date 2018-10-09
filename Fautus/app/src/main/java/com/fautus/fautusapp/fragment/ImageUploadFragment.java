package com.fautus.fautusapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.ImageDataEntity;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fautus.fautusapp.utils.AppConstants.MOMENT_PHOTO_ENTITY;

/**
 * Created by sys on 11-May-17.
 */

public class ImageUploadFragment extends BaseFragment implements InterfaceBtnCallback {

    @BindView(R.id.img_recycler_view)
    RecyclerView mImgRecyclerView;

    ArrayList<ImageDataEntity> mThumbnailsArrBitmapList;
    ArrayList<String> mImgPathArrStrList;
    HashMap<String, Boolean> mImgPathHashMapList;
    private int mSelectedImgSizeInt = 0;
    private Timer mMomentCancelledTimer;
    private boolean isMomentCancelled = false;
    private Dialog mMomentCanCelDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_image_upload_screen, container, false);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header right img*/
        ((HomeScreen) getActivity()).setDrawerAction(false);
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moments));
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        isMomentCancelled = false;
        initView();
    }


    private void initView() {
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.moment) + " " + getResources().getString(R.string.colon_sym) + " " + MOMENT_PHOTO_ENTITY.getMoment().getString(ParseAPIConstants.adHocCode));


        ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMomentCancelled) {
                    ArrayList<PhotoEntity> photoArrList = new ArrayList<>();
                    DialogManager.getInstance().showProgress(getActivity());
                    for (String key : mImgPathHashMapList.keySet()) {
                        if (mImgPathHashMapList.get(key)) {
                            PhotoEntity photoEntity = new PhotoEntity();
                            photoEntity.setPhoto_path(key);
                            photoEntity.setPhoto_selected(AppConstants.SUCCESS_CODE);
                            photoEntity.setPhoto_purchased(AppConstants.FAILURE_CODE);
                            photoArrList.add(photoEntity);
                        }
                    }
                    DialogManager.getInstance().hideProgress();
                    if (photoArrList.size() > 0) {
                        AppConstants.PHOTO_NEW_UPLOAD = AppConstants.SUCCESS_CODE;
                        AppConstants.MOMENT_PHOTO_ENTITY.setPhoto(photoArrList);
                        AppConstants.MOMENT_PHOTO_ENTITY.setPhoto_uploaded(AppConstants.FAILURE_CODE);
                        ((HomeScreen) getActivity()).addFragment(new PhotoMomentUploadFragment());
                    } else {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.app_name), getResources().getString(R.string.select_one_photo), ImageUploadFragment.this);
                    }
                }
            }
        });

        ((HomeScreen) getActivity()).mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.PHOTO_NEW_UPLOAD = AppConstants.FAILURE_CODE;
                getActivity().onBackPressed();
            }
        });

        DialogManager.getInstance().showProgress(getActivity());
        getLocalImagesFromGallery localImagesFromGallery = new getLocalImagesFromGallery();
        localImagesFromGallery.execute();
    }


    private class getLocalImagesFromGallery extends AsyncTask<String, Void, ArrayList<ImageDataEntity>> {

        @Override
        protected ArrayList<ImageDataEntity> doInBackground(String... params) {
            //ArrayList<String> contactList = new ArrayList<>();
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;
            Cursor imgCursor = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);
            if (getActivity() != null && imgCursor != null) {

                int image_column_index = imgCursor.getColumnIndex(MediaStore.Images.Media._ID);

                ImageDataEntity mData;
                mThumbnailsArrBitmapList = new ArrayList<>();
                mImgPathArrStrList = new ArrayList<>();
                mImgPathHashMapList = new HashMap<>();
                for (int i = 0; i < imgCursor.getCount(); i++) {
                    if (getActivity() != null && getActivity().getContentResolver() != null) {
                        mData = new ImageDataEntity();
                        imgCursor.moveToPosition(i);
                        int id = imgCursor.getInt(image_column_index);
                        int dataColumnIndex = imgCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        mData.setmThumbImageList(MediaStore.Images.Thumbnails.getThumbnail(
                                getActivity().getContentResolver(), id,
                                MediaStore.Images.Thumbnails.MICRO_KIND, null));
                        mData.setmSelCheckbox(AppConstants.FAILURE_CODE);
                        mImgPathArrStrList.add(imgCursor.getString(dataColumnIndex));
                        mImgPathHashMapList.put(imgCursor.getString(dataColumnIndex), false);
                        mThumbnailsArrBitmapList.add(mData);
                    }
                }
                imgCursor.close();
            }
            return mThumbnailsArrBitmapList;
        }

        @Override
        protected void onPostExecute(ArrayList<ImageDataEntity> result) {
            final UploadPhotoAdapter uploadPhotoAdapter = new UploadPhotoAdapter(getActivity(), result);
            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            mImgRecyclerView.setLayoutManager(layoutManager);
            mImgRecyclerView.setAdapter(uploadPhotoAdapter);

            checkMomentCancelledByUser();
            DialogManager.getInstance().hideProgress();
        }

    }


    class UploadPhotoAdapter extends RecyclerView.Adapter<UploadPhotoAdapter.Holder> {

        private Context mContext;
        private ArrayList<ImageDataEntity> mThumbnailsArrBitmapList;

        private UploadPhotoAdapter(Context context, ArrayList<ImageDataEntity> thumbnailsArrBitmapList) {
            mThumbnailsArrBitmapList = thumbnailsArrBitmapList;
            mContext = context;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_image_upload_view, parent, false);
            return new Holder(rootView);
        }

        @Override
        public void onBindViewHolder(final Holder holder, final int position) {

            final ImageDataEntity mImgUploadDataEntity = mThumbnailsArrBitmapList.get(position);
            holder.mThumbImg.setImageBitmap(mImgUploadDataEntity.getmThumbImageList());
            holder.mSelectedImg.setAlpha(mImgUploadDataEntity.getmSelCheckbox().equalsIgnoreCase(AppConstants.FAILURE_CODE) ? 0.2f : 1.0f);
            holder.mSelectedImgLay.setTag(mThumbnailsArrBitmapList.get(position).getmSelCheckbox());
            holder.mSelectedImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View selectedImgLay) {

                    String imgTagStatusStr = (String) selectedImgLay.getTag();
                    if ((mSelectedImgSizeInt < 9 && imgTagStatusStr.equalsIgnoreCase(AppConstants.FAILURE_CODE)) || imgTagStatusStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        holder.mSelectedImg.setAlpha(imgTagStatusStr.equalsIgnoreCase(AppConstants.FAILURE_CODE) ? 1.0f : 0.2f);
                        mImgPathHashMapList.put(mImgPathArrStrList.get(holder.getAdapterPosition()), imgTagStatusStr.equalsIgnoreCase(AppConstants.FAILURE_CODE));
                        mSelectedImgSizeInt = imgTagStatusStr.equalsIgnoreCase(AppConstants.FAILURE_CODE) ? mSelectedImgSizeInt + 1 : mSelectedImgSizeInt - 1;
                        mImgUploadDataEntity.setmSelCheckbox(imgTagStatusStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? AppConstants.FAILURE_CODE : AppConstants.SUCCESS_CODE);
                        ((HomeScreen) getActivity()).setHeaderRightText(mSelectedImgSizeInt == 0 ? getActivity().getString(R.string.done) : getActivity().getString(R.string.done) + "(" + mSelectedImgSizeInt + ")");
                        notifyDataSetChanged();
                    } else {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.app_name), getResources().getString(R.string.select_only_photo), ImageUploadFragment.this);
                    }


                }
            });

        }

        @Override
        public int getItemCount() {
            return mThumbnailsArrBitmapList.size();
        }

        class Holder extends RecyclerView.ViewHolder {

            @BindView(R.id.thumb_img)
            ImageView mThumbImg;

            @BindView(R.id.selected_img)
            ImageView mSelectedImg;

            @BindView(R.id.selected_img_lay)
            RelativeLayout mSelectedImgLay;

            Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }

    /* To check if the moment is cancelled or not */
    private void checkMomentCancelledByUser() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cancelCheckMomentCancelledByUserTimer();
                    mMomentCancelledTimer = new Timer();
                    mMomentCancelledTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (getActivity() != null && NetworkUtil.isNetworkAvailable(getActivity())) {
                                AppConstants.MOMENT_PHOTO_ENTITY.getMoment().fetchInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (object != null && object.get(ParseAPIConstants.enabled) != null && !object.getBoolean(ParseAPIConstants.enabled) && !isMomentCancelled) {
                                            isMomentCancelled = true;
                                            cancelCheckMomentCancelledByUserTimer();
                                            baseFragmentAlertDismiss(mMomentCanCelDialog);
                                            mMomentCanCelDialog = DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.moment_cancelled_topic), getResources().getString(R.string.moment_cancelled_msg), new InterfaceBtnCallback() {
                                                @Override
                                                public void onOkClick() {
                                                    if (getActivity() != null) {
                                                        DialogManager.getInstance().showProgress(getActivity());
                                                        disconnectChat();
                                                    }
                                                }
                                            });

                                        }
                                        if (isMomentCancelled) {
                                            cancelCheckMomentCancelledByUserTimer();
                                        }
                                    }
                                });
                            } else {
                                baseFragmentAlertDismiss(mMomentCanCelDialog);
                                cancelCheckMomentCancelledByUserTimer();
                            }

                        }
                    }, 3000, 3000);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isMomentCancelled = true;
        cancelCheckMomentCancelledByUserTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        isMomentCancelled = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelCheckMomentCancelledByUserTimer();
        baseFragmentAlertDismiss(mMomentCanCelDialog);
    }


    /*Cancelling the check moment cancelled timer */
    private void cancelCheckMomentCancelledByUserTimer() {
        if (mMomentCancelledTimer != null) {
            mMomentCancelledTimer.cancel();
            mMomentCancelledTimer.purge();
        }
    }

    /* Disconnect SendBird Chat SDK */
    private void disconnectChat() {
        if (AppConstants.SEND_BIRD_GROUP_CHANNEL != null) {
            AppConstants.SEND_BIRD_GROUP_CHANNEL.leave(new GroupChannel.GroupChannelLeaveHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    SendBird.disconnect(new SendBird.DisconnectHandler() {
                        @Override
                        public void onDisconnected() {
                            moveToHomeScreen();
                        }
                    });
                }
            });
        } else {
            moveToHomeScreen();
        }
    }

    private void moveToHomeScreen() {
        if (getActivity() != null) {
            final ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
            photographerQuery.whereEqualTo(ParseAPIConstants.user, ParseUser.getCurrentUser());
            photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (getActivity() != null) {
                        if (e == null && object != null) {
                            object.put(ParseAPIConstants.isAvailable, AppConstants.PHOTOGRAPHER_AVA);
                            object.saveEventually();
                        }
                        DialogManager.getInstance().hideProgress();
                        ((HomeScreen) getActivity()).addFragment(new PhotoHomeFragment());

                    }
                }
            });
        }
    }
}


