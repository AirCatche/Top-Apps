package com.slobodianiuk.topapps.model.itemRecycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(context: Context,recyclerView: RecyclerView,listener: ItemOnClick)
    : View.OnTouchListener {
    var gestureDetector: GestureDetector? = null
    private var listen: ItemOnClick? = null

    init {
        listen = listener
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                val child = recyclerView.findChildViewUnder(e?.x!!, e.y)
                if (child != null && listen != null) {
                    listener.onItemClick(child, recyclerView.getChildAdapterPosition(child))
                }
                return super.onSingleTapUp(e)
            }
            override fun onLongPress(e: MotionEvent?) {
                val child = recyclerView.findChildViewUnder(e?.x!!, e.y)
                if (child != null && listen != null) {
                    listener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
                }
                return super.onLongPress(e)
            }
        })
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        gestureDetector?.onTouchEvent(event)
        return false
    }
}