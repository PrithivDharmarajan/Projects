package com.smaat.renterblock.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.TypefaceSingleton;

public class CallChatActivity extends BaseActivity implements OnClickListener {

    private String message, type, chat_id;
    private String user_name;
    private TextView mCallTxt;
    private Button mAccept, mReject, mEnd_of_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_chat);
        ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
        Typeface mTypefaceBold = TypefaceSingleton.getInstance()
                .getHelveticaBold(this);
        setFont(root, mTypefaceBold);
        setupUI(root);
        mCallTxt = (TextView) findViewById(R.id.call_txt);
        mAccept = (Button) findViewById(R.id.accept);
        mReject = (Button) findViewById(R.id.reject);
        mEnd_of_call = (Button) findViewById(R.id.end_call);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = extras.getString("msg");
            type = extras.getString("type");
            chat_id = extras.getString("chat_id");
            user_name = message.substring(message.lastIndexOf(" ") + 1);

            AppConstants.Type_of_call = type;
        }
        if (AppConstants.flag_video_chat_screen.equalsIgnoreCase("true")) {
            mAccept.setVisibility(View.GONE);
            mEnd_of_call.setVisibility(View.VISIBLE);
        } else {
            mAccept.setVisibility(View.VISIBLE);
            mEnd_of_call.setVisibility(View.GONE);
        }
        if (type != null && type.equals("voice")) {
            AppConstants.Type_of_call = "voice";
            mCallTxt.setText(message);
        } else {
            AppConstants.Type_of_call = "video";
            mCallTxt.setText(message);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:

                // TODO accept Call
//			Intent video_chat = new Intent(CallChatActivity.this,
//					VideoChatMainActivity.class);
//			video_chat.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//			video_chat.putExtra("name", user_name);
//			video_chat.putExtra("group_id", chat_id);
//			video_chat.putExtra("call_from", "GCM");
//			startActivity(video_chat);
//			finish();
                break;
            case R.id.end_call:
                // TODO End Call
//			if (AppConstants.flag_video_chat_screen.equalsIgnoreCase("true")) {
//				if (VideoChatMainActivity.mConferenceManager != null)
//					VideoChatMainActivity.mConferenceManager.leaveSession();
//				VideoCallActivity.mConferenceManager.leaveSession();
//
//				// VideoChatMainActivity.videoonPause();
//				VideoCallActivity.mVideo_call_act.finish();
//				VideoChatMainActivity.video_chat_main.finish();
//			}
//
//			Intent video_chats = new Intent(CallChatActivity.this,
//					VideoChatMainActivity.class);
//			video_chats.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//			video_chats.putExtra("name", user_name);
//			video_chats.putExtra("group_id", chat_id);
//			video_chats.putExtra("call_from", "GCM");
//			startActivity(video_chats);
//			finish();
                break;
            case R.id.reject:
                launchActivity(MapFragmentActivity.class);
                finish();
                break;
        }
    }
}
