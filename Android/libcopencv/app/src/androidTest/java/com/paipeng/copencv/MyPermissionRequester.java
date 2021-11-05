package com.paipeng.copencv;

import android.app.UiAutomation;
import android.os.Build;
import android.util.Log;

import androidx.test.InstrumentationRegistry;

public class MyPermissionRequester {

    public static final String TAG = MyPermissionRequester.class.getSimpleName();

    public static void request(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            UiAutomation auto = InstrumentationRegistry.getInstrumentation().getUiAutomation();
            String cmd = "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName() + " %1$s";
            String cmdTest = "pm grant " + InstrumentationRegistry.getContext().getPackageName() + " %1$s";
            String currCmd;
            for (String perm : permissions) {
                execute(String.format(cmd, perm), auto);
                execute(String.format(cmdTest, perm), auto);
            }
        }
        //GrantPermissionRule.grant(permissions);
    }

    private static void execute(String currCmd, UiAutomation auto){
        Log.d(TAG, "exec cmd: " + currCmd);
        auto.executeShellCommand(currCmd);
    }
}

