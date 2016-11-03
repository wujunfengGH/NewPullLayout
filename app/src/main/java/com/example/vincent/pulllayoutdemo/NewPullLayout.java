package com.example.vincent.pulllayoutdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.vincent.pulllayoutdemo.utils.DensityUtil;
import com.example.vincent.pulllayoutdemo.utils.ScrollingUtil;

/**
 * @创建者 Vincent
 * @创时间 2016/11/3
 * @描述 ${TODO}
 */

public class NewPullLayout extends FrameLayout {

    //触摸获得Y的位置
    private float mTouchY;
    //触摸获得X的位置(为防止滑动冲突而设置)
    private float mTouchX;

    //子控件
    private View mChildView;
    //头部layout
    protected FrameLayout mHeadLayout;

    //最大下拉高度 = mTopHeight*2
    protected float mTopHeight;
    //最大上拉高度 = mBottomHeight*2
    private float mBottomHeight;

    //底部layout
    private FrameLayout mBottomLayout;

    private View mHeaderView;
    private View mBottomView;

    //动画的变化率
    private DecelerateInterpolator decelerateInterpolator;

    //触发高度
    private float mChangePosition;

    private static final int PULL_DOWN = 1;
    private static final int PULL_UP = 2;
    private int state = PULL_DOWN;
    private PageChangeListener mPageChangeListener;

    public NewPullLayout(Context context) {
        this(context,null);
    }

    public NewPullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        mTopHeight = DensityUtil.dp2px(getContext(),160);
        mBottomHeight = DensityUtil.dp2px(getContext(),160);
        mChangePosition = DensityUtil.dp2px(getContext(),60);

        //使用isInEditMode解决可视化编辑器无法识别自定义控件的问题
        if (isInEditMode()) return;
        if (getChildCount() > 1) throw new RuntimeException("Only one childView is supported. 只能拥有一个子控件哦。");

        //在动画开始的地方快然后慢;
        decelerateInterpolator = new DecelerateInterpolator(10);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //添加头部
        if (mHeadLayout == null) {
            FrameLayout headViewLayout = new FrameLayout(getContext());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams.gravity = Gravity.TOP;
            headViewLayout.setLayoutParams(layoutParams);

            mHeadLayout = headViewLayout;

            this.addView(mHeadLayout);//addView(view,-1)添加到-1的位置

            if (mHeaderView == null){
                LinearLayout linearLayout = new LinearLayout(getContext());
                LayoutParams headerLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(getContext(),50));
                linearLayout.setLayoutParams(headerLayoutParams);
                setHeaderView(linearLayout);
            }
        }

        //添加底部
        if (mBottomLayout == null) {
            FrameLayout bottomViewLayout = new FrameLayout(getContext());
            LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams2.gravity = Gravity.BOTTOM;
            bottomViewLayout.setLayoutParams(layoutParams2);

            mBottomLayout = bottomViewLayout;
            this.addView(mBottomLayout);

            if (mBottomView == null) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                LayoutParams bottomLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(getContext(),50));
                linearLayout.setLayoutParams(bottomLayoutParams);
                setBottomView(linearLayout);
            }
        }


//        if (mHeadLayout != null) mHeadLayout.setVisibility(GONE);
//        if (mBottomLayout != null) mBottomLayout.setVisibility(GONE);


        //获得子控件
        mChildView = getChildAt(0);
        if (mChildView == null) return;
        mChildView.animate().setInterpolator(new DecelerateInterpolator());
    }

    /**
     * 设置头部View
     */
    public void setHeaderView(final View headerView) {
        if (headerView != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    mHeadLayout.removeAllViewsInLayout();
                    mHeadLayout.addView(headerView);
                }
            });
            mHeaderView = headerView;
        }
    }

    /**
     * 设置底部View
     */
    public void setBottomView(final View bottomView) {
        if (bottomView != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    mBottomLayout.removeAllViewsInLayout();
                    mBottomLayout.addView(bottomView);
                }
            });
            mBottomView = bottomView;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = ev.getX();
                mTouchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - mTouchX;
                float dy = ev.getY() - mTouchY;
                if (Math.abs(dx) <= Math.abs(dy)) {//滑动允许最大角度为45度
                    if (dy > 0 && !ScrollingUtil.canChildScrollUp(mChildView)) {
                        state = PULL_DOWN;
                        return true;
                    } else if (dy < 0 && !ScrollingUtil.canChildScrollDown(mChildView)) {
                        state = PULL_UP;
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = e.getY() - mTouchY;

                if (state == PULL_DOWN) {
                    dy = Math.min(mTopHeight * 2, dy);
                    dy = Math.max(0, dy);

                    if (mChildView != null) {
                        float offsetY = decelerateInterpolator.getInterpolation(dy / mTopHeight / 2) * dy / 2;
                        mChildView.setTranslationY(offsetY);

                        mHeadLayout.getLayoutParams().height = (int) offsetY;
                        mHeadLayout.requestLayout();

                    }
                } else if (state == PULL_UP) {
                    dy = Math.min(mBottomHeight * 2, Math.abs(dy));
                    dy = Math.max(0, dy);
                    if (mChildView != null) {
                        float offsetY = -decelerateInterpolator.getInterpolation(dy / mBottomHeight / 2) * dy / 2;
                        mChildView.setTranslationY(offsetY);

                        mBottomLayout.getLayoutParams().height = (int) -offsetY;
                        mBottomLayout.requestLayout();

                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mChildView != null) {
                    if (state == PULL_DOWN) {
                        if (mChildView.getTranslationY() >= mChangePosition && mPageChangeListener != null) {
//
                            mPageChangeListener.toPreviousPage();
                        }
                        animChildView(0f);

                    } else if (state == PULL_UP) {
                        if (Math.abs(mChildView.getTranslationY()) >= mBottomHeight - mChangePosition && mPageChangeListener != null) {
                            mPageChangeListener.toNextPage();
                        }
                        animChildView(0f);

                    }
                }
                return true;
        }
        return super.onTouchEvent(e);
    }


    /*************************************  执行动画  *****************************************/
    /**
     * 向下兼容,把mChildView.animate().setUpdateListener()改成ObjectAnimator.addUpdateListener
     * @param endValue mChildView最终位移
     */
    private void animChildView(float endValue) {
        animChildView(endValue, 300);
    }

    private void animChildView(float endValue, long duration) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(mChildView, "translationY", mChildView.getTranslationY(), endValue);
        oa.setDuration(duration);
        oa.setInterpolator(new DecelerateInterpolator());//设置速率为递减
        oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) mChildView.getTranslationY();//获得mChildView当前y的位置
                height = Math.abs(height);

                if (state == PULL_DOWN) {
                    mHeadLayout.getLayoutParams().height = height;
                    mHeadLayout.requestLayout();//重绘

//                    mHeadLayout.setVisibility(VISIBLE);
//                    mBottomLayout.setVisibility(GONE);
                } else if (state == PULL_UP) {
                    mBottomLayout.getLayoutParams().height = height;
                    mBottomLayout.requestLayout();

                }
            }
        });
        oa.start();
    }


    /**
     * @param pageChangeListener 回调接口
     */
    public void setPageChangeListener(PageChangeListener pageChangeListener){
        mPageChangeListener = pageChangeListener;
    }


    /**
     * 回调接口
     */
    public interface PageChangeListener {
        void toPreviousPage();
        void toNextPage();
    }
}
