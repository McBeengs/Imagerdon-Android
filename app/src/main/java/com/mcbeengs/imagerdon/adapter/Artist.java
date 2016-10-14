package com.mcbeengs.imagerdon.adapter;

/**
 * Created by McBeengs on 08/10/2016.
 */

public class Artist {

    private int id;
    private int serverId;
    private String serverName;
    private String artistName;
    private String iconUrl;
    private long firstDownloaded;
    private long lastUpdated;
    private int imageCount;
    private int scrapsCount;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getFirstDownloaded() {
        return firstDownloaded;
    }

    public void setFirstDownloaded(long firstDownloaded) {
        this.firstDownloaded = firstDownloaded;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getScrapsCount() {
        return scrapsCount;
    }

    public void setScrapsCount(int scrapsCount) {
        this.scrapsCount = scrapsCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
