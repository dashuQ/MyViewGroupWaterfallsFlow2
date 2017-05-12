package com.example.myviewgroupwaterfallsflow.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义ViewGroup瀑布流
 * <p>
 * 写之前先看，搞清楚原理思想，再动手写代码，这时知道原理思想和逻辑就算写不出来看代码映像就更深些，光敲不弄清原理敲也是白敲。
 * <p>
 * Created by lenovo on 2017/5/11.
 */

public class MyViewGroupWaterfallsFlow extends ViewGroup {


    private int mChildWidth = 0;
    private int mColumns = 3;
    private int mHorizontalSpace = 20;
    private int[] mTop;
    private int mVerticalSpace =20;

    public MyViewGroupWaterfallsFlow(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        mTop = new int[mColumns];
    }

    public MyViewGroupWaterfallsFlow(Context context) {
        super(context,null);
        mTop = new int[mColumns];
    }

    public MyViewGroupWaterfallsFlow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTop = new int[mColumns];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //得到总宽度
        int measureWidth = 0;
        int measureHeight = 0;
        if (widthMode == MeasureSpec.EXACTLY) {
            measureWidth = widthSize;
            measureHeight = heightSize;
        } else {
            //measureChildren测量
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            //得到单个Item的宽度
            mChildWidth = (widthSize - (mColumns - 1) * mHorizontalSpace) / mColumns;
            int childCount = getChildCount();
            if (childCount < mColumns) {
                measureWidth = childCount * mChildWidth + (childCount - 1) * mHorizontalSpace;
            } else {
                measureWidth = widthSize;
            }
            clearTop();
            //遍历所有子view;
            for (int i = 0; i < childCount; i++) {
                View child = this.getChildAt(i);
                //子view高=子view高*单个Item的宽度/子view宽
                int childHeight = child.getMeasuredHeight() * mChildWidth / child.getMeasuredWidth();
                int minColum = getMinHeightColum();
                WaterfallLayoutParams lParams = (WaterfallLayoutParams) child.getLayoutParams();
                lParams.left = minColum * (mChildWidth + mHorizontalSpace);
                lParams.top = mTop[minColum];
                lParams.right=lParams.left + mChildWidth;
                lParams.bottom = lParams.top+childHeight;
                mTop[minColum] += mVerticalSpace + childHeight;
            }
            measureHeight = getMaxHeight();
        }
        setMeasuredDimension(measureWidth,measureHeight);
    }

    public int getMaxHeight() {
        int maxHeight =0;
        for(int i=0;i<mColumns;i++){
            if(mTop[i] >maxHeight){
                maxHeight = mTop[i];
            }
        }
        return maxHeight;
    }

    public static class WaterfallLayoutParams extends ViewGroup.LayoutParams{
        public int left=0;
        public int top=0;
        public int right=0;
        public int bottom =0;
        public WaterfallLayoutParams(Context context,AttributeSet attrs){
            super(context,attrs);
        }
        public WaterfallLayoutParams(int width, int height) {
            super(width, height);
        }

        public WaterfallLayoutParams(android.view.ViewGroup.LayoutParams params) {
            super(params);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return super.generateLayoutParams(attrs);
        return new WaterfallLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
//        return super.generateLayoutParams(p);
        return new WaterfallLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
//        return super.generateDefaultLayoutParams();
        return new WaterfallLayoutParams(WaterfallLayoutParams.WRAP_CONTENT,WaterfallLayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
//        return super.checkLayoutParams(p);
        return p instanceof WaterfallLayoutParams;
    }

    private void clearTop() {
        for (int i = 0; i < mColumns; i++) {
            mTop[i] = 0;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        clearTop();
        for(int i=0;i<childCount;i++){
            View child = this.getChildAt(i);

            /**实现方法一：*/
            /*int childHeight = child.getMeasuredHeight() * mChildWidth / child.getMeasuredWidth();
            //Log.e(TAG,"onLayout MeasureHeight = " + child.getMeasuredHeight() + "; MeasureWidth = " + child.getMeasuredWidth());
            int minColum = getMinHeightColum();
            int tleft = minColum * (mChildWidth + mHorizontalSpace);
            int ttop = mTop[minColum];
            int tright = tleft + mChildWidth;
            int tbottom = ttop + childHeight;
            mTop[minColum] += mVerticalSpace + childHeight;
            child.layout(tleft, ttop, tright, tbottom);*/

            /**实现方法二：*/
            WaterfallLayoutParams lParams = (WaterfallLayoutParams) child.getLayoutParams();
            child.layout(lParams.left,lParams.top,lParams.right,lParams.bottom);
        }
    }

    public int getMinHeightColum() {
        int minColum = 0;
        for (int i = 0; i < mColumns; i++) {
            if (mTop[i] < mTop[minColum]) {
                minColum = i;
            }
        }
        return minColum;
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int index);
    }

    public void setOnItemClickListener(final OnItemClickListener listener){
        for(int i=0;i<getChildCount();i++){
            final int index =i;
            View v=getChildAt(i);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,index);
                }
            });
        }
    }

}
