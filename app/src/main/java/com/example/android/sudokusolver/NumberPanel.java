package com.example.android.sudokusolver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class NumberPanel extends View {

    private int mWidth;
    private int mHeight;
    private float padSize;
    private Paint padCell;
    private Paint padText;
    private Paint lines;
    private StringBuffer panel;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
        padSize=(float)mWidth/9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i=0;i<9;i++){
            float[] xy=getXY(i);
            canvas.drawRoundRect(xy[0]-padSize/2,xy[1]-padSize/2,xy[0]+padSize/2,xy[1]+padSize/2,50,10,padCell);
//            canvas.drawRect();
            canvas.drawText(panel,i,i+1,xy[0],xy[1]+padText.getTextSize()/2,padText);
            canvas.drawLine(i*padSize,0,i*padSize,padSize,lines);
        }
        canvas.drawLine(9*padSize,0,9*padSize,padSize,lines);
        canvas.drawLine(0,0,9*padSize,0,lines);
        canvas.drawLine(0,padSize,9*padSize,padSize,lines);
    }

    private float[] getXY(int i){
        float[] result=new float[2];

        result[0]=i*padSize+padSize/2;
        result[1]=padSize/2;
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(){
        padCell=new Paint(Paint.ANTI_ALIAS_FLAG);
        padCell.setColor(Color.GRAY);
        padCell.setStyle(Paint.Style.FILL_AND_STROKE);

        padText=new Paint(Paint.ANTI_ALIAS_FLAG);
        padText.setColor(Color.BLACK);
        padText.setStyle(Paint.Style.STROKE);
        padText.setTextAlign(Paint.Align.CENTER);
        padText.setTextSize(60f);

        lines=new Paint(Paint.ANTI_ALIAS_FLAG);
        lines.setColor(Color.BLACK);
        lines.setStyle(Paint.Style.STROKE);
        lines.setStrokeWidth(5);

        panel=new StringBuffer();
        for(int i=1;i<=9;i++)
            panel.append(i);
    }

    public NumberPanel(Context context) {
        super(context);
        init();
    }

    public NumberPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getPosition(float x){
        int xpos=-1;
        for(int i=1;i<=9;i++)
            if(i*padSize>x){
                xpos=i;
                break;
            }
        if(xpos==-1)
            return -1;
        return xpos-1;
    }

    public float getPadSize(){
        return padSize;
    }
}