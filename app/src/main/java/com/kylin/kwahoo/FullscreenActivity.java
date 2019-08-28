package com.kylin.kwahoo;

import java.util.Timer;
import java.util.TimerTask;

import com.kylin.kwahoo.util.SdCardUtil;
import com.kylin.kwahoo.util.ShowWebView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 */
@SuppressLint("ClickableViewAccessibility")
public class FullscreenActivity extends Activity implements OnFocusChangeListener, OnGestureListener{
    Button goback,goforward,webFresh,setMenu,backToMain,btnsearch;
    EditText editText;
    WebView mWebview;
    MyTask myTask;
    ProgressBar progressBar;
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
//	 private Context context;

    GestureDetector detector;
    View bttomview,table;

    @SuppressLint({"NewApi", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        sdInit();
        setContentView(R.layout.activity_fullscreen);

        editText=findViewById(R.id.myEditText);
        mWebview=findViewById(R.id.fullscreen_content);
        bttomview=findViewById(R.id.fullscreen_content_controls);
        table=findViewById(R.id.table);
//		myReceiver=new MyReceiver(this);

        btnsearch=findViewById(R.id.btnsearch);
        goback=findViewById(R.id.goback);
        goforward=findViewById(R.id.goforward);
        webFresh=findViewById(R.id.webFresh);
        setMenu=(Button)findViewById(R.id.setMenu);
        backToMain=(Button)findViewById(R.id.backToMain);
        progressBar=(ProgressBar)findViewById(R.id.webProgress);

        BtnClick btnClick=new BtnClick();
        goback.setOnClickListener(btnClick);
        goforward.setOnClickListener(btnClick);
        setMenu.setOnClickListener(btnClick);
        webFresh.setOnClickListener(btnClick);
        backToMain.setOnClickListener(btnClick);
        editText.setHorizontallyScrolling(true);
        editText.setFocusable(true);
        myTask=new MyTask();
//		myTask.doInBackground("https://qfsky.cn/star/");
//		myTask.doInBackground("file:///android_asset/mainHtml.html");
        myTask.doInBackground("https://kwahoo.top");
        editText.setOnFocusChangeListener(this);
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                    if (isLegalURL(editText.getText().toString())) {
                        fixURL(editText.getText().toString());
                    }
                    else {
                        myTask.doInBackground("https://www.baidu.com/#wd="+editText.getText().toString());
                    }
                }
                return false;
            }
        });

        detector = new GestureDetector(this,this);
        mWebview.setDownloadListener(new DownloadListener(){
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
//				BrowserUtil.download(context, url, contentDisposition, mimetype);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
//		this.context=mWebview.getContext();
//		mWebview.setDownloadListener(new KDownloadListener(mWebview.getContext()));
//		mWebview.setDownloadListener(new KDownloadListener(this.context));
    }

    //	static {
    public void sdInit(){
        if (SdCardUtil.checkSdCard()==true) {
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR);
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILEPHOTO);
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILEIMAGE);
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILECACHE);
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILEUSER+"/icon");
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILECACHE+"/webviewCache");
            SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILECACHE+"/webcache");
        }
    }
