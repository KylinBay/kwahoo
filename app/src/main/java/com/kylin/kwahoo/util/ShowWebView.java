package com.kylin.kwahoo.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ShowWebView extends WebViewClient {
	Activity activity;
	Boolean flag=true;
	AdBlock adBlock;
	public ShowWebView(Activity activity) {
		super();
		this.activity = activity;
//		adBlock=new AdBlock(activity);
	}
	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
	    handler.proceed();
	}


	@Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		adBlock=new AdBlock(view.getContext());
    	if(url==null)
    		return false;
    	if (adBlock.isAd(url)) {
    		Toast.makeText(activity, "已屏蔽广告", Toast.LENGTH_LONG).show();
    		return false;
		}
    	try{
//    		允许应用调起
    	if (url.startsWith("weixin://")
    			|| url.startsWith("alipays://")
    			|| url.startsWith("mailto:")
    			|| url.startsWith("tel:")
    			|| url.startsWith("tbopen://")
    			|| url.startsWith("openapp.jdmobile://")
    			|| url.startsWith("bilibili://")
    			|| url.startsWith("baidumap://")
    			|| url.startsWith("mqq://")
    			|| url.startsWith("zhihu://")
    			|| url.startsWith("ppstream://")
    			|| url.startsWith("thunder://")
    			|| url.startsWith("vipshop://")
    			|| url.startsWith("weishiiosscheme://")
    			|| url.startsWith("tudou://")
    			|| url.startsWith("renrenios://")
    			|| url.startsWith("mttbrowser://")
    			|| url.startsWith("tmall://")
    			|| url.startsWith("wbmain://")
    			|| url.startsWith("KingsoftOfficeApp://")	//WPS Office
    			|| url.startsWith("line://")	//Line
    			|| url.startsWith("youku://")
    			|| url.startsWith("iosamap://")
    			|| url.startsWith("prefs:root=SETTING")
    			|| url.startsWith("prefs:root=MOBILE_DATA_SETTINGS_ID")
    			|| url.startsWith("prefs:root=WIFI")	//WiFi
    			|| url.startsWith("prefs:root=LOCATION_SERVICES")
    			|| url.startsWith("ucbrowser://")
    			|| url.startsWith("baiduyun://")
    			) {
//		if(RightForCallApp.isAllowForCall(url))
//		{
    			Intent intent1=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
    			if (flag==true) {
					new MyDialog(activity, intent1).showDlg();
					flag=false;
			}
    		return true;
		}
//    	不允许调起的app
    	if (url.startsWith("baiduboxapp://")
    			|| url.startsWith("baiduboxlite://")
    			|| url.startsWith("snssdk143://")
    			|| url.startsWith("sinaweibo://")
    			|| url.startsWith("dialer://")
    			|| url.startsWith("tencent100385258://")
    			|| url.startsWith("QQmusic://")
    			|| url.startsWith("baidumusic://")
    			|| url.startsWith("sinaweibosso.422729959://")

    			|| url.startsWith("dianping:// ")
    			|| url.startsWith("tencentrm://")
    			|| url.startsWith("pptv://")	//pptv
    			|| url.startsWith("com.baofeng.play://")
    			|| url.startsWith("baiduvideoiphone://")
    			|| url.startsWith("bdviphapp://")
    			|| url.startsWith("bdNavi://")
    			|| url.startsWith("sohunews://")
    			|| url.startsWith("tenvideo://")
    			|| url.startsWith("tenvideo2://")
    			|| url.startsWith("tenvideo3://")
    			|| url.startsWith("TencentWeibo://")
    			|| url.startsWith("bainuo://")
    			|| url.startsWith("photowonder://")
    			|| url.startsWith("wondercamera://")
    			|| url.startsWith("com.kingsoft.powerword.6://")
    			|| url.startsWith("AVPlayer://")	//AVPlayer
    			|| url.startsWith("AVPlayerHD://")	//AVPlayer HD
    			|| url.startsWith("gplayer://")	//GPlayer
    			|| url.startsWith("nplayer-http://")	//nPlayer
    			|| url.startsWith("com.moke.moke-1://")
    			|| url.startsWith("yddictproapp://")
    			|| url.startsWith("wcc://")
    			|| url.startsWith("snssdk141://")
    			)
//  		if(RightForCallApp.isObjectForCall(url))
    	{
    		Intent intent1=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
    		activity.startActivity(intent1);
		}
    	}catch(Exception e){
    		return true;
    	}
		
        return false;
    }
    @Override
    public void onReceivedError(WebView view, int errorCode,
            String description, String failingUrl) {
    	switch (errorCode) {
		case ERROR_TIMEOUT:
			Toast.makeText(activity, "请求超时", Toast.LENGTH_LONG).show();
			break;

		case ERROR_BAD_URL:
			Toast.makeText(activity, "无法到达", Toast.LENGTH_LONG).show();
			break;
		}
    }
}

