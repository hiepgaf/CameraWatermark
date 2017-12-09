package jaypaw.pm.easywatermark;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import jaypaw.pm.easywatermark.utility.Utility;

public class MainActivity extends AppCompatActivity {

    File imageFile;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 998;
    int TAKE_PHOTO_CODE = 0;

    /*RadioGroup positionpicker1;
    RadioGroup positionpicker2;
    RadioGroup positionpicker3;*/

    RadioGroup positionpicker;

    RadioGroup colorGroup;

    ScrollView scrollView;

    EditText messageBox;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shareeasybmicalculator:
                //shareMobileMapperWithOthers();
                Utility.shareAppWithOthers(MainActivity.this);
                return true;
            case R.id.about:
                openAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAbout() {
        Log.d(this.getClass().getSimpleName(), "Opening About Activity");
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    /*private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                positionpicker2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                positionpicker2.clearCheck(); // clear the second RadioGroup!
                positionpicker2.setOnCheckedChangeListener(listener2); //reset the listener

                positionpicker3.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                positionpicker3.clearCheck(); // clear the second RadioGroup!
                positionpicker3.setOnCheckedChangeListener(listener3); //reset the listener

                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                positionpicker1.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                positionpicker1.clearCheck(); // clear the second RadioGroup!
                positionpicker1.setOnCheckedChangeListener(listener1); //reset the listener

                positionpicker3.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                positionpicker3.clearCheck(); // clear the second RadioGroup!
                positionpicker3.setOnCheckedChangeListener(listener3); //reset the listener

                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }
    };


    private RadioGroup.OnCheckedChangeListener listener3 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                positionpicker2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                positionpicker2.clearCheck(); // clear the second RadioGroup!
                positionpicker2.setOnCheckedChangeListener(listener2); //reset the listener

                positionpicker1.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                positionpicker1.clearCheck(); // clear the second RadioGroup!
                positionpicker1.setOnCheckedChangeListener(listener1); //reset the listener

                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }
    };*/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = (ScrollView) findViewById(R.id.watermarkscrollview);

        checkRequiredPermissions();

        //intializePositionRadioGroups();

        colorGroup = (RadioGroup) findViewById(R.id.colorpicker);
        colorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(RadioGroup group, int checkedId) {
                  if (checkedId != -1) {
                      hideSoftKeyboard();
                  }
              }
          });

        messageBox = (EditText) findViewById(R.id.watermarkmessage);

        messageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox.setBackgroundColor(Color.WHITE);
            }
        });

        ImageView capture = (ImageView) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String msgstr = messageBox.getText().toString();
                Log.d(this.getClass().getSimpleName(),"Message: " + msgstr);
                if (msgstr == null || msgstr.length() == 0) {
                    //messageBox.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                    messageBox.setBackgroundColor(Color.RED);
                } else {
                    triggerCamera();
                }
            }
        });
    }

    /*private void intializePositionRadioGroups() {
        positionpicker1 = (RadioGroup) findViewById(R.id.positionpicker1);
        positionpicker2 = (RadioGroup) findViewById(R.id.positionpicker2);
        positionpicker3 = (RadioGroup) findViewById(R.id.positionpicker3);

        positionpicker1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        positionpicker2.clearCheck();
        positionpicker3.clearCheck();

        positionpicker1.setOnCheckedChangeListener(listener1);
        positionpicker2.setOnCheckedChangeListener(listener2);
        positionpicker3.setOnCheckedChangeListener(listener3);
    }*/

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

        //EditText messageBox = (EditText) findViewById(R.id.watermarkmessage);
        String message = messageBox.getText().toString();

        String color = getSelectedColor();
        String opacity = getSelectedOpacity();
        String position = getSelectedPosition();

        Log.d(this.getClass().getSimpleName(), "Message: " + message + " color: " + color + "  position: " + position + "  opacity: " + opacity);
        Log.d(this.getClass().getSimpleName(), "Watermarking Image: " + imageFile.getAbsolutePath());

        //RadioButton positionRadioButton = getSelectedPositionRadioButton();

        if (color != null && opacity != null){

            Bitmap bitmap1 = getWorkingBitmap();
            float resolutionDensity = getResources().getDisplayMetrics().density;
            //Bitmap bitmap = Utility.waterMark(bitmap1, message, getColorCode(color), getOPacity(opacity),getPostionRadioButtonId(positionRadioButton), resolutionDensity);
            Bitmap bitmap = Utility.waterMark(bitmap1, message, getColorCode(color), getOPacity(opacity),position, resolutionDensity);

            try {
                Utility.saveBitmapAsImageFile(imageFile, bitmap);
                scanFile(imageFile.getAbsolutePath());
                openScreenshot(imageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(this.getClass().getSimpleName(), "Invalid Input!!");
        }
    }

    /*private RadioButton getSelectedPositionRadioButton() {
        RadioGroup positionpicker1 = (RadioGroup) findViewById(R.id.positionpicker1);
        RadioGroup positionpicker2 = (RadioGroup) findViewById(R.id.positionpicker2);
        RadioGroup positionpicker3 = (RadioGroup) findViewById(R.id.positionpicker3);

        RadioButton selectedRadioButton = null;
        if (getSelectedPositionRadioButton(positionpicker1) != null) {
            selectedRadioButton = getSelectedPositionRadioButton(positionpicker1);
        } else if (getSelectedPositionRadioButton(positionpicker2) != null) {
            selectedRadioButton = getSelectedPositionRadioButton(positionpicker2);
        } else if(getSelectedPositionRadioButton(positionpicker3) != null) {
            selectedRadioButton = getSelectedPositionRadioButton(positionpicker3);
        }
        return selectedRadioButton;
    }*/



    /*private String getPostionRadioButtonId(View view) {

        if (view == null) {
            return "MM";
        }

        switch(view.getId()) {
            case R.id.LT: {
                return "LT";
            }
            case R.id.MT: {
                return "MT";
            }
            case R.id.RT: {
                return "RT";
            }
            case R.id.LM: {
                return "LM";
            }

            case R.id.MM: {
                return "MM";
            }
            case R.id.RM: {
                return "RM";
            }
            case R.id.LB: {
                return "LB";
            }
            case R.id.MB: {
                return "MB";
            }
            case R.id.RB: {
                return "RB";
            }
            default: {
                return "MM";
            }
        }
    }*/

    private RadioButton getSelectedPositionRadioButton(RadioGroup radioGroup ) {
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonId);
        return selectedRadioButton;
    }

    private String getSelectedOpacity() {
        RadioGroup opacityGroup = (RadioGroup) findViewById(R.id.opacitypicker);
        int selectedOpacity = opacityGroup.getCheckedRadioButtonId();
        RadioButton opacityRadio = (RadioButton) findViewById(selectedOpacity);

        String opacityStr = null;

        if (opacityRadio == null) {
            opacityStr = "90%";
        } else {
            opacityStr = opacityRadio.getText().toString();
        }

        return opacityStr;
    }

    private String getSelectedPosition() {
        positionpicker = (RadioGroup) findViewById(R.id.positionpicker);
        int selectedPosition = positionpicker.getCheckedRadioButtonId();
        RadioButton positionRadio = (RadioButton) findViewById(selectedPosition);
        String positionStr = positionRadio.getText().toString();

        if (positionStr == null) {
            positionStr = "Middle";
        } else {
            positionStr = positionRadio.getText().toString();
        }
        return positionStr;
    }

    private String getSelectedColor() {
        //RadioGroup colorGroup = (RadioGroup) findViewById(R.id.colorpicker);
        int selectedColor = colorGroup.getCheckedRadioButtonId();
        RadioButton colorRadio = (RadioButton) findViewById(selectedColor);
        String colorStr = null;

        if (colorRadio == null) {
            colorStr = "Red";
        } else {
            colorStr = colorRadio.getText().toString();
        }

        return colorStr;
    }

    /*private String getSelectedFont() {
        RadioGroup fontGroup = (RadioGroup) findViewById(R.id.fontpicker);
        int selectedFont = fontGroup.getCheckedRadioButtonId();
        RadioButton fontRadio = (RadioButton) findViewById(selectedFont);
        String font = (String) fontRadio.getText();
    }*/

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

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void scanFile(String path) {

        MediaScannerConnection.scanFile(MainActivity.this,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
}
