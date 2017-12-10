package jaypaw.pm.easywatermark.domain;

import android.graphics.Bitmap;

/**
 * Created by pmodi4 on 12/10/2017.
 */

public class ImageItem {
    private Bitmap image;
    private String title;

    private String fullFileName;

    public ImageItem(Bitmap image, String title, String fullFileName) {
        super();
        this.image = image;
        this.title = title;
        this.fullFileName = fullFileName;
    }

    public ImageItem(String title, String fullFileName) {
        super();
        this.image = image;
        this.title = title;
        this.fullFileName = fullFileName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }
}
