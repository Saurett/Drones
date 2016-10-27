package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ksoap2.serialization.SoapObject;

import texium.mx.drones.R;
import texium.mx.drones.fragments.inetrface.FragmentGalleryListener;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.utils.Constants;


public class MemberGalleryDescriptionFragment extends Fragment implements DialogInterface.OnClickListener {

    private SoapObject soapObject;
    private Tasks _TASK_INFO;
    private static TaskGallery _DESCRIPTION;

    static FragmentGalleryListener activityListener;
    private ProgressDialog pDialog;

    private static TextView memberTitle, memberDescription;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_member, container, false);

        memberTitle = (TextView) view.findViewById(R.id.member_title);
        memberDescription = (TextView) view.findViewById(R.id.member_description);

        memberTitle.setText(_TASK_INFO.getTask_tittle());
        memberDescription.setText(_TASK_INFO.getTask_content());

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        _DESCRIPTION = (TaskGallery) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY_DESCRIPTION);
        _TASK_INFO = (Tasks) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);
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

    private void showQuestion(int item) {

        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        switch (item) {

            case Constants.ITEM_SYNC_SERVER_DEFAULT:

                ad.setTitle(getString(R.string.default_title_alert_dialog));
                ad.setMessage(getString(R.string.default_no_edit_msg));
                ad.setCancelable(false);
                ad.setPositiveButton(getString(R.string.default_positive_button), this);

                break;
            default:

                ad.setTitle(getString(R.string.default_title_alert_dialog));
                ad.setMessage(getString(R.string.default_alert_empty_unique_description));
                ad.setCancelable(false);
                ad.setPositiveButton(getString(R.string.default_positive_button), this);

                break;
        }


        ad.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:

                break;
            case DialogInterface.BUTTON_NEGATIVE:

                break;
        }
    }


}
