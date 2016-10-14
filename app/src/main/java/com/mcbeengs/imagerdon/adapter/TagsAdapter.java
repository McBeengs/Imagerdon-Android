package com.mcbeengs.imagerdon.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mcbeengs.imagerdon.R;
import com.mcbeengs.imagerdon.database.ArtistsSQLite;

import java.util.List;

/**
 * Created by McBeengs on 02/10/2016.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private int artistId;
    private static List<String> mTags;
    private static AlertDialog dialog;
    private static Context mContext;
    public static AddTag ADD;
    public static RemoveTag REMOVE;

    public TagsAdapter(Context context, int artistId, List<String> tags) {
        mContext = context;
        this.artistId = artistId;
        mTags = tags;
        mTags.add(0, "Add new tag");
        ADD = new AddTag();
        REMOVE = new RemoveTag();
    }

    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View taskView = inflater.inflate(R.layout.activity_tag_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TagsAdapter.ViewHolder holder, int position) {
        holder.tagText.setText(mTags.get(position));

        //handle first row "add new task"
        if (mTags.get(position).equals("Add new tag")) {
            try {
                holder.tagText.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                holder.tagText.setBackgroundColor(Color.parseColor("#99FF66"));
                holder.tagText.setTextColor(Color.parseColor("#22B14C"));
            } catch (Exception ex) {

            }
        }
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView tagText;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setFocusable(false);

            tagText = (TextView) itemView.findViewById(R.id.tag_text);
        }


        @Override
        public void onClick(View view) {
            if (tagText.getText().toString().equals("Add new tag")) {
                ArtistsSQLite sql = new ArtistsSQLite(mContext);
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final List<String> list = sql.getAllTags();
                String[] tags = new String[list.size()];

                if (tags.length == 0) {
                    builder.setMessage("There are no tags registered. Please create at least one at the \"Tags\" " +
                            "section on the navigation menu.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                    sql.insertTag("Tag 1");
                    sql.insertTag("Tag 2");
                    sql.insertTag("Tag 3");
                } else {
                    builder.setTitle("Add new tag");
                    builder.setMessage("Select a tag to be linked to this artist:");

                    for (int i = 0; i < list.size(); i++) {
                        tags[i] = list.get(i);
                    }

                    final Spinner sp = new Spinner(mContext);
                    sp.setAdapter(new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_dropdown_item, tags));
                    builder.setView(sp);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TagsAdapter.ADD.add(sp.getSelectedItem().toString());
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (!tagText.getText().toString().equals("Add new tag")) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setMessage("Are you sure to remove this tag?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TagsAdapter.REMOVE.remove(tagText.getText().toString());
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                alert.show();
            }
            return false;
        }
    }

    public class AddTag {

        public void add(String tag) {
            ArtistsSQLite sql = new ArtistsSQLite(mContext);
            sql.addTagToArtist(artistId, tag);
            mTags.add(tag);
            notifyItemInserted(mTags.size() - 1);
            sql.close();
        }
    }

    public class RemoveTag {

        public void remove(String tag) {
            ArtistsSQLite sql = new ArtistsSQLite(mContext);
            sql.removeTagFromArtist(artistId, tag);

            int c;
            for (c = 0; c < mTags.size(); c++) {
                if (mTags.get(c).equals(tag)) {
                    break;
                }
            }
            mTags.remove(c);
            notifyItemRemoved(c);
            sql.close();
        }
    }
}
