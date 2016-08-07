package com.omahagdg.dagger2demo.forecast;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.dagger.Injector;
import com.omahagdg.dagger2demo.main.WeatherState;
import com.omahagdg.dagger2demo.utils.ResourcesHelper;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ForecastItemView extends CardView {

    @Inject SimpleDateFormat simpleDateFormat;
    @Inject ForecastPresenter presenter;

    @BindView(R.id.tv_date) TextView dateText;
    @BindView(R.id.tv_temp_min) TextView tempMinText;
    @BindView(R.id.tv_temp_max) TextView tempMaxText;

    private ForecastResponse.Day day;

    public ForecastItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.forecast_item, this, true);
        ButterKnife.bind(this);
        Injector.<ForecastComponent>getComponent(context).inject(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        int margin = (int) context.getResources().getDimension(R.dimen.default_padding);
        params.setMargins(margin, margin / 2, margin, margin / 2);
        setLayoutParams(params);
        setRadius(context.getResources().getDimension(R.dimen.card_corner_radius));
    }

    void setData(ForecastResponse.Day day) {
        this.day = day;
        dateText.setText(simpleDateFormat.format(day.getDate()));
        tempMinText.setText(getContext().getString(R.string.low_temp_format, day.temp().min()));
        tempMaxText.setText(getContext().getString(R.string.high_temp_format, day.temp().max()));
        WeatherState weatherState = WeatherState.fromApiCode(day.weather().get(0).icon());
        setCardBackgroundColor(ResourcesHelper.getColor(getContext(), weatherState.colorRes));
    }

    @OnClick void clicked() {
        presenter.daySelected(day, true);
    }
}
