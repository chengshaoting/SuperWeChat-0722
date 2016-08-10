package cn.ucai.fulicenter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.ImageLoader;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;


public class CartFragment extends Fragment {

    final static String TAG = NewGoodFragment.class.getSimpleName();
    Context mContext;
    static final int ACTION_DOWNLOAD=0;
    static final int ACTION_PULL_DOWN=1;
    int mPageId=1;
    CartAdapter mAdapter;
    RecyclerView mrvBoutique;
    SwipeRefreshLayout msrl;
    ArrayList<CartBean> mCartList;
    TextView mtvRefreshHint;
    LinearLayoutManager mLayoutManager;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cart,null);
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
                        CartBean[] array = gson.fromJson(result, CartBean[].class);
                        mAdapter.setMore(!(array==null||array.length==0));
                        if(!mAdapter.isMore()){
                            return;
                        }
                        mCartList = utils.array2List(array);


                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }

    private void initView(View layout) {
        mrvBoutique= (RecyclerView) layout.findViewById(R.id.rv_cart);
        mCartList=new ArrayList<>();
        mAdapter=new CartAdapter(getContext(),mCartList);
        mrvBoutique.setAdapter(mAdapter);
        mLayoutManager=new LinearLayoutManager(mContext);
        mrvBoutique.setLayoutManager(mLayoutManager);
        msrl = (SwipeRefreshLayout) layout.findViewById(R.id.srl_cart);
        mtvRefreshHint= (TextView) layout.findViewById(R.id.tvRefreshHint);
    }



}
