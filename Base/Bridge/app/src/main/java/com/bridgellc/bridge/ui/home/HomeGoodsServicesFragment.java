package com.bridgellc.bridge.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.stickygridview.StickyGridHeaderAdapter;
import com.bridgellc.bridge.stickygridview.StickyGridHeadersGridView;
import com.bridgellc.bridge.ui.ProductDetailsBuyNeg;
import com.bridgellc.bridge.ui.ProductDetailsScreen;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by USER on 3/11/2016.
 */
public class HomeGoodsServicesFragment extends Fragment {



    private View view;
    private StickyGridHeadersGridView mHomeGridView;

    private ArrayList<HomeSingleItemEntity> mArrayList = new ArrayList<HomeSingleItemEntity>();
    private ArrayList<HomeSingleItemEntity> mHomeListRes = new ArrayList<HomeSingleItemEntity>();

    private LinkedHashMap<String, ArrayList<HomeSingleItemEntity>> mDateWiseSeparatedList = new LinkedHashMap<String, ArrayList<HomeSingleItemEntity>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_grid, container, false);

        if (getArguments() != null && getArguments().containsKey("HomeDataList")) {
            mArrayList = (ArrayList<HomeSingleItemEntity>) getArguments().get("HomeDataList");
            mHomeGridView = (StickyGridHeadersGridView) view.findViewById(R.id.asset_grid);

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }


    private LinkedHashMap<String, ArrayList<HomeSingleItemEntity>> getDateWiseSeparatedList(ArrayList<HomeSingleItemEntity> list) {
        LinkedHashMap<String, ArrayList<HomeSingleItemEntity>> localList = new LinkedHashMap<String, ArrayList<HomeSingleItemEntity>>();

        for (HomeSingleItemEntity entity : list) {
            if (localList.containsKey(entity.getCreated_datetime().split(" ")[0])) {
                ArrayList<HomeSingleItemEntity> enList = localList.get(entity.getCreated_datetime().split(" ")[0]);
                enList.add(entity);
                localList.put(entity.getCreated_datetime().split(" ")[0], enList);
            } else {
                ArrayList<HomeSingleItemEntity> enList = new ArrayList<HomeSingleItemEntity>();
                enList.add(entity);
                localList.put(entity.getCreated_datetime().split(" ")[0], enList);
            }
        }

        return localList;
    }

    private void initComponents() {

        // mParentLayout = (LinearLayout) view.findViewById(R.id.parent_lay);

        if (mArrayList != null) {


//
//
//            mDateWiseSeparatedList = getDateWiseSeparatedList(mArrayList);
//
//            ArrayList<String> keystrings = new ArrayList<String>();
//
//            for (String key : mDateWiseSeparatedList.keySet()
//                    ) {
//                keystrings.add(key);
//            }
//            Log.d("size",""+mDateWiseSeparatedList.size());
//
//            int count=mDateWiseSeparatedList.keySet().size();
//            for (int i = 0; i < 1; i++) {
//                LayoutInflater view = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View v = view.inflate(R.layout.home_screen_fragment_sub_layout, null);
//
//                TextView textView = (TextView) v.findViewById(R.id.date_view);
//
//
//
//                textView.setText("some");
//


            mHomeListRes.removeAll(mHomeListRes);

            for (int j = 0; j < mArrayList.size(); j++) {
                HomeSingleItemEntity headerobj = mArrayList.get(j);
                Calendar cal = Calendar.getInstance();

                SimpleDateFormat month_date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                Date d = new Date();
                try {
                    d = month_date1.parse(mArrayList.get(j).getCreated_datetime().split(" ")[0]);
                } catch (Exception e) {

                }

                cal.setTime(d);

                SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.US);

                String month_name = month_date.format(cal.getTime());

                String totalString = month_name + " " + cal.get(Calendar.DATE) + ", " + cal.get(Calendar.YEAR);

                headerobj.setHeaderakey(totalString);
                mHomeListRes.add(headerobj);
            }


            mHomeGridView.setAdapter(new StickyGridHeaderAdapter<String>(getActivity()
                    .getApplicationContext(),
                    R.layout.home_screen_grid_header, mHomeListRes));

            mHomeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long
                        l) {


//                        StickyGridHeaderAdapter.TagClass tagClass = (StickyGridHeaderAdapter.TagClass) view.getTag();

//                        String tag = tagClass.tag;
//                        ProductDetailsBuyNeg.mHomeSingleItemEntity = tagClass.mHomeSingleItemEntity;
                    if ((mHomeListRes.get(pos).getUser_id().equalsIgnoreCase
                            (GlobalMethods.getUserID(getActivity())))) {
//                            ProductDetailsBuyNeg.isGreenRound = tag.split("_")[0].equalsIgnoreCase("b");
//                            ProductDetailsBuyNeg.productImg = tag.split("_")[1];
//                            ProductDetailsBuyNeg.priceAmount = tag.split("_")[2];

                        ProductDetailsScreen.mFooterOneTxt = getString(R.string.edit);
                        ProductDetailsScreen.mFooterBtnCount = 1;
                        ProductDetailsScreen.isFromHome = true;
//                        ProductDetailsScreen.priceAmount = tag.split("_")[2];
                        ProductDetailsScreen.mHomeSingleItemEntity = mHomeListRes.get(pos);
                        ((BaseFragmentActivity) getActivity()).nextScreen(ProductDetailsScreen
                                .class, false);


                    } else {

                        ProductDetailsBuyNeg.mHomeSingleItemEntity = mHomeListRes.get(pos);
                        ((BaseFragmentActivity) getActivity()).nextScreen(ProductDetailsBuyNeg
                                .class, false);

                    }


//                        if(!(tagClass.mHomeSingleItemEntity.getUser_id().equalsIgnoreCase
//                                (GlobalMethods.getUserID(getActivity())))){
//                            ProductDetailsBuyNeg.isGreenRound = tag.split("_")[0].equalsIgnoreCase("b");
//                            ProductDetailsBuyNeg.productImg = tag.split("_")[1];
//                            ProductDetailsBuyNeg.priceAmount = tag.split("_")[2];
//
//
//
//                            ((BaseFragmentActivity) getActivity()).nextScreen(ProductDetailsBuyNeg
//                                    .class, false);}
//                        else{
//                            ProductDetailsScreen.mFooterOneTxt = getString(R.string.edit);
//                            ProductDetailsScreen.mFooterBtnCount = 1;
//                            ProductDetailsScreen.isFromHome = true;
//                            ProductDetailsScreen.priceAmount = tag.split("_")[2];
//                            ProductDetailsScreen.mHomeSingleItemEntity = tagClass
//                                    .mHomeSingleItemEntity;
//                            ((BaseFragmentActivity) getActivity()).nextScreen(ProductDetailsScreen
//                                    .class, false);
//                        }
                }
            });
            HomeScreenActivity.swipeRefreshLayout.setEnabled(true);
            mHomeGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    boolean enable = true;
                    if (mHomeGridView != null && mHomeGridView.getChildCount() > 0) {
                        // check if the first item of the list is visible
                        boolean firstItemVisible = mHomeGridView.getFirstVisiblePosition() == 0;
                        // check if the top of the first item is visible
                        //  boolean topOfFirstItemVisible = mHomeGridView.getChildAt(0).getTop() == 0;
                        // enabling or disabling the refresh layout
                        enable = firstItemVisible;
                    }
                    HomeScreenActivity.swipeRefreshLayout.setEnabled(enable);
                }
            });

            mHomeGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int postion,
                                               long l) {
                    DialogManager.ImageViewer(getActivity(),mHomeListRes.get(postion).getPicture1());

                    return true;
                }
            });


            //   }
        }

    }

    private boolean listIsAtTop() {
        if (mHomeGridView.getChildCount() == 0) return true;
        return mHomeGridView.getChildAt(0).getTop() == 0;
    }

}
