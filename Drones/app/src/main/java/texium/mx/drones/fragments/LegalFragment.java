package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.ArrayList;

import texium.mx.drones.R;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.utils.Constants;


public class LegalFragment extends Fragment implements View.OnClickListener {

    static FragmentTaskListener activityListener;

    private static Switch closure;
    private static RadioGroup radioGroup;

    private static Users SESSION_DATA;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_legal, container, false);

        closure = (Switch) view.findViewById(R.id.switch_closure);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);

        closure.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        activityListener.addTasksListMarkers(new ArrayList<Tasks>());
        SESSION_DATA = (Users) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityListener = (FragmentTaskListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " debe implementar TaskListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.switch_closure:

                boolean checked = ((Switch) v).isChecked();

                radioGroup.setVisibility((checked) ? View.VISIBLE : View.INVISIBLE);

                break;

            default:
                break;
        }
    }

}