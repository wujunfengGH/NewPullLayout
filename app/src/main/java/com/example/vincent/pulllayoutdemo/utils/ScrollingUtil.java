/**
 * Copyright 2015 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.vincent.pulllayoutdemo.utils;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

public class ScrollingUtil {

    private ScrollingUtil() {
    }

    /**
     * 用来判断是否可以下拉
     */
    public static boolean canChildScrollUp(View mChildView) {
        if (mChildView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(mChildView, -1) || mChildView.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(mChildView, -1);
        }
    }

    /**
     * Whether it is possible for the child view of this layout to scroll down. Override this if the child view is a custom view.
     * 判断是否可以上拉
     */
    public static boolean canChildScrollDown(View mChildView) {
        if (Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(mChildView, 1) || mChildView.getScrollY() < 0;
        } else {
            return ViewCompat.canScrollVertically(mChildView, 1);
        }
    }

    public static boolean isScrollViewOrWebViewToTop(View view) {
        return view != null && view.getScrollY() == 0;
    }

    public static boolean isAbsListViewToTop(AbsListView absListView) {
        if (absListView != null) {
            int firstChildTop = 0;
            if (absListView.getChildCount() > 0) {
                // 如果AdapterView的子控件数量不为0，获取第一个子控件的top
                firstChildTop = absListView.getChildAt(0).getTop() - absListView.getPaddingTop();
            }
            if (absListView.getFirstVisiblePosition() == 0 && firstChildTop == 0) {
                return true;
            }
        }
        return false;
    }




    public static boolean isWebViewToBottom(WebView webView) {
        return webView != null && webView.getContentHeight() * webView.getScale() == (webView.getScrollY() + webView.getMeasuredHeight());
    }

    public static boolean isScrollViewToBottom(ScrollView scrollView) {
        if (scrollView != null) {
            int scrollContentHeight = scrollView.getScrollY() + scrollView.getMeasuredHeight() - scrollView.getPaddingTop() - scrollView.getPaddingBottom();
            int realContentHeight = scrollView.getChildAt(0).getMeasuredHeight();
            if (scrollContentHeight == realContentHeight) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAbsListViewToBottom(AbsListView absListView) {
        if (absListView != null && absListView.getAdapter() != null && absListView.getChildCount() > 0 && absListView.getLastVisiblePosition() == absListView.getAdapter().getCount() - 1) {
            View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);

            return lastChild.getBottom() <= absListView.getMeasuredHeight();
        }
        return false;
    }




    public static void scrollToBottom(final ScrollView scrollView) {
        if (scrollView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    public static void scrollToBottom(final AbsListView absListView) {
        if (absListView != null) {
            if (absListView.getAdapter() != null && absListView.getAdapter().getCount() > 0) {
                absListView.post(new Runnable() {
                    @Override
                    public void run() {
                        absListView.setSelection(absListView.getAdapter().getCount() - 1);
                    }
                });
            }
        }
    }


    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}