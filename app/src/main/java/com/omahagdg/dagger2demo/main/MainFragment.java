package com.omahagdg.dagger2demo.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.base.ActivityPresenter;
import com.omahagdg.dagger2demo.base.MyApplication;
import com.omahagdg.dagger2demo.dagger.ComponentCache;
import com.omahagdg.dagger2demo.dagger.DaggerContextWrapper;
import com.omahagdg.dagger2demo.dagger.Injector;
import com.omahagdg.dagger2demo.dagger.Scoped;
import com.omahagdg.dagger2demo.forecast.ForecastFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static com.omahagdg.dagger2demo.ui.TransitionHelper.getDetailBundle;
import static com.omahagdg.dagger2demo.ui.TransitionHelper.setUpDetailsTransition;

public class MainFragment extends Fragment
        implements MainView,
        WeatherAdapter.AdapterClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        Scoped<MainComponent> {

    @Inject MainPresenter presenter;
    @Inject ActivityPresenter activityPresenter;
    @Inject LocationPermissionHelper locationPermissionHelper;

    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    private WeatherAdapter adapter;
    private List<WeatherResponse> data;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initializeOrGetComponent(getArguments()).inject(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.takeView(this);
        activityPresenter.registerActivityResultDelegate(locationPermissionHelper);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override public void setData(List<WeatherResponse> data) {
        swipeRefreshLayout.setRefreshing(false);
        this.data = data;
        if (adapter == null) {
            adapter = new WeatherAdapter(data, this);
            adapter.setHasStableIds(true);
            recyclerView.setAdapter(adapter);
        } else {
            if (recyclerView.getAdapter() == null) {
                recyclerView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override public void itemSelected(int position) {
        goToDetails(position);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void goToDetails(int position) {
        WeatherResponse weatherResponse = data.get(position);
        View parentView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        View sharedImage = ButterKnife.findById(parentView, R.id.iv_weather_icon);
        View sharedCity = ButterKnife.findById(parentView, R.id.tv_city_name);
        View sharedTemp = ButterKnife.findById(parentView, R.id.tv_temp);

        String[] transitionNames = getDetailBundle(sharedImage, sharedCity, sharedTemp);
        ForecastFragment forecastFragment = ForecastFragment.newInstance(
                weatherResponse, transitionNames);
        setUpDetailsTransition(this, forecastFragment, getContext());

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, forecastFragment, "detail_fragment")
                .addToBackStack(null)
                .addSharedElement(sharedImage, transitionNames[0])
                .addSharedElement(sharedCity, transitionNames[1])
                .addSharedElement(sharedTemp, transitionNames[2])
                .commit();
    }

    @OnClick(R.id.fab) void fabClicked() {
        presenter.loadRandom();
    }

    @Override public void showPermissionDeniedMessage(boolean requestAgain) {
        Snackbar.make(getView(), getContext().getString(R.string.location_permission_denied), LENGTH_LONG)
                .setAction(R.string.enable, view -> {
                    if (requestAgain) {
                        presenter.loadWeatherForLocation();
                    } else {
                        activityPresenter.goToAppSystemSettings();
                    }
                })
                .show();
    }

    @Override public void toggleFab(boolean show) {
        if (show) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_location) {
            presenter.loadWeatherForLocation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onRefresh() {
        presenter.refreshData();
    }

    @Override public Context getContext() {
        return new DaggerContextWrapper(super.getContext(), getScopeTag());
    }

    @Override public String getScopeTag() {
        return getClass().getName();
    }

    @Override public MainComponent initializeOrGetComponent(Bundle args) {
        MainComponent component;
        if (!Injector.checkComponent(getContext())) {
            component = ((MyApplication) getContext().getApplicationContext()).getComponent()
                    .plus(new MainModule());
            ComponentCache componentCache = ComponentCache.get(getContext());
            componentCache.put(this, component);
        } else {
            component = Injector.getComponent(getContext());
        }
        return component;
    }

    @Override public void onDestroyView() {
        presenter.dropView(this);
        activityPresenter.unregisterActivityResultDelegate(locationPermissionHelper);
        super.onDestroyView();
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }
}
