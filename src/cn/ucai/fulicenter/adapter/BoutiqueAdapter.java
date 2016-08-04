package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.ImageLoader;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.NewGoodBean;

/**
 * Created by sks on 2016/8/2.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    String footerText;
    ViewGroup parent;
    ArrayList<BoutiqueBean> mBoutiqueList;
    static final int ITEM_FOOTER=0;
    boolean more;

    static final int ITEM_BOUTIQUE=1;

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> mBoutiqueList) {
        this.mContext = mContext;
        this.mBoutiqueList = mBoutiqueList;
        mBoutiqueList.addAll(mBoutiqueList);
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
        switch (viewType){
            case ITEM_FOOTER:
                layout= LayoutInflater.from(mContext).inflate(R.layout.item_footer,null);
                holder=new FooterViewHlder(layout);
                break;
            case ITEM_BOUTIQUE:
                layout=LayoutInflater.from(mContext).inflate(R.layout.item_boutique,null);
                holder=new NewBoutiqueViewHolder(layout);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
          if(getItemViewType(position)==ITEM_FOOTER){
              ((FooterViewHlder)holder).tvFooter.setText(footerText);
              return;
          }
        NewBoutiqueViewHolder viewHolder = (NewBoutiqueViewHolder) holder;
        final BoutiqueBean goods = mBoutiqueList.get(position);
        viewHolder.tvBoutiqueName.setText(goods.getName());
        viewHolder.tvBoutiqueTitle.setText(goods.getTitle());
        viewHolder.tvBoutiqueDetails.setText(goods.getDescription());
        ImageLoader.build()
                .url(I.SERVER_ROOT)
                .addParam(I.KEY_REQUEST,I.REQUEST_DOWNLOAD_BOUTIQUE_IMG)
                .addParam("imageurl",goods.getImageurl())
                .width(180)
                .height(200)
                .imageView(viewHolder.ivBoutiquethumb)
                .defaultPicture(R.drawable.default_image)
                .listener(parent)
                .showImage(mContext);
        viewHolder.layout_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,BoutiqueChildActivity.class)
                        .putExtra(D.GoodDetails.KEY_GOODS_ID,goods.getId()).putExtra("hahaha",goods.getTitle()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBoutiqueList==null?0:mBoutiqueList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return ITEM_FOOTER;
        }else {
            return ITEM_BOUTIQUE;
        }
    }
    public void initGoodsList(ArrayList<BoutiqueBean> mGoodsList) {
        this.mBoutiqueList.clear();
        this.mBoutiqueList.addAll(mGoodsList);
        notifyDataSetChanged();
    }
    public void addGoodsList(ArrayList<BoutiqueBean> mGoodsList) {
        this.mBoutiqueList.addAll(mGoodsList);
        notifyDataSetChanged();
    }

    class NewBoutiqueViewHolder extends RecyclerView.ViewHolder{
        ImageView ivBoutiquethumb;
        TextView tvBoutiqueName,tvBoutiqueTitle,tvBoutiqueDetails;
        LinearLayout layout_good;

        public NewBoutiqueViewHolder(View itemView) {
            super(itemView);
            ivBoutiquethumb= (ImageView) itemView.findViewById(R.id.iv_boutique_thumb);
            tvBoutiqueName= (TextView) itemView.findViewById(R.id.tv_boutique_name);
            tvBoutiqueTitle= (TextView) itemView.findViewById(R.id.tv_boutique_title);
            tvBoutiqueDetails= (TextView) itemView.findViewById(R.id.tv_boutique_details);
            layout_good = (LinearLayout) itemView.findViewById(R.id.layout_boutique);
        }
    }
    class FooterViewHlder extends RecyclerView.ViewHolder {
        TextView tvFooter;
        public FooterViewHlder(View itemView) {
            super(itemView);
            tvFooter= (TextView) itemView.findViewById(R.id.tvFooter);
        }
    }
}
