package com.calix.calixgigaspireapp.ui.guest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.guest.GuestContactAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.CommonResponse;
import com.calix.calixgigaspireapp.output.model.ContactResponse;
import com.calix.calixgigaspireapp.output.model.GuestWifiEntity;
import com.calix.calixgigaspireapp.output.model.RouterMapResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DateUtil;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetupGuestContact extends BaseActivity {


    @BindView(R.id.setup_guest_contact_lay)
    RelativeLayout mSetupGuestContactParLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.setup_guest_contact_header_bg_lay)
    RelativeLayout mSetupGuestContactHeaderBgLay;

    @BindView(R.id.contact_txt)
    TextView mContactTxt;

    @BindView(R.id.setup_guest_contact_recycler_view)
    RecyclerView mSetupGuestContactRecyclerView;

    @BindView(R.id.setup_guest_contact_toggle_img)
    SwitchCompat mGuestContactToggle;

    @BindView(R.id.setup_search_edt)
    EditText mSearchEdt;

    @BindView(R.id.contacts_not_available_txt)
    TextView mContactsNotAvailableTxt;

    @BindView(R.id.setup_search_lay)
    CardView mSetupSearchLay;

    @BindView(R.id.all_contact_lay)
    RelativeLayout mAllContactLay;

    @BindView(R.id.done_btn)
    Button mDoneBtn;

    private ArrayList<ContactResponse> mContactArrListRes = new ArrayList<>();
    private LinkedHashMap<String, String> mContactsLinkedHashMap = new LinkedHashMap<>();
    private boolean mIsSendMsgBool = false;
    private CompoundButton.OnCheckedChangeListener mSelectAllContactChangeListener;
    private int mPauseAllInt = 0;
    private String mRouterIdStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_setup_guest_contact);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Once user send msg to the contact we changed the flag "isSendMsgBool" as true and the flag default value is false */
        if (mIsSendMsgBool) {
            onBackPressed();
        }
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mSetupGuestContactParLay);

        setHeaderView();

        mSelectAllContactChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.clear();
                for (int posInt = 0; posInt < mContactArrListRes.size(); posInt++) {
                    mContactArrListRes.get(posInt).setChecked(isChecked);
                    if (isChecked) {
                        AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.put(mContactArrListRes.get(posInt).getNumber(), mContactArrListRes.get(posInt).getNumber());
                    }
                }
                mSearchEdt.setText("");
                setAdapter(mContactArrListRes);
            }
        };

        mGuestContactToggle.setOnCheckedChangeListener(mSelectAllContactChangeListener);

        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int a, int b, int c) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int a, int b, int c) {
                charSequence = charSequence.toString();
                ArrayList<ContactResponse> contactArrListRes = new ArrayList<>();
                AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.clear();

                for (int posInt = 0; posInt < mContactArrListRes.size(); posInt++) {
                    if (mContactArrListRes.get(posInt).getName().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            mContactArrListRes.get(posInt).getNumber().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        if (mGuestContactToggle.isChecked()) {
                            mContactArrListRes.get(posInt).setChecked(true);
                        }
                        contactArrListRes.add(mContactArrListRes.get(posInt));
                        if (mContactArrListRes.get(posInt).isChecked()) {
                            AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.put(mContactArrListRes.get(posInt).getNumber(), mContactArrListRes.get(posInt).getNumber());
                        }
                    }
                }
                setAdapter(contactArrListRes);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*Keypad button action*/
        mSearchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftKeyboard(SetupGuestContact.this);
                }
                return true;
            }
        });

        /*Ask permission for */
        if (askPermissions()) {
            new ContactList().execute();
        }


    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.setup_search_lay, R.id.done_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.setup_search_lay:
                mSearchEdt.setEnabled(true);
                break;
            case R.id.done_btn:
                sendGuestNetworkContactAPICall();
                break;
        }
    }

    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.setup_guest_wifi));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSetupGuestContactHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mSetupGuestContactHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(SetupGuestContact.this)));
                    mSetupGuestContactHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(SetupGuestContact.this), 0, 0);
                }

            });
        }
    }


    /*To get permission for access image camera and storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            int readStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int writeStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int sendSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

            if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
            }
            if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (sendSmsPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
            }

        }

        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                @Override
                public void onPositiveClick() {
                    new ContactList().execute();
                }

                public void onNegativeClick() {
                    onBackPressed();
                }
            });
        }

        return addPermission;
    }

    private ArrayList<ContactResponse> getContactDetails() {
        mContactArrListRes = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int nameInt = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberInt = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (cursor.moveToFirst()) {
                do {
                    String nameStr = cursor.getString(nameInt);
                    String numStr = cursor.getString(numberInt);
                    if (mContactsLinkedHashMap.get(numStr) == null) {
                        ContactResponse contactResponse = new ContactResponse();
                        contactResponse.setName(nameStr);
                        contactResponse.setNumber(numStr);
                        contactResponse.setChecked(false);
                        mContactsLinkedHashMap.put(numStr, nameStr);
                        mContactArrListRes.add(contactResponse);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        Collections.sort(mContactArrListRes, new Comparator<ContactResponse>() {
            @Override
            public int compare(ContactResponse contactOne, ContactResponse contactTwo) {
                return contactOne.getName().compareTo(contactTwo.getName());
            }
        });
        return mContactArrListRes;
    }

    /*routerMapAPICall calls*/
    private void routerMapAPICall() {
        APIRequestHandler.getInstance().routerMapAPICall(this);
    }

    private void setAdapter(final ArrayList<ContactResponse> getContactDetails) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mSetupSearchLay.setVisibility(mContactArrListRes.size() > 0 ? View.VISIBLE : View.GONE);
                mContactsNotAvailableTxt.setVisibility(getContactDetails.size() > 0 ? View.GONE : View.VISIBLE);
                mAllContactLay.setVisibility(getContactDetails.size() > 0 ? View.VISIBLE : View.GONE);
                mSetupGuestContactRecyclerView.setVisibility(getContactDetails.size() > 0 ? View.VISIBLE : View.GONE);
                mDoneBtn.setVisibility(getContactDetails.size() > 0 ? View.VISIBLE : View.GONE);
                mContactTxt.setVisibility(getContactDetails.size() > 0 ? View.VISIBLE : View.GONE);

                mSetupGuestContactRecyclerView.setLayoutManager(new LinearLayoutManager(SetupGuestContact.this));
                mSetupGuestContactRecyclerView.setAdapter(new GuestContactAdapter(getContactDetails, mGuestContactToggle, mSelectAllContactChangeListener, mContactTxt, SetupGuestContact.this));

            }
        });
    }

    private void sendGuestNetworkContactAPICall() {
        if (AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.size() > 0) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPauseAllInt = 0;
                    Set<String> totalPhNumStr = AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.keySet();
                    for (String phNumStr : totalPhNumStr) {
                        mPauseAllInt += 1;
                        APIRequestHandler.getInstance().sendGuestContactAPICall(mContactsLinkedHashMap.get(phNumStr), PreferenceUtil.getUserId(SetupGuestContact.this), mRouterIdStr, SetupGuestContact.this);
                    }
                }
            });


        } else {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.select_one_contact), this);
        }
    }

    private void sendingMsg() {
        mIsSendMsgBool = true;

        if (AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.size() > 0) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    StringBuilder contactNumStr = new StringBuilder();
                    Set<String> totalPhNumStr = AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.keySet();
                    for (String phNumStr : totalPhNumStr) {
                        System.out.println(phNumStr);
                        contactNumStr.append(phNumStr).append(";");
                    }

                    GuestWifiEntity guestWifiInputModel = AppConstants.GUEST_WIFI_DETAILS;
                    String shareTxt = "\nAndroid Scanner App : https://play.google.com/store/apps/details?id=com.qrbarcode.scanner\n\n" +
                            "IOS Scanner App: https://itunes.apple.com/cr/app/qrbot/id1048473097?l=en&mt=8\n\n" +
                            "Guest Network Details \n\n" +
                            "Event Name" + " : " + guestWifiInputModel.getEventName() + "\n" +
                            (guestWifiInputModel.isIndefinite() ? "" : "Start Date" + " : " + DateUtil.getCustomDateAndTimeFormat(guestWifiInputModel.getDuration().getStartTime(), AppConstants.CUSTOM_DATE_TIME_FORMAT) + "\t\t\t" +
                                    "End Date" + " : " + DateUtil.getCustomDateAndTimeFormat(guestWifiInputModel.getDuration().getEndTime(), AppConstants.CUSTOM_DATE_TIME_FORMAT) + "\n")
                            +
                            "SSID" + " : " + guestWifiInputModel.getGuestWifiName() + "\n" +
                            "Password" + " : " + guestWifiInputModel.getGuestWifiPassword();

                    Intent messageSendingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    Uri bmpUri = (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? FileProvider.getUriForFile(SetupGuestContact.this, getApplicationContext().getPackageName() + ".provider", Objects.requireNonNull(getOutputMediaFile(guestWifiInputModel.getQrImage()))) : Uri.fromFile(getOutputMediaFile(guestWifiInputModel.getQrImage()));
                    Uri bmpUri = getImageUri(SetupGuestContact.this,guestWifiInputModel.getQrImage());
                    String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(SetupGuestContact.this);

                    messageSendingIntent.setPackage(defaultSmsApp);
//                        messageSendingIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
                    messageSendingIntent.putExtra("address", contactNumStr.toString());
                    messageSendingIntent.putExtra("sms_body", shareTxt);

                    messageSendingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    messageSendingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    messageSendingIntent.setType("image/png");


                    try {

                        startActivity(messageSendingIntent);
                    } catch (Exception e) {

                        sysOut("Exception---" + e.toString());
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/jpeg");
                        intent.putExtra("address", contactNumStr.toString());
                        intent.putExtra(Intent.EXTRA_TEXT, shareTxt);
                        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "Share Guest Network Details"));

                    }
                }
            });

        } else {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.select_one_contact), this);
        }

    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss",
                Locale.getDefault()).format(new Date());

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, getString(R.string.app_name) + getString(R.string.hyphen_sym) + timeStamp, null);
        return Uri.parse(path);
    }

    private File getOutputMediaFile(Bitmap bmp) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(this.getClass().getSimpleName(), getString(R.string.failed_directory));
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss",
                Locale.getDefault()).format(new Date());

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + getString(R.string.app_name) + getString(R.string.hyphen_sym) + timeStamp + ".png");
        try {
            FileOutputStream out = new FileOutputStream(mediaFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return mediaFile;
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RouterMapResponse) {
            RouterMapResponse routerMapResponse = (RouterMapResponse) resObj;
            mRouterIdStr = routerMapResponse.getRouters().size() > 0 && !routerMapResponse.getRouters().get(0).getRouterId().isEmpty() ? routerMapResponse.getRouters().get(0).getRouterId() : "";
        } else if ((resObj instanceof CommonResponse)) {
            if (mPauseAllInt != 0 && (mPauseAllInt == AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.size())) {
                sendingMsg();
            }
        }
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (resObj instanceof RouterMapResponse) {
                                routerMapAPICall();
                            } else {
                                sendGuestNetworkContactAPICall();
                            }
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        String searchStr = mSearchEdt.getText().toString().trim();
        if (searchStr.isEmpty()) {
            previousScreen(GuestNetwork.class);
        } else {
            mSearchEdt.setText("");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ContactList extends AsyncTask<String, Void, ArrayList<ContactResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogManager.getInstance().showProgress(SetupGuestContact.this);
                }
            });
        }

        @Override
        protected ArrayList<ContactResponse> doInBackground(String... strings) {
            return getContactDetails();
        }

        @Override
        protected void onPostExecute(final ArrayList<ContactResponse> contactResponses) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AppConstants.SELECTED_CONTACT_LINKED_HASH_MAP.clear();
                    setAdapter(contactResponses);
                    routerMapAPICall();
                    DialogManager.getInstance().hideProgress();
                }
            });
        }
    }

}


