package cn.net.xuefei.king;

import android.content.pm.PackageManager;
import android.os.Build;
import java.util.Arrays;
import org.json.JSONObject;

public class PermissionsHelper {
    private static MainActivity m_Activity = null;

    public static void Init(MainActivity activity) {
        m_Activity = activity;
    }

    public static int CheckPermissions(String permission) {
        int havePermission = 0;
        if (Build.VERSION.SDK_INT >= 23)
            havePermission = m_Activity.checkSelfPermission(permission);
        return havePermission;
    }

    public static void RequestPermissions(int requestCode, String[] permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean needPermission = false;
            for (String permisson : permissions) {
                if (m_Activity.checkSelfPermission(permisson) != PackageManager.PERMISSION_GRANTED) {
                    needPermission = true;
                    break;
                }
            }
            if (needPermission) {
                m_Activity.requestPermissions(permissions, requestCode);
            } else {
                int[] grantResults = new int[permissions.length];
                Arrays.fill(grantResults, 1);
                onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            int[] grantResults = new int[permissions.length];
            Arrays.fill(grantResults, 1);
            onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestCode", requestCode);
            jsonObject.put("permissions", permissions);
            jsonObject.put("grantResults", grantResults);
            BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.onRequestPermissionsResult, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("PermissionsHelper/onRequestPermissionsResult()/Exception:" + e);
        }
    }
}