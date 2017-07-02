package com.gameapps.alex.singlethreadgame.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.gameapps.alex.singlethreadgame.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FacebookActivity extends AppCompatActivity {

//    private static final boolean isPermittedByFacebook = false;

    private TextView textView;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    ImageView imageView;
    LoginButton login_button;
    FacebookCallback<LoginResult> callback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        login_button = (LoginButton) findViewById(R.id.login_button);
        textView = (TextView) findViewById(R.id.txtStatus);
        imageView = (ImageView) findViewById(R.id.imageView);
        Log.i("get img view", " " + imageView.toString());

        FacebookSdk.sdkInitialize(getApplicationContext());
        ProfilePictureView profilePictureView;
        //innitConntrolers();
        //loginFB();


        callbackManager = CallbackManager.Factory.create();
        textView = (TextView) findViewById(R.id.txtStatus);
        login_button = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        if(!isNetworkConnected())
        {
            Toast.makeText(this , "You need internet connection!" , Toast.LENGTH_LONG).show();
            finish();
        }
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                getDataFromFBProfile(newProfile);
                getFriendsAndMoveAct();
            }
        };



        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                getDataFromFBProfile(profile);
                getFriendsAndMoveAct();

            }

            @Override
            public void onCancel() {
            }

            public void onError(FacebookException error) {

            }

        };

        login_button.setReadPermissions("user_photos");
        login_button.registerCallback(callbackManager, callback);

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void launchPopup() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        getFriendsAndMoveAct();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        login_button.callOnClick();
                        break;
                }
            }
        };

        Profile p = Profile.getCurrentProfile();
        if (p != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Logged in - proceed as " + p.getFirstName() + " " + p.getLastName() + "?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }


    public void getDataFromFBProfile(Profile profile) {
        if (profile == null) {
            return;
        }
        textView.setText("Hello "+profile.getName());

        Uri url = profile.getProfilePictureUri(150, 150);

        Log.i("get img", " " + url);

        Picasso.with(getApplicationContext())
                .load(url)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("getdsgsedimg", " sf dgrfdhdtgj dhsrfhsdfbadsg dasgdsag");
                        launchPopup();
                    }

                    @Override
                    public void onError() {
                        Log.i("getdsgsedimg2", " sf dgrfdhdtgj dhsrfhsdfbadsg dasgdsag");
                    }
                });


    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.startTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        if (isLoggedIn()) {
            getDataFromFBProfile(profile);
        }
    }

    private void getFriendsAndMoveAct() {
        getFriendsAndMoveAct(true);
    }

    public void getFriendsAndMoveAct(boolean isRunning) {
        if (!isRunning) return;
        Profile profile = Profile.getCurrentProfile();
        if (profile == null)
            return;


        Drawable imageDrawable = imageView.getDrawable();
        FriendsListActivity.playerImage = imageDrawable;
        startActivity(new Intent(this, FriendsListActivity.class));
    }


    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

//        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;
        double ratio = 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }


}
