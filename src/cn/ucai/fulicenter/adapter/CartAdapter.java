package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.ImageLoader;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by sks on 2016/8/2.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ViewGroup parent;
    ArrayList<CartBean> mGoodsList;
    boolean more;
     int sortBy;


    public CartAdapter(Context mContext, ArrayList<CartBean> mGoodsList) {
        this.mContext = mContext;
        this.mGoodsList = mGoodsList;
        mGoodsList.addAll(mGoodsList);
    }
    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent=parent;
        RecyclerView.ViewHolder holder = null;
        View layout;
                layout=LayoutInflater.from(mContext).inflate(R.layout.item_cart,null);
                holder=new NewGoodsViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewGoodsViewHolder viewHolder = (NewGoodsViewHolder) holder;
        final CartBean cart = mGoodsList.get(position);
        viewHolder.tvCartName.setText(cart.getGoods().getGoodsName());
        viewHolder.tvPrice.setText(cart.getGoods().getCurrencyPrice());
        viewHolder.tvCartCount.setText("("+cart.getCount()+")");
        ImageUtils.setGoodThumb(mContext,viewHolder.ivCartThumb,cart.getGoods().getGoodsImg());
    }

    @Override
    public int getItemCount() {
        return mGoodsList==null?mGoodsList.size():0;
    }


    public void initGoodsList(ArrayList<CartBean> mGoodsList) {
        this.mGoodsList.clear();
        this.mGoodsList.addAll(mGoodsList);
        notifyDataSetChanged();
    }
    public void addGoodsList(ArrayList<CartBean> mGoodsList) {
        this.mGoodsList.addAll(mGoodsList);
        notifyDataSetChanged();
    }

    class NewGoodsViewHolder extends RecyclerView.ViewHolder{
        CheckBox cbSelected;
        ImageView ivCartThumb,ivCartAdd,ivCartDel;
        TextView tvCartName,tvCartCount,tvPrice;
        RelativeLayout mLayoutCart;
        public NewGoodsViewHolder(View itemView) {
            super(itemView);
            cbSelected= (CheckBox) itemView.findViewById(R.id.cb_cart_selected);
            ivCartThumb= (ImageView) itemView.findViewById(R.id.iv_cart_thumb);
            ivCartAdd= (ImageView) itemView.findViewById(R.id.iv_cart_add);
            ivCartDel= (ImageView) itemView.findViewById(R.id.iv_collect_delete);
            tvCartName= (TextView) itemView.findViewById(R.id.tv_cart_name);
            tvCartCount= (TextView) itemView.findViewById(R.id.tv_cart_count);
            tvPrice= (TextView) itemView.findViewById(R.id.tv_good_price);
            mLayoutCart= (RelativeLayout) itemView.findViewById(R.id.layout_cart);

        }
    }


}
