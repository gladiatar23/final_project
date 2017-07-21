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
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

/**
 * Created by USER on 25/06/2017.
 */
//Class of Face Recognition
public class ImageProcessing {


    //---------------------------------Bitmap cropping---------------------------------//

    public static boolean isFaceDetectionSuccessful;

    //    public BitmapDrawable identifyFace(int jpgDrawableId) {
    public static Bitmap identifyFace(Context context , Bitmap myBitmap) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;
//        Bitmap myBitmap = BitmapFactory.decodeResource(
//                getApplicationContext().getResources(),jpgDrawableId,
//                // R.drawable.test1,
//                options);
        Paint myRectPaint = new Paint();
        myRectPaint.setStrokeWidth(3);
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
        isFaceDetectionSuccessful = (faces.size() != 0);

        for(int i=0; i<faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float x1 = thisFace.getPosition().x;
            float y1 = thisFace.getPosition().y;
            float x2 = x1 + thisFace.getWidth();
            float y2 = y1 + thisFace.getHeight();
            Rect rect = new Rect((int)x1, (int)y1, (int)x2, (int)y2);

            tempBitmap = getCroppedBitmap(tempBitmap , rect);
        }
//        return new BitmapDrawable(context.getResources(),tempBitmap);
        return tempBitmap;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap , Rect rectEllipse) {
        Bitmap output = Bitmap.createBitmap(rectEllipse.width(),
                rectEllipse.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, rectEllipse.width(), rectEllipse.height());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(rectEllipse.width() / 2, rectEllipse.height() / 2,
                rectEllipse.height() / 2, paint);
//        canvas.drawOval(0, 0 , bitmap.getWidth()/2 , bitmap.getHeight()/2, paint);
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

    public static Bitmap scaleBitmap(Bitmap scaleMe , int width , int height) {
        return Bitmap.createScaledBitmap(scaleMe, width, height, false);
    }


    //---------------------------------Bitmap combining---------------------------------//


//TODO
    public static Bitmap layBitmapOnTop(Bitmap bitmapTop, Bitmap bitmapBottom) {
        final double HEAD_LOCATION_HORIZONTAL_RATIO = (double)48/192; //[6/24 - 8/24
        final double HEAD_LOCATION_VERTICAL_RATIO = 0;
        final double HEAD_TO_STICK_RATIO = (double)33/90;

        Bitmap mergedBitmap = null;

        mergedBitmap = Bitmap.createBitmap(bitmapBottom.getWidth(), bitmapBottom.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mergedBitmap);

        int headSize = (int)(HEAD_TO_STICK_RATIO*bitmapBottom.getWidth());
        bitmapTop = scaleBitmap(bitmapTop , headSize , (int) (headSize*1.2)); //TODO - find better ratio and relocate the top

        int locationW = (int)(HEAD_LOCATION_HORIZONTAL_RATIO*bitmapBottom.getWidth());
        int locationH = (int)(HEAD_LOCATION_VERTICAL_RATIO*bitmapBottom.getHeight());
        canvas.drawBitmap(bitmapBottom, 0 , 0, null);
        canvas.drawBitmap(bitmapTop, locationW, locationH, null);

        int finalImageHeight = Math.max(locationH , bitmapBottom.getHeight());  //max of top-of-head and stickman height
//        crop to get rid of excess area above
        mergedBitmap = Bitmap.createBitmap(mergedBitmap, 0, 0 , mergedBitmap.getWidth(), finalImageHeight);  //crop out excess height

        return mergedBitmap;
    }

}
