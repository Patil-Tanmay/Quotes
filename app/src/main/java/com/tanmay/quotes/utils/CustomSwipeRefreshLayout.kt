package com.tanmay.quotes.utils

import android.content.Context
import android.util.AttributeSet
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
        when (event.action) {
            ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            ACTION_MOVE -> {
                val isScrolledByX = abs(event.x - startX) > touchSlop
                val isScrolledByY = abs(event.y - startY) > touchSlop
                if (!forbidSwipe && isScrolledByY) {
                    isStartScrolledByY = true
                }
                if ((isScrolledByX || forbidSwipe) && !isStartScrolledByY) {
                    forbidSwipe = true
                    return false
                }
            }
            ACTION_CANCEL, ACTION_UP -> {
                forbidSwipe = false
                isStartScrolledByY = false
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        if (forbidSwipe) return
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

}