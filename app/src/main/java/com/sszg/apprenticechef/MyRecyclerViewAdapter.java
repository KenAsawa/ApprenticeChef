package com.sszg.apprenticechef;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private static ArrayList<RecipeListItem> mData;
    private Context context;
    private ItemClickListener mClickListener;

    MyRecyclerViewAdapter() {
        mData = new ArrayList<>();
    }

    public void addRecipe(RecipeListItem recipeListItem) {
        mData.add(recipeListItem);
    }

    public RecipeListItem getRecipe(int index) {
        return mData.get(index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RecipeListItem item = mData.get(i);
        viewHolder.foodName.setText(item.getRecipeName());
        viewHolder.foodDate.setText(item.getDate());
        viewHolder.foodPictures.setImageDrawable(getImage(context, item.getImageName()));
    }

    public static Drawable getImage(Context c, String ImageName) {
        return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView foodPictures;
        private TextView foodName;
        private TextView foodDate;

        public ViewHolder(View itemView) {
            super(itemView);
            foodPictures = itemView.findViewById(R.id.food_pictures);
            foodName = itemView.findViewById(R.id.food_name);
            foodDate = itemView.findViewById(R.id.food_date);
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
