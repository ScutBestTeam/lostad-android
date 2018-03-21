package com.lostad.app.demo.view.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lostad.app.base.util.DownloadUtil;
import com.lostad.app.base.util.ImageChooserUtil;
import com.lostad.app.base.util.ImageTools;
import com.lostad.app.base.util.Validator;
import com.lostad.app.base.view.BaseActivity;
import com.lostad.app.base.view.component.BaseFormActivity;
import com.lostad.app.base.view.component.FormAddressActivity;
import com.lostad.app.base.view.component.FormNumActivity;
import com.lostad.app.base.view.component.FormTextActivity;
import com.lostad.app.base.view.component.FormTextChinaeseActivity;
import com.lostad.app.demo.IConst;
import com.lostad.app.demo.Model.ImageInfo;
import com.lostad.app.demo.R;
import com.lostad.app.demo.entity.LoginConfig;
//import com.lostad.app.demo.entity.UserInfo;
import com.lostad.app.demo.Model.UserInfo;
import com.lostad.app.demo.view.PublishActivity;
import com.lostad.applib.core.MyCallback;
import com.lostad.applib.util.DialogUtil;
import com.lostad.applib.util.FileDataUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.squareup.picasso.Picasso;

import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;

public class FormMyInfoActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.iv_head)
    private ImageView iv_head;

    @ViewInject(R.id.tv_nickname)
    private TextView tv_nickname;

    @ViewInject(R.id.tv_sex)
    private TextView tv_sex;

    @ViewInject(R.id.tv_weight)
    private TextView tv_weight;

    @ViewInject(R.id.tv_height)
    private TextView tv_height;

    @ViewInject(R.id.tv_age)
    private TextView tv_age;

    @ViewInject(R.id.tv_addr)
    private TextView tv_addr;

    //赞杰追加
    @ViewInject(R.id.tv_email)
    private TextView tv_email;

    @ViewInject(R.id.tv_birthday)
    private TextView tv_birthday;

    @ViewInject(R.id.tv_personalsign)
    private TextView tv_personalsign;

    @ViewInject(R.id.tv_school)
    private TextView tv_school;


    private LoginConfig mUserInfo = new LoginConfig();
    private File mFileHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_myinfo);
        x.view().inject(this);
        super.initToolBarWithBack(toolbar);
        setTitle("个人信息");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginConfig login = (LoginConfig)getLoginConfig();
        if(login!=null){
            mUserInfo = login;
            updateUI(login);
        }

    }

    @Event(R.id.iv_head)
    private void onClickHead(View v) {
//        ImageChooserUtil.showPicturePicker(this, true);
//      Intent i = new Intent(this,HeadGridActivity.class);
//      startActivityForResult(i, 100);
        PictureSelector.create(FormMyInfoActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio(
                .maxSelectNum(9)// 最大图片选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    //	public void onClickNext(View v) {
//		next();
//	}
    @Event(R.id.ll_nickname)
    private void onClickName(View v) {
        try {
            Intent i = new Intent(FormMyInfoActivity.this, FormTextChinaeseActivity.class);
            i.putExtra("value", tv_nickname.getText());
            i.putExtra(FormTextActivity.KEY_MAX_LEN, 12);
            startActivityForResult(i, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Event(R.id.ll_sex)
    private void onClickSex(View v) {
        try {
            String[] itemList = {"男", "女", "取消"};
            DialogUtil.showAlertMenuCust(ctx, "选择性别", itemList, new MyCallback<Integer>() {
                @Override
                public void onCallback(Integer index) {
                    if (0 == index) {
                        tv_sex.setText("男");
                        mUserInfo.setSex("0");
                    } else if (1 == index) {
                        tv_sex.setText("女");
                        mUserInfo.setSex("1");
                    } else {
                        tv_sex.setText("");
                        mUserInfo.setSex("");
                    }
                    update(mUserInfo);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Event(R.id.ll_age)
    private void onClick3(View v) {
        Intent i = new Intent(FormMyInfoActivity.this, FormNumActivity.class);
        i.putExtra(FormNumActivity.KEY_MAX_LEN, 2);
        i.putExtra(FormNumActivity.KEY_IS_INT, true);
        i.putExtra(FormNumActivity.KEY_MAX_VALUE, 65);
        i.putExtra(FormNumActivity.KEY_MIN_VALUE, 14);
        i.putExtra("value", tv_age.getText());
        i.putExtra("desc", "填写年龄可以让系统帮助您匹配更合适的驴友哟");
        i.putExtra(FormNumActivity.KEY_NULL_ABLE, false);
        startActivityForResult(i,3);
    }



    @Event(R.id.ll_weight)
    private void onClick5(View v) {
        Intent i = new Intent(ctx, FormNumActivity.class);
        i.putExtra(FormNumActivity.KEY_IS_INT, true);
        i.putExtra("value", tv_weight.getText());
        i.putExtra("desc", "填写体重，让系统对您的运动做出更合理的评估");
        i.putExtra(FormNumActivity.KEY_MIN_VALUE, 30);
        i.putExtra(FormNumActivity.KEY_MAX_VALUE, 150);
        i.putExtra(FormNumActivity.KEY_MAX_DESC, "体重不能大于150kg");
        i.putExtra(FormNumActivity.KEY_MIN_DESC, "体重不能小于30kg");

        i.putExtra(FormNumActivity.KEY_NULL_ABLE, false);
        startActivityForResult(i,5);
    }

    @Event(R.id.ll_address)
    private void onClickAddress(View v) {
        Intent i = new Intent(FormMyInfoActivity.this, FormAddressActivity.class);
        i.putExtra("value", tv_height.getText());
        i.putExtra("desc", "您所在的城市");
        startActivityForResult(i, 6);
    }

    private void update(LoginConfig u) {
        try {
            getMyApp().getDb().saveOrUpdate(u);
            new Thread(){
                @Override
                public void run() {

                }
            }.start();
            // commit to server 。。。
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(LoginConfig u) {
        try {
            tv_nickname.setText(u.getNickname());
            tv_sex.setText(u.getSex());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String d;
        switch (requestCode) {
            case 0:
                break;
            case 1:
                d = data.getStringExtra("data");
                mUserInfo.setNickname(d);
                if (!Validator.isBlank(d)) {
                    update(mUserInfo);
                }
                break;
            case 3:
                tv_age.setText(data.getStringExtra("data"));
//                d = data.getStringExtra(BaseFormActivity.KEY_VALUE);
//                mUserInfo. = (d);
//
//                if (!Validator.isBlank(d)) {
//                    update(mSysConfig);
//                }
                break;
            case PictureConfig.CHOOSE_REQUEST:

                if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                    if (resultCode == RESULT_OK) {
                        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                StringBuilder sb = new StringBuilder();
                        for (LocalMedia p : selectList) {
//                    sb.append(p);
//                    sb.append("\n")

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

                        x.image().bind(iv_head, p.getPath(), mImageOptions);
                        }
                        //yjPublishEdit.setText(sb.toString());
                    }
                }
            }


    }

    private void initUI(final UserInfo config) {

        if (config == null)
            return;

        if (Validator.isNotEmpty(config.avatarUrl)) {
            DownloadUtil.loadImage(this, iv_head, config.avatarUrl);
        }
        tv_nickname.setText(config.nickname);
        setSexValue(mUserInfo.sex);
    }


    public void setSexValue(String sex) {
        try {
            if (sex == null || "1".equals(sex)) {//默认是男
                tv_sex.setText("男");
            } else {
                tv_sex.setText("女");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//        ComponentName c = getCallingActivity();
//       
//    	if(!mSysConfig.isInfoReay()){
//	        getMenuInflater().inflate(R.menu.menu_pass, menu);
//		}
//
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		
//		if (item.getItemId() == R.id.action_next) {
//			next();
//		}
//
//		return super.onOptionsItemSelected(item);
//	}


    private void uploadHead(final File mFileSelected) {
        if (mFileSelected == null) {
            return;
        }
//		new Thread(){
//			public void run() {
//				final String key =IConst.ALIYUN_OSS_KEY_PREFIX_HEDAD+"/"+FileDataUtil.createJpgFileName(getLoginConfig().getId().toString());
//				FileManager.getInstance(ctx).resumableUpload(mFileSelected.getAbsolutePath(), key,new SaveCallback() {
//					@Override
//					public void onSuccess(final String objectKey) {
//
//						mSysConfig.setPicpath(objectKey);
//						LogMe.d(key+"<<<<<<<<<<<<<<<<"+objectKey);
//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								DialogUtil.showToasMsg(ctx, "上传成功！");
//								mSysConfig.setPicpath(objectKey);
//								update(mSysConfig);
//								String url = getMyApplication().getPicSSO()+objectKey;
//								//DownloadUtil.loadImage(iv_head, url,R.drawable.head_default, R.drawable.head_default, R.drawable.head_default);
//							}
//						});
//					}
//
//					@Override
//					public void onProgress(String objectKey, int byteCount, int totalSize) {
//					}
//
//					@Override
//					public void onFailure(String objectKey, OSSException ossException) {
//						ossException.printStackTrace();
//						runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								DialogUtil.showToasMsg(ctx, "上传失败！");
//								//getFinalBitmap().display(iv_head, mSysConfig.getPicpath());
//							}
//						});
//
//					}
//				});
//
//			};
//		}.start();

    }

//DownloadUtil.loadImage(iv_head, url,R.drawable.head_default, R.drawable.head_default, R.drawable.head_default);
}
