package com.sszg.apprenticechef;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class ListRecyclerView extends RecyclerView.Adapter<ListRecyclerView.ViewHolder> {

    private ItemCL mClickListener;
    private static ArrayList<String> mData;
    private Context context;

    public ListRecyclerView() {
        mData = new ArrayList<>();
    }

    public void addIngredient(String ingredient) {
        mData.add(ingredient);
    }

    public String getItem(int position) {
        return mData.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String item = mData.get(i);
        viewHolder.editText.setText(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemCL {
        void onItemClick(View view, int position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemCL itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.editText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
