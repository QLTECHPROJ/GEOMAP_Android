package com.geomap.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class DrawingView(context : Context?, attrs : AttributeSet?) :
    View(context, attrs) {
    private var erase = false
    private var drawPath : Path = Path()
    private var drawPaint : Paint = Paint()
    private var canvasPaint : Paint
    private var paintColor = -0x9a0000
    private var tempColor : String? = null
    private var drawCanvas : Canvas? = null
    private var canvasBitmap : Bitmap? = null
    var isFilled = false
    var isEdited = false

    init {
        drawPaint.color = paintColor
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 20F
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        canvasPaint = Paint(Paint.DITHER_FLAG)
        isFilled = false
        isEdited = false
    }

    override fun onDraw(canvas : Canvas) {  //draw view
        canvas.drawBitmap(canvasBitmap!!, 0F, 0F, canvasPaint)
        canvas.drawPath(drawPath, drawPaint)
        Log.e("MotionEvent", "onDraw")
    }

    override fun onSizeChanged(w : Int, h : Int, oldw : Int, oldh : Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
        Log.e("MotionEvent", "onSizeChanged")
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(touchX, touchY)
                isFilled = true
                isEdited = true
                Log.e("MotionEvent", "ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(touchX, touchY)
                isFilled = true
                isEdited = true
                Log.e("MotionEvent", "ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                drawCanvas!!.drawPath(drawPath, drawPaint)
                drawPath.reset()
                isFilled = true
                isEdited = true
                Log.e("MotionEvent", "ACTION_UP")
            }
            else -> return false
        }
        Log.e("DrawView", isFilled.toString())
        invalidate()
        return true
    }

    fun setErase(isErase : Boolean) {
        erase = isErase
        if (erase) {
            drawPaint.color = Color.WHITE
        } else drawPaint.color = paintColor
        Log.e("MotionEvent", "setErase")
    }

    fun setColor(newColor : String?) {
        invalidate()
        paintColor = Color.parseColor(newColor)
        drawPaint.color = paintColor
        tempColor = newColor
        Log.e("MotionEvent", "setColor")
    }

    fun startNew() {
        drawCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
        invalidate()
        isFilled = false
        isEdited = false
    }
}