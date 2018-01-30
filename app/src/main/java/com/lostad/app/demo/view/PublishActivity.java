package com.lostad.app.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import com.lostad.app.demo.DataPresenter ;
import com.lostad.app.demo.R;
import com.lostad.app.demo.MyApplication ;
import com.lostad.app.demo.event.SendTweetEvent;
import com.lostad.app.demo.Model.ImageInfo;
import com.lostad.app.demo.Model.ResultInfo ;
import com.lostad.app.demo.network.NetworkManager;
import com.lostad.app.demo.util.GlobalUtils;
import com.lostad.app.demo.widget.enter.EmojiFragment;
import com.lostad.app.demo.widget.enter.EnterEmojiLayout;
import com.lostad.app.demo.widget.enter.EnterLayout;
import com.lostad.app.base.view.BaseActivity;

public class PublishActivity extends BaseActivity
        implements EmojiFragment.EnterEmojiLayout, DataPresenter.SendTweet {

    @Bind(R.id.lay_photo_container)
    LinearLayout layPhotoContainer;
    @Bind(R.id.popPhoto)
    ImageButton btnPopPhoto;
    @Bind(R.id.btn_send)
    ImageButton btnSend;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.inputLayout)
    LinearLayout inputLayout;
    @Bind(R.id.bottom_layout)
    LinearLayout bottomLayout;

    ArrayList<String> mSelectPath;
    ArrayList<ImageInfo> mData = new ArrayList<>();
    EnterEmojiLayout enterLayout;
    EditText msgEdit;


    private boolean mFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);

        btnPopPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelectorActivity.startSelect(PublishActivity.this, 2, 9,
                        MultiImageSelectorActivity.MODE_MULTI);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgEdit.getText().toString().equals("")) {
                    Toast.makeText(PublishActivity.this, "请说点什么吧~", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgress(true);
                DataPresenter.sendTweet(MyApplication.getCurrUser().getUserId(),
                        msgEdit.getText().toString(), mData, PublishActivity.this);
            }
        });

        initEnter();

        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.colorPrimary));
        progressBar.setIndeterminateDrawable(threeBounce);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
//                this.finish();
            default:
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSendTweet(ResultInfo info) {
        showProgress(false);
        if (info.getResult() != null && info.getResult().equals(NetworkManager.SUCCESS)) {
            msgEdit.setText("");
            mData.clear();
            layPhotoContainer.removeAllViews();
            GlobalUtils.popSoftkeyboard(PublishActivity.this, msgEdit, false);
            Toast.makeText(PublishActivity.this, getString(R.string.send_tweet_success)
                    , Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new SendTweetEvent());
            PublishActivity.this.finish();
        } else {
            Toast.makeText(PublishActivity.this, getString(R.string.error_network)
                    , Toast.LENGTH_SHORT).show();
        }
    }

    private void initEnter() {
        enterLayout = new EnterEmojiLayout(this, null);
        msgEdit = enterLayout.content;
        enterLayout.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterLayout.popKeyboard();
            }
        });
        enterLayout.content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFirstFocus && hasFocus) {
                    mFirstFocus = false;
                    enterLayout.popKeyboard();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        enterLayout.closeEnterPanel();
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public EnterLayout getEnterLayout() {
        return enterLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                StringBuilder sb = new StringBuilder();
                layPhotoContainer.removeAllViews();
                for (String p : mSelectPath) {
//                    sb.append(p);
//                    sb.append("\n");

                    View itemView = View.inflate(PublishActivity.this, R.layout.item_publish_photo, null);
                    ImageView img = (ImageView) itemView.findViewById(R.id.img);
                    itemView.setTag(p);

                    Picasso.with(PublishActivity.this)
                            .load(new File(p))
                            .resize(200, 200)
                            .centerCrop()
                            .into(img);
                    if (layPhotoContainer != null) {
                        layPhotoContainer.addView(itemView,
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                    }

                    mData.add(new ImageInfo(p));
                }
                //yjPublishEdit.setText(sb.toString());
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            inputLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            bottomLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        } else {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            inputLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            bottomLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PublishActivity.class);
        context.startActivity(intent);
    }
}
