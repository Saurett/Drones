package texium.mx.drones;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import texium.mx.drones.fragments.PhotoGalleryFragment;
import texium.mx.drones.utils.Constants;

public class TabActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getTitle() + " / Titulo de la tarea");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        FragmentManager manager = getSupportFragmentManager();

        removeAllFragment(manager);

        int numTab = tab.getPosition();
        switch (numTab) {
            case 0:
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.add(R.id.photo_gallery_fragment_container, new PhotoGalleryFragment(), Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                fragmentTransaction.commit();
                break;
            case 1:
                break;
            case 2:
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        FragmentManager manager = getSupportFragmentManager();

        removeAllFragment(manager);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        FragmentManager manager = getSupportFragmentManager();

        removeAllFragment(manager);

        int numTab = tab.getPosition();
        switch (numTab) {
            case 0:
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.add(R.id.photo_gallery_fragment_container, new PhotoGalleryFragment(), Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                fragmentTransaction.commit();
                break;
            case 1:
                break;
            case 2:
                break;
        }

    }

    public void removeAllFragment(FragmentManager fragmentManager) {

        Fragment photo = fragmentManager.findFragmentByTag(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
        if (null != photo) {
            fragmentManager.beginTransaction().remove(photo).commit();
        }

        Fragment video = fragmentManager.findFragmentByTag(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
        if (null != video) {
            fragmentManager.beginTransaction().remove(video).commit();
        }

        Fragment document = fragmentManager.findFragmentByTag(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
        if (null != document) {
            fragmentManager.beginTransaction().remove(document).commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

           int numero = getArguments().getInt(ARG_SECTION_NUMBER);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {

                case 0:
                    PhotoGalleryFragment pgf = new PhotoGalleryFragment();
                    return pgf;
                case 1:
                    return PlaceholderFragment.newInstance(position + 1);
                case 2:
                    PhotoGalleryFragment pgf3 = new PhotoGalleryFragment();
                    return pgf3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return Constants.IMAGE_SECTION;
                case 1:
                    return Constants.VIDEO_SECTION;
                case 2:
                    return Constants.DOCUMENT_SECTION;
            }
            return null;
        }
    }
}
