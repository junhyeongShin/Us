package com.example.us;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.renderscript.Sampler;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ItemViewHolder> {

    private static final String TAG = "Adapter";
    private ArrayList<Board_list> mData;

    public Adapter(ArrayList<Board_list> mData) {
        this.mData = mData;
        System.out.println(TAG+"mData : "+mData);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView TextView_board_itemView_title;
        TextView TextView_board_itemView_writer;
        TextView TextView_board_itemView_time;
        TextView TextView_board_itemView_views;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            System.out.println(TAG+"ItemViewHolder");
            TextView_board_itemView_title = itemView.findViewById(R.id.TextView_board_itemView_title);
            TextView_board_itemView_writer = itemView.findViewById(R.id.TextView_board_itemView_writer);
            TextView_board_itemView_time = itemView.findViewById(R.id.TextView_board_itemView_time);
            TextView_board_itemView_views = itemView.findViewById(R.id.TextView_board_itemView_views);
        }

        public void onBind(Board_list item_board){
            System.out.println(TAG+"onBind");

            // 현재시간을 msec 으로 구한다.
                long now = System.currentTimeMillis();

                // 현재시간을 date 변수에 저장한다.
                Date date = new Date(String.valueOf(item_board.getCreate_time()));

                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd ");
                SimpleDateFormat sdfNow_year = new SimpleDateFormat("yyyy");
                SimpleDateFormat sdfNow_month = new SimpleDateFormat("MM");
                SimpleDateFormat sdfNow_Time = new SimpleDateFormat("HH:00");
                SimpleDateFormat sdfNow_day = new SimpleDateFormat("dd");
                SimpleDateFormat sdfNow_hour = new SimpleDateFormat("HH");

                // nowDate 변수에 값을 저장한다.
                String formatDate = sdfNow.format(date);
                String formatYear = sdfNow_year.format(date);
                String formatMonth = sdfNow_month.format(date);
                String formatDay = sdfNow_day.format(date);
                String formatTime = sdfNow_Time.format(date);
                String formatHour = sdfNow_hour.format(date);

            TextView_board_itemView_title.setText(item_board.getTitle());
            TextView_board_itemView_writer.setText(item_board.getEmail());
            TextView_board_itemView_time.setText(formatYear+" / "+formatMonth+" / "+formatDay);
            TextView_board_itemView_views.setText("조회수 : "+String.valueOf(item_board.getViews()));

        }
    }

    @Override
    public Adapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println(TAG+"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_board_item, parent, false);
        return new Adapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ItemViewHolder holder, final int position) {
        System.out.println(TAG+"onBindViewHolder");
        holder.onBind(mData.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = mData.get(position).getId();
                System.out.println(getClass().getSimpleName()+"_onClick_position : "+position);
                System.out.println(getClass().getSimpleName()+"_onClick_index : "+index);

                notifyDataSetChanged();


            }
        });
    }

    @Override
    public long getItemId(int position) {
        System.out.println(getClass().getName()+"-getItemId- : "+position);
        return position;
    }

    @Override
    public int getItemCount() {
//        System.out.println(getClass().getName()+"-getItemCount- : "+mData.size());
        return mData.size();
    }

    public void addItem(Board_list item_board){
        System.out.println(getClass().getName()+"-addItem- ");
        mData.add(item_board);
        notifyDataSetChanged();
    }
}



