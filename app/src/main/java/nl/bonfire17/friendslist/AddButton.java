package nl.bonfire17.friendslist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class AddButton extends android.support.v7.widget.AppCompatButton {

    private Paint paint;

    public AddButton(Context context) {
        super(context);
        init();
    }

    public AddButton(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setARGB(0, 255, 255,255);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(100, 100, 100, paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(87.5f,25, 112.5f, 150, paint);
        canvas.drawRect(25,87.5f, 150, 112.5f, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
