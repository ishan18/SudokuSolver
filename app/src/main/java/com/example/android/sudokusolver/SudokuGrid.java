package com.example.android.sudokusolver;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SudokuGrid extends View {

    private int mWidth;
    private int mHeight;
    private Paint[] cell;
    private Paint cellText;
    private Paint lines;
    private Paint fatLines;
    private float cellSize;
    private StringBuffer board=new StringBuffer();
    private int currentSelected;
    private final int INDEX_UNFILLED_CELL=0;
    private final int INDEX_FILLED_CELL=1;
    private final int INDEX_WRONG_CELL=2;
    private final int INDEX_FULL_CORRECT=3;
    private final int INDEX_SELECTED_CELL=4;
    private final int INDEX_PERMANENT_CELL=5;
    private int[] filledRow,filledColumn,filledBox;
    private boolean[] permanent;
    private boolean[] wrongFilled;
    private AttributeSet attributeSet;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=w;
        cellSize=(float)mWidth/9;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
            {
                float[] xy=getXY(j,i);
                int pos=i*9+j;
                if(pos==currentSelected){
                    canvas.drawRect(xy[0]-cellSize/2,xy[1]-cellSize/2,xy[0]+cellSize/2,xy[1]+cellSize/2,cell[INDEX_SELECTED_CELL]);
                }
                else if(board.charAt(i*9+j)==' '){
                    canvas.drawRect(xy[0]-cellSize/2,xy[1]-cellSize/2,xy[0]+cellSize/2,xy[1]+cellSize/2,cell[INDEX_UNFILLED_CELL]);
                }
                else if(filledRow[i]==9 || filledBox[i/3*3+j/3]==9 || filledColumn[j]==9)
                    canvas.drawRect(xy[0]-cellSize/2,xy[1]-cellSize/2,xy[0]+cellSize/2,xy[1]+cellSize/2,cell[INDEX_FULL_CORRECT]);
                else if(wrongFilled[pos])
                    canvas.drawRect(xy[0]-cellSize/2,xy[1]-cellSize/2,xy[0]+cellSize/2,xy[1]+cellSize/2,cell[INDEX_WRONG_CELL]);
                else if(permanent[pos])
                    canvas.drawRect(xy[0]-cellSize/2,xy[1]-cellSize/2,xy[0]+cellSize/2,xy[1]+cellSize/2,cell[INDEX_PERMANENT_CELL]);
                else
                    canvas.drawRect(xy[0]-cellSize/2,xy[1]-cellSize/2,xy[0]+cellSize/2,xy[1]+cellSize/2,cell[INDEX_FILLED_CELL]);

                canvas.drawText(board,i*9+j,i*9+j+1,xy[0],xy[1]+cellText.getTextSize()/2,cellText);
            }
        for(int i=0;i<=9;i++){
            if(i%3==0){
                canvas.drawLine(i*cellSize,0,i*cellSize,9*cellSize,fatLines);
            }
            else{
                canvas.drawLine(i*cellSize,0,i*cellSize,9*cellSize,lines);
            }
        }
        for(int i=0;i<=9;i++){
            if(i%3==0){
                canvas.drawLine(0,i*cellSize,9*cellSize,i*cellSize,fatLines);
            }
            else {
                canvas.drawLine(0,i*cellSize,9*cellSize,i*cellSize,lines);
            }
        }
    }

    private float[] getXY(int x,int y)
    {
        float[] result=new float[2];
        result[0]=cellSize*x+cellSize/2;
        result[1]=cellSize*y+cellSize/2;
        return result;
    }

    public SudokuGrid(Context context) {
        super(context);
        attributeSet=null;
        init();
    }

    public SudokuGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attributeSet=attrs;
        init();
    }

    public SudokuGrid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attributeSet=attrs;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void init()
    {
        cellText=new Paint(Paint.ANTI_ALIAS_FLAG);
        cellText.setColor(Color.BLACK);
        cellText.setTextAlign(Paint.Align.CENTER);
        cellText.setStyle(Paint.Style.FILL_AND_STROKE);

        cell=new Paint[6];
        cell[INDEX_UNFILLED_CELL]=new Paint(Paint.ANTI_ALIAS_FLAG);
        cell[INDEX_UNFILLED_CELL].setColor(Color.BLACK);
        cell[INDEX_UNFILLED_CELL].setStyle(Paint.Style.STROKE);
        cell[INDEX_UNFILLED_CELL].setStrokeWidth(5);

        cell[INDEX_FILLED_CELL]=new Paint(Paint.ANTI_ALIAS_FLAG);
        cell[INDEX_FILLED_CELL].setStyle(Paint.Style.FILL_AND_STROKE);
        cell[INDEX_FILLED_CELL].setStrokeWidth(5);

        cell[INDEX_SELECTED_CELL]=new Paint(Paint.ANTI_ALIAS_FLAG);
        cell[INDEX_SELECTED_CELL].setStyle(Paint.Style.FILL_AND_STROKE);
        cell[INDEX_SELECTED_CELL].setStrokeWidth(5);

        cell[INDEX_FULL_CORRECT]=new Paint(Paint.ANTI_ALIAS_FLAG);
        cell[INDEX_FULL_CORRECT].setStyle(Paint.Style.FILL_AND_STROKE);
        cell[INDEX_FULL_CORRECT].setStrokeWidth(5);

        cell[INDEX_WRONG_CELL]=new Paint(Paint.ANTI_ALIAS_FLAG);
        cell[INDEX_WRONG_CELL].setStyle(Paint.Style.FILL_AND_STROKE);
        cell[INDEX_WRONG_CELL].setStrokeWidth(5);

        cell[INDEX_PERMANENT_CELL]=new Paint(Paint.ANTI_ALIAS_FLAG);
        cell[INDEX_PERMANENT_CELL].setStyle(Paint.Style.FILL_AND_STROKE);
        cell[INDEX_PERMANENT_CELL].setStrokeWidth(5);

        if(attributeSet!=null){
            TypedArray typedArray=getContext().obtainStyledAttributes(attributeSet,R.styleable.SudokuGrid,0,0);
            cell[INDEX_FILLED_CELL].setColor(typedArray.getColor(R.styleable.SudokuGrid_filledCellColor,Color.GRAY));
            cell[INDEX_WRONG_CELL].setColor(typedArray.getColor(R.styleable.SudokuGrid_wrongCellColor,Color.RED));
            cell[INDEX_FULL_CORRECT].setColor(typedArray.getColor(R.styleable.SudokuGrid_rightCellColor,Color.GREEN));
            cell[INDEX_SELECTED_CELL].setColor(typedArray.getColor(R.styleable.SudokuGrid_currentCellColor,Color.BLUE));
            cell[INDEX_PERMANENT_CELL].setColor(typedArray.getColor(R.styleable.SudokuGrid_permanentCellColor,Color.CYAN));
            cellText.setTextSize(typedArray.getDimension(R.styleable.SudokuGrid_android_textSize,60f));
            typedArray.recycle();
        }

        lines=new Paint(Paint.ANTI_ALIAS_FLAG);
        lines.setColor(Color.BLACK);
        lines.setStrokeWidth(5);

        fatLines=new Paint(lines);
        fatLines.setStrokeWidth(10);

        currentSelected=0;

        setMinimumHeight(getWidth());

        for(int i=0;i<81;i++)
            board.append(' ');

        filledBox=new int[9];
        filledColumn=new int[9];
        filledRow=new int[9];
        for(int i=0;i<9;i++)
            filledBox[i]=0;
        for(int i=0;i<9;i++)
            filledColumn[i]=0;
        for(int i=0;i<9;i++)
            filledRow[i]=0;

        permanent=new boolean[81];
        for(int i=0;i<81;i++)
            permanent[i]=false;
        wrongFilled=new boolean[81];
        for(int i=0;i<81;i++)
            wrongFilled[i]=false;
    }

    public int getPosition(float x,float y){
        int xpos=-1,ypos=-1;
        for(int i=1;i<=9;i++)
            if(i*cellSize>x){
                xpos=i;
                break;
            }
        for(int i=1;i<=9;i++)
            if(i*cellSize>y){
                ypos=i;
                break;
            }
        if(xpos==-1 || ypos==-1)
            return -1;
        return (ypos-1)*9+xpos-1;
    }

