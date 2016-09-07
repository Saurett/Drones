package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.VideoGalleryAdapter;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.fragments.inetrface.FragmentGalleryListener;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;


public class VideoGalleryFragment extends Fragment implements View.OnClickListener {

    private Tasks _TASK_INFO;
    private SoapObject soapObject;
    private static Users SESSION_DATA;

    static FragmentGalleryListener activityListener;
    static List<TaskGallery> videoGallery;

    RecyclerView videos_list;

    static VideoGalleryAdapter video_gallery_adapter;

    private ProgressDialog pDialog;
    static FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_gallery_video, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();


        videos_list = (RecyclerView) view.findViewById(R.id.video_gallery_list);

        video_gallery_adapter = new VideoGalleryAdapter();
        video_gallery_adapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        _TASK_INFO = (Tasks) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_ITEM_VIDEO_GALLERY);
        wsTaskList.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityListener = (FragmentGalleryListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " debe implementar ");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    public static void showQuestion(DecodeGallery decodeGallery) {
        activityListener.updateDecodeGallery(decodeGallery);
        activityListener.showQuestion(decodeGallery.getIdView());
    }


    public static void removeAt(int position) {
        videoGallery.remove(position);
        video_gallery_adapter.removeItem(position);

        //Move to preview list
        if (video_gallery_adapter.getItemCount() > 0) {
            activityListener.getDecodeGallery().setTaskGallery(videoGallery.get(position - 1));
            activityListener.openDescriptionFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
        } else {
            activityListener.closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
        }

        //setEmptyView(Constants.SEARCH);
    }


    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private List<TaskGallery> tempGalleryList;
        private String textError;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
            tempGalleryList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getString(R.string.default_load_videos));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_ITEM_VIDEO_GALLERY:

                        tempGalleryList = new ArrayList<>();
                        List<TaskGallery> taskGalleries = new ArrayList<>();

                        soapObject = SoapServices.getTaskFiles(getContext(), _TASK_INFO.getTask_id(), 0, Constants.VIDEO_FILE_TYPE);

                        if (soapObject.hasProperty(Constants.SOAP_PROPERTY_DIFFGRAM)) {
                            SoapObject soDiffGram = (SoapObject) soapObject.getProperty(Constants.SOAP_PROPERTY_DIFFGRAM);
                            if (soDiffGram.hasProperty(Constants.SOAP_PROPERTY_NEW_DATA_SET)) {
                                SoapObject soNewDataSet = (SoapObject) soDiffGram.getProperty(Constants.SOAP_PROPERTY_NEW_DATA_SET);

                                for (int i = 0; i < soNewDataSet.getPropertyCount(); i++) {
                                    SoapObject soItem = (SoapObject) soNewDataSet.getProperty(i);

                                    TaskGallery videoServer = new TaskGallery();

                                    Integer systemType = (Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_SYSTEM_ID).toString()).
                                            equals(Constants.ITEM_SYNC_SERVER_DEFAULT)
                                            ? Constants.ITEM_SYNC_SERVER_DEFAULT : Constants.ITEM_SYNC_SERVER_CLOUD);

                                    videoServer.setId(Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_ID).toString()));
                                    videoServer.setSync_type(systemType);

                                    if (soItem.hasProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT)) {
                                        videoServer.setDescription(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                                    }

                                    videoServer.setFile_type(Constants.VIDEO_FILE_TYPE);

                                    TaskGallery videoLocal = BDTasksManagerQuery.getFileByServerId(getContext(),videoServer);

                                    Boolean exist = (videoLocal.getCve() != null);

                                    if (!exist) {

                                        /*Bitmap serverPhoto = FileServices.reverseVideoFrameFromVideo(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_SERVER_ADDRESS).toString());

                                        if (serverPhoto == null) continue;

                                        videoServer.setPhoto_bitmap(serverPhoto);
                                        videoServer.setBase_package(FileServices.attachImgFromBitmap(videoServer.getPhoto_bitmap()));
                                        */

                                        taskGalleries.add(videoServer);
                                    }
                                }
                            }
                        }

                        if (!taskGalleries.isEmpty()) {

                            FilesManager filesManager = new FilesManager();
                            filesManager.setTaskGalleries(taskGalleries);

                            BDTasksManagerQuery.addTaskDetailPhotoGallery(getContext(),_TASK_INFO.getTask_id(),
                                    "Se agregan videos por ws", _TASK_INFO.getTask_status(),_TASK_INFO.getTask_user_id(),
                                    filesManager,true);

                            //TODO GUARDAR VIDEO EN EL DISPOSITIVO
                        }

                        List<Integer> taskGallery = BDTasksManagerQuery.getListTaskDetail(getContext(), _TASK_INFO.getTask_id());

                        if (!taskGallery.isEmpty()) {
                            List<TaskGallery> allPhotos = BDTasksManagerQuery.getGalleryFiles(
                                    getContext(), taskGallery, Constants.VIDEO_FILE_TYPE, null, Constants.ACTIVE);

                            for (TaskGallery video : allPhotos) {


                                //if (video.getBase_package() == null) continue;
/*
                                if (!video.getBase_package().isEmpty()) {
                                    byte[] decodedString = Base64.decode(video.getBase_package(), Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    video.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 800, 500, true));
                                }
                                */

                                tempGalleryList.add(video);
                            }
                        }

                        validOperation = true;

                        break;
                }
            } catch (ConnectException e) {

                textError = e.getMessage();
                validOperation = false;

                try {

                    List<Integer> taskGallery = BDTasksManagerQuery.getListTaskDetail(getContext(), _TASK_INFO.getTask_id());

                    if (!taskGallery.isEmpty()) {
                        List<TaskGallery> allPhotos = BDTasksManagerQuery.getGalleryFiles(getContext(), taskGallery, Constants.VIDEO_FILE_TYPE, null, Constants.ACTIVE);

                        for (TaskGallery video : allPhotos) {

                            /*
                            if (video.getBase_package() == null) continue;

                            if (!video.getBase_package().isEmpty()) {
                                byte[] decodedString = Base64.decode(video.getBase_package(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                video.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 800, 500, true));
                            }
                            */

                            tempGalleryList.add(video);
                        }
                    }
                    validOperation = (tempGalleryList.size() > 0);
                    textError =  (tempGalleryList.size() > 0) ? textError
                            : "La galeria de videos se encuentra vacía";
                } catch (Exception ex) {
                    textError = ex.getMessage();
                    ex.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                videoGallery = new ArrayList<>();
                pDialog.dismiss();
                if (success) {

                    if (tempGalleryList.size() > 0) {
                        videoGallery.addAll(tempGalleryList);
                        video_gallery_adapter.addAll(videoGallery);

                        videos_list.setAdapter(video_gallery_adapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        videos_list.setLayoutManager(linearLayoutManager);

                        activityListener.setExtraDecodeGallery(videoGallery.get(0));

                        FragmentManager fmDescription = getActivity().getSupportFragmentManager();
                        FragmentTransaction description = fmDescription.beginTransaction();
                        description.add(R.id.detail_gallery_container, new VideoGalleryDescriptionFragment(), Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                        description.commit();
                    } else {
                        Toast.makeText(getActivity(), "La galeria de videos se encuentra vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La galeria de videos se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
