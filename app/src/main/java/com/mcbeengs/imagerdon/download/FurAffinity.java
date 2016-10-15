package com.mcbeengs.imagerdon.download;

import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcbeengs.imagerdon.adapter.Task;
import com.mcbeengs.imagerdon.adapter.TaskAdapter;

/**
 * Created by McBeengs on 08/10/2016.
 */

public class FurAffinity extends AsyncTask<Void, Integer, Void> {

    private Task task;
    TextView taskType;
    TextView artistName;
    TextView infoDisplay;
    ProgressBar progressBar;
    ImageButton playButton;

    public FurAffinity(Task task, TaskAdapter.ViewHolder view) {
        this.task = task;

        taskType = view.taskType;
        artistName = view.artistName;
        infoDisplay = view.infoDisplay;
        progressBar = task.getProgressBar();
        playButton = view.playButton;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        task.setProgress(values[0]);
        ProgressBar bar = task.getProgressBar();
        if (bar != null) {
            bar.setProgress(task.getProgress());
            bar.invalidate();
        }

        TextView display = task.getInfoDisplay();
        if (display != null) {
            display.setText("Downloading, " + (task.getMaxProgress() - task.getProgress()) + " images left");
            display.invalidate();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        task.setDownloadState(Task.DownloadState.QUEUED.DOWNLOADING);
        task.setMaxProgress(100);
        for (int i = 0; i <= task.getMaxProgress(); i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            publishProgress(i);

        }

        task.setDownloadState(Task.DownloadState.COMPLETE);
        return null;
    }
}
