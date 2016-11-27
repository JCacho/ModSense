package ru.pvolan.strip1.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.List;

import ru.pvolan.strip1.e.SensorData;
import ru.pvolan.strip1.h.CalendarHelper;

public class GraphView extends View {





    public GraphView (Context context) {
        super (context);
        init();
    }

    public GraphView (Context context, AttributeSet attrs) {
        super (context, attrs);
        init();
    }

    public GraphView (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init();
    }


    TextPaint textPaint1;
    TextPaint valueLabelPaint;
    TextPaint timeLabelPaint;
    Paint axisPaint;
    Paint linePaint;
    Paint pointPaint;


    private void init(){

        float dp = getResources ().getDisplayMetrics ().density;


        textPaint1 = new TextPaint ();
        textPaint1.setAntiAlias (true);
        textPaint1.setTextSize (12*dp);

        valueLabelPaint = new TextPaint ();
        valueLabelPaint.setAntiAlias (true);
        valueLabelPaint.setTextSize (12*dp);
        valueLabelPaint.setTextAlign (Paint.Align.RIGHT);


        timeLabelPaint = new TextPaint ();
        timeLabelPaint.setAntiAlias (true);
        timeLabelPaint.setTextAlign (Paint.Align.CENTER);
        timeLabelPaint.setTextSize (12*dp);

        axisPaint = new Paint ();
        axisPaint.setAntiAlias (true);
        axisPaint.setColor (0xffcccccc);
        axisPaint.setStrokeWidth (1*dp);

        linePaint = new Paint ();
        linePaint.setAntiAlias (true);
        linePaint.setColor (0xff666666);
        linePaint.setStrokeWidth (2*dp);

        pointPaint = new Paint ();
        pointPaint.setAntiAlias (true);
        pointPaint.setStyle (Paint.Style.FILL);
        pointPaint.setColor (0xffff0000);


        horizontalStep = 10*dp;
        verticalPadding = 20*dp;
        pointRadius = 2*dp;
        valueLabelHPadding = 5*dp;
        valueLabelYPadding = 2*dp;
        timeLabelPadding = 2*dp;
        graphRightPadding = 20*dp;

        pixPerSec = dpPerSec*dp;
    }


    private SensorData sensorData;

    private float maxValue;
    private float midValue;
    private float minValue;

    private long lastTimeUnix;

    private float horizontalStep;
    private float verticalPadding;
    private float pointRadius;
    private float valueLabelHPadding;
    private float valueLabelYPadding;
    private float timeLabelPadding;
    private float graphRightPadding;

    private float dpPerSec = 0.2f;
    private int xLabelStep = 300; //sec
    private float pixPerSec;


    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw (canvas);
        if(sensorData == null) return;

        drawAxisLine (canvas, maxValue);
        drawAxisLine (canvas, midValue);
        drawAxisLine (canvas, minValue);


        List<SensorData.Item> data = sensorData.getData ();

        for(int i = data.size () - 1; i>=1; i--){
            SensorData.Item item1 = data.get (i);
            SensorData.Item item2 = data.get (i-1);

            canvas.drawLine ( timeToX (item1.timestamp), valueToY (item1.value), timeToX (item2.timestamp), valueToY (item2.value), linePaint );
        }

        for(int i = data.size () - 1; i>=0; i--){
            SensorData.Item item = data.get (i);
            canvas.drawCircle ( timeToX (item.timestamp), valueToY (item.value), pointRadius, pointPaint );
        }


        for(int i = 0; i< 10; i++){
            drawXAxisLine(canvas, lastTimeUnix - (i*xLabelStep));
        }

    }


    private void drawAxisLine (Canvas canvas, float value) {

        int width = getWidth ();
        float y = valueToY (value);
        canvas.drawLine (0, y, width, y, axisPaint);
        canvas.drawText (Float.toString (value), width-valueLabelHPadding, y-valueLabelYPadding, valueLabelPaint);
    }



    private void drawXAxisLine (Canvas canvas, long timeUnix) {
        int height = getHeight ();
        float xPos = timeToX (timeUnix);
        canvas.drawLine (xPos, 0, xPos, height - verticalPadding, axisPaint);

        Calendar calendarTime = CalendarHelper.createFromUnix (timeUnix);
        canvas.drawText (CalendarHelper.format (calendarTime, "HH:mm:ss"), xPos, height-timeLabelPadding, timeLabelPaint);
    }


    private float valueToY(float value){

        int height = getHeight ();
        float pixPerValue = (height - 2*verticalPadding) / (maxValue - minValue);

        return height - verticalPadding - ( (value-minValue) * pixPerValue );
    }


    private float timeToX(long valueUnix){
        float lastTimeX = getWidth () - graphRightPadding;

        return lastTimeX - ( (lastTimeUnix - valueUnix) * pixPerSec );
    }

    private float timeToX(Calendar time){

        long valueUnix = CalendarHelper.toUnix (time);

        return timeToX (valueUnix);


    }


    public void setSensorData (SensorData sensorData) {
        this.sensorData = sensorData;


        List<SensorData.Item> data = sensorData.getData ();

        if(data.size () == 0){
            maxValue = 1;
            minValue = 0;
            lastTimeUnix = CalendarHelper.toUnix (Calendar.getInstance ());
        } else {

            minValue = data.get (0).value;
            maxValue = data.get (0).value + 1;

            for (SensorData.Item item : data) {
                if(item.value < minValue) minValue = item.value;
                if(item.value > maxValue) maxValue = item.value;
            }

            lastTimeUnix = CalendarHelper.toUnix (data.get ( data.size ()-1 ).timestamp);
        }

        midValue = (maxValue + minValue) / 2;



        invalidate ();
    }
}
