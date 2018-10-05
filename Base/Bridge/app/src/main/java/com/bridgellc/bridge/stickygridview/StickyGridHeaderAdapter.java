
package com.bridgellc.bridge.stickygridview;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.utils.ImageViewRounded;
import com.bridgellc.bridge.utils.TypefaceSingleton;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class StickyGridHeaderAdapter<T> extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private int mHeaderResId;
    private LayoutInflater mInflater;
    private ArrayList<HomeSingleItemEntity> mHomeDataRes;
    private Context mContext;
    private Typeface mLightFont, mRegulartFont;

    public StickyGridHeaderAdapter(Context context, int headerResId, ArrayList<HomeSingleItemEntity> ArrayList) {

        this.mHeaderResId = headerResId;
        this.mHomeDataRes = ArrayList;
        mInflater = LayoutInflater.from(context);
        this.mContext = context;

        this.mLightFont = TypefaceSingleton.getTypeface().getLightFont(context);
        this.mRegulartFont = TypefaceSingleton.getTypeface().getRegularFont(context);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return mHomeDataRes.size();
    }

    @Override
    public String getItem(int position) {
        return mHomeDataRes.get(position).getHeaderakey();
    }


    public CharSequence getHeaderId(int position) {
        return getItem(position);
    }

    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            mHeaderHolder = new HeaderViewHolder(convertView);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }

        mHeaderHolder.mHeadertxt.setText(getItem(position));

        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_grid_item, parent, false);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.mItemCost.setText(mContext.getString(R.string.dollar_sym) + String.format(Locale.ENGLISH,"%.0f", Double
                .parseDouble(mHomeDataRes.get(position).getItem_cost())));
        mHolder.mItemName.setText(mHomeDataRes.get(position).getItem_name());

        Glide.with(mContext)
                .load(mHomeDataRes.get(position).getPicture1())
                .asBitmap().into(mHolder.imageViewRounded);

        if (mHomeDataRes.get(position).getItem_mode().equalsIgnoreCase(mContext.getString(R
                .string.one))) {
            mHolder.mItemCost.setBackgroundResource(R.drawable.home_blue_small);
        } else {
            mHolder.mItemCost.setBackgroundResource(R.drawable.home_green_small);
        }

        return convertView;
    }


    private class HeaderViewHolder {
        private TextView mHeadertxt;

        private HeaderViewHolder(View mView) {
            mHeadertxt = (TextView) mView.findViewById(R.id.grid_header_txt);
            mHeadertxt.setTypeface(mLightFont);
        }
    }

    private class ViewHolder {
        private TextView mItemCost, mItemName;
        private ImageViewRounded imageViewRounded;

        private ViewHolder(View view) {

            mItemCost = (TextView) view.findViewById(R.id.product_price);
            mItemName = (TextView) view.findViewById(R.id.product_name);
            imageViewRounded = (ImageViewRounded) view.findViewById(R.id.product_img);

            mItemCost.setTypeface(mRegulartFont);
            mItemName.setTypeface(mRegulartFont);
        }

    }


}
