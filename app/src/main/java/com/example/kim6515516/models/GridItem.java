package com.example.kim6515516.models;
import android.graphics.Bitmap;
/**
 * Created by kim6515516 on 2016-03-27.
 */
public class GridItem {
    Bitmap image;
    String title;
    int index;

    public GridItem(Bitmap image, String title, int index) {
        super();
        this.image = image;
        this.title = title;
        this.index = index;
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


    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}