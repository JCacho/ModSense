package ru.pvolan.strip1.f.power;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.pvolan.SApp;
import ru.pvolan.strip1.R;
import ru.pvolan.strip1.e.SensorData;
import ru.pvolan.strip1.e.SensorInfo;
import ru.pvolan.strip1.view.EW;
import ru.pvolan.strip1.view.GraphView;
import ru.pvolan.strip1.view.ProgressFrame;

public class PowerFragment extends android.support.v4.app.Fragment {


    public static PowerFragment getInstance (SensorInfo info) {
        Bundle args = new Bundle ();
        args.putString ("stripid", info.getStripId ());
        args.putString ("socketid", info.getSocketId ());
        args.putString ("type", info.getType ());
        PowerFragment f = new PowerFragment ();
        f.setArguments (args);
        return f;
    }


    private SensorInfo getSensorInfo(){
        return new SensorInfo (
                getArguments ().getString ("stripid"),
                getArguments ().getString ("socketid"),
                getArguments ().getString ("type")
        );
    }


    GraphView graphView;
    ProgressFrame progressFrame;


    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate (R.layout.power_f_layout, container, false);
        graphView = (GraphView) v.findViewById (R.id.graph_view);
        progressFrame = (ProgressFrame) v.findViewById (R.id.progress_frame);



        (new LoadTask (true)).execute (getSensorInfo ());



        return v;
    }




    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        graphView = null;
        progressFrame = null;
    }


    private class LoadTask extends AsyncTask<SensorInfo, Void, EW<SensorData>> {

        private boolean showProgress;

        public LoadTask (boolean showProgress) {
            this.showProgress = showProgress;
        }

        @Override
        protected void onPreExecute () {
            super.onPreExecute ();
            if(showProgress) progressFrame.setVisibility (View.VISIBLE);
        }

        @Override
        protected EW<SensorData> doInBackground (SensorInfo... params) {
            try {
                return new EW<> (SApp.getApp ().getApi ().getSensorData(params[0]), null);
            } catch (Exception e) {
                return new EW<> (new SensorData (new ArrayList<SensorData.Item> ()), e);
            }
        }

        @Override
        protected void onPostExecute (EW<SensorData> res) {
            super.onPostExecute (res);

            if(res.error != null) {
                Toast.makeText (SApp.getApp (), res.error.toString (), Toast.LENGTH_LONG).show ();
                Log.e ("Error", res.error.toString ());
                res.error.printStackTrace ();
            }

            if(graphView != null) {
                progressFrame.setVisibility (View.GONE);

                graphView.setSensorData (res.data);
            }

            (new Handler ()).postDelayed (new Runnable () {
                @Override
                public void run () {
                    if(graphView != null) (new LoadTask (false)).execute (getSensorInfo ());
                }
            }, 15000);
        }
    }

}
