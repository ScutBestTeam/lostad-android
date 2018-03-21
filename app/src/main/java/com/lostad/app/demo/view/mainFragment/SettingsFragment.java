package com.lostad.app.demo.view.mainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lostad.app.base.view.fragment.BaseFragment;
import com.lostad.app.demo.DataPresenter;
import com.lostad.app.demo.Model.UserInfo;
import com.lostad.app.demo.MyApplication;
import com.lostad.app.demo.R;
import com.lostad.app.demo.network.NetworkManager;
import com.lostad.app.demo.util.StringUtils;
import com.lostad.app.demo.view.LoginActivity;
import com.lostad.app.demo.view.TweetDetailActivity;
import com.lostad.app.demo.view.my.FormMyInfoActivity;
import com.lostad.app.demo.view.my.ListMyTourActivity;
import com.lostad.applib.core.MyCallback;
import com.lostad.applib.entity.ILoginConfig;
import com.lostad.applib.util.DialogUtil;
import com.squareup.picasso.Picasso;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * @author sszvip
 * 
 */
public class SettingsFragment extends BaseFragment implements DataPresenter.GetUserInfo {

	private MyApplication mApp;

	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.iv_head)
	private ImageView iv_head;
	@ViewInject(R.id.tv_phone)
	private TextView tv_phone;

	@ViewInject(R.id.btn_quit)
	private TextView btn_quit;
	ILoginConfig mLogin;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
		x.view().inject(this, rootView);
        mApp = (MyApplication)getApp();
		//注入view
		return rootView;
	}


	@Override
	public void onGetUserInfo(UserInfo info) {
		ImageOptions mImageOptions = new ImageOptions.Builder()
				// 加载中或错误图片的ScaleType
				//.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
				// 默认自动适应大小
				// .setSize(...)
				.setFailureDrawableId(R.mipmap.load_fail)
				.setLoadingDrawableId(R.mipmap.ic_launcher)
				.setIgnoreGif(false)
				.setUseMemCache(true)
				.setImageScaleType(ImageView.ScaleType.CENTER).build();

		x.image().bind(iv_head, info.getImg_url(), mImageOptions);
		tv_phone.setText(info.getPhone());
		tv_name.setText(info.getName());
	}

	@Override
	public void onResume() {
		super.onResume();
//		mLogin = getLoginConfig();
//		if(mLogin!=null){
//			tv_name.setText(getLoginConfig().getName());
//			tv_phone.setText(getLoginConfig().getPhone());
//			btn_quit.setText("退出");
//		}else{
//			tv_name.setText("未登陆");
//			tv_phone.setText("手机号未知");
//			btn_quit.setText("注册/登陆");
//		}
		UserInfo userInfo = DataPresenter.requestUserInfoFromCache(MyApplication.getCurrUser().getUserId());
		if (false) {

			ImageOptions mImageOptions = new ImageOptions.Builder()
					// 加载中或错误图片的ScaleType
					//.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
					// 默认自动适应大小
					// .setSize(...)
					.setFailureDrawableId(R.mipmap.load_fail)
					.setLoadingDrawableId(R.mipmap.ic_launcher)
					.setIgnoreGif(false)
					.setUseMemCache(true)
					.setImageScaleType(ImageView.ScaleType.CENTER).build();

			x.image().bind(iv_head, userInfo.getImg_url(), mImageOptions);
			tv_phone.setText(userInfo.phone);
			tv_name.setText(userInfo.getName());
		} else {
			DataPresenter.requestUserInfoById(MyApplication.getCurrUser().getUserId(), SettingsFragment.this);
		}
	}

	@Event(R.id.ll_userinfo)
	private void onClickUserInfo(View v){
		//赞杰假装已经登录
		/*if(mLogin==null){
             toLoginActivity();
		}else{*/
		if(true){
			try {
				Intent i =  new Intent(ctx, FormMyInfoActivity.class);
				startActivity(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}



	@Event(R.id.ll_10)
	private void onClick_ll_11(View v){
		if(mLogin==null){
			toLoginActivity();
		}else {
//			Intent i = new Intent(ctx, ListMyOrderActivity.class);
//			startActivity(i);
		}
	}
	@Event(R.id.ll_11)
	private void onClick_ll_12(View v){
		//赞杰假装已经登录
		/*if(mLogin==null){
			toLoginActivity();
		}else {*/
		if(true){
			Intent i = new Intent(ctx, ListMyTourActivity.class);
			startActivity(i);
		}
	}


	@Event(R.id.ll_20)
	private void onClick_ll_20(View v){
		if(mLogin==null){
			toLoginActivity();
		}else {
//			Intent i = new Intent(ctx, ListUserActivity.class);
//			startActivity(i);
		}
	}

	@Event(R.id.ll_21)
	private void onClick_ll_21(View v){
		if(mLogin==null){
			toLoginActivity();
		}else {
//			Intent i = new Intent(ctx, LoginActivity.class);
//			startActivity(i);
	    }
	}

	@Event(R.id.ll_30)
	private void onClick_ll_30(View v){
		if(mLogin==null){
			toLoginActivity();
		}else {
			Intent i = new Intent(ctx, LoginActivity.class);
			startActivity(i);
		}
	}


	@Event(R.id.btn_quit)
	private void onClickQuit(View v){
		if(mLogin==null) {
			toLoginActivity();
		}else{
			DialogUtil.showAlertYesNo(ctx, "确定要退出吗？", new MyCallback<Boolean>() {
				@Override
				public void onCallback(Boolean yes) {
					if (yes) {
						mApp.quit(true);
					}
				}
			});
		}

	}

	private void toLoginActivity(){
		Intent i = new Intent(ctx, LoginActivity.class);
		startActivity(i);
	}

}
