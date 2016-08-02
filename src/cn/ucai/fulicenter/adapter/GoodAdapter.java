package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.ImageLoader;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodBean;

/**
 * Created by sks on 2016/8/2.
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    String footerText;
    ViewGroup parent;
    ArrayList<NewGoodBean> mGoodsList;
    static final int ITEM_FOOTER=0;
    boolean more;

    static final int ITEM_GOODS=1;

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public GoodAdapter(Context mContext, ArrayList<NewGoodBean> mGoodsList) {
        this.mContext = mContext;
        this.mGoodsList = mGoodsList;
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
            case ITEM_GOODS:
                layout=LayoutInflater.from(mContext).inflate(R.layout.item_good,null);
                holder=new NewGoodsViewHolder(layout);
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
          if(getItemViewType(position)==ITEM_FOOTER){
              ((FooterViewHlder)holder).tvFooter.setText(footerText);
              return;
          }
        NewGoodsViewHolder viewHolder = (NewGoodsViewHolder) holder;
        NewGoodBean goods = mGoodsList.get(position);
        viewHolder.tvGoodName.setText(goods.getGoodsBrief());
        viewHolder.tvPrice.setText(goods.getCurrencyPrice());
        ImageLoader.build()
                .url(I.SERVER_ROOT)
                .addParam(I.KEY_REQUEST,I.REQUEST_DOWNLOAD_ALBUM_IMG)
                .addParam("img_url",goods.getGoodsImg())
                .width(180)
                .height(200)
                .imageView(viewHolder.ivNewGoods)
                .defaultPicture(R.drawable.default_image)
                .listener(parent)
                .showImage(mContext);
    }

    @Override
    public int getItemCount() {
        return mGoodsList==null?0:mGoodsList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==getItemCount()-1){
            return ITEM_FOOTER;
        }else {
            return ITEM_GOODS;
        }
    }

    class NewGoodsViewHolder extends RecyclerView.ViewHolder{
        ImageView ivNewGoods;
        TextView tvGoodName,tvPrice;

        public NewGoodsViewHolder(View itemView) {
            super(itemView);
            ivNewGoods= (ImageView) itemView.findViewById(R.id.niv_good_thumb);
            tvGoodName= (TextView) itemView.findViewById(R.id.tv_good_name);
            tvPrice= (TextView) itemView.findViewById(R.id.tv_good_price);
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
