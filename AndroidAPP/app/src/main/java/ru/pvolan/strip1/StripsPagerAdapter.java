package ru.pvolan.strip1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.pvolan.strip1.e.SensorInfo;
import ru.pvolan.strip1.f.StrpFragment;
import ru.pvolan.strip1.f.UnknownSensorFragment;
import ru.pvolan.strip1.f.power.PowerFragment;
import ru.pvolan.strip1.f.temo.TemperatureFragment;

public class StripsPagerAdapter extends FragmentPagerAdapter {


    List<SensorInfo> data = new ArrayList<> ();

    List<String> strips = new ArrayList<> ();


    public StripsPagerAdapter (FragmentManager fm) {
        super (fm);
    }

    @Override
    public int getCount () {
        return strips.size ();
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return strips.get (position);
    }


    @Override
    public Fragment getItem (int position) {
        return StrpFragment.getInstance (data, strips.get (position));
    }

    public void setData (List<SensorInfo> sensors) {
        data = sensors;

        strips = new ArrayList<> ();

        for (SensorInfo s : data) {
            String stripId = s.getStripId ();
            if(!strips.contains (stripId)) {
                strips.add (stripId);
            }
        }

        notifyDataSetChanged ();
    }

    @Override
    public int getItemPosition (Object object) {
        return POSITION_NONE;
    }
}
