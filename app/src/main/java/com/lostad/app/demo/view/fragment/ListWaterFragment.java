package com.lostad.app.demo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lostad.app.base.view.fragment.BaseFragment;
import com.lostad.app.demo.R;
import com.lostad.app.demo.entity.Tour;
import com.lostad.app.demo.entity.TourList4j;
import com.lostad.app.demo.view.tour.OrderPayActivity;
import com.lostad.applib.util.DialogUtil;
import com.lostad.applib.util.Validator;
import com.lostad.applib.view.widget.WaterDropListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sszvip
 * 
 */
public class ListWaterFragment extends BaseFragment implements WaterDropListView.IWaterDropListViewListener,AdapterView.OnItemClickListener {

	@ViewInject(R.id.lv_data)
	private WaterDropListView lv_data;

    //正在加载

	@ViewInject( R.id.tv_loading)
	private TextView tv_loading;

	@ViewInject(R.id.iv_loading)
	private ImageView iv_loading;

	private ListTourAdapter mAdapter;
	private List<Tour> mListData;
    private String mType = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater,container,savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_list_water, container, false);
		ViewUtils.inject(this, rootView);//注入
		mListData= new ArrayList<Tour>();
		mAdapter = new ListTourAdapter(mType,ctx, mListData);
		lv_data.setAdapter(mAdapter);
		lv_data.setWaterDropListViewListener(this);
		lv_data.setPullLoadEnable(true);

		loadData(false);
		return rootView;
	}
//////////////////////////////////////////////////////////////////
//  切换fragment界面时需要刷新时，打开这里 。此方法在fragment初始化后才有效
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		LogMe.d("fragment", this.getClass().getName() + "===========setUserVisibleHint  isVisibleToUser:" + isVisibleToUser);
//		if(isVisibleToUser && lv_data!=null) {//可见，且界面已经初始化
//			lv_data.setRefreshing();
//		} else {//
//
//		}
//	}
////////////////////////////////////////////////////////////////////

	@Override
	public void onRefresh() {
//		ExecutorService executorService = Executors.newSingleThreadExecutor();
//		executorService.execute(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(2000);
//					handler.sendEmptyMessage(1);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		});

		loadData(false);

	}

	@Override
	public void onLoadMore() {
        loadData(true);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Tour t = mListData.get(position-1);
		Intent i = new Intent(ctx,OrderPayActivity.class);
		i.putExtra("bean", t);
		startActivity(i);
	}


	 
    /**
     * 如果是下拉刷新，先不要清空数据，以免闪屏体验不好。
     * 上拉加载数据时，不清空数据
     * 功能描述:       isLoadMore 是否加载更多操作
     * @param:
     * @return:
     * @Author:      sszvip@qq.com
     * @Create Date: 2015-9-18下午5:10:16
     */
//	private void loadData(final boolean isLoadMore) {
//		showLoading();
//		new AsyncTask<Integer,Integer,TourList4j>(){
//			@Override
//			protected TourList4j doInBackground(Integer... integers) {
//				int start = 0;
//				if(isLoadMore){//加载更多
//					start = mListData.size();
//				}
//				TourList4j g4j = TourManager.getInstance().listTourAll(mType, start);
//
//				return g4j;
//			}
//
//			@Override
//			protected void onPostExecute(TourList4j g4j) {
//				if(g4j.isSuccess()) {
//					if(g4j.list!=null && g4j.list.size()>0){
//						if(isLoadMore==false){//如果是刷新数据
//							mListData.clear();//清空以前的
//							mAdapter.notifyDataSetChanged();
//						}
//						mListData.addAll(g4j.list);
//						mAdapter.notifyDataSetChanged();
//					}else{
//						lv_data.end();
//					}
//				} else {
//					DialogUtil.showToastCust(ctx, g4j.getMsg());
//
//				}
//				dismissLoding(isLoadMore,null);
//			}
//		}.execute(); //线程池里的线程逐个顺次执行
//	}

	private void loadData(final boolean isLoadMore) {
		showLoading();
        String url = "";
		new HttpUtils().send(HttpRequest.HttpMethod.GET,
				url,
				new RequestCallBack<TourList4j>() {
					@Override
					public void onSuccess(ResponseInfo<TourList4j> responseInfo) {
						TourList4j data4j = responseInfo.result;
						if(data4j.isSuccess()) {
							if(data4j.list.size()==0){//加载完毕，（也可以比较pageSize）
								lv_data.end();
							}else{//未加载完毕
								if(isLoadMore==false){//如果是刷新数据
									mListData.clear();//清空以前的
								}
								mListData.addAll(data4j.list);
								mAdapter.notifyDataSetChanged();
							}
						}
						dismissLoding(isLoadMore, data4j.getMsg());
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						dismissLoding(isLoadMore,null);
						DialogUtil.showToastCust(ctx, msg);
					}
				});
	}

	//////////////////加载效果,以下代码可以直接复制粘贴////////////////////////////////////////////////////////////////////////////////
	private void showLoading() {
		if (mListData == null || mListData.size() == 0) {
			iv_loading.setVisibility(View.GONE);

			tv_loading.setVisibility(View.VISIBLE);
			tv_loading.setText("正在加载...");
		}
	}


	private void dismissLoding(boolean isLoadMore,String msg) {
		try{
			if(isLoadMore==false){//如果是刷新数据
				lv_data.stopRefresh();
			}else{
				lv_data.stopLoadMore();
			}
//			if(mListData.size()==0){
//				lv_data.end();
//			}
			/// ((AnimationDrawable) iv_loading.getDrawable()).stop();
			if (mListData == null || mListData.size() == 0) {
				iv_loading.setVisibility(View.VISIBLE);
				tv_loading.setVisibility(View.VISIBLE);

				iv_loading.setImageResource(R.mipmap.img_no_data);
				if(Validator.isBlank(msg)){
					tv_loading.setText("加载数据失败！");
				}else{
					tv_loading.setText(msg);
				}
			} else {
				iv_loading.setVisibility(View.GONE);
				tv_loading.setVisibility(View.GONE);
			}


		}catch (Exception e){
			e.printStackTrace();
		}
	}
}