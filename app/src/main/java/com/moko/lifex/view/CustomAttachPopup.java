package com.moko.lifex.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.core.AttachPopupView;
import com.moko.lifex.AppConstants;
import com.moko.lifex.R;
import com.moko.lifex.activity.MoreActivity;
import com.moko.lifex.activity.EnergyPlugSettingActivity;
import com.moko.lifex.entity.MokoDevice;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


public class CustomAttachPopup extends AttachPopupView {
    private MokoDevice mokoDevice;

    public CustomAttachPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_more;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tv_more).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转更多
                if (mokoDevice != null) {
                    Intent intent = new Intent(getContext(), MoreActivity.class);
                    intent.putExtra(AppConstants.EXTRA_KEY_DEVICE, mokoDevice);
                    getContext().startActivity(intent);
                    dismiss();
                }
            }
        });
        findViewById(R.id.tv_setting).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转设置
                if (mokoDevice != null) {
                    Intent intent = new Intent(getContext(), EnergyPlugSettingActivity.class);
                    intent.putExtra(AppConstants.EXTRA_KEY_DEVICE, mokoDevice);
                    getContext().startActivity(intent);
                    dismiss();
                }
            }
        });
        findViewById(R.id.ll_bg).setBackgroundResource(mokoDevice.on_off ?
                R.drawable.popup_bg : R.drawable.popup_bg_white);
        ((TextView) findViewById(R.id.tv_more)).setTextColor(
                ContextCompat.getColor(getContext(),
                        mokoDevice.on_off ? R.color.white_fffefe : R.color.black_333333));
        ((TextView) findViewById(R.id.tv_setting)).setTextColor(
                ContextCompat.getColor(getContext(),
                        mokoDevice.on_off ? R.color.white_fffefe : R.color.black_333333));
    }

    public void setData(MokoDevice mokoDevice) {
        this.mokoDevice = mokoDevice;
    }

    public void setBg(boolean on_off) {
        findViewById(R.id.ll_bg).setBackgroundResource(on_off ?
                R.drawable.popup_bg : R.drawable.popup_bg_white);
        ((TextView) findViewById(R.id.tv_more)).setTextColor(
                ContextCompat.getColor(getContext(),
                        on_off ? R.color.white_fffefe : R.color.black_333333));
        ((TextView) findViewById(R.id.tv_setting)).setTextColor(
                ContextCompat.getColor(getContext(),
                        on_off ? R.color.white_fffefe : R.color.black_333333));
    }
}
