package texium.mx.drones.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import texium.mx.drones.R;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;


public class LegalDescriptionFragment extends Fragment{

    static FragmentTaskListener activityListener;

    private static EditText description;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_legal_description, container, false);

        description = (EditText) view.findViewById(R.id.legal_description);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        activityListener.addTasksListMarkers(new ArrayList<Tasks>());

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

}
