package com.example.bremme.eva_projectg6;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIUtil;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by brechttanghe on 4/11/15.
 */
public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder>{

    private List<Challenge> challengeDataSet;
    private UserLocalStore userLocalStore;
    private RestApiRepository repo;
    private Context context;
    private LinearLayout linearLayout;
    private RatingBar rating;
    private ImageView image;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
    public ChallengeAdapter(final List<Challenge> challengeDataSet,Context context,CallbackManager cb, ShareDialog s){
        callbackManager = cb;
        shareDialog = s;
        this.context = context;
        userLocalStore = new UserLocalStore(context);
        repo = new RestApiRepository();
        this.challengeDataSet = challengeDataSet;
        Log.i("DAtaset init" , challengeDataSet.size()+" ic " + this.challengeDataSet.size() );
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simplerow, parent ,false);
        ViewHolder vh = new ViewHolder(v);
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        parent.findViewById(R.id.challengeImage);
        // this part is optional
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ImageView image = (ImageView) holder.view.findViewById(R.id.challengeImage);
        Picasso.with(context).load(challengeDataSet.get(position).getUrlImage()).into(image);
        TextView description = (TextView) holder.view.findViewById(R.id.challengeDescription);
        description.setText(challengeDataSet.get(position).getDescription());
        TextView title = (TextView) holder.view.findViewById(R.id.challengeTitle);
        title.setText(challengeDataSet.get(position).getName());
        Button button = (Button) holder.view.findViewById(R.id.buttonDone);
        button.setText("Voltooi challenge");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("button", "THE FACKING SIZE OF THE DATASET " + challengeDataSet.size());
                if (challengeDataSet.size() < 21) {
                    showCompleteDialog(challengeDataSet.get(0));
                } else {
                    showCompleteChallengeSetDialog();
                }

            }
        });
        if(position!=0){
            ImageView completedImage = (ImageView) holder.view.findViewById(R.id.CompletedImage);
            completedImage.setImageResource(R.drawable.completedstamp);
            completedImage.setAlpha(155);
            //setLocked(image);
        }else{
            button.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return challengeDataSet.size();
    }
    private void completeCurrentChallenge()
    {
        //todo fixen
        Ion.with(context)
                .load(repo.getCOMPLETECHALLENGE())
                .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .setBodyParameter("username",userLocalStore.getUsername())
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {


            }
        });
    }

    private void shareChallengeOnFacebook()
    {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://groep6webapp.herokuapp.com/#/home"))
                    .setContentDescription(context.getResources().getString(R.string.challengeDone))
                    .build();
            shareDialog.show(linkContent);

        }

    }
    private void shareChallengeSetFacebook() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://groep6webapp.herokuapp.com/#/home"))
                    .setContentDescription(context.getResources().getString(R.string.challengeSetDone))
                    .build();
            shareDialog.show(linkContent);
        }

    }
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);
        return mutableBitmap;
    }
    public static void setLocked(ImageView v)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        //v.setAlpha(128);   // 128 = 0.5
    }
    private void showCompleteDialog(final Challenge challenge) {
        Log.i("selected Challenge", challenge.getName());
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context).setTitle(challenge.getName())
                    .setPositiveButton(context.getResources().getString(R.string.posButtonDialog), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            completeCurrentChallenge();
                            shareChallengeOnFacebook();//also redirects to choosechallenge
                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.negButtonDialog), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            completeCurrentChallenge();
                            setRating();
                            goToChooseChallenge();
                        }
                    });
            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = LayoutInflater.from(this.context);
            View dialogLayout = inflater.inflate(R.layout.challengedialog, null);
            linearLayout = (LinearLayout) dialogLayout.findViewById(R.id.challengeLayout);
            ImageView image = new ImageView(this.context);
            Picasso.with(context).load(challengeDataSet.get(0).getUrlImage()).into(image);
            linearLayout.addView(image);
            rating = new RatingBar(context);
            rating.setTag("ratingbar");
            rating.setRating(5);
            rating.setNumStars(5);
            rating.setPadding(0,0,0,0);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rating.setLayoutParams(param);
            TextView text = new TextView(context);
            text.setText(R.string.rate);
            text.setPadding(60, 0, 0, 0);
            linearLayout.addView(text);
            linearLayout.addView(rating);
            dialog.setView(dialogLayout);
            dialog.show();
        }
        catch (Exception e) {
            Log.i("Messaerror","an error occured");
        }
    }
    private void showCompleteChallengeSetDialog()
    {
        try {
            AlertDialog.Builder builder =new AlertDialog.Builder(this.context)
                    .setTitle(context.getResources().getString((R.string.challengeSetDialogTitle)))
                    .setPositiveButton(context.getResources().getString(R.string.posButtonDialog), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            shareChallengeSetFacebook();
                            completeChallengeSeries();
                            setRating();

                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.negButtonDialog), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            completeChallengeSeries();
                            setRating();
                            goToStartSeries();

                        }
                    });
            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = LayoutInflater.from(this.context);
            View dialogLayout = inflater.inflate(R.layout.challengedialog, null);
            linearLayout = (LinearLayout) dialogLayout.findViewById(R.id.challengeLayout);
            ImageView image = new ImageView(this.context);
            Picasso.with(context).load(challengeDataSet.get(0).getUrlImage()).into(image);
            linearLayout.addView(image);
            rating  = new RatingBar(context);
            rating.setTag("ratingbar");
            rating.setRating(5);
            rating.setNumStars(5);
            LinearLayout.LayoutParams param= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rating.setLayoutParams(param);
            rating.setPadding(0, 0, 0, 0);
            TextView text = new TextView(context);
            text.setText(R.string.rate);
            text.setPadding(60,0,0,0);
            linearLayout.addView(text);
            linearLayout.addView(rating);
            dialog.setView(dialogLayout);
            dialog.show();
        } catch (Exception e) {
        }
    }

    private void goToStartSeries() {
        context.startActivity(new Intent(context,StartChallengeSet.class));
    }

    public Drawable scaleImage(Drawable image)
    {
        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 600, 350, false);
        image = new BitmapDrawable(context.getResources(), bitmapResized);
        return image;
    }
    private void goToChooseChallenge()
    {
        Intent i = new Intent(context,ChooseChallenge.class);
        context.startActivity(i);
    }
    private int getStarScore()
    {
        return rating.getNumStars();
    }
    private void setRating()
    {
        Log.i("userId",userLocalStore.getUserId());
        Ion.with(context)
                .load(repo.getSETSCORERATING())
                .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .setBodyParameter("user",userLocalStore.getUserId())
                .setBodyParameter("challenge",challengeDataSet.get(0).getId())
                .setBodyParameter("score", getStarScore() + "")
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {

            }
        });
    }
    private void completeChallengeSeries()
    {
        Ion.with(context)
                .load(repo.getCOMPLETECHALLENGESERIES())
                .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .setBodyParameter("username", userLocalStore.getUsername())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    }
                });
    }
    public static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.i("loading image ","is mislukt ");
            e.printStackTrace();
            return null;
        }
    }

}
