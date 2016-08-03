package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

public class GoodDetailsActivity extends Activity {
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

    }
}
