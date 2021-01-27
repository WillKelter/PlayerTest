package com.example.playertest;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;


public class Track {
    private String url;
    private String artist;
    private String title;
    private Bitmap albumCover;
    private MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    Context context;

    public String getUrl() {
        return url;
    }

    public String getArtist() {
        artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
       if (artist == null) {artist = "Unknown artist";}
        return artist;
    }

    public String getTitle() {
       title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if (title == null) {title = "Unnamed track";}
        return title;
    }

    public Bitmap getAlbumCover() {
        byte [] data = retriever.getEmbeddedPicture();
        if(data != null)
        {
            albumCover = BitmapFactory.decodeByteArray(data, 0, data.length);

        } else {albumCover = BitmapFactory.decodeResource(context.getApplicationContext().getResources() ,R.drawable.albumcoverplaceholder); }
        return albumCover;
    }



    public Track(Context context, String url) {
        this.url = url;
        this.context = context;
     retriever.setDataSource(context.getApplicationContext(), Uri.parse(this.getUrl()));
    }


}
