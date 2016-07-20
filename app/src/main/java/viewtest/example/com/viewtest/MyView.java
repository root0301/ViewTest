package viewtest.example.com.viewtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import java.util.Random;


public class MyView extends View {

    /**
     *  文本内容
     */
    private String mText;
    /**
     * 文本颜色
     */
    private int mColor;
    /**
     * 文本字体大小
     */
    private int mSize;
    /**
     * 绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    /**
     * Description:获得自定义样式属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 解析attrs，获取到我们定义的属性
         */
        TypedArray td = context.obtainStyledAttributes(attrs,R.styleable.MyView,defStyleAttr,0);
        int count = td.getIndexCount();
        for (int i=0; i<count;i++) {
            int attr = td.getIndex(i);
            switch (attr) {
                case R.styleable.MyView_Text:
                    mText = td.getString(attr);
                    break;
                case R.styleable.MyView_TextColor:
                    mColor = td.getColor(attr, Color.RED);
                    break;
                case R.styleable.MyView_TextSize:
                    mSize = td.getDimensionPixelSize(attr,(int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,18,getResources().getDisplayMetrics()
                    ));
                    break;
            }
        }
        td.recycle();
        mPaint = new Paint();
        mPaint.setTextSize(mSize);
        mBound = new Rect();
        mPaint.getTextBounds(mText,0,mText.length(),mBound);
        /**
         * 设置监听，被点击时文本内容发生改变
         */
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = randText();
                /**
                 * 文本发生改变，重绘View
                 */
                postInvalidate();
            }
        });
    }

    private String randText() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<4;i++) {
            int randNumber = r.nextInt(10);
            sb.append(""+randNumber);
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mSize);
            mPaint.getTextBounds(mText,0,mText.length(),mBound);
            float textWidth = mBound.width();
            int expWidth = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = expWidth;
        }
        if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mSize);
            mPaint.getTextBounds(mText,0,mText.length(),mBound);
            float textHeight = mBound.height();
            int expHeight = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = expHeight;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mColor);
        canvas.drawText(mText,getWidth()/2 - mBound.width()/2,getHeight()/2 + mBound.height()/2,mPaint);
    }
}



