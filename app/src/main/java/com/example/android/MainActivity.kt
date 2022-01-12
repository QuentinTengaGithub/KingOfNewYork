package com.example.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getGameScreen(view: View) {
        val intent = Intent(this, GameActivity::class.java).apply {
        }
        startActivity(intent)
    }

    fun getCreditsScreen(view: View) {
        val intent =  Intent(this, CreditsActivity::class.java).apply {
        }
        startActivity(intent)
    }
}
