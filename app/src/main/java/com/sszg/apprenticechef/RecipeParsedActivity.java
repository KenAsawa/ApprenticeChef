package com.sszg.apprenticechef;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class RecipeParsedActivity extends AppCompatActivity {

    private ListView listView;

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
            ArrayList<String> itemList = new ArrayList<>();
            for (String s : items) {
                String[] parts = s.split(" ");
                if (parts.length > 3) {
                    try {
                        Integer.parseInt(parts[0]);
                        itemList.add(s);
                    } catch (Exception ex) {

                    }
                }
            }
            System.out.println("LIST BEFORE IS : " + itemList);
            itemList = RecipeMath.GetBestMeasure(itemList, 1, false);
            System.out.println("LIST AFTER IS : " + itemList);
            listView = findViewById(R.id.listView);
        }

    }
}
