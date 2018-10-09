package com.smaat.ipharma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2/8/2017.
 */

public class OfferFragment extends BaseFragment {

    View m_profileRootView;

    @BindView(R.id.parent_layout)
    LinearLayout m_linParent;

    @BindView(R.id.paymentscreen)
    ImageView m_paymentScreen;


    @BindView(R.id.offerscreen)
    ImageView m_offerScreen;

    @BindView(R.id.offers_text)
    TextView m_OffersText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        m_profileRootView = inflater.inflate(R.layout.ui_paymentscreen, container, false);
        ButterKnife.bind(this, m_profileRootView);
        setupUI(m_linParent);
        //m_paymentScreen.setImageResource(R.drawable.offers_screen);
        m_paymentScreen.setVisibility(View.GONE);
        m_offerScreen.setVisibility(View.VISIBLE);

        m_OffersText.setText(R.string.no_offers);
        return m_profileRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.offers),getString(R.string.home));
    }

}
