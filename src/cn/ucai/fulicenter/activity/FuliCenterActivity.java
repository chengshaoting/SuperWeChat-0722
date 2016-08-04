package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodAdapter;

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
        mFragment = new Fragment[3];
        mFragment[0] = new NewGoodFragment();
        mFragment[1] = new BoutiqueFragment();
        mFragment[2] = new CategoryFragment();

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
                index=3;
                break;
            case R.id.rbPersonalCenter:
                index=4;
                break;
        }
        if(index!=currentIndex){
            setRadioButtonstatus(index);
            currentIndex=index;
        }

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


}
