package jaypaw.pm.easywatermark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jaypaw.pm.easywatermark.adapter.GridViewAdapter;
import jaypaw.pm.easywatermark.domain.ImageItem;
import jaypaw.pm.easywatermark.utility.Utility;

public class GallaryActivity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);

        Log.d(this.getClass().getSimpleName(), "Loading thumbnails");

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getImageItemList());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                Log.d(this.getClass().getSimpleName(), "Item: " + item.getTitle() + "  " + item.getImage());
                //Create intent
                Intent intent = new Intent(GallaryActivity.this, DetailsActivity.class);
                intent.putExtra("title", item.getTitle());
                //intent.putExtra("image", item.getImage());
                intent.putExtra("filename", item.getFullFileName());

                //Start details activity
                startActivity(intent);
            }
        });

        /*Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile("abcd"),
                40, 40);*/
        Log.d(this.getClass().getSimpleName(), "Loaded all thumbnails");
    }

    private List<ImageItem> getImageItemList() {
        File directory = Utility.getAppDirectory();
        final List<ImageItem> imageItems = new ArrayList<>();

        if (directory != null) {
            String[] imageList = directory.list();
            Utility.sortImageFile(imageList);
            imageList = Utility.reverseArray(imageList);

            StringBuilder sb = null;
            for (String imageFileName : imageList) {
                sb = new StringBuilder();
                sb.append(directory).append("/").append(imageFileName);
                Log.d(this.getClass().getSimpleName(), "Full Image Path: " + sb.toString());
                //Bitmap bitmap = BitmapFactory.decodeFile(sb.toString());
                //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
                imageItems.add(new ImageItem(imageFileName, sb.toString()));
            }
        }

        return imageItems;
    }


}
