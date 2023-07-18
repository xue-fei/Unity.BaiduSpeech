package cn.net.xuefei.king;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import java.util.HashMap;
import org.json.JSONObject;

public class Wakeup {
    private static boolean isInited = false;

    private static MainActivity m_Activity = null;

    private static EventManager wp = null;

    private static EventListener eventListener;

    public Wakeup() {}

    public Wakeup(MainActivity activity) {
        m_Activity = activity;
        Init();
    }

    private static void Init() {
        if (isInited) {
            Debug.LogWarning("Wakeup/Init()/");
            return;
        }
        Debug.Log("Wakeup/Init()/");
                isInited = true;
        wp = EventManagerFactory.create(m_Activity.getApplicationContext(), "wp");
        eventListener = new EventListener() {
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                Wakeup.OnEventListener(name, params, data, offset, length);
            }
        };
        wp.registerListener(eventListener);
    }

    public static void Start(String wakeUpPath) {
        Debug.Log("Wakeup/Start()/");
                HashMap<Object, Object> map = new HashMap<>();
        map.put("kws-file", wakeUpPath);
        String json = (new JSONObject(map)).toString();
        wp.send("wp.start", json, null, 0, 0);
    }

    public static void Stop() {
        Debug.Log("Wakeup/Stop()/");
                wp.send("wp.stop", null, null, 0, 0);
    }

    public static void Release() {
        Debug.Log("Wakeup/Release()/");
                Stop();
        wp.unregisterListener(eventListener);
        wp = null;
        isInited = false;
    }

    private static void OnEventListener(String name, String params, byte[] data, int offset, int length) {
        Debug.Log("Wakeup/OnEventListener()/"+ name + "|" + params);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", name);
            jsonObject.put("param", params);
            jsonObject.put("data", data);
            jsonObject.put("offset", offset);
            jsonObject.put("length", length);
            BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnWakeupCallback, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("Wakeup/OnWakeupCallback()/"+ e);
        }
    }
}