package com.kylin.kwahoo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SplashActivity extends Activity {

	 private  final int SPLASH_DISPLAY_LENGHT = 3000;


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint({ "NewApi", "InlinedApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if(Build.VERSION.SDK_INT >= 21) {
				View decorView = getWindow().getDecorView();
			    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | 
			    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			    getWindow().setStatusBarColor(Color.TRANSPARENT);
			}
		setContentView(R.layout.activity_splash);
		final ImageView imageView = (ImageView) findViewById(R.id.imageView);
		final int GET_ERROR = 0;
		final int GET_OK  = 1;
		final Handler handler = new Handler(){  
			public void handleMessage(Message msg) {  
	            switch (msg.what) {  
	            case GET_OK:  
//	            	ImageView imageView = (ImageView) findViewById(R.id.imageView);
					imageView.setImageBitmap((Bitmap) msg.obj);
	                break;  
	            case GET_ERROR:
	            	Toast.makeText(SplashActivity.this, "网络出小差了", Toast.LENGTH_LONG).show();
	            	break;
	            }  
	        };  
	    }; 
		final File file = new File(getCacheDir(), "kwahoo.png");
		if(file.exists())
		{
			Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			
			imageView.setImageBitmap(bm);
		}
		else {
		    Thread thread = new Thread()
		    {
		    	@Override
		    	public void run() {
		    	    String path = "https://kwahoo.top/Kwahoo.png";
		    	    try {
		    	    URL url = new URL(path);
		    	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    	    conn.setRequestMethod("GET");
		    	    conn.setConnectTimeout(2000);
		    	    conn.setReadTimeout(2000);
		    	    conn.connect();
		    	    if(conn.getResponseCode() == 200)
		    	    {
		    	    	InputStream is = conn.getInputStream();
		    	    			
		    	    	FileOutputStream fos = new FileOutputStream(file);
		    	    	byte[] b = new byte[1024];
		    	    	int len = 0;
		    	    	while((len = is.read(b)) != -1)
		    	    	{
		    	    		fos.write(b, 0, len);
		    	    	}
		    	    	fos.close();
		    	    	is.close();
		    	    			
		    	    	Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
//		    	    	Message msg = handler.obtainMessage();
		    	    	Message msg = new Message();
		    	    	msg.obj = bm;
		    	    	msg.what = GET_OK;
		    	    	handler.sendMessage(msg);
		    	   }
		    	   else {
						Message msg = new Message();
						msg.what = GET_ERROR;
						handler.sendMessage(msg);
					}
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	}
		    }
		};
		thread.start();
	}

			new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,FullscreenActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        },SPLASH_DISPLAY_LENGHT);
	}
}
