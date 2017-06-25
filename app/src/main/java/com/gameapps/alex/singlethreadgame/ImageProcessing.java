package com.gameapps.alex.singlethreadgame;

import android.app.AlertDialog;
import android.content.Context;
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
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by USER on 25/06/2017.
 */

public class ImageProcessing {


    //---------------------------------Bitmap cropping---------------------------------//


    //    public BitmapDrawable identifyFace(int jpgDrawableId) {
    public static Bitmap identifyFace(Context context , Bitmap myBitmap) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;
//        Bitmap myBitmap = BitmapFactory.decodeResource(
//                getApplicationContext().getResources(),jpgDrawableId,
//                // R.drawable.test1,
//                options);
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(5);
        myRectPaint.setColor(Color.RED);
        myRectPaint.setStyle(Paint.Style.STROKE);
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
        FaceDetector faceDetector = new
                FaceDetector.Builder(context).setTrackingEnabled(false)
                .build();
        if(!faceDetector.isOperational()){
            new AlertDialog.Builder(context).setMessage("Could not set up the face detector!").show();
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
//        return new BitmapDrawable(context.getResources(),tempBitmap);
        return tempBitmap;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap , Rect rectEllipse) {
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



    //---------------------------------Bitmap converters---------------------------------//


    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }



    //---------------------------------Bitmap combining---------------------------------//



    public static Bitmap layBitmapOnTop(Bitmap bitmapTop, Bitmap bitmapBottom) {
        final double HEAD_LOCATION_HORIZONTAL_RATIO = (double)1/4; //[6/24 - 8/24
        final double HEAD_LOCATION_VERTICAL_RATIO = 0;
        final double HEAD_TO_STICK_RATIO = (double)1/3;

        Bitmap mergedBitmap = null;

        int w, h = 0;

        h = bitmapTop.getHeight() + bitmapBottom.getHeight();
        w = Math.max(bitmapBottom.getWidth() , bitmapTop.getWidth());

        mergedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mergedBitmap);

        int headSize = (int)(HEAD_TO_STICK_RATIO*bitmapBottom.getWidth());
        bitmapTop = scaleBitmap(bitmapTop , headSize , headSize);

        int locationW = (int)(HEAD_LOCATION_HORIZONTAL_RATIO*bitmapBottom.getWidth());
        int locationH = (int)(HEAD_LOCATION_VERTICAL_RATIO*bitmapBottom.getHeight());
        canvas.drawBitmap(bitmapBottom, 0 , 0, null);
        canvas.drawBitmap(bitmapTop, locationW, locationH, null);


        return mergedBitmap;
    }

    public static Bitmap scaleBitmap(Bitmap scaleMe , int width , int height) {
        return Bitmap.createScaledBitmap(scaleMe, width, height, false);
    }

    public static Bitmap mergeBitmap(Bitmap bitmapTop, Bitmap bitmapBottom) {

        Bitmap mergedBitmap = null;

        int w, h = 0;

        h = bitmapTop.getHeight() + bitmapBottom.getHeight();
        w = Math.max(bitmapBottom.getWidth() , bitmapTop.getWidth());

        mergedBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mergedBitmap);

        canvas.drawBitmap(bitmapTop, 0f, 0f, null);
        canvas.drawBitmap(bitmapBottom, 0f, bitmapTop.getHeight(), null);


        return mergedBitmap;
    }

}
