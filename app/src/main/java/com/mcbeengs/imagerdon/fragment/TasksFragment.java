package com.mcbeengs.imagerdon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcbeengs.imagerdon.R;
import com.mcbeengs.imagerdon.adapter.Task;
import com.mcbeengs.imagerdon.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class TasksFragment extends BaseFragment {

    protected static RecyclerView recyclerView;
    private static TaskAdapter adapter;
    private static CoordinatorLayout noTasksPane;
    private static List<Task> stack = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        noTasksPane = (CoordinatorLayout) view.findViewById(R.id.no_tasks_pane);

        if (adapter == null) {
            adapter = new TaskAdapter(view.getContext(), (List) new ArrayList<>());
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (adapter.getItemCount() <= 0) {
            noTasksPane.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public static void addTask(Context context, int type, int server, String url) {
        noTasksPane.setVisibility(View.GONE);

        if (recyclerView.getAdapter().getItemCount() < /*pref.getInt("simultaneous_tasks", 5)*/ 1) {
            Task task = new Task(type, server, url);
            adapter = (TaskAdapter) recyclerView.getAdapter();
            adapter.addTask(task);

        } else {
            stack.add(new Task(type, server, url));
            Snackbar.make(recyclerView, "Task added to waiting stack    (" + stack.size() + " awaiting)", Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
        }
    }
}
