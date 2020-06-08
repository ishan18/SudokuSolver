package com.example.android.sudokusolver;

import java.util.ArrayList;

public class SolveSudoku {

    private int[][] board;

    SolveSudoku(String s){
        board=new int[10][10];
        int x=0;
        for(int i=1;i<=9;i++)
            for(int j=1;j<=9;j++){
                if(s.charAt(x)==' ')
                    board[i][j]=0;
                else
                    board[i][j]=s.charAt(x)-'0';
                x++;
            }
    }

    boolean checkRow(int r, int num)
    {
        for(int i=1;i<=9;i++)
            if(board[r][i]==num)
                return false;
        return true;
    }
    boolean checkCol(int c, int num)
    {
        for(int i=1;i<=9;i++)
            if(board[i][c]==num)
                return false;
        return true;
    }
    boolean checkBox(int r, int c, int num)
    {
        for(int i=1;i<=3;i++)
            for(int j=1;j<=3;j++)
                if(board[(r-1)/3*3+i][(c-1)/3*3+j]==num)
                    return false;
        return true;
    }

    boolean checkRow(int[][] currentBoard,int r, int num)
    {
        for(int i=1;i<=9;i++)
            if(currentBoard[r][i]==num)
                return false;
        return true;
    }
    boolean checkCol(int[][] currentBoard,int c, int num)
    {
        for(int i=1;i<=9;i++)
            if(currentBoard[i][c]==num)
                return false;
        return true;
    }
    boolean checkBox(int[][] currentBoard,int r, int c, int num)
    {
        for(int i=1;i<=3;i++)
            for(int j=1;j<=3;j++)
                if(currentBoard[(r-1)/3*3+i][(c-1)/3*3+j]==num)
                    return false;
        return true;
    }

    boolean func()
    {
        for(int i=1;i<=9;i++)
            for(int j=1;j<=9;j++)
                if(board[i][j]==0)
                {
                    for(int num=1;num<=9;num++)
                    {
                        if(checkRow(i,num) && checkCol(j,num) && checkBox(i,j,num))
                        {
                            board[i][j]=num;
                            if(func())
                                return true;
                            board[i][j]=0;
                        }
                    }
                    return false;
                }
        return true;
    }

    public StringBuffer solve(){

        for(int i=1;i<=9;i++)
            for(int j=1;j<=9;j++)
                if(board[i][j]!=0)
                {
                    int num=board[i][j];
                    board[i][j]=0;
                    if(!checkRow(i,num) || !checkCol(j,num) || !checkBox(i,j,num))
                    {
                        return null;
                    }
                    board[i][j]=num;
                }
        if(!func())
            return null;
        StringBuffer stringBuffer=new StringBuffer();
        for(int i=1;i<=9;i++)
            for(int j=1;j<=9;j++)
                stringBuffer.append(board[i][j]);
        return stringBuffer;
    }

    private long funcCount(int[][] currentBoard) {
        for(int i=1;i<=9;i++)
            for(int j=1;j<=9;j++)
                if(currentBoard[i][j]==0)
                {
                    long count=0;
                    for(int num=1;num<=9;num++)
                    {
                        if(checkRow(currentBoard,i,num) && checkCol(currentBoard,j,num) && checkBox(currentBoard,i,j,num))
                        {
                            currentBoard[i][j]=num;
                            count+=funcCount(currentBoard);
                            currentBoard[i][j]=0;
                        }
                    }
                    return count;
                }
        return 1;
    }

    int countBlank(ArrayList<Integer> positions,StringBuffer sampleBoard){

        int l=0,h=81,ans=-1;
        while(l<=h){
            int mid=(l+h)/2;
            int[][] currentBoard=new int[10][10];
            for(int i=1;i<=9;i++)
                for(int j=1;j<=9;j++){
                    int pos=(i-1)*9+j-1;
                    if(sampleBoard.charAt(pos)!=' ')
                        currentBoard[i][j]=sampleBoard.charAt(pos)-'0';
                    else
                        currentBoard[i][j]=0;
                }

            for(int i=0;i<mid;i++){
                int x=positions.get(i)/9+1;
                int y=positions.get(i)%9+1;
                currentBoard[x][y]=0;
            }
            long sol=funcCount(currentBoard);
            if(sol==1){
                ans=mid;
                l=mid+1;
            }
            else
                h=mid-1;
        }
        return ans;
    }
}
