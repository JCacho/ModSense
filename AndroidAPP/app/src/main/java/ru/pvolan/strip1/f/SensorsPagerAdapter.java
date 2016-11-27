package ru.pvolan.strip1.f;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.pvolan.strip1.e.SensorInfo;
import ru.pvolan.strip1.f.power.PowerFragment;
import ru.pvolan.strip1.f.temo.TemperatureFragment;

public class SensorsPagerAdapter extends FragmentPagerAdapter {


    List<SensorInfo> sensorsWeNeed = new ArrayList<> ();
    String stripId;


    public SensorsPagerAdapter (FragmentManager fm) {
        super (fm);
    }

    @Override
    public int getCount () {
        return sensorsWeNeed.size () + 1;

    }

    @Override
    public CharSequence getPageTitle (int position) {

        if(position == 0) return "Controls";

        SensorInfo sensorInfo = sensorsWeNeed.get (position-1);
        return sensorInfo.getSocketId () + " " + sensorInfo.getType ();
    }


    @Override
    public Fragment getItem (int position) {



        if(position == 0) return ControlFragment.getInstance(sensorsWeNeed, stripId);

        SensorInfo sensorInfo = sensorsWeNeed.get (position-1);
        String type = sensorInfo.getType ();

        if(type.equals ("temp")) return TemperatureFragment.getInstance(sensorInfo);
        if(type.equals ("current")) return PowerFragment.getInstance(sensorInfo);

        return UnknownSensorFragment.getInstance ();
    }

    public void setSensorsWeNeed (List<SensorInfo> sensors, String stripId) {
        sensorsWeNeed = new ArrayList<> ();

        this.stripId = stripId;

        for (SensorInfo s : sensors) {
            if(s.getStripId ().equals (stripId)){
                sensorsWeNeed.add (s);
            }
        }

        notifyDataSetChanged ();
    }

    @Override
    public int getItemPosition (Object object) {
        return POSITION_NONE;
    }
}
