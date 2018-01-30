package com.lostad.app.demo.view.mainFragment;

/**
 * Created by zanjie on 2017/9/23.
 */
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.lostad.app.base.view.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.lostad.app.demo.DataPresenter ;
import com.lostad.app.demo.R;
import com.lostad.app.demo.MyApplication ;
import com.lostad.app.demo.view.PublishActivity ;
import com.lostad.app.demo.view.TweetDetailActivity;
import com.lostad.app.demo.adapter.TweetsAdapter;
import com.lostad.app.demo.event.SendTweetEvent;
import com.lostad.app.demo.Model.TweetInfo ;
import com.lostad.app.demo.network.NetworkManager;
import com.lostad.app.base.view.widget.RecyclerItemClickListener;

public class IntegrationFragmentDiscover extends BaseFragment
        implements DataPresenter.GetTweets{

    @Bind(R.id.tweets_recycler_list)
    RecyclerView tweetsRecyclerList;
    @Bind(R.id.add_tweets_fab)
    FloatingActionButton addTweetsFab;

    List<TweetInfo.TweetsEntity> tweetsList = new ArrayList<>();
    TweetsAdapter tweetsAdapter = new TweetsAdapter(tweetsList);
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;

    public IntegrationFragmentDiscover() {
        // Required empty public constructor
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendTweetEvent event){
        refreshData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        addTweetsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishActivity.actionStart(getActivity());
            }
        });

        refresher.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        tweetsRecyclerList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        tweetsRecyclerList.setLayoutManager(layoutManager);
        tweetsRecyclerList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onItemClickListener));
        tweetsRecyclerList.setItemAnimator(new DefaultItemAnimator());
        tweetsRecyclerList.setAdapter(tweetsAdapter);
        refreshData();

        return view;
    }

    @Override
    public void onGetTweets(TweetInfo info){
        refresher.setRefreshing(false);
        if(info.getResult().equals(NetworkManager.SUCCESS)){
            tweetsList.clear();
            for(TweetInfo.TweetsEntity entity : info.getTweets()){
                tweetsList.add(entity);
            }

            tweetsAdapter.notifyDataSetChanged();
        }

    }

    private void refreshData() {
        refresher.setRefreshing(true);
        DataPresenter.requestTweets(MyApplication.getCurrUser().getUserId(),
                "0", NetworkManager.TIME_NEW, IntegrationFragmentDiscover.this);
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    int i = tweetsList.size() - position - 1;
                    TweetInfo.TweetsEntity entity = tweetsList.get(i);
                    entity.setTweets_img(tweetsList.get(i).getTweets_img());
                    TweetDetailActivity.actionStart(getActivity(), entity);
                }
            };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

}
