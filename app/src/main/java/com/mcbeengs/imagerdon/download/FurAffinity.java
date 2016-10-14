package com.mcbeengs.imagerdon.download;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mcbeengs.imagerdon.adapter.Task;
import com.mcbeengs.imagerdon.adapter.TaskAdapter;

/**
 * Created by McBeengs on 08/10/2016.
 */

public class FurAffinity extends BasicCore {

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
        progressBar = view.progressBar;
        playButton = view.playButton;

        taskType.setText(task.getTypeOfTask() == Task.DOWNLOAD_TASK ? "Download Task" : "Upload Task");
        artistName.setText("qualquer coisa");
        progressBar.setMax(task.getNumOfImages());
    }

    @Override
    public void run() {
        progressBar.setMax(100);
        for (int i = 0; i < 100; i++) {
            progressBar.setProgress(i);
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.invalidate();
                }
            });

            final int c = i;
            infoDisplay.post(new Runnable() {
                @Override
                public void run() {
                    infoDisplay.setText("Downloading, " + (100 - c) + " images left");
                    infoDisplay.invalidate();
                }
            });


            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
