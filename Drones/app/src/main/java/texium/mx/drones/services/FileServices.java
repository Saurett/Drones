package texium.mx.drones.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;

/**
 * Created by texiumuser on 11/03/2016.
 */
public class FileServices {

    public static List<String> attachImg(Activity activity, List<Uri> uriFilesPicture) throws Exception {
        List<String> stringsPicture = new ArrayList<>();

        Context context = activity.getApplicationContext();

        try {
            for (Uri uri : uriFilesPicture) {
                String imageEncoded;

                InputStream is = activity.getContentResolver()
                        .openInputStream(uri);
                Bitmap img = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream convert = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 50, convert);
                byte[] b = convert.toByteArray();
                imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

                stringsPicture.add(imageEncoded);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachImg Exception",e.getMessage());
            throw  new Exception(context.getString(R.string.default_attaching_img_error));
        }

        return stringsPicture;
    }

    public static List<String> attachVideo(Activity activity, List<Uri> uriFileVideo) throws Exception {
        List<String> stringsVideo = new ArrayList<>();

        Context context = activity.getApplicationContext();
        try {

            for (Uri uri : uriFileVideo) {

                String videoEncoded = "";

                InputStream is = activity.getContentResolver().openInputStream(uri);

                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                // this is storage overwritten on each iteration with bytes
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];

                // we need to know how may bytes were read to write them to the byteBuffer
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }
                // and then we can return your byte array.
                videoEncoded = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);

                stringsVideo.add(videoEncoded);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachImg Exception",e.getMessage());
            throw  new Exception(context.getString(R.string.default_attaching_video_error));
        }

        return stringsVideo;
    }
}
