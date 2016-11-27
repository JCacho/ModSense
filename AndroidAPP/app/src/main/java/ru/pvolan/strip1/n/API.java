package ru.pvolan.strip1.n;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import ru.pvolan.SApp;
import ru.pvolan.strip1.e.SensorData;
import ru.pvolan.strip1.e.SensorInfo;
import ru.pvolan.strip1.h.CalendarHelper;

public class API {


    public API (Context appContext) {
    }

    public SensorData getSensorData (SensorInfo sensor) throws Exception {

        String resp = getSync ( String.format ("http://modsense.herokuapp.com/sdata?socketid=%s&type=%s&stripid=%s",
                sensor.getSocketId (),
                sensor.getType (),
                sensor.getStripId ()));

        JSONObject json = new JSONObject (resp);

        JSONArray dataValuesJson = json.getJSONArray ("data");
        List<SensorData.Item> values = new ArrayList<SensorData.Item> ();
        for (int j = 0; j < dataValuesJson.length (); j++) {
            JSONObject valueJson = dataValuesJson.getJSONObject (j);
            values.add (new SensorData.Item (
                    CalendarHelper.createFromUnix (valueJson.getLong ("time")),
                    (float)(valueJson.getDouble ("value"))
            ));
        }

        Collections.sort (values, new Comparator<SensorData.Item> () {
            @Override
            public int compare (SensorData.Item lhs, SensorData.Item rhs) {
                long diff = lhs.timestamp.getTimeInMillis () - rhs.timestamp.getTimeInMillis ();
                if(diff < 0) return -1;
                if(diff == 0) return 0;
                return 1;
            }
        });

        return new SensorData (values);
    }

    public List<SensorInfo> getSensors () throws Exception {


        String resp = getSync ("http://modsense.herokuapp.com/types");

        JSONObject json = new JSONObject (resp);

        JSONArray typesJson = json.getJSONArray ("sensortypes");

        List<SensorInfo> result = new ArrayList<> (typesJson.length ());
        for (int i = 0; i < typesJson.length (); i++) {
            JSONObject o = typesJson.getJSONObject (i);
            result.add (new SensorInfo (o.getString ("stripid"), o.getString ("socketid"),  o.getString ("type")));
        }

        return result;
    }






    private String getSync(String url) throws Exception {
        Log.i("NET", "------REQUEST");

        HttpURLConnection conn = (HttpURLConnection) new URL (url).openConnection();

        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(false);

        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        conn.setDoInput(true);
        conn.setChunkedStreamingMode(1024);



        Log.i("NET", "------REQUEST END" );

        Log.i("NET", "------RESPONSE" );



        //Status
        int statusCode = conn.getResponseCode();
        String responseMessage = conn.getResponseMessage();
        Log.i("NET", "Status: " + statusCode + " " + responseMessage);


        //Process data ----------

        String strContent = getContentOrEmpty(conn);
        Log.i("NET", "Content: " + strContent);

        if(statusCode != 200)
        {
            throw new Exception ("code " + statusCode );
        }


        Log.i("NET", "------RESPONSE END" );

        return strContent;
    }



    private String getContentOrEmpty(HttpURLConnection conn) throws IOException {

        InputStream is;
        try {
            is = conn.getInputStream();
        }catch (IOException exc){
            is = conn.getErrorStream();
        }
        String strContent = convertBody(is);
        is.close();
        return strContent;
    }


    public static String convertBody(InputStream in) throws IOException
    {
        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ( (bytesRead = in.read(buffer)) != -1 ) {
            baos.write(buffer, 0, bytesRead);
        }
        return new String(baos.toByteArray(), "utf-8");
    }





    public void setOn (String stripId, String socketId, boolean on) throws Exception {
        Log.i("NET", "------REQUEST");
        String status = "" + socketId + (on?"1":"0");
        String yrl = String.format ("http://modsense.herokuapp.com/status?stripname=%s&status=%s", stripId, status);
        HttpURLConnection conn = (HttpURLConnection) new URL (yrl).openConnection();

        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(false);

        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        conn.setDoInput(true);
        conn.setChunkedStreamingMode(1024);



        Log.i("NET", "------REQUEST END" );

        Log.i("NET", "------RESPONSE" );



        //Status
        int statusCode = conn.getResponseCode();
        String responseMessage = conn.getResponseMessage();
        Log.i("NET", "Status: " + statusCode + " " + responseMessage);


        //Process data ----------

        String strContent = getContentOrEmpty(conn);
        Log.i("NET", "Content: " + strContent);

        if(statusCode != 200)
        {
            throw new Exception ("code " + statusCode );
        }


        Log.i("NET", "------RESPONSE END" );


    }
}
