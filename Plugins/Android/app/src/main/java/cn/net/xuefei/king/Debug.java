package cn.net.xuefei.king;

import android.util.Log;

public class Debug {
    public static String TAG = "baiduspeech";

    public static void Log(String msg) {
        Log.d(TAG, msg);
    }

    public static void LogWarning(String msg) {
        Log.d(TAG, msg);
    }

    public static void LogError(String msg) {
        Log.d(TAG, msg);
    }
}
