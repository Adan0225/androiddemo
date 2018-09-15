/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2015 cpoopc
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.czy.reecycleviewheader;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.imczy.common_util.log.LogUtil;

import java.util.Arrays;
import java.util.List;

public class ScrollableHelper
{
    public static final String TAG = "ScrollableHelper";

    private View mCurrView;
    private ScrollableContainer mScrollableContainer;

    private int mSystemVersion = android.os.Build.VERSION.SDK_INT;

    public interface ScrollableContainer
    {
        View getScrollableView();
    }

    public void setCurrentScrollableContainer(ScrollableContainer
            scrollableContainer)
    {
        mCurrView = scrollableContainer.getScrollableView();
        mScrollableContainer = scrollableContainer;
    }

    private View getScrollableView()
    {
        return mScrollableContainer.getScrollableView();
    }

    /**
     * 判断是否滑动到顶部方法,ScrollAbleLayout根据此方法来做一些逻辑判断
     * 目前只实现了AdapterView,ScrollView,RecyclerView
     * 需要支持其他view可以自行补充实现
     *
     * @return
     */
    public boolean isTop()
    {
        View scrollableView = getScrollableView();
        boolean isCanScrollDown = ViewCompat.canScrollVertically(scrollableView, -1);
        return !isCanScrollDown;

        //        if (scrollableView == null) {
        ////            throw new NullPointerException("You should call ScrollableHelper.setCurrentScrollableContainer() to set ScrollableContainer.");
        //            return false;
        //        }
        //        if (scrollableView instanceof AdapterView) {
        //            return isAdapterViewTop((AdapterView) scrollableView);
        //        }
        //        if (scrollableView instanceof ScrollView) {
        //            return isScrollViewTop((ScrollView) scrollableView);
        //        }
        //        if (scrollableView instanceof RecyclerView) {
        //            return isRecyclerViewTop((RecyclerView) scrollableView);
        //        }
        //        if (scrollableView instanceof WebView) {
        //            return isWebViewTop((WebView) scrollableView);
        //        }
        //        throw new IllegalStateException("scrollableView must be a instance of AdapterView|ScrollView|RecyclerView");
    }

    private static boolean isRecyclerViewTop(RecyclerView recyclerView)
    {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager)
                                                layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);

                if (childAt == null || (firstVisibleItemPosition == 0 && childAt != null &&
                                        childAt.getTop() == 0)) {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager)
                                                   layoutManager).findFirstVisibleItemPositions(null);
                List firstVisibleItemPositionList = Arrays.asList(firstVisibleItemPositions);
                View childAt = recyclerView.getChildAt(0);

                if (childAt == null || (firstVisibleItemPositionList.contains(0) &&
                                        childAt != null && childAt.getTop() == 0)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isAdapterViewTop(AdapterView adapterView)
    {
        if (adapterView != null) {
            int firstVisiblePosition = adapterView.getFirstVisiblePosition();
            View childAt = adapterView.getChildAt(0);

            if (childAt == null || (firstVisiblePosition == 0 && childAt != null &&
                                    childAt.getTop() == 0)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isScrollViewTop(ScrollView scrollView)
    {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }

        return false;
    }

    private static boolean isWebViewTop(WebView scrollView)
    {
        if (scrollView != null) {
            int scrollViewY = scrollView.getScrollY();
            return scrollViewY <= 0;
        }

        return false;
    }

    @SuppressLint("NewApi")
    public void smoothScrollBy(int velocityY, int distance, int duration)
    {
        View scrollableView = getScrollableView();

        if (scrollableView instanceof AbsListView) {
            AbsListView absListView = (AbsListView) scrollableView;

            if (mSystemVersion >= 21) {
                absListView.fling(velocityY);
            } else {
                absListView.smoothScrollBy(distance, duration);
            }
        } else if (scrollableView instanceof ScrollView) {
            ((ScrollView) scrollableView).fling(velocityY);
        } else if (scrollableView instanceof RecyclerView) {
            ((RecyclerView) scrollableView).fling(0, velocityY);
        } else if (scrollableView instanceof WebView) {
            ((WebView) scrollableView).flingScroll(0, velocityY);
        }


    }


}
