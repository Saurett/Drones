package texium.mx.drones.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.models.FilesManager;

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

    public static List<FilesManager> attachVideo(Activity activity, List<Uri> uriFileVideo) throws Exception {
        List<FilesManager> encodeVideos = new ArrayList<>();

        Context context = activity.getApplicationContext();
        try {

            for (Uri uri : uriFileVideo) {

                FilesManager encodeVideo = new FilesManager();

                InputStream is = activity.getContentResolver().openInputStream(uri);

                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

                // this is storage overwritten on each iteration with bytes
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                // we need to know how may bytes were read to write them to the byteBuffer
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }
                // and then we can return your byte array.
                //encodeVideo = getPackageBase64(context, byteBuffer.toByteArray());

                String tempData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                encodeVideo.setEncodeVideoFiles(tempData);

                encodeVideos.add(encodeVideo);
            }

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e("OutOfMemoryVideo Exception",e.getMessage());
            throw  new Exception(context.getString(R.string.default_out_of_memory));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachVideo Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_attaching_video_error));
        }

        return encodeVideos;
    }

    /*

    @Deprecated
    public static FilesManager getPackageBase64(Context context, byte[] dataPackage) throws Exception {

        FilesManager data = new FilesManager();

        int minBitPackage = 10000;
        int maxBitPackage = 1000000;

        String fmEncodeVideo = "";

        try {
            int packSize = dataPackage.length;
            int pack = (packSize > maxBitPackage) ? maxBitPackage : minBitPackage;
            int packNumbers = packSize / pack;
            int packSpecial = packSize - (packNumbers * pack);
            boolean specialItem = (packSpecial > 0);

            int endCount = pack;
            int starCount = 0;

            for (int i = 0; i <= packNumbers;) {

                String tempData = "";

                //Only the last iteration
                if ((i == packNumbers) && (specialItem)) {

                    tempData = Base64.encodeToString(Arrays.copyOfRange(dataPackage, starCount
                            , packSize) , Base64.DEFAULT);
                } else {

                    tempData = Base64.encodeToString(Arrays.copyOfRange(dataPackage, starCount
                            , endCount) , Base64.DEFAULT);

                    starCount = endCount;
                    endCount += pack;
                }

                i++;

                //BDTasksManagerQuery.addTaskFiles(context,2010,tempData, Constants.VIDEO_FILE_TYPE);

            }

            data.setEncodeVideoFiles(fmEncodeVideo);


        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e("OutOfMemoryVideo Exception",e.getMessage());
            throw  new OutOfMemoryError(context.getString(R.string.default_out_of_memory));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PackingVideo Exception",e.getMessage());
            throw  new Exception(context.getString(R.string.default_attaching_video_error));
        }

        return  data;
    }

    */

    public static List<String> getPackageList(Context context, String dataPackage) throws Exception {

        List<String> data = new ArrayList<>();
        int minBitPackage = 1000;
        int maxBitPackage = 3000000;

        try {
            int  packSize = dataPackage.length();
            int pack = (packSize > maxBitPackage) ? maxBitPackage : minBitPackage;
            int packNumbers = packSize / pack;
            int packSpecial = packSize - (packNumbers * pack);
            boolean specialItem = (packSpecial > 0);

            int endCount = pack;
            int starCount = 0;

            for (int i = 0; i <= packNumbers;) {

                String tempData = "";

                if ((i == packNumbers) && (specialItem)) {

                    tempData = dataPackage.substring(starCount,packSize);

                } else {

                    tempData = dataPackage.substring(starCount,endCount);

                    starCount = endCount;
                    endCount += pack;
                }

                data.add(tempData);

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PackingVideo Exception",e.getMessage());
            throw  new Exception(context.getString(R.string.default_attaching_video_error));
        }

        return  data;
    }

    public static Bitmap getBitmapFromURL(String src) {
        Bitmap myBitmap = null;
        try {

            URL url = new URL(src);
            //Quit blank space
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return myBitmap;
    }

    public static String attachImgFromBitmap(Bitmap bitmap) throws Exception {
        String imageEncoded = null;
        try {
            ByteArrayOutputStream convert = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, convert);
            byte[] b = convert.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachImg Exception",e.getMessage());
        }

        return imageEncoded;
    }
}
