package com.example.zhihuapplication.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhihuapplication.Activity.MainActivity;
import com.example.zhihuapplication.R;
import com.example.zhihuapplication.ShortNewsData;

import java.util.ArrayList;
import java.util.TreeMap;

public class myRecyclerAdapter extends RecyclerView.Adapter {
    static final int HEAD_VIEW = 0;
    static final int DATE_VIEW =1;
    static final int NORMAL_VIEW = 2;
    static final int FOOT_VIEW=3;
    ArrayList<ShortNewsData> list ;
    MainActivity activityContext;
    String targetDate;
    TreeMap map;
    String TAG = "myTag";
    private OnItemClickListener mClicklistener;
    public myRecyclerAdapter(TreeMap map,MainActivity activityContext){
        this.map = map;
        list = (ArrayList<ShortNewsData>) map.get("latestDate");
        this.activityContext = activityContext;
        Log.d("lzx", "myRecyclerAdapter: "+map.get("latestDate"));
        list.add(0,new ShortNewsData(((ArrayList<ShortNewsData>) map.get("latestDate")).get(0).getDate()));
    }
    static class DateViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_date);
        }
    }
    static class NormalViewHolder extends RecyclerView.ViewHolder{
        private final TextView titleTextView;

        private final ImageView imageView;
        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_text_title);
            imageView = itemView.findViewById(R.id.img_text);
        }
    }
    static class FootViewHolder extends RecyclerView.ViewHolder{

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
    public void upDate(TreeMap map){

        ArrayList<ShortNewsData> list = (ArrayList<ShortNewsData>) map.get(targetDate);
        this.list.add(new ShortNewsData(list.get(0).getDate()));
        for (int num = 0 ; num < list.size() ; num++) {
            this.list.add(list.get(num));
        }
        final ArrayList<ShortNewsData> mList = this.list;


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // 刷新操作

                notifyItemRangeChanged(mList.size() - list.size() -1 , list.size());
            }
        });


    }
    public void refreshDate(TreeMap map){
        int size = ((ArrayList<ShortNewsData>) this.map.get("latestDate")).size()+1;
        for(int num = 0 ; num <size ; num++){
            list.remove(0);
        }
        for(int num = 0 ; num< ((ArrayList<ShortNewsData>) map.get("latestDate")).size() ; num++){
            list.add(0,((ArrayList<ShortNewsData>)map.get("latestDate")).get(num));
        }

        list.add(0,new ShortNewsData(((ArrayList<ShortNewsData>) map.get("latestDate")).get(0).getDate()));


        notifyItemRangeRemoved(0,size+4);

        notifyItemRangeInserted(0,((ArrayList<ShortNewsData>) map.get("latestDate")).size()+4);

        activityContext.closeSwipeRefreshLayout();

        this.map = map;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context mContext = viewGroup.getContext();
        switch (i){
            case DATE_VIEW:

                View view = LayoutInflater.from(mContext).inflate(R.layout.mydate,viewGroup,false);
                    DateViewHolder dataViewHolder = new DateViewHolder(view);
                return dataViewHolder;
            case NORMAL_VIEW:
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.mycardview,viewGroup,false);
                NormalViewHolder normalViewHolder = new NormalViewHolder(view1);
                return normalViewHolder;
            default :
                View view2 = LayoutInflater.from(mContext).inflate(R.layout.footer_layout,viewGroup,false);
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view2) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };

                return viewHolder;

        }

        
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof DateViewHolder){
            ((DateViewHolder) viewHolder).textView.setText(list.get(i).getDate());
        }
        else {
            if (viewHolder instanceof NormalViewHolder){
                //not load image

                ((NormalViewHolder) viewHolder).titleTextView.setText(list.get(i).getTitle());
                String url = list.get(i).getImages().replaceAll("\\\\","").replace("[","").replace("]","").replace("\"","");
                Log.d(TAG, "onBindViewHolder: "+url);
                Glide.with(viewHolder.itemView.getContext()).load(url).override(300,300).placeholder(R.mipmap.picture_load_fail).into(((NormalViewHolder) viewHolder).imageView);

            }else {
                //load more
                targetDate = list.get(list.size()-1).getDate();
                Log.d(TAG, "onBindViewHolder: "+targetDate);
                activityContext.loadMore(targetDate);
            }
        }
        if(mClicklistener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mClicklistener.onItemClick(viewHolder.itemView,position,list.get(i).getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1 ;
    }

    @Override
    public int getItemViewType(int position) {

        if(position >=0 & position < getItemCount() - 1 ){
           if(list.get(position ).getId() == null){
               return DATE_VIEW;
           }
           else{
               return NORMAL_VIEW;
           }
       }
       if(position == getItemCount()){
           return FOOT_VIEW;
       }
        return 5;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position,String args);
    }
    public void setOnItemClickListener(OnItemClickListener mClickListener){
        this.mClicklistener = mClickListener;
    }

}
