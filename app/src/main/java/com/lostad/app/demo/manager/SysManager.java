package com.lostad.app.demo.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lostad.app.base.view.BaseActivity;
import com.lostad.app.demo.IConst;
import com.lostad.app.base.util.RequestUtil;
import com.lostad.app.demo.MyApplication;
import com.lostad.app.demo.view.Register0Activity;
import com.lostad.applib.entity.BaseBeanRsult;
import com.lostad.applib.util.DialogUtil;
import com.lostad.applib.util.LogMe;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * @author sszvip@qq.com
 * @date 2015-10-21
 */
public class SysManager {
    private static SysManager instance;

    private SysManager() {

    }

    private static final String TAG = "SysManager";

    public static synchronized SysManager getInstance() {
        if (instance == null) {
            instance = new SysManager();
        }

        return instance;
    }


    /**
     * 获取验证码
     */
    public BaseBeanRsult getVerifyCode(String phone, final BaseActivity baseActivity) {
        final BaseBeanRsult c = new BaseBeanRsult(false, "连接服务器失败");
        Gson g = new Gson();
        try {
            Map m = new HashMap();
            m.put("PHONE", phone);

            String data = g.toJson(m);
            LogMe.d("data", data);
            String url = IConst.URL_BASE + "/appCode";
//			String j = RequestUtil.postJson(url,null, data);
            OkHttpUtils
                    .post()//
                    .params(m)
                    .url(url)//
                    .build()//
                    .connTimeOut(5000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            c.setMsg("已发送至你的手机");
                            c.setSuccess(true);
                            //更新ui进程
                            baseActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtil.dismissProgress();
                                    Toast.makeText(baseActivity, c.getMsg(), Toast.LENGTH_LONG).show();
                                    if (c.isSuccess()) {
                                        baseActivity.startToRecordTime();
                                    }
                                }
                            });
                        }
                    });
            LogMe.d("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
//弃用
//    public BaseBeanRsult getVerifyCodeForUpdatePwd(String phone) {
//        BaseBeanRsult c = null;
//        Gson g = new Gson();
//        try {
//
//            Map m = new HashMap();
//            m.put("phone", phone);
//
//            String data = g.toJson(m);
//            LogMe.d("data", data);
//            String url = IConst.URL_BASE + "  ";
//            String j = RequestUtil.postJson(url, null, data);
//            LogMe.d("data", data);
//            c = g.fromJson(j, BaseBeanRsult.class);
//            if (c == null) {
//                c = new BaseBeanRsult(false, "服务器返回数据异常");
//            }
//        } catch (Exception e) {
//            c = new BaseBeanRsult(false, "服务器返回数据异常！" + e.getMessage());
//            e.printStackTrace();
//        }
//        return c;
//    }

    public BaseBeanRsult validateCode(final String phone, String code, final BaseActivity baseActivity) {
        final BaseBeanRsult c = new BaseBeanRsult(false, "");
        Gson g = new Gson();
        try {

            Map m = new HashMap();
            m.put("PHONE", phone);
            m.put("RCODE", code);
            String data = g.toJson(m);
            LogMe.d("data", data);
            String url = IConst.URL_BASE + "/appCode/checkRcode";
            OkHttpUtils
                    .post()//
                    .url(url)//
                    .params(m)
                    .build()//
                    .connTimeOut(3000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Map result = new Gson().fromJson(response, HashMap.class);
                            if (result.get("result").equals("1"))
                                c.setSuccess(true);
                            else
                                c.setMsg("验证码错误");
                            baseActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtil.dismissProgress();
                                    if (c.isSuccess()) {
                                        Toast.makeText(baseActivity, "验证通过!", Toast.LENGTH_LONG).show();
                                        baseActivity.toNextActivity(phone);
                                    } else {
                                        Toast.makeText(baseActivity, c.getMsg(), Toast.LENGTH_LONG)
                                                .show();

                                    }
                                }
                            });
                        }
                    });
            LogMe.d("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }
//弃用
//    public BaseBeanRsult validateCodeForUpdatePwd(String phone, String code) {
//        BaseBeanRsult c = null;
//        Gson g = new Gson();
//        try {
//            Map m = new HashMap();
//
//            String data = g.toJson(m);
//            LogMe.d("data", data);
//            String url = IConst.URL_BASE + "  ";
//            String j = RequestUtil.postJson(url, null, data);
//            LogMe.d("data", data);
//            c = g.fromJson(j, BaseBeanRsult.class);
//            if (c == null) {
//                c = new BaseBeanRsult(false, "服务器返回数据异常");
//            }
//        } catch (Exception e) {
//            c = new BaseBeanRsult(false, "服务器返回数据异常！" + e.getMessage());
//            e.printStackTrace();
//        }
//        return c;
//    }

    public void logout(final MyApplication myApplication) {
        try {
            Map m = new HashMap();
            String url = IConst.URL_BASE + "/appuser/logout";
            OkHttpUtils
                    .post()//
                    .url(url)//
                    .build()//
                    .connTimeOut(3000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            Map result = new Gson().fromJson(response, HashMap.class);
                            if(result.get("result").equals(true))
                            myApplication.dbquit();
                            else myApplication.dbquit();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
