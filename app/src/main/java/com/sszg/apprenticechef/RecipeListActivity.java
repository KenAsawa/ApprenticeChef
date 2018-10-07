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
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
                Crop.pickImage(RecipeListActivity.this);

            }
        });
        setupGrid();
        populateGrid();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            try {
                handleCrop(resultCode, result);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).start(this);
    }

    private void handleCrop(int resultCode, Intent result) throws FileNotFoundException {
        if (resultCode == RESULT_OK) {
            final Uri croppedImage = Crop.getOutput(result);
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
                                Toast.makeText(getApplicationContext(),
                                        text.getText(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    public void setupGrid() {
        recyclerView = findViewById(R.id.recyclerView);
        int numOfColumns = 2;
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


}
