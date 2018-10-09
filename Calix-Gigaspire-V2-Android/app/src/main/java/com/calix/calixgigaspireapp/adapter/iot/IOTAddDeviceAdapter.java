package com.calix.calixgigaspireapp.adapter.iot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.AddIOTDeviceEntity;
import com.calix.calixgigaspireapp.output.model.TempIOTElite;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.iot.ColorDimmableLight;
import com.calix.calixgigaspireapp.ui.iot.IOTAlmondBlink;
import com.calix.calixgigaspireapp.ui.iot.IOTSensor;
import com.calix.calixgigaspireapp.ui.iot.IOTSiren;
import com.calix.calixgigaspireapp.ui.iot.IOTSwitch;
import com.calix.calixgigaspireapp.ui.iot.IOTThermostat;
import com.calix.calixgigaspireapp.ui.iot.ZWACIRExtender;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IOTAddDeviceAdapter extends RecyclerView.Adapter<IOTAddDeviceAdapter.Holder> {

    private Context mContext;
    private ArrayList<AddIOTDeviceEntity> mCategoriesRes;

    public IOTAddDeviceAdapter(ArrayList<AddIOTDeviceEntity> categoriesRes, Context context) {
        mContext = context;
        mCategoriesRes = categoriesRes;
    }

    @NonNull
    @Override
    public IOTAddDeviceAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_add_iot_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final IOTAddDeviceAdapter.Holder holder, int position) {

        AddIOTDeviceEntity categoriesRes = mCategoriesRes.get(position);
        deviceImg(categoriesRes.getId(), position);
        holder.mIOTDeviceNameTxt.setText(categoriesRes.getName());
        holder.mIOTDeviceImg.setImageResource(categoriesRes.getDeviceImage());

        /*Item onclick*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AppConstants.TEMP_IOT_Details = new TempIOTElite();
//                AppConstants.TEMP_IOT_Details.setDeviceType(mCategoriesRes.get(holder.getAdapterPosition()).getId());
//                AppConstants.TEMP_IOT_Details.setDeviceImage(mCategoriesRes.get(holder.getAdapterPosition()).getDeviceImage());
//                ((BaseActivity) mContext).nextScreen(mCategoriesRes.get(holder.getAdapterPosition()).getClass_name());
                DialogManager.getInstance().showAddIOTAlertPopup(mContext, mCategoriesRes.get(holder.getAdapterPosition()).getName(), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {
                    }

                    @Override
                    public void onPositiveClick() {
                        AppConstants.ADD_IOT_DEVICE_DETAILS = mCategoriesRes.get(holder.getAdapterPosition());
                        APIRequestHandler.getInstance().iotDeviceConfigAPICall(String.valueOf(AppConstants.ADD_IOT_DEVICE_DETAILS.getId()), (BaseActivity) mContext);
                    }
                });


            }
        });

    }


    /*find the device Image*/
    private void deviceImg(int deviceTypeInt, int resPosInt) {
        switch (deviceTypeInt) {
            case 1:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSwitch.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.binary_switch);
                break;
            case 2:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSwitch.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.multi_level_switch);
                break;
            case 3:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.binary_sensor);
                break;
            case 4:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSwitch.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.multi_level_switch);
                break;
            case 5:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.door_lock);
                break;
            case 6:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSiren.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.alarm);
                break;
            case 7:
                mCategoriesRes.get(resPosInt).setClass_name(IOTThermostat.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.thermostat);
                break;
//            case 10: return R.drawable.standard_cie;
            case 10:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.thermostat);
                break;
            case 11:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.motion_sensor);
                break;
            case 12:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSwitch.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.contact_switch);
                break;
            case 13:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.fire_sensor);
                break;
            case 14:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.water_sensor);
                break;
            case 15:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.gas_sensor);
                break;
            case 16:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.personal_emergency_device);
                break;
            case 17:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.vibration__movement_sensor);
                break;
            case 18:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.remote_control);
                break;
            case 19:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSiren.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.key_fob);
                break;
            case 20:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.key_pad);
                break;
            case 21:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSiren.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.standard_warning_device);
                break;
            case 22:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSwitch.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.smart_ac_switch);
                break;
            case 24:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.occupancy_sensor);
                break;
            case 25:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.light_sensor);
                break;
            case 26:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.window_covering);
                break;
            case 27:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.temperature_sensor);
                break;
            case 28:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.zigbee_doorlock);
                break;
            case 29:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.zigbee_temp_sensor);
                break;
            case 30:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.pressure_sensor);
                break;
            case 32:
                mCategoriesRes.get(resPosInt).setClass_name(ColorDimmableLight.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.color_dimmable_light);
                break;
            case 36:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.smoke_detector);
                break;
            case 37:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.flood_sensor);
                break;
            case 38:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.shock_sensor);
                break;
            case 39:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.door_sensor);
                break;
            case 40:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.motion_sensor);
                break;
            case 41:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.movement_sensor);
                break;
            case 42:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSiren.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.siren);
                break;
            case 43:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSwitch.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.multi_switch);
                break;
            case 44:
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.unknown_on_off_module);
                break;
            case 45:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSwitch.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.binary_power_switch);
                break;
            case 46:
                mCategoriesRes.get(resPosInt).setClass_name(IOTThermostat.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.set_point_thermostat);
                break;
            case 48:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.hue_lamp);
                break;
            case 49:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.multi_sensor);
                break;
            case 50:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSwitch.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.securifi_smart_switch);
                break;
            case 52:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSiren.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.shutter_icon);
                break;
            case 53:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.garage_door_opener);
                break;
//            case 54: return R.drawable.zw_to_acir_extender;
            case 54:
                mCategoriesRes.get(resPosInt).setClass_name(ZWACIRExtender.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.garage_door_opener);
                break;
            case 55:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSiren.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.multi_sound_siren);
                break;
            case 56:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.energy_reader);
                break;
            case 57:
                mCategoriesRes.get(resPosInt).setClass_name(IOTThermostat.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.nest_thermostat);
                break;
            case 58:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.binary_switch);
                break;
            case 59:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.notification_sensor);
                break;
//            case 60: return R.drawable.generic_device;
            case 60:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.notification_sensor);
                break;
            case 61:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.securifi_button);
                break;
            case 62:
                mCategoriesRes.get(resPosInt).setClass_name(IOTThermostat.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.zen_thermostat);
                break;
            case 63:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSiren.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.almond_siren);
                break;
            case 64:
                mCategoriesRes.get(resPosInt).setClass_name(IOTAlmondBlink.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.almond_blink);
                break;
            case 98:
                mCategoriesRes.get(resPosInt).setClass_name(ColorDimmableLight.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.hue_lamp);
                break;
            case 99:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.almond_siren);
                break;
            case 100:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.flood);
                break;
            case 101:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.smoke_detector_icon);
                break;
//            case 65: return R.drawable.zbir_extender;
            case 65:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.flood);
                break;
            case 66:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.flood);
                break;
            case 67:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.flood);
                break;
            case 68:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSiren.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.flood);
                break;
            case 69:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.flood);
                break;
            case 71:
                mCategoriesRes.get(resPosInt).setClass_name(IOTSensor.class);
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.flood);
                break;
            default:
                mCategoriesRes.get(resPosInt).setDeviceImage(R.drawable.default_iot_device_blue);

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
