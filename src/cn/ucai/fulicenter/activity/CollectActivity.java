package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuliCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.ImageLoader;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CollectAdapter;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.view.DisplayUtil;

public class CollectActivity extends BaseActivity {
    final static String TAG = NewGoodFragment.class.getSimpleName();
    Context mContext;
    static final int ACTION_DOWNLOAD=0;
    static final int ACTION_PULL_DOWN=1;
    static final int ACTION_PULL_UP=2;
    static final int PAGE_SIZE=10;
    static final int CAT_ID=0;
    int mPageId=0;
    CollectAdapter mAdapter;
    RecyclerView mrvNewGoods;
    SwipeRefreshLayout msrl;
    ArrayList<CollectBean> mGoodsList;
    TextView mtvRefreshHint,tvTitle;
    GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        mContext = this;
        initView();
        initData();
        setListener();
    }
    private void setListener() {
        setDownListener();
        setDownUpListener();
        setUpdateCollectListListener();
    }

    private void setDownUpListener() {
        mrvNewGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastPosition>=mAdapter.getItemCount()-1&&mAdapter.isMore()){
                    mPageId+=lastPosition;
                    findCollectList(ACTION_PULL_UP,mPageId);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPosition=mLayoutManager.findLastVisibleItemPosition();
            }
        });

    }

    private void setDownListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setEnabled(true);
                msrl.setRefreshing(true);
                mtvRefreshHint.setVisibility(View.VISIBLE);
                mPageId=1;
                findCollectList(ACTION_PULL_DOWN,mPageId);
            }
        });
    }

    private void initData() {
        String userName = FuliCenterApplication.getInstance().getUserName();
        Log.e(TAG,"userName="+userName);
        if(userName.isEmpty())finish();
        findCollectList(ACTION_DOWNLOAD,mPageId);

    }

    private void findCollectList(final int action, int pageId) {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME, FuliCenterApplication.getInstance().getUserName())
                .addParam(I.PAGE_ID,pageId+"")
                .addParam(I.PAGE_SIZE,PAGE_SIZE+"")
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG,"1111111111111");
                        Log.e(TAG,"result="+result);
                        Gson gson = new Gson();
                        CollectBean[] array = gson.fromJson(result, CollectBean[].class);
                        mAdapter.setMore(!(array==null||array.length==0));
                        if(!mAdapter.isMore()){
                            if(action==ACTION_PULL_UP){
                                mAdapter.setFooterText("没有更多新货了");
                            }
                            return;
                        }
                        ArrayList<CollectBean> collectList = utils.array2List(array);
                        switch (action){
                            case ACTION_DOWNLOAD:
                                mAdapter.initGoodsList(collectList);
                                mAdapter.setFooterText("疯狂加载中");
                                break;
                            case ACTION_PULL_DOWN:
                                mAdapter.initGoodsList(collectList);
                                mAdapter.setFooterText("疯狂加载中");
                                msrl.setRefreshing(false);
                                mtvRefreshHint.setVisibility(View.GONE);
                                ImageLoader.release();
                                break;
                            case ACTION_PULL_UP:
                                mAdapter.addGoodsList(mGoodsList);
                                break;
                        }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }

    private void initView() {
        DisplayUtil.Back(CollectActivity.this,"收藏的宝贝");
        mrvNewGoods= (RecyclerView) findViewById(R.id.rv_collect);
        mGoodsList=new ArrayList<>();
        mAdapter=new CollectAdapter(mContext,mGoodsList);
        mrvNewGoods.setAdapter(mAdapter);
        mLayoutManager=new GridLayoutManager(mContext,2);
        mrvNewGoods.setLayoutManager(mLayoutManager);
        msrl = (SwipeRefreshLayout) findViewById(R.id.srl_collect);
        mtvRefreshHint= (TextView) findViewById(R.id.tvRefreshHint);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        String hahaha = getIntent().getStringExtra("hahaha");
        tvTitle.setText(hahaha);
    }
    class UpdateCollectListReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }
    UpdateCollectListReceiver mReceiver;
    private void setUpdateCollectListListener(){
        mReceiver = new UpdateCollectListReceiver();
        IntentFilter filter = new IntentFilter("update_collect_list");
        registerReceiver(mReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }
}
