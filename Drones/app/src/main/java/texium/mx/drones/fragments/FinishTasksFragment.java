package texium.mx.drones.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import texium.mx.drones.R;
import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.utils.Constants;


public class FinishTasksFragment extends Fragment implements View.OnClickListener {

    static FragmentTaskListener activityListener;

    private Button send_task_button,close_window_button,next_task_button,back_task_button;
    private TextView title_task_window, content_task_window,comment_task_window;

    static Map<Long,Object> taskToken = new HashMap<>();

    static private int _ACTUAL_POSITION;
    static private int _ACTUAL_COUNT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finish_tasks,container,false);

        close_window_button = (Button) view.findViewById(R.id.close_window_button);
        send_task_button = (Button) view.findViewById(R.id.send_task_button);
        next_task_button = (Button) view.findViewById(R.id.next_task_button);
        back_task_button = (Button) view.findViewById(R.id.back_task_button);

        title_task_window = (TextView) view.findViewById(R.id.title_task_window);
        content_task_window = (TextView) view.findViewById(R.id.content_task_window);
        comment_task_window = (TextView) view.findViewById(R.id.comment_task_window);

        back_task_button.setOnClickListener(this);
        send_task_button.setOnClickListener(this);
        close_window_button.setOnClickListener(this);
        next_task_button.setOnClickListener(this);


        View tokenView = (View) taskToken.get(1L);
        TaskListAdapter tokenAdapter = (TaskListAdapter) taskToken.get(2L);
        Tasks tokenTask = (Tasks) taskToken.get(3L);
        TasksDecode tokenTaskDecode = (TasksDecode) taskToken.get(4L);

        title_task_window.setText(tokenTask.getTask_tittle());
        content_task_window.setText(tokenTask.getTask_content());

        if (_ACTUAL_POSITION == _ACTUAL_COUNT) {
            next_task_button.setEnabled(false);
            next_task_button.setVisibility(View.INVISIBLE);
        }

        if ((_ACTUAL_POSITION > 0) && (_ACTUAL_POSITION < _ACTUAL_COUNT)) {
            next_task_button.setEnabled(true);
            next_task_button.setVisibility(View.VISIBLE);

            back_task_button.setEnabled(true);
            back_task_button.setVisibility(View.VISIBLE);

        }

        if (_ACTUAL_POSITION <= 0) {
            back_task_button.setEnabled(false);
            back_task_button.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        if (taskToken.isEmpty()) {
            taskToken = activityListener.getToken();

            TaskListAdapter backAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
            TasksDecode taskDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

            _ACTUAL_POSITION = taskDecode.getTask_position();
            _ACTUAL_COUNT = backAdapter.getItemCount() - 1;
        }
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
            case R.id.send_task_button:

                SpannableStringBuilder ssb = (SpannableStringBuilder) comment_task_window.getText();

                TaskListAdapter sendAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                Tasks sendTask = (Tasks) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS);
                TasksDecode sendDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

                sendDecode.setTask_coment(ssb.toString());

                taskToken = new HashMap<>();

                activityListener.taskActions(v, sendAdapter, sendTask, sendDecode);
                break;
            case R.id.close_window_button:

                taskToken = new HashMap<>();

                activityListener.closeActiveTaskFragment(v);
                activityListener.clearTaskToken();
                break;
            case R.id.back_task_button:

                TaskListAdapter backAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                TasksDecode backDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

                _ACTUAL_POSITION--;

                if (_ACTUAL_POSITION <= 0) {
                    back_task_button.setEnabled(false);
                    back_task_button.setVisibility(View.INVISIBLE);
                }

                if ((_ACTUAL_POSITION > 0) && (_ACTUAL_POSITION < _ACTUAL_COUNT)) {
                    next_task_button.setEnabled(true);
                    next_task_button.setVisibility(View.VISIBLE);

                    //back_task_button.setEnabled(true);
                    //back_task_button.setVisibility(View.VISIBLE);

                }

                Tasks actualBackTask = backAdapter.getItemByPosition(_ACTUAL_POSITION);
                backDecode.setTask_position(_ACTUAL_POSITION);

                title_task_window.setText(actualBackTask.getTask_tittle());
                content_task_window.setText(actualBackTask.getTask_content());
                comment_task_window.setText(new String());

                taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS, actualBackTask);
                taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE,backDecode);

                break;
            case R.id.next_task_button:

                TaskListAdapter nextAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                TasksDecode nextDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

                _ACTUAL_POSITION++;


                if (_ACTUAL_POSITION == _ACTUAL_COUNT) {
                    next_task_button.setEnabled(false);
                    next_task_button.setVisibility(View.INVISIBLE);
                }

                if ((_ACTUAL_POSITION > 0) && (_ACTUAL_POSITION < _ACTUAL_COUNT)) {
                    //next_task_button.setEnabled(true);
                    //next_task_button.setVisibility(View.VISIBLE);

                    back_task_button.setEnabled(true);
                    back_task_button.setVisibility(View.VISIBLE);

                }

                Tasks actualNextTask = nextAdapter.getItemByPosition(_ACTUAL_POSITION);
                nextDecode.setTask_position(_ACTUAL_POSITION);

                title_task_window.setText(actualNextTask.getTask_tittle());
                content_task_window.setText(actualNextTask.getTask_content());
                comment_task_window.setText(new String());

                taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS, actualNextTask);
                taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE,nextDecode);
                break;
            default:
                break;
        }
    }
}
