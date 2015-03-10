package com.thram.thrammusicplayer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ListView;

import com.thram.thrammusicplayer.R;
import com.thram.thrammusicplayer.activities.ThramMusicPlayerActivity;
import com.thram.thrammusicplayer.adapters.ListFilesAdapter;
import com.thram.thrammusicplayer.model.AudioFile;
import com.thram.thrammusicplayer.utils.AnimationFactory;
import com.thram.thrammusicplayer.utils.Animations;

import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageState;

/**
 * Created by thram on 8/03/15.
 */
public class LibraryFragment extends Fragment {

    public final String DEFAULT_LIBRARY_PATH = "Music";

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ThramMusicPlayerActivity activity = (ThramMusicPlayerActivity) getActivity();
        activity.setWindowsBackground(getResources().getColor(R.color.library_color));
        activity.setTitle("Library");
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        //getting SDcard root path
        final String state = getExternalStorageState();
        if (MEDIA_MOUNTED.equals(state)) {
            //Some audio may be explicitly marked as not being music
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
            String[] projection = {
                    MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.YEAR,
                    MediaStore.Audio.Media.COMPOSER, MediaStore.Audio.Media.TRACK
            };
            Cursor cursor = activity.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection, selection, null, null);

            final List<AudioFile> audioFiles = new ArrayList<>();
            while (cursor.moveToNext()) {
                audioFiles.add(new AudioFile(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
            }
            cursor.close();
            ListFilesAdapter adapter = new ListFilesAdapter(getActivity(), audioFiles);
            ListView fileList = ((ListView) rootView.findViewById(R.id.file_list));
            fileList.setAdapter(adapter);
            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final int[] animationsStarted = {0};
                    final int[] animationsEnded = {0};
                    final AudioFile audioFile = audioFiles.get(position);
                    getActivity().setTitle(audioFile.id3Tags.title);
                    Animations.flip(view, AnimationFactory.FlipDirection.TOP_BOTTOM);
                    int count = parent.getChildCount();
                    for (int i = 0; i < count; i++) {
                        final View childAt = parent.getChildAt(i);
                        int start = i * 100;
                        AnimationSet animation = Animations.leave(childAt, 200, start, AnimationFactory.LeaveDirection.RIGHT);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                animationsStarted[0]++;
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                animationsEnded[0]++;
                                childAt.setVisibility(View.GONE);
                                if (animationsStarted[0] == animationsEnded[0]) {
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                                    ft.replace(R.id.container, PlayerFragment.newInstance(audioFile.fileUri), "Player Fragment");
                                    ft.addToBackStack(null);
                                    ft.commit();
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                }
            });
        } else {
            Log.d("SD CARD", "Not mounted");
        }

        return rootView;
    }
}
