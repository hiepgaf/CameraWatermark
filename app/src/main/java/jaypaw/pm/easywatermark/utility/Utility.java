package jaypaw.pm.easywatermark.utility;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static Bitmap waterMark(Bitmap bitmap, String watermarkmsg, int color, int opacity, String positionId, float resolutionDensity) {
        //get source image width and height
        //int w = bitmap.getWidth();
        //int h = bitmap.getHeight();

        int pictureWidthPixel = bitmap.getWidth();

        //Integer basicfontSize = new Integer(40);
        int basicfontSize = (int) (40 * resolutionDensity);
        Log.d(Utility.class.getSimpleName(), "basicfontsize: " + basicfontSize + "   resolution: " + resolutionDensity);
        int fontSize = getFontSize(watermarkmsg, pictureWidthPixel, basicfontSize);
        float msgWidthPixel = getMessageWidthPixel(watermarkmsg, fontSize);

        int posX = getPositionX(positionId, bitmap, watermarkmsg, msgWidthPixel);
        int posY = getPositionY(positionId, bitmap, watermarkmsg);

        //Log.d(Utility.class.getSimpleName(), "ImgW: " + w + " ImgH: " + h + "  posX: "+ posX + "  posY: " + posY);

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
        //float fontSize = 20 * resolutionDensity;

        paint.setTextSize(fontSize);
        paint.setAntiAlias(true);
        //set should be underlined or not
        paint.setUnderlineText(false);
        //draw text on given location
        canvas.drawText(watermarkmsg, posX, posY, paint);

        return bitmap;
    }

    private static Integer getFontSize(String msg, int pictureWidth, Integer basicFontSize) {
        float msgWidthPixel = getMessageWidthPixel(msg, basicFontSize);
        int fontSize = basicFontSize;
        while(msgWidthPixel > pictureWidth) {
            if (fontSize > 9 ) {
                fontSize -= 1;
            }
            Log.d(Utility.class.getSimpleName(), fontSize + "  " + msgWidthPixel + "  " + pictureWidth);
            msgWidthPixel = getMessageWidthPixel(msg, fontSize);
        }

        Log.d(Utility.class.getSimpleName(), "Final fontsize: " + fontSize);
        return fontSize;
    }


    private static int getPositionX(String id, Bitmap bitmap, String msg, float msgWidthpixel) {

        int pictureWidth = bitmap.getWidth();
        int msgWidthInt = (int) msgWidthpixel;


        switch(id) {
            case "LT":
            case "LM":
            case "LB": {
                /*int onethirdOfPic = pictureWidth / 3;
                int positionPoint = onethirdOfPic / 2;
                int finalX = positionPoint - (msgWidthInt / 2);
                return finalX;*/
                int halfOfPic = pictureWidth / 2;
                int finalX = halfOfPic - (msgWidthInt / 2);
                return finalX;
            }
            case "MT":
            case "MM":
            case "MB":    {
                int halfOfPic = pictureWidth / 2;
                int finalX = halfOfPic - (msgWidthInt / 2);
                return finalX;
            }
            case "RT":
            case "RM":
            case "RB": {
                /*int halfOfPic = pictureWidth / 2;
                int positionPoint = halfOfPic + (halfOfPic / 2);
                int finalX = positionPoint - (msgWidthInt / 2);
                return finalX;*/
                int halfOfPic = pictureWidth / 2;
                int finalX = halfOfPic - (msgWidthInt / 2);
                return finalX;
            }

            default: {
                int halfOfPic = pictureWidth / 2;
                int finalX = halfOfPic - (msgWidthInt / 2);
                return finalX;
            }
        }
    }

    private static int getPositionY(String id, Bitmap bitmap, String msg) {

        int pictureHeight = bitmap.getHeight();
        //float msgWidth = getMessageWidthPixel(msg);
        //int msgWidthInt = (int) msgWidth;


        switch(id) {
            case "LT":
            case "MT":
            case "RT": {
                int onethirdOfPic = pictureHeight / 3;
                int positionPoint = onethirdOfPic / 2;
                //int finalY = positionPoint - (msgWidthInt / 2);
                return positionPoint;
            }
            case "LM":
            case "MM":
            case "RM":    {
                int halfOfPic = pictureHeight / 2;
                //int finalX = halfOfPic - (msgWidthInt / 2);
                return halfOfPic;
            }
            case "LB":
            case "MB":
            case "RB": {
                int onethirdOfPic = pictureHeight / 3;
                int positionPoint = (onethirdOfPic * 2) + (onethirdOfPic / 2);
                return positionPoint;
            }

            default: {
                int halfOfPic = pictureHeight / 2;
                //int finalX = halfOfPic - (msgWidthInt / 2);
                return halfOfPic;
            }
        }
    }

    private static float getMessageWidthPixel(String msg, float fontSize) {
        Paint mPaint = new Paint();
        mPaint.setTextSize(fontSize);
        float width = mPaint.measureText(msg, 0, msg.length());
        Log.d(Utility.class.getSimpleName(), "fontsize: " + fontSize + "  msgWidthPixel: " + width);
        return width;
    }

}