//	}

    public void click(View v) {
        Toast.makeText(FullscreenActivity.this, "正在打开网页", Toast.LENGTH_SHORT).show();
        String website=editText.getText().toString().trim();
//        隐藏输入框
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        if (isLegalURL(website)) {
            fixURL(website);
        }
        else {
            website = "https://www.baidu.com/#wd="+website;
            myTask.doInBackground(website);
        }
    }

    /*
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK &&customView != null) {
            hideCustomView();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_BACK){
            exitBy2Click();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *  双击退出
     */
    private static boolean isExit=false;
    private void exitBy2Click(){
        Timer tExit=null;
        if(isExit==false){
            isExit=true;
//            Toast.makeText(this, "在", Toast.LENGTH_SHORT).show();
            tExit=new Timer();
            tExit.schedule(new TimerTask() {

                @Override
                public void run() {
                    isExit=false;
                }
            },2000);
        }
        else{
            finish();
            System.exit(0);
        }
    }

    public void fixURL(String webSite){
        String str="http://";
        String str1="https://";
        String str2="HTTP://";
        String str3="HTTPS://";
        if(webSite.contains(str) || webSite.contains(str1)
                || webSite.contains(str2) || webSite.contains(str3))
        {
            webSite=webSite.trim();
        }
        else{
            webSite="http://"+webSite;
            webSite=webSite.trim();
        }
        myTask.doInBackground(webSite);
    }

    /**
     * function 判断网址是否合法
     * @param webSite
     * @return true
     */
    public boolean isLegalURL(String webSite){
        DomainName domainName=new DomainName();
        if(DomainName.isMatchDomainName(webSite)){
            return true;
        }
        if (domainName.isMatchIPAddr(webSite)) {
            return true;
        }
        return false;
    }


    public class BtnClick implements OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.goback){
                if(mWebview.canGoBack())
                    mWebview.goBack();
                else
                    Toast.makeText(FullscreenActivity.this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            }
            if(v.getId()==R.id.goforward){
                if(mWebview.canGoForward())
                    mWebview.goForward();
                else
                    Toast.makeText(FullscreenActivity.this, "没有更多的页了", Toast.LENGTH_SHORT).show();
            }
            if(v.getId()==R.id.webFresh){
                mWebview.reload();//ˢ��
                Toast.makeText(FullscreenActivity.this, "已刷新", Toast.LENGTH_SHORT).show();
            }
            if(v.getId()==R.id.setMenu)
            {
                Intent setintent=new Intent();
                setintent.setClass(FullscreenActivity.this, SettingsActivity.class);
                startActivity(setintent);
//				Toast.makeText(FullscreenActivity.this, "功能正在开发中", Toast.LENGTH_SHORT).show();

            }

            //回到主页面
            if (v.getId()==R.id.backToMain) {
                showPopupMenu(backToMain);
            }
        }
        /**
         * 功能   弹出更多页面
         * @param view
         */
        @SuppressLint("InflateParams")
        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(FullscreenActivity.this, view);

            popupMenu.getMenuInflater().inflate(R.menu.setmenu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id=item.getItemId();
                    if (id==R.id.action_main) {
//		            		mWebview.loadUrl("file:///android_asset/mainHtml.html");
                        mWebview.loadUrl("https://kwahoo.top");
                    }
                    if (id==R.id.action_search) {
//		            		stub_import.setVisibility(View.VISIBLE);
//		            		stub_import.bringToFront();
                        final AlertDialog.Builder builder=new AlertDialog.Builder(FullscreenActivity.this);
                        View view=LayoutInflater.from(FullscreenActivity.this).inflate(R.layout.dialog_search, null);
                        builder.setView(view);
                        final AlertDialog dialog=builder.show();
                        Window window=dialog.getWindow();
                        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.TOP);

                    }
                    if (id==R.id.action_del) {
//		            		mWebview.loadUrl("https://qfsky.cn/star/");
//		            		mWebview.loadUrl("file:///android_asset/mainHtml.html");
                        mWebview.loadUrl("https://kwahoo.top");
//		            		clearWebViewCache();

                    }
                    return false;
                }
            });

            // PopupMenu 关闭
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
//		                Toast.makeText(getApplicationContext(), "已关闭PopupMenu", Toast.LENGTH_SHORT).show();
                }
            });

            popupMenu.show();
        }

    }


    /**
     * handler 后台更新UI
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    editText.setText(msg.obj.toString());
                    break;
                case 2:
                    if (msg.obj.toString().equals("https://kwahoo.top")) {
                        editText.setSelectAllOnFocus(true);
                        editText.selectAll();
                    }
                    else {
                        editText.setText(msg.obj.toString());
                        editText.setSelectAllOnFocus(true);
                        editText.selectAll();
                    }

                    break;
                case 3:
//            	searchInfo.setText(msg.obj.toString());
                    break;
                case 4:
//            	searchInfo.setText("0/0");
                    break;
            }
        };
    };

    /**
     * 功能   后台网络任务处理
     *
     */

    class MyTask extends AsyncTask<String, Integer, String>  {
        @Override
        protected String doInBackground(String... params) {
            webview_init(mWebview);
            mWebview.loadUrl(params[0]);
            mWebview.setWebViewClient(new ShowWebView(FullscreenActivity.this));
            mWebview.setWebChromeClient(new WebChromeClient(){
                @Override
                public View getVideoLoadingProgressView() {
                    FrameLayout frameLayout = new FrameLayout(FullscreenActivity.this);
                    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    return frameLayout;
                }
                @Override
                public void onShowCustomView(View view, CustomViewCallback callback) {
                    showCustomView(view, callback);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }
                /**
                 * 隐藏视图
                 */
                @Override
                public void onHideCustomView() {
                    hideCustomView();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    Message message=new Message();
                    message.what=1;
                    message.obj=title;
                    mHandler.sendMessage(message);
                }
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    publishProgress(newProgress);
                }
                @Override
                public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
                    super.onGeolocationPermissionsShowPrompt(origin, callback);
                }


            });
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values[0]==100){
                progressBar.setVisibility(View.GONE);
            }
            else{
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(values[0]); //设置进度条
            }
        }

        @SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
        @SuppressWarnings("deprecation")
        public void webview_init(WebView mWebview){
            mWebview.getSettings().setJavaScriptEnabled(true);
            mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebview.getSettings().setAllowFileAccess(true);
            mWebview.getSettings().setDefaultTextEncodingName("UTF-8");
            mWebview.getSettings().setLoadWithOverviewMode(true);
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.getSettings().setDomStorageEnabled(true);
            mWebview.getSettings().setSupportZoom(true);
            mWebview.getSettings().setBuiltInZoomControls(true);
            mWebview.getSettings().setDisplayZoomControls(false);

//				mWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

            mWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebview.getSettings().setPluginState(PluginState.ON);
            mWebview.getSettings().setLoadsImagesAutomatically(true);

            mWebview.getSettings().setAppCacheEnabled(true);
            String cacheDirPath = SdCardUtil.getFilecache()+ APP_CACAHE_DIRNAME;
            mWebview.getSettings().setAppCachePath(cacheDirPath);
            mWebview.getSettings().setDatabaseEnabled(true);
            mWebview.getSettings().setDatabasePath(cacheDirPath);

            mWebview.getSettings().setGeolocationEnabled(true);

//		        mWebview.getSettings().setMediaPlaybackRequiresUserGesture(true);
            mWebview.getSettings().setLoadWithOverviewMode(false);
            mWebview.getSettings().setUseWideViewPort(true);
            mWebview.getSettings().setSavePassword(true);
            mWebview.getSettings().setSaveFormData(true);
            mWebview.getSettings().setSupportMultipleWindows(true);
//            解决某些页面自动放大的问题
            int screenDensity = getResources().getDisplayMetrics().densityDpi;
            WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
            switch (screenDensity)
            {
                case DisplayMetrics.DENSITY_LOW:
                    zoomDensity = WebSettings.ZoomDensity.CLOSE;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                case DisplayMetrics.DENSITY_XHIGH:
                case DisplayMetrics.DENSITY_XXHIGH:
                default:
                    zoomDensity = WebSettings.ZoomDensity.FAR;
                    break;
            }
            mWebview.getSettings().setDefaultZoom(zoomDensity);
        }

    }

    /**
     *
     * @param view
     * @param callback
     */
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        FullscreenActivity.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mWebview.setVisibility(View.VISIBLE);
    }

    static class FullscreenHolder extends FrameLayout {

        @SuppressWarnings("deprecation")
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }
    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onResume() {
        super.onResume();
        mWebview.onResume();
        mWebview.resumeTimers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebview.reload();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebview.stopLoading();
        mWebview.setWebChromeClient(null);
        mWebview.setWebViewClient(null);
        mWebview=null;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Message message=new Message();
            message.what=2;
            message.obj=mWebview.getUrl();
            mHandler.sendMessage(message);
        }
        if (!hasFocus) {
            Message message=new Message();
            message.what=1;
            message.obj=mWebview.getTitle();
            mHandler.sendMessage(message);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//			if(e1!=null){
//				float beginY = e1.getY();
//				float endY = e2.getY();
//				if(beginY-endY>60&& Math.abs(distanceY)>0){
//					editText.setVisibility(View.GONE);
//					btnsearch.setVisibility(View.GONE);
//
//				}else if(endY-beginY>60&&Math.abs(distanceY)>0){
//					editText.setVisibility(View.VISIBLE);
//					btnsearch.setVisibility(View.VISIBLE);
//
//				}
//			}
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1!=null){
            float beginY = e1.getY();
            float endY = e2.getY();
            if(beginY-endY>40&& Math.abs(velocityY)>0){
                editText.setVisibility(View.GONE);
                btnsearch.setVisibility(View.GONE);
                table.setVisibility(View.GONE);
//					mWebview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//					boolean isDp=false;
                setViewMargin(mWebview,0,0,0,0);
                handler.removeCallbacks(mHidePart2Runnable);
                handler.post(mHidePart2Runnable);

            }else if(endY-beginY>40&&Math.abs(velocityY)>0){
                editText.setVisibility(View.VISIBLE);
                btnsearch.setVisibility(View.VISIBLE);
                table.setVisibility(View.VISIBLE);
//					boolean isDp=false;
                setViewMargin(mWebview,0,0,0,130);
                handler.removeCallbacks(mShowPart2Runnable);
                handler.post(mShowPart2Runnable);
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
//			mWebview.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    /**
     * @param view
     * @param left
     * @param right
     * @param top
     * @param bottom
     * @return
     */
    public ViewGroup.LayoutParams setViewMargin(View view,int left, int right, int top, int bottom) {
        if (view == null) {
            return null;
        }
        int leftPx = left;
        int rightPx = right;
        int topPx = top;
        int bottomPx = bottom;
        ViewGroup.LayoutParams params = view.getLayoutParams();
//		    int bttom=view.getLayoutParams().;
        ViewGroup.MarginLayoutParams marginParams = null;
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }
//		    if (!isDp) {
//		        leftPx = px2dp(this, left);
//		        rightPx = px2dp(this,right);
//		        topPx = px2dp(this,top);
//		        bottomPx = px2dp(this,bottom);
//		    }
//		    if (bttomflag) {
//		    	bottom=marginParams.bottomMargin;
//		    	bttomflag=false;
//			}
        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
        view.setLayoutParams(marginParams);
        return marginParams;
    }

//		 public int px2dp(Context context, float pxValue) {
//		        final float scale = context.getResources().getDisplayMetrics().density;
//		        return (int) (pxValue / scale + 0.5f);
//		    }

    private Handler handler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            bttomview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            bttomview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    };

}
