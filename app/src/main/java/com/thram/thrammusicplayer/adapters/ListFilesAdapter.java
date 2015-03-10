package com.thram.thrammusicplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.thram.thrammusicplayer.R;
import com.thram.thrammusicplayer.model.AudioFile;
import com.thram.thrammusicplayer.utils.Animations;

import java.util.List;

/**
 * Created by thram on 10/03/15.
 */
public class ListFilesAdapter extends ArrayAdapter<AudioFile> {
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
        holder.displayName = (TextView) cell.findViewById(R.id.file_display_name);
        holder.displayName.setText(audioFile.displayName);
        int start = 100 + position * 100;
        Animations.enter(cell, 200, start);
        return cell;
    }


    static class ViewHolder {
        TextView displayName;
    }
}
