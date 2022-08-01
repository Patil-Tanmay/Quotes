package com.tanmay.quotes.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.lang.Math.abs

class CustomSwipeToRefreshLayout
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    private val touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var startX = 0f
    private var startY = 0f
    private var forbidSwipe = false
    private var isStartScrolledByY = false

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        Log.e("CustomSwipe", "onInterceptTouchEvent: ", )
//        when (event.action) {
//            ACTION_DOWN -> {
//                startX = event.x
//                startY = event.y
//            }
//            ACTION_MOVE -> {
//                val isScrolledByX = abs(event.x - startX) > touchSlop
//                val isScrolledByY = abs(event.y - startY) > touchSlop
//                if (!forbidSwipe && isScrolledByY) {
//                    isStartScrolledByY = true
//                }
//                if ((isScrolledByX || forbidSwipe) && !isStartScrolledByY) {
//                    forbidSwipe = true
//                    return false
//                }
//            }
//            ACTION_CANCEL, ACTION_UP -> {
//                forbidSwipe = false
//                isStartScrolledByY = false
//            }
//        }
        val a =  super.onInterceptTouchEvent(event)
        Log.e("CustomSwipe", "onInterceptTouchEvent: after", )
        return a
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("CustomSwipe", "dispatchTouchEvent: ", )
        val a = super.dispatchTouchEvent(ev)
        Log.e("CustomSwipe", "dispatchTouchEvent: after", )
        return a
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("CustomSwipe", "onTouchEvent: ", )
        val a = super.onTouchEvent(ev)
        Log.e("CustomSwipe", "onTouchEvent: after",  )
        return a
    }

    //    override fun onNestedScroll(
//        target: View,
//        dxConsumed: Int,
//        dyConsumed: Int,
//        dxUnconsumed: Int,
//        dyUnconsumed: Int
//    ) {
////        if (forbidSwipe) return
//        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
//    }

}