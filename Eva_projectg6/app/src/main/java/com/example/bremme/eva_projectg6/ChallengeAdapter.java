package com.example.bremme.eva_projectg6;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bremme.eva_projectg6.R;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brechttanghe on 4/11/15.
 */
public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder>{

    private List<Challenge> challengeDataSet;
    private Drawable dImages[];
    private UserLocalStore userLocalStore;
    private Toolbar toolbar;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public ChallengeAdapter(List<Challenge> challengeDataSet){this.challengeDataSet = challengeDataSet;}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simplerow, parent ,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView title = (TextView) holder.view.findViewById(R.id.challengeTitle);
        ImageView image = (ImageView) holder.view.findViewById(R.id.challengeImage);
        TextView description = (TextView) holder.view.findViewById(R.id.challengeDescription);

        title.setText(challengeDataSet.get(position).getName());
        image.setImageResource(R.drawable.test);
        description.setText(challengeDataSet.get(position).getDescription());


    }

    @Override
    public int getItemCount() {
        return challengeDataSet.size();
    }


}
