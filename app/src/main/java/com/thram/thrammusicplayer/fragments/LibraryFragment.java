package com.thram.thrammusicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.thram.thrammusicplayer.utils.FileManagerTools;

import java.util.List;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageState;

/**
 * Created by thram on 8/03/15.
 */
public class LibraryFragment extends Fragment {

    public static String TAG = "LibraryFragment";
    private ListView fileList;
    private ListFilesAdapter adapter;
    private List<AudioFile> audioFiles;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ThramMusicPlayerActivity activity = (ThramMusicPlayerActivity) getActivity();
        activity.setupToolbar(getResources().getString(R.string.library_fragment_title), getResources().getString(R.string.library_fragment_subtitle));
        setHasOptionsMenu(true);
        activity.setWindowsBackground(getResources().getColor(R.color.library_color));
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        //getting SDcard root path
        final String state = getExternalStorageState();
        if (MEDIA_MOUNTED.equals(state)) {
            //Some audio may be explicitly marked as not being music
            audioFiles = FileManagerTools.listMediaFiles();
            adapter = new ListFilesAdapter(activity, audioFiles);
            fileList = ((ListView) rootView.findViewById(R.id.file_list));
            fileList.setAdapter(adapter);
            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final int[] animationsStarted = {0};
                    final int[] animationsEnded = {0};

                    final AudioFile audioFile = audioFiles.get(position);
                    int count = parent.getLastVisiblePosition();
                    for (int i = 0; i < count; i++) {
                        final View childAt = parent.getChildAt(i);
                        if (childAt != null && childAt.getVisibility() == View.VISIBLE) {
                            int start = i * 100;
                            AnimationSet animation = Animations.leave(childAt, 200, start, AnimationFactory.TranslateDirection.RIGHT);
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
                                        activity.changeFragment(PlayerFragment.newInstance(audioFile), "Player Fragment");
                                    }
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }

                    }
                }
            });
        } else {
            Log.d("SD CARD", "Not mounted");
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.library, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order:
                break;
            case R.id.view:
                changeView();
                adapter.fileViewMode = !adapter.fileViewMode;
                if (adapter.fileViewMode)
                    item.setIcon(R.drawable.ic_view_file_data);
                else
                    item.setIcon(R.drawable.ic_view_music_data);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeView() {
        int count = fileList.getCount();
        for (int i = 0; i < count; i++) {
            final View childAt = fileList.getChildAt(i);
            if (childAt != null && childAt.getVisibility() == View.VISIBLE)
                Animations.flip(childAt, AnimationFactory.FlipDirection.TOP_BOTTOM);
        }
    }
}
