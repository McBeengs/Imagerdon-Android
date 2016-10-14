package com.mcbeengs.imagerdon.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.mcbeengs.imagerdon.R;
import com.mcbeengs.imagerdon.adapter.Artist;
import com.mcbeengs.imagerdon.adapter.ArtistAdapter;
import com.mcbeengs.imagerdon.adapter.TagsAdapter;
import com.mcbeengs.imagerdon.database.ArtistsSQLite;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * Created by McBeengs on 09/10/2016.
 */

public class ArtistActivity extends BaseActivity implements View.OnClickListener {

    private boolean artistDeleted = false;
    private boolean isInitiated = false;
    private Artist artist;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView tagsRecyclerView;
    private ImageView background;
    private EditText description;
    private ImageButton favorite;
    private ImageButton folderButton;
    private ImageButton updateButton;
    private ImageButton deleteButton;
    private ImageButton browseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        setUpToolbar();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ArtistIconCollapsedToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ArtistIconExpandedToolbar);

        if (artist == null) {
            artist = new ArtistsSQLite(this).getArtistsByMatch((String) getIntent().getSerializableExtra("artist_name")).get(0);
        }

        initComponents();
    }

    private void initComponents() {
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        background = (ImageView) findViewById(R.id.background_image);
        Picasso.with(this).load(artist.getIconUrl()).transform(new BlurTransformation(this, 100))
                .fit().into(background);

        CircularImageView icon = (CircularImageView) findViewById(R.id.artist_icon);
        Picasso.with(this).load(artist.getIconUrl()).fit().into(icon);


        if (!isInitiated) {
            new SetupToolbarWithPalette().execute(collapsingToolbarLayout);

            Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/eras_bold_itc.TTF");
            TextView artistName = (TextView) findViewById(R.id.artist_name);
            artistName.setTypeface(customFont);
            artistName.setText(artist.getArtistName());

            TextView serverName = (TextView) findViewById(R.id.server_name);
            serverName.setText(artist.getServerName());

            TextView quickActions = (TextView) findViewById(R.id.quick_actions);
            quickActions.setTypeface(customFont);

            TextView descriptionLabel = (TextView) findViewById(R.id.description_label);
            descriptionLabel.setTypeface(customFont);

            TextView tagsLabel = (TextView) findViewById(R.id.tags_label);
            tagsLabel.setTypeface(customFont);

            final ArtistsSQLite sql = new ArtistsSQLite(this);
            favorite = (ImageButton) findViewById(R.id.favorite_fab);
            favorite.setOnClickListener(this);

            if (sql.isArtistFaved(artist)) {
                favorite.setColorFilter(Color.parseColor("#e0421b"));
            }

            folderButton = (ImageButton) findViewById(R.id.gallery_button);
            folderButton.setOnClickListener(this);

            updateButton = (ImageButton) findViewById(R.id.update_button);
            updateButton.setOnClickListener(this);

            deleteButton = (ImageButton) findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(this);

            browseButton = (ImageButton) findViewById(R.id.browse_button);
            browseButton.setOnClickListener(this);

            description = (EditText) findViewById(R.id.description);
            description.setText(sql.getDescription(artist));
            description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    sql.setDescription(description.getText().toString(), artist);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            tagsRecyclerView = (RecyclerView) findViewById(R.id.tags_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            tagsRecyclerView.setLayoutManager(layoutManager);
            List<String> list = sql.getTagsFromArtist(artist.getId());
            tagsRecyclerView.setAdapter(new TagsAdapter(this, artist.getId(), list));

            sql.close();
            isInitiated = true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == favorite) {
            ArtistsSQLite sql = new ArtistsSQLite(getContext());
            if (sql.isArtistFaved(artist)) {
                sql.setArtistUnfaved(artist);
                favorite.setColorFilter(Color.parseColor("#FFFFFF"));
                Toast.makeText(this, "Artist \"" + artist.getArtistName() + "\" removed from favorites.", Toast.LENGTH_SHORT).show();
            } else {
                sql.setArtistFaved(artist);
                favorite.setColorFilter(Color.parseColor("#e0421b"));
                Toast.makeText(this, "Artist \"" + artist.getArtistName() + "\" added to favorites.", Toast.LENGTH_SHORT).show();
            }
            sql.close();
        } else if (view == folderButton) {
            Toast.makeText(this, "Open button_folder for \"" + artist.getArtistName() + "\"", Toast.LENGTH_SHORT).show();
        } else if (view == updateButton) {
            Toast.makeText(this, "Update \"" + artist.getArtistName() + "\"", Toast.LENGTH_SHORT).show();
        } else if (view == deleteButton) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Are you sure you want to button_delete \"" + artist.getArtistName() + "\" ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            artistDeleted = true;

                            new AlertDialog.Builder(getContext())
                                    .setMessage("you want to button_delete all his / her images too?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            onBackPressed();
                                            ArtistAdapter.REMOVE.remove(artist.getArtistName());
                                            Toast.makeText(getContext(), "Delete all " + artist.getArtistName() + " images", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            onBackPressed();
                                            ArtistAdapter.REMOVE.remove(artist.getArtistName());
                                        }
                                    }).show();
                            Toast.makeText(getContext(), "Artist \"" + artist.getArtistName() + "\" deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
        } else if (view == browseButton) {
            String url;
            switch (artist.getServerId()) {
                case 0:
                    url = "http://" + artist.getArtistName().toLowerCase() + ".deviantart.com/gallery/?catpath=/";
                    break;
                case 1:
                    url = "http://" + artist.getArtistName().toLowerCase() + ".tumblr.com/archive/";
                    break;
                case 2:
                    url = "http://www.furaffinity.net/gallery/" + artist.getArtistName().toLowerCase() + "/";
                    break;
                case 3:
                    url = "http://www.pixiv.net/";
                    break;
                case 4:
                    url = "https://e621.net/post/index/1/" + artist.getArtistName().toLowerCase() + "/";
                    break;
                default:
                    url = "http://www.google.com";
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    private class SetupToolbarWithPalette extends AsyncTask<CollapsingToolbarLayout, Integer, Void> {

        @Override
        protected Void doInBackground(CollapsingToolbarLayout... collapsingToolbarLayouts) {
            try {
                final CollapsingToolbarLayout collapsingToolbarLayout = collapsingToolbarLayouts[0];
                Bitmap bitmap = Picasso.with(getContext()).load(artist.getIconUrl()).get();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onGenerated(Palette palette) {
                        int def = 0x000000;
                        collapsingToolbarLayout.setContentScrimColor(palette.getLightVibrantColor(def));
                        background.setBackgroundColor(palette.getLightVibrantColor(def));
                        //darken version of LightVibrantColor for status bar
                        float[] hsv = new float[3];
                        Color.colorToHSV(palette.getLightVibrantColor(def), hsv);
                        hsv[2] *= 0.8f; // value component
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(Color.HSVToColor(hsv));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
