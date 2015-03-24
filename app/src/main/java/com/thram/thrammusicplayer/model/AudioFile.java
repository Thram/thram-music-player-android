package com.thram.thrammusicplayer.model;

/**
 * Created by thram on 10/03/15.
 */
public class AudioFile {

    private final int id;
    public String fileUri;
    public String fileName;
    public String folder;
    public String size;
    public String bitrate;
    public String displayName;
    public ID3Tags id3Tags;
    public boolean shown = false;

    public AudioFile(int id, String fileUri, String sizeBytes, String displayName, String title, String album, String artist, String durationMilliseconds) {
        this.id = id;
        this.fileUri = fileUri;
        float sizeMbytes = Integer.parseInt(sizeBytes) / 1024 / 1024;
        this.size = String.format("%.2fMb", sizeMbytes);
        String[] fileUriArray = fileUri.split("/");
        this.fileName = fileUriArray[fileUriArray.length - 1];
        this.folder = fileUriArray[fileUriArray.length - 2];
        // CALCULATE BITRATE size / duration (in secs)
        //        File Size: 10.3MB (87013064 bits)
        //                           4089607
        //        Length: 5:16 (316 Seconds)
        //        Which gives: 87013064 bits / 316 seconds = 273426.147 bits/sec or ~273kbps
        //        Actual Bitrate: 259kbps
        Integer duration = Integer.parseInt(durationMilliseconds);
        float seconds = duration / 1000;
        int sizeBits = Integer.parseInt(sizeBytes) * 8;
        int bps = Math.round(sizeBits / seconds / 1024);
        this.bitrate = String.format("%dkbps", bps);
        this.displayName = displayName;
        this.id3Tags = new ID3Tags();
        this.id3Tags.title = title;
        this.id3Tags.album = album;
        this.id3Tags.artists = artist;
        int min = (int) (seconds / 60);
        int sec = (int) (seconds - (min * 60));
        this.id3Tags.length = min + ":" + String.format("%02d", sec);
    }
}
