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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.DocumentGalleryAdapter;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.fragments.inetrface.FragmentGalleryListener;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.FileServices;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;


public class DocumentGalleryFragment extends Fragment implements View.OnClickListener {

    private Tasks _TASK_INFO;
    private SoapObject soapObject;
    private static Users SESSION_DATA;

    static FragmentGalleryListener activityListener;
    private static List<TaskGallery> documentGallery;
    private static List<Integer> deleteFiles;

    private static RecyclerView document_list;
    private static LinearLayout emptyGallery;

    static DocumentGalleryAdapter document_gallery_adapter;

    private ProgressDialog pDialog;
    static FragmentManager fragmentManager;

    private static Button documentGalleryBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_gallery_document, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();


        document_list = (RecyclerView) view.findViewById(R.id.document_gallery_list);
        emptyGallery = (LinearLayout) view.findViewById(R.id.emptyDocumentGallery);
        documentGalleryBtn = (Button) view.findViewById(R.id.internal_gallery_document_button);

        document_gallery_adapter = new DocumentGalleryAdapter();
        document_gallery_adapter.setOnClickListener(this);
        documentGalleryBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        _TASK_INFO = (Tasks) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_ITEM_DOCUMENT_GALLERY);
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
        document_list.setVisibility((document_gallery_adapter.getItemCount() > 0) ? View.VISIBLE : View.INVISIBLE);
        emptyGallery.setVisibility((document_gallery_adapter.getItemCount() > 0) ? View.INVISIBLE : View.VISIBLE);
        activityListener.setEmptyDescription(document_gallery_adapter.getItemCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.internal_gallery_document_button:
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
        documentGallery.remove(position);
        document_gallery_adapter.removeItem(position);

        //Move to preview list
        if (document_gallery_adapter.getItemCount() > 0) {
            activityListener.getDecodeGallery().setTaskGallery(documentGallery.get(((position > 0) ? position - 1 : 0)));
            activityListener.openDescriptionFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
        } else {
            activityListener.closeFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
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
            pDialog.setMessage(getString(R.string.default_load_document));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_ITEM_DOCUMENT_GALLERY:

                        tempGalleryList = new ArrayList<>();
                        List<TaskGallery> taskGalleries = new ArrayList<>();
                        deleteFiles = new ArrayList<>();

                        soapObject = SoapServices.getTaskFiles(getContext(), _TASK_INFO.getTask_id(), 0, Constants.DOCUMENT_FILE_TYPE);

                        if (soapObject.hasProperty(Constants.SOAP_PROPERTY_DIFFGRAM)) {
                            SoapObject soDiffGram = (SoapObject) soapObject.getProperty(Constants.SOAP_PROPERTY_DIFFGRAM);
                            if (soDiffGram.hasProperty(Constants.SOAP_PROPERTY_NEW_DATA_SET)) {
                                SoapObject soNewDataSet = (SoapObject) soDiffGram.getProperty(Constants.SOAP_PROPERTY_NEW_DATA_SET);

                                for (int i = 0; i < soNewDataSet.getPropertyCount(); i++) {
                                    SoapObject soItem = (SoapObject) soNewDataSet.getProperty(i);

                                    TaskGallery documentServer = new TaskGallery();

                                    Integer systemType = (Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_SYSTEM_ID).toString()).
                                            equals(Constants.ITEM_SYNC_SERVER_DEFAULT)
                                            ? Constants.ITEM_SYNC_SERVER_DEFAULT : Constants.ITEM_SYNC_SERVER_CLOUD);

                                    documentServer.setId(Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_ID).toString()));
                                    documentServer.setSync_type(systemType);

                                    if (soItem.hasProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT)) {
                                        documentServer.setDescription(soItem.getPrimitiveProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                                    }

                                    documentServer.setFile_type(Constants.DOCUMENT_FILE_TYPE);

                                    TaskGallery documentLocal = BDTasksManagerQuery.getFileByServerId(getContext(), documentServer);

                                    Boolean exist = (documentLocal.getCve() != null);

                                    if (!exist) {

                                        TaskGallery decodeDocument = FileServices.downloadFile(getActivity(),soItem.getProperty(
                                                Constants.SOAP_OBJECT_KEY_TASK_SERVER_ADDRESS).toString());

                                        documentServer.setLocalURI(decodeDocument.getLocalURI());

                                        taskGalleries.add(documentServer);
                                    } else {

                                        if (documentLocal.getSync_type().equals(Constants.ITEM_SYNC_SERVER_CLOUD)) {

                                            documentLocal.setDescription(documentServer.getDescription());

                                            BDTasksManagerQuery.updateTaskFile(getContext(), documentLocal);
                                        }

                                    }

                                    Integer status = Integer.valueOf(soItem.getProperty(Constants.SOAP_OBJECT_KEY_TASK_STATUS).toString());

                                    if (status.equals(Constants.INACTIVE)) deleteFiles.add(documentServer.getId());

                                }
                            }
                        }

                        if (!taskGalleries.isEmpty()) {

                            for (TaskGallery documentGallery : taskGalleries) {
                                BDTasksManagerQuery.addTaskDetailVideo(getContext(), _TASK_INFO.getTask_id(),
                                        "Se agregan documentos por ws", _TASK_INFO.getTask_status(), _TASK_INFO.getTask_user_id(),
                                        documentGallery, true);
                            }
                        }

                        List<Integer> taskGallery = BDTasksManagerQuery.getListTaskDetail(getContext(), _TASK_INFO.getTask_id());

                        if (!taskGallery.isEmpty()) {
                            List<TaskGallery> allDocuments = BDTasksManagerQuery.getGalleryFiles(
                                    getContext(), taskGallery, Constants.DOCUMENT_FILE_TYPE, null, Constants.ACTIVE);

                            for (TaskGallery document : allDocuments) {

                                if (deleteFiles.contains(document.getId())) {
                                    BDTasksManagerQuery.deleteTaskFile(getContext(),document);
                                    continue;
                                }

                                tempGalleryList.add(document);
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
                        List<TaskGallery> allDocuments = BDTasksManagerQuery.getGalleryFiles(getContext(), taskGallery, Constants.DOCUMENT_FILE_TYPE, null, Constants.ACTIVE);

                        /*
                        for (TaskGallery document : allDocuments) {

                            if (document.getBase_package() != null ) {
                                if (!document.getBase_package().isEmpty()) {
                                    byte[] decodedString = Base64.decode(document.getBase_package(), Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    document.setPhoto_bitmap(Bitmap.createScaledBitmap(decodedByte, 800, 500, true));
                                }
                            }
                        }*/

                        tempGalleryList.addAll(allDocuments);

                    }
                    validOperation = (tempGalleryList.size() > 0);
                    textError = (tempGalleryList.size() > 0) ? textError
                            : "La galeria de documentos se encuentra vacía";
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
                documentGallery = new ArrayList<>();
                pDialog.dismiss();
                if (success) {

                    if (tempGalleryList.size() > 0) {
                        documentGallery.addAll(tempGalleryList);
                        document_gallery_adapter.addAll(documentGallery);

                        document_list.setAdapter(document_gallery_adapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        document_list.setLayoutManager(linearLayoutManager);

                        activityListener.setExtraDecodeGallery(documentGallery.get(0));

                        FragmentManager fmDescription = getActivity().getSupportFragmentManager();
                        FragmentTransaction description = fmDescription.beginTransaction();
                        description.add(R.id.detail_gallery_container, new DocumentGalleryDescriptionFragment(), Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
                        description.commit();
                    } else {
                        Toast.makeText(getActivity(), "La galeria de documentos se encuentra vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La galeria de documentos se encuentra vacía" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setEmptyView(null);
        }
    }


}
