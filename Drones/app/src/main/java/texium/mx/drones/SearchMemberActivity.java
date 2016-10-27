package texium.mx.drones;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.fragments.PreviewMemberFragment;
import texium.mx.drones.fragments.SearchMemberFragment;
import texium.mx.drones.fragments.inetrface.FragmentSearchListener;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.Users;
import texium.mx.drones.utils.Constants;

/**
 * Created by texiumuser on 24/10/2016.
 */

public class SearchMemberActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, FragmentSearchListener, DialogInterface.OnClickListener {

    private static Tasks _TASK_INFO;
    private ProgressDialog pDialog;

    public SearchView searchView;
    public static FragmentManager fragmentManager;

    private static String _PROFILE_SEARCH;
    private static DecodeGallery _DECODE_GALLERY;

    private static List<TaskGallery> _PREVIEW_MEMBERS;
    private static List<Integer> actualMembers;
    private List<Integer> previewMembers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_member);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        try {
            _TASK_INFO = (Tasks) getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        setTitle(getTitle() + " - " + _TASK_INFO.getTask_tittle());

        fragmentManager = getSupportFragmentManager();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        _PROFILE_SEARCH = "";
        _DECODE_GALLERY = new DecodeGallery();
        _PREVIEW_MEMBERS = new ArrayList<>();
        actualMembers = new ArrayList<>();
        previewMembers = new ArrayList<>();

        try {
            List<Users> members = BDTasksManagerQuery.getMembers(getApplicationContext(),_TASK_INFO.getTask_id());

            for (Users user :
                    members) {
                actualMembers.add(user.getIdUser());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentTransaction search_gallery = fragmentManager.beginTransaction();
        search_gallery.add(R.id.list_member_container, new SearchMemberFragment(), Constants.FRAGMENT_SEARCH_MEMBER_GALLERY_TAG);
        search_gallery.commit();

        FragmentTransaction preview_gallery = fragmentManager.beginTransaction();
        preview_gallery.add(R.id.list_preview_member_container, new PreviewMemberFragment(), Constants.FRAGMENT_PREVIEW_MEMBER_GALLERY_TAG);
        preview_gallery.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_member, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Buscar ...");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:

                AsyncCallWS wsMember = new AsyncCallWS(Constants.WS_KEY_ITEM_SAVE_MEMBER);
                wsMember.execute();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {

            updateProfileSearch(query);

            closeFragment(Constants.FRAGMENT_SEARCH_MEMBER_GALLERY_TAG);

            replaceFragmentSearchFragment();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void updateProfileSearch(String query) {
        _PROFILE_SEARCH = query;
    }

    @Override
    public String getProfileSearch() {
        return _PROFILE_SEARCH;
    }

    @Override
    public DecodeGallery updateDecodeGallery(DecodeGallery oldGallery) {
        return _DECODE_GALLERY = oldGallery;
    }

    @Override
    public List<TaskGallery> getPreviewMembers() {
        return _PREVIEW_MEMBERS;
    }

    @Override
    public List<TaskGallery> updatePreviewMembers(List<TaskGallery> oldGallery) {
        return _PREVIEW_MEMBERS = oldGallery;
    }

    @Override
    public DecodeGallery getDecodeGallery() {
        return _DECODE_GALLERY;
    }

    @Override
    public void closeFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void replaceFragmentSearchFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.list_member_container, new SearchMemberFragment());
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void replaceFragmentPreviewFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.list_preview_member_container, new PreviewMemberFragment());
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void showQuestion(Integer idView) {

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        Boolean showAlert = false;

        Integer idMember = 0;

        try {
            idMember = _DECODE_GALLERY.getTaskGallery().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (idView) {
            case R.id.item_search_member_add:

                if (actualMembers.contains(idMember)) {

                    ad.setTitle(getString(R.string.default_title_alert_dialog));
                    ad.setMessage(getString(R.string.default_no_add_member_one));
                    ad.setCancelable(false);
                    ad.setPositiveButton(getString(R.string.default_positive_button), this);

                    showAlert = actualMembers.contains(idMember);

                } else if (previewMembers.contains(idMember)) {

                    ad.setTitle(getString(R.string.default_title_alert_dialog));
                    ad.setMessage(getString(R.string.default_no_add_member_two));
                    ad.setCancelable(false);
                    ad.setPositiveButton(getString(R.string.default_positive_button), this);

                    showAlert = previewMembers.contains(idMember);

                } else if (_TASK_INFO.getTask_user_id().equals(idMember)) {

                    ad.setTitle(getString(R.string.default_title_alert_dialog));
                    ad.setMessage(getString(R.string.default_no_add_member_three));
                    ad.setCancelable(false);
                    ad.setPositiveButton(getString(R.string.default_positive_button), this);

                    showAlert = _TASK_INFO.getTask_user_id().equals(idMember);
                } else {

                    AsyncCallWS wsMember = new AsyncCallWS(Constants.WS_KEY_ITEM_ADD_PREVIEW_MEMBER);
                    wsMember.execute();

                }

                break;
            case R.id.item_preview_member_delete:

                AsyncCallWS wsMember = new AsyncCallWS(Constants.WS_KEY_ITEM_DELETE_PREVIEW_MEMBER);
                wsMember.execute();

                break;
        }


        if (showAlert) ad.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    private class AsyncCallWS extends AsyncTask<Void, String, Boolean> {

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

            switch (webServiceOperation) {

                case Constants.WS_KEY_ITEM_SAVE_MEMBER:

                    pDialog = new ProgressDialog(SearchMemberActivity.this);
                    pDialog.setMessage(getString(R.string.default_save_members));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    break;
                default:

                    pDialog = new ProgressDialog(SearchMemberActivity.this);
                    pDialog.setMessage(getString(R.string.default_member_msg));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    break;

            }


        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_ITEM_SAVE_MEMBER:

                        for (TaskGallery taskGallery : _PREVIEW_MEMBERS) {
                            //TODO WEB SERVICE
                            previewMembers.remove(taskGallery.getId());
                            BDTasksManagerQuery.addMember(getApplicationContext(),taskGallery.getId(),_TASK_INFO.getTask_id());
                        }

                        validOperation = true;

                        break;
                    case Constants.WS_KEY_ITEM_ADD_MEMBER:
                        validOperation = false;
                        break;
                    case Constants.WS_KEY_ITEM_ADD_PREVIEW_MEMBER:

                        TaskGallery taskGallery = _DECODE_GALLERY.getTaskGallery();

                        previewMembers.add(taskGallery.getId());
                        _PREVIEW_MEMBERS.add(taskGallery);

                        validOperation = true;

                        break;
                    case Constants.WS_KEY_ITEM_DELETE_PREVIEW_MEMBER:

                        previewMembers.remove(_DECODE_GALLERY.getTaskGallery().getId());
                        _PREVIEW_MEMBERS.remove(_DECODE_GALLERY.getTaskGallery());

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
                pDialog.dismiss();
                if (success) {

                    switch (webServiceOperation) {
                        case Constants.WS_KEY_ITEM_SAVE_MEMBER:

                            String tempText = (textError.isEmpty() ? "Guardado correctamente" : textError);
                            Toast.makeText(getApplicationContext(), tempText, Toast.LENGTH_SHORT).show();

                            finish();

                            break;
                        case Constants.WS_KEY_ITEM_ADD_PREVIEW_MEMBER:

                            SearchMemberFragment.removeAt(_DECODE_GALLERY.getPosition());
                            closeFragment(Constants.FRAGMENT_PREVIEW_MEMBER_GALLERY_TAG);
                            replaceFragmentPreviewFragment();

                            break;
                        case Constants.WS_KEY_ITEM_DELETE_PREVIEW_MEMBER:

                            PreviewMemberFragment.removeAt(_DECODE_GALLERY.getPosition());

                            break;
                    }


                } else {
                    String tempText = (textError.isEmpty() ? "La busqueda no arroja resultados" : textError);
                    Toast.makeText(getApplicationContext(), tempText, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
