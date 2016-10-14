package com.mcbeengs.imagerdon.adapter;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class Task {

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
    private int progress = 0;

    public Task(int typeOfTask, int server, String url) {
        this.server = server;
        this.typeOfTask = typeOfTask;
        this.url = url;
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
