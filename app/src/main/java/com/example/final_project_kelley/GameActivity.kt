package com.example.final_project_kelley

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var balloonContainer: FrameLayout
    private lateinit var scoreText: TextView
    private lateinit var timerText: TextView
    private lateinit var gameOverText: TextView

    private var score = 0
    private var timeRemaining = 30
    private val balloonSpeed = 20L // Delay between balloon movements (in ms)
    private val handler = Handler()
    private var isGameActive = true // Flag to control game state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Set custom title for the app bar
        supportActionBar?.title = "Balloon Pop Game"

        balloonContainer = findViewById(R.id.balloonContainer)
        scoreText = findViewById(R.id.scoreText)
        timerText = findViewById(R.id.timerText)
        gameOverText = findViewById(R.id.gameOverText)

        gameOverText.visibility = View.GONE // Hide the game over text initially

        startGame()
    }

    private fun startGame() {
        // Start the timer
        handler.postDelayed(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                if (timeRemaining > 0) {
                    timeRemaining--
                    timerText.text = "Time: $timeRemaining"
                    handler.postDelayed(this, 1000)
                } else {
                    endGame() // End the game when the timer reaches 0
                }
            }
        }, 1000)

        // Generate balloons
        generateBalloons()
    }

    private fun endGame() {
        isGameActive = false // Stop all game activities
        gameOverText.text = "Game Over!" // Display the game over message
        gameOverText.visibility = View.VISIBLE // Show the game over text
    }

    private fun generateBalloons() {
        val balloonRunnable = object : Runnable {
            override fun run() {
                if (!isGameActive) return // Stop generating balloons if the game is over

                val balloon = BalloonView(this@GameActivity, null)
                balloon.layoutParams = FrameLayout.LayoutParams(200, 300)

                // Set random position for the balloon
                val randomX = Random.nextInt(0, balloonContainer.width - balloon.width)
                val randomY = balloonContainer.height
                balloon.translationX = randomX.toFloat()
                balloon.translationY = randomY.toFloat()

                // Handle balloon pop event
                balloon.onBalloonPopped = {
                    score++
                    scoreText.text = "Score: $score"
                }

                balloonContainer.addView(balloon)

                // Move the balloon upward
                val moveBalloonRunnable = object : Runnable {
                    override fun run() {
                        if (!isGameActive) {
                            balloonContainer.removeView(balloon) // Remove balloons when game ends
                            return
                        }

                        if (balloon.translationY > -balloon.height.toFloat()) {
                            balloon.translationY -= balloonSpeed
                            handler.postDelayed(this, balloonSpeed)
                        } else {
                            balloonContainer.removeView(balloon)
                        }
                    }
                }

                handler.post(moveBalloonRunnable)
                handler.postDelayed(this, Random.nextLong(500, 1500)) // Random delay for next balloon
            }
        }

        handler.post(balloonRunnable)
    }
}
