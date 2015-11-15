package com.example.bremme.eva_projectg6;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bremme.eva_projectg6.Repository.RestApiRepository;
import com.example.bremme.eva_projectg6.domein.Challenge;
import com.example.bremme.eva_projectg6.domein.UserLocalStore;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.apache.commons.httpclient.URIUtil;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * Created by brechttanghe on 4/11/15.
 */
public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder>{

    private List<Challenge> challengeDataSet;
    private Drawable dImages[];
    private UserLocalStore userLocalStore;
    private RestApiRepository repo;
    private Context context;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
    public ChallengeAdapter(final List<Challenge> challengeDataSet,Context context){
        this.context = context;
        userLocalStore = new UserLocalStore(context);
        repo = new RestApiRepository();
        this.challengeDataSet = challengeDataSet;
        dImages = new Drawable[challengeDataSet.size()];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < challengeDataSet.size(); i++) {
                        dImages[i] = ChooseChallenge.loadImageFromWebOperations(challengeDataSet.get(i).getUrl().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simplerow, parent ,false);
        ViewHolder vh = new ViewHolder(v);
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
       shareDialog = new ShareDialog((Activity) context);
        // this part is optional
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final ImageView image = (ImageView) holder.view.findViewById(R.id.challengeImage);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        image.setImageDrawable(loadImageFromWebOperations(challengeDataSet.get(position).getUrl().toString()));

                } catch (Exception e) {
                    Log.i("challengeadapptr", "loaden image mislukt");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
       // image.setImageResource(R.drawable.test);
        TextView description = (TextView) holder.view.findViewById(R.id.challengeDescription);
        description.setText(challengeDataSet.get(position).getDescription());
        TextView title = (TextView) holder.view.findViewById(R.id.challengeTitle);
        title.setText(challengeDataSet.get(position).getName());
        Button button = (Button) holder.view.findViewById(R.id.buttonDone);
        button.setText("Challenge voltooid");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareOnFacebook(convertToBitmap(dImages[0],300,300)); //was om te testen
            }
        });
    }
    @Override
    public int getItemCount() {
        return challengeDataSet.size();
    }

    public static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    private void completeCurrentChallenge()
    {
        Ion.with(context)
                .load(repo.getCURRENTCHALLENGE()).setHeader("username",userLocalStore.getUsername())
                .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if(result.equals("is gelukt"))
                {
                    //melding alst gelukt is
                }else
                {
                    //code alst mislukt is
                }
            }
        });
    }

    private void shareOnFacebook(Bitmap b)
    {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com")).setContentTitle("Eva")
                .build();
        //SharePhoto photo = new SharePhoto.Builder().setBitmap(b).build();
        //SharePhotoContent content = new SharePhotoContent.Builder()
          //      .addPhoto(photo)
            //    .build();
        //todo betere content insteken
        ShareApi.share(content,null);

    }
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
}
