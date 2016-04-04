package com.omahagdg.dagger2demo.dagger;

import android.os.Bundle;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bgogetap on 3/13/16.
 */
public class ComponentCacheTest {

    @Test
    public void testCorrectComponentReturnedIfMapHasComponentForTag() {
        Object componentOne = new Object();
        Object componentTwo = new Object();
        ComponentCache componentCache = new ComponentCache();
        componentCache.put(new DummyScoped(), componentOne);
        componentCache.put(new DummyScopedTwo(), componentTwo);

        Object firstComponent = componentCache.getComponentForTag(new DummyScoped().getScopeTag());
        Assert.assertTrue(firstComponent == componentOne);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerThrownIfComponentForTagDoesntExist() {
        Object componentOne = new Object();
        ComponentCache componentCache = new ComponentCache();
        componentCache.put(new DummyScoped(), componentOne);

        componentCache.getComponentForTag("tag2");
    }

    @Test
    public void testOnlyCorrectComponentRemovedFromMapWhenDestroyed() {
        Object componentOne = new Object();
        Object componentTwo = new Object();
        ComponentCache componentCache = new ComponentCache();
        componentCache.put(new DummyScoped(), componentOne);
        componentCache.put(new DummyScopedTwo(), componentTwo);

        componentCache.destroyComponentForTag("tag");
        assertFalse(componentCache.hasComponentForTag("tag"));
        assertTrue(componentCache.hasComponentForTag("tag2"));
    }

    static class DummyScoped implements Scoped {

        @Override public String getScopeTag() {
            return "tag";
        }

        @Override public Object initializeOrGetComponent(Bundle savedInstanceState) {
            return new Object();
        }
    }

    static class DummyScopedTwo implements Scoped {

        @Override public String getScopeTag() {
            return "tag2";
        }

        @Override public Object initializeOrGetComponent(Bundle savedInstanceState) {
            return new Object();
        }
    }
}
