package jaypaw.pm.easywatermark;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by pmodi4 on 12/10/2017.
 */

public class DetailsActivity extends ActionBarActivity {

    private Bitmap bitmap;

    private String imageFilePath;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shareimage:
                shareImageWithOthers();
                return true;
            case R.id.opennativegallery:
                openImageInNativeGallery();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openImageInNativeGallery() {
        File imageFile = new File(imageFilePath);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        String title = getIntent().getStringExtra("title");
        //Bitmap bitmap = getIntent().getParcelableExtra("image");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

        bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("filename"), options);

        imageFilePath = getIntent().getStringExtra("filename");

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
    }

    private void shareImageWithOthers() {
        Uri uri = Uri.parse(imageFilePath);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, "Sharing my new watermarked image..");
        startActivity(Intent.createChooser(share, "Share Watermark Image"));
    }
}