package com.example.yunan.tripscanner;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yunan on 2017-05-28.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    Context context;
    Trip trip;

    public RecyclerAdapter(Context context, Object obj) {
        this.context = context;
        if(obj instanceof Trip){
            trip = (Trip)obj;
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    /** 정보 및 이벤트 처리는 이 메소드에서 구현 **/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        HashMap<String,Object> temp;

        temp = (HashMap<String,Object>)trip.getTrips().get(position);
        HashMap <String, Object> ownerTemp = (HashMap<String, Object>) temp.get("owner");
        holder.addressView.setText(temp.get("address").toString());
        holder.ownerNameView.setText(ownerTemp.get("name").toString());
        holder.checkInView.setText(temp.get("check_in").toString());
        holder.checkOutView.setText(temp.get("check_out").toString());

        /*HashMap<String,String> noticeItem = noticeList.get(position);
        holder.tv_writer.setText(noticeItem.get("writer")); //작성자
        Log.e("[writer]", noticeItem.get("writer"));
        holder.tv_title.setText(noticeItem.get("title")); //제목
        holder.tv_content.setText(noticeItem.get("content")); //내용 일부
        holder.tv_date.setText(noticeItem.get("regist_day")); //작성일*/
    }

    @Override
    public int getItemCount() {
        return this.trip.getTrips().size();
    }
    /** item layout 불러오기 **/
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView addressView;
        //ImageView imageView;
        TextView ownerNameView;
        TextView checkInView;
        TextView checkOutView;

        public ViewHolder(View v) {
            super(v);
            /*tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_date = (TextView) v.findViewById(R.id.tv_date);
            tv_content = (TextView) v.findViewById(R.id.tv_content);
            tv_writer = (TextView) v.findViewById(R.id.tv_writer);*/
            cardView = (CardView) v.findViewById(R.id.card_view);
            addressView = (TextView) cardView.findViewById(R.id.title_address);
//            imageView = (ImageView) imageView.findViewById(R.id.image);
            ownerNameView = (TextView) cardView.findViewById(R.id.owner_name);
            checkInView = (TextView) cardView.findViewById(R.id.check_in);
            checkOutView = (TextView) cardView.findViewById(R.id.check_out);
        }
    }

}
