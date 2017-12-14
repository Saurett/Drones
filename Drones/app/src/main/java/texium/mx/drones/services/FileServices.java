package texium.mx.drones.services;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.TaskGallery;

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
            Log.e("AttachImg Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_attaching_img_error));
        }

        return stringsPicture;
    }

    public static String attachImg(Activity activity, Uri uri, int size) throws Exception {
        String stringsPicture = new String();

        Context context = activity.getApplicationContext();

        try {
            String imageEncoded;

            InputStream is = activity.getContentResolver()
                    .openInputStream(uri);
            Bitmap img = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream convert = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, size, convert);
            byte[] b = convert.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            stringsPicture = imageEncoded;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachImg Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_attaching_img_error));
        }

        return stringsPicture;
    }

    public static Bitmap attachImgBitmap(Activity activity, Uri uri, int size) throws Exception {

        Context context = activity.getApplicationContext();

        try {
            InputStream is = activity.getContentResolver()
                    .openInputStream(uri);
            Bitmap img = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream convert = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, size, convert);


            return img;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachImg Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_attaching_img_error));
        }
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
                encodeVideo.setEncodeSingleFile(tempData);

                encodeVideos.add(encodeVideo);
            }

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e("OutOfMemoryVideo Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_out_of_memory));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachVideo Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_attaching_video_error));
        }

        return encodeVideos;
    }

    public static FilesManager attachVideos(Activity activity, Uri uriFileVideo) throws Exception {
        FilesManager data = new FilesManager();

        Context context = activity.getApplicationContext();
        try {

            InputStream is = activity.getContentResolver().openInputStream(uriFileVideo);

            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            // this is storage overwritten on each iteration with bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            String tempData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
            data.setEncodeSingleFile(tempData);
            data.setTitle(uriFileVideo.toString());
/*
            String realPath = FileServices.getPath(activity.getApplicationContext(), Uri.parse(uriFileVideo.toString()));
            InputStream is = new BufferedInputStream(new FileInputStream(realPath));

            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            while (is.available() > 0) {
                byteBuffer.write(is.read());
            }

            String tempData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
            data.setEncodeSingleFile(tempData);
            data.setTitle(uriFileVideo.toString());*/

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e("OutOfMemoryVideo Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_out_of_memory));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachVideo Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_attaching_video_error));
        }

        return data;
    }

    public static FilesManager attachFile(Activity activity, Uri uriFile) throws Exception {
        FilesManager data = new FilesManager();

        Context context = activity.getApplicationContext();
        try {
            InputStream is = activity.getContentResolver().openInputStream(uriFile);

            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            // this is storage overwritten on each iteration with bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            // we need to know how may bytes were read to write them to the byteBuffer
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            String tempData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
            data.setEncodeSingleFile(tempData);
            data.setTitle(uriFile.toString());

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e("OutOfMemoryVideo Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_out_of_memory));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachVideo Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_attaching_video_error));
        }

        return data;
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
        int midBitPackage = 1000000;
        int maxBitPackage = 3000000;

        try {
            int packSize = dataPackage.length();
            int pack = (packSize > maxBitPackage) ? maxBitPackage :
                    (packSize < midBitPackage) ? packSize : midBitPackage;
            int packNumbers = packSize / pack;
            int packSpecial = packSize - (packNumbers * pack);
            boolean specialItem = (packSpecial > 0);

            int endCount = pack;
            int starCount = 0;

            for (int i = 0; i <= packNumbers; ) {

                String tempData = "";

                if ((i == packNumbers) && (specialItem)) {

                    tempData = dataPackage.substring(starCount, packSize);

                } else {

                    if (endCount <= packSize) {
                        tempData = dataPackage.substring(starCount, endCount);

                        starCount = endCount;
                        endCount += pack;
                    }
                }

                if (!tempData.isEmpty()) data.add(tempData);

                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PackingVideo Exception", e.getMessage());
            throw new Exception(context.getString(R.string.default_attaching_video_error));
        }

        return data;
    }

    public static Bitmap getBitmapFromURL(String src) {
        Bitmap myBitmap = null;
        try {

            String path = quitUrlBlackSpace(src);
            URL url = URI.create(path).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myBitmap;
    }

    public static String attachImgFromBitmap(Bitmap bitmap, int resolution) throws Exception {
        String imageEncoded = null;
        try {
            ByteArrayOutputStream convert = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, resolution, convert);
            byte[] b = convert.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AttachImg Exception", e.getMessage());
        }

        return imageEncoded;
    }

    public static Bitmap reverseVideoFrameFromVideo(String videoPath)
            throws Exception {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            videoPath = quitUrlBlackSpace(videoPath);

            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception in reverseVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Video.Media.DATA};
            cursor = context.getContentResolver().query(contentURI, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static TaskGallery downloadFile(Activity activity, String fileURL) throws IOException {
        TaskGallery taskGallery = new TaskGallery();
        try {

            Boolean isImage = false;

            String videoURIPath;
            fileURL = quitUrlBlackSpace(fileURL);
            URL url = new URL(fileURL);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                            fileURL.length());
                }

                isImage = (contentType.contains("image/"));

                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);

                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = Environment.getExternalStorageDirectory() + File.separator + fileName;

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                videoURIPath = saveFilePath.toString();

                System.out.println("File downloaded" + videoURIPath);
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                throw new Exception("No file to download. Server replied HTTP code: " + responseCode);
            }

            httpConn.disconnect();

            Uri uri = Uri.fromFile(new File(videoURIPath));

            Bitmap bitmap = (isImage) ? attachImgBitmap(activity, uri, 100) :
                    ThumbnailUtils.createVideoThumbnail(new File(videoURIPath).getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);

            taskGallery.setLocalURI(uri.toString());
            taskGallery.setPhoto_bitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskGallery;
    }

    public static Bitmap createVideoThumbnail(Context context, Uri uri) {
        MediaMetadataRetriever mediametadataretriever = new MediaMetadataRetriever();

        try {
            mediametadataretriever.setDataSource(context, uri);
            Bitmap bitmap = mediametadataretriever.getFrameAtTime(-1L);
            if (null != bitmap) {
                return ThumbnailUtils.extractThumbnail(bitmap, 100, 100, 2);
            }
            return bitmap;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        } finally {
            try {
                mediametadataretriever.release();
            } catch (RuntimeException e) {
            }
        }
    }

    public static Bitmap createDocumentThumbnail(Context context, Uri uri) throws Exception {
        try {
            final ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(
                    uri, "r");
            final FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return bitmap;
        } catch (Exception e) {
            throw new Exception("failed to parse image uri");
        }
    }

    public static String quitUrlBlackSpace(String fileURL) {
        String correctUri = "";
        try {
            URL url = new URL(fileURL);
            //Quit blank space
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());

            correctUri = uri.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return correctUri;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}
