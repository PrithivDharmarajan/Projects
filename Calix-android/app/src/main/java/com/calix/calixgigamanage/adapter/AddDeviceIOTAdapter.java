package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.AddIOTDeviceEntity;
import com.calix.calixgigamanage.ui.iot.IOTAddZigBeeDevice;
import com.calix.calixgigamanage.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddDeviceIOTAdapter extends RecyclerView.Adapter<AddDeviceIOTAdapter.Holder> {

    private Context mContext;
    private ArrayList<AddIOTDeviceEntity> mCategoriesRes;

    public AddDeviceIOTAdapter(ArrayList<AddIOTDeviceEntity> categoriesRes, Context context) {
        mContext = context;
        mCategoriesRes = categoriesRes;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_add_iot_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {

        AddIOTDeviceEntity categoriesRes = mCategoriesRes.get(position);
        holder.mIOTDeviceNameTxt.setText(categoriesRes.getName());
        holder.mIOTDeviceImg.setImageResource(deviceImg(categoriesRes.getId()));

        /*Item onclick*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.ADD_IOT_DEVICE_DETAILS = mCategoriesRes.get(holder.getAdapterPosition());
                ((BaseActivity) mContext).nextScreen(IOTAddZigBeeDevice.class);

            }
        });

    }


    /*find the device Image*/
    private int deviceImg(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 1:
                return R.drawable.binary_switch;
            case 2:
                return R.drawable.multi_level_switch;
            case 3:
                return R.drawable.binary_sensor;
            case 4:
                return R.drawable.multi_level_switch;
            case 5:
                return R.drawable.door_lock;
            case 6:
                return R.drawable.alarm;
            case 7:
                return R.drawable.thermostat;
//            case 10: return R.drawable.standard_cie;
            case 11:
                return R.drawable.motion_sensor;
            case 12:
                return R.drawable.contact_switch;
            case 13:
                return R.drawable.fire_sensor;
            case 14:
                return R.drawable.water_sensor;
            case 15:
                return R.drawable.gas_sensor;
            case 16:
                return R.drawable.personal_emergency_device;
            case 17:
                return R.drawable.vibration__movement_sensor;
            case 18:
                return R.drawable.remote_control;
            case 19:
                return R.drawable.key_fob;
            case 20:
                return R.drawable.key_pad;
            case 21:
                return R.drawable.standard_warning_device;
            case 22:
                return R.drawable.smart_ac_switch;
            case 24:
                return R.drawable.occupancy_sensor;
            case 25:
                return R.drawable.light_sensor;
            case 26:
                return R.drawable.window_covering;
            case 27:
                return R.drawable.temperature_sensor;
            case 28:
                return R.drawable.zigbee_doorlock;
            case 29:
                return R.drawable.zigbee_temp_sensor;
            case 30:
                return R.drawable.pressure_sensor;
            case 32:
                return R.drawable.color_dimmable_light;
            case 36:
                return R.drawable.smoke_detector;
            case 37:
                return R.drawable.flood_sensor;
            case 38:
                return R.drawable.shock_sensor;
            case 39:
                return R.drawable.door_sensor;
            case 40:
                return R.drawable.motion_sensor;
            case 41:
                return R.drawable.movement_sensor;
            case 42:
                return R.drawable.siren;
            case 43:
                return R.drawable.multi_switch;
            case 44:
                return R.drawable.unknown_on_off_module;
            case 45:
                return R.drawable.binary_power_switch;
            case 46:
                return R.drawable.set_point_thermostat;
            case 48:
                return R.drawable.hue_lamp;
            case 49:
                return R.drawable.multi_sensor;
            case 50:
                return R.drawable.securifi_smart_switch;
            case 52:
                return R.drawable.roller_shutter;
            case 53:
                return R.drawable.garage_door_opener;
//            case 54: return R.drawable.zw_to_acir_extender;
            case 55:
                return R.drawable.multi_sound_siren;
            case 56:
                return R.drawable.energy_reader;
            case 57:
                return R.drawable.nest_thermostat;
            case 58:
                return R.drawable.nest_smoke_detector;
            case 59:
                return R.drawable.notification_sensor;
//            case 60: return R.drawable.generic_device;
            case 61:
                return R.drawable.securifi_button;
            case 62:
                return R.drawable.zen_thermostat;
            case 63:
                return R.drawable.almond_siren;
            case 64:
                return R.drawable.almond_blink;
//            case 65: return R.drawable.zbir_extender;
            default:
                return R.mipmap.ic_launcher;
        }
    }

    @Override
    public int getItemCount() {
        return mCategoriesRes.size();
    }

    public class Holder extends RecyclerView.ViewHolder {


        @BindView(R.id.iot_device_img)
        ImageView mIOTDeviceImg;

        @BindView(R.id.iot_device_name_txt)
        TextView mIOTDeviceNameTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
