package com.goldenratio.commonweal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.goldenratio.commonweal.R;
import com.goldenratio.commonweal.adapter.HelpListViewAdapter;
import com.goldenratio.commonweal.adapter.HelpViewPagerAdapter;
import com.goldenratio.commonweal.bean.Help;
import com.goldenratio.commonweal.bean.Help_Top;
import com.goldenratio.commonweal.ui.activity.HelpDetailActivity;
import com.goldenratio.commonweal.ui.view.PullToRefreshListView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class
HelpFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ViewPager mViewPager;
    private PullToRefreshListView mListView;
    private CirclePageIndicator indicator;
    private Handler mHandler;
    private List<Help_Top> mList;
    private View mHeaderView;
    private LinearLayout mLlNoNet;
    private List<Help> mHelpLlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView();
        initData4Sp();
        mListView.setOnItemClickListener(this);
        return view;
    }

    /**
     * 从intent获取预加载的数据
     * 当数据不完整时，重新进行获取
     */
    private void initData4Sp() {
        Intent intent = getActivity().getIntent();
        mHelpLlist = (List<Help>) intent.getSerializableExtra("help");
        mList = (List<Help_Top>) intent.getSerializableExtra("top");
        if (mHelpLlist != null){
            mListView.setAdapter(new HelpListViewAdapter(getContext(), mHelpLlist));
        }else {
            initData(1);
        }
        if (mList != null){
            mViewPager = (ViewPager) mHeaderView.findViewById(R.id.vp_news_title);
            mViewPager.setAdapter(new HelpViewPagerAdapter(getContext(), mList));
            indicator.setViewPager(mViewPager);
            indicator.setSnap(true);
            topSliding();
        }else {
            initData(2);
        }
    }

    private View initView() {
        View view = View.inflate(getContext(), R.layout.fragment_help, null);
        mListView = (PullToRefreshListView) view.findViewById(R.id.lv_help);
        mLlNoNet = (LinearLayout) view.findViewById(R.id.ll_no_net);
        mLlNoNet.setOnClickListener(this);


        mHeaderView = View.inflate(getContext(), R.layout.view_help_hander, null);
        indicator = (CirclePageIndicator) mHeaderView.findViewById(R.id.indicator);
        Log.d("CN", "initView: ++++++++++++++++++++++++++++++++++++++");
        //头文件

        mListView.addHeaderView(mHeaderView);
        mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(0);
            }

            @Override
            public void onLoadMore() {
            }
        });
        return view;
    }

    /**
     * 初始化数据
     * @param flag 标志位  0-全部加载  1-加载项目  2-加载轮播数据
     */
    public void initData(int flag) {
        if (flag == 1 || flag == 0){
            BmobQuery<Help> bmobQuery = new BmobQuery<>();
            bmobQuery.order("-createdAt");
            bmobQuery.findObjects(new FindListener<Help>() {
                @Override
                public void done(List<Help> list, BmobException e) {
                    if (e == null) {
                        mHelpLlist = list;
//
//                mViewPager = (ViewPager) mHeaderView.findViewById(R.id.vp_news_title);
//                mViewPager.setAdapter(new HelpViewPagerAdapter(getContext(),list));
//                indicator.setViewPager(mViewPager);
//                indicator.setSnap(true);

                        mListView.setAdapter(new HelpListViewAdapter(getContext(), list));
                        mListView.onRefreshComplete();
                        hideLinearLayout();
                    } else {
                        mLlNoNet.setVisibility(View.VISIBLE);
                        Log.i("bmob", "下载失败：" + e.getMessage());
                        mListView.onRefreshComplete();
                    }
                }
            });
        }
        if (flag == 2 || flag == 0){
            BmobQuery<Help_Top> bmobQueryTop = new BmobQuery<>();
            bmobQueryTop.order("-createdAt");
            bmobQueryTop.findObjects(new FindListener<Help_Top>() {
                @Override
                public void done(List<Help_Top> list, BmobException e) {
                    if (e == null) {
                        mList = list;
                        mViewPager = (ViewPager) mHeaderView.findViewById(R.id.vp_news_title);
                        mViewPager.setAdapter(new HelpViewPagerAdapter(getContext(), list));
                        indicator.setViewPager(mViewPager);
                        indicator.setSnap(true);

                        topSliding();
                        hideLinearLayout();
                    } else {
                        mLlNoNet.setVisibility(View.VISIBLE);
                        Log.i("top", "查询失败：" + e.getMessage());
                        mListView.onRefreshComplete();
                    }
                }

            });
        }



    }
    // 获取头部数据


    public void topSliding() {
          /*
           实现图片轮播
         */

        if (mHandler == null) {
            mHandler = new android.os.Handler() {
                public void handleMessage(android.os.Message msg) {
                    int currentItem = mViewPager.getCurrentItem();
                    currentItem++;

                    if (currentItem > mList.size() - 1) {
                        currentItem = 0;// 如果已经到了最后一个页面,跳到第一页
                    }

                    mViewPager.setCurrentItem(currentItem);

                    mHandler.sendEmptyMessageDelayed(0, 3000);// 继续发送延时3秒的消息,形成内循环
                }

            };

            // 保证启动自动轮播逻辑只执行一次
            mHandler.sendEmptyMessageDelayed(0, 3000);// 发送延时3秒的消息
        }
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), HelpDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("HelpList", mHelpLlist.get(position - 2));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_no_net:
                initData(0);
                break;
        }
    }

    private void hideLinearLayout() {
        if (mLlNoNet.getVisibility() == View.VISIBLE) {
            mLlNoNet.setVisibility(View.GONE);
        }
    }
}