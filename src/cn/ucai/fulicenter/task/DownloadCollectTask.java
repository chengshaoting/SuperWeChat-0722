package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.ucai.fulicenter.FuliCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;

/**
 * Created by sks on 2016/7/20.
 */
public class DownloadCollectTask {
    private static final String TAG = DownloadCollectTask.class.getSimpleName();
    String userName;
    Context mContext;
    int pageId = 0;
    int pageSize = 100;
    public DownloadCollectTask(Context context, String userName) {
        mContext = context;
        this.userName = userName;
    }
    public void execute(){
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.url(I.SERVER_ROOT)
                .addParam(I.KEY_REQUEST,I.REQUEST_FIND_COLLECTS)
                .addParam(I.Contact.USER_NAME,userName)
                .addParam(I.PAGE_ID,pageId+"")
                .addParam(I.PAGE_SIZE,pageSize+"")
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "s=" + s);
                        Gson gson = new Gson();
                        CollectBean[] array = gson.fromJson(s, CollectBean[].class);
                        ArrayList<CollectBean> collectBeen = utils.array2List(array);
                    }
                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error=" + error);
                    }
                });
    }
}
