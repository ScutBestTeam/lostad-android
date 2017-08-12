package com.lostad.app.demo.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lostad.app.base.view.BaseActivity;
import com.lostad.app.demo.IConst;
import com.lostad.app.demo.R;
import com.lostad.app.demo.entity.LoginConfig;
import com.lostad.app.demo.manager.UserManager;
import com.lostad.app.demo.task.LoginTask;
import com.lostad.applib.core.MyCallback;
import com.lostad.applib.entity.BaseBeanRsult;
import com.lostad.applib.util.DialogUtil;
import com.lostad.applib.util.LogMe;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 用户注册
 *
 * @Author sszvip@qq.com
 */

public class Register1Activity extends BaseActivity {
    @ViewInject(R.id.et_register_pass)
    private EditText et_register_pass;

    @ViewInject(R.id.et_register_repass)
    private EditText et_register_repass;
    @ViewInject(R.id.et_register_email)
    private EditText et_register_email;
    @ViewInject(R.id.et_register_name)
    private EditText et_register_name;

//    @ViewInject(R.id.cb)
//    private CheckBox cb_tyxy;

    public String mPhone;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        setTitle("用户注册");
        x.view().inject(this);
        super.initToolBarWithBack((Toolbar) findViewById(R.id.toolbar));

        mPhone = getIntent().getStringExtra("phone");
    }


//    @Event(R.id.tv_protocol)
//    private void onClickProtocal(View arg0) {
//        String url = IConst.URL_BASE + IConst.API_PROTOCOL;
//        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        startActivity(it);
//    }

    @Event(R.id.btn_register)
    private void onClickReg(View arg0) {
        register();
    }

    /**
     *
     */
    private void register() {

        final String psw = et_register_pass.getText().toString();
        String pew_next = et_register_repass.getText().toString();
        String email = et_register_email.getText().toString();
        String name = et_register_name.getText().toString();
        if ("".equals(email)) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(name)) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if ("".equals(psw)) {
            Toast.makeText(this, "请输入您的登录密码!", Toast.LENGTH_LONG).show();
            return;
        }
        if (psw.length() < 6) {
            Toast.makeText(this, "密码长度应该大于6位!", Toast.LENGTH_LONG).show();
            return;
        }

        if ("".equals(pew_next)) {
            Toast.makeText(this, "请输入您的确认密码!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!psw.equals(pew_next)) {
            Toast.makeText(this, "您两次输入的密码不一致，请确认!", Toast.LENGTH_LONG).show();
            return;
        }

//        if (!cb_tyxy.isChecked()) {
//            Toast.makeText(this, "请仔细阅读用户协议，并确认!", Toast.LENGTH_LONG).show();
//            return;
//        }

        final LoginConfig lc = new LoginConfig();

        DialogUtil.showProgress(this);
        doregister(mPhone,name,mPhone,email,psw);
    }

    public void doregister(String username, String name, String phone, String email, final String password) {
        try {
            Map m = new HashMap();
            m.put("USERNAME", username);
            m.put("PHONE", phone);
            m.put("NAME", name);
            m.put("EMAIL", email);
            m.put("PASSWORD", password);
            String url = IConst.URL_BASE + "/appuser/register";
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
                            Map result = new Gson().fromJson(response, HashMap.class);
                            if (result.get("result").equals("01")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUtil.dismissProgress();
                                         //注册成功后直接登陆
                                        LoginConfig lc=new LoginConfig();
                                            lc.password = password;
                                            lc.phone = mPhone;
                                            LoginTask lt = new LoginTask(Register1Activity.this, lc, new MyCallback<Boolean>() {
                                                @Override
                                                public void onCallback(Boolean success) {
                                                    if (success) {
                                                        toMainActivty();
                                                    }
                                                }
                                            });
                                            lt.execute();
                                    }
                                });
                            }
                            if (result.get("result").equals("00")){
                                DialogUtil.dismissProgress();
                                Toast.makeText(Register1Activity.this, "服务器错误", Toast.LENGTH_LONG).show();
                            }
                            if (result.get("result").equals("03")){
                                DialogUtil.dismissProgress();
                                Toast.makeText(Register1Activity.this, "请求参数不完整", Toast.LENGTH_LONG).show();
                            }
                            if (result.get("result").equals("04")){
                                DialogUtil.dismissProgress();
                                Toast.makeText(Register1Activity.this, "该手机已被注册", Toast.LENGTH_LONG).show();
                            }
                            if (result.get("result").equals("06")){
                                DialogUtil.dismissProgress();
                                Toast.makeText(Register1Activity.this, "手机未通过验证", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        }


    }