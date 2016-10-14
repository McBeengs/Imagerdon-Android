package com.mcbeengs.imagerdon.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.mcbeengs.imagerdon.R;
import com.mcbeengs.imagerdon.SlidingTabLayout;
import com.mcbeengs.imagerdon.activity.prefs.PrefsActivity;
import com.mcbeengs.imagerdon.adapter.MainActivityTabsAdapter;
import com.mcbeengs.imagerdon.adapter.Task;
import com.mcbeengs.imagerdon.fragment.ArtistsFragment;
import com.mcbeengs.imagerdon.fragment.TasksFragment;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.OrderType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BoomMenuButton boomMenuButton;
    private boolean isBoomMenuStarted = false;
    private boolean isBoomButtonVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        setUpToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        FragmentPagerAdapter adapterViewPager = new MainActivityTabsAdapter(getContext(), getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setPageTransformer(true, new RotateUpTransformer());

        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        tabLayout.setDistributeEvenly(true);
        //tabLayout.setCustomTabView(R.layout.tab_layout, R.id.tab_text);
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });
        tabLayout.setViewPager(viewPager);

        boomMenuButton = (BoomMenuButton) findViewById(R.id.boom);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_turn_night_mode) {
            Toast.makeText(this, "Night Mode", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_about) {
            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, PrefsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (isBoomMenuStarted) {
            return;
        }

        int[][] subButtonColors = new int[5][2];
        int[] boomColors = new int[]{R.color.colorDABoom, R.color.colorTUBoom, R.color.colorFABoom, R.color.colorPIBoom, R.color.colorE621Boom};
        for (int i = 0; i < 5; i++) {
            subButtonColors[i][1] = ContextCompat.getColor(this, boomColors[i]);
            subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
        }

        new BoomMenuButton.Builder()
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.button_da_boom), subButtonColors[0], null)
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.button_tu_boom), subButtonColors[1], null)
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.button_fa_boom), subButtonColors[2], null)
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.button_pi_boom), subButtonColors[3], null)
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.button_e621_boom), subButtonColors[4], null)
                .button(ButtonType.CIRCLE)
                .boom(BoomType.LINE)
                .autoDismiss(false)
                .showOrder(OrderType.RANDOM)
                .hideOrder(OrderType.RANDOM)
                .place(PlaceType.CIRCLE_5_1)
                .init(boomMenuButton);

        boomMenuButton.setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener() {
            @Override
            public void onClick(final int buttonIndex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final EditText urlField = new EditText(MainActivity.this);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Add a task")
                                .setMessage("Paste the url below:")
                                .setView(urlField)
                                .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String url = urlField.getText().toString();
                                        TasksFragment.addTask(getContext(), Task.DOWNLOAD_TASK, buttonIndex, url);
                                        boomMenuButton.dismiss();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        boomMenuButton.dismiss();
                                    }
                                })
                                .show();
                    }
                });
            }
        });

        boomMenuButton.setAnimatorListener(new BoomMenuButton.AnimatorListener() {
            @Override
            public void toShow() {
                animateBoomButton();
            }

            @Override
            public void showing(float fraction) {

            }

            @Override
            public void showed() {
                if (isBoomButtonVisible) {
                    animateBoomButton();
                }
            }

            @Override
            public void toHide() {
                animateBoomButton();
            }

            @Override
            public void hiding(float fraction) {

            }

            @Override
            public void hided() {
                if (!isBoomButtonVisible) {
                    animateBoomButton();
                }
            }
        });

        //android:src="@android:drawable/ic_input_add"
        //setForeground() documentation is bugged, added an suppression just to not bother
        boomMenuButton.setForeground(ContextCompat.getDrawable(this, R.drawable.ic_plus));
        boomMenuButton.setForegroundGravity(Gravity.CENTER);


        isBoomMenuStarted = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !isBoomButtonVisible) {
            isBoomButtonVisible = true;
            boomMenuButton.dismiss();
            animateBoomButton();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && ArtistsFragment.IS_SEARCH_PANE_VISIBLE) {
            ArtistsFragment.hideSearchPane();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void animateBoomButton() {
        ScaleAnimation grow = new ScaleAnimation(
                0.0f, 1.0f, // X inicial e final
                0.0f, 1.0f, // Y inicial e final
                Animation.RELATIVE_TO_SELF, 0.5f, // Eixo X
                Animation.RELATIVE_TO_SELF, 0.5f  // Eixo Y
        );
        ScaleAnimation shrink = new ScaleAnimation(
                1.0f, 0.0f, // X inicial e final
                1.0f, 0.0f, // Y inicial e final
                Animation.RELATIVE_TO_SELF, 0.5f, // Eixo X
                Animation.RELATIVE_TO_SELF, 0.5f  // Eixo Y
        );

        Animation anim = isBoomButtonVisible ? shrink : grow;
        anim.setDuration(200);
        anim.setFillAfter(true);
        boomMenuButton.startAnimation(anim);
        isBoomButtonVisible = !isBoomButtonVisible;
    }
}
