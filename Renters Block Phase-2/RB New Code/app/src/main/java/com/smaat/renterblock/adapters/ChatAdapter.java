package com.smaat.renterblock.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.ChatEntity;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DateUtil;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 10/26/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private Context mContext;
    private ArrayList<ChatEntity> mChatList = new ArrayList<>();
    private String mUserID, mFileType;

    public ChatAdapter(Context context, ArrayList<ChatEntity> mChatList) {
        this.mChatList = mChatList;
        mContext = context;
        mUserID = PreferenceUtil.getUserID(mContext);
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_view_row, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        ChatEntity chatEntity = mChatList.get(position);

        boolean isUserMsgBool = chatEntity.getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(mContext));


        //sender data
        if (isUserMsgBool) {
            holder.mSenderNameTxt.setText(chatEntity.getUser_name());

            if (chatEntity.getUserimage().isEmpty()) {
                holder.mSenderImg.setImageResource(R.drawable.default_profile_icon);
            } else {
                try {
                    Glide.with(mContext)
                            .load(chatEntity.getUserimage()).error(R.drawable.default_profile_icon)
                            .into(holder.mSenderImg);
                } catch (Exception ex) {
                    holder.mSenderImg.setImageResource(R.drawable.default_profile_icon);
                    Log.e(AppConstants.TAG, ex.getMessage());
                }
            }


            if (chatEntity.getFile_type().equalsIgnoreCase("image")
                    || chatEntity.getFile_type().equalsIgnoreCase("video")
                    || chatEntity.getFile_type().equalsIgnoreCase("audio")) {

                holder.mSenderFileUserNameTxt.setText(chatEntity.getUser_name());
                holder.mSenderDataLayout.setVisibility(View.VISIBLE);
                holder.mReceiverDataLayout.setVisibility(View.GONE);
                holder.mSenderLayout.setVisibility(View.GONE);
                holder.mReceiverLayout.setVisibility(View.GONE);

                try {
                    holder.mSenderFileTimeTxt.setText(DateUtil.timeDiff(chatEntity.getSend_time()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                displaySenderFile(holder.getAdapterPosition(), holder.mSenderDownloadImg, holder.mSenderPhotoImg
                        , holder.mSenderProgressBar, chatEntity.getFile_type(), chatEntity.getFile(), holder.mSenderDataLayout);


            } else {

                holder.mSenderLayout.setVisibility(View.VISIBLE);
                holder.mSenderDataLayout.setVisibility(View.GONE);
                holder.mReceiverDataLayout.setVisibility(View.GONE);
                holder.mReceiverLayout.setVisibility(View.GONE);

                holder.mSenderMsgTxt.setText(chatEntity.getMessage());
                try {
                    holder.mSenderTimeTxt.setText(DateUtil.timeDiff(chatEntity.getSend_time()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } else {
            /*receiver msg*/
            holder.mReceiverNameTxt.setText(chatEntity.getUsername());


            if (chatEntity.getUserimage().isEmpty()) {
                holder.mReceiverImg.setImageResource(R.drawable.default_profile_icon);
            } else {
                try {
                    Glide.with(mContext)
                            .load(chatEntity.getUserimage()).error(R.drawable.default_profile_icon)
                            .into(holder.mReceiverImg);
                } catch (Exception ex) {
                    holder.mReceiverImg.setImageResource(R.drawable.default_profile_icon);
                    Log.e(AppConstants.TAG, ex.getMessage());
                }
            }


            if (chatEntity.getFile_type().equalsIgnoreCase("image")
                    || chatEntity.getFile_type().equalsIgnoreCase("video")
                    || chatEntity.getFile_type().equalsIgnoreCase("audio")) {
                holder.mReceiverFileUserNameTxt.setText(chatEntity.getUsername());

                holder.mReceiverDataLayout.setVisibility(View.VISIBLE);
                holder.mSenderDataLayout.setVisibility(View.GONE);
                holder.mSenderLayout.setVisibility(View.GONE);
                holder.mReceiverLayout.setVisibility(View.GONE);
                try {
                    holder.mReceiverFileTimeTxt.setText(DateUtil.timeDiff(chatEntity.getSend_time()));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                displayReceiverFile(holder.getAdapterPosition(),
                        holder.mReceiverDownloadImg, holder.mReceiverPhotoImg,
                        holder.mReceiverProgressBar, chatEntity.getFile(), chatEntity.getFile_type(), holder.mReceiverDataLayout);


            } else {
                holder.mReceiverLayout.setVisibility(View.VISIBLE);
                holder.mReceiverDataLayout.setVisibility(View.GONE);
                holder.mSenderDataLayout.setVisibility(View.GONE);
                holder.mSenderLayout.setVisibility(View.GONE);

                holder.mReceiverMsgTxt.setText(chatEntity.getMessage());

                try {
                    holder.mReceiverTimeTxt.setText(DateUtil.timeDiff(chatEntity.getSend_time()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        String mFileName;
        int position;
        ProgressBar mProgressBar;
        ImageView mDownLoadImg;

        public DownloadFileFromURL(String file, int adapterPosition, ProgressBar progressBar, ImageView downloadImg) {
            mFileName = file;
            position = adapterPosition;
            mProgressBar = progressBar;
            mDownLoadImg = downloadImg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... file_url) {
            int count;
            // Output stream
            OutputStream output = null;
            try {
                URL url = new URL(file_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                String extStorageDirectory = Environment
                        .getExternalStorageDirectory().toString();
                String filePath = null;
                File file = null;

                if (mFileType != null
                        && (!mFileType.equalsIgnoreCase("sent_image")
                        && !mFileType.equalsIgnoreCase("sent_audio") && !mFileType
                        .equalsIgnoreCase("sent_video"))) {
                    if (mFileType != null && mFileType.equalsIgnoreCase("image")) {
                        filePath = File.separator + "RB_Files" + File.separator + "RB_Images";
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filePath);
                        File newfname = new File(file_url[0]);
                        output = new FileOutputStream(file + "/" + newfname.getName());
                    } else if (mFileType != null && mFileType.equalsIgnoreCase("audio")) {
                        filePath = File.separator + "RB_Files" + File.separator + "RB_Audios";
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filePath);
                        File newfname = new File(file_url[0]);
                        output = new FileOutputStream(file + "/" + newfname.getName());

                    } else if (mFileType != null && mFileType.equalsIgnoreCase("video")) {
                        filePath = mFileName;

                        filePath = File.separator + "RB_Files" + File.separator + "RB_Videos";
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filePath);
                        File newfname = new File(file_url[0]);
                        output = new FileOutputStream(file + "/" + newfname.getName());

//                        file = new File(extStorageDirectory + File.separator
//                                + "RB_Files" + File.separator + "RB_Videos");
                    }
                } else {

                    if (mFileType != null && mFileType.equalsIgnoreCase("sent_image")) {
                        filePath = File.separator + "RB_Sent_files" + File.separator + "Sent_images";
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filePath);
                        File newfname = new File(file_url[0]);
                        output = new FileOutputStream(file + "/" + newfname.getName());
                    } else if (mFileType != null
                            && mFileType.equalsIgnoreCase("sent_audio")) {
                        filePath = File.separator + "RB_Sent_files" + File.separator + "Sent_Sounds";
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + filePath);
                        File newfname = new File(file_url[0]);
                        output = new FileOutputStream(file + "/" + newfname.getName());

                    } else if (mFileType != null
                            && mFileType.equalsIgnoreCase("sent_video")) {
                        filePath = mFileName;
                        file = new File(extStorageDirectory + File.separator
                                + "RB_Sent_files" + File.separator
                                + "Sent_videos");
                    }
                }
                if (!file.exists()) {
                    file.mkdirs();
                }
                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
//                if (from_click.equalsIgnoreCase("1")) {
//                    playingAudio(file);
//                    from_click = "0";
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Success";
        }

        @Override
        protected void onPostExecute(String file_url) {
            super.onPostExecute(file_url);
            if (file_url.equalsIgnoreCase("Success")) {
//                if (mFileType != null
//                        && (!mFileType.equalsIgnoreCase("sent_image")
//                        && !mFileType.equalsIgnoreCase("sent_audio") && !mFileType
//                        .equalsIgnoreCase("sent_video"))) {
                mProgressBar.setVisibility(View.GONE);
                mDownLoadImg.setVisibility(View.GONE);


//                }

            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private void displayReceiverFile(final int adapterPosition, final ImageView receiverDownloadImg,
                                     ImageView receiverPhotoImg,
                                     final ProgressBar receiverProgressBar,
                                     String file, final String fileType, FrameLayout receiverDataLayout) {

        switch (fileType) {
            case "image":
                final File isFile = receiveImagePath(file);
                String file_name = AppConstants.CHAT_FILE_BASE_URL + file;
                if (isFile.exists()) {
                    receiverDownloadImg.setVisibility(View.GONE);
                } else {
                    receiverDownloadImg.setVisibility(View.VISIBLE);
                }
                if (file.isEmpty()) {
                    receiverPhotoImg.setImageResource(R.drawable.default_prop_icon);
                } else {
                    try {
                        Glide.with(mContext)
                                .load(file_name).error(R.drawable.default_prop_icon)
                                .into(receiverPhotoImg);
                    } catch (Exception ex) {
                        receiverPhotoImg.setImageResource(R.drawable.default_prop_icon);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }

                break;
            case "audio":
                String pathVideo = File.separator + "RB_Files" + File.separator + "RB_Audios";
                File videoDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + pathVideo);
                File newFName = new File(file);
                final File file1 = new File(videoDir + "/" + newFName.getName());

                if (file1.exists()) {
                    receiverDownloadImg.setVisibility(View.GONE);
                } else {
                    receiverDownloadImg.setVisibility(View.VISIBLE);
                }
                receiverPhotoImg.setImageResource(R.drawable.audio);
                break;
            case "video":
                receiverPhotoImg.setImageResource(R.drawable.video_icon);
                File isFile1 = receiverVideoPath(file);

                if (isFile1.exists()) {
                    receiverDownloadImg.setVisibility(View.GONE);
                } else {
                    receiverDownloadImg.setVisibility(View.VISIBLE);
                }
                break;
        }

        receiverDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFileType = fileType;
                switch (fileType) {
                    case "image":
                        File isFile = receiveImagePath(mChatList.get(adapterPosition).getFile());
                        if (isFile.exists()) {
                            DialogManager.getInstance().showImageDialog(mContext,
                                    AppConstants.CHAT_FILE_BASE_URL + mChatList
                                            .get(adapterPosition).getFile());
                        } else {
                            new DownloadFileFromURL(mChatList.get(adapterPosition)
                                    .getFile(), adapterPosition, receiverProgressBar, receiverDownloadImg)
                                    .execute(AppConstants.CHAT_FILE_BASE_URL + mChatList
                                            .get(adapterPosition).getFile());
                        }
                        break;
                    case "audio":
                        String pathVideo = File.separator + "RB_Files" + File.separator + "RB_Audios";
                        File videdir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + pathVideo);
                        File newfname = new File(mChatList.get(adapterPosition).getFile());
                        final File file = new File(videdir + "/" + newfname.getName());
                        if (file.exists()) {
                            playingAudio(file);
                        } else {
                            // from_click = "1";
                            new DownloadFileFromURL(mChatList.get(adapterPosition)
                                    .getFile(), adapterPosition, receiverProgressBar, receiverDownloadImg).
                                    execute(AppConstants.CHAT_FILE_BASE_URL + mChatList
                                            .get(adapterPosition).getFile());
                        }
                        break;
                    case "video":
                        File isFile1 = receiverVideoPath(mChatList.get(adapterPosition).getFile());
                        if (isFile1.exists()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(isFile1),
                                    "video/*");
                            mContext.startActivity(Intent.createChooser(intent,
                                    "Complete action using"));

                        } else {
                            new DownloadFileFromURL(mChatList.get(adapterPosition)
                                    .getFile(), adapterPosition, receiverProgressBar, receiverDownloadImg)
                                    .execute(AppConstants.CHAT_FILE_BASE_URL + mChatList
                                            .get(adapterPosition).getFile());
                        }
                        break;
                }

            }
        });
    }


    private void playingAudio(File file) {

        Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
        File file1 = new File(file.getAbsolutePath());
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file1).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        myIntent.setDataAndType(Uri.fromFile(file1), mimetype);
        mContext.startActivity(myIntent);

    }


    private void displaySenderFile(final int adapterPosition, final ImageView senderDownloadImg,
                                   ImageView senderPhotoImg,
                                   final ProgressBar senderProgressBar,
                                   final String file_type, final String file, FrameLayout senderDataLayout) {
        switch (file_type) {
            case "image":
                File isFile = sentImagePath(file.contains("/") ? getnameOfFile(file) : file);
                String file_name = AppConstants.CHAT_FILE_BASE_URL + file;
                if (isFile.exists()) {

                    senderDownloadImg.setVisibility(View.GONE);
                } else {
                    senderDownloadImg.setVisibility(View.VISIBLE);
                }


                if (file.isEmpty()) {
                    senderPhotoImg.setImageResource(R.drawable.default_prop_icon);
                } else {
                    try {
                        Glide.with(mContext)
                                .load(mChatList.get(adapterPosition).getGroupchat_id().isEmpty() ? mChatList.get(adapterPosition).getFile() : file_name).error(R.drawable.default_prop_icon)
                                .into(senderPhotoImg);

                    } catch (Exception ex) {
                        senderPhotoImg.setImageResource(R.drawable.default_prop_icon);
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }
                break;
            case "audio":
                senderDownloadImg.setVisibility(View.GONE);
                senderPhotoImg.setImageResource(R.drawable.audio);
                break;
            case "video":
                senderDownloadImg.setVisibility(View.GONE);
                senderPhotoImg.setImageResource(R.drawable.video_icon);
                break;

        }

        senderDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (file_type) {
                    case "image":
                        File isFile = sentImagePath(file.contains("/") ? getnameOfFile(file) : file);
                        if (isFile.exists()) {
                            DialogManager.getInstance().showImageDialog(mContext,
                                    mChatList.get(adapterPosition).getGroupchat_id().isEmpty() ?
                                            mChatList
                                                    .get(adapterPosition).getFile() :
                                            AppConstants.CHAT_FILE_BASE_URL + mChatList
                                                    .get(adapterPosition).getFile());
                        } else {
                            mFileType = "sent_image";
                            new DownloadFileFromURL(mChatList.get(adapterPosition)
                                    .getFile(), adapterPosition, senderProgressBar, senderDownloadImg)
                                    .execute(AppConstants.CHAT_FILE_BASE_URL + mChatList
                                            .get(adapterPosition).getFile());
                        }
                        break;
                    case "audio":
                        String pathVideo = File.separator + "RB_Sent_files" + File.separator + "Sent_Sounds";
                        File videdir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + pathVideo);
                        File newfname = new File(file);
                        File file = new File(videdir + "/" + newfname.getName());
                        if (file.exists()) {
                            playingAudio(file);
                        } else {
//							from_click = "1";
                            mFileType = "sent_audio";
                            new DownloadFileFromURL(mChatList.get(adapterPosition)
                                    .getFile(), adapterPosition, senderProgressBar, senderDownloadImg)
                                    .execute(AppConstants.CHAT_FILE_BASE_URL + mChatList
                                            .get(adapterPosition).getFile());
                        }
                        break;
                    case "video":

                        File videoFile1 = sentVideoPath(mChatList.get(adapterPosition).getFile()
                                .contains("/") ? getnameOfFile(mChatList.get(adapterPosition).getFile()) :
                                mChatList.get(adapterPosition).getFile());

                        // File videoFile1 = sentVideoPath(adapterPosition);
                        if (videoFile1 != null && videoFile1.exists()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(videoFile1),
                                    "video/*");
                            mContext.startActivity(Intent.createChooser(intent,
                                    "Complete action using"));

                        } else {
                            mFileType = "sent_video";
                            new DownloadFileFromURL(mChatList.get(adapterPosition)
                                    .getFile(), adapterPosition, senderProgressBar, senderDownloadImg)
                                    .execute(AppConstants.CHAT_FILE_BASE_URL + mChatList
                                            .get(adapterPosition).getFile());
                        }
                        break;
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.receiver_layout)
        LinearLayout mReceiverLayout;

        @BindView(R.id.sender_layout)
        LinearLayout mSenderLayout;

        @BindView(R.id.receiver_data_layout)
        FrameLayout mReceiverDataLayout;

        @BindView(R.id.sender_data_layout)
        FrameLayout mSenderDataLayout;

        @BindView(R.id.receiver_img)
        ImageView mReceiverImg;

        @BindView(R.id.receiver_msg)
        TextView mReceiverMsgTxt;

        @BindView(R.id.receiver_username)
        TextView mReceiverNameTxt;

        @BindView(R.id.receiver_time)
        TextView mReceiverTimeTxt;

        @BindView(R.id.receiver_download)
        ImageView mReceiverDownloadImg;


        @BindView(R.id.sender_msg)
        TextView mSenderMsgTxt;

        @BindView(R.id.sender_img)
        ImageView mSenderImg;

        @BindView(R.id.sender_username)
        TextView mSenderNameTxt;

        @BindView(R.id.sender_time)
        TextView mSenderTimeTxt;


        @BindView(R.id.receiver_photo)
        ImageView mReceiverPhotoImg;

        @BindView(R.id.receiver_photo_username)
        TextView mReceiverFileUserNameTxt;

        @BindView(R.id.receiver_data_time)
        TextView mReceiverFileTimeTxt;

        @BindView(R.id.receiver_progress)
        ProgressBar mReceiverProgressBar;


        @BindView(R.id.sender_photo)
        ImageView mSenderPhotoImg;

        @BindView(R.id.sender_download)
        ImageView mSenderDownloadImg;


        @BindView(R.id.sender_photo_username)
        TextView mSenderFileUserNameTxt;

        @BindView(R.id.sender_data_time)
        TextView mSenderFileTimeTxt;

        @BindView(R.id.sender_progress)
        ProgressBar mSenderProgressBar;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private File sentImagePath(String image_file) {
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        File file = new File(extStorageDirectory + File.separator
                + "RB_Sent_files" + File.separator + "Sent_images");

        String filePath = file + File.separator
                + image_file;
        File isFile = new File(filePath);
        return isFile;
    }


//    private File sentImagePath(String image_file) {
//        File file;
//        String extStorageDirectory = Environment.getExternalStorageDirectory()
//                .toString();
//        file = new File(extStorageDirectory + File.separator
//                + "RB_Sent_files" + File.separator + "Sent_images");
//
//        try {
//            image_file = image_file.split(".")[0];
//            String filePath = file + File.separator
//                    + image_file;
//            File isFile = new File(filePath);
//            return isFile;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return file;
//
//    }


    private File receiveImagePath(String filepath) {
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        File file = new File(extStorageDirectory + File.separator + "RB_Files"
                + File.separator + "RB_Images");
//        String filePath = file
//                + File.separator
//                + mChatList
//                .get(position)
//                .getFile()
//                .substring(
//                        mChatList.get(position).getFile()
//                                .lastIndexOf("/") + 1);
        String filePath = file
                + File.separator
                + filepath;
        filePath = filePath.replace(AppConstants.CHAT_FILE_BASE_URL, "");
        File isFile = new File(filePath);
        return isFile;
    }

    private File sentVideoPath(String filepath) {
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();

        File file = new File(extStorageDirectory + File.separator
                + "RB_Sent_files" + File.separator + "Sent_videos");

        String filePath = file + File.separator
                + filepath;
        filePath = filePath.replace(AppConstants.CHAT_FILE_BASE_URL, "");
        File isFile = new File(filePath);
        return isFile;
    }

    private File receiverVideoPath(String filepath) {
        String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
        File file = new File(extStorageDirectory + File.separator + "RB_Files"
                + File.separator + "RB_Videos");
//        String filePath = file
//                + File.separator
//                + mChatList
//                .get(position)
//                .getFile()
//                .substring(
//                        mChatList.get(position).getFile()
//                                .lastIndexOf("/") + 1);
        String filePath = file
                + File.separator
                + filepath;
        filePath = filePath.replace(AppConstants.CHAT_FILE_BASE_URL, "");
        File isFile = new File(filePath);
        return isFile;
    }


//    private File receiverVideoPath(int position) {
//        String extStorageDirectory = Environment.getExternalStorageDirectory()
//                .toString();
//        File file = new File(extStorageDirectory + File.separator + "RB_Files"
//                + File.separator + "RB_Videos");
//        String filePath = file
//                + File.separator
//                + mChatList
//                .get(position)
//                .getFile()
//                .substring(
//                        mChatList.get(position).getFile()
//                                .lastIndexOf("/") + 1);
//        File isFile = new File(filePath);
//        return isFile;
//    }

    private String getnameOfFile(String path) {
        String imgpath = path;
        String result = imgpath.substring(imgpath.lastIndexOf("/") + 1);
        return result;
    }


}

