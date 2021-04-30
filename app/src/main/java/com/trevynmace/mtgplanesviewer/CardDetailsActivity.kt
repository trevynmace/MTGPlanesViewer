package com.trevynmace.mtgplanesviewer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.trevynmace.mtgplanesviewer.data.model.Card

class CardDetailsActivity : AppCompatActivity() {
    private lateinit var mCard: Card

    private lateinit var mCardNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_details)

        try {
            mCard = intent.getSerializableExtra("card") as Card

            if (mCard == null) {
                //TODO: pop up something saying the card couldnt' be loaded?
                finish()
            }
        }
        catch (e: Exception) {
            //TODO: pop up something saying the card couldnt' be loaded?
            finish()
        }

        mCardNameTextView = findViewById(R.id.card_name)
        mCardNameTextView.text = mCard.name
    }
}