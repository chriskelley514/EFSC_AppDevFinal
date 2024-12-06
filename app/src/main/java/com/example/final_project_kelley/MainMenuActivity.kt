package com.example.final_project_kelley

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Get references to the buttons from the layout
        val playButton: Button = findViewById(R.id.btnPlay)
        val exitButton: Button = findViewById(R.id.btnExit)

        // Set click listener for Play button
        playButton.setOnClickListener {
            navigateToGame()
        }

        // Set click listener for Exit button
        exitButton.setOnClickListener {
            finish() // Close the current activity
        }
    }

    // Start GameActivity when the Play button is clicked
    private fun navigateToGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}
