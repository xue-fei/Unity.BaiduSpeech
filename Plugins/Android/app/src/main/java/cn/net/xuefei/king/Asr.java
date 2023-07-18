package cn.net.xuefei.king;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import org.json.JSONObject;

public class Asr {
    private static MainActivity m_Activity = null;

    private static volatile boolean isInited = false;

    private static boolean isOfflineEngineLoaded = false;

    private static EventManager asr = null;

    private static EventListener eventListener;

    public Asr() {}

    public Asr(MainActivity activity) {
        m_Activity = activity;
        Init();
    }

    private static void Init() {
        if (isInited) {
            Debug.LogWarning("Asr/Init()/");
            return;
        }
        Debug.Log("Asr/Init()/");
                isInited = true;
        asr = EventManagerFactory.create(m_Activity.getApplicationContext(), "asr");
        eventListener = new EventListener() {
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                Asr.OnEventListener(name, params, data, offset, length);
            }
        };
        asr.registerListener(eventListener);
    }

    public static void LoadOfflineEngine(String json) {
        Debug.Log("Speech/LoadOfflineEngine()/json:" + json.toString());
        asr.send("asr.kws.load", json, null, 0, 0);
        isOfflineEngineLoaded = true;
    }

    public static void Start(String json) {
        if (!isInited) {
            Debug.LogWarning("Speech/Start()/");
            return;
        }
        Debug.Log("Speech/Start()/json:" + json.toString());
        asr.send("asr.start", json, null, 0, 0);
    }

    public static void Cancel() {
        if (!isInited) {
            Debug.LogWarning("Speech/Cancel()/");
            return;
        }
        Debug.Log("Speech/Cancel()/");
                asr.send("asr.cancel", null, null, 0, 0);
    }

    public static void Stop() {
        if (!isInited) {
            Debug.LogWarning("Speech/Stop()/");
            return;
        }
        Debug.Log("Speech/Stop()/");
                asr.send("asr.stop", null, null, 0, 0);
    }

    public static void Release() {
        if (asr == null)
            return;
        Cancel();
        if (isOfflineEngineLoaded) {
            asr.send("asr.kws.unload", null, null, 0, 0);
            isOfflineEngineLoaded = false;
        }
        asr.unregisterListener(eventListener);
        asr = null;
        isInited = false;
    }

    private static void OnEventListener(String name, String params, byte[] data, int offset, int length) {
        Debug.Log("Speech/OnEventListener()/"+ name + "|" + params);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", name);
            jsonObject.put("param", params);
            jsonObject.put("data", data);
            jsonObject.put("offset", offset);
            jsonObject.put("length", length);
            BaiduSpeechHelper.SendPlatformMessageToUnity(MessageCode.OnAsrCallback, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("Speech/OnSpeechCallback()/"+ e);
        }
    }
}
