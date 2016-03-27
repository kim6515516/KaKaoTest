package com.example.kim6515516.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.example.kim6515516.activities.MainActivity;
import com.example.kim6515516.utils.ApplicationConstants;
import com.example.kim6515516.utils.vLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * Created by kim6515516 on 2016-03-27.
 * 옵저버 패턴, MVC에서 모델 부분
 */
public class Model extends Observable {
    ArrayList<GridItem> mGridImgArray;
    LinkedList<GridItem> mPostImgBuffer;
    ArrayList<ImgTagItem> mImgUrls;
    boolean mLockListView;
    int mSeekbar = 0;
    Context mContext;

    public Model() {
        mGridImgArray = new ArrayList<GridItem>();
        mPostImgBuffer = new LinkedList<GridItem>();
        mImgUrls = new ArrayList<ImgTagItem>();
    }
    public Model( ArrayList<GridItem> gridArray, Context context) {
        mGridImgArray = gridArray;
        mPostImgBuffer = new LinkedList<GridItem>();
        mImgUrls = new ArrayList<ImgTagItem>();
        mContext = context;
    }

    public void init() {
        GetImgURLThread initImgUrls = new GetImgURLThread();
        initImgUrls.execute();

        GetImageFromWeb mInitImgDownloadThread = new GetImageFromWeb();
        GetImageFromWeb mPostImgDownloadThread = new GetImageFromWeb();

        if(mGridImgArray.size() == 0) {
            mInitImgDownloadThread.execute(0, 10, mGridImgArray);
            mPostImgDownloadThread.execute(10, 20, mPostImgBuffer);
        }
    }

    public void updateImgArray() {

        mLockListView = true;

        int size = mGridImgArray.size();
        int postBufferSize = mPostImgBuffer.size();
        if( size == 0 || postBufferSize ==0)
        {
            mLockListView = false;
            return;
        }

        vLog.trace(mGridImgArray.get(0).index + " " + mGridImgArray.get(size - 1).index + " " + mSeekbar);
        if(  (mGridImgArray.get(0).index +  mGridImgArray.get(size-1).index)/2  <= mSeekbar/2 +4 ) {

            int lastBufferIndex = mPostImgBuffer.get(postBufferSize-1).index;

            for(int i=0; i< postBufferSize; i++){
                mGridImgArray.add(mPostImgBuffer.get(i));
            }

            for(int i=0; i< postBufferSize; i++){
                mPostImgBuffer.remove(0);
            }
//            mCustomGridAdapter.notifyDataSetChanged();
            notifyObserver(ApplicationConstants.NOTI_CHANGE_DATA);

            GetImageFromWeb mImgDownloadThread = new GetImageFromWeb();
            mImgDownloadThread.execute(lastBufferIndex + 1, lastBufferIndex + 10, mPostImgBuffer);

        } else
            mLockListView = false;

    }

    public void notifyObserver(int action) {

        setChanged();
        this.notifyObservers(new Integer(action));
    }

    public boolean getListViewLock() {
        return mLockListView;
    }

    public int getGridImgArray() {
        return mGridImgArray.size();
    }


    public void updateImages(int firstVisibleItem, int visibleItemCount) {

        mSeekbar = mGridImgArray.get(firstVisibleItem).index;

        if (mLockListView== false) {
              this.updateImgArray();
            }

    }

    class GetImgURLThread extends AsyncTask<Integer, Void, Integer> {
        //        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public GetImgURLThread() {
            // WeakReference 를 사용하는 이유는 image 처럼 메모리를 많이 차지하는 객체에 대한 가비지컬렉터를 보장하기 위해서입니다.
//            imageViewReference = new WeakReference<ImageView>();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                URL conn = new URL(ApplicationConstants.DOMAIN_URL + "/collections/archive/slim-aarons.aspx");
                URLConnection yc = conn.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yc.getInputStream()));
                String inputLine;
                StringBuffer sbf = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                    sbf.append(inputLine);

                in.close();
                Document doc = Jsoup.parse(sbf.toString());
                Elements imgThumSources = doc.select("div.gallery-item-group img[src] ");

                int index = 0;
                for (Element e : imgThumSources) {
                    String imgSrc = e.attr("src");
                    mImgUrls.add(new ImgTagItem(index, imgSrc, ""));
                    index++;
                }
                index = 0;
                Elements imgFullSources = doc.select("div.gallery-item-group div.gallery-item-caption a[href]");

                for (Element e : imgFullSources) {
                    String imgSrc = e.attr("href");
                    mImgUrls.get(index).setoriURL(imgSrc);
                    index++;
                }

                Elements titles = doc.select("div.gallery-item-group div.gallery-item-caption a[href]");
                index = 0;
                for(Element e: titles){
                    String title = e.text();
                    mImgUrls.get(index).setTitle(title);
                    index++;
                }


            } catch (Exception e) {
                vLog.trace(e.toString());
            }
            return 0;
        }
        @Override
        protected void onPreExecute()
        {
            notifyObserver(ApplicationConstants.NOTI_PROGRESS_VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer integer) {
//            mProgress.setVisibility(View.INVISIBLE);
            notifyObserver(ApplicationConstants.NOTI_PROGRESS_INVISIBLE);
            super.onPostExecute(integer);
        }
    }


    class GetImageFromWeb extends AsyncTask<Object, Void, Integer> {
        //        private final WeakReference<ImageView> imageViewReference;
        private int start = 0;
        private int end = 0;
        List<GridItem> tempArray;

        public GetImageFromWeb() {
            // WeakReference 를 사용하는 이유는 image 처럼 메모리를 많이 차지하는 객체에 대한 가비지컬렉터를 보장하기 위해서입니다.
//            imageViewReference = new WeakReference<ImageView>();
        }

        @Override
        protected void onPreExecute()
        {
//            mProgress.setVisibility(View.VISIBLE);
            notifyObserver(ApplicationConstants.NOTI_PROGRESS_VISIBLE);
        }

        @Override
        protected   Integer doInBackground(Object... params) {
            start = (Integer)params[0];
            end = (Integer)params[1];
            tempArray = (List<GridItem>)params[2];

            for (int i = start; i < end; i++) {
                try {

                    Bitmap bm= Glide.with(mContext)
                            .load(ApplicationConstants.DOMAIN_URL + mImgUrls.get(i).getThumURL())
                            .asBitmap().
                            into(100, 100). // Width and height
                            get();
                    tempArray.add(new GridItem(bm, mImgUrls.get(i).getTitle(), mImgUrls.get(i).index));

                } catch (Exception e) {
                    vLog.trace(e.toString());
                }
            }
            mLockListView = false;
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
//            mCustomGridAdapter.notifyDataSetChanged();
//            mProgress.setVisibility(View.INVISIBLE);
            notifyObserver(ApplicationConstants.NOTI_CHANGE_DATA);
            notifyObserver(ApplicationConstants.NOTI_PROGRESS_INVISIBLE);
            super.onPostExecute(integer);
        }

    }
}
