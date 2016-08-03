package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.ImageLoader;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;


public class BoutiqueFragment extends Fragment {

    final static String TAG = NewGoodFragment.class.getSimpleName();
    Context mContext;
    static final int ACTION_DOWNLOAD=0;
    static final int ACTION_PULL_DOWN=1;
    static final int ACTION_PULL_UP=2;
    int mPageId=1;
    BoutiqueAdapter mAdapter;
    RecyclerView mrvBoutique;
    SwipeRefreshLayout msrl;
    ArrayList<BoutiqueBean> mBoutiqueList;
    TextView mtvRefreshHint;
    LinearLayoutManager mLayoutManager;

    public BoutiqueFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_boutique,container,false);
        mContext = layout.getContext();
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setDownListener();
    }



    private void setDownListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msrl.setEnabled(true);
                msrl.setRefreshing(true);
                mtvRefreshHint.setVisibility(View.VISIBLE);
                mPageId=1;
                downloadBoutiqueList(ACTION_PULL_DOWN,mPageId);
            }
        });
    }

    private void initData() {
        downloadBoutiqueList(ACTION_DOWNLOAD,mPageId);

    }

    private void downloadBoutiqueList(final int action, int pageId) {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG,"1111111111111");
                        Log.e(TAG,"result="+result);
                        Gson gson = new Gson();
                        BoutiqueBean[] array = gson.fromJson(result, BoutiqueBean[].class);
                        mAdapter.setMore(!(array==null||array.length==0));
                        if(!mAdapter.isMore()){
                            if(action==ACTION_PULL_UP){
                                mAdapter.setFooterText("没有更多精品了");
                            }
                            return;
                        }
                        mBoutiqueList = utils.array2List(array);
                        switch (action){
                            case ACTION_DOWNLOAD:
                                mAdapter.initGoodsList(mBoutiqueList);
//                                mAdapter.setFooterText("疯狂加载中");
                                break;
                            case ACTION_PULL_DOWN:
                                mAdapter.initGoodsList(mBoutiqueList);
//                                mAdapter.setFooterText("疯狂加载中");
                                msrl.setRefreshing(false);
                                mtvRefreshHint.setVisibility(View.GONE);
                                ImageLoader.release();
                                break;

                        }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }

    private void initView(View layout) {
        mrvBoutique= (RecyclerView) layout.findViewById(R.id.rv_new_good);
        mBoutiqueList=new ArrayList<>();
        mAdapter=new BoutiqueAdapter(getContext(),mBoutiqueList);
        mrvBoutique.setAdapter(mAdapter);
        mLayoutManager=new LinearLayoutManager(mContext);
        mrvBoutique.setLayoutManager(mLayoutManager);
        msrl = (SwipeRefreshLayout) layout.findViewById(R.id.srl_new_good);
        mtvRefreshHint= (TextView) layout.findViewById(R.id.tvRefreshHint);
    }



}
