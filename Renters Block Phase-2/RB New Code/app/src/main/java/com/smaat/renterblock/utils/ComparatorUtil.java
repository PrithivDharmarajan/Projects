package com.smaat.renterblock.utils;

import com.smaat.renterblock.entity.PropertyEntity;

import java.util.Comparator;
import java.util.Date;


public class ComparatorUtil {

    public static Comparator<PropertyEntity> SQUARE_FEET_SORT = new Comparator<PropertyEntity>() {

        @Override
        public int compare(PropertyEntity arg0, PropertyEntity arg1) {
            String max_price1 = arg0.getSquare_footage();
            String max_price2 = arg1.getSquare_footage();

            return max_price2.compareTo(max_price1);
        }

    };
    public static Comparator<PropertyEntity> RATING_SORT = new Comparator<PropertyEntity>() {

        @Override
        public int compare(PropertyEntity arg0, PropertyEntity arg1) {
            String rating1 = arg0.getProperty_rating();
            String rating2 = arg1.getProperty_rating();

            return rating2.compareTo(rating1);
        }

    };
    public static Comparator<PropertyEntity> PRICE_MIN_TO_MAX_SORT = new Comparator<PropertyEntity>() {

        @Override
        public int compare(PropertyEntity arg0, PropertyEntity arg1) {
            String max_price1 = arg0.getPrice_range();
            String max_price2 = arg1.getPrice_range();

            return max_price1.compareTo(max_price2);
        }

    };

    public static Comparator<PropertyEntity> PRICE_MAX_TO_MIN_SORT = new Comparator<PropertyEntity>() {

        @Override
        public int compare(PropertyEntity arg0, PropertyEntity arg1) {
            String max_price1 = arg0.getPrice_range();
            String max_price2 = arg1.getPrice_range();

            return max_price2.compareTo(max_price1);
        }

    };

    public static Comparator<PropertyEntity> BED_SORT = new Comparator<PropertyEntity>() {

        @Override
        public int compare(PropertyEntity arg0, PropertyEntity arg1) {
            String beds1 = arg0.getBeds();
            String beds2 = arg1.getBeds();

            return beds2.compareTo(beds1);
        }

    };

    public static Comparator<PropertyEntity> BATH_SORT = new Comparator<PropertyEntity>() {

        @Override
        public int compare(PropertyEntity arg0, PropertyEntity arg1) {
            String baths1 = arg0.getBaths();
            String baths2 = arg1.getBaths();

            return baths2.compareTo(baths1);
        }

    };

    public static Comparator<PropertyEntity> DATE_SORT = new Comparator<PropertyEntity>() {

        @Override
        public int compare(PropertyEntity arg0, PropertyEntity arg1) {
            Date date1 = DateUtil.getDate(arg0.getProperty_datetime());
            Date date2 = DateUtil.getDate(arg1.getProperty_datetime());

            return date2.compareTo(date1);
        }

    };

}
