package com.kylin.kwahoo.util;

import java.io.File;

import android.os.Environment;

public class SdCardUtil {
	   public static final String FILEDIR = "/Kwahoo";
	   public static final String FILEPHOTO = "/photos";
	   public static final String FILEIMAGE = "/images";
	   public static final String FILECACHE = "/cache";
	   public static final String FILEUSER = "/user";
	   
	   public static boolean checkSdCard() {
		      if (Environment.getExternalStorageState().equals(
		           Environment.MEDIA_MOUNTED))
		        return true;
		      else
		        return false;
		   }
	   /*
	    */
		 public static String getSdPath(){  
			  return Environment.getExternalStorageDirectory()+"/";  
		 }
		 public static  void  createFileDir(String fileDir){
			 String path=getSdPath()+fileDir;
			 File path1=new File(path);
			    if(!path1.exists())
			    {
			       path1.mkdirs();
			    }
		}
		public static String getFiledir() {
			return FILEDIR+"/";
		}
		public static String getFilecache() {
			return getSdPath()+getFiledir()+FILECACHE+"/";
		}
	   
}
