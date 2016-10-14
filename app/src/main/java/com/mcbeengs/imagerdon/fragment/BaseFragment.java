package com.mcbeengs.imagerdon.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class BaseFragment extends Fragment {

    private Map<String, Work> works = new HashMap<String, Work>();
    private ProgressDialog progress;
    private SwipeRefreshLayout swipeLayout;

    public Context getContext() {
        return getActivity();
    }

    public void startWork(String cod, WorkListener listener) {
        startWork(cod, listener, 0);
    }

    public void startWork(String cod, WorkListener listener, int progressId) {
        Log.d("livroandroid", "startTask: " + cod);
        View view = getView();
        if (view == null) {
            throw new RuntimeException("Somente pode iniciar a task se a view do fragment foi criada.\nChame o startTask depois do onCreateView");
        }

        Work work = this.works.get(cod);
        if (work == null) {
            // Somente executa se já não está executando
            work = new Work(cod, listener, progressId);
            this.works.put(cod, work);
            work.execute();
        }
    }

    private class WorkResult<T> {
        private T response;
        private Exception exception;
    }

    public interface WorkListener<T> {
        // Executa em background e retorna o objeto
        T execute() throws Exception;

        // Atualiza a view na UI Thread
        void updateView(T response);

        // Chamado caso o método execute() lance uma exception
        void onError(Exception exception);

        // Chamado caso a task tenha sido cancelada
        void onCancelled(String cod);
    }

    private class Work extends AsyncTask<Void, Void, WorkResult> {

        private String cod;
        private WorkListener listener;
        private int progressId;

        private Work(String cod, WorkListener listener, int progressId) {
            this.cod = cod;
            this.listener = listener;
            this.progressId = progressId;
        }

        @Override
        protected void onPreExecute() {
            showProgress(this, progressId);
        }

        @Override
        protected WorkResult doInBackground(Void... params) {
            WorkResult r = new WorkResult();
            try {
                r.response = listener.execute();
            } catch (Exception e) {

            }
            return r;
        }

        protected void onPostExecute(WorkResult result) {
            try {
                if (result != null) {
                    if (result.exception != null) {
                        listener.onError(result.exception);
                    } else {
                        listener.updateView(result.response);
                    }
                }
            } finally {
                works.remove(cod);
                closeProgress(progressId);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            works.remove(cod);
            listener.onCancelled(cod);
        }
    }

    public static class BaseWork<T> implements WorkListener<T> {

        @Override
        public T execute() throws Exception {
            return null;
        }

        @Override
        public void updateView(T response) {

        }

        @Override
        public void onError(Exception exception) {

        }

        @Override
        public void onCancelled(String cod) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        stopTasks();
    }

    private void stopTasks() {
        if (works != null) {
            for (String key : works.keySet()) {
                Work task = works.get(key);
                if (task != null) {
                    boolean running = task.getStatus().equals(AsyncTask.Status.RUNNING);
                    if (running) {
                        task.cancel(true);
                        closeProgress(0);
                    }
                }
            }
            works.clear();
        }
    }

    private void closeProgress(int progressId) {
        if (progressId > 0 && getView() != null) {
            View view = getView().findViewById(progressId);
            if (view != null) {
                if (view instanceof SwipeRefreshLayout) {
                    SwipeRefreshLayout srl = (SwipeRefreshLayout) view;
                    srl.setRefreshing(false);
                } else {
                    view.setVisibility(View.GONE);
                }
                return;
            }
        }

        Log.d("livroandroid", "closeProgress()");
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    protected void showProgress(final Work task, int progressId) {
        if (progressId > 0 && getView() != null) {
            View view = getView().findViewById(progressId);
            if (view != null) {
                if (view instanceof SwipeRefreshLayout) {
                    SwipeRefreshLayout srl = (SwipeRefreshLayout) view;
                    if (!srl.isRefreshing()) {
                        srl.setRefreshing(true);
                    }
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                return;
            }
        }

        // Mostra o dialog e permite cancelar
        if (progress == null) {
            progress = ProgressDialog.show(getActivity(), "Aguarde", "Por favor aguarde...");
            progress.setCancelable(true);
            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // Cancela a AsyncTask
                    task.cancel(true);
                }
            });
        }
    }

    protected void setTextString(int resId, String text) {
        View view = getView();
        if (view != null) {
            TextView t = (TextView) view.findViewById(resId);
            if (t != null) {
                t.setText(text);
            }
        }
    }

    protected String getTextString(int resId) {
        View view = getView();
        if (view != null) {
            TextView t = (TextView) view.findViewById(resId);
            if (t != null) {
                return t.getText().toString();
            }
        }
        return null;
    }

    public android.support.v7.app.ActionBar getActionBar() {
        AppCompatActivity ac = getAppCompatActivity();
        return ac.getSupportActionBar();
    }

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }
}
