package com.smaat.renterblock.ui;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyTypeEntity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class PropertyTypeActivity extends BaseActivity implements
		OnClickListener {

	private Button mAllTypes, mSingleFamilyHome, mCondo, mTownhouse, mCoop,
			mApartment, mLoft, mTIC, mApartmentCondo, mMobileManufactured,
			mFarmRanch, mLotLand, mMultiFamily, mIncomeInvestment, mHouseBoat;

	private boolean boolAllTypes = true, boolSingleFamilyHome = true,
			boolCondo = true, boolTownhouse = true, boolCoop = true,
			boolApartment = true, boolLoft = true, boolTIC = true,
			boolApartmentCondo = true, boolMobileManufactured = true,
			boolFarmRanch = true, boolLotLand = true, boolMultiFamily = true,
			boolIncomeInvestment = true, boolHouseBoat = true;
	public static ArrayList<String> mPropertyTypeList = new ArrayList<String>();

	public static PropertyTypeEntity mPropertyTypeEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_type);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		// Typeface mTypeface =
		// TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelveticaBold(
				this);
		setFont(root, mTypeface);
		setupUI(root);
		initComponents();
		setDefaultType();
	}

	private void initComponents() {

		mAllTypes = (Button) findViewById(R.id.all_types);
		mSingleFamilyHome = (Button) findViewById(R.id.single_family);
		mCondo = (Button) findViewById(R.id.condo);
		mTownhouse = (Button) findViewById(R.id.town_house);
		mCoop = (Button) findViewById(R.id.coop);
		mApartment = (Button) findViewById(R.id.apartment);
		mLoft = (Button) findViewById(R.id.loft);
		mTIC = (Button) findViewById(R.id.tic);
		mApartmentCondo = (Button) findViewById(R.id.apartment_condo);
		mMobileManufactured = (Button) findViewById(R.id.mobile_manufactured);
		mFarmRanch = (Button) findViewById(R.id.farm_ranch);
		mLotLand = (Button) findViewById(R.id.lot_land);
		mMultiFamily = (Button) findViewById(R.id.multi_family);
		mIncomeInvestment = (Button) findViewById(R.id.income_investment);
		mHouseBoat = (Button) findViewById(R.id.houseboat);

		mPropertyTypeEntity = (PropertyTypeEntity) GlobalMethods.readObject(
				this, AppConstants.HOME_RES_OBJ_KEY, PropertyTypeEntity.class);
		if (mPropertyTypeEntity == null) {
			mPropertyTypeEntity = new PropertyTypeEntity();
			mPropertyTypeEntity.setAllTypes("Select");
			mPropertyTypeEntity.setSingle_Family_Home("");
			mPropertyTypeEntity.setCondo("");
			mPropertyTypeEntity.setTownhouse("");
			mPropertyTypeEntity.setCoop("");
			mPropertyTypeEntity.setApartment("");
			mPropertyTypeEntity.setLoft("");
			mPropertyTypeEntity.setTIC("");
			mPropertyTypeEntity.setApartment_Condo_Townhouse("");
			mPropertyTypeEntity.setMobile_Manufactured("");
			mPropertyTypeEntity.setFarm_Ranch("");
			mPropertyTypeEntity.setLot_land("");
			mPropertyTypeEntity.setMulti_Family("");
			mPropertyTypeEntity.setIncome_Investment("");
			mPropertyTypeEntity.setHouseboat("");
			setDefaultType();
		} else {
			setDefaultType();
		}

	}

	private void setDefaultType() {
		if (mPropertyTypeEntity.getAllTypes().equalsIgnoreCase("All Types")) {
			mPropertyTypeList.clear();
			mPropertyTypeEntity.setAllTypes("All Types");
			mAllTypes.setBackgroundResource(R.drawable.tick_on);
			mSingleFamilyHome.setBackgroundResource(R.drawable.tick_on);
			mCondo.setBackgroundResource(R.drawable.tick_on);
			mTownhouse.setBackgroundResource(R.drawable.tick_on);
			mCoop.setBackgroundResource(R.drawable.tick_on);
			mApartment.setBackgroundResource(R.drawable.tick_on);
			mLoft.setBackgroundResource(R.drawable.tick_on);
			mTIC.setBackgroundResource(R.drawable.tick_on);
			mApartmentCondo.setBackgroundResource(R.drawable.tick_on);
			mMobileManufactured.setBackgroundResource(R.drawable.tick_on);
			mFarmRanch.setBackgroundResource(R.drawable.tick_on);
			mLotLand.setBackgroundResource(R.drawable.tick_on);
			mMultiFamily.setBackgroundResource(R.drawable.tick_on);
			mIncomeInvestment.setBackgroundResource(R.drawable.tick_on);
			mHouseBoat.setBackgroundResource(R.drawable.tick_on);

			boolAllTypes = false;
			boolSingleFamilyHome = false;
			boolCondo = false;
			boolTownhouse = false;
			boolCoop = false;
			boolApartment = false;
			boolLoft = false;
			boolTIC = false;
			boolApartmentCondo = false;
			boolMobileManufactured = false;
			boolFarmRanch = false;
			boolLotLand = false;
			boolMultiFamily = false;
			boolIncomeInvestment = false;
			boolHouseBoat = false;
		} else {
			mPropertyTypeList.clear();
			mPropertyTypeEntity.setAllTypes("Select");
			mAllTypes.setBackgroundResource(R.drawable.tick_off);
			mSingleFamilyHome.setBackgroundResource(R.drawable.tick_off);
			mCondo.setBackgroundResource(R.drawable.tick_off);
			mTownhouse.setBackgroundResource(R.drawable.tick_off);
			mCoop.setBackgroundResource(R.drawable.tick_off);
			mApartment.setBackgroundResource(R.drawable.tick_off);
			mLoft.setBackgroundResource(R.drawable.tick_off);
			mTIC.setBackgroundResource(R.drawable.tick_off);
			mApartmentCondo.setBackgroundResource(R.drawable.tick_off);
			mMobileManufactured.setBackgroundResource(R.drawable.tick_off);
			mFarmRanch.setBackgroundResource(R.drawable.tick_off);
			mLotLand.setBackgroundResource(R.drawable.tick_off);
			mMultiFamily.setBackgroundResource(R.drawable.tick_off);
			mIncomeInvestment.setBackgroundResource(R.drawable.tick_off);
			mHouseBoat.setBackgroundResource(R.drawable.tick_off);

			boolAllTypes = true;
			boolSingleFamilyHome = true;
			boolCondo = true;
			boolTownhouse = true;
			boolCoop = true;
			boolApartment = true;
			boolLoft = true;
			boolTIC = true;
			boolApartmentCondo = true;
			boolMobileManufactured = true;
			boolFarmRanch = true;
			boolLotLand = true;
			boolMultiFamily = true;
			boolIncomeInvestment = true;
			boolHouseBoat = true;
			setAnotherViews();
		}

		setAllTypes();
		saveObject();
	}

	private void setAnotherViews() {
		if (mPropertyTypeEntity.getSingle_Family_Home().equalsIgnoreCase(
				"Single-Family Home")) {
			mPropertyTypeEntity.setSingle_Family_Home("Single-Family Home");
			mSingleFamilyHome.setBackgroundResource(R.drawable.tick_on);
			boolSingleFamilyHome = false;
			mPropertyTypeList.add("Single-Family Home");

		} else {
			mSingleFamilyHome.setBackgroundResource(R.drawable.tick_off);
			boolSingleFamilyHome = true;
			mPropertyTypeList.remove("Single-Family Home");
			mPropertyTypeEntity.setSingle_Family_Home("");
		}
		if (mPropertyTypeEntity.getCondo().equalsIgnoreCase("Condo")) {
			mCondo.setBackgroundResource(R.drawable.tick_on);
			boolCondo = false;
			mPropertyTypeList.add("Condo");
			mPropertyTypeEntity.setCondo("Condo");

		} else {
			mCondo.setBackgroundResource(R.drawable.tick_off);
			boolCondo = true;
			mPropertyTypeList.remove("Condo");
			mPropertyTypeEntity.setCondo("");

		}
		if (mPropertyTypeEntity.getTownhouse().equalsIgnoreCase("Townhouse")) {
			mTownhouse.setBackgroundResource(R.drawable.tick_on);
			boolTownhouse = false;
			mPropertyTypeList.add("Townhouse");
			mPropertyTypeEntity.setTownhouse("Townhouse");
		} else {
			mTownhouse.setBackgroundResource(R.drawable.tick_off);
			boolTownhouse = true;
			mPropertyTypeList.remove("Townhouse");
			mPropertyTypeEntity.setTownhouse("");

		}
		if (mPropertyTypeEntity.getCoop().equalsIgnoreCase("Coop")) {
			mCoop.setBackgroundResource(R.drawable.tick_on);
			boolCoop = false;
			mPropertyTypeList.add("Coop");
			mPropertyTypeEntity.setCoop("Coop");

		} else {
			mCoop.setBackgroundResource(R.drawable.tick_off);
			boolCoop = true;
			mPropertyTypeList.remove("Coop");
			mPropertyTypeEntity.setCoop("");

		}
		if (mPropertyTypeEntity.getApartment().equalsIgnoreCase("Apartment")) {
			mApartment.setBackgroundResource(R.drawable.tick_on);
			boolApartment = false;
			mPropertyTypeList.add("Apartment");
			mPropertyTypeEntity.setApartment("Apartment");

		} else {
			mApartment.setBackgroundResource(R.drawable.tick_off);
			boolApartment = true;
			mPropertyTypeList.remove("Apartment");
			mPropertyTypeEntity.setApartment("");

		}
		if (mPropertyTypeEntity.getLoft().equalsIgnoreCase("Loft")) {
			mLoft.setBackgroundResource(R.drawable.tick_on);
			boolLoft = false;
			mPropertyTypeList.add("Loft");
			mPropertyTypeEntity.setLoft("Loft");

		} else {
			mLoft.setBackgroundResource(R.drawable.tick_off);
			boolLoft = true;
			mPropertyTypeList.remove("Loft");
			mPropertyTypeEntity.setLoft("");

		}
		if (mPropertyTypeEntity.getTIC().equalsIgnoreCase("TIC")) {
			mTIC.setBackgroundResource(R.drawable.tick_on);
			boolTIC = false;
			mPropertyTypeList.add("TIC");
			mPropertyTypeEntity.setTIC("TIC");

		} else {
			mTIC.setBackgroundResource(R.drawable.tick_off);
			boolTIC = true;
			mPropertyTypeList.remove("TIC");
			mPropertyTypeEntity.setTIC("");

		}
		if (mPropertyTypeEntity.getApartment_Condo_Townhouse()
				.equalsIgnoreCase("Apartment/Condo/Townhouse")) {
			mApartmentCondo.setBackgroundResource(R.drawable.tick_on);
			boolApartmentCondo = false;
			mPropertyTypeList.add("Apartment/Condo/Townhouse");
			mPropertyTypeEntity
					.setApartment_Condo_Townhouse("Apartment/Condo/Townhouse");

		} else {
			mApartmentCondo.setBackgroundResource(R.drawable.tick_off);
			boolApartmentCondo = true;
			mPropertyTypeList.remove("Apartment/Condo/Townhouse");
			mPropertyTypeEntity.setApartment_Condo_Townhouse("");

		}
		if (mPropertyTypeEntity.getMobile_Manufactured().equalsIgnoreCase(
				"Mobile/Manufactured")) {
			mMobileManufactured.setBackgroundResource(R.drawable.tick_on);
			boolMobileManufactured = false;
			mPropertyTypeList.add("Mobile/Manufactured");
			mPropertyTypeEntity.setMobile_Manufactured("Mobile/Manufactured");

		} else {
			mMobileManufactured.setBackgroundResource(R.drawable.tick_off);
			boolMobileManufactured = true;
			mPropertyTypeList.remove("Mobile/Manufactured");
			mPropertyTypeEntity.setMobile_Manufactured("");
		}
		if (mPropertyTypeEntity.getFarm_Ranch().equalsIgnoreCase("Farm/Ranch")) {
			mFarmRanch.setBackgroundResource(R.drawable.tick_on);
			boolFarmRanch = false;
			mPropertyTypeList.add("Farm/Ranch");
			mPropertyTypeEntity.setFarm_Ranch("Farm/Ranch");

		} else {
			mFarmRanch.setBackgroundResource(R.drawable.tick_off);
			boolFarmRanch = true;
			mPropertyTypeList.remove("Farm/Ranch");
			mPropertyTypeEntity.setFarm_Ranch("");

		}
		if (mPropertyTypeEntity.getLot_land().equalsIgnoreCase("Lot/land")) {
			mLotLand.setBackgroundResource(R.drawable.tick_on);
			boolLotLand = false;
			mPropertyTypeList.add("Lot/land");
			mPropertyTypeEntity.setLot_land("Lot/land");

		} else {
			mLotLand.setBackgroundResource(R.drawable.tick_off);
			boolLotLand = true;
			mPropertyTypeList.remove("Lot/land");
			mPropertyTypeEntity.setLot_land("");

		}
		if (mPropertyTypeEntity.getMulti_Family().equalsIgnoreCase(
				"Multi-Family")) {
			mMultiFamily.setBackgroundResource(R.drawable.tick_on);
			boolMultiFamily = false;
			mPropertyTypeList.add("Multi-Family");
			mPropertyTypeEntity.setMulti_Family("Multi-Family");

		} else {
			mMultiFamily.setBackgroundResource(R.drawable.tick_off);
			boolMultiFamily = true;
			mPropertyTypeList.remove("Multi-Family");
			mPropertyTypeEntity.setMulti_Family("");
		}
		if (mPropertyTypeEntity.getIncome_Investment().equalsIgnoreCase(
				"Income/Investment")) {
			mIncomeInvestment.setBackgroundResource(R.drawable.tick_on);
			boolIncomeInvestment = false;
			mPropertyTypeList.add("Income/Investment");
			mPropertyTypeEntity.setIncome_Investment("Income/Investment");

		} else {
			mIncomeInvestment.setBackgroundResource(R.drawable.tick_off);
			boolIncomeInvestment = true;
			mPropertyTypeList.remove("Income/Investment");
			mPropertyTypeEntity.setIncome_Investment("");
		}
		if (mPropertyTypeEntity.getHouseboat().equalsIgnoreCase("Houseboat")) {
			mHouseBoat.setBackgroundResource(R.drawable.tick_on);
			boolHouseBoat = false;
			mPropertyTypeList.add("Houseboat");
			mPropertyTypeEntity.setHouseboat("Houseboat");
		} else {
			mHouseBoat.setBackgroundResource(R.drawable.tick_off);
			boolHouseBoat = true;
			mPropertyTypeList.remove("Houseboat");
			mPropertyTypeEntity.setHouseboat("");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			finish();
			break;
		case R.id.cancel:
			finish();
			break;
		case R.id.set:
			if (mPropertyTypeList.size() == 0) {

				SplashScreen.mAgentFilterLocalEntity.setProperty_experties("");
			} else {
				String mString = mPropertyTypeList.toString();
				String propertyType = mString.replace("[", "");
				String propertyType1 = propertyType.replace("]", "");
				SplashScreen.mAgentFilterLocalEntity
						.setProperty_experties(propertyType1);
			}
			finish();
			AppConstants.isAPI = true;
			break;
		case R.id.all_types_lay:
			if (boolAllTypes) {
				mPropertyTypeList.clear();
				mPropertyTypeEntity.setAllTypes("All Types");
				mAllTypes.setBackgroundResource(R.drawable.tick_on);
				mSingleFamilyHome.setBackgroundResource(R.drawable.tick_on);
				mCondo.setBackgroundResource(R.drawable.tick_on);
				mTownhouse.setBackgroundResource(R.drawable.tick_on);
				mCoop.setBackgroundResource(R.drawable.tick_on);
				mApartment.setBackgroundResource(R.drawable.tick_on);
				mLoft.setBackgroundResource(R.drawable.tick_on);
				mTIC.setBackgroundResource(R.drawable.tick_on);
				mApartmentCondo.setBackgroundResource(R.drawable.tick_on);
				mMobileManufactured.setBackgroundResource(R.drawable.tick_on);
				mFarmRanch.setBackgroundResource(R.drawable.tick_on);
				mLotLand.setBackgroundResource(R.drawable.tick_on);
				mMultiFamily.setBackgroundResource(R.drawable.tick_on);
				mIncomeInvestment.setBackgroundResource(R.drawable.tick_on);
				mHouseBoat.setBackgroundResource(R.drawable.tick_on);

				boolAllTypes = false;
				boolSingleFamilyHome = false;
				boolCondo = false;
				boolTownhouse = false;
				boolCoop = false;
				boolApartment = false;
				boolLoft = false;
				boolTIC = false;
				boolApartmentCondo = false;
				boolMobileManufactured = false;
				boolFarmRanch = false;
				boolLotLand = false;
				boolMultiFamily = false;
				boolIncomeInvestment = false;
				boolHouseBoat = false;

			} else {
				mPropertyTypeList.clear();
				mPropertyTypeEntity.setAllTypes("Select");
				mPropertyTypeEntity.setSingle_Family_Home("");
				mPropertyTypeEntity.setCondo("");
				mPropertyTypeEntity.setTownhouse("");
				mPropertyTypeEntity.setCoop("");
				mPropertyTypeEntity.setApartment("");
				mPropertyTypeEntity.setLoft("");
				mPropertyTypeEntity.setTIC("");
				mPropertyTypeEntity.setApartment_Condo_Townhouse("");
				mPropertyTypeEntity.setMobile_Manufactured("");
				mPropertyTypeEntity.setFarm_Ranch("");
				mPropertyTypeEntity.setLot_land("");
				mPropertyTypeEntity.setMulti_Family("");
				mPropertyTypeEntity.setIncome_Investment("");
				mPropertyTypeEntity.setHouseboat("");
				mAllTypes.setBackgroundResource(R.drawable.tick_off);
				mSingleFamilyHome.setBackgroundResource(R.drawable.tick_off);
				mCondo.setBackgroundResource(R.drawable.tick_off);
				mTownhouse.setBackgroundResource(R.drawable.tick_off);
				mCoop.setBackgroundResource(R.drawable.tick_off);
				mApartment.setBackgroundResource(R.drawable.tick_off);
				mLoft.setBackgroundResource(R.drawable.tick_off);
				mTIC.setBackgroundResource(R.drawable.tick_off);
				mApartmentCondo.setBackgroundResource(R.drawable.tick_off);
				mMobileManufactured.setBackgroundResource(R.drawable.tick_off);
				mFarmRanch.setBackgroundResource(R.drawable.tick_off);
				mLotLand.setBackgroundResource(R.drawable.tick_off);
				mMultiFamily.setBackgroundResource(R.drawable.tick_off);
				mIncomeInvestment.setBackgroundResource(R.drawable.tick_off);
				mHouseBoat.setBackgroundResource(R.drawable.tick_off);

				boolAllTypes = true;
				boolSingleFamilyHome = true;
				boolCondo = true;
				boolTownhouse = true;
				boolCoop = true;
				boolApartment = true;
				boolLoft = true;
				boolTIC = true;
				boolApartmentCondo = true;
				boolMobileManufactured = true;
				boolFarmRanch = true;
				boolLotLand = true;
				boolMultiFamily = true;
				boolIncomeInvestment = true;
				boolHouseBoat = true;
			}
			setAllTypes();
			saveObject();
			break;
		case R.id.single_family_lay:
			if (boolSingleFamilyHome) {
				mPropertyTypeEntity.setSingle_Family_Home("Single-Family Home");
				mSingleFamilyHome.setBackgroundResource(R.drawable.tick_on);
				boolSingleFamilyHome = false;
				mPropertyTypeList.add("Single-Family Home");

			} else {
				mSingleFamilyHome.setBackgroundResource(R.drawable.tick_off);
				boolSingleFamilyHome = true;
				mPropertyTypeList.remove("Single-Family Home");
				mPropertyTypeEntity.setSingle_Family_Home("");
			}
			setAllTypes();
			saveObject();
			break;
		case R.id.condo_lay:
			if (boolCondo) {
				mCondo.setBackgroundResource(R.drawable.tick_on);
				boolCondo = false;
				mPropertyTypeList.add("Condo");
				mPropertyTypeEntity.setCondo("Condo");

			} else {
				mCondo.setBackgroundResource(R.drawable.tick_off);
				boolCondo = true;
				mPropertyTypeList.remove("Condo");
				mPropertyTypeEntity.setCondo("");

			}
			saveObject();
			setAllTypes();
			break;
		case R.id.town_house_lay:
			if (boolTownhouse) {
				mTownhouse.setBackgroundResource(R.drawable.tick_on);
				boolTownhouse = false;
				mPropertyTypeList.add("Townhouse");
				mPropertyTypeEntity.setTownhouse("Townhouse");
			} else {
				mTownhouse.setBackgroundResource(R.drawable.tick_off);
				boolTownhouse = true;
				mPropertyTypeList.remove("Townhouse");
				mPropertyTypeEntity.setTownhouse("");

			}
			setAllTypes();
			saveObject();
			break;
		case R.id.coop_lay:
			if (boolCoop) {
				mCoop.setBackgroundResource(R.drawable.tick_on);
				boolCoop = false;
				mPropertyTypeList.add("Coop");
				mPropertyTypeEntity.setCoop("Coop");

			} else {
				mCoop.setBackgroundResource(R.drawable.tick_off);
				boolCoop = true;
				mPropertyTypeList.remove("Coop");
				mPropertyTypeEntity.setCoop("");

			}
			setAllTypes();
			saveObject();
			break;
		case R.id.apartment_lay:
			if (boolApartment) {
				mApartment.setBackgroundResource(R.drawable.tick_on);
				boolApartment = false;
				mPropertyTypeList.add("Apartment");
				mPropertyTypeEntity.setApartment("Apartment");

			} else {
				mApartment.setBackgroundResource(R.drawable.tick_off);
				boolApartment = true;
				mPropertyTypeList.remove("Apartment");
				mPropertyTypeEntity.setApartment("");

			}
			setAllTypes();
			saveObject();
			break;
		case R.id.loft_lay:
			if (boolLoft) {
				mLoft.setBackgroundResource(R.drawable.tick_on);
				boolLoft = false;
				mPropertyTypeList.add("Loft");
				mPropertyTypeEntity.setLoft("Loft");

			} else {
				mLoft.setBackgroundResource(R.drawable.tick_off);
				boolLoft = true;
				mPropertyTypeList.remove("Loft");
				mPropertyTypeEntity.setLoft("");

			}
			setAllTypes();
			saveObject();
			break;
		case R.id.tic_lay:
			if (boolTIC) {
				mTIC.setBackgroundResource(R.drawable.tick_on);
				boolTIC = false;
				mPropertyTypeList.add("TIC");
				mPropertyTypeEntity.setTIC("TIC");

			} else {
				mTIC.setBackgroundResource(R.drawable.tick_off);
				boolTIC = true;
				mPropertyTypeList.remove("TIC");
				mPropertyTypeEntity.setTIC("");

			}
			setAllTypes();
			saveObject();
			break;
		case R.id.apartment_condo_lay:
			if (boolApartmentCondo) {
				mApartmentCondo.setBackgroundResource(R.drawable.tick_on);
				boolApartmentCondo = false;
				mPropertyTypeList.add("Apartment/Condo/Townhouse");
				mPropertyTypeEntity
						.setApartment_Condo_Townhouse("Apartment/Condo/Townhouse");

			} else {
				mApartmentCondo.setBackgroundResource(R.drawable.tick_off);
				boolApartmentCondo = true;
				mPropertyTypeList.remove("Apartment/Condo/Townhouse");
				mPropertyTypeEntity.setApartment_Condo_Townhouse("");

			}
			setAllTypes();
			saveObject();
			break;
		case R.id.mobile_manufactured_lay:
			if (boolMobileManufactured) {
				mMobileManufactured.setBackgroundResource(R.drawable.tick_on);
				boolMobileManufactured = false;
				mPropertyTypeList.add("Mobile/Manufactured");
				mPropertyTypeEntity
						.setMobile_Manufactured("Mobile/Manufactured");

			} else {
				mMobileManufactured.setBackgroundResource(R.drawable.tick_off);
				boolMobileManufactured = true;
				mPropertyTypeList.remove("Mobile/Manufactured");
				mPropertyTypeEntity.setMobile_Manufactured("");
			}
			setAllTypes();
			saveObject();
			break;
		case R.id.farm_ranch_lay:
			if (boolFarmRanch) {
				mFarmRanch.setBackgroundResource(R.drawable.tick_on);
				boolFarmRanch = false;
				mPropertyTypeList.add("Farm/Ranch");
				mPropertyTypeEntity.setFarm_Ranch("Farm/Ranch");

			} else {
				mFarmRanch.setBackgroundResource(R.drawable.tick_off);
				boolFarmRanch = true;
				mPropertyTypeList.remove("Farm/Ranch");
				mPropertyTypeEntity.setFarm_Ranch("");

			}
			setAllTypes();
			saveObject();
			break;
		case R.id.lot_land_lay:
			if (boolLotLand) {
				mLotLand.setBackgroundResource(R.drawable.tick_on);
				boolLotLand = false;
				mPropertyTypeList.add("Lot/land");
				mPropertyTypeEntity.setLot_land("Lot/land");

			} else {
				mLotLand.setBackgroundResource(R.drawable.tick_off);
				boolLotLand = true;
				mPropertyTypeList.remove("Lot/land");
				mPropertyTypeEntity.setLot_land("");

			}
			setAllTypes();
			saveObject();
			break;
		case R.id.multi_family_lay:
			if (boolMultiFamily) {
				mMultiFamily.setBackgroundResource(R.drawable.tick_on);
				boolMultiFamily = false;
				mPropertyTypeList.add("Multi-Family");
				mPropertyTypeEntity.setMulti_Family("Multi-Family");

			} else {
				mMultiFamily.setBackgroundResource(R.drawable.tick_off);
				boolMultiFamily = true;
				mPropertyTypeList.remove("Multi-Family");
				mPropertyTypeEntity.setMulti_Family("");
			}
			setAllTypes();
			saveObject();
			break;
		case R.id.income_investment_lay:
			if (boolIncomeInvestment) {
				mIncomeInvestment.setBackgroundResource(R.drawable.tick_on);
				boolIncomeInvestment = false;
				mPropertyTypeList.add("Income/Investment");
				mPropertyTypeEntity.setIncome_Investment("Income/Investment");

			} else {
				mIncomeInvestment.setBackgroundResource(R.drawable.tick_off);
				boolIncomeInvestment = true;
				mPropertyTypeList.remove("Income/Investment");
				mPropertyTypeEntity.setIncome_Investment("");
			}
			setAllTypes();
			saveObject();
			break;
		case R.id.houseboat_lay:
			if (boolHouseBoat) {
				mHouseBoat.setBackgroundResource(R.drawable.tick_on);
				boolHouseBoat = false;
				mPropertyTypeList.add("Houseboat");
				mPropertyTypeEntity.setHouseboat("Houseboat");
			} else {
				mHouseBoat.setBackgroundResource(R.drawable.tick_off);
				boolHouseBoat = true;
				mPropertyTypeList.remove("Houseboat");
				mPropertyTypeEntity.setHouseboat("");
			}
			setAllTypes();
			saveObject();
			break;
		}

	}

	private void saveObject() {
		GlobalMethods.saveObject(this, mPropertyTypeEntity,
				AppConstants.HOME_RES_OBJ_KEY);
	}

	private void setAllTypes() {
		if (boolSingleFamilyHome == false && boolCondo == false
				&& boolTownhouse == false && boolCoop == false
				&& boolApartment == false && boolLoft == false
				&& boolTIC == false && boolApartmentCondo == false
				&& boolMobileManufactured == false && boolFarmRanch == false
				&& boolLotLand == false && boolMultiFamily == false
				&& boolIncomeInvestment == false && boolHouseBoat == false) {
			mAllTypes.setBackgroundResource(R.drawable.tick_on);
			boolAllTypes = false;
			mPropertyTypeEntity.setAllTypes("All Types");
			mPropertyTypeList.clear();
		} else if (boolSingleFamilyHome == true && boolCondo == true
				&& boolTownhouse == true && boolCoop == true
				&& boolApartment == true && boolLoft == true && boolTIC == true
				&& boolApartmentCondo == true && boolMobileManufactured == true
				&& boolFarmRanch == true && boolLotLand == true
				&& boolMultiFamily == true && boolIncomeInvestment == true
				&& boolHouseBoat == true) {
			mPropertyTypeList.clear();
			mAllTypes.setBackgroundResource(R.drawable.tick_off);
			boolAllTypes = true;
			mPropertyTypeEntity.setAllTypes("");
		} else {
			mAllTypes.setBackgroundResource(R.drawable.tick_off);
			boolAllTypes = true;
			mPropertyTypeEntity.setAllTypes("");
		}
	}
}
