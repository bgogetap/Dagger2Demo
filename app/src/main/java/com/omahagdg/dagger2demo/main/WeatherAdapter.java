package com.omahagdg.dagger2demo.main;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omahagdg.dagger2demo.R;
import com.omahagdg.dagger2demo.utils.ResourcesHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.omahagdg.dagger2demo.utils.ResourcesHelper.getDrawable;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<WeatherResponse> data;
    private final AdapterClickListener listener;

    WeatherAdapter(List<WeatherResponse> data, AdapterClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_response_item, parent, false);
        WeatherViewHolder holder = new WeatherViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            listener.itemSelected(holder.getAdapterPosition());
        });
        return holder;
    }

    @Override public int getItemCount() {
        return data.size();
    }

    @Override public long getItemId(int position) {
        return data.get(position).id();
    }

    @Override public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherResponse item = data.get(position);
        Context context = holder.itemView.getContext();
        holder.cityName.setText(item.name());
        holder.currentTemp.setText(context.getString(R.string.degree_symbol_format, item.main().temp()));
        holder.lowTemp.setText(context.getString(R.string.low_temp_format, item.main().temp_min()));
        holder.highTemp.setText(context.getString(R.string.high_temp_format, item.main().temp_max()));
        WeatherState icon = WeatherState.fromApiCode(item.weather().get(0).icon());
        holder.weatherIcon.setImageDrawable(getDrawable(context, icon.iconRes));
        holder.parentCardView.setCardBackgroundColor(ResourcesHelper.getColor(context, icon.colorRes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.weatherIcon.setTransitionName("transImage" + position);
            holder.cityName.setTransitionName("transCity" + position);
            holder.currentTemp.setTransitionName("transTemp" + position);
        }
    }

    static final class WeatherViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_city_name) TextView cityName;
        @Bind(R.id.tv_temp) TextView currentTemp;
        @Bind(R.id.tv_low) TextView lowTemp;
        @Bind(R.id.tv_high) TextView highTemp;
        @Bind(R.id.iv_weather_icon) ImageView weatherIcon;
        @Bind(R.id.cv_weather_item) CardView parentCardView;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface AdapterClickListener {

        void itemSelected(int position);
    }
}
