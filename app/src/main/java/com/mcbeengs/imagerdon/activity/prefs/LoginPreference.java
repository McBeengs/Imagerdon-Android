package com.mcbeengs.imagerdon.activity.prefs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mcbeengs.imagerdon.R;

import static java.lang.Thread.sleep;

/**
 * Created by McBeengs on 01/10/2016.
 */

public class LoginPreference extends DialogPreference implements DialogInterface.OnClickListener, View.OnClickListener {

    private static final String androidDNS = "http://schemas.android.com/apk/res/android";
    private Context context;
    private SharedPreferences sharedPreferences;
    private String server;
    private String keyToUsername;
    private String keyToPassword;
    private EditText usernameText;
    private EditText passwordText;
    private Button cancelButton;
    private Button testButton;

    public LoginPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoginPreference, 0, 0);
        try {
            int i = ta.getInteger(R.styleable.LoginPreference_server, -1);
            switch (i) {
                case 0:
                    server = "deviantart";
                    break;
                case 1:
                    server = "tumblr";
                    break;
                case 2:
                    server = "furaffinity";
                    break;
                case 3:
                    server = "pixiv";
                    break;
                case 4:
                    server = "e621";
                    break;
            }

            keyToUsername = ta.getString(R.styleable.LoginPreference_keyToUsername);
            keyToPassword = ta.getString(R.styleable.LoginPreference_keyToPassword);
        } finally {
            ta.recycle();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setPositiveButtonText(null);
        setNegativeButtonText(null);
    }


    @Override
    protected View onCreateDialogView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_settings_login, null);

        usernameText = (EditText) layout.findViewById(R.id.username);
        passwordText = (EditText) layout.findViewById(R.id.password);
        cancelButton = (Button) layout.findViewById(R.id.cancelButton);
        testButton = (Button) layout.findViewById(R.id.testButton);

        usernameText.setText(sharedPreferences.getString(server + "_user", "Username"));
        passwordText.setText(sharedPreferences.getString(server + "_pass", "Password"));
        cancelButton.setOnClickListener(this);
        testButton.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cancelButton) {
            ((AlertDialog) getDialog()).dismiss();
        } else if (view.getId() == R.id.testButton) {
            new SubmitForm(getContext(), usernameText.getText().toString(), passwordText.getText().toString()).execute();
        }
    }

    private class SubmitForm extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private SharedPreferences sharedPref;
        private Context context;
        private String user;
        private String pass;

        public SubmitForm(Context context, String user, String pass) {
            this.context = context;
            this.user = user;
            this.pass = pass;
        }

        protected void onPostExecute(Void dResult) {
            //dialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Your Title")
                    .setMessage("Some message...")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        protected void onPreExecute() {
            dialog = ProgressDialog.show(context, "", "Loading...", true, true);
            sharedPref = getSharedPreferences();
        }

        protected Void doInBackground(Void... params) {
            try {
                sleep(5000);
            } catch (Exception ex) {

            }

            //Implement WebService test here...

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(server + "_user", user);
            editor.putString(server + "_pass", pass);
            editor.commit();

            dialog.cancel();
            dialog.dismiss();

            return null;
        }
    }
}

