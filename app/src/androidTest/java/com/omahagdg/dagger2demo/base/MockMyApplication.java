package com.omahagdg.dagger2demo.base;

public class MockMyApplication extends MyApplication {

    @Override protected MyApplicationComponent createComponent() {
        return DaggerMockMyApplicationComponent.builder()
                .mockMyApplicationModule(new MockMyApplicationModule(this))
                .build();
    }
}