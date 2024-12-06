package com.example.final_project_kelley

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gameLayout: ConstraintLayout
    private lateinit var scoreTextView: TextView
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())
    private val balloonList = mutableListOf<BalloonView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameLayout = findViewById(R.id.gameLayout)
        scoreTextView = findViewById(R.id.scoreTextView)

        startGame()
    }

    private fun startGame() {
        score = 0
        updateScore()
        spawnBalloons()
    }

    private fun spawnBalloons() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (balloonList.size < 10) { // Limit number of balloons on screen
                    createBalloon()
                }
                handler.postDelayed(this, 1000) // Spawn every second
            }
        }, 1000)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createBalloon() {
        val balloon = BalloonView(this, null)
        val screenSize = Point()
        windowManager.defaultDisplay.getSize(screenSize)

        // Generate a random x position for the balloon, ensuring it stays within the screen bounds
        val xPosition = Random.nextInt(0, screenSize.x - 200) // Avoid spawning off-screen horizontally

        // Set the initial position of the balloon below the screen
        balloon.x = xPosition.toFloat()
        balloon.y = (screenSize.y + 200).toFloat() // Start below the screen

        // Add touch listener for popping the balloon
        balloon.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                gameLayout.removeView(balloon)
                balloonList.remove(balloon)
                score += 10
                updateScore()
            }
            true
        }

        balloonList.add(balloon)
        gameLayout.addView(balloon)

        // Balloon animation: move upwards and remove if it goes off-screen
        balloon.animate()
            .translationY(-200f)
            .setDuration(4000)
            .withEndAction {
                gameLayout.removeView(balloon)
                balloonList.remove(balloon)
                checkGameOver()
            }
            .start()
    }

    @SuppressLint("SetTextI18n")
    private fun updateScore() {
        scoreTextView.text = "Score: $score"
    }

    @SuppressLint("SetTextI18n")
    private fun checkGameOver() {
        if (balloonList.isEmpty()) {
            handler.removeCallbacksAndMessages(null)
            // Show Game Over screen or restart option
            scoreTextView.text = "Game Over! Final Score: $score"
        }
    }
}
