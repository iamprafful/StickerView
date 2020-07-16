package tech.greedylabs.stickerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements  View.OnTouchListener{

    MyCustomView myCustomView;
    RelativeLayout stickerView;
    private int _xDelta;
    private int _yDelta;
    private ViewGroup mRootLayout;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    ImageButton eraserButton, closeButton, flipButton;
    int eraserState = 0;
    boolean erasable = false;
    MenuItem done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myCustomView = new MyCustomView(MainActivity.this);
        stickerView = findViewById(R.id.stickerView);
        mRootLayout = (ViewGroup) findViewById(R.id.main_activity);
        stickerView.addView(myCustomView);
        eraserButton = findViewById(R.id.eraser_button);
        closeButton = findViewById(R.id.closeButton);
        flipButton = findViewById(R.id.flipButton);
        eraserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                if(eraserState==0)
                {
                    erasable = true;
                    flipButton.setVisibility(View.GONE);
                    eraserButton.setVisibility(View.GONE);
                    closeButton.setVisibility(View.GONE);
                    eraserState =1;
                    done.setVisible(true);
                }
                myCustomView.setErase(erasable);
            }
        });
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myCustomView.getRotationY()==180)
                    myCustomView.setRotationY(0);
                else
                    myCustomView.setRotationY(180);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerView.setVisibility(View.GONE);
            }
        });
        stickerView.setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        done = menu.findItem(R.id.eraser_off);
        done.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.eraser_off)
        {
            erasable = false;
            eraserState=0;
            done.setVisible(false);
            myCustomView.setErase(erasable);
            flipButton.setVisibility(View.VISIBLE);
            eraserButton.setVisibility(View.VISIBLE);
            closeButton.setVisibility(View.VISIBLE);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        view.performClick();
        if(!erasable)
        {
            flipButton.setVisibility(View.VISIBLE);
            eraserButton.setVisibility(View.VISIBLE);
            closeButton.setVisibility(View.VISIBLE);
            stickerView.setBackgroundResource(R.drawable.border);
            mScaleGestureDetector.onTouchEvent(event);
            final int X = (int) event.getRawX();
            final int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                            .getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            mRootLayout.invalidate();
        }
        return !erasable;
    }


    public void tappedOutside(View view) {
        if(!erasable)
        {
            flipButton.setVisibility(View.GONE);
            eraserButton.setVisibility(View.GONE);
            closeButton.setVisibility(View.GONE);
            stickerView.setBackgroundResource(R.drawable.empty_border);
        }
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            stickerView.setScaleX(mScaleFactor);
            stickerView.setScaleY(mScaleFactor);
            return true;
        }
    }
}