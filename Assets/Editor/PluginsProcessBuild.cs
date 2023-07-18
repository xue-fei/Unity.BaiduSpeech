using UnityEngine;
using System.IO;
using UnityEditor.Callbacks;
using UnityEditor;
using System.Collections;

#if UNITY_IOS
using UnityEditor.iOS.Xcode;
using UnityEditor.iOS.Xcode.Extensions;
#endif

public class PluginsProcessBuild
{
#if UNITY_IOS
    [PostProcessBuildAttribute(88)]
    public static void onPostProcessBuild(BuildTarget buildTarget, string path)
    {
        if (buildTarget != BuildTarget.iOS)
        {
            Debug.LogWarning("Target is not iPhone. XCodePostProcess will not run");
            return;
        }
        //导入文件
        PBXProject proj = new PBXProject();
        string projPath = path + "/Unity-iPhone.xcodeproj/project.pbxproj";
        proj.ReadFromFile(projPath);
        //xcode Target
        string target = proj.GetUnityMainTargetGuid();
        string unityTarget = proj.GetUnityFrameworkTargetGuid();
        // add extra framework(s)
        proj.AddFrameworkToProject(target, "AssetsLibrary.framework", false);
        proj.AddFrameworkToProject(target, "Photos.framework", true);
        proj.AddFrameworkToProject(target, "SystemConfiguration.framework", true);
        proj.AddFrameworkToProject(target, "security.framework", false);
        proj.AddFrameworkToProject(target, "CoreTelephony.framework", false);
         
        proj.AddBuildProperty(target, "OTHER_LDFLAGS", "-ObjC");
        proj.AddBuildProperty(target, "ENABLE_BITCODE", "NO");

        string fileGuidLibz = proj.AddFile("usr/lib/libz.tbd", "Libraries/libz.tbd", PBXSourceTree.Sdk);
        proj.AddFileToBuild(target, fileGuidLibz);
        proj.AddFileToBuild(unityTarget, fileGuidLibz);

        string fileGuidSqlite = proj.AddFile("usr/lib/libsqlite3.tbd", "Libraries/libsqlite3.tbd", PBXSourceTree.Sdk);
        proj.AddFileToBuild(target, fileGuidSqlite);
        proj.AddFileToBuild(unityTarget, fileGuidSqlite); 
         
        // rewrite to file
        File.WriteAllText(projPath, proj.WriteToString());

        string plistPath = path + "/Info.plist";
        PlistDocument plist = new PlistDocument();
        plist.ReadFromString(File.ReadAllText(plistPath));

        // Get root
        PlistElementDict plistElementDict = plist.root;
    
        plistElementDict.SetString("NSMicrophoneUsageDescription", "语音聊天时需要访问麦克风"); 
        plistElementDict.SetString("NSPhotoLibraryAddUsageDescription", "保存图片时需要访问相册");
        plistElementDict.SetString("NSPhotoLibraryUsageDescription", "读取图片时需要访问相册");
        plistElementDict.SetString("NSLocationWhenInUseUsageDescription", "显示位置时需要访问您的位置");
         
        // Write to file
        File.WriteAllText(plistPath, plist.WriteToString());
    }
#endif
}