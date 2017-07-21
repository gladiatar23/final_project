package com.gameapps.alex.singlethreadgame.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Profile;
import com.gameapps.alex.singlethreadgame.BuildConfig;
import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.ImageProcessing;
import com.gameapps.alex.singlethreadgame.R;
import com.gameapps.alex.singlethreadgame.sprite_definition.Enums;

public class FriendsListActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_USER_IMG = "EXTRA_USER_IMG";
    public static final String EXTRA_JSON_FRIENDS = "EXTRA_JSON_FRIENDS";

    public static final int IMAGE_FRAME_WIDTH = 150;
    public static final int IMAGE_FRAME_HEIGHT = 150;

    public static Drawable playerImage;
    Button backFromFriends;

    private ImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        setContentView(R.layout.activity_friends_list);
        backFromFriends = (Button) findViewById(R.id.backFromFriends);
        backFromFriends.setOnClickListener(this);
        launchPopup();

//        Intent intent = getIntent();
//        Uri imageUri = Uri.parse(intent.getStringExtra(EXTRA_USER_IMG));
//        String jsondata = intent.getStringExtra(EXTRA_JSON_FRIENDS);


        userAvatar = (ImageView) findViewById(R.id.userAvatar);

        Button toPlay = (Button) findViewById(R.id.play_again);
        toPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPlay();
            }
        });

        Button back = (Button) findViewById(R.id.backFromFriends);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMain();
            }
        });


    }

    public void goToPlay() {

        if (playerImage != null) {
            Bitmap bitmapUserStickman = ImageProcessing.drawableToBitmap(playerImage);

            bitmapUserStickman = ImageProcessing.identifyFace(this, bitmapUserStickman);


            if (!ImageProcessing.isFaceDetectionSuccessful) {
                Toast.makeText(this, "Unable to detect face, have this one instead", Toast.LENGTH_LONG).show();   //showing regular stickman head through transparency
                bitmapUserStickman = BitmapFactory.decodeResource(getResources(), R.drawable.one_pixel);
            }

            Log.i("image_size_bitmap" , "sizes are: " + bitmapUserStickman.getWidth() + ", " + bitmapUserStickman.getHeight());
            Bitmap drawableBm = BitmapFactory.decodeResource(getResources(), R.drawable.stickman);
            bitmapUserStickman = ImageProcessing.layBitmapOnTop(bitmapUserStickman, drawableBm);   //head on stickman bitmapUserStickman

            //random lavel
            GameSession.currentHeroBitmap = bitmapUserStickman;
            GameSession.currentLevel = GameSession.Level.values()[0];
            GameSession.initializeStatics(true);
            startActivity(new Intent(this, GameActivity.class));
//            userAvatar.setImageBitmap(bitmapUserStickman);
        }

//        try {
//            Log.i("daliughfdgiuh" , "here: " +imageUri.toString());
//
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//            userAvatar.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //placing friends - TODO: take images (only handles names right now)
//        JSONArray friendslist;
//        ArrayList<String> friends = new ArrayList<String>();
//
//        try {
//            friendslist = new JSONArray(jsondata);
//            for (int l=0; l < friendslist.length(); l++) {
//                friends.add(friendslist.getJSONObject(l).getString("name"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friends); // simple textview for list item
//        ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setAdapter(adapter);
//
//        for(int i=0 ; i<adapter.getCount() ; i++){
//            Object obj = adapter.getItem(i);
//        }
    }
//    TODO:
    public void goBackToMain() {
//        Intent intent = new Intent(this , MainMenuActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromFriends)
        {
            finish();
        }
    }
    //        Ask the user if he wants to change the image (from Facebook) he uses
    private void launchPopup() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        jumpToGallery();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked

                        break;
                }
            }
        };

        Profile p = Profile.getCurrentProfile();
        if (p != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertPopup);
            builder.setMessage("Do you want to change face picture?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    }
   // Goes to image selection activity
    private void jumpToGallery() {
        startActivity(new Intent(this, GalleryActivity.class));
    }
}
