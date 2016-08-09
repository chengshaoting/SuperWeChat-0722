package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.squareup.okhttp.internal.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuliCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.Utils;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.data.OkHttpUtils2;

/**
 * Created by sks on 2016/7/20.
 */
public class DownloadCartListTask {
    private static final String TAG = DownloadCartListTask.class.getSimpleName();
    String userName;
    Context mContext;
    public DownloadCartListTask(Context context, String userName) {
        mContext = context;
        this.userName = userName;
    }
    public void execute(){
        final OkHttpUtils2<CartBean[]> utils = new OkHttpUtils2<CartBean[]>();
        utils.url(I.SERVER_ROOTT)
                .addParam(I.KEY_REQUEST,I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,userName)
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] s) {
                        Log.e(TAG, "s=" + s);
                        if(s!=null){
                            ArrayList<CartBean> list = Utils.array2List(s);
                            List<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
                            for (final CartBean cart:list){
                                if (!cartList.contains(cart)){
                                    OkHttpUtils2<GoodDetailsBean> utils2 = new OkHttpUtils2<GoodDetailsBean>();
                                    utils2.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                                            .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(cart.getGoodsId()))
                                            .targetClass(GoodDetailsBean.class)
                                            .execute(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                                                @Override
                                                public void onSuccess(GoodDetailsBean result) {
                                                    cart.setGoods(result);
                                                }

                                                @Override
                                                public void onError(String error) {

                                                }
                                            });
                                    cartList.add(cart);
                                }else {
                                    cartList.get(cartList.indexOf(cart)).setChecked(cart.isChecked());
                                    cartList.get(cartList.indexOf(cart)).setCount(cart.getCount());
                                }
                            }
                            mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error=" + error);
                    }
                });
    }
}
