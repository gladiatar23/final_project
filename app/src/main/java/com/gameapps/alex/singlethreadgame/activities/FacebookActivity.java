package com.gameapps.alex.singlethreadgame.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class FacebookActivity extends AppCompatActivity implements View.OnClickListener{

//    private static final boolean isPermittedByFacebook = false;

    private TextView textView;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    ImageView imageView;
    LoginButton login_button;
    FacebookCallback<LoginResult> callback;
    Button backFromFB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        backFromFB = (Button) findViewById(R.id.backFromFB);
        backFromFB.setOnClickListener(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        login_button = (LoginButton) findViewById(R.id.login_button);
        textView = (TextView) findViewById(R.id.txtStatus);
        imageView = (ImageView) findViewById(R.id.FaceOfBook);
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

    void getDataFromFBProfile(Profile profile) {
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

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromFB)
        {
            finish();
        }

    }
}
