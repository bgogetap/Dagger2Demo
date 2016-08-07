package com.omahagdg.dagger2demo.forecast;

import android.content.Context;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.dagger.Injector;
import com.omahagdg.dagger2demo.utils.ResourcesHelper;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayDetailView extends CardView {

    @Inject SimpleDateFormat simpleDateFormat;

    @BindView(R.id.tv_date) TextView dateText;
    @BindView(R.id.tv_day) TextView dayText;
    @BindView(R.id.tv_high) TextView highText;
    @BindView(R.id.tv_low) TextView lowText;
    @BindView(R.id.tv_humidity) TextView humidityText;
    @BindView(R.id.tv_morning) TextView morningText;
    @BindView(R.id.tv_evening) TextView eveningText;
    @BindView(R.id.tv_night) TextView nightText;
    @BindView(R.id.tv_description) TextView weatherDescription;

    public DayDetailView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_day, this, true);
        ButterKnife.bind(this);
        Injector.<ForecastComponent>getComponent(context).inject(this);
        setRadius(context.getResources().getDimension(R.dimen.card_corner_radius));
        setCardBackgroundColor(ResourcesHelper.getColor(context, R.color.colorPrimaryLight));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(context.getResources().getDimension(R.dimen.day_detail_elevation));
        }
    }

    void setDay(ForecastResponse.Day day) {
        dateText.setText(simpleDateFormat.format(day.getDate()));
        weatherDescription.setText(day.weather().get(0).description());
        highText.setText(getDegreeString(R.string.high, day.temp().max()));
        lowText.setText(getDegreeString(R.string.low, day.temp().min()));
        morningText.setText(getDegreeString(R.string.morning, day.temp().morn()));
        dayText.setText(getDegreeString(R.string.day, day.temp().day()));
        eveningText.setText(getDegreeString(R.string.evening, day.temp().eve()));
        nightText.setText(getDegreeString(R.string.night, day.temp().night()));
        humidityText.setText(getContext().getString(R.string.humidity_format, day.humidity()));
    }

    private String getDegreeString(@StringRes int descriptorRes, double temp) {
        String descriptor = getContext().getString(descriptorRes);
        return getContext().getString(R.string.descriptor_with_degree_format, descriptor, temp);
    }
}
