package jaypaw.pm.easywatermark.utility;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.bitmap;

/**
 * Created by has7 on 12/6/2017.
 */

public class Utility {

    public static File createImageFile() {

        File newfile = new File(getAppDirectory(), getImageFileName());
        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
            Log.e(Utility.class.getSimpleName(), e.getMessage());
        }

        return newfile;
    }

    private static String getImageFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append("watermark-").append(getTimeStamp()).append(".jpg");
        Log.d(Utility.class.getSimpleName(), "fileName: " + sb.toString());
        return sb.toString();
    }

    private static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyhhmmss");
        String timestamp = format.format(new Date());
        return timestamp;
    }

    private static File getAppDirectory() {
        String root = Environment.getExternalStorageDirectory().toString();
        final File directory = new File(root + "/watermark/");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory;
    }

    public static void saveBitmapAsImageFile(File imageFile, Bitmap bitmap) throws IOException {
        Log.d(Utility.class.getSimpleName(), "Saving watermarked file: " + imageFile.getAbsolutePath());
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        outputStream.flush();
        outputStream.close();
        Log.d(Utility.class.getSimpleName(), "Saved watermarked file: " + imageFile.getAbsolutePath());
    }

    public static Bitmap waterMark(Bitmap bitmap, String watermark, int color, int opacity) {
        //get source image width and height
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int msglength = watermark.length();
        int posX = (w/2) - msglength;
        Log.d(Utility.class.getSimpleName(), w + "  " + msglength + "  " + posX);

        //Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        //create canvas object
        Canvas canvas = new Canvas(bitmap);
        //draw bitmap on canvas
        canvas.drawBitmap(bitmap, 0, 0, null);
        //create paint object
        Paint paint = new Paint();
        //apply color
        paint.setColor(color);
        //set transparency
        //paint.setAlpha(250);
        paint.setAlpha(opacity);
        //set text size
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        //set should be underlined or not
        paint.setUnderlineText(false);
        //draw text on given location
        canvas.drawText(watermark, posX, 200, paint);

        return bitmap;
    }
}
