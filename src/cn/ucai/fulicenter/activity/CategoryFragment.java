package cn.ucai.fulicenter.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private static final String TAG = CategoryFragment.class.getSimpleName();
    Context mContext;
    ExpandableListView melvLayout;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    CategoryAdapter mAdapter;

    public CategoryFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        mContext =layout.getContext();
        initView(layout);
        initData();
        return layout;
    }

    private void initData() {
        getGroupList();
    }

    private void initView(View layout) {
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(mContext,mGroupList,mChildList);
        melvLayout= (ExpandableListView) layout.findViewById(R.id.elvLayout);
        melvLayout.setAdapter(mAdapter);
    }


    private void getGroupList() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<>();
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP);
        utils.targetClass(String.class);
        utils.execute(new OkHttpUtils2.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String result) {
                int i=0;
                Log.e(TAG, "result02=" + result);
                Gson gson = new Gson();
                final CategoryGroupBean[] array = gson.fromJson(result, CategoryGroupBean[].class);
                ArrayList<CategoryGroupBean> group = utils.array2List(array);
                Log.e(TAG, "group=" + group);
                for(CategoryGroupBean g:group) {
                    mChildList.add(new ArrayList<CategoryChildBean>());
                    Log.e(TAG, "id========"+g.getId());
                    getChild(i, group);
                    i++;
                }


            }

            @Override
            public void onError(String error) {

            }
        });
    }
    int groupcount=0;
    private void getChild(final int i, final ArrayList<CategoryGroupBean> group) {
        final OkHttpUtils2<String> utils2 = new OkHttpUtils2<String>();
        utils2.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam("parent_id", String.valueOf(group.get(i).getId()))
                .addParam(I.PAGE_ID, "1")
                .addParam(I.PAGE_SIZE, "100")
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        groupcount++;
                        Gson gson = new Gson();
                        CategoryChildBean[] categoryChildBeen = gson.fromJson(result, CategoryChildBean[].class);
                        ArrayList<CategoryChildBean> categoryChildBeen1 = utils2.array2List(categoryChildBeen);
                        ArrayList<ArrayList<CategoryChildBean>> child = new ArrayList<ArrayList<CategoryChildBean>>();
                        child.add(categoryChildBeen1);
                        mChildList.set(i, categoryChildBeen1);
                        Log.e(TAG, "child=" + child);
                        if (groupcount == group.size()) {
                            mAdapter.addItems(group,mChildList);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

}


















