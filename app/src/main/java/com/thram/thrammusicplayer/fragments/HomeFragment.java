package com.thram.thrammusicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.GridView;

import com.thram.thrammusicplayer.App;
import com.thram.thrammusicplayer.R;
import com.thram.thrammusicplayer.activities.ThramMusicPlayerActivity;
import com.thram.thrammusicplayer.adapters.CardGridViewAdapter;
import com.thram.thrammusicplayer.model.Card;
import com.thram.thrammusicplayer.utils.AnimationFactory;
import com.thram.thrammusicplayer.utils.Animations;

import java.util.ArrayList;

/**
 * Created by thram on 8/03/15.
 */
public class HomeFragment extends Fragment {
    public static int animationsStarted = 0;
    public static int animationsEnded = 0;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Home");
        ((ThramMusicPlayerActivity) getActivity()).setWindowsBackground(null);
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        GridView grid = (GridView) rootView.findViewById(R.id.grid);
        grid.setColumnWidth(App.Display.width / 2);
        final ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("player", getResources().getString(R.string.player_card_title), getResources().getString(R.string.player_card_description), getResources().getColor(R.color.player_color), R.drawable.ic_player_card, R.drawable.ic_player_card_back));
        cards.add(new Card("library", getResources().getString(R.string.library_card_title), getResources().getString(R.string.library_card_description), getResources().getColor(R.color.library_color), R.drawable.ic_library_card, R.drawable.ic_library_card_back));
        cards.add(new Card("news", getResources().getString(R.string.news_card_title), getResources().getString(R.string.news_card_description), getResources().getColor(R.color.news_color), R.drawable.ic_news_card, R.drawable.ic_news_card_back));
        cards.add(new Card("playlist", getResources().getString(R.string.playlist_card_title), getResources().getString(R.string.playlist_card_description), getResources().getColor(R.color.playlist_color), R.drawable.ic_playlist_card, R.drawable.ic_playlist_card_back));
        cards.add(new Card("lyrics", getResources().getString(R.string.lyrics_card_title), getResources().getString(R.string.lyrics_card_description), getResources().getColor(R.color.lyrics_color), R.drawable.ic_lyrics_card, R.drawable.ic_lyrics_card_back));
        cards.add(new Card("settings", getResources().getString(R.string.settings_card_title), getResources().getString(R.string.settings_card_description), getResources().getColor(R.color.settings_color), R.drawable.ic_settings_card, R.drawable.ic_settings_card_back));
        CardGridViewAdapter adapter = new CardGridViewAdapter(getActivity(), cards);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                animationsStarted = 0;
                animationsEnded = 0;
                Animations.flip(view, AnimationFactory.FlipDirection.LEFT_RIGHT);
                view.setBackgroundColor(getResources().getColor(R.color.dark_overlay));
                final Card card = cards.get(position);
                getActivity().setTitle(card.title);
                ((ThramMusicPlayerActivity) getActivity()).setWindowsBackground(card.color);
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++) {
                    final View childAt = parent.getChildAt(i);
                    if (!childAt.equals(view)) {
                        int start = i * 100;
                        AnimationSet animation = Animations.leave(childAt, 200, start, AnimationFactory.LeaveDirection.BOTTOM);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                animationsStarted++;
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                animationsEnded++;
                                childAt.setVisibility(View.GONE);
                                if (animationsStarted == animationsEnded) {
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.setCustomAnimations(R.anim.fade_in,
                                            R.anim.fade_out);
                                    switch (card.tag) {
                                        case "player":
                                            ft.replace(R.id.container, PlayerFragment.newInstance(null), "Player Fragment");
                                            break;
                                        case "library":
                                            ft.replace(R.id.container, LibraryFragment.newInstance(), "Library Fragment");
                                            break;
                                        case "playlist":
                                            ft.replace(R.id.container, PlaylistFragment.newInstance(), "Playlist Fragment");
                                            break;
                                        case "news":
                                            ft.replace(R.id.container, NewsFragment.newInstance(), "News Fragment");
                                            break;
                                        case "lyrics":
                                            ft.replace(R.id.container, LyricsFragment.newInstance(), "Lyrics Fragment");
                                            break;
                                        case "settings":
                                            ft.replace(R.id.container, SettingsFragment.newInstance(), "Settings Fragment");
                                            break;
                                        default:
                                            ft.replace(R.id.container, HomeFragment.newInstance(), "Home Fragment");

                                    }
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
            }
        });
        return rootView;
    }


}
