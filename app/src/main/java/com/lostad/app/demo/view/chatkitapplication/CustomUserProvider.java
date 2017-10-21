package com.lostad.app.demo.view.chatkitapplication;

import android.widget.Toast;

import com.google.gson.Gson;
import com.lostad.app.demo.IConst;
import com.lostad.app.demo.MyApplication;
import com.lostad.app.demo.entity.LoginConfig;
import com.lostad.app.demo.task.LoginTask;
import com.lostad.app.demo.view.Register1Activity;
import com.lostad.applib.core.MyCallback;
import com.lostad.applib.util.DialogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wli on 15/12/4.
 * 实现自定义用户体系
 */
public class CustomUserProvider implements LCChatProfileProvider {

  private static CustomUserProvider customUserProvider;

  public synchronized static CustomUserProvider getInstance() {
    if (null == customUserProvider) {
      customUserProvider = new CustomUserProvider();
    }
    return customUserProvider;
  }

  private CustomUserProvider() {
  }

  private static List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();

  @Override
  public void fetchProfiles(final List<String> list, final LCChatProfilesCallBack callBack) {
    String url = IConst.URL_BASE;
      String params=new Gson().toJson(list);
    OkHttpUtils
            .post()//
            .url(url)//
            .addParams("userList",params)
            .build()//
            .connTimeOut(5000)
            .execute(new StringCallback() {

              @Override
              public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
              }

              @Override
              public void onResponse(String response, int id) {
                  List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
                  userList = new Gson().fromJson(response, List.class);
                callBack.done(userList, null);
              }
            });


  }

}