package com.trevynmace.mtgplanesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.trevynmace.mtgplanesviewer.data.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.Main) {
            val cardName = NetworkService.getCardsAsync().await()
            changeText(cardName)
        }
    }

    fun changeText(text: String) {
        val textView = findViewById<TextView>(R.id.text)
        textView.text = text
    }
}