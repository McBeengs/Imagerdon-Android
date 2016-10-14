package com.mcbeengs.imagerdon.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcbeengs.imagerdon.R;
import com.mcbeengs.imagerdon.activity.ArtistActivity;
import com.mcbeengs.imagerdon.database.ArtistsSQLite;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private static List<Artist> mArtists;
    private static AlertDialog dialog;
    private static Context mContext;
    public static RemoveArtist REMOVE;

    public ArtistAdapter(Context context, List<Artist> artists) {
        mContext = context;
        mArtists = artists;
        Collections.sort(mArtists, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                return o1.getArtistName().compareTo(o2.getArtistName());
            }
        });
        REMOVE = new RemoveArtist();
    }

    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View taskView = inflater.inflate(R.layout.fragment_artist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ArtistAdapter.ViewHolder holder, int position) {
        Artist artist = mArtists.get(position);

        holder.progress.setVisibility(View.VISIBLE);
        Picasso.with(mContext).load(artist.getIconUrl()).fit().into(holder.artistIcon, new Callback() {
            @Override
            public void onSuccess() {
                holder.progress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progress.setVisibility(View.GONE);
            }
        });

        holder.artistName.setText(artist.getArtistName());
        holder.serverName.setText(artist.getServerName());
    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView artistIcon;
        public TextView artistName;
        public TextView serverName;
        public ProgressBar progress;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            artistIcon = (ImageView) itemView.findViewById(R.id.artist_icon);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            serverName = (TextView) itemView.findViewById(R.id.server_name);
            progress = (ProgressBar) itemView.findViewById(R.id.progress);

            Typeface custom_font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/eras_bold_itc.TTF");
            artistName.setTypeface(custom_font);
        }


        @Override
        public void onClick(View view) {
            ImageView icon = (ImageView) view.findViewById(R.id.artist_icon);
            Intent intent = new Intent(view.getContext(), ArtistActivity.class);
            intent.putExtra("artist_name", artistName.getText().toString());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), icon, "avatar");
                view.getContext().startActivity(intent, options.toBundle());
            } else {
                view.getContext().startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View modal = inflater.inflate(R.layout.dialog_long_press_artist, null);
            final View getName = view;

            Button updateButton = (Button) modal.findViewById(R.id.update_button);
            Button deleteButton = (Button) modal.findViewById(R.id.delete_button);


            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(mContext)
                            .setMessage("Are you sure you want to button_update \"" + artistName.getText() + "\" ?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(mContext, "Update artist \"" + artistName.getText() + "\"", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(mContext)
                            .setMessage("Are you sure you want to button_delete \"" + artistName.getText() + "\" ?, this cannot be undone.")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int c;
                                    TextView name = (TextView) getName.findViewById(R.id.artist_name);
                                    for (c = 0; c < mArtists.size(); c++) {
                                        if (mArtists.get(c).getArtistName().equals(name.getText().toString())) {
                                            break;
                                        }
                                    }
                                    REMOVE.remove(c);

                                    new AlertDialog.Builder(mContext)
                                            .setMessage("you want to button_delete all his / her images too?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(mContext, "Delete all " + artistName.getText() + " images", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                    Toast.makeText(mContext, "Artist \"" + artistName.getText() + "\" deleted", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
            dialog = new AlertDialog.Builder(mContext)
                    .setTitle(artistName.getText() + "'s quick actions")
                    .setView(modal)
                    .show();
            return false;
        }
    }

    public class RemoveArtist {

        public void remove(int position) {
            ArtistsSQLite sql = new ArtistsSQLite(mContext);
            sql.deleteArtistByName(mArtists.get(position).getArtistName());
            mArtists.remove(position);
            notifyItemRemoved(position);
        }

        public void remove(String name) {
            int c;
            for (c = 0; c < mArtists.size(); c++) {
                if (mArtists.get(c).getArtistName().equals(name)) {
                    break;
                }
            }

            ArtistsSQLite sql = new ArtistsSQLite(mContext);
            sql.deleteArtistByName(name);
            mArtists.remove(c);
            notifyItemRemoved(c);
        }
    }
}
