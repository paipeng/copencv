package com.paipeng.copencv;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.paipeng.copencv", appContext.getPackageName());
    }

    @Test
    public void testOpenCV2() {
        COpenCVApi cOpenCVApi = new COpenCVApi();

        String o = cOpenCVApi.stringFromJNI();
        System.out.println("StringFromJNI: " + o);
        assertEquals(o, "Hello from C++");
    }
}