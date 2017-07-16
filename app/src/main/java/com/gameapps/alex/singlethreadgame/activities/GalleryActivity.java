package com.gameapps.alex.singlethreadgame.activities;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.gameapps.alex.singlethreadgame.R;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn;
    Button backFromGallery;
    private ImageView chosenPIC;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        setContentView(R.layout.activity_gallery);
        backFromGallery = (Button)findViewById(R.id.backFromGallery);
        backFromGallery.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        btn = (Button) findViewById(R.id.btn);
        chosenPIC = (ImageView) findViewById(R.id.chosenPIC);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this,R.style.MyAlertPopup);

        pictureDialog.setTitle("Select Action");
        pictureDialog.setIcon(R.drawable.banan);
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    FriendsListActivity.playerImage= new BitmapDrawable(getResources(), bitmap);

                    Toast.makeText(GalleryActivity.this, "Image Selected!", Toast.LENGTH_SHORT).show();
                    chosenPIC.setImageBitmap(bitmap);
                    //FriendsListActivity.playerImage = chosenPIC.getDrawable();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(GalleryActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            chosenPIC.setImageBitmap(thumbnail);
            //FriendsListActivity.playerImage = chosenPIC.getDrawable();
            FriendsListActivity.playerImage= new BitmapDrawable(getResources(), thumbnail);
            //saveImage(thumbnail);
            Toast.makeText(GalleryActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight)
    {
        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = reqWidth;
        int nHeight = reqHeight;

        float parentRatio = (float) reqHeight / reqWidth;

        nHeight = bHeight;
        nWidth = (int) (reqWidth * parentRatio);

        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true);
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }




    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromGallery){
            finish();
        }

    }
}
