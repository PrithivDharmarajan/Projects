package com.smaat.renterblock.friends.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.entity.ChatEntity;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class FriendsChatAdapter extends ArrayAdapter<String> {
	private Context context;
	private int layout;
	private AQuery aq;
	private ArrayList<ChatEntity> chatMessages;
	private String UserID, senderName;
	private String Type;
	private ListView listView;
	Typeface helvetica_normal;
	String from_click = "0";

	public FriendsChatAdapter(Context context, int layout, AQuery aq,
							  ArrayList<ChatEntity> chatMessages, ListView listView) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.aq = aq;
		this.chatMessages = chatMessages;
		this.listView = listView;
		helvetica_normal = TypefaceSingleton.getInstance()
				.getHelvetica(context);
		UserID = (String) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_userId);
		senderName = (String) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_user_name);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(layout, null);
		aq = aq.recycle(convertView);
		ViewGroup root = (ViewGroup) convertView
				.findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
				context);
		// Typeface mTypefaceBold = TypefaceSingleton.getInstance()
		// .getHelveticaBold(mContext);
		((BaseActivity) context).setFont(root, mTypeface);

		SenderAndReceiver(position);
		return convertView;
	}

	@Override
	public int getCount() {
		int size;
		if (chatMessages != null && chatMessages.size() > 0) {
			size = chatMessages.size();
		} else {
			size = 0;
		}
		return size;
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		private String imageoutput;
		String file_name;
		int position;

		public DownloadFileFromURL(String file_name, int position) {
			this.file_name = file_name;
			this.position = position;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (Type != null
					&& (!Type.equalsIgnoreCase("sent_image")
					&& !Type.equalsIgnoreCase("sent_audio") && !Type
					.equalsIgnoreCase("sent_video"))) {
				ProgressBar progress = getReceiverProgressBar(position);
				progress.setVisibility(View.VISIBLE);
				ImageView download = getReceiverDownload(position);
				download.setVisibility(View.GONE);
			} else {
				if (Type.equalsIgnoreCase("sent_image")) {
					((BaseActivity) context).ImageViewer(file_name);
				}
			}

		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				int lenghtOfFile = conection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);
				String extStorageDirectory = Environment
						.getExternalStorageDirectory().toString();
				String filePath = null;
				File file = null;

				if (Type != null
						&& (!Type.equalsIgnoreCase("sent_image")
						&& !Type.equalsIgnoreCase("sent_audio") && !Type
						.equalsIgnoreCase("sent_video"))) {
					if (Type != null && Type.equalsIgnoreCase("image")) {
						filePath = file_name;
						file = new File(extStorageDirectory + File.separator
								+ "RB_Files" + File.separator + "RB_Images");
					} else if (Type != null && Type.equalsIgnoreCase("audio")) {
						filePath = file_name;

						file = new File(extStorageDirectory + File.separator
								+ "RB_Files" + File.separator + "RB_Audios");
					} else if (Type != null && Type.equalsIgnoreCase("video")) {
						filePath = file_name;

						file = new File(extStorageDirectory + File.separator
								+ "RB_Files" + File.separator + "RB_Videos");
					}
				} else {
					if (Type != null && Type.equalsIgnoreCase("sent_image")) {
						filePath = file_name;
						file = new File(extStorageDirectory + File.separator
								+ "RB_Sent_files" + File.separator
								+ "Sent_images");
					} else if (Type != null
							&& Type.equalsIgnoreCase("sent_audio")) {
						filePath = file_name;
						file = new File(extStorageDirectory + File.separator
								+ "RB_Sent_files" + File.separator
								+ "Sent_Sounds");
					} else if (Type != null
							&& Type.equalsIgnoreCase("sent_video")) {
						filePath = file_name;
						file = new File(extStorageDirectory + File.separator
								+ "RB_Sent_files" + File.separator
								+ "Sent_videos");
					}
				}
				if (!file.exists()) {
					file.mkdirs();
				}
				imageoutput = file + File.separator
						+ filePath.substring(filePath.lastIndexOf("/") + 1);

				OutputStream output = new FileOutputStream(imageoutput);
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
				if (from_click.equalsIgnoreCase("1")) {
					File fl = new File(imageoutput);
					playingAudio(fl);
					from_click = "0";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Success";
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
		}

		@Override
		protected void onPostExecute(String file_url) {

			if (file_url.equalsIgnoreCase("Success")) {
				if (Type != null
						&& (!Type.equalsIgnoreCase("sent_image")
						&& !Type.equalsIgnoreCase("sent_audio") && !Type
						.equalsIgnoreCase("sent_video"))) {
					ProgressBar progress = getReceiverProgressBar(position);
					progress.setVisibility(View.GONE);
					ImageView download = getReceiverDownload(position);
					download.setVisibility(View.GONE);
				}

			}
		}

	}

	private void SenderAndReceiver(final int position) {
		if (chatMessages.get(position).getUser_id() != null
				&& chatMessages.get(position).getUser_id()
				.equalsIgnoreCase(UserID)) {

			senderData(position);

		} else {
			receiverData(position);

		}
	}

	private void receiverData(final int position) {
		aq.id(R.id.receiver_usename).getTextView()
				.setText(chatMessages.get(position).getUsername());
		// aq.id(R.id.receiver_usename).getTextView()
		// .setText(chatMessages.get(position).getSender_name());
		if (chatMessages.get(position).getFile_type().equalsIgnoreCase("image")
				|| chatMessages.get(position).getFile_type()
				.equalsIgnoreCase("video")
				|| chatMessages.get(position).getFile_type()
				.equalsIgnoreCase("audio")) {

			try {
				aq.id(R.id.receiver_data_time)
						.getTextView()
						.setText(
								GlobalMethods.timeDiff(chatMessages.get(
										position).getSend_time()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (chatMessages.get(position).getFile_type()
					.equalsIgnoreCase("image")) {

				displayReceiverImageFile(position);
			}

			else if (chatMessages.get(position).getFile_type()
					.equalsIgnoreCase("audio")) {

				displayReceiverAudioFile(position);
			} else if (chatMessages.get(position).getFile_type()
					.equalsIgnoreCase("video")) {

				displayReceiverVideoFile(position);
			}
			aq.id(R.id.sender_data_layout).getView().setVisibility(View.GONE);
			aq.id(R.id.receiver_data_layout).getView()
					.setVisibility(View.VISIBLE);

			aq.id(R.id.receiver_layout).getView().setVisibility(View.GONE);
			aq.id(R.id.sender_layout).getView().setVisibility(View.GONE);

		} else {

			try {
				aq.id(R.id.receiver_time)
						.getTextView()
						.setText(
								GlobalMethods.timeDiff(chatMessages.get(
										position).getSend_time()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			aq.id(R.id.receiver_layout).getView().setVisibility(View.VISIBLE);
			aq.id(R.id.receiver_msg).getTextView()
					.setText(chatMessages.get(position).getMessage());
		}

	}

	private void displayReceiverVideoFile(final int position) {
		aq.id(R.id.receiver_photo).getImageView()
				.setImageResource(R.drawable.video_icon);
		aq.id(R.id.receiver_photo_username).getTextView()
				.setText(chatMessages.get(position).getUsername());
		File isFile = videoPath(position);

		if (isFile.exists()) {
			aq.id(R.id.receiver_downoad).getImageView()
					.setVisibility(View.GONE);
		}
		aq.id(R.id.receiver_data_layout).getView()
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Type = chatMessages.get(position).getFile_type();
						File isFile = videoPath(position);
						if (isFile.exists()) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(isFile),
									"video/*");
							context.startActivity(Intent.createChooser(intent,
									"Complete action using"));

						} else {
							new DownloadFileFromURL(chatMessages.get(position)
									.getFile(), position).execute(chatMessages
									.get(position).getFile());
						}

					}
				});
	}

	private File sentAuidoPath(int position) {

		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		File file = new File(extStorageDirectory + File.separator
				+ "RB_Sent_files" + File.separator + "Sent_Sounds");
		// filePath.substring(filePath.lastIndexOf("/") + 1)
		String filePath = file + File.separator
				+ chatMessages.get(position).getFile();
		File isFile = new File(filePath);
		return isFile;
	}

	private File sentImagePath(int position) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		File file = new File(extStorageDirectory + File.separator
				+ "RB_Sent_files" + File.separator + "Sent_images");

		String filePath = file + File.separator
				+ chatMessages.get(position).getFile();

		// String path_fil = chatMessages.get(position).getFile();
		// String filePath = file + File.separator
		// + path_fil.substring(path_fil.lastIndexOf("/") + 1);

		File isFile = new File(filePath);
		return isFile;
	}

	private File sentVideoPath(int position) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();

		File file = new File(extStorageDirectory + File.separator
				+ "RB_Sent_files" + File.separator + "Sent_videos");

		String filePath = file + File.separator
				+ chatMessages.get(position).getFile();
		filePath = filePath.replace("http://smaatapps.com/rb/chatfile/", "");
		File isFile = new File(filePath);
		return isFile;
	}

	private File auidoPath(int position) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		File file = new File(extStorageDirectory + File.separator + "RB_Files"
				+ File.separator + "RB_Audios");
		String filePath = file
				+ File.separator
				+ chatMessages
				.get(position)
				.getFile()
				.substring(
						chatMessages.get(position).getFile()
								.lastIndexOf("/") + 1);
		File isFile = new File(filePath);
		return isFile;
	}

	private File imagePath(int position) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		File file = new File(extStorageDirectory + File.separator + "RB_Files"
				+ File.separator + "RB_Images");
		String filePath = file
				+ File.separator
				+ chatMessages
				.get(position)
				.getFile()
				.substring(
						chatMessages.get(position).getFile()
								.lastIndexOf("/") + 1);
		File isFile = new File(filePath);
		return isFile;
	}

	private File videoPath(int position) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		File file = new File(extStorageDirectory + File.separator + "RB_Files"
				+ File.separator + "RB_Videos");
		String filePath = file
				+ File.separator
				+ chatMessages
				.get(position)
				.getFile()
				.substring(
						chatMessages.get(position).getFile()
								.lastIndexOf("/") + 1);
		File isFile = new File(filePath);
		return isFile;
	}

	private void displayReceiverAudioFile(final int position) {

		aq.id(R.id.receiver_photo).getImageView()
				.setImageResource(R.drawable.audio);
		aq.id(R.id.receiver_photo_username).getTextView()
				.setText(chatMessages.get(position).getUsername());
		File isFile = auidoPath(position);

		if (isFile.exists()) {
			aq.id(R.id.receiver_downoad).getImageView()
					.setVisibility(View.GONE);
		}

		aq.id(R.id.receiver_data_layout).getView()
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Type = chatMessages.get(position).getFile_type();

						File isFile = auidoPath(position);

						if (isFile.exists()) {
							playingAudio(isFile);
						} else {
							new DownloadFileFromURL(chatMessages.get(position)
									.getFile(), position).execute(chatMessages
									.get(position).getFile());
						}

					}
				});

	}

	private void displayReceiverImageFile(final int position) {

		aq.id(R.id.receiver_photo).image(chatMessages.get(position).getFile(),
				true, true);
		aq.id(R.id.receiver_photo_username).getTextView()
				.setText(chatMessages.get(position).getUsername());
		File isFile = imagePath(position);

		if (isFile.exists()) {
			aq.id(R.id.receiver_downoad).getImageView()
					.setVisibility(View.GONE);
		} else {
			aq.id(R.id.receiver_downoad).getImageView()
					.setVisibility(View.VISIBLE);
		}
		aq.id(R.id.receiver_data_layout).getView()
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Type = chatMessages.get(position).getFile_type();
						File isFile = imagePath(position);
						if (isFile.exists()) {
							((BaseActivity) context).ImageViewer(isFile
									.getAbsolutePath());
						} else {

							new DownloadFileFromURL(chatMessages.get(position)
									.getFile(), position).execute(chatMessages
									.get(position).getFile());
						}

					}
				});

	}

	private ProgressBar getReceiverProgressBar(int index) {
		View v = listView
				.getChildAt(index - listView.getFirstVisiblePosition());

		ProgressBar progress = (ProgressBar) v
				.findViewById(R.id.receiver_progress);
		return progress;
	}

	private ImageView getReceiverDownload(int index) {
		View v = listView
				.getChildAt(index - listView.getFirstVisiblePosition());
		ImageView download = (ImageView) v.findViewById(R.id.receiver_downoad);
		return download;
	}

	private ProgressBar getSenderProgressBar(int index) {
		View v = listView
				.getChildAt(index - listView.getFirstVisiblePosition());
		ProgressBar progress = (ProgressBar) v
				.findViewById(R.id.sender_progress);
		return progress;
	}

	private ImageView getSenderDownload(int index) {
		View v = listView
				.getChildAt(index - listView.getFirstVisiblePosition());
		ImageView download = (ImageView) v.findViewById(R.id.sender_downoad);
		return download;
	}

	private void senderData(final int position) {

		aq.id(R.id.sender_usename).getTextView().setText(senderName);
		if (chatMessages.get(position).getFile_type().equalsIgnoreCase("image")
				|| chatMessages.get(position).getFile_type()
				.equalsIgnoreCase("video")
				|| chatMessages.get(position).getFile_type()
				.equalsIgnoreCase("audio")) {

			try {
				aq.id(R.id.sender_data_time)
						.getTextView()
						.setText(
								GlobalMethods.timeDiff(chatMessages.get(
										position).getSend_time()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (chatMessages.get(position).getFile_type()
					.equalsIgnoreCase("image")) {

				displaySenderImageFile(position);

			} else if (chatMessages.get(position).getFile_type()
					.equalsIgnoreCase("audio")) {

				displaySenderAuidoFile(position);
			} else if (chatMessages.get(position).getFile_type()
					.equalsIgnoreCase("video")) {

				displaySenderVideoFile(position);
			}

			aq.id(R.id.receiver_data_layout).getView().setVisibility(View.GONE);
			aq.id(R.id.sender_data_layout).getView()
					.setVisibility(View.VISIBLE);

			aq.id(R.id.receiver_layout).getView().setVisibility(View.GONE);
			aq.id(R.id.sender_layout).getView().setVisibility(View.GONE);

		} else {

			try {
				aq.id(R.id.sender_time)
						.getTextView()
						.setText(
								GlobalMethods.timeDiff(chatMessages.get(
										position).getSend_time()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			aq.id(R.id.sender_layout).getView().setVisibility(View.VISIBLE);
			aq.id(R.id.sender_msg).getTextView()
					.setText(chatMessages.get(position).getMessage());
		}

	}

	private void displaySenderVideoFile(final int position) {
		final File isFile = sentVideoPath(position);
		final String file_name = getnameOfFile(isFile.getAbsolutePath());
		if (isFile.exists()

			// && file_name.equalsIgnoreCase(chatMessages
			// .get(position)
			// .getFile()
			// .substring(
			// chatMessages.get(position).getFile()
			// .lastIndexOf("/") + 1))
				) {

			aq.id(R.id.sender_downoad).getImageView().setVisibility(View.GONE);
		} else if (!isFile.exists()) {
			aq.id(R.id.sender_downoad).getImageView().setVisibility(View.GONE);
		}
		aq.id(R.id.sender_photo).getImageView()
				.setImageResource(R.drawable.video_icon);
		aq.id(R.id.sender_photo_username).getTextView().setText(senderName);
		aq.id(R.id.sender_data_layout).getView()
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isFile.exists()

							// && file_name.equalsIgnoreCase(chatMessages
							// .get(position)
							// .getFile()
							// .substring(
							// chatMessages.get(position)
							// .getFile()
							// .lastIndexOf("/") + 1))
								) {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(isFile),
									"video/*");
							context.startActivity(Intent.createChooser(intent,
									"Complete action using"));

						} else {
							Type = "sent_video";
							new DownloadFileFromURL(chatMessages.get(position)
									.getFile(), position).execute(chatMessages
									.get(position).getFile());
						}
					}
				});

	}

	private void playingAudio(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "audio/*");
		context.startActivity(Intent.createChooser(intent, "Play audio..."));

	}

	private void displaySenderAuidoFile(final int position) {
		final File isFile = sentAuidoPath(position);
		final String file_name = getnameOfFile(isFile.getAbsolutePath());
		if (isFile.exists()
				&& file_name.equalsIgnoreCase(chatMessages.get(position)
				.getFile())) {

			aq.id(R.id.sender_downoad).getImageView().setVisibility(View.GONE);
		} else if (!isFile.exists()) {
			aq.id(R.id.sender_downoad).getImageView().setVisibility(View.GONE);
		}
		aq.id(R.id.sender_photo).getImageView()
				.setImageResource(R.drawable.audio);
		aq.id(R.id.sender_photo_username).getTextView().setText(senderName);
		aq.id(R.id.sender_data_layout).getView()
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isFile.exists()
								&& file_name.equalsIgnoreCase(chatMessages.get(
								position).getFile())) {
							playingAudio(isFile);
						} else if (!isFile.exists()) {
							from_click = "1";
							Type = "sent_audio";
							new DownloadFileFromURL(chatMessages.get(position)
									.getFile(), position).execute(chatMessages
									.get(position).getFile());
						}
					}
				});
	}

	private void displaySenderImageFile(final int position) {
		final File isFile = sentImagePath(position);
		final String file_name = "http://smaatapps.com/rb/chatfile/"
				+ getnameOfFile(isFile.getAbsolutePath());
		if (isFile.exists()
				&& file_name.equalsIgnoreCase(chatMessages.get(position)
				.getFile())) {

			aq.id(R.id.sender_downoad).getImageView().setVisibility(View.GONE);
		} else if (!isFile.exists()) {
			aq.id(R.id.sender_downoad).getImageView().setVisibility(View.GONE);
		}
		aq.id(R.id.sender_photo_username).getTextView().setText(senderName);
		aq.id(R.id.sender_photo).image(chatMessages.get(position).getFile());
		aq.id(R.id.sender_data_layout).getView()
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (isFile.exists()
								&& file_name.equalsIgnoreCase(chatMessages.get(
								position).getFile())) {
							((BaseActivity) context).ImageViewer(isFile
									.getAbsolutePath());
						} else if (!isFile.exists()) {
							Type = "sent_image";
							new DownloadFileFromURL(chatMessages.get(position)
									.getFile(), position).execute(chatMessages
									.get(position).getFile());
						}
					}
				});
	}

	private String getnameOfFile(String path) {
		String imgpath = path;
		String result = imgpath.replace("http:/smaatapps.com/rb/chatfile/", "");
		// String result = imgpath.substring(imgpath.lastIndexOf("/") + 1);
		return result;
	}

}
