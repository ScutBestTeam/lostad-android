package com.lostad.app.demo;

import android.os.Environment;


public class IConst {
   public final static String PATH_ROOT                   = Environment.getExternalStorageDirectory().toString()+ "/1_tour";
   public static final String    DB_NAME                  = "tour_0107.db";
   public static final int       DB_VER_NUM               = 1;

   public final static String URL_BASE                    = "http://47.94.194.219:8080/FH-WEB2";//"
   public final static String URL_BASE2                   = "http://47.94.194.219:8080/test/";
   public final static String KEY_GIS_PROVINCE                 = "province";
   public final static String KEY_GIS_CITY                     = "city";
   public final static String KEY_GIS_DISTRICT                 = "district";
   public final static String KEY_GIS_LATITUDE                 = "latitude";
   public final static String KEY_GIS_LONGTITUDE               = "longitude";
   public final static int    VALUE_ROWS                       = 15;//分页，一次加载15条
   
//   public static final String  ALIYUN_OSS_AK1            = "";
//   public static final String  ALIYUN_OSS_SK             = "";
//   public static final String  GlobalDefaultHostId       = "oss-cn-qingdao.aliyuncs.com";
//   public static final String  ALIYUN_OSS_URL_ENDPOINT   = "http://"+GlobalDefaultHostId+"/";
   public static final String  ALIYUN_OSS_BUCKET_NAME          = "test";
   public static final String  ALIYUN_OSS_KEY_PREFIX_HEDAD     = "pber/head";
   public static final String  ALIYUN_OSS_KEY_PREFIX_SHARE     = "pber/share";
   public static final String  ALIYUN_OSS_KEY_PREFIX_SPORT     = "pber/hd";
   public static final String  URL_IMG_SPORT                   = "http://test.oss-cn-hangzhou.aliyuncs.com/pber/hd.jpg";
   //socket广播
   public static final String ACTION_SOCKET_DATA               = "action_socket_data";


   public static final  String PACKAGE_TYPE_GROUP = "1004_TG";
   public static final  String API_PROTOCOL="";

   public static  final String API_LOGIN          = "/appuser/login";
   public static  final String API_REGISTER       = "/appuser/register";
   public static  final String API_USER_UPDATE    = "/appuser/modify";
   public static  final String API_PWD_UPDATE     = "/appuser/modifyPsw";//phone/old/new
   public static  final String API_PWD_FIND       = "/appuser/resetPsw";//phone/new
}
/**
http://121.42.25.194:88/vtSell/getGgxxApi

http://121.42.25.194:88//vtSell/getSpxxAjava.lang.Stringpi

http://121.42.25.194:88//vtSell/getSpPjxxApi
*/	