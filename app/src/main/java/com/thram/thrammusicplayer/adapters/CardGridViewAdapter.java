package com.thram.thrammusicplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.thram.thrammusicplayer.App;
import com.thram.thrammusicplayer.R;
import com.thram.thrammusicplayer.model.Card;
import com.thram.thrammusicplayer.utils.Animations;

import java.util.List;

/**
 * Created by thram on 8/03/15.
 */
public class CardGridViewAdapter extends ArrayAdapter<Card> {
    private final Context context;
    private final List<Card> cards;

    public CardGridViewAdapter(Context context, List<Card> cards) {
        super(context, R.layout.card_view, cards);
        this.context = context;
        this.cards = cards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Card card = cards.get(position);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        ViewFlipper cell = (ViewFlipper) inflater.inflate(R.layout.card_view, parent, false);
        ViewGroup.LayoutParams params = cell.getLayoutParams();
        params.height = App.Display.width / 2;
        params.width = App.Display.width / 2;
        cell.setLayoutParams(params);
        cell.findViewById(R.id.front).setBackgroundColor(card.color);
        cell.findViewById(R.id.back).setBackgroundColor(card.color);

        ViewHolder holder = new ViewHolder();
        holder.title = (TextView) cell.findViewById(R.id.title);
        holder.title.setText(card.title);
        holder.description = (TextView) cell.findViewById(R.id.description);
        holder.description.setText(card.description);
        if (card.imagePath != null) {
            holder.image = (ImageView) cell.findViewById(R.id.image);
//            Use Picasso to set image
        }

        int start = 100 + position * 100;
        Animations.enter(cell, 200, start);
        return cell;
    }


    static class ViewHolder {
        TextView title;
        TextView description;
        ImageView image;
    }
}
