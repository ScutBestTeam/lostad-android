package com.lostad.app.demo.util;

import android.app.Activity;
import android.content.Intent;

import com.lostad.app.demo.MyApplication;
import com.lostad.app.demo.entity.LoginConfig;
import com.lostad.app.demo.view.LoginActivity;

import org.xutils.ex.DbException;

/**
 * Created by hts on 2017/8/16.
 */

public class AuthUtil {
    /**
     * 有可能服务器session过期导致需要重登录，每次url请求callback都要有401 未登录result的处理，即以下方法
     * @param myApplication
     * @param activity
     * @throws DbException
     */
    public static void relogin(MyApplication myApplication,Activity activity) throws DbException {
    myApplication.getDb().delete(LoginConfig.class);
    Intent intent=new Intent(activity,LoginActivity.class);
    activity.startActivity(intent);
}
}
