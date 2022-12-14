package com.geomap.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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
    private lateinit var drawCanvas : Canvas
    private lateinit var canvasBitmap : Bitmap

    init {
        drawPaint.color = paintColor
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 20F
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onDraw(canvas : Canvas) {  //draw view
        canvas.drawBitmap(canvasBitmap, 0F, 0F, canvasPaint)
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onSizeChanged(w : Int, h : Int, oldw : Int, oldh : Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> drawPath.lineTo(touchX, touchY)
            MotionEvent.ACTION_UP -> {
                drawCanvas.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setErase(isErase : Boolean) {
        erase = isErase
        if (erase) {
            drawPaint.color = Color.WHITE
        } else drawPaint.color = paintColor
    }

    fun setColor(newColor : String?) {
        invalidate()
        paintColor = Color.parseColor(newColor)
        drawPaint.color = paintColor
        tempColor = newColor
    }

    fun startNew() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR)
        invalidate()
    }
}