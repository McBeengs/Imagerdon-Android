package com.mcbeengs.imagerdon.adapter;

import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class Task {

    private volatile DownloadState mDownloadState = DownloadState.NOT_STARTED;
    private volatile ProgressBar mProgressBar;
    private volatile TextView mInfoDisplay;
    private volatile String displayText;
    private volatile Integer mProgress;
    private volatile int progress = 0;
    private volatile int maxProgress = 100;
    public final static int DEVIANT_ART = 0;
    public final static int TUMBLR = 1;
    public final static int FUR_AFFINITY = 2;
    public final static int PIXIV = 3;
    public final static int E621 = 4;
    public final static int DOWNLOAD_TASK = 5;
    public final static int UPDATE_TASK = 6;
    private int typeOfTask;
    private int server;
    private String url;
    private String artistName;
    private int numOfImages;
    private boolean isPaused = true;

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }


    public enum DownloadState {
        NOT_STARTED,
        QUEUED,
        DOWNLOADING,
        COMPLETE
    }

    public Task(int typeOfTask, int server, String url) {
        this.server = server;
        this.typeOfTask = typeOfTask;
        this.url = url;
        mProgressBar = null;
        mInfoDisplay = null;
        mProgress = 0;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }
    public void setProgressBar(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    public TextView getInfoDisplay() {
        return mInfoDisplay;
    }

    public void setInfoDisplay(TextView infoDisplay) {
        this.mInfoDisplay = infoDisplay;
    }

    public void setDownloadState(DownloadState state) {
        mDownloadState = state;
    }
    public DownloadState getDownloadState() {
        return mDownloadState;
    }

    public Integer getProgress() {
        return mProgress;
    }

    public void setProgress(Integer progress) {
        this.mProgress = progress;
    }

    public Integer getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(Integer progress) {
        this.maxProgress = progress;
    }

    public int getTypeOfTask() {
        return typeOfTask;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getNumOfImages() {
        return numOfImages;
    }

    public int getServer(){
        return server;
    }
}
