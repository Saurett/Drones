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
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.PhotoGalleryAdapter;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.fragments.inetrface.FragmentGalleryListener;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.PhotoGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.FileServices;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;


public class PhotoGalleryFragment extends Fragment implements View.OnClickListener {

    private Tasks _TASK_INFO;
    private SoapObject soapObject;
    private static Users SESSION_DATA;

    static FragmentGalleryListener activityListener;
    static List<PhotoGallery> photoGallery;

    RecyclerView photos_list;

    static PhotoGalleryAdapter photo_gallery_adapter;

    private ProgressDialog pDialog;
    static FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_gallery_photo, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();


        photos_list = (RecyclerView) view.findViewById(R.id.photo_gallery_list);

        photo_gallery_adapter = new PhotoGalleryAdapter();
        photo_gallery_adapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        _TASK_INFO = (Tasks) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_ITEM_PHOTO_GALLERY);
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
        photoGallery.remove(position);
        photo_gallery_adapter.removeItem(position);

        //Move to preview list
        activityListener.getDecodeGallery().setPhotoGallery(photoGallery.get(position - 1));
        activityListener.openDescriptionFragment(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
        //setEmptyView(Constants.SEARCH);
    }


    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private List<PhotoGallery> tempGalleryList;
        private String textError;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
            tempGalleryList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getString(R.string.default_load_pictures));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_ITEM_PHOTO_GALLERY:

                        tempGalleryList = new ArrayList<>();
                        List<PhotoGallery> photoGalleries = new ArrayList<>();

                        soapObject = SoapServices.getTaskFiles(getContext(), _TASK_INFO.getTask_id(), 0);

                        if (soapObject.hasProperty(Constants.SOAP_PROPERTY_DIFFGRAM)) {
                            SoapObject soDiffGram = (SoapObject) soapObject.getProperty(Constants.SOAP_PROPERTY_DIFFGRAM);
                            if (soDiffGram.hasProperty(Constants.SOAP_PROPERTY_NEW_DATA_SET)) {
                                SoapObject soNewDataSet = (SoapObject) soDiffGram.getProperty(Constants.SOAP_PROPERTY_NEW_DATA_SET);

                                for (int i = 0; i < soNewDataSet.getPropertyCount(); i++) {
                                    SoapObject soItem = (SoapObject) soNewDataSet.getProperty(i);

                                    PhotoGallery photoServer = new PhotoGallery();

                                    Integer systemType = (Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_SYSTEM_ID).toString()).
                                            equals(Constants.ITEM_SYNC_SERVER_DEFAULT)
                                            ? Constants.ITEM_SYNC_SERVER_DEFAULT : Constants.ITEM_SYNC_SERVER_CLOUD);

                                    photoServer.setId(Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_ID).toString()));
                                    photoServer.setSync_type(systemType);

                                    if (soItem.hasProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT)) {
                                        photoServer.setDescription(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                                    }

                                    photoServer.setFile_type(Constants.PICTURE_FILE_TYPE);

                                    PhotoGallery photoLocal = BDTasksManagerQuery.getPhotoByServerId(getContext(),photoServer);

                                    Boolean exist = (photoLocal.getCve() != null);

                                    if (!exist) {
                                        Bitmap serverPhoto = FileServices.getBitmapFromURL(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_SERVER_ADDRESS).toString());

                                        if (serverPhoto == null) continue;

                                        photoServer.setPhoto_bitmap(serverPhoto);
                                        photoServer.setBase_package(FileServices.attachImgFromBitmap(photoServer.getPhoto_bitmap()));
                                        photoGalleries.add(photoServer);
                                    }
                                }
                            }
                        }

                        if (!photoGalleries.isEmpty()) {

                            FilesManager filesManager = new FilesManager();
                            filesManager.setPhotoGalleries(photoGalleries);

                            BDTasksManagerQuery.addTaskDetailPhotoGallery(getContext(),_TASK_INFO.getTask_id(),
                                    "Se agregan imagenes por ws", _TASK_INFO.getTask_status(),_TASK_INFO.getTask_user_id(),
                                    filesManager,true);
                        }

                        List<Integer> taskGallery = BDTasksManagerQuery.getListTaskDetail(getContext(), _TASK_INFO.getTask_id());

                        if (!taskGallery.isEmpty()) {
                            List<PhotoGallery> allPhotos = BDTasksManagerQuery.getGalleryFiles(
                                    getContext(), taskGallery, Constants.PICTURE_FILE_TYPE, null, Constants.ACTIVE);

                            for (PhotoGallery photo : allPhotos) {

                                if (photo.getBase_package() == null) continue;

                                byte[] decodedString = Base64.decode(photo.getBase_package(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                photo.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 800, 500, true));

                                tempGalleryList.add(photo);
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
                        List<PhotoGallery> allPhotos = BDTasksManagerQuery.getGalleryFiles(getContext(), taskGallery, Constants.PICTURE_FILE_TYPE, null, Constants.ACTIVE);

                        for (PhotoGallery photo : allPhotos) {

                            if (photo.getBase_package() == null) continue;

                            byte[] decodedString = Base64.decode(photo.getBase_package(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            photo.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 800, 500, true));

                            tempGalleryList.add(photo);
                        }
                    }
                    validOperation = (tempGalleryList.size() > 0);
                    textError =  (tempGalleryList.size() > 0) ? textError
                            : "La galeria de imagenes se encuentra vacía";
                } catch (Exception ex) {
                    textError = ex.getMessage();
                    ex.printStackTrace();
                }

            } catch (Exception e) {
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                photoGallery = new ArrayList<>();
                pDialog.dismiss();
                if (success) {

                    if (tempGalleryList.size() > 0) {
                        photoGallery.addAll(tempGalleryList);
                        photo_gallery_adapter.addAll(photoGallery);

                        photos_list.setAdapter(photo_gallery_adapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        photos_list.setLayoutManager(linearLayoutManager);

                        activityListener.setExtraDecodeGallery(photoGallery.get(0));

                        FragmentManager fmDescription = getActivity().getSupportFragmentManager();
                        FragmentTransaction description = fmDescription.beginTransaction();
                        description.add(R.id.detail_gallery_container, new PhotoGalleryDescriptionFragment(), Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                        description.commit();
                    } else {
                        Toast.makeText(getActivity(), "La galeria de imagenes se encuentra vacía", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La galeria de imagenes se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
