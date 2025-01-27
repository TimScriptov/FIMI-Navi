package com.fimi.app.x8s.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fimi.android.app.R;
import com.fimi.kernel.animutils.IOUtils;
import com.fimi.kernel.utils.NumberUtil;

public class X8BatteryLayout extends RelativeLayout {
    private X8BatteryView batteryCoreView;
    private String batteryName;
    private String defaultValue;
    private TextView tvBatteryCoreState;

    public X8BatteryLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.x8_view_battery_layout, null);
        addView(myView);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.X8BatteryLayout);
        this.batteryName = typedArray.getString(R.styleable.X8BatteryLayout_coreName);
        this.defaultValue = typedArray.getString(R.styleable.X8BatteryLayout_defaultValue);
        typedArray.recycle();
        this.batteryCoreView = myView.findViewById(R.id.img_battery_core_view);
        this.tvBatteryCoreState = myView.findViewById(R.id.img_battery_core_state);
        this.tvBatteryCoreState.setText(this.batteryName + IOUtils.LINE_SEPARATOR_UNIX + this.defaultValue);
    }

    public void setData(double curVoltage, double otherVoltage1, double otherVoltage2, int disChargeCnt) {
        int power;
        int state = 0;
        double press1 = curVoltage - otherVoltage1;
        double press2 = curVoltage - otherVoltage2;
        double press3 = otherVoltage1 - otherVoltage2;
        state = 1;
        if (curVoltage <= 3.2d) {
            state = 3;
        }
        if ((2.8d <= curVoltage && curVoltage < 3.0d && disChargeCnt >= 5) || ((2.5d <= curVoltage && curVoltage < 2.8d && disChargeCnt >= 3) || (curVoltage < 2.5d && disChargeCnt >= 1))) {
            state = 4;
        }
        state = 2;
        String tip = "";
        switch (state) {
            case 1:
                tip = String.format(getContext().getString(R.string.x8_battery_setting_format), X8BatteryView.COLOR_ABNORMAL_YELLOW, getContext().getString(R.string.x8_battery_setting_over_pressure));
                break;
            case 2:
                tip = String.format(getContext().getString(R.string.x8_battery_setting_format), X8BatteryView.COLOR_SERIOUS_RED, getContext().getString(R.string.x8_battery_setting_corrupted));
                break;
            case 3:
                tip = String.format(getContext().getString(R.string.x8_battery_setting_format), X8BatteryView.COLOR_ABNORMAL_YELLOW, getContext().getString(R.string.x8_battery_setting_over_release));
                break;
            case 4:
                tip = String.format(getContext().getString(R.string.x8_battery_setting_format), X8BatteryView.COLOR_SERIOUS_RED, getContext().getString(R.string.x8_battery_setting_over_release_serious));
                break;
        }
        String text = this.batteryName + "<br/>" + NumberUtil.decimalPointStr(curVoltage, 2) + "V" + tip;
        this.tvBatteryCoreState.setText(Html.fromHtml(text));
        this.batteryCoreView.setState(state);
        if (curVoltage > 4.35d) {
            power = 100;
        } else if (4.235d < curVoltage && curVoltage <= 4.35d) {
            power = 90;
        } else if (4.12d < curVoltage && curVoltage <= 4.235d) {
            power = 80;
        } else if (4.005d < curVoltage && curVoltage <= 4.12d) {
            power = 70;
        } else if (3.89d < curVoltage && curVoltage <= 4.005d) {
            power = 60;
        } else if (3.775d < curVoltage && curVoltage <= 3.89d) {
            power = 50;
        } else if (3.66d < curVoltage && curVoltage <= 3.775d) {
            power = 40;
        } else if (3.545d < curVoltage && curVoltage <= 3.66d) {
            power = 30;
        } else if (3.43d < curVoltage && curVoltage <= 3.545d) {
            power = 20;
        } else if (3.315d < curVoltage && curVoltage <= 3.43d) {
            power = 10;
        } else if (3.2d < curVoltage && curVoltage <= 3.315d) {
            power = 5;
        } else {
            power = 0;
        }
        this.batteryCoreView.setPower(power);
    }
}
