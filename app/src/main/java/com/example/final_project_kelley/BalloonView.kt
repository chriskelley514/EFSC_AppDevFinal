package com.example.final_project_kelley

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class BalloonView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val balloonPaint = Paint()
    private val stringPaint = Paint()
    private var isPopped = false
    var onBalloonPopped: (() -> Unit)? = null

    init {
        // Set the string paint to black color
        stringPaint.color = Color.BLACK
        stringPaint.strokeWidth = 5f

        // Set a random color for the balloon
        setRandomColor()
    }

    // Set a random color for the balloon
    private fun setRandomColor() {
        val colors = arrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA)
        val randomColor = colors[Random.nextInt(colors.size)]
        balloonPaint.color = randomColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // If the balloon is not popped, draw the balloon and string
        if (!isPopped) {
            // Draw the balloon as an oval shape
            canvas.drawOval(0f, 0f, width.toFloat(), height.toFloat(), balloonPaint)

            // Draw the string of the balloon (black line)
            val stringStartX = width / 2f
            val stringStartY = height.toFloat()
            val stringEndY = height + 200f // Length of the string
            canvas.drawLine(stringStartX, stringStartY, stringStartX, stringEndY, stringPaint)
        }
    }

    // Handle balloon click to pop it
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isPopped && event.action == MotionEvent.ACTION_DOWN) {
            isPopped = true
            onBalloonPopped?.invoke() // Trigger the pop event
            invalidate() // Redraw the balloon to show it as popped
        }
        return true
    }
}
