package com.mcbeengs.imagerdon.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcbeengs.imagerdon.R;
import com.mcbeengs.imagerdon.download.FurAffinity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTasks;
    private List<Task> runningTasks = new ArrayList<>();
    private static Context mContext;

    public TaskAdapter(Context context, List<Task> tasks) {
        mContext = context;
        mTasks = tasks;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View taskView = inflater.inflate(R.layout.fragment_task_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TaskAdapter.ViewHolder holder, int position) {
        final Task task = mTasks.get(position);

        holder.taskType.setText(task.getTypeOfTask() == Task.DOWNLOAD_TASK ? "Download Task" : "Upload Task");
        holder.artistName.setText(task.getArtistName());
        holder.progressBar.setProgress(task.getProgress());
        holder.progressBar.setMax(task.getMaxProgress());
        holder.infoDisplay.setText(task.getDisplayText());
        task.setInfoDisplay(holder.infoDisplay);
        task.setProgressBar(holder.progressBar);

        holder.playButton.setEnabled(task.getDownloadState() == Task.DownloadState.NOT_STARTED);
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setDownloadState(Task.DownloadState.QUEUED);
                holder.playButton.setEnabled(false);
                holder.playButton.invalidate();

                switch (task.getServer()) {
                    case 2:
                        FurAffinity fa = new FurAffinity(task, holder);
                        fa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void addTask(Task task) {
        mTasks.add(task);
        notifyItemInserted(mTasks.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView taskType;
        public TextView artistName;
        public TextView infoDisplay;
        public ProgressBar progressBar;
        public ImageButton playButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            taskType = (TextView) itemView.findViewById(R.id.task_type);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            infoDisplay = (TextView) itemView.findViewById(R.id.info_display);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            playButton = (ImageButton) itemView.findViewById(R.id.play_button);

            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/eras_bold_itc.TTF");
            taskType.setTypeface(custom_font);
        }
    }
}
