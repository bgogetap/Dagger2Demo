package com.omahagdg.dagger2demo.forecast;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.base.MyApplication;
import com.omahagdg.dagger2demo.dagger.ComponentCache;
import com.omahagdg.dagger2demo.dagger.DaggerContextWrapper;
import com.omahagdg.dagger2demo.dagger.Injector;
import com.omahagdg.dagger2demo.dagger.Scoped;
import com.omahagdg.dagger2demo.main.WeatherState;
import com.omahagdg.dagger2demo.main.WeatherResponse;
import com.omahagdg.dagger2demo.utils.ResourcesHelper;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.ViewAnimationUtils.createCircularReveal;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.omahagdg.dagger2demo.utils.AnimationHelper.animateViewFromBottom;

public class ForecastFragment extends Fragment
        implements ForecastView, Scoped<ForecastComponent> {

    @Inject ForecastPresenter presenter;

    @Bind(R.id.iv_weather_icon) ImageView iconImage;
    @Bind(R.id.tv_city_name) TextView cityName;
    @Bind(R.id.tv_temp) TextView tempText;
    @Bind(R.id.ll_forecast_items) View forecastItems;
    @Bind(R.id.forecast_container) LinearLayout forecastContainer;
    @Bind(R.id.shadow_view) View shadowView;
    @Bind(R.id.v_background) View headerBackground;
    @Bind(R.id.cv_header) CardView headerCardView;

    private int backgroundColor;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initializeOrGetComponent(getArguments()).inject(this);
        presenter.takeView(this);
        String[] transitionNames = getArguments().getStringArray("transition_names");
        if (transitionNames != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconImage.setTransitionName(transitionNames[0]);
            cityName.setTransitionName(transitionNames[1]);
            tempText.setTransitionName(transitionNames[2]);
        }

        showReveal(savedInstanceState == null);
    }

    private void showReveal(boolean animate) {
        if (animate) {
            headerBackground.post(this::startCircularReveal);
            forecastItems.animate().alpha(1.0f).setDuration(1000).start();
        } else {
            forecastItems.setAlpha(1.0f);
            headerBackground.setVisibility(View.VISIBLE);
        }
    }

    private void startCircularReveal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            headerBackground.setVisibility(View.VISIBLE);
            int width = headerBackground.getMeasuredWidth();
            int height = headerBackground.getMeasuredHeight();
            int cx = width / 2;
            int cy = height / 2;
            int finalRadius = Math.max(width, height) / 2;

            Animator animator = createCircularReveal(headerBackground, cx, cy, 0, finalRadius);
            animator.start();
        } else {
            headerCardView.setCardBackgroundColor(backgroundColor);
        }
    }

    @Override public void setForecastData(ForecastResponse response) {
        for (ForecastResponse.Day day : response.list()) {
            ForecastItemView view = new ForecastItemView(getContext());
            forecastContainer.addView(view);
            view.setData(day);
        }
    }

    @Override public void setHeaderData(WeatherResponse weatherResponse) {
        WeatherState weatherState = WeatherState.fromApiCode(weatherResponse.weather().get(0).icon());
        backgroundColor = ResourcesHelper.getColor(getContext(), weatherState.colorRes);
        headerBackground.setBackgroundColor(backgroundColor);
        cityName.setText(weatherResponse.name());
        tempText.setText(getString(R.string.degree_symbol_format, weatherResponse.main().temp()));
        iconImage.setImageDrawable(ResourcesHelper.getDrawable(getContext(), weatherState.iconRes));
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void daySelected(ForecastResponse.Day day, boolean animate) {
        shadowView.setVisibility(View.VISIBLE);
        shadowView.bringToFront();
        shadowView.setOnTouchListener((v, event) -> {
            removeDayView();
            return true;
        });
        DayDetailView dayDetailView = new DayDetailView(getContext());
        dayDetailView.setDay(day);
        if (animate) {
            getView().post(() -> animateViewFromBottom(dayDetailView, (ViewGroup) getView()));
        } else {
            LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            ((ViewGroup) getView()).addView(dayDetailView, params);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void removeDayView() {
        ((ViewGroup) getView()).removeViewAt(((ViewGroup) getView()).getChildCount() - 1);
        shadowView.setOnTouchListener(null);
        shadowView.setVisibility(View.GONE);
        presenter.clearSelectedDay();
    }

    @Override public Context getContext() {
        return new DaggerContextWrapper(super.getContext(), getScopeTag());
    }

    @Override public void onDestroyView() {
        presenter.dropView(this);
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        //TODO: safer way to tell if Fragment is being destroyed for good?
        if (!getActivity().isChangingConfigurations()) {
            Injector.destroyComponent(getContext());
        }
    }

    @Override public String getScopeTag() {
        return getClass().getName();
    }

    /**
     * The component could be built here if we had passed in a {@link Context}. This would have
     * allowed us to provide the {@link WeatherResponse} without it needing to implement Parcelable.
     * While it is easy to use POJOs in Dagger modules, we run into an issue when we encounter
     * process death. To make the experience as seamless as possible, it is ideal to have the
     * ability to recreate your modules after process death. This means that if you require data
     * that came from another screen, you'll need it to be parcelable so it can be put into a Bundle
     */
    public static ForecastFragment newInstance(
            WeatherResponse weatherResponse, String[] transitionNames) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("transition_names", transitionNames);
        bundle.putParcelable("weather_response", weatherResponse);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override public ForecastComponent initializeOrGetComponent(Bundle args) {
        ForecastComponent component;
        if (!Injector.checkComponent(getContext())) {
            component = buildComponent(getContext(), args.getParcelable("weather_response"), this);
        } else {
            component = Injector.getComponent(getContext());
        }
        return component;
    }

    private static ForecastComponent buildComponent(
            Context context, WeatherResponse weatherResponse, Scoped<ForecastComponent> scoped) {
        ForecastComponent component = ((MyApplication) context.getApplicationContext())
                .getComponent()
                .plus(new ForecastModule(weatherResponse));
        ComponentCache.get(context).put(scoped, component);
        return component;
    }
}
