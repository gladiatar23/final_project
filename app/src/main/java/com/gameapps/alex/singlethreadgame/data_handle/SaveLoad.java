package com.gameapps.alex.singlethreadgame.data_handle;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by USER on 21/03/2017.
 */

public class SaveLoad {

    public static void createAndSaveFile(Activity activity , String fileName , String saveData) {

        String stringToStore = saveData;

        FileOutputStream outputStream;

        try {
            outputStream = activity.openFileOutput(fileName , Context.MODE_PRIVATE);
            outputStream.write(stringToStore.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "File write fail");
        }

    }

    public static int readNumberFromFile(Activity activity , String fileName) {

        InputStream inputStreamFile;

        try {
            inputStreamFile = activity.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStreamFile);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            inputStreamFile.close();
            return Integer.valueOf(stringBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "File read fail");
        }

        return 0;
    }

}
