package com.gameapps.alex.singlethreadgame.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gameapps.alex.singlethreadgame.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

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
            userAvatar.setImageDrawable(playerImage);
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


    public BitmapDrawable identifyFace(int jpgDrawableId) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;
        Bitmap myBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),jpgDrawableId,
                // R.drawable.test1,
                options);
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
        FaceDetector faceDetector = new
                FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                .build();
        if(!faceDetector.isOperational()){
            new AlertDialog.Builder(this).setMessage("Could not set up the face detector!").show();
            return null;
        }
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        for(int i=0; i<faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            tempBitmap = getCroppedBitmap(tempBitmap , new Rect((int)x1, (int)y1, (int)x2, (int)y2));
        }
        return new BitmapDrawable(getResources(),tempBitmap);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap , Rect rectEllipse) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rectEllipse, rect, paint);
        return output;
    }

}
