package ru.pvolan.strip1.f;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class UnknownSensorFragment extends Fragment {


    public static UnknownSensorFragment getInstance () {
        Bundle args = new Bundle ();
        UnknownSensorFragment f = new UnknownSensorFragment ();
        f.setArguments (args);
        return f;
    }




    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView tv = new TextView (getContext ());
        tv.setText ("Unknown sensor");

        return tv;
    }


}
