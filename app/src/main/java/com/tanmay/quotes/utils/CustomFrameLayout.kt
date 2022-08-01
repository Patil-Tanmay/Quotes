package com.tanmay.quotes.utils

import android.content.Context
import android.media.metrics.Event
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CustomFrameLayout
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int =0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes){

    private val parentSwipeRefreshLayout: SwipeRefreshLayout?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is SwipeRefreshLayout) {
                v = v.parent as? View
            }
            return v as? SwipeRefreshLayout
        }

    private val customRecView : CustomRecyclerView?
    get() {
        var v = children
        for (children in v){

        }
        return v as? CustomRecyclerView
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        if (parentSwipeRefreshLayout!= null && customRecView!=null){
        when(ev?.action){
            ACTION_DOWN ->{
                parentSwipeRefreshLayout?.isEnabled = true
            }

            ACTION_MOVE ->{
                parentSwipeRefreshLayout?.isEnabled = false
            }
        }

//        }
        return super.onInterceptTouchEvent(ev)
    }
}