/*
 * Copyright (c) 2015. DineshPrasanth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kd.dynamic.calendar.generator;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ImageGenerator {

    private final String LIBRARY_TAG = "Dynamic Calendar Icon";
    private final String IMAGE_GENERATED = "com.dynamic.image.generator";
    private final String IMAGE_GENERATED_KEY = IMAGE_GENERATED + ".count";
    SharedPreferences mPrefs;
    private Context mContext;
    //private Bitmap mSource;
    private Bitmap mDestination;
    private float mScaleFactor;
    private int mIconWidth;
    private int mIconHeight;
    private float mMonthSize;
    private float mDateSize;
    private int mDatePosition;
    private int mMonthPosition;
    private int mMonthColor;
    private int mDateColor;
    private boolean mMonthColorSet = false;
    private boolean mDateColorSet = false;
    private Typeface mDateTypeFace;
    private Typeface mMonthTypeFace;
    private boolean mNeedToStoreInStorage = false;
    private String mDate;
    private String mMonth;


    /**
     * Get the context of the activity.
     *
     * @param context The context in which image is to be placed
     */
    public ImageGenerator(Context context) {
        mContext = context;
        mScaleFactor = mContext.getResources().getDisplayMetrics().density;
        mPrefs = mContext.getSharedPreferences(IMAGE_GENERATED, Context.MODE_PRIVATE);
    }

    /**
     * Set the size of the icon to be generated.
     * <p/>
     * MANDATORY
     *
     * @param width  The width of the image
     * @param height The height of the image
     */
    public void setIconSize(int width, int height) {
        mIconWidth = (int) mScaleFactor * width;
        mIconHeight = (int) mScaleFactor * height;
    }

    /**
     * Set the size of the month font to be generated.
     * <p/>
     * MANDATORY
     *
     * @param monthSize The size of the date font
     */
    public void setMonthSize(float monthSize) {
        mMonthSize = (int) mScaleFactor * monthSize;
    }

    /**
     * Set the size of the date font to be generated.
     * <p/>
     * MANDATORY
     *
     * @param dateSize The size of the date font
     */
    public void setDateSize(float dateSize) {
        mDateSize = (int) mScaleFactor * dateSize;
    }

    /**
     * Set the Y co-ordinate of Month
     * <p/>
     * MANDATORY
     *
     * @param y Y co-ordinate from top in pixels
     */
    public void setMonthPosition(int y) {
        mMonthPosition = (int) mScaleFactor * y;
    }

    /**
     * Set the Y co-ordinate of Date
     * <p/>
     * MANDATORY
     *
     * @param y Y co-ordinate from top in pixels
     */
    public void setDatePosition(int y) {
        mDatePosition = (int) mScaleFactor * y;
    }

    /**
     * Apply the specified color to the Month font
     * <p/>
     * OPTIONAL
     *
     * @param color The color of the Month font
     */
    public void setMonthColor(int color) {
        mMonthColor = color;
        mMonthColorSet = true;
    }

    /**
     * Apply the specified color to the Date font
     * <p/>
     * OPTIONAL
     *
     * @param color The color of the Date font
     */
    public void setDateColor(int color) {
        mDateColor = color;
        mDateColorSet = true;
    }

    /**
     * Apply the specified TypeFace to the date font
     * <p/>
     * OPTIONAL
     *
     * @param fontName Name of the date font to be generated
     */
    public void setDateTypeFace(String fontName) {
        mDateTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + fontName);
    }

    /**
     * Apply the specified TypeFace to the month font
     * <p/>
     * OPTIONAL
     *
     * @param fontName Name of the month font to be generated
     */
    public void setMonthTypeFace(String fontName) {
        mMonthTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + fontName);
    }

    public void setStorageToSDCard(boolean toSDCard) {
        mNeedToStoreInStorage = toSDCard;
    }


    public Bitmap generateDateImage(Calendar dateString, int backgroundImage) {

        Log.d(LIBRARY_TAG, "The destination size set is: " + mIconWidth + "x" + mIconHeight);
        // Get the individual date and month from the date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        mDate = dateFormat.format(dateString.getTime());
        mMonth = monthFormat.format(dateString.getTime());

        // Print the separated date and month
        Log.d(LIBRARY_TAG, mDate + ":" + mMonth);

        // To ensure that it loads the correct size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        mDestination = BitmapFactory.decodeResource(mContext.getResources(), backgroundImage, options);
        Log.d(LIBRARY_TAG, "Size of the image selected: " + mDestination.getWidth() + " x " + mDestination.getHeight());

        // Set the size of the Destination image based on the accepted values
        mDestination = Bitmap.createScaledBitmap(mDestination, mIconWidth, mIconHeight, false);

        Rect bounds = new Rect();
        Canvas canvas = new Canvas(mDestination);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(mDestination, 0f, 0f, null);

        paint.setTextSize(mMonthSize);
        if (mMonthColorSet) paint.setColor(mMonthColor);
        else paint.setColor(Color.BLUE);
        paint.getTextBounds(mMonth, 0, mMonth.length(), bounds);
        canvas.drawText(mMonth, (canvas.getWidth() - bounds.width()) / 2, mMonthPosition, paint);

        paint.setTextSize(mDateSize);
        if (mDateColorSet) paint.setColor(mDateColor);
        else paint.setColor(Color.GREEN);
        paint.getTextBounds(mDate, 0, mDate.length(), bounds);
        canvas.drawText(mDate, (canvas.getWidth() - bounds.width()) / 2, mDatePosition, paint);

        Log.d(LIBRARY_TAG, "Image has been generated!");
        mPrefs.edit().putInt(IMAGE_GENERATED_KEY, mPrefs.getInt(IMAGE_GENERATED_KEY, 0) + 1).apply();
        if (mNeedToStoreInStorage) {
            try {
                File dirMake = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CalendarImageGenerated/");
                dirMake.mkdirs();

                mDestination.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(dirMake, mPrefs.getInt(IMAGE_GENERATED_KEY, 0) + ".png")));

                Log.d(LIBRARY_TAG, "Image Stored in " + dirMake.getAbsolutePath() + "GeneratedCalendar.png");

                return mDestination;

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return mDestination;
    }

    private float spToPixels(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        Log.d(LIBRARY_TAG, "SP:" + sp + "-> Pixel: " + Math.round(sp * scaledDensity) + "\n");
        return Math.round(sp * scaledDensity);
    }

   /* public Bitmap generateImage(Context context, String month, String date) {
       Bitmap src = BitmapFactory.decodeResource(context.getResources(), R.drawable.calendar_empty); // the original file yourimage.jpg i added in resources
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(spToPixels(context, 9f));
        tPaint.setColor(Color.WHITE);
        cs.drawBitmap(src, 0f, 0f, null);
        float height = tPaint.measureText("yY");
        float width_month = tPaint.measureText(month);
        float x_month = (src.getWidth() - width_month) / 2;
        cs.drawText(month, x_month, height + spToPixels(context, 1.5f), tPaint); // 15f is to put space between top edge and the text, if you want to change it, you can

        tPaint.setTextSize(spToPixels(context, 32f));
        int APP_THEME = MyFunctions.getAppTheme(context);
        TypedArray ar = context.getResources().obtainTypedArray(APP_THEME);
        int APPLY_NATIVE_COLOR = ar.getResourceId(2, 0);
        ar.recycle();

        tPaint.setColor(context.getResources().getColor(APPLY_NATIVE_COLOR)); // Change color based on Theme
        height = tPaint.measureText("yY");
        float width_date = tPaint.measureText(date);
        float x_date = (src.getWidth() - width_date) / 2;
        cs.drawText(date, x_date, height + spToPixels(context, 7f), tPaint);
        try {
            File dirMake = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.RemindMeBuddy/");
            dirMake.mkdirs();

            dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(dirMake, "GeneratedCalendar.png")));
            // dest is Bitmap, if you want to preview the final image, you can display it on screen also before saving
            return dest;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }*/
}
