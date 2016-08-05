package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.ImageLoader;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.view.DisplayUtil;

public class CategoryChildActivity extends BaseActivity {
    final static String TAG = NewGoodFragment.class.getSimpleName();
    Context mContext;
    static final int ACTION_DOWNLOAD=0;
    static final int ACTION_PULL_DOWN=1;
    static final int ACTION_PULL_UP=2;
    static final int PAGE_SIZE=10;
    static final int CAT_ID=0;
    int mPageId=0;
    GoodAdapter mAdapter;
    RecyclerView mrvNewGoods;
    SwipeRefreshLayout msrl;
    ArrayList<NewGoodBean> mGoodsList;
    TextView mtvRefreshHint,tvTitle;
    GridLayoutManager mLayoutManager;

    Button btnSortPrice,btnSortAddTime;
    boolean mSortPriceAsc;
    boolean mSortAddTimeAsc;
    int sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        mContext = this;
        sortBy = I.SORT_BY_ADDTIME_DESC;
        initView();
        initData();
        setListener();
    }
    private void setListener() {
        setDownListener();
        setDownUpListener();
        SortStatusChangedListener listener = new SortStatusChangedListener();
        btnSortPrice.setOnClickListener(listener);
        btnSortAddTime.setOnClickListener(listener);
    }

    private void setDownUpListener() {
        mrvNewGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastPosition>=mAdapter.getItemCount()-1&&mAdapter.isMore()){
                    mPageId+=lastPosition;
                    downloadNewGoodsList(ACTION_PULL_UP,mPageId);
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
                downloadNewGoodsList(ACTION_PULL_DOWN,mPageId);
            }
        });
    }

    private void initData() {
        downloadNewGoodsList(ACTION_DOWNLOAD,mPageId);

    }

    private void downloadNewGoodsList(final int action, int pageId) {
        int cat_id = getIntent().getIntExtra(I.NewAndBoutiqueGood.CAT_ID,0);
        final OkHttpUtils2<String> utils = new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam("cat_id",String.valueOf(cat_id))
                .addParam(I.PAGE_ID,pageId+"")
                .addParam(I.PAGE_SIZE,PAGE_SIZE+"")
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG,"1111111111111");
                        Log.e(TAG,"result="+result);
                        Gson gson = new Gson();
                        NewGoodBean[] array = gson.fromJson(result, NewGoodBean[].class);
                        mAdapter.setMore(!(array==null||array.length==0));
                        if(!mAdapter.isMore()){
                            if(action==ACTION_PULL_UP){
                                mAdapter.setFooterText("没有更多新货了");
                            }
                            return;
                        }
                        ArrayList<NewGoodBean> mGoodsList = utils.array2List(array);
                        switch (action){
                            case ACTION_DOWNLOAD:
                                mAdapter.initGoodsList(mGoodsList);
                                mAdapter.setFooterText("疯狂加载中");
                                break;
                            case ACTION_PULL_DOWN:
                                mAdapter.initGoodsList(mGoodsList);
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
        DisplayUtil.Back(CategoryChildActivity.this);
        mrvNewGoods= (RecyclerView) findViewById(R.id.rv_new_good);
        mGoodsList=new ArrayList<>();
        mAdapter=new GoodAdapter(mContext,mGoodsList);
        mrvNewGoods.setAdapter(mAdapter);
        mLayoutManager=new GridLayoutManager(mContext,2);
        mrvNewGoods.setLayoutManager(mLayoutManager);
        msrl = (SwipeRefreshLayout) findViewById(R.id.srl_category_child);
        mtvRefreshHint= (TextView) findViewById(R.id.tvRefreshHint);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        String hahaha = getIntent().getStringExtra("TITLE");
        tvTitle.setText(hahaha);
        btnSortAddTime= (Button) findViewById(R.id.btn_sort_addTime);
        btnSortPrice= (Button) findViewById(R.id.btn_sort_price);

    }
    private void sortByAddTime(){
        Collections.sort(mGoodsList, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean goodLeft, NewGoodBean goodRight) {
                return (int) (Long.valueOf(goodRight.getAddTime())-Long.valueOf(goodLeft.getAddTime()));
            }
        });
    }
    class SortStatusChangedListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_sort_price:
                    if(mSortPriceAsc){
                        sortBy=I.SORT_BY_PRICE_ASC;
                    }else {
                        sortBy=I.SORT_BY_PRICE_DESC;
                    }
                    mSortPriceAsc=!mSortPriceAsc;
                    break;
                case R.id.btn_sort_addTime:
                    if(mSortAddTimeAsc){
                        sortBy=I.SORT_BY_ADDTIME_ASC;
                    }else {
                        sortBy=I.SORT_BY_ADDTIME_DESC;
                    }
                    mSortAddTimeAsc = !mSortAddTimeAsc;
                    break;
            }
            mAdapter.setSortBy(sortBy);
        }
    }
}
