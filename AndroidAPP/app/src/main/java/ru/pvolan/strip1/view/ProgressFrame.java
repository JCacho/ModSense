package ru.pvolan.strip1.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import ru.pvolan.strip1.R;


public class ProgressFrame extends FrameLayout{

    private ProgressBar progressBar;

    public ProgressFrame (Context context) {
        super(context);
        init();
    }

    public ProgressFrame (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressFrame (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setBackgroundColor (0xffffffff);

        View v = inflater.inflate(R.layout.indeterminate_progress_bar, this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    protected ProgressBar getProgressBar() {
        return progressBar;
    }
}
