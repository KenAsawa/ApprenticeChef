package com.sszg.apprenticechef;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class RecipeParsedActivity extends AppCompatActivity implements ListRecyclerView.ItemCL {

    private RecyclerView recyclerView;
    private ListRecyclerView adapter;
    private Button portion;
    private EditText divisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_parsed);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String listToParse = bundle.getString("INGREDIENTS");
            String[] items = new String[0];
            if (listToParse != null) {
                items = listToParse.split("\n");
            }
            final ArrayList<String> itemList = new ArrayList<>();
            for (String s : items) {
                String[] parts = s.split(" ");
                if (parts.length > 3) {
                    try {
                        if (parts[0].contains("/")) {
                            itemList.add(s);
                        } else {
                            Integer.parseInt(parts[0]);
                            itemList.add(s);
                        }
                    } catch (Exception ex) {

                    }
                }
            }
            portion = findViewById(R.id.portion);
            divisor = findViewById(R.id.divisor);
            setupGrid(itemList);
            portion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int d = Integer.parseInt(divisor.getText().toString().trim());
                        if (d > 0) {
                            //System.out.println("LIST BEFORE IS : " + itemList);
                            ArrayList<String> newList = RecipeMath.GetBestMeasure(itemList, d, false);
                            //System.out.println("LIST AFTER IS : " + newList);
                            setupList(newList);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Cut Portions by a number Greater then 0", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {

                    }

                }
            });
        }

    }

    public void setupList(ArrayList<String> items) {
        adapter.clearList();
        for (String s : items) {
            adapter.addIngredient(s);
        }
        adapter.notifyDataSetChanged();
    }


    public void setupGrid(ArrayList<String> items) {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListRecyclerView();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        setupList(items);
    }

    @Override
    public void onItemClick(View view, int position) {
        String item = adapter.getItem(position);
    }
}
