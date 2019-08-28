package com.kylin.kwahoo.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

/**
 *	调用APP的窗口
 */
public class MyDialog {
	Activity activity;
	Intent intent1;
	public MyDialog(Activity activity,Intent intent1) {
		super();
		this.activity = activity;
		this.intent1=intent1;
	}
	public void showDlg(){
		
		Builder builder=new Builder(activity);
		builder.setTitle("来自网页的提示");
        builder.setMessage("是否打开本地APP");
        builder.setPositiveButton("立即前往", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {	
            	try{
            		activity.startActivity(intent1);
                	Toast.makeText(activity, "正在打开", Toast.LENGTH_LONG).show();
            	}catch(Exception e){
            		Toast.makeText(activity, "没有安装此App", Toast.LENGTH_LONG).show();
            	}
            }
        });
        builder.setNegativeButton("留在此处", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            	Toast.makeText(activity, "本网页浏览", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
	}

}
