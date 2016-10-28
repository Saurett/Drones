package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.PreviewMemberAdapter;
import texium.mx.drones.fragments.inetrface.FragmentSearchListener;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.utils.Constants;


public class PreviewMemberFragment extends Fragment implements View.OnClickListener {

    private Tasks _TASK_INFO;
    private SoapObject soapObject;
    private static Users SESSION_DATA;

    static FragmentSearchListener activityListener;
    static List<TaskGallery> taskGallery;

    static RecyclerView member_list;
    private static LinearLayout emptyPreview;

    static PreviewMemberAdapter preview_member_adapter;

    private ProgressDialog pDialog;
    static FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_preview_member, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();

        member_list = (RecyclerView) view.findViewById(R.id.preview_member_list);
        emptyPreview = (LinearLayout) view.findViewById(R.id.empty_preview_member_gallery);

        preview_member_adapter = new PreviewMemberAdapter();
        preview_member_adapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        _TASK_INFO = (Tasks) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_ITEM_PREVIEW_GALLERY);
        wsTaskList.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityListener = (FragmentSearchListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " debe implementar ");
        }
    }

    private static void setEmptyView(View view) {
        member_list.setVisibility((preview_member_adapter.getItemCount() > 0) ? View.VISIBLE : View.INVISIBLE);
        emptyPreview.setVisibility((preview_member_adapter.getItemCount() > 0) ? View.INVISIBLE : View.VISIBLE);
        //activityListener.setEmptyDescription(1);
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
        taskGallery.remove(position);
        preview_member_adapter.removeItem(position);

        //Move to preview list
        if (position > 0) {
            activityListener.getDecodeGallery().setTaskGallery(taskGallery.get(((position > 0) ? position - 1 : 0)));
        } else {
            //activityListener.closeFragment(Constants.FRAGMENT_PREVIEW_MEMBER_GALLERY_TAG);
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
                    case Constants.WS_KEY_ITEM_PREVIEW_GALLERY:

                        tempGalleryList = new ArrayList<>();
                        tempGalleryList.addAll(activityListener.getPreviewMembers());

                        validOperation = true;

                       break;
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

                        Collections.sort(tempGalleryList, new Comparator() {
                            @Override
                            public int compare(Object softDrinkOne, Object softDrinkTwo) {
                                //use instanceof to verify the references are indeed of the type in question
                                return ((TaskGallery) softDrinkOne).getMember_name()
                                        .compareTo(((TaskGallery) softDrinkTwo).getMember_name());
                            }
                        });

                        taskGallery.addAll(tempGalleryList);
                        preview_member_adapter.addAll(taskGallery);

                        member_list.setAdapter(preview_member_adapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        member_list.setLayoutManager(linearLayoutManager);



                    } else {
                        Toast.makeText(getActivity(), "La busqueda no arroja resultados", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? "La busqueda no arroja resultados" : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setEmptyView(null);
        }
    }


}
