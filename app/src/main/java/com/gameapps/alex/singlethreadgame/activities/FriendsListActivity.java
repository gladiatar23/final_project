package com.gameapps.alex.singlethreadgame.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.gameapps.alex.singlethreadgame.GameSession;
import com.gameapps.alex.singlethreadgame.ImageProcessing;
import com.gameapps.alex.singlethreadgame.R;

public class FriendsListActivity extends AppCompatActivity {

    public static final String EXTRA_USER_IMG = "EXTRA_USER_IMG";
    public static final String EXTRA_JSON_FRIENDS = "EXTRA_JSON_FRIENDS";

    public static Drawable playerImage;



    private ImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

//        Intent intent = getIntent();
//        Uri imageUri = Uri.parse(intent.getStringExtra(EXTRA_USER_IMG));
//        String jsondata = intent.getStringExtra(EXTRA_JSON_FRIENDS);



        userAvatar = (ImageView) findViewById(R.id.userAvatar);
        if(playerImage != null) {
            Bitmap bitmapUserStickman = ImageProcessing.drawableToBitmap(playerImage);
            bitmapUserStickman = ImageProcessing.identifyFace(this , bitmapUserStickman);

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






}
