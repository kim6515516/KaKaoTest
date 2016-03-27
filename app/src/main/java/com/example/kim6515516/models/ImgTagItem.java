package com.example.kim6515516.models;

/**
 * Created by kim6515516 on 2016-03-27.
 */
public class ImgTagItem {
    int index;
    String thumURL;
    String oriURL;
    String title;
    public ImgTagItem(int _index, String _thumURL, String _oriURL) {
        index = _index;
        thumURL = _thumURL;
        oriURL = _oriURL;
    }

    public int setThumURL(String _thumURL) {
        thumURL = _thumURL;
        return 0;
    }
    public int setoriURL(String _oriURL) {
        oriURL = _oriURL;
        return 0;
    }

    public String getThumURL() {
        return thumURL;
    }

    public int setTitle(String _title) {
        title = _title;
        return 0;
    }

    public String getTitle() {
        return title;
    }


}
