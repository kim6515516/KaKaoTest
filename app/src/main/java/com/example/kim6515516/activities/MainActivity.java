package com.example.kim6515516.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.kim6515516.kakaotest.R;
import com.example.kim6515516.models.CustomGridViewAdapter;
import com.example.kim6515516.models.GridItem;
import com.example.kim6515516.models.Model;
import com.example.kim6515516.utils.ApplicationConstants;
import com.example.kim6515516.utils.vLog;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * 옵저버패턴, MVC에서 MV부분
 */
public class MainActivity extends AppCompatActivity implements Observer, AbsListView.OnScrollListener {

    final String TAG = MainActivity.class.getSimpleName();
    ListView mListView;
    ArrayList<GridItem> mGridImgArray;
    CustomGridViewAdapter mCustomGridAdapter;
    private ProgressBar mProgress;

    Model mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridImgArray = new ArrayList<GridItem>();


        mListView = (ListView) findViewById(R.id.listView);
        mCustomGridAdapter = new CustomGridViewAdapter(this, R.layout.raw_grid, mGridImgArray);
        mListView.setAdapter(mCustomGridAdapter);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);


        mModel = new Model(mGridImgArray, this);
        mModel.addObserver(this);
        mModel.init();
        mListView.setOnScrollListener(this);

    }

    @Override
    public void update(Observable observable, Object data) {

        int category = ((Integer)data).intValue();
        vLog.trace("category : " + category);
        switch (category) {
            case ApplicationConstants.NOTI_CHANGE_DATA:
                mCustomGridAdapter.notifyDataSetChanged();
                break;
            case ApplicationConstants.NOTI_PROGRESS_INVISIBLE:
                mProgress.setVisibility(View.INVISIBLE);
                break;
            case ApplicationConstants.NOTI_PROGRESS_VISIBLE:
                mProgress.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
        if(mModel.getGridImgArray() ==0)
            return;
        mModel.updateImages(firstVisibleItem, visibleItemCount);
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }


}
