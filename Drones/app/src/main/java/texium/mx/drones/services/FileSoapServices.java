package texium.mx.drones.services;

import android.content.Context;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.utils.Constants;

/**
 * Created by texiumuser on 01/09/2016.
 */
public class FileSoapServices {

    private static SoapPrimitive soapPrimitive;

    public static Integer syncAllFiles(Context context, Integer idTask, Integer idUser) throws Exception {

        Integer success = 0;

        try {

            List<Integer> query = new ArrayList<>();

            query.add(Constants.ITEM_SYNC_LOCAL_TABLET);
            query.add(Constants.ITEM_SYNC_SERVER_CLOUD_OFF);

            List<Integer> taskGallery = BDTasksManagerQuery.getListTaskDetail(context, idTask);

            if (!taskGallery.isEmpty()) {
                List<TaskGallery> galleryBefore = BDTasksManagerQuery.getGalleryFiles(context,
                        taskGallery, Constants.PICTURE_FILE_TYPE, query, Constants.ACTIVE);

                //All Normal Photo Sync
                for (TaskGallery photo :
                        galleryBefore) {

                    if (photo.getDescription().isEmpty()) {
                        throw new Exception(context.getString(R.string.default_alert_empty_descripcion));
                    }

                    if (photo.getId() > 0) {
                        SoapServices.updatePhotoFile(context, photo, idUser);
                    } else {
                        soapPrimitive = SoapServices.savePhotoFile(context, photo, idTask, idUser);
                        photo.setId(Integer.valueOf(soapPrimitive.toString()));
                    }

                    photo.setSync_type(Constants.ITEM_SYNC_SERVER_CLOUD);
                    BDTasksManagerQuery.updateTaskFile(context, photo);
                    success++;
                }

                query = new ArrayList<>();
                query.add(Constants.ITEM_SYNC_SERVER_DELETE);

                galleryBefore = BDTasksManagerQuery.getGalleryFiles(context,
                        taskGallery, Constants.PICTURE_FILE_TYPE, query, Constants.INACTIVE);

                for (TaskGallery photo : galleryBefore) {

                    if (photo.getId() > 0) {
                        soapPrimitive = SoapServices.deletePhotoFile(context, photo.getId(), idUser);
                        if (null != soapPrimitive)
                            BDTasksManagerQuery.deleteTaskFile(context, photo);
                        success++;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception(e.getMessage());
        }

        return success;
    }



    public static Boolean syncAllFilesWithDetail(Context context, Integer idTask, Integer idUser, Integer idTaskDetail) {
        Boolean success = false;
        try {

            List<Integer> query = new ArrayList<>();

            query.add(Constants.ITEM_SYNC_LOCAL_TABLET);
            query.add(Constants.ITEM_SYNC_SERVER_CLOUD_OFF);

            List<Integer> taskGallery = BDTasksManagerQuery.getListTaskDetail(context, idTask);

            if (!taskGallery.isEmpty()) {

                BDTasksManagerQuery.updateTaskDetail(context
                        , idTaskDetail, Constants.SERVER_SYNC_FALSE);

                List<TaskGallery> galleryBefore = BDTasksManagerQuery.getGalleryFiles(context,
                        taskGallery, Constants.PICTURE_FILE_TYPE, query, Constants.ACTIVE);

                for (TaskGallery photo :
                        galleryBefore) {

                    if (photo.getId() > 0) {
                        SoapServices.updatePhotoFile(context, photo, idUser);
                    } else {
                        soapPrimitive = SoapServices.savePhotoFile(context, photo, idTask, idUser);
                        photo.setId(Integer.valueOf(soapPrimitive.toString()));
                    }

                    photo.setSync_type(Constants.ITEM_SYNC_SERVER_CLOUD);
                    BDTasksManagerQuery.updateTaskFile(context, photo);
                }

                query = new ArrayList<>();
                query.add(Constants.ITEM_SYNC_SERVER_DELETE);

                galleryBefore = BDTasksManagerQuery.getGalleryFiles(context,
                        taskGallery, Constants.PICTURE_FILE_TYPE, query, Constants.INACTIVE);

                for (TaskGallery photo :
                        galleryBefore) {

                    if (photo.getId() > 0) {
                        soapPrimitive = SoapServices.deletePhotoFile(context, photo.getId(), idUser);
                        if (null != soapPrimitive)
                            BDTasksManagerQuery.deleteTaskFile(context, photo);
                    }
                }

                BDTasksManagerQuery.updateTaskDetail(context
                        , idTaskDetail
                        , Constants.SERVER_SYNC_TRUE);
            }

            success = true;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return success;
    }
}
