package ru.pvolan.strip1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.pvolan.SApp;
import ru.pvolan.strip1.e.SensorInfo;
import ru.pvolan.strip1.view.EW;
import ru.pvolan.strip1.view.ProgressFrame;
import ru.pvolan.strip1.view.SlidingTabLayout;


public class MainActivity extends AppCompatActivity {


    ViewPager vp;
    SlidingTabLayout slidingTabLayout;
    ProgressFrame progressFrame;
    StripsPagerAdapter adapter;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);

        setContentView (R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor (0xffffffff);

        vp = (ViewPager) findViewById (R.id.view_pager);
        vp.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                return true;
            }
        });
        slidingTabLayout = (SlidingTabLayout) findViewById (R.id.sliding_tab_layout);
        progressFrame = (ProgressFrame) findViewById (R.id.progress_frame);

        adapter = new StripsPagerAdapter (getSupportFragmentManager ());
        vp.setAdapter (adapter);
        slidingTabLayout.setViewPager (vp);
        updateSensors ();
    }



    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);

        MenuItem itemEditProfile = menu.findItem (R.id.action_refresh);
        itemEditProfile.setOnMenuItemClickListener (new MenuItem.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick (MenuItem item) {
                updateSensors ();
                return true;
            }
        });


        return super.onCreateOptionsMenu (menu);
    }





    private void updateSensors(){
        new LoadTask ().execute ();
    }



    private class LoadTask extends AsyncTask<String, Void, EW<List<SensorInfo>>> {

        public LoadTask () {
        }

        @Override
        protected void onPreExecute () {
            super.onPreExecute ();
            progressFrame.setVisibility (View.VISIBLE);
        }

        @Override
        protected EW<List<SensorInfo>> doInBackground (String... params) {
            try {
                return new EW<> (SApp.getApp ().getApi ().getSensors(), null);
            } catch (Exception e) {
                return new EW<List<SensorInfo>> (new ArrayList<SensorInfo> (), e);
            }
        }

        @Override
        protected void onPostExecute (EW<List<SensorInfo>> res) {
            super.onPostExecute (res);

            if(res.error != null){
                Toast.makeText (MainActivity.this, res.error.toString (), Toast.LENGTH_LONG).show ();
                Log.e ("Error", res.error.toString ());
                res.error.printStackTrace ();
            }


            progressFrame.setVisibility (View.GONE);

            adapter.setData ( res.data );
            slidingTabLayout.setViewPager (vp);

        }
    }

}
