package com.example.autoviewpager.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.autoviewpager.R

class PageIndicator (context : Context, attributeSet : AttributeSet?= null) : View(context, attributeSet){

    private var eventCount = 0
    private var position = 0
    private var length = 0f

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.style = Paint.Style.FILL_AND_STROKE
        this.color = resources.getColor(R.color.melonGreen)
    }


    init {


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        length = width.toFloat() / eventCount
        canvas?.drawRect( position * length ,0f, (position+1) * length, height.toFloat(), paint )

    }

    fun setEventCount( _eventCount : Int) {
        this.eventCount = _eventCount
    }

    fun refresh( _position : Int ) {
        this.position = _position
        invalidate()
    }
}

@BindingAdapter("app:page_count")
fun PageIndicator.setPageCount( count : Int) {
    setEventCount(count)
}