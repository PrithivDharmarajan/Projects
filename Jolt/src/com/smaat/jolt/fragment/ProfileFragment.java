package com.smaat.jolt.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.entity.UserDetailsEntity;
import com.smaat.jolt.model.PlanEntity;
import com.smaat.jolt.model.ProfileResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogMangerCallback;
import com.smaat.jolt.util.GlobalMethods;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends BaseFragment implements OnClickListener,
		DialogMangerCallback {

	private ImageView user_profile_img;
	private Button mPurchasePlanBtn, mAddBtn;
	private TextView user_name_txt, user_email, cardNumber, active_credit_card,
			mMyPlans, email_subscription_txt;
	public static ArrayList<PlanEntity> mUserPlanList;
	static String mNoOfDrinks, mTxt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.my_account, container, false);
		setupUI(rootview);

		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		setupUI(mViewGroup);

		hideSoftKeyboard(getActivity());

		initComponents(view);
	}

	private void initComponents(View view) {

		mPurchasePlanBtn = (Button) view.findViewById(R.id.purchasePlan);
		mAddBtn = (Button) view.findViewById(R.id.add_btn);

		user_name_txt = (TextView) view.findViewById(R.id.user_name_txt);
		user_email = (TextView) view.findViewById(R.id.user_email);

		user_profile_img = (ImageView) view.findViewById(R.id.user_profile_img);

		cardNumber = (TextView) view.findViewById(R.id.cardNumber);

		mMyPlans = (TextView) view.findViewById(R.id.my_plans);

		active_credit_card = (TextView) view
				.findViewById(R.id.active_credit_card);
		email_subscription_txt = (TextView) view
				.findViewById(R.id.email_subscription_txt);

		user_name_txt.setTypeface(HomeScreen.helveticaNeueBold);
		user_email.setTypeface(HomeScreen.helveticaNeueMedium);

		active_credit_card.setTypeface(HomeScreen.helveticaNeueBold);
		cardNumber.setTypeface(HomeScreen.helveticaNeueMedium);
		email_subscription_txt.setTypeface(HomeScreen.helveticaNeueBold);

		mPurchasePlanBtn.setTypeface(HomeScreen.helveticaNeueBold);
		mAddBtn.setTypeface(HomeScreen.helveticaNeueBold);
		mMyPlans.setTypeface(HomeScreen.helveticaNeueBold);

		setClickListener();
		setData();

		APIRequestHandler.getInstance().getMyProfile(
				GlobalMethods.getUserID(getActivity()), this);
	}

	private void setData() {
		user_name_txt.setText((String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.FULL_NAME));
		System.out.println((String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.USER_ID));
		user_email.setText((String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.EMAIL_ADDRESS));
		String profilePic = (String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.PROFILE_PIC);
		Picasso.with(getActivity())
				.load(profilePic)
				.placeholder(
						getActivity().getResources().getDrawable(
								R.drawable.default_shop_image))
				.error(getActivity().getResources().getDrawable(
						R.drawable.default_shop_image)).fit().centerCrop()
				.into(user_profile_img);

		String cardNumberValue = ((String) GlobalMethods
				.getValueFromPreference(getActivity(),
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.CARD_NUMBER)).trim();

		if (cardNumberValue.isEmpty()) {
			mAddBtn.setText(getString(R.string.add));
			cardNumber.setText(getString(R.string.no_credit_card_associated));

		} else {
			// after Checking that will removed
			mAddBtn.setText(getString(R.string.edit));

			String card = getString(R.string.XXXX_XXXX)
					+ cardNumberValue.substring(cardNumberValue.length() - 4);
			cardNumber.setText(card);

		}
	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		if (responseObj instanceof ProfileResponse) {

			ProfileResponse response = (ProfileResponse) responseObj;

			if (response.getError_code().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {

				UserDetailsEntity user = response.getProfile();
				mNoOfDrinks = user.getNoOfDrinksAvailable();
				mUserPlanList = user.getPlan();
			}
		}
	}

	public void setClickListener() {
		mPurchasePlanBtn.setOnClickListener(this);
		mAddBtn.setOnClickListener(this);
		email_subscription_txt.setOnClickListener(this);
		mMyPlans.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.purchasePlan:

			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			HomeScreen.moveBackFragment = new ProfileFragment();
			((HomeScreen) getActivity()).updateDisplayFragment(
					new JoltPlansFragment(), true);

			break;
		case R.id.add_btn:
			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			HomeScreen.moveBackFragment = new ProfileFragment();
			((HomeScreen) getActivity()).updateDisplayFragment(
					new AddCreditCard(), true);

			break;
		case R.id.email_subscription_txt:
			mTxt = "";
			GlobalMethods.SendMail(getActivity(),
					getString(R.string.recommend_mail_id),
					getString(R.string.want_subscription), mTxt);
			break;
		
		case R.id.my_plans:
			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			HomeScreen.moveBackFragment = new ProfileFragment();
			((HomeScreen) getActivity()).updateDisplayFragment(new MyPlans(),
					true);
			break;
		}
	}

	@Override
	public void onOkclick() {
		// TODO Auto-generated method stub

	}

}
