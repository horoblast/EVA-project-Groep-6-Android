package com.example.bremme.eva_projectg6;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
    private LinearLayout linearLayout;
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
        button.setText("Voltooi challenge");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompleteDialog(challengeDataSet.get(0));
               // shareOnFacebook(convertToBitmap(dImages[0], 300, 300)); //was om te testen
            }
        });
        ImageView completedImage = (ImageView) holder.view.findViewById(R.id.CompletedImage);
        if(challengeDataSet.size() - 1 > position){
            completedImage.setImageResource(R.drawable.completedstamp);
            completedImage.setAlpha(155);
            button.setVisibility(View.GONE);
            //setLocked(image);
        }else{
        }


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
        //todo fixen
        Ion.with(context)
                .load(repo.getCOMPLETECHALLENGE()).setHeader("username",userLocalStore.getUsername())
                .setHeader("Authorization", "Bearer " + userLocalStore.getToken())
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (result.equals("is gelukt")) {
                    //melding alst gelukt is
                    Log.i("Me","complete gelukt");
                } else {
                    //code alst mislukt is
                    Log.i("Me","complete mislukt");
                }
            }
        });
    }

    private void shareOnFacebook(String url,String text)
    {
        Log.i("Me","Sharing on facebook "+url);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://groep6webapp.herokuapp.com/#/home"))
                .setContentTitle(text)
                .setContentDescription("I just finished a challenge!")
                .setImageUrl(Uri.parse(url))
                .build();
        //todo betere content insteken
        ShareApi.share(content, null);

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
    private void showCompleteDialog(final Challenge challenge)
    {
        Log.i("selected Challenge", challenge.getName());
        try {
            AlertDialog.Builder builder =new AlertDialog.Builder(this.context)
                    .setTitle(challenge.getName()).setIcon(dImages[0])
                    .setPositiveButton("Voltooi en deel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TextView textView = (TextView) linearLayout.findViewWithTag("challengetext");
                            if(textView.getText().toString().length()==0)
                            shareOnFacebook(challenge.getUrl().toString(),challenge.getName());
                            else
                                shareOnFacebook(challenge.getUrl().toString(),textView.getText().toString());
                            completeCurrentChallenge();
                        }
                    })
                    .setNegativeButton("Voltooi", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!  lal
                            completeCurrentChallenge();
                        }
                    });
            final AlertDialog dialog = builder.create();
            LayoutInflater inflater = LayoutInflater.from(this.context);
            View dialogLayout = inflater.inflate(R.layout.challengedialog, null);
            linearLayout = (LinearLayout) dialogLayout.findViewById(R.id.challengeLayout);
            ImageView image = new ImageView(this.context);
            image.setImageDrawable(scaleImage(dImages[0]));
            EditText text = new EditText(this.context);
            TextView textv = new TextView(this.context);
            textv.setText("share text");
            text.setTag("challengetext");
            linearLayout.addView(textv);
            linearLayout.addView(text);
            linearLayout.addView(image);
            dialog.setView(dialogLayout);
            dialog.show();
        } catch (Exception e) {
        }
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
}
