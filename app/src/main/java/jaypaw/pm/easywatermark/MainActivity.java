package jaypaw.pm.easywatermark;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jaypaw.pm.easywatermark.utility.Utility;

public class MainActivity extends AppCompatActivity {

    File imageFile;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 998;
    int TAKE_PHOTO_CODE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkRequiredPermissions();

        ImageView capture = (ImageView) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                triggerCamera();
            }
        });
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void triggerCamera() {
        Log.d(this.getClass().getSimpleName(), "========TRIGGERED CAMERA===========");
        imageFile = Utility.createImageFile();
        Log.d(this.getClass().getSimpleName(), "Image File Path: " + imageFile.getAbsolutePath());

        Uri outputFileUri = Uri.fromFile(imageFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d(this.getClass().getSimpleName(), "Pic saved");

            if (imageFile != null) {
                watermarkImage();
            }
        }
    }

    private Bitmap getWorkingBitmap() {
        /*View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        Bitmap drawableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        //selected_photo.setImageBitmap(bitmap);

        return drawableBitmap;
    }

    private void watermarkImage() {

        EditText messageBox = (EditText) findViewById(R.id.watermarkmessage);
        String message = messageBox.getText().toString();

        RadioGroup colorGroup = (RadioGroup) findViewById(R.id.colorpicker);
        int selectedColor = colorGroup.getCheckedRadioButtonId();
        RadioButton colorRadio = (RadioButton) findViewById(selectedColor);
        String color = (String) colorRadio.getText();

        /*RadioGroup fontGroup = (RadioGroup) findViewById(R.id.fontpicker);
        int selectedFont = fontGroup.getCheckedRadioButtonId();
        RadioButton fontRadio = (RadioButton) findViewById(selectedFont);
        String font = (String) fontRadio.getText();*/

        RadioGroup opacityGroup = (RadioGroup) findViewById(R.id.opacitypicker);
        int selectedOpacity = opacityGroup.getCheckedRadioButtonId();
        RadioButton opacityRadio = (RadioButton) findViewById(selectedOpacity);
        String opacity = (String) opacityRadio.getText();

        Log.d(this.getClass().getSimpleName(), "Message: " + message + " color: " + color);

        Log.d(this.getClass().getSimpleName(), "Watermarking Image: " + imageFile.getAbsolutePath());
        Bitmap bitmap1 = getWorkingBitmap();
        Bitmap bitmap = Utility.waterMark(bitmap1, message, getColorCode(color), getOPacity(opacity));

        try {
            Utility.saveBitmapAsImageFile(imageFile, bitmap);
            openScreenshot(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getOPacity(String opacityStr) {

        switch(opacityStr) {
            case "25%": {
                return 63;
            }
            case "50%": {
                return 127;

            }
            case "75%": {
                return 190;

            }
            case "90%": {
                return 230;
            }
            default: {
                return 127;
            }
        }
    }

    private int getColorCode(String colorStr) {

        switch(colorStr) {
            case "Blue": {
                return getResources().getColor(R.color.colorBlue);

            }
            case "Red": {
                return getResources().getColor(R.color.colorRed);

            }
            case "Green": {
                return getResources().getColor(R.color.colorGreen);

            }
            case "Black": {
                return getResources().getColor(R.color.colorBlack);

            }
            default: {
                return getResources().getColor(R.color.colorAccent);
            }
        }
    }

    private void checkRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(this.getClass().getSimpleName(), requestCode + "  " + grantResults[0] + "  " + permissions);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //populateFriendList();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Request to Write is Necessary");
                    alertBuilder.setMessage("Write to storage permission is necessary for to save photos.");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkRequiredPermissions();
                        }
                    });

                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    Toast.makeText(this, "Until you grant the permission, we canot save captured photos.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot save captured photos.", Toast.LENGTH_LONG).show();
            }
        }

    }
}
