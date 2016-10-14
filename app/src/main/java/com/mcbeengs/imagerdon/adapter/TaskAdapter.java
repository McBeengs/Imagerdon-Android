package com.mcbeengs.imagerdon.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private static ExecutorService executor;
    private List<Task> mTasks;
    private List<Task> executingTasks;
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
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        Task task = mTasks.get(position);

        Toast.makeText(mContext, "yes", Toast.LENGTH_SHORT).show();
//
//        if (executor == null) {
//            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
//            executor = Executors.newFixedThreadPool(pref.getInt("simultaneous_tasks", 5));
//        }
//
//        if (executingTasks == null) {
//            executingTasks = new ArrayList<>();
//        }
//
//        if (!executingTasks.contains(task)) {
//            executingTasks.add(task);
//            switch (task.getServer()) {
//                case 2:
//                    executor.execute(new FurAffinity(task, holder));
//                    break;
//            }
//        }
//        TextView taskType = holder.taskType;
//        TextView artistName = holder.artistName;
//        TextView infoDisplay = holder.infoDisplay;
//        ProgressBar progressBar = holder.progressBar;
//        ImageButton playButton = holder.playButton;
//
//        taskType.setText(task.getTypeOfTask() == Task.DOWNLOAD_TASK ? "Download Task" : "Upload Task");
//        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/eras_bold_itc.TTF");
//        taskType.setTypeface(custom_font);
//        artistName.setText(task.getArtistName());
//        progressBar.setMax(task.getNumOfImages());
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
