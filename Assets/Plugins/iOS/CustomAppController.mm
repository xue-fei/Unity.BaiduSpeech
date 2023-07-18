#import "UnityAppController.h" 

#import "BDSEventManager.h"
#import "BDSASRDefines.h"
#import "BDSASRParameters.h"

#import "BDSWakeupDefines.h"
#import "BDSWakeupParameters.h"

//#error "请在百度AI官网新建应用，配置包名，并在此填写应用的 api key, secret key, appid(即appcode)"
const NSString* APP_ID = @"32097154";
const NSString* API_KEY = @"8GtmKHNgOzE6d5urlXWckrag";
const NSString* SECRET_KEY = @"7pfGMDh5hb9ywZ2nvDNmbCqE8jwXKNhh";

@interface CustomAppController : UnityAppController
@end

IMPL_APP_CONTROLLER_SUBCLASS (CustomAppController)

@implementation CustomAppController

// 向Unity传递参数；
extern void UnitySendMessage(const char *, const char *, const char *);

NSString *m_launchInfo = @"";

NSString *m_objName = @"";
NSString *m_methodName = @"";

extern "C"
{
    void GetLaunchInfo(const char *objName, const char *funcName);
}
void GetLaunchInfo(const char *objName, const char *funcName)
{
    BOOL result1 = [m_launchInfo isEqualToString:@""];
    BOOL result2 = [m_launchInfo hasPrefix:@"wx"];
    if (!result1&&!result2) 
	{ 
        NSLog(@"GetLaunchInfo m_launchInfo:%@",m_launchInfo);
        UnitySendMessage( objName,funcName, [m_launchInfo UTF8String]);
        // 清空，防止造成干扰;
        m_launchInfo = @"";
    }
    else
    {
        NSLog(@"GetLaunchInfo m_launchInfo:%@",m_launchInfo);
    }
}
  
extern "C"
{
    void CallPhone(const char *phone);
}
//拨打电话
void CallPhone(const char *phone)
{
    NSString*str0 = [NSString stringWithFormat:@"%s", phone];
    NSMutableString* str=[[NSMutableString alloc] initWithFormat:@"tel:%@",str0];
	[[UIApplication sharedApplication] openURL:[NSURL URLWithString:str]];  
}
  
- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<NSString *,id> *)options
{
    m_launchInfo = [url absoluteString];
    NSLog(@"openURL1 m_launchInfo:%@",m_launchInfo);
    if(![m_launchInfo isEqualToString:@""])
    {
        return  [WXApi handleOpenURL:url delegate:[WXApiManager sharedManager]];
    }
    else
    {
        return true;
    }
}

- (BOOL)application:(UIApplication*)application openURL:(NSURL*)url sourceApplication:(NSString*)sourceApplication annotation:(id)annotation
{
    //添加的代码
    m_launchInfo = [url absoluteString];
    NSLog(@"openURL2 m_launchInfo:%@",m_launchInfo);

    return true; 
} 
@end
