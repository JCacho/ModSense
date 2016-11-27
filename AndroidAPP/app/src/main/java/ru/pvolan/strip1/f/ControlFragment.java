package ru.pvolan.strip1.f;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ru.pvolan.SApp;
import ru.pvolan.strip1.R;
import ru.pvolan.strip1.e.SensorInfo;
import ru.pvolan.strip1.view.ProgressFrame;
import ru.pvolan.strip1.view.SlidingTabLayout;

public class ControlFragment extends Fragment {


    public static ControlFragment getInstance (List<SensorInfo> sensors, String stripId) {
        Bundle args = new Bundle ();

        JSONArray sensorsJson = new JSONArray ();
        for (SensorInfo s : sensors) {
            sensorsJson.put (s.toJson ());
        }
        args.putString ("sensors", sensorsJson.toString ());
        args.putString ("stripId", stripId);


        ControlFragment f = new ControlFragment ();
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

    
    LinearLayout ln;

/*
    ViewPager vp;
    SlidingTabLayout slidingTabLayout;
    SensorsPagerAdapter adapter;
*/

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate (R.layout.control_fragment, container, false);

        ln = (LinearLayout) v.findViewById (R.id._linearlayout);
        
        
        List<SensorInfo> sensors = getSensors ();
        List<String> sockets = new ArrayList<> ();
        for (SensorInfo s : sensors) {
            if(!sockets.contains (s.getSocketId ())) sockets.add (s.getSocketId ());
        }


        sockets.add (0,"0");

        for (final String socketId : sockets) {
            LinearLayout ls = new LinearLayout (container.getContext ());
            ls.setLayoutParams (new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ln.addView (ls);


            final ProgressFrame pon = new ProgressFrame (container.getContext ());
            pon.setVisibility (View.GONE);
            pon.setLayoutParams (new LinearLayout.LayoutParams (0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            ls.addView (pon);
            

            final Button bon = new Button (container.getContext ());
            if(socketId.equals ("0")) bon.setText ("Master on");
            else bon.setText ("Socket " + socketId + " on");
            bon.setLayoutParams (new LinearLayout.LayoutParams (0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            ls.addView (bon);
            bon.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    doMagic(bon, pon, getStripId (), socketId, true);
                }
            });


            final ProgressFrame poff = new ProgressFrame (container.getContext ());
            poff.setVisibility (View.GONE);
            poff.setLayoutParams (new LinearLayout.LayoutParams (0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            ls.addView (poff);

            final Button boff = new Button (container.getContext ());
            if(socketId.equals ("0")) boff.setText ("Master off");
            else boff.setText ("Socket " + socketId + " off");
            boff.setLayoutParams (new LinearLayout.LayoutParams (0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            ls.addView (boff);
            boff.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    doMagic(boff, poff, getStripId (), socketId, false);
                }
            });
        }



        return v;
    }

    private void doMagic (final Button bon, final ProgressFrame pon, final String stripId, final String socketId, final boolean on) {
        new AsyncTask<Void, Void, Exception> (){

            @Override
            protected void onPreExecute () {
                super.onPreExecute ();
                bon.setVisibility (View.GONE);
                pon.setVisibility (View.VISIBLE);
            }

            @Override
            protected Exception doInBackground (Void... params) {
                try {
                    SApp.getApp ().getApi ().setOn(stripId, socketId, on);
                } catch (Exception e) {
                    return e;
                }
                return null;
            }


            @Override
            protected void onPostExecute (Exception e) {
                super.onPostExecute (e);

                bon.setVisibility (View.VISIBLE);
                pon.setVisibility (View.GONE);

                if(e!=null) Toast.makeText (getContext (), e.getMessage (), Toast.LENGTH_LONG).show ();
            }
        }.execute ();
    }



    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        ln = null;
    }

}
