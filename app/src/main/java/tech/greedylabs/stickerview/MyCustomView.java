package tech.greedylabs.stickerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MyCustomView extends View {
    private Bitmap mSourceBitmap;
    private Canvas mSourceCanvas = new Canvas();
    private Paint mDestPaint = new Paint();
    private Path mDestPath = new Path();
    boolean erase = false;


    public void setErase(boolean erase) {
        this.erase = erase;
    }

    public MyCustomView(Context context) {
        super(context);
        //convert drawable file into bitmap
        Bitmap rawBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sticker);

        //convert bitmap into mutable bitmap
        int centreX = (dpToPixels(getResources().getDisplayMetrics(), 200)  - rawBitmap.getWidth()) /2;
        int centreY = (dpToPixels(getResources().getDisplayMetrics(), 200)  - rawBitmap.getHeight()) /2;
        mSourceBitmap = Bitmap.createBitmap(rawBitmap.getWidth(), rawBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        float left = -25;
        float top = -25;
        RectF dst = new RectF(left, top, left + dpToPixels(getResources().getDisplayMetrics(), 200) , top + dpToPixels(getResources().getDisplayMetrics(), 200) );

        mSourceCanvas.setBitmap(mSourceBitmap);
        mSourceCanvas.drawBitmap(rawBitmap, null, dst, null);

        mDestPaint.setAlpha(0);
        mDestPaint.setAntiAlias(true);
        mDestPaint.setStyle(Paint.Style.STROKE);
        mDestPaint.setStrokeJoin(Paint.Join.ROUND);
        mDestPaint.setStrokeCap(Paint.Cap.ROUND);
        mDestPaint.setStrokeWidth(50);
        mDestPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }
    public static int dpToPixels(final DisplayMetrics display_metrics, final float dps) {
        final float scale = display_metrics.density;
        return (int) (dps * scale + 0.5f);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        if(true)
        {
            //Draw path
            mSourceCanvas.drawPath(mDestPath, mDestPaint);

            //Draw bitmap
            canvas.drawBitmap(mSourceBitmap, 0, -0, null);

            super.onDraw(canvas);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(erase)
        {
            float xPos = event.getX();
            float yPos = event.getY();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    mDestPath.moveTo(xPos, yPos);
                    break;

                case MotionEvent.ACTION_MOVE:
                    mDestPath.lineTo(xPos, yPos);
                    break;

                default:
                    return false;
            }

            invalidate();
        }
        return erase;
    }
}
