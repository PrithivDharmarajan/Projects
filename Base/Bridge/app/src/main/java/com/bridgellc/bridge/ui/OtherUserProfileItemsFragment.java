package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.HomeGridAdapter;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.utils.DialogManager;

import java.util.ArrayList;

public class OtherUserProfileItemsFragment extends Fragment {

    private View view;

    private ArrayList<HomeSingleItemEntity> mArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.other_userprofile_gridlayout, container, false);

        if (getArguments() != null && getArguments().containsKey("HomeDataList"))
            mArrayList = (ArrayList<HomeSingleItemEntity>) getArguments().get("HomeDataList");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents();
    }


    private void initComponents() {

        GridView mHomeGridView = (GridView) view.findViewById(R.id.gridview);

        if (mArrayList != null) {

            HomeGridAdapter mHomeAdapter = new HomeGridAdapter(getActivity(), mArrayList);
            mHomeGridView.setAdapter(mHomeAdapter);


            mHomeGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                    OtherUserProductDetails.mHomeSingleItemEntity = mArrayList.get(pos);
                    ((BaseFragmentActivity) getActivity()).nextScreen(OtherUserProductDetails
                            .class, false);
                }
            });

            mHomeGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int Position,
                                               long l) {
                    DialogManager.ImageViewer(getActivity(), mArrayList.get(Position).getPicture1());
                    return true;
                }
            });


        }
    }


}