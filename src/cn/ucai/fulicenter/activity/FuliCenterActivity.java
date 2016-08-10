package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.FuliCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.UserAvatar;

import android.view.View;

/**
 * Created by sks on 2016/8/2.
 */
public class FuliCenterActivity extends BaseActivity {
    private static final String TAG = FuliCenterActivity.class.getSimpleName();
    RadioButton rbNewGood;
    RadioButton rbBoutique;
    RadioButton rbCategory;

    RadioButton rbCart;
    RadioButton rbPersonalCenter;
    TextView tvCartHint;
    RadioButton[] mrbArr;
    Fragment[] mFragment;

    int index;
    int currentIndex;
    ViewPager mViewPager;
    ViewPageAdapter mAdapter;

    public static final int ACTION_LOGIN=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_center_main);
        initFragment();
        initView();
        setListener();
    }

    private void setListener() {
    }

    private void initFragment() {
        mFragment = new Fragment[5];
        mFragment[0] = new NewGoodFragment();
        mFragment[1] = new BoutiqueFragment();
        mFragment[2] = new CategoryFragment();
        mFragment[3] = new CartFragment();
        mFragment[4] = new PersonCenterFragment();

    }
    class ViewPageAdapter extends FragmentPagerAdapter {
        Fragment[] fragments;
        public ViewPageAdapter(FragmentManager fm,Fragment[] fragments) {
            super(fm);
            this.fragments=fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
    private void initView() {
        rbBoutique= (RadioButton) findViewById(R.id.rbBoutique);
        rbCart= (RadioButton) findViewById(R.id.rbCart);
        rbCategory= (RadioButton) findViewById(R.id.rbCategory);
        rbNewGood= (RadioButton) findViewById(R.id.rbNewGoods);
        rbPersonalCenter= (RadioButton) findViewById(R.id.rbPersonalCenter);
        tvCartHint= (TextView) findViewById(R.id.tvCartHint);
        mrbArr = new RadioButton[5];
        mrbArr[0]= rbNewGood;
        mrbArr[1]= rbBoutique;
        mrbArr[2]= rbCategory;
        mrbArr[3]= rbCart;
        mrbArr[4]= rbPersonalCenter;


        mViewPager= (ViewPager) findViewById(R.id.vpPager);
        mAdapter=new ViewPageAdapter(getSupportFragmentManager(),mFragment);
        mViewPager.setAdapter(mAdapter);
    }

    public void onCheckedChange(View view){
        switch (view.getId()){
            case R.id.rbNewGoods:
                index=0;
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rbBoutique:
                index=1;
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rbCategory:
                index=2;
                mViewPager.setCurrentItem(2);
                break;
            case R.id.rbCart:
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    index = 3;
                } else {
                    gotoLogin();
                }
                mViewPager.setCurrentItem(3);
                break;
            case R.id.rbPersonalCenter:
                if(DemoHXSDKHelper.getInstance().isLogined()){
                    index=4;
                }else {
                    gotoLogin();
                }
                mViewPager.setCurrentItem(4);
                break;
        }
        if(index!=currentIndex){
            setRadioButtonstatus(index);
            currentIndex=index;
        }
    }
    private void gotoLogin(){
        startActivityForResult(new Intent(FuliCenterActivity.this,LoginActivity.class),ACTION_LOGIN);

    }

    private void setRadioButtonstatus(int index) {
        for (int i=0;i<mrbArr.length;i++){
            if(index==i){
                mrbArr[i].setChecked(true);
            }else {
                mrbArr[i].setChecked(false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ACTION_LOGIN){
            if(DemoHXSDKHelper.getInstance().isLogined()){

            }else {
                setRadioButtonstatus(currentIndex);
                mViewPager.setCurrentItem(currentIndex);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        if(DemoHXSDKHelper.getInstance().isLogined()){

        }else {
            index=currentIndex;
            if (index==4){
                index=0;
                mViewPager.setCurrentItem(ACTION_LOGIN);
            }
        }

    }
}
