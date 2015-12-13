package me.zq.youjoin.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zq.youjoin.R;
import me.zq.youjoin.YouJoinApplication;
import me.zq.youjoin.activity.PublishActivity;
import me.zq.youjoin.model.TweetInfo;
import me.zq.youjoin.network.NetworkManager;
import me.zq.youjoin.network.ResponseListener;
import me.zq.youjoin.utils.LogUtils;
import me.zq.youjoin.widget.recycler.RecyclerItemClickListener;

public class TweetsFragment extends BaseFragment {

    @Bind(R.id.tweets_recycler_list)
    RecyclerView tweetsRecyclerList;
    @Bind(R.id.add_tweets_fab)
    FloatingActionButton addTweetsFab;

    List<TweetInfo.TweetsEntity> tweetsList = new ArrayList<>();
    TweetsAdapter tweetsAdapter = new TweetsAdapter(tweetsList);
    @Bind(R.id.refresher)
    SwipeRefreshLayout refresher;

    public TweetsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yj_fragment_tweets, container, false);

        ButterKnife.bind(this, view);

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

    private void refreshData() {
        refresher.setRefreshing(true);
        NetworkManager.postRequestTweets(Integer.toString(YouJoinApplication.getCurrUser().getId()), "0",
                NetworkManager.TIME_NEW, new ResponseListener<TweetInfo>() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        LogUtils.e(TAG, volleyError.toString());
                        refresher.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(TweetInfo info) {
                        tweetsList = info.getTweets();
                        tweetsAdapter.setDataList(tweetsList);
                        tweetsAdapter.notifyDataSetChanged();
                        refresher.setRefreshing(false);
                    }
                });
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener =
            new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
//            Book book = mAdapter.getBook(position);
//            Intent intent = new Intent(getActivity(), BookDetailActivity.class);
//            intent.putExtra("book", book);
//
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                            view.findViewById(R.id.ivBook), getString(R.string.transition_book_img));
//
//            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                }
            };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
