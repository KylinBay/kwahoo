package com.kylin.kwahoo.util;

import java.io.File;
import java.io.FileOutputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.Locale;
//
//import com.kwahoo.DomainName;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
//import android.preference.PreferenceManager;
import android.webkit.URLUtil;

public class BrowserUtil {
	
    public static final String SUFFIX_HTML = ".html";
    public static final String SUFFIX_PNG = ".png";
    public static final String SUFFIX_TXT = ".txt";
    
    public static final String SEARCH_ENGINE_GOOGLE = "https://www.google.com/search?q=";
    public static final String SEARCH_ENGINE_DUCKDUCKGO = "https://duckduckgo.com/?q=";
    public static final String SEARCH_ENGINE_STARTPAGE = "https://startpage.com/do/search?query=";
    public static final String SEARCH_ENGINE_BING = "http://www.bing.com/search?q=";
    public static final String SEARCH_ENGINE_BAIDU = "http://www.baidu.com/s?wd=";
    
    public static final String URL_PREFIX_GOOGLE_PLAY = "www.google.com/url?q=";
    public static final String URL_SUFFIX_GOOGLE_PLAY = "&sa";
    public static final String URL_PREFIX_GOOGLE_PLUS = "plus.url.google.com/url?q=";
    public static final String URL_SUFFIX_GOOGLE_PLUS = "&rct";

    public static final String UA_DESKTOP = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    public static final String URL_ENCODING = "UTF-8";
    public static final String URL_ABOUT_BLANK = "about:blank";
    public static final String URL_SCHEME_ABOUT = "about:";
    public static final String URL_SCHEME_MAIL_TO = "mailto:";
    public static final String URL_SCHEME_FILE = "file://";
    public static final String URL_SCHEME_FTP = "ftp://";
    public static final String URL_SCHEME_HTTP = "http://";
    public static final String URL_SCHEME_HTTPS = "https://";
    public static final String URL_SCHEME_INTENT = "intent://";
    
//	���ع���
    public static void download(Context context, String url, String contentDisposition, String mimeType) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String filename = URLUtil.guessFileName(url, contentDisposition, mimeType); // Maybe unexpected filename.

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(filename);
        request.setMimeType(mimeType);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
    
    public static String screenshot(Context context, Bitmap bitmap, String name) {
        if (bitmap == null) {
            return null;
        }

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (name == null || name.trim().isEmpty()) {
            name = String.valueOf(System.currentTimeMillis());
        }
        name = name.trim();

        int count = 0;
        File file = new File(dir, name + SUFFIX_PNG);
        while (file.exists()) {
            count++;
            file = new File(dir, name + "." + count + SUFFIX_PNG);
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
    
//    public static String queryWrapper(Context context, String query) {
//        // Use prefix and suffix to process some special links
//        String temp = query.toLowerCase(Locale.getDefault());
//        if (temp.contains(URL_PREFIX_GOOGLE_PLAY) && temp.contains(URL_SUFFIX_GOOGLE_PLAY)) {
//            int start = temp.indexOf(URL_PREFIX_GOOGLE_PLAY) + URL_PREFIX_GOOGLE_PLAY.length();
//            int end = temp.indexOf(URL_SUFFIX_GOOGLE_PLAY);
//            query = query.substring(start, end);
//        } else if (temp.contains(URL_PREFIX_GOOGLE_PLUS) && temp.contains(URL_SUFFIX_GOOGLE_PLUS)) {
//            int start = temp.indexOf(URL_PREFIX_GOOGLE_PLUS) + URL_PREFIX_GOOGLE_PLUS.length();
//            int end = temp.indexOf(URL_SUFFIX_GOOGLE_PLUS);
//            query = query.substring(start, end);
//        }
//
//        if (DomainName.isMatchDomainName(query)) {
//            if (query.startsWith(URL_SCHEME_ABOUT) || query.startsWith(URL_SCHEME_MAIL_TO)) {
//                return query;
//            }
//
//            if (!query.contains("://")) {
//                query = URL_SCHEME_HTTP + query;
//            }
//
//            return query;
//        }
//
//        try {
//            query = URLEncoder.encode(query, URL_ENCODING);
//        } catch (UnsupportedEncodingException u) {}
//
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        String custom = sp.getString(context.getString(R.string.sp_search_engine_custom), SEARCH_ENGINE_GOOGLE);
//        final int i = Integer.valueOf(sp.getString(context.getString(R.string.sp_search_engine), "0"));
//        switch (i) {
//            case 0:
//                return SEARCH_ENGINE_GOOGLE + query;
//            case 1:
//                return SEARCH_ENGINE_DUCKDUCKGO + query;
//            case 2:
//                return SEARCH_ENGINE_STARTPAGE + query;
//            case 3:
//                return SEARCH_ENGINE_BING + query;
//            case 4:
//                return SEARCH_ENGINE_BAIDU + query;
//            case 5:
//                return custom + query;
//            default:
//                return SEARCH_ENGINE_GOOGLE + query;
//        }
//    }
}
