package com.example.us;

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
    private ArrayList<Item_board> mData;

    public Adapter(ArrayList<Item_board> mData) {
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

        public void onBind(Item_board item_board){
            System.out.println(TAG+"onBind");
            TextView_board_itemView_title.setText(item_board.getTitle());
            TextView_board_itemView_writer.setText(item_board.getWriter());
            TextView_board_itemView_time.setText(item_board.getTime());
//            TextView_board_itemView_views.setText(item_board.getViews());

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
                int index = mData.get(position).getIndex();
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
        System.out.println(getClass().getName()+"-getItemCount- : "+mData.size());
        return mData.size();
    }

    public void addItem(Item_board item_board){
        System.out.println(getClass().getName()+"-addItem- ");
        mData.add(item_board);
        notifyDataSetChanged();
    }
}



