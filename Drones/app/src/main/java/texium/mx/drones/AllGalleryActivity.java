package texium.mx.drones;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import texium.mx.drones.fragments.PhotoGalleryDescriptionFragment;
import texium.mx.drones.fragments.PhotoGalleryFragment;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.utils.Constants;

public class AllGalleryActivity extends AppCompatActivity {

    private static Tasks TASK_INFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_gallery);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        try {
            TASK_INFO = (Tasks) getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
            getIntent().putExtra(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY, TASK_INFO);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getTitle() + " - " + TASK_INFO.getTask_tittle());

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction list_gallery = fragmentManager.beginTransaction();
        list_gallery.add(R.id.list_gallery_container, new PhotoGalleryFragment(), Constants.FRAGMENT_PHOTO_GALLERY_LIST_TAG);
        list_gallery.commit();

        FragmentManager fmDescription = getSupportFragmentManager();
        FragmentTransaction description = fmDescription.beginTransaction();
        description.add(R.id.detail_gallery_container, new PhotoGalleryDescriptionFragment(), Constants.FRAGMENT_PHOTO_GALLERY_TAG);
        description.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
