package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.FileServices;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;


public class VideoGalleryFragment extends Fragment implements View.OnClickListener {

    private Tasks _TASK_INFO;
    private SoapObject soapObject;
    private static Users SESSION_DATA;

    private static FragmentGalleryListener activityListener;
    private static List<TaskGallery> videoGallery;
    private static List<Integer> deleteFiles;

    private static RecyclerView videos_list;
    private static LinearLayout emptyGallery;

    static VideoGalleryAdapter video_gallery_adapter;

    private ProgressDialog pDialog;
    static FragmentManager fragmentManager;
    private Button videoGalleryBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_gallery_video, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();


        videos_list = (RecyclerView) view.findViewById(R.id.video_gallery_list);
        emptyGallery = (LinearLayout) view.findViewById(R.id.emptyVideoGallery);
        videoGalleryBtn = (Button) view.findViewById(R.id.internal_gallery_video_button);

        video_gallery_adapter = new VideoGalleryAdapter();
        video_gallery_adapter.setOnClickListener(this);

        videoGalleryBtn.setOnClickListener(this);

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

    private static void setEmptyView(Integer request) {
        videos_list.setVisibility((video_gallery_adapter.getItemCount() > 0) ? View.VISIBLE : View.INVISIBLE);
        emptyGallery.setVisibility((video_gallery_adapter.getItemCount() > 0) ? View.INVISIBLE : View.VISIBLE);
        activityListener.setEmptyDescription(video_gallery_adapter.getItemCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.internal_gallery_video_button:
                activityListener.openGallery();
                break;
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
            activityListener.getDecodeGallery().setTaskGallery(videoGallery.get(((position > 0) ? position - 1 : 0)));
            activityListener.openDescriptionFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
        } else {
            activityListener.closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
        }

        setEmptyView(null);
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
                        deleteFiles = new ArrayList<>();

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
                                        videoServer.setDescription(soItem.getPrimitiveProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                                    }

                                    videoServer.setFile_type(Constants.VIDEO_FILE_TYPE);

                                    TaskGallery videoLocal = BDTasksManagerQuery.getFileByServerId(getContext(), videoServer);

                                    Boolean exist = (videoLocal.getCve() != null);

                                    if (!exist) {

                                        TaskGallery decodeVideo = FileServices.downloadFile(getActivity(),soItem.getProperty(
                                                Constants.SOAP_OBJECT_KEY_TASK_SERVER_ADDRESS).toString());

                                        videoServer.setLocalURI(decodeVideo.getLocalURI());
                                        videoServer.setPhoto_bitmap(decodeVideo.getPhoto_bitmap());
                                        videoServer.setBase_package(FileServices.attachImgFromBitmap(videoServer.getPhoto_bitmap(),100));

                                        taskGalleries.add(videoServer);
                                    } else {

                                        if (videoLocal.getSync_type().equals(Constants.ITEM_SYNC_SERVER_CLOUD)) {

                                            videoLocal.setDescription(videoServer.getDescription());

                                            BDTasksManagerQuery.updateTaskFile(getContext(), videoLocal);
                                        }

                                    }

                                    Integer status = Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_STATUS).toString());

                                    if (status.equals(Constants.INACTIVE)) deleteFiles.add(videoServer.getId());
                                }
                            }
                        }

                        if (!taskGalleries.isEmpty()) {

                            for (TaskGallery videoGallery : taskGalleries) {
                                BDTasksManagerQuery.addTaskDetailVideo(getContext(), _TASK_INFO.getTask_id(),
                                        "Se agregan videos por ws", _TASK_INFO.getTask_status(), _TASK_INFO.getTask_user_id(),
                                        videoGallery, true);
                            }
                        }

                        List<Integer> taskGallery = BDTasksManagerQuery.getListTaskDetail(getContext(), _TASK_INFO.getTask_id());

                        if (!taskGallery.isEmpty()) {
                            List<TaskGallery> allPhotos = BDTasksManagerQuery.getGalleryFiles(
                                    getContext(), taskGallery, Constants.VIDEO_FILE_TYPE, null, Constants.ACTIVE);

                            for (TaskGallery video : allPhotos) {

                                if (deleteFiles.contains(video.getId())) {
                                    BDTasksManagerQuery.deleteTaskFile(getContext(),video);
                                    continue;
                                }

                                if (!video.getBase_package().isEmpty()) {
                                    byte[] decodedString = Base64.decode(video.getBase_package(), Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    video.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 800, 500, true));
                                }

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

                            if (!video.getBase_package().isEmpty()) {
                                byte[] decodedString = Base64.decode(video.getBase_package(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                video.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 800, 500, true));
                            }

                            tempGalleryList.add(video);
                        }
                    }
                    validOperation = (tempGalleryList.size() > 0);
                    textError = (tempGalleryList.size() > 0) ? textError
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

            setEmptyView(null);
        }
    }


}
