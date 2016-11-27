package ru.pvolan.strip1.e;

import java.util.Calendar;
import java.util.List;

import ru.pvolan.strip1.h.CalendarHelper;

public class SensorData {

    private List<Item> data;


    public SensorData (List<Item> data) {
        this.data = data;
    }

    public static class Item{
        public Item (Calendar timestamp, float value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public final Calendar timestamp;
        public final float value;

        @Override
        public String toString () {
            return "Item{" +
                    "timestamp=" + CalendarHelper.toNiceString (timestamp) +
                    ", value=" + value +
                    '}';
        }
    }


    public List<Item> getData () {
        return data;
    }

    @Override
    public String toString () {
        return "SensorData{" +
                "data=" + data +
                '}';
    }
}
