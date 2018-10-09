package com.smaat.renterblock.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AlertFilterObjectEntity;
import com.smaat.renterblock.entity.AlertsEntity;
import com.smaat.renterblock.entity.AlertsResultEntity;
import com.smaat.renterblock.entity.FilterEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TabFilterFragment extends BaseFragment {

    private int[] tabTitle = {
            R.string.for_rent,
            R.string.for_sale};

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_lay)
    TabLayout mTabLayout;
    /*used var in alert API purpose*/
    private int mSelectedPos = 0;
    private String alertObj = "";
    private String type = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_tab_filter_main, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);

        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    default:
                        v.performClick();
                }
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
          /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(false);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);


            if (AppConstants.CALLED_FROM_ALERT_FRAG && !AppConstants.ALERT_ENTITY.isEditbool()) {
                ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.add_icon, 1);
            } else {
                ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.mipmap.app_icon, 2);
            }


            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 0);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.mipmap.app_icon, 0,"");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);


            if (AppConstants.CALLED_FROM_ALERT_FRAG && AppConstants.ALERT_ENTITY.isEditbool()) {
                ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.save), 1);
            } else {
                ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);
            }



            /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.filter_options), 1);
            ((HomeScreen) getActivity()).setHeaderEdt(getString(R.string.app_name), 0);

            /*header click on edit  old alert*/

            ((HomeScreen) getActivity()).mHeaderRightThirdTxtLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() != null) {
                        setFilterObjectForAlert();

                        DialogManager.getInstance().alertNameDialog(getActivity(), AppConstants.ALERT_ENTITY.getAlert_name(), new InterfaceEdtWithBtnCallback() {
                            @Override
                            public void onFirstEdtBtnClick(String firstEdtStr) {
                                if (!firstEdtStr.isEmpty()) {

                                    APIRequestHandler.getInstance().alertCreateAPICall(TabFilterFragment.this, AppConstants.ALERT_ENTITY.getAlert_id(), firstEdtStr, type, alertObj);
                                    AppConstants.ALERT_ENTITY = new AlertsResultEntity();

                                }
                            }

                        });
                    }
                }
            });


            /*header click on new Alert creation*/
            ((HomeScreen) getActivity()).mHeaderRightFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (getActivity() != null) {

                        if (AppConstants.CALLED_FROM_ALERT_FRAG) {

                            setFilterObjectForAlert();


                            DialogManager.getInstance().alertNameDialog(getActivity(), AppConstants.ALERT_ENTITY.getAlert_name(), new InterfaceEdtWithBtnCallback() {
                                @Override
                                public void onFirstEdtBtnClick(String firstEdtStr) {
                                    if (!firstEdtStr.isEmpty()) {

                                        APIRequestHandler.getInstance().alertCreateAPICall(TabFilterFragment.this, "", firstEdtStr, type, alertObj);

                                    }

                                }
                            });


                        }
                    }
                }
            });

            setupViewPager(mViewPager);
            setListener();
        }
    }

    private void setFilterObjectForAlert() {
        switch (mSelectedPos) {
            case 0:
                type = AppConstants.RENT;
                break;
            case 1:
                type = AppConstants.SALE;
                break;

        }
        FilterEntity rentFilter = AppConstants.RENT_FILTER_ENTITY;

        FilterEntity alertRentObj = new FilterEntity();

        alertRentObj.setDistance("1000");
        alertRentObj.setLatitude(AppConstants.ALERT_ENTITY.getLatitude());
        alertRentObj.setLongitude(AppConstants.ALERT_ENTITY.getLongitude());
        alertRentObj.setLocation(AppConstants.ALERT_ENTITY.getLocation());
        alertRentObj.setNew_construction(rentFilter.getNew_construction());
        alertRentObj.setLot_size(rentFilter.getLot_size());
        alertRentObj.setDays_on_RB(rentFilter.getDays_on_RB());
        alertRentObj.setFore_closure(rentFilter.getFore_closure());
        alertRentObj.setBeds(rentFilter.getBeds());
        alertRentObj.setBaths(rentFilter.getBaths());
        alertRentObj.setKeywords(rentFilter.getKeywords());
        alertRentObj.setNo_fee(rentFilter.getNo_fee());
        alertRentObj.setMLS(rentFilter.getMLS());
        alertRentObj.setProperty_type(rentFilter.getProperty_type());
        alertRentObj.setReduced_prices(rentFilter.getReduced_prices());
        alertRentObj.setSold_within(rentFilter.getSold_within());
        alertRentObj.setPrice_range_min(rentFilter.getPrice_range_min());
        alertRentObj.setPrice_range_max(rentFilter.getPrice_range_max());
        alertRentObj.setSquare_footage_min(rentFilter.getSquare_footage_min());
        alertRentObj.setSquare_footage_max(rentFilter.getSquare_footage_max());
        alertRentObj.setYear_build_min(rentFilter.getYear_build_min());
        alertRentObj.setYear_build_max(rentFilter.getYear_build_max());
        alertRentObj.setResale(rentFilter.getResale());
        alertRentObj.setOpen_house(rentFilter.getOpen_house());


        FilterEntity saleFilter = AppConstants.SALE_FILTER_ENTITY;

        FilterEntity alertSaleObj = new FilterEntity();
        alertSaleObj.setDistance("1000");
        alertSaleObj.setLatitude(AppConstants.ALERT_ENTITY.getLatitude());
        alertSaleObj.setLongitude(AppConstants.ALERT_ENTITY.getLongitude());
        alertSaleObj.setLocation(AppConstants.ALERT_ENTITY.getLocation());
        alertSaleObj.setNew_construction(saleFilter.getNew_construction());
        alertSaleObj.setLot_size(saleFilter.getLot_size());
        alertSaleObj.setDays_on_RB(saleFilter.getDays_on_RB());
        alertSaleObj.setFore_closure(saleFilter.getFore_closure());
        alertSaleObj.setBeds(saleFilter.getBeds());
        alertSaleObj.setBaths(saleFilter.getBaths());
        alertSaleObj.setKeywords(saleFilter.getKeywords());
        alertSaleObj.setNo_fee(saleFilter.getNo_fee());
        alertSaleObj.setMLS(saleFilter.getMLS());
        alertSaleObj.setProperty_type(saleFilter.getProperty_type());
        alertSaleObj.setReduced_prices(saleFilter.getReduced_prices());
        alertSaleObj.setSold_within(saleFilter.getSold_within());
        alertSaleObj.setPrice_range_min(saleFilter.getPrice_range_min());
        alertSaleObj.setPrice_range_max(saleFilter.getPrice_range_max());
        alertSaleObj.setSquare_footage_min(saleFilter.getSquare_footage_min());
        alertSaleObj.setSquare_footage_max(saleFilter.getSquare_footage_max());
        alertSaleObj.setYear_build_min(saleFilter.getYear_build_min());
        alertSaleObj.setYear_build_max(saleFilter.getYear_build_max());
        alertSaleObj.setResale(saleFilter.getResale());
        alertSaleObj.setOpen_house(saleFilter.getOpen_house());


        AlertFilterObjectEntity alertObjEntity = new AlertFilterObjectEntity();
        alertObjEntity.setSale(alertSaleObj);
        alertObjEntity.setRent(alertRentObj);
        alertObj = new Gson().toJson(alertObjEntity);
    }

    private void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                mSelectedPos = position;


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new RentFilterFragment());
        adapter.addFragment(new SaleFilterFragment());

        viewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText(tabTitle[0]);
        mTabLayout.getTabAt(1).setText(tabTitle[1]);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof CommonResponse) {
            CommonResponse mAlertResponse = (CommonResponse) resObj;
            if (mAlertResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), mAlertResponse.getMsg(), this);
                AppConstants.CALLED_FROM_ALERT_FRAG = false;
                ((HomeScreen) getActivity()).onBackPressed();
            }

        }
    }
}
