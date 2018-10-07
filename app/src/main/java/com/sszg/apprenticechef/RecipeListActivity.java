package com.sszg.apprenticechef;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

public class RecipeListActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab = findViewById(R.id.fab);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        setupGrid();
        populateGrid();
    }

    public void setupGrid() {
        recyclerView = findViewById(R.id.recyclerView);
        int numOfColumns = 1;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numOfColumns));
        adapter = new MyRecyclerViewAdapter();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void populateGrid() {
        RecipeListItem recipe1 = new RecipeListItem("pizza", "Pepperoni Pizza", "12/08/18");
        RecipeListItem recipe2 = new RecipeListItem("pizza", "Cheese Pizza", "12/03/18");
        RecipeListItem recipe3 = new RecipeListItem("pizza", "Sasauge Pizza", "11/09/17");
        adapter.addRecipe(recipe1);
        adapter.addRecipe(recipe2);
        adapter.addRecipe(recipe3);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        RecipeListItem recipeListItem = adapter.getRecipe(position);
        Toast.makeText(this, "Title: " + recipeListItem.getRecipeName(), Toast.LENGTH_SHORT).show();
        //Intent bookReader = new Intent(this, BookReaderActivity.class);
        //startActivity(bookReader);
    }


    final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(floatingActionButton.getContext(),  R.anim.shake);
            anim.setDuration(200L);
            floatingActionButton.startAnimation(anim);
        }
    });
}
