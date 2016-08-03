package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.view.DisplayUtil;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodDetailsActivity extends Activity {
    String TAG = GoodDetailsActivity.class.getSimpleName();
    ImageView ivShare;
    ImageView ivCollect;
    ImageView ivCart;
    TextView  tvCartCount;
    TextView  tvGoodEnglishName;
    TextView  tvGoodName;
    TextView  tvGoodPriceCurrent;
    TextView  tvGoodPriceShop;
    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    WebView wvGoodBrief;
    int mGoodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_details);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Cart.GOODS_ID,String.valueOf(mGoodId))
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG,"result==="+result);
                        Gson gson = new Gson();
                        GoodDetailsBean goodDetailsBean = gson.fromJson(result, GoodDetailsBean.class);
                        if(goodDetailsBean!=null){
                            showGoodDetails(goodDetailsBean);
                        }else {
                            Toast.makeText(getApplicationContext(),"获取信息失败",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getApplicationContext(),"获取信息失败",Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void showGoodDetails(GoodDetailsBean goodDetailsBean) {
        tvGoodEnglishName.setText(goodDetailsBean.getGoodsEnglishName());
        tvGoodName.setText(goodDetailsBean.getGoodsName());
        tvGoodPriceShop.setText("商品价格"+goodDetailsBean.getCurrencyPrice());
        tvGoodPriceCurrent.setText("商品当前价格"+goodDetailsBean.getCurrencyPrice());
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator,getAlbumImageUrl(goodDetailsBean),
                goodDetailsBean.getProperties()[0].getAlbums().length);
        wvGoodBrief.loadDataWithBaseURL(null,goodDetailsBean.getGoodsBrief(),D.TEXT_HTML,D.UTF_8,null);
    }

    private String[] getAlbumImageUrl(GoodDetailsBean goodDetailsBean) {
        String[] albumImageUrl = new String[]{};
        if(goodDetailsBean.getPromotePrice()!=null&&goodDetailsBean.getProperties().length>0){
            AlbumsBean[] albums = goodDetailsBean.getProperties()[0].getAlbums();
            albumImageUrl=new String[albums.length];
            for (int i=0;i<albumImageUrl.length;i++){
                albumImageUrl[i]=albums[i].getImgUrl();
            }
        }
        return albumImageUrl;

    }

    private void initData() {
        mGoodId=getIntent().getIntExtra(D.GoodDetails.KEY_GOODS_ID,0);
    }

    private void initView() {
        ivShare= (ImageView) findViewById(R.id.iv_good_share);
        ivCollect=(ImageView) findViewById(R.id.iv_good_collect);
        ivCart=(ImageView) findViewById(R.id.iv_good_cart);
        tvCartCount= (TextView) findViewById(R.id.tv_good_counts);
        tvGoodEnglishName= (TextView) findViewById(R.id.tv_good_EnglishName);
        tvGoodName= (TextView) findViewById(R.id.tv_good_name);
        tvGoodPriceCurrent= (TextView) findViewById(R.id.tv_price_current);
        tvGoodPriceShop= (TextView) findViewById(R.id.tv_price_shop);
        mSlideAutoLoopView= (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator= (FlowIndicator) findViewById(R.id.indicator);
        wvGoodBrief= (WebView) findViewById(R.id.wv_good);
        WebSettings settings = wvGoodBrief.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
        DisplayUtil.initBack(this);

    }
}
