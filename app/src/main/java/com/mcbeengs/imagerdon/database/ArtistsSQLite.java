package com.mcbeengs.imagerdon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.mcbeengs.imagerdon.adapter.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by McBeengs on 09/10/2016.
 */

public class ArtistsSQLite extends SQLiteOpenHelper {

    private static final String DB_NAME = "artists_sqlite.db";
    private static final int VERSION = 1;
    private Context mContext;

    public ArtistsSQLite(Context context) {
        super(context, DB_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql;

        sql = "CREATE TABLE IF NOT EXISTS `artist` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `server` INT NOT NULL," +
                "`name` VARCHAR(100) NOT NULL , `avatar_url` VARCHAR(150) NOT NULL , `first_downloaded` LONG NOT NULL ," +
                "`last_updated` LONG NOT NULL , `image_count` INT NOT NULL, `scrap_count` INT NOT NULL)";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS `tag` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tag` VARCHAR(100) NOT NULL );";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS `inner_tag` ( `artist_id` INT NOT NULL , `tag_id` INT NOT NULL );";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS `info` ( `artist_id` INT NOT NULL, `description` TEXT(255), `fav` BOOLEAN NOT NULL );";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i1 == 2) {
            // do whatever da fuck i need
        }
    }

    public boolean insertArtist(Artist artist) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put("server", artist.getServerId());
        values.put("name", artist.getArtistName());
        values.put("avatar_url", artist.getIconUrl());
        values.put("first_downloaded", artist.getFirstDownloaded());
        values.put("last_updated", artist.getLastUpdated());
        values.put("image_count", artist.getImageCount());
        values.put("scrap_count", artist.getScrapsCount());

        db.insert("artist", null, values);

        values = new ContentValues();
        values.put("artist_id", getArtistId(artist.getArtistName(), artist.getServerId()));
        values.put("description", "Insert an description here...");
        values.put("fav", false);

        db.insert("info", null, values);
        return true;
    }

    public boolean deleteArtistByName(String name) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("artist", "name = ?", new String[]{name}) > 0;
    }

    public Artist getArtistById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("artist", null, "id = '" + id + "'", null, null, null, null, null);

        if (cursor.moveToFirst()) {
            return parseCursor(cursor);
        }

        return null;
    }

    public List<Artist> getArtistsByMatch(String string) {
        SQLiteDatabase db = getReadableDatabase();
        List<Artist> list = new ArrayList<>();

        Cursor cursor = db.query("artist", null, "name LIKE '%" + string + "%'", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(parseCursor(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Artist> getArtistsByServer(int server) {
        SQLiteDatabase db = getReadableDatabase();
        List<Artist> list = new ArrayList<>();

        Cursor cursor = db.query("artist", null, "server = '" + server + "'", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(parseCursor(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Artist> getFavedArtists() {
        SQLiteDatabase db = getReadableDatabase();
        List<Artist> list = new ArrayList<>();

        Cursor cursor = db.query("info", null, "fav = '1'", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(getArtistById(cursor.getInt(cursor.getColumnIndex("artist_id"))));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Artist> getAllArtists() {
        List<Artist> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] fields = new String[]{"id", "server", "name", "avatar_url", "first_downloaded", "last_updated",
                "image_count", "scrap_count"};

        Cursor cursor = db.query("artist", fields, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(parseCursor(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public boolean artistExistsOnDB(String name, int server) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("artist", null, "name = '" + name + "' AND server = '" + server + "'", null, null, null, null, null);
        return cursor.moveToFirst();
    }

    public boolean setArtistFaved(Artist artist) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put("fav", 1);

        return db.update("info", values, "artist_id = '" + getArtistId(artist.getArtistName(), artist.getServerId()) + "'", null) > 0;
    }

    public boolean setArtistUnfaved(Artist artist) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put("fav", 0);

        return db.update("info", values, "artist_id = '" + getArtistId(artist.getArtistName(), artist.getServerId()) + "'", null) > 0;
    }

    public boolean isArtistFaved(Artist artist) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("info", null, "artist_id = '" + getArtistId(artist.getArtistName(), artist.getServerId()) + "'", null, null, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getShort(cursor.getColumnIndex("fav")) > 0;
        } else {
            return false;
        }
    }

    public String getDescription(Artist artist) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("info", null, "artist_id = '" + getArtistId(artist.getArtistName(), artist.getServerId())
                + "'", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("description"));
        } else {
            return "deu ruim";
        }
    }

    public void setDescription(String description, Artist artist) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        values.put("description", description);

        db.update("info", values, "artist_id = '" + getArtistId(artist.getArtistName(), artist.getServerId()) + "'", null);
    }

    public boolean insertTag(String tag) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query("tag", null, "tag = '" + tag + "'", null, null, null, null, null);
        if (!cursor.moveToNext()) {
            values.put("tag", tag);
            return db.insert("tag", null, values) > 0;
        } else {
            return false;
        }
    }

    public boolean addTagToArtist(int artistId, String tag) {
        SQLiteDatabase db = getWritableDatabase();
        int tagId = getTagId(tag);
        Cursor cursor = db.query("inner_tag", null, "artist_id = '" + artistId + "' AND tag_id = '" + tagId + "'", null, null, null, null, null);

        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            db = getWritableDatabase();

            values.put("artist_id", artistId);
            values.put("tag_id", tagId);

            return db.insert("inner_tag", null, values) > 0;
        } else {
            return false;
        }
    }

    public List<String> getTagsFromArtist(int artistId) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("inner_tag", null, "artist_id = '" + artistId + "'", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Cursor c = db.query("tag", null, "id = '" + cursor.getString(cursor.getColumnIndex("tag_id")) + "'", null, null, null, null, null);
                if (c.moveToFirst()) {
                    do {
                        list.add(c.getString(c.getColumnIndex("tag")));
                    } while (c.moveToNext());
                }
            } while (cursor.moveToNext());
        }
        return list;
    }

    public boolean removeTagFromArtist(int artistId, String tag) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("inner_tag", "tag_id = ? AND artist_id = ?", new String[]{Integer.toString(getTagId(tag)), Integer.toString(artistId)});
        return true;
    }

    public boolean deleteTag(String tag) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.delete("tag", "tag = ?", new String[]{tag});
            db.delete("inner_tag", "tag_id = ?", new String[]{Integer.toString(getTagId(tag))});
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<String> getAllTags() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] fields = new String[]{"id", "tag"};

        Cursor cursor = db.query("tag", fields, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("tag")));
            } while (cursor.moveToNext());
        }
        return list;
    }

    private int getArtistId(String artist, int server) {
        SQLiteDatabase db = getReadableDatabase();
        String[] fields = new String[]{"id", "name", "server"};

        Cursor cursor = db.query("artist", fields, "name = '" + artist + "' AND server = '" + server + "'", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex("name")).equals(artist)) {
                    return cursor.getInt(cursor.getColumnIndex("id"));
                }
            } while (cursor.moveToNext());
        }

        return -1;
    }

    private int getTagId(String tag) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] fields = new String[]{"id", "tag"};

        Cursor cursor = db.query("tag", fields, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex("tag")).equals(tag)) {
                    return cursor.getInt(cursor.getColumnIndex("id"));
                }
            } while (cursor.moveToNext());
        }

        return -1;
    }

    private Artist parseCursor(Cursor cursor) {
        Artist artist = new Artist();
        artist.setId(cursor.getInt(cursor.getColumnIndex("id")));
        artist.setServerId(cursor.getInt(cursor.getColumnIndex("server")));
        switch (artist.getServerId()) {
            case 0:
                artist.setServerName("DeviantArt");
                break;
            case 1:
                artist.setServerName("Tumblr");
                break;
            case 2:
                artist.setServerName("FurAffinity");
                break;
            case 3:
                artist.setServerName("Pixiv");
                break;
            case 4:
                artist.setServerName("e621");
                break;
        }
        artist.setArtistName(cursor.getString(cursor.getColumnIndex("name")));
        artist.setIconUrl(cursor.getString(cursor.getColumnIndex("avatar_url")));
        artist.setFirstDownloaded(cursor.getLong(cursor.getColumnIndex("first_downloaded")));
        artist.setLastUpdated(cursor.getLong(cursor.getColumnIndex("last_updated")));
        artist.setImageCount(cursor.getInt(cursor.getColumnIndex("image_count")));
        artist.setScrapsCount(cursor.getInt(cursor.getColumnIndex("scrap_count")));

        return artist;
    }
}
