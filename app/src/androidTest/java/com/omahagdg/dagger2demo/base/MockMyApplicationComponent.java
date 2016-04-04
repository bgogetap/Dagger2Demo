package com.omahagdg.dagger2demo.base;

import com.omahagdg.dagger2demo.forecast.MockForecastComponent;
import com.omahagdg.dagger2demo.forecast.MockForecastModule;
import com.omahagdg.dagger2demo.main.FragmentTestBase;
import com.omahagdg.dagger2demo.main.MockMainComponent;
import com.omahagdg.dagger2demo.main.MockMainModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MockMyApplicationModule.class)
public interface MockMyApplicationComponent extends MyApplicationComponent {

    void inject(MyApplication application);

    void inject(FragmentTestBase fragmentTestBase);

    MockMainComponent plus(MockMainModule mockMainModule);

    MockForecastComponent plus(MockForecastModule mockForecastModule);
}
