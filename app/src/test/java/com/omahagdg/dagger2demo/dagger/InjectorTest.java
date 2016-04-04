package com.omahagdg.dagger2demo.dagger;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by bgogetap on 3/13/16.
 */
@SuppressWarnings("WrongConstant") public class InjectorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInjectorThrowsIllegalArguementExceptionWhenContextNotDaggerContext() {
        Context context = Mockito.mock(Context.class);
        Injector.checkComponent(context);
    }
}
