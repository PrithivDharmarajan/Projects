package com.smaat.jolt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.entity.ShopDetailsEntity;
import com.smaat.jolt.model.GlobalResponse;
import com.smaat.jolt.ui.BaseActivity;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GifMovieView;
import com.smaat.jolt.util.GlobalMethods;
import com.squareup.picasso.Picasso;

public class CoffeWaitFragment extends BaseFragment implements OnClickListener {

	private GifMovieView animAwesome, good, average, poor;
	private LinearLayout freeCoffeLay;
	private TextView coffee_shope_name, coffee_shope_address, time_date,
			coffee_type_count, coffee_type, coming_right_up;
	private ImageView shop_logo;
	private FrameLayout awesomeAnim, goodAnim, avgAnim, poorAnim;
	private final String AWESOME = "4", GOOD = "3", AVG = "2", POOR = "1";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(
				R.layout.shope_details_lets_do_this_freecoffee, container,
				false);
		setupUI(rootview);

		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.shope_details);
		setupUI(mViewGroup);

		GlobalMethods.setFont(mViewGroup, HomeScreen.helveticaNeueLight);

		BaseActivity.setFont(mViewGroup, HomeScreen.helveticaNeueMedium);
		hideSoftKeyboard(getActivity());

		initComponents(view);

	}

	private void initComponents(View view) {

		animAwesome = (GifMovieView) view.findViewById(R.id.awesomeAnimImage);
		good = (GifMovieView) view.findViewById(R.id.goodAnimImage);
		average = (GifMovieView) view.findViewById(R.id.avgAnimImage);
		poor = (GifMovieView) view.findViewById(R.id.poorAnimImage);

		freeCoffeLay = (LinearLayout) view.findViewById(R.id.freeCoffeLay);

		coffee_shope_name = (TextView) view
				.findViewById(R.id.coffee_shope_name);
		coffee_shope_address = (TextView) view
				.findViewById(R.id.coffee_shope_address);
		time_date = (TextView) view.findViewById(R.id.time_date);
		coffee_type_count = (TextView) view
				.findViewById(R.id.coffee_type_count);
		coffee_type = (TextView) view.findViewById(R.id.coffee_type);
		coming_right_up = (TextView) view.findViewById(R.id.coming_right_up);

		shop_logo = (ImageView) view.findViewById(R.id.shop_logo);

		awesomeAnim = (FrameLayout) view.findViewById(R.id.awesomeAnim);
		goodAnim = (FrameLayout) view.findViewById(R.id.goodAnim);
		avgAnim = (FrameLayout) view.findViewById(R.id.avgAnim);
		poorAnim = (FrameLayout) view.findViewById(R.id.poorAnim);

		coffee_shope_name.setTypeface(HomeScreen.helveticaNeueBold);
		coffee_shope_address.setTypeface(HomeScreen.helveticaNeueLight);
		time_date.setTypeface(HomeScreen.helveticaNeueLight);
		coffee_type_count.setTypeface(HomeScreen.helveticaNeueMedium);
		coffee_type.setTypeface(HomeScreen.helveticaNeueMedium);
		coming_right_up.setTypeface(HomeScreen.helveticaNeueBold);

		setListner();
		setData();
	}

	private void setListner() {
		freeCoffeLay.setOnClickListener(this);
		awesomeAnim.setOnClickListener(this);
		goodAnim.setOnClickListener(this);
		avgAnim.setOnClickListener(this);
		poorAnim.setOnClickListener(this);
	}

	ShopDetailsEntity shopDetails;

	private void setData() {

		shopDetails = AvailableDrinks.shopDetails;
		Picasso.with(getActivity())
				.load(AppConstants.BASE_TIMTHUMB
						+ shopDetails.getShopLogoImageUrl()
						+ "&q=200&zc=0&w=200")
				.placeholder(
						getActivity().getResources().getDrawable(
								R.drawable.converstation_jolt_icon))
				.error(getActivity().getResources().getDrawable(
						R.drawable.converstation_jolt_icon)).into(shop_logo);

		coffee_shope_name.setText(shopDetails.getShopName());
		coffee_shope_address.setText(shopDetails.getShopStreet());

		time_date.setText(GlobalMethods.getCurrentDate());

		if (ShopLetsDoThis.mChooseDrinkSize != null) {
			coffee_type.setText(ShopLetsDoThis.mChooseDrinkSize + " "
					+ ShopLetsDoThis.mChooseDrinks);
		}

		if (ShopLetsDoThis.mCupCount != null) {
			coffee_type_count.setText(ShopLetsDoThis.mCupCount + " "
					+ getString(R.string.drinks));
		}

		animAwesome.setMovieResource(R.drawable.awesome);
		animAwesome.setPaused(false);

		good.setMovieResource(R.drawable.good);
		good.setPaused(false);

		average.setMovieResource(R.drawable.average);
		average.setPaused(false);

		poor.setMovieResource(R.drawable.poor);
		poor.setPaused(false);
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.freeCoffeLay:
			moveToShareScreen();
			break;

		case R.id.awesomeAnim:
			APIRequestHandler.getInstance().setHowWasIt(
					GlobalMethods.getUserID(getActivity()),
					shopDetails.getShopId(), AWESOME, this);
			break;
		case R.id.goodAnim:
			APIRequestHandler.getInstance().setHowWasIt(
					GlobalMethods.getUserID(getActivity()),
					shopDetails.getShopId(), GOOD, this);
			break;
		case R.id.avgAnim:
			APIRequestHandler.getInstance().setHowWasIt(
					GlobalMethods.getUserID(getActivity()),
					shopDetails.getShopId(), AVG, this);
			break;
		case R.id.poorAnim:
			APIRequestHandler.getInstance().setHowWasIt(
					GlobalMethods.getUserID(getActivity()),
					shopDetails.getShopId(), POOR, this);
			break;
		}
	}

	@Override
	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof GlobalResponse) {

			GlobalResponse globalResponse = (GlobalResponse) responseObj;

			if (globalResponse != null
					&& globalResponse.getError_code().equalsIgnoreCase(
							AppConstants.SUCCESS_CODE)) {

				DialogManager.showToast(getActivity(), globalResponse.getMsg());

				moveToShareScreen();

			} else {
				DialogManager.showToast(getActivity(), globalResponse.getMsg());
			}

		}

	}

	private void moveToShareScreen() {
		HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
		GlobalMethods.storeValuetoPreference(getActivity(),
				GlobalMethods.STRING_PREFERENCE,
				AppConstants.COFFEE_WAIT_SCREEN,
				AppConstants.IS_COFFEE_WAIT_SCREEN);
		((HomeScreen) getActivity()).mFragment = new GetFreeCoffeeFragment();
		((HomeScreen) getActivity()).updateDisplayFragment(
				((HomeScreen) getActivity()).mFragment, true);
	}
}
