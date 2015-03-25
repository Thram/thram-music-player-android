package com.thram.thrammusicplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.thram.thrammusicplayer.App;
import com.thram.thrammusicplayer.R;
import com.thram.thrammusicplayer.model.AudioFile;

import java.util.List;

/**
 * Created by thram on 10/03/15.
 */
public class ListFilesAdapter extends ArrayAdapter<AudioFile> {
    public boolean fileViewMode = false;
    private final Context context;
    private final List<AudioFile> audioFileList;

    public ListFilesAdapter(Context context, List<AudioFile> audioFileList) {
        super(context, R.layout.file_item, audioFileList);
        this.context = context;
        this.audioFileList = audioFileList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AudioFile audioFile = audioFileList.get(position);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        ViewFlipper cell = (ViewFlipper) inflater.inflate(R.layout.file_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.title = (TextView) cell.findViewById(R.id.title);
        holder.title.setText(audioFile.id3Tags.title);
        holder.title.setWidth((int) Math.floor(App.Display.width * .75));
        holder.duration = (TextView) cell.findViewById(R.id.duration);
        holder.duration.setText(audioFile.id3Tags.length);
        holder.duration.setWidth((int) Math.floor(App.Display.width * .25));
        holder.album = (TextView) cell.findViewById(R.id.album);
        holder.album.setText(audioFile.id3Tags.album);
        holder.album.setWidth((int) Math.floor(App.Display.width * .5));
        holder.artist = (TextView) cell.findViewById(R.id.artist);
        holder.artist.setText(audioFile.id3Tags.artists);
        holder.artist.setWidth((int) Math.floor(App.Display.width * .5));
        holder.fileName = (TextView) cell.findViewById(R.id.file_name);
        holder.fileName.setText(audioFile.fileName);
        holder.fileName.setWidth((int) Math.floor(App.Display.width * .75));
        holder.size = (TextView) cell.findViewById(R.id.size);
        holder.size.setText(audioFile.size);
        holder.size.setWidth((int) Math.floor(App.Display.width * .25));
        holder.folder = (TextView) cell.findViewById(R.id.folder);
        holder.folder.setText(audioFile.folder);
        holder.folder.setWidth((int) Math.floor(App.Display.width * .75));
        holder.bitrate = (TextView) cell.findViewById(R.id.bitrate);
        holder.bitrate.setText(audioFile.bitrate);
        holder.bitrate.setWidth((int) Math.floor(App.Display.width * .25));
        if (fileViewMode) {
            cell.showNext();
        }
        return cell;
    }

    static class ViewHolder {
        TextView title;
        TextView duration;
        TextView album;
        TextView artist;
        TextView fileName;
        TextView folder;
        TextView size;
        TextView bitrate;
    }
}
