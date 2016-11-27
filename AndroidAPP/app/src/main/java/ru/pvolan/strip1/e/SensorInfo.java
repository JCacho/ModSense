package ru.pvolan.strip1.e;

import org.json.JSONException;
import org.json.JSONObject;

public class SensorInfo {

    private String stripId;
    private String socketId;
    private String type;

    public SensorInfo (JSONObject json) {
        try {
            this.stripId = json.getString ("stripId");
            this.socketId = json.getString ("socketId");
            this.type = json.getString ("type");
        } catch (JSONException e) {
            throw new RuntimeException (e);
        }
    }

    public SensorInfo (String stripId, String socketId, String type) {
        this.stripId = stripId;
        this.socketId = socketId;
        this.type = type;
    }

    public String getStripId () {
        return stripId;
    }

    public String getSocketId () {
        return socketId;
    }

    public String getType () {
        return type;
    }

    public JSONObject toJson(){
        try {
            JSONObject jsonObject = new JSONObject ();
            jsonObject.put ("stripId", stripId);
            jsonObject.put ("socketId", socketId);
            jsonObject.put ("type", type);
            return jsonObject;
        } catch (JSONException e) {
            throw new RuntimeException (e);
        }
    }



}
