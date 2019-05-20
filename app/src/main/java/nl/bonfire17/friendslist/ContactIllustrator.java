package nl.bonfire17.friendslist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.android.friendslist.R;

public class ContactIllustrator extends View {

    private Paint paint;
    private int contactNumber;

    public ContactIllustrator(Context context) {
        super(context);
        init();
    }

    public ContactIllustrator(Context context, AttributeSet set) {
        super(context, set);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorPrimary));
    }

    public void setContactNumber(int number){
        this.contactNumber = number;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        for(int i = 0; i < contactNumber; i++){
            canvas.drawCircle((i * 75) + 21, 13, 10, paint);
            canvas.drawCircle((i * 75) + 21, 40, 20, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
