package com.thram.thrammusicplayer.model;

/**
 * Created by thram on 10/03/15.
 */
public class AudioFile {

    private final int id;
    public String fileUri;
    public String displayName;
    public ID3Tags id3Tags;

    public AudioFile(int id, String fileUri, String displayName, String title, String album, String artist, String duration) {
        this.id = id;
        this.fileUri = fileUri;
        this.displayName = displayName;
        this.id3Tags = new ID3Tags(title, album, artist, duration);
    }
}
