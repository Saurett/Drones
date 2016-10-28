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
import texium.mx.drones.adapters.MemberGalleryAdapter;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.fragments.inetrface.FragmentGalleryListener;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.utils.Constants;


public class MemberGalleryFragment extends Fragment implements View.OnClickListener {

    private Tasks _TASK_INFO;
    private SoapObject soapObject;
    private static Users SESSION_DATA;

    static FragmentGalleryListener activityListener;
    static List<TaskGallery> taskGallery;

    static RecyclerView member_list;
    private static LinearLayout emptyGallery;

    static MemberGalleryAdapter member_gallery_adapter;

    private ProgressDialog pDialog;
    static FragmentManager fragmentManager;

    private static Button memberGalleryBtn;

    static Boolean reload = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_gallery_member, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        member_list = (RecyclerView) view.findViewById(R.id.member_gallery_list);
        emptyGallery = (LinearLayout) view.findViewById(R.id.emptyMemberGallery);
        memberGalleryBtn = (Button) view.findViewById(R.id.internal_gallery_member_button);

        member_gallery_adapter = new MemberGalleryAdapter();
        member_gallery_adapter.setOnClickListener(this);
        memberGalleryBtn.setOnClickListener(this);

        FragmentManager fmDescription = getActivity().getSupportFragmentManager();
        FragmentTransaction description = fmDescription.beginTransaction();
        description.add(R.id.detail_gallery_container, new MemberGalleryDescriptionFragment(), Constants.FRAGMENT_MEMBER_GALLERY_TAG);
        description.commit();

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        _TASK_INFO = (Tasks) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_ITEM_MEMBER_GALLERY);
        wsTaskList.execute();
    }

    @Override
    public void onStart() {

        if (reload) {
            activityListener.closeFragment(Constants.FRAGMENT_MEMBER_GALLERY_TAG);
            activityListener.replaceFragmentMemberFragment();
            reload = false;
        }

        super.onStart();
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

    private static void setEmptyView(View view) {
        member_list.setVisibility((member_gallery_adapter.getItemCount() > 0) ? View.VISIBLE : View.INVISIBLE);
        emptyGallery.setVisibility((member_gallery_adapter.getItemCount() > 0) ? View.INVISIBLE : View.VISIBLE);
        activityListener.setEmptyDescription(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.internal_gallery_photo_button:
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


    public static void reloadMembers(Boolean callWs) {
        reload = callWs;
    }


    public static void removeAt(int position) {
        taskGallery.remove(position);
        member_gallery_adapter.removeItem(position);

        //Move to preview list
        if (position > 0) {
            activityListener.getDecodeGallery().setTaskGallery(taskGallery.get(((position > 0) ? position - 1 : 0)));
        } else {
            activityListener.closeFragment(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
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
                    case Constants.WS_KEY_ITEM_MEMBER_GALLERY:

                        tempGalleryList = new ArrayList<>();
                        List<TaskGallery> memberGalleries = new ArrayList<>();

                        throw new ConnectException("Aqui va la cosa");

                        /*

                        soapObject = SoapServices.getTaskFiles(getContext(), _TASK_INFO.getTask_id(), 0, Constants.PICTURE_FILE_TYPE);

                        if (soapObject.hasProperty(Constants.SOAP_PROPERTY_DIFFGRAM)) {
                            SoapObject soDiffGram = (SoapObject) soapObject.getProperty(Constants.SOAP_PROPERTY_DIFFGRAM);
                            if (soDiffGram.hasProperty(Constants.SOAP_PROPERTY_NEW_DATA_SET)) {
                                SoapObject soNewDataSet = (SoapObject) soDiffGram.getProperty(Constants.SOAP_PROPERTY_NEW_DATA_SET);

                                for (int i = 0; i < soNewDataSet.getPropertyCount(); i++) {
                                    SoapObject soItem = (SoapObject) soNewDataSet.getProperty(i);

                                    TaskGallery photoServer = new TaskGallery();

                                    Integer systemType = (Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_SYSTEM_ID).toString()).
                                            equals(Constants.ITEM_SYNC_SERVER_DEFAULT)
                                            ? Constants.ITEM_SYNC_SERVER_DEFAULT : Constants.ITEM_SYNC_SERVER_CLOUD);

                                    photoServer.setId(Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_ID).toString()));
                                    photoServer.setSync_type(systemType);

                                    if (soItem.hasProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT)) {
                                        photoServer.setDescription(soItem.getPrimitiveProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                                    }

                                    photoServer.setFile_type(Constants.PICTURE_FILE_TYPE);

                                    TaskGallery photoLocal = BDTasksManagerQuery.getFileByServerId(getContext(), photoServer);

                                    Boolean exist = (photoLocal.getCve() != null);

                                    if (!exist) {

                                        TaskGallery decodePhoto =  FileServices.downloadFile(getActivity(),soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_SERVER_ADDRESS).toString());

                                        if (decodePhoto.getPhoto_bitmap() == null) continue;

                                        photoServer.setLocalURI(decodePhoto.getLocalURI());
                                        photoServer.setPhoto_bitmap(decodePhoto.getPhoto_bitmap());
                                        photoServer.setBase_package(FileServices.attachImgFromBitmap(photoServer.getPhoto_bitmap(), 50));
                                        memberGalleries.add(photoServer);
                                    }
                                }
                            }
                        }

                        if (!memberGalleries.isEmpty()) {

                            FilesManager filesManager = new FilesManager();
                            filesManager.setTaskGalleries(memberGalleries);

                            BDTasksManagerQuery.addTaskDetailPhotoGallery(getContext(), _TASK_INFO.getTask_id(),
                                    "Se agregan imagenes por ws", _TASK_INFO.getTask_status(), _TASK_INFO.getTask_user_id(),
                                    filesManager, true);
                        }

                        List<Integer> taskGallery = BDTasksManagerQuery.getListTaskDetail(getContext(), _TASK_INFO.getTask_id());

                        if (!taskGallery.isEmpty()) {
                            List<TaskGallery> allPhotos = BDTasksManagerQuery.getGalleryFiles(
                                    getContext(), taskGallery, Constants.PICTURE_FILE_TYPE, null, Constants.ACTIVE);

                            for (TaskGallery photo : allPhotos) {

                                if (photo.getBase_package() == null) continue;

                                byte[] decodedString = Base64.decode(photo.getBase_package(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                photo.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 800, 500, true));

                                tempGalleryList.add(photo);
                            }
                        }


                        validOperation = true;


                        break;

                        */
                }
            } catch (ConnectException e) {

                textError = e.getMessage();
                validOperation = false;

                try {

                    List<Integer> serverSync = new ArrayList<>();

                    serverSync.add(Constants.ITEM_SYNC_SERVER_CLOUD);
                    serverSync.add(Constants.ITEM_SYNC_LOCAL_TABLET);
                    serverSync.add(Constants.ITEM_SYNC_SERVER_DEFAULT);

                    List<TaskGallery> allMembers = BDTasksManagerQuery.getMembers(getContext(),
                            _TASK_INFO.getTask_id(), serverSync, null);

                    for (TaskGallery memberGallery : allMembers) {
                        TaskGallery member = new TaskGallery();

                        member.setCve(memberGallery.getCve());
                        member.setId(memberGallery.getId());
                        member.setSync_type(memberGallery.getSync_type());
                        member.setMember_name(memberGallery.getMember_name().replaceAll("-"," ").trim());
                        member.setMember_job(memberGallery.getMember_job());

                        member.setPhoto_bitmap(BitmapFactory.decodeResource(getContext().getResources(),R.drawable.empty_member_profile));

                        if (memberGallery.getBase_package() != null) {
                            byte[] decodedString = Base64.decode(memberGallery.getBase_package(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            member.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 650, 650, true));
                        }

                        tempGalleryList.add(member);
                    }

                    validOperation = true;
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
                taskGallery = new ArrayList<>();
                pDialog.dismiss();
                if (success) {

                    if (tempGalleryList.size() > 0) {
                        taskGallery.addAll(tempGalleryList);
                        member_gallery_adapter.addAll(taskGallery);

                        member_list.setAdapter(member_gallery_adapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        member_list.setLayoutManager(linearLayoutManager);

                        activityListener.setExtraDecodeGallery(taskGallery.get(0));

                    } else {
                        Toast.makeText(getActivity(), "La galeria de miembros se encuentra vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La galeria de miembros se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setEmptyView(null);
        }
    }


}