//    public void setPermanentCells(StringBuffer stringBuffer){
//        for(int i=0;i<81;i++)
//            if(stringBuffer.toString().charAt(i)!=' ')
//                permanent[i]=true;
//    }

    public void setCurrentSelected(int pos){
        currentSelected=pos;

        invalidate();
    }

    public float getCellSize(){
        return cellSize;
    }

    public boolean setValue(int pos,int num){

        if(permanent[pos])
            return false;
        if(board.toString().charAt(pos)==(char)(num+'0'))
            return false;
        if(board.toString().charAt(pos)==' ' || wrongFilled[pos]){
            int x=pos%9;
            int y=pos/9;
            filledBox[y/3*3+x/3]++;
            filledRow[y]++;
            filledColumn[x]++;
        }
        board.setCharAt(pos,(char)(num+'0'));
        wrongFilled[pos]=false;

        invalidate();

        return true;
    }

    public boolean setWrongValue(int pos,int num){
        if(permanent[pos])
            return false;
        if(board.toString().charAt(pos)==(char)(num+'0'))
            return false;
        if(board.toString().charAt(pos)!=' ' && !wrongFilled[pos]){
            int x=pos%9,y=pos/9;
            filledColumn[x]--;
            filledBox[x/3+y/3*3]--;
            filledRow[y]--;
        }
        board.setCharAt(pos,(char)(num+'0'));
        wrongFilled[pos]=true;

        invalidate();
        return true;
    }

    public boolean eraseValue(int pos){

        if(permanent[pos])
            return false;
        if(board.charAt(pos)==' ')
            return false;
        board.setCharAt(pos,' ');

        if(!wrongFilled[pos]){
            int x=pos%9;
            int y=pos/9;
            filledBox[y/3*3+x/3]--;
            filledRow[y]--;
            filledColumn[x]--;
        }
        wrongFilled[pos]=false;
        invalidate();

        return true;
    }
    public StringBuffer getBoard(){
        return board;
    }

    public void setBoard(StringBuffer board){
        this.board=board;

        for(int i=0;i<9;i++)
            filledColumn[i]=0;
        for(int i=0;i<9;i++)
            filledRow[i]=0;
        for(int i=0;i<9;i++)
            filledBox[i]=0;
        for(int i=0;i<81;i++)
            if(board.toString().charAt(i)!=' '){
                int x=i%9;
                int y=i/9;
                filledBox[y/3*3+x/3]++;
                filledRow[y]++;
                filledColumn[x]++;
                permanent[i]=true;
            }
        invalidate();
    }

    public boolean checkComplete() {
        for(int i=0;i<9;i++)
            if(filledRow[i]<9 || filledBox[i]<9 || filledColumn[i]<9)
                return false;
        return true;
    }
}