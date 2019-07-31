package com.ime.testlivevideo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ime.rtmp.ImeLivePlayer;
import com.ime.rtmp.ImePicturePlayer;
import com.ime.rtmp.ui.ImeVideoView;
import com.tencent.rtmp.ui.TXCloudVideoView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImeVideoView mView;
    ImeLivePlayer livePlayer;
    TXCloudVideoView view;
    String playUrl = "http://video.oaksh.cn/live/test1.flv";
    boolean isRecord = false;
    boolean playVideo = true;
    ImePicturePlayer picturePlayer;
    //    String picture = "https://img-blog.csdnimg.cn/20190730100031267.jpg";
    String picture = "2019-07-29 14:19:14.jpg";
    //    String testUrl = "rtmp://10.9.20.164/live/stream";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        picturePlayer = new ImePicturePlayer(this);
        mView = findViewById(R.id.glViewS);
        view = findViewById(R.id.TXVideoView);
        livePlayer = new ImeLivePlayer(this);
        if (playVideo) {
            livePlayer.setPlayerView(mView, view);
            livePlayer.startPlay(playUrl);
        }
        else {
            picturePlayer.setPlayerView(mView);
            picturePlayer.startPlay(picture);
        }
    }

    @Override protected void onPause() {
        super.onPause();
        if (playVideo) {
            livePlayer.pause();
        }
        else {
            picturePlayer.pause();
        }

    }

    @Override protected void onResume() {
        super.onResume();
        if (playVideo) {
            livePlayer.resume();
        }
        else {
            picturePlayer.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playVideo) {
            livePlayer.stopPlay(false); // true 代表清除最后一帧画面
        }
        else {
            picturePlayer.stopPlay();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SphereRotation:
                ImeVideoView.sphereRotationMode();
                break;
            case R.id.SphereClip:
                ImeVideoView.sphereClipMode();
                break;
            case R.id.SphereSpread:
                ImeVideoView.sphereSpreadMode();
                break;
            case R.id.FourClip:
                ImeVideoView.fourClipMode();
                break;
            default:
                ImeVideoView.sphereRotationMode();
                break;
        }
    }

    public void onSnapshot(View v) {
        livePlayer.snapshot();
    }

    public void onRecord(View v) {
        isRecord = !isRecord;
        livePlayer.streamRecord(isRecord);
    }

    public void onTestImage(View v) {
        playVideo = false;
        livePlayer.stopPlay(true);
        picturePlayer.setPlayerView(mView);
        picturePlayer.startPlay(picture);
    }

    public void onTestVideo(View v) {
        playVideo = true;
        picturePlayer.stopPlay();
        livePlayer.setPlayerView(mView, view);
        livePlayer.startPlay(playUrl);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)) {
                permissions.add(Manifest.permission.INTERNET);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), 100);
            }
        }
    }

}
