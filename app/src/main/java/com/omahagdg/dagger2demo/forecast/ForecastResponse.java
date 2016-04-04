package com.omahagdg.dagger2demo.forecast;

import com.google.auto.value.AutoValue;
import com.omahagdg.dagger2demo.retrofit.AutoGson;

import java.util.Date;
import java.util.List;

@AutoValue @AutoGson
public abstract class ForecastResponse {

    public static ForecastResponse create(City city, List<Day> list) {
        return new AutoValue_ForecastResponse(city, list);
    }

    public abstract City city();

    public abstract List<Day> list();

    @AutoValue @AutoGson
    static abstract class City {

        static City create(long id, String name, String country) {
            return new AutoValue_ForecastResponse_City(id, name, country);
        }

        abstract long id();

        abstract String name();

        abstract String country();
    }

    @AutoValue @AutoGson
    public static abstract class Day {

        public static Day create(long dt, Temp temp, List<Weather> weather, double humidity) {
            return new AutoValue_ForecastResponse_Day(dt, temp, weather, humidity);
        }

        abstract long dt();

        abstract Temp temp();

        abstract List<Weather> weather();

        abstract double humidity();

        public Date getDate() {
            return new Date(dt() * 1000);
        }
    }

    @AutoValue @AutoGson
    public abstract static class Temp {

        static Temp create(
                double day,
                double min,
                double max,
                double night,
                double eve,
                double morn
        ) {
            return new AutoValue_ForecastResponse_Temp(day, min, max, night, eve, morn);
        }

        abstract double day();

        abstract double min();

        abstract double max();

        abstract double night();

        abstract double eve();

        abstract double morn();
    }

    @AutoValue @AutoGson
    static abstract class Weather {
        static Weather create(long id, String main, String description, String icon) {
            return new AutoValue_ForecastResponse_Weather(id, main, description, icon);
        }

        abstract long id();

        abstract String main();

        abstract String description();

        abstract String icon();
    }
}
