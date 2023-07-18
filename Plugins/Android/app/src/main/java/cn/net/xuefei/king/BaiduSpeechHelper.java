package cn.net.xuefei.king;

import com.unity3d.player.UnityPlayer;
import org.json.JSONObject;

public class BaiduSpeechHelper {
    private static MainActivity m_Activity = null;

    private static String m_UnityObjectName = "";

    private static String m_Methodname = "OnMessage";

    public static void Init(MainActivity activity, String unityObjectName) {
        m_Activity = activity;
        m_UnityObjectName = unityObjectName;
    }

    public static void SendPlatformMessageToUnity(MessageCode msgCode, String param) {
        int msgCodeInt = msgCode.ordinal();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgCode", msgCodeInt);
            jsonObject.put("Content", param);
            UnityPlayer.UnitySendMessage(m_UnityObjectName, m_Methodname, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("BaiduSpeechHelper/SendPlatformMessageToUnity()/Exception:" + e);
        }
    }

    public static void SendPlatformMessageToUnity(MessageCode msgCode, String... params) {
        int msgCodeInt = msgCode.ordinal();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msgCode", msgCodeInt);
            jsonObject.put("ContentArr", params);
            UnityPlayer.UnitySendMessage(m_UnityObjectName, m_Methodname, jsonObject.toString());
        } catch (Exception e) {
            Debug.LogWarning("BaiduSpeechHelper/SendPlatformMessageToUnity()/Exception:" + e);
        }
    }

    public static void SendUnityMessageToPlatform(int iMsgCode, String... params) {
        MessageCode msgCode = MessageCode.values()[iMsgCode];
        //null.$SwitchMap$com$hiscene$baiduspeech$MessageCode[msgCode.ordinal()];
    }
}
