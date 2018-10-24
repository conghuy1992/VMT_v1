package com.monamedia.vmt.common;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;


import com.monamedia.vmt.R;
import com.monamedia.vmt.common.interfaces.Statics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageLoader {

    public static Uri convertUri(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        Uri myUri = Uri.parse("file://" + result);
        return myUri;
    }

    public static File getOutputMediaFile(Context context, int type) {
        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),Statics.IMAGE_DIRECTORY_NAME);
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Statics.pathDownload);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat(Statics.DATE_FORMAT_PICTURE, Locale.getDefault()).format(new Date());

        File mediaFile;

        if (type == Statics.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + Utils.getMsg(context, R.string.pre_file_name) + timeStamp + Statics.IMAGE_JPG);
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String getPathFromURI(Uri contentUri, Context context) {
        String result;
        try {
            Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                if (idx == -1) {
                    result = Environment.getExternalStorageDirectory() + contentUri.getPath();
                    result = result.replace("/external_files", "");
                } else {
                    result = cursor.getString(idx);
                }

                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        }
        return result;
    }

    public static Bitmap createScaleBitmap(String pathFile, int reqSize) {
        try {
            // Get the dimensions of the View
            int targetW = reqSize;
            int targetH = reqSize;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathFile, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bmResult = BitmapFactory.decodeFile(pathFile, bmOptions);
            ExifInterface exifReader = new ExifInterface(pathFile);
            int exifOrientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            int exifToDegrees = exifToDegrees(exifOrientation);

            return bitMapRotate(exifToDegrees, bmResult);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap bitMapRotate(int exifToDegrees, Bitmap bmPhotoResult) {
        //Create object of new Matrix.
        Matrix matrix = new Matrix();
        //set image rotation value to 90 degrees in matrix.
        matrix.postRotate(exifToDegrees);
//        matrix.postScale(0.5f, 0.5f);
        int newWidth = bmPhotoResult.getWidth();
        int newHeight = bmPhotoResult.getHeight();
        //Create bitmap with new values.
        Bitmap bMapRotate = Bitmap.createBitmap(bmPhotoResult, 0, 0, newWidth, newHeight, matrix, true);
        return bMapRotate;
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Uri getOutputMediaFileUri(Context context, int type) {
        return Uri.fromFile(ImageLoader.getOutputMediaFile(context, type));
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap convertBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


}
