package ru.pvolan.strip1.f;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ru.pvolan.SApp;
import ru.pvolan.strip1.R;
import ru.pvolan.strip1.StripsPagerAdapter;
import ru.pvolan.strip1.e.SensorData;
import ru.pvolan.strip1.e.SensorInfo;
import ru.pvolan.strip1.view.EW;
import ru.pvolan.strip1.view.GraphView;
import ru.pvolan.strip1.view.ProgressFrame;
import ru.pvolan.strip1.view.SlidingTabLayout;

public class StrpFragment extends Fragment {


    public static StrpFragment getInstance (List<SensorInfo> sensors, String stripId) {
        Bundle args = new Bundle ();

        JSONArray sensorsJson = new JSONArray ();
        for (SensorInfo s : sensors) {
            sensorsJson.put (s.toJson ());
        }
        args.putString ("sensors", sensorsJson.toString ());
        args.putString ("stripId", stripId);


        StrpFragment f = new StrpFragment ();
        f.setArguments (args);
        return f;
    }


    private List<SensorInfo> getSensors(){
        try {
            JSONArray ar = new JSONArray (getArguments ().getString ("sensors"));
            List<SensorInfo> sensors  = new ArrayList<> ();

            for (int i = 0; i < ar.length (); i++) {
                sensors.add (new SensorInfo (ar.getJSONObject (i)));
            }

            return sensors;
        } catch (JSONException e) {
            throw new RuntimeException (e);
        }
    }

    private String getStripId(){
        return getArguments ().getString ("stripId");
    }


    ViewPager vp;
    SlidingTabLayout slidingTabLayout;
    SensorsPagerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate (R.layout.strip_fragment, container, false);

        vp = (ViewPager) v.findViewById (R.id.view_pager);
        vp.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                return true;
            }
        });
        slidingTabLayout = (SlidingTabLayout) v.findViewById (R.id.sliding_tab_layout);


        adapter = new SensorsPagerAdapter (getChildFragmentManager ());
        adapter.setSensorsWeNeed (getSensors (), getStripId ());
        vp.setAdapter (adapter);

        slidingTabLayout.setViewPager (vp);
        slidingTabLayout.setDistributeEvenly (true);




        return v;
    }


    @Override
    public void onResume () {
        super.onResume ();
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        slidingTabLayout = null;
        vp = null;
        adapter = null;
    }

}
