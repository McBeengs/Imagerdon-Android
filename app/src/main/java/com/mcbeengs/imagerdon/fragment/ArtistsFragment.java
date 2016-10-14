package com.mcbeengs.imagerdon.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mcbeengs.imagerdon.R;
import com.mcbeengs.imagerdon.adapter.Artist;
import com.mcbeengs.imagerdon.adapter.ArtistAdapter;
import com.mcbeengs.imagerdon.database.ArtistsSQLite;

/**
 * Created by McBeengs on 08/10/2016.
 */

public class ArtistsFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private boolean isStarted = false;
    protected static RecyclerView recyclerView;
    private StaggeredGridLayoutManager gridLayoutManager;
    private static ArtistAdapter adapter;
    private static LinearLayout searchPane;
    private TextView nothingLabel;
    private ImageButton searchButton;
    private RadioButton daRadio;
    private RadioButton tuRadio;
    private RadioButton faRadio;
    private RadioButton piRadio;
    private RadioButton e621Radio;
    private RadioButton allRadio;
    private RadioButton favRadio;
    private RadioButton lastRadio;
    public static boolean IS_SEARCH_PANE_VISIBLE = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_artists, container, false);
        mContext = view.getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if (adapter == null) {
            ArtistsSQLite sql = new ArtistsSQLite(view.getContext());
            if (!sql.artistExistsOnDB("Freckles", 2)) {
                Artist artist = new Artist();
                artist.setServerId(2);
                artist.setArtistName("Freckles");
                artist.setIconUrl("http://a.facdn.net/1464666087/freckles.gif");
                artist.setServerName("FurAffinity");
                sql.insertArtist(artist);
            }
            if (!sql.artistExistsOnDB("Xpray", 2)) {
                Artist artist = new Artist();
                artist.setServerId(2);
                artist.setArtistName("Xpray");
                artist.setIconUrl("http://a.facdn.net/1424255659/xpray.gif");
                artist.setServerName("FurAffinity");
                sql.insertArtist(artist);
            }
            adapter = new ArtistAdapter(getContext(), sql.getAllArtists());
        }
        recyclerView.setAdapter(adapter);

        setupLayoutManager();
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (!isStarted) {
            searchButton = (ImageButton) view.findViewById(R.id.search_fab);
            searchButton.setOnClickListener(this);

            searchPane = (LinearLayout) view.findViewById(R.id.search_pane);

            TextView filters = (TextView) view.findViewById(R.id.filter_label);
            Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/eras_bold_itc.TTF");
            filters.setTypeface(customFont);

            filters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        CoordinatorLayout row1 = (CoordinatorLayout) view.findViewById(R.id.buttons_row_1);
                        LinearLayout row2 = (LinearLayout) view.findViewById(R.id.buttons_row_2);
                        CoordinatorLayout row3 = (CoordinatorLayout) view.findViewById(R.id.buttons_row_3);

                        if (row1.getVisibility() == View.VISIBLE) {
                            row1.setVisibility(View.GONE);
                            row2.setVisibility(View.GONE);
                            row3.setVisibility(View.GONE);
                            IS_SEARCH_PANE_VISIBLE = false;
                        } else {
                            row1.setVisibility(View.VISIBLE);
                            row2.setVisibility(View.VISIBLE);
                            row3.setVisibility(View.VISIBLE);
                            IS_SEARCH_PANE_VISIBLE = true;
                        }
                    }
                }
            });

            nothingLabel = (TextView) view.findViewById(R.id.nothing_label);

            final EditText searchText = (EditText) view.findViewById(R.id.search_text);
            searchText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchText.setText("");
                }
            });
            searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!searchText.getText().toString().equals("Type something...")) {
                        uncheckAll();
                        allRadio.setChecked(true);
                        ArtistsSQLite sql = new ArtistsSQLite(mContext);
                        adapter = new ArtistAdapter(mContext, sql.getArtistsByMatch(searchText.getText().toString()));
                        onAdapterChanged(adapter);
                        sql.close();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b && searchText.getText().toString().isEmpty()) {
                        searchText.setText("Type something...");
                    }
                }
            });

            daRadio = (RadioButton) view.findViewById(R.id.da_radio_filter);
            tuRadio = (RadioButton) view.findViewById(R.id.tu_radio_filter);
            faRadio = (RadioButton) view.findViewById(R.id.fa_radio_filter);
            piRadio = (RadioButton) view.findViewById(R.id.pi_radio_filter);
            e621Radio = (RadioButton) view.findViewById(R.id.e621_radio_filter);
            allRadio = (RadioButton) view.findViewById(R.id.all_radio_filter);
            favRadio = (RadioButton) view.findViewById(R.id.fav_radio_filter);
            lastRadio = (RadioButton) view.findViewById(R.id.last_radio_filter);

            daRadio.setOnClickListener(this);
            tuRadio.setOnClickListener(this);
            faRadio.setOnClickListener(this);
            piRadio.setOnClickListener(this);
            e621Radio.setOnClickListener(this);
            allRadio.setOnClickListener(this);
            favRadio.setOnClickListener(this);
            lastRadio.setOnClickListener(this);

            isStarted = true;
        }

        return view;
    }

    private void setupLayoutManager() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            gridLayoutManager =
                    new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    public static void hideSearchPane() {
        searchPane.setVisibility(View.GONE);
        IS_SEARCH_PANE_VISIBLE = false;
    }

    @Override
    public void onClick(View view) {
        ArtistsSQLite sql = new ArtistsSQLite(mContext);
        switch (view.getId()) {
            case R.id.search_fab:
                if (searchPane.getVisibility() == View.GONE) {
                    searchPane.setVisibility(View.VISIBLE);
                    IS_SEARCH_PANE_VISIBLE = true;
                } else {
                    searchPane.setVisibility(View.GONE);
                    IS_SEARCH_PANE_VISIBLE = false;
                }
                break;
            case R.id.da_radio_filter:
                uncheckAll();
                daRadio.setChecked(true);
                adapter = new ArtistAdapter(mContext, sql.getArtistsByServer(0));
                onAdapterChanged(adapter);
                break;
            case R.id.tu_radio_filter:
                uncheckAll();
                tuRadio.setChecked(true);
                adapter = new ArtistAdapter(mContext, sql.getArtistsByServer(1));
                onAdapterChanged(adapter);
                break;
            case R.id.fa_radio_filter:
                uncheckAll();
                faRadio.setChecked(true);
                adapter = new ArtistAdapter(mContext, sql.getArtistsByServer(2));
                onAdapterChanged(adapter);
                break;
            case R.id.pi_radio_filter:
                uncheckAll();
                piRadio.setChecked(true);
                adapter = new ArtistAdapter(mContext, sql.getArtistsByServer(3));
                onAdapterChanged(adapter);
                break;
            case R.id.e621_radio_filter:
                uncheckAll();
                e621Radio.setChecked(true);
                adapter = new ArtistAdapter(mContext, sql.getArtistsByServer(4));
                onAdapterChanged(adapter);
                break;
            case R.id.all_radio_filter:
                uncheckAll();
                allRadio.setChecked(true);
                adapter = new ArtistAdapter(mContext, sql.getAllArtists());
                onAdapterChanged(adapter);
                break;
            case R.id.fav_radio_filter:
                uncheckAll();
                favRadio.setChecked(true);
                adapter = new ArtistAdapter(mContext, sql.getFavedArtists());
                onAdapterChanged(adapter);
                break;
            case R.id.last_radio_filter:
                uncheckAll();
                lastRadio.setChecked(true);
                Toast.makeText(mContext, "do sql method to get 5 last downloaded", Toast.LENGTH_SHORT).show();
                break;
        }
        sql.close();
    }



    private void onAdapterChanged(ArtistAdapter adapter) {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        recyclerView.setAdapter(adapter);
        setupLayoutManager();
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 0) {
            nothingLabel.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            nothingLabel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void uncheckAll() {
        RadioButton[] radios = new RadioButton[]{daRadio, tuRadio, faRadio, piRadio, e621Radio, allRadio, favRadio, lastRadio};
        for (RadioButton radio : radios) {
            radio.setChecked(false);
        }
    }
}
