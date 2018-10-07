package com.sszg.apprenticechef;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class RecipeListActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(RecipeListActivity.this);


            }
        });
        setupGrid();
        populateGrid();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Toast.makeText(getApplicationContext(), "Parsing Recipe...", Toast.LENGTH_LONG).show();
                OCR(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void OCR(final Uri croppedImage) {
        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyA93uFdl40MvQqVp0wzyPegT05EEI1t4FU"));
        final Vision vision = visionBuilder.build();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(croppedImage);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    Image inputImage = new Image();
                    inputImage.setContent(encodedImage);
                    Feature desiredFeature = new Feature();
                    desiredFeature.setType("DOCUMENT_TEXT_DETECTION");
                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputImage);
                    request.setFeatures(Arrays.asList(desiredFeature));
                    BatchAnnotateImagesRequest batchRequest =
                            new BatchAnnotateImagesRequest();

                    batchRequest.setRequests(Arrays.asList(request));
                    BatchAnnotateImagesResponse batchResponse =
                            vision.images().annotate(batchRequest).execute();
                    final TextAnnotation text = batchResponse.getResponses()
                            .get(0).getFullTextAnnotation();
                    // Display toast on UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(),
                            //        text.getText(), Toast.LENGTH_LONG).show();
                            System.out.println(text.getText());
                            Intent intent = new Intent(RecipeListActivity.this, RecipeParsedActivity.class);
                            intent.putExtra("INGREDIENTS", text.getText());
                            startActivity(intent);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
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
        RecipeListItem recipe1 = new RecipeListItem("pizza", "Pepperoni Pizza", "12/08/18", ("2 1/4 tsp active dry yeast\n" +
                "    2 tsp granulated sugar\n" +
                "    1 1/2 cups warm water , 110 degrees, divided\n" +
                "    3 Tbsp olive oil, plus more for brushing\n" +
                "    1 1/2 tsp salt\n" +
                "    1 tsp white vinegar\n" +
                "    3 1/2 - 4 cups bread flour\n" +
                "    Pizza Sauce (yields enough for 3 large pizzas)\n" +
                "    2 (8 oz) cans tomato sauce\n" +
                "    1/4 cup tomato paste\n" +
                "    1 1/2 Tbsp extra virgin olive oil\n" +
                "    1 1/2 tsp honey\n" +
                "    2 tsp chopped fresh oregano\n" +
                "    2 tsp chopped fresh basil\n" +
                "    1/4 tsp dried thyme (or 3/4 tsp fresh)\n" +
                "    1 clove garlic, finely minced\n" +
                "    Salt and freshly ground black pepper , to taste\n" +
                "    Topping (yields enough for 1 13-inch pizza)\n" +
                "    1 Tbsp olive oil\n" +
                "    1 clove garlic , finely minced\n" +
                "    2 cups shredded mozzarella cheese (8 oz)\n" +
                "    1/4 cup finely shredded parmesan cheese (1 oz)\n" +
                "    20 slices pepperoni (I recommend turkey, it's just as good as the regular)\n" +
                "    2 Tbsp fresh oregano (chopped if using larger leaves)\n" +
                "    Red pepper flakes , for serving (optional)"));
        RecipeListItem recipe2 = new RecipeListItem("pasta", "Alfredo Pasta", "12/03/18", ("2 cups milk any fat %\n" +
                "    11/2 cups chicken broth\n" +
                "    3 tbsp butter\n" +
                "    1 large garlic clove minced\n" +
                "    8 ounces fettuccine\n" +
                "    1/2 cup heavy cream\n" +
                "    3/4 cup freshly grated parmesan plus more for garnish\n" +
                "    Salt & pepper to taste\n" +
                "    Parsley for garnish"));
        RecipeListItem recipe3 = new RecipeListItem("pancakes", "Pancakes & Eggs", "11/09/17", ("2 cups all-purpose flour\n" +
                "3 tablespoons sugar\n" +
                "1/2 teaspoons baking powder\n" +
                "1 1/2 teaspoons baking soda\n" +
                "1/4 teaspoons kosher salt\n" +
                "2 1/2 cups buttermilk\n" +
                "2 large eggs\n" +
                "3 tablespoons unsalted butter melted\n"));
        adapter.addRecipe(recipe1);
        adapter.addRecipe(recipe2);
        adapter.addRecipe(recipe3);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        RecipeListItem recipeListItem = adapter.getRecipe(position);
        //Toast.makeText(this, "Title: " + recipeListItem.getRecipeName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RecipeListActivity.this, RecipeParsedActivity.class);
        System.out.println(recipeListItem.getIngredientsList());
        intent.putExtra("INGREDIENTS", recipeListItem.getIngredientsList());
        startActivity(intent);
    }


}
