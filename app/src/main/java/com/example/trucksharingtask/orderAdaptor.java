package com.example.trucksharingtask;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class orderAdaptor extends RecyclerView.Adapter<orderAdaptor.OrderViewHolder>{
    private Context context;
    private List<orderModel> orderList;

    public orderAdaptor(Context context, List<orderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public orderAdaptor.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_recycler, parent, false);
        return new OrderViewHolder(itemView,orderList,context);
    }

    @Override
    public void onBindViewHolder(@NonNull orderAdaptor.OrderViewHolder holder, int position) {
        holder.order_title.setText(orderList.get(position).getGoodType());
        String descriptionText = orderList.get(position).getSender() + " sending " + orderList.get(position).getGoodType();
        holder.descriptionTextView.setText(descriptionText);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView orderImageView;
        TextView order_title, descriptionTextView;
        ImageView shareButton;
        List<orderModel> orderList;
        private Context mContext;
        public OrderViewHolder(@NonNull View itemView, List<orderModel> orderList,Context context) {
            super(itemView);
            orderImageView = itemView.findViewById(R.id.order_image);
            order_title = itemView.findViewById(R.id.order_title);
            descriptionTextView = itemView.findViewById(R.id.order_description);
            shareButton = itemView.findViewById(R.id.share_btn);
            itemView.setOnClickListener(this);
            this.orderList = orderList;
            this.mContext = context;
            shareButton.setOnClickListener(view -> {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, order_title.getText().toString() + "\n" + descriptionTextView.getText().toString());
                shareIntent.setType("text/plain");
                Intent shareOrder = Intent.createChooser(shareIntent, null);
                context.startActivity(shareOrder);
            });
        }


        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Intent intent = new Intent(mContext, OrderDetails.class);
            intent.putExtra("sender", orderList.get(position).getSender());
            intent.putExtra("receiver", orderList.get(position).getReceiver());
            intent.putExtra("date", orderList.get(position).getDate());
            intent.putExtra("time", orderList.get(position).getTime());
            intent.putExtra("location",orderList.get(position).getLocation());
            intent.putExtra("goodType", orderList.get(position).getGoodType());
            intent.putExtra("weight", orderList.get(position).getWeight());
            intent.putExtra("width", orderList.get(position).getWidth());
            intent.putExtra("height", orderList.get(position).getHeight());
            intent.putExtra("length", orderList.get(position).getLength());
            mContext.startActivity(intent);
        }
    }
}
