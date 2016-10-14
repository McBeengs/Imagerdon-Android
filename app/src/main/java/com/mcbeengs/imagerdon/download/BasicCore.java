package com.mcbeengs.imagerdon.download;

/**
 * Created by McBeengs on 08/10/2016.
 */

public class BasicCore extends Thread {

    @Override
    public void run(){
        throw new UnsupportedOperationException("This method must be overwritten");
    }

    public void pause(){
        throw new UnsupportedOperationException("This method must be overwritten");
    }

    public void play(){
        throw new UnsupportedOperationException("This method must be overwritten");
    }

    public void terminate(){
        throw new UnsupportedOperationException("This method must be overwritten");
    }
}
