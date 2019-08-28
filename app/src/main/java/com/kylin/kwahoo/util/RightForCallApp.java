package com.kylin.kwahoo.util;

import java.util.regex.Pattern;

public class RightForCallApp {
//
    private static final String[] allow = {
        "weixin://", "alipays://", "mailto:", "tel:", "tbopen://", "openapp.jdmobile://",
        "bilibili://", "baidumap://", "mqq://", "zhihu://", "ppstream://", "ppstream://",
        "thunder://", "vipshop://", "weishiiosscheme://", "tudou://", "renrenios://", "mttbrowser://",
        "tmall://", "wbmain://", "KingsoftOfficeApp://", "line://", "youku://", "iosamap://",
        "prefs:root=SETTING", "prefs:root=MOBILE_DATA_SETTINGS_ID", "prefs:root=WIFI",
        "prefs:root=LOCATION_SERVICES", "ucbrowser://", "baiduyun://"
    };

    private static final String[] object = {
        "baiduboxapp://", "baiduboxlite://", "snssdk143://", "sinaweibo://", "dialer://", "tencent100385258://",
        "QQmusic://", "baidumusic://", "sinaweibosso.422729959://", "dianping:// ", "tencentrm://", "pptv://",
        "com.baofeng.play://", "baiduvideoiphone://", "bdviphapp://", "bdNavi://", "sohunews://", "tenvideo://",
        "tenvideo2://", "tenvideo3://", "TencentWeibo://", "bainuo://", "photowonder://", "wondercamera://",
        "com.kingsoft.powerword.6://", "AVPlayer://", "AVPlayerHD://", "gplayer://", "nplayer-http://",
        "com.moke.moke-1://", "yddictproapp://", "wcc://", "snssdk141://"
    };

    private static Pattern matchAllowAppCall(){
        Pattern WEB_URL;
        StringBuilder sb = new StringBuilder();
//        sb.append("(");
        for (int i = 0; i < allow.length; i++)
        {
            sb.append(allow[i]);
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
//        sb.append(")");
        String pattern = sb.toString();
        WEB_URL = Pattern.compile(pattern);
        return WEB_URL;
    }
    private static Pattern matchObjectAppCall(){
        Pattern WEB_URL;
        StringBuilder sb = new StringBuilder();
//        sb.append("(");
        for (int i = 0; i < object.length; i++)
        {
            sb.append(object[i]);
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
//        sb.append(")");
        String pattern = sb.toString();
        WEB_URL = Pattern.compile(pattern);
        return WEB_URL;
    }

    public static boolean isAllowForCall(String appUrl){
        Pattern appSite = matchAllowAppCall();
        if(appSite.matcher(appUrl).matches())
            return true;
        return false;
    }

    public static boolean isObjectForCall(String appUrl){
        Pattern appSite = matchObjectAppCall();
        if(appSite.matcher(appUrl).matches())
            return true;
        return false;
    }
}
