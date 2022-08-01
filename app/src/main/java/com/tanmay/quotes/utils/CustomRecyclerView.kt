package com.tanmay.quotes.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2

class CustomRecyclerView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int =0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val parentSwipeRefreshLayout: SwipeRefreshLayout?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is SwipeRefreshLayout) {
                v = v.parent as? View
            }
            return v as? SwipeRefreshLayout
        }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        Log.e("CustomRecView", "onInterceptTouchEvent: ", )
//        parentSwipeRefreshLayout?.requestDisallowInterceptTouchEvent(true)
        val a = super.onInterceptTouchEvent(e)
        Log.e("CustomRecView", "onInterceptTouchEvent: after",  )
        return a
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        Log.e("CustomRecView", "onTouchEvent: ", )
        val a = super.onTouchEvent(e)
        Log.e("CustomRecView", "onTouchEvent: after",  )
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("CustomRecView", "dispatchTouchEvent: ", )
        val a = super.dispatchTouchEvent(ev)
        Log.e("CustomRecView", "dispatchTouchEvent: after", )
        return a
    }
}