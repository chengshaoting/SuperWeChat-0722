package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.FuliCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.Utils;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.data.OkHttpUtils2;

/**
 * Created by sks on 2016/7/20.
 */
public class DownloadCollectCountTask {
    private static final String TAG = DownloadCollectCountTask.class.getSimpleName();
    String userName;
    Context mContext;
    public DownloadCollectCountTask(Context context, String userName) {
        mContext = context;
        this.userName = userName;
    }
    public void execute(){
        final OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.url(I.SERVER_ROOT)
                .addParam(I.KEY_REQUEST,I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Contact.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        Log.e(TAG, "s=" + msg);
                        if (msg != null) {
                            if(msg.isSuccess()){
                                FuliCenterApplication.getInstance().setCollectCount(Integer.valueOf(msg.getMsg())
                                );
                            }else {
                                FuliCenterApplication.getInstance().setCollectCount(0);
                            }
                           mContext.sendStickyBroadcast(new Intent("update_collect"));
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error=" + error);
                    }
                });
    }
}
