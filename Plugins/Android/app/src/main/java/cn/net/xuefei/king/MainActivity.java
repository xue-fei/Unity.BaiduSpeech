package cn.net.xuefei.king;
import android.os.Bundle;
import cn.unity3d.player.UnityPlayerNativeActivity;

public class MainActivity extends UnityPlayerNativeActivity {
    private static MainActivity m_Activity = null;

    private static String m_UnityObjectName = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void Init(MainActivity activity, String unityObjectName) {
        m_Activity = activity;
        m_UnityObjectName = unityObjectName;
        BaiduSpeechHelper.Init(m_Activity, m_UnityObjectName);
        PermissionsHelper.Init(m_Activity);
    }

    public int CheckPermissions(String permission) {
        Debug.Log("MainActivity/CheckPermissions()/permission:" + permission);
        return PermissionsHelper.CheckPermissions(permission);
    }

    public void RequestPermissions(int requestCode, String[] permissions) {
        Debug.Log("MainActivity/RequestPermissions()/requestCode:" + requestCode + "--permissions:" + permissions.toString());
        PermissionsHelper.RequestPermissions(requestCode, permissions);
    }

    public Asr NewAsr() {
        Debug.Log("MainActivity/NewAsr()/");
        return new Asr(m_Activity);
    }

    public Wakeup NewWakeup() {
        Debug.Log("MainActivity/NewWakeup()/");
        return new Wakeup(m_Activity);
    }

    public void onRequestPermissionsResult(int requestCode,   String[] permissions,   int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Debug.Log("MainActivity/onRequestPermissionsResult()/permissions:" + permissions.toString());
        PermissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}