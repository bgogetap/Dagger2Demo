package com.omahagdg.dagger2demo.main;

import com.omahagdg.dagger2demo.dagger.ForMain;

import dagger.Subcomponent;

@ForMain
@Subcomponent(modules = MockMainModule.class)
public interface MockMainComponent extends MainComponent {

    WeatherService weatherService();
}
