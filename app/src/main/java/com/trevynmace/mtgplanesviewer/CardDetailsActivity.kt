package com.trevynmace.mtgplanesviewer

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.trevynmace.mtgplanesviewer.data.model.Card

class CardDetailsActivity : AppCompatActivity() {
    private lateinit var mCard: Card

    private lateinit var mSetTextView: TextView
    private lateinit var mCardImageView: ImageView
    private lateinit var mCardCostLayout: RelativeLayout
    private lateinit var mRarityTextView: TextView
    private lateinit var mArtistTextView: TextView
    private lateinit var mFlavorTextView: TextView
    private lateinit var mBackImageButton: ImageButton
    private lateinit var mCardNameTextView: TextView
    private lateinit var mCardTypeTextView: TextView
    private lateinit var mCardCostTextView: TextView
    private lateinit var mPowerToughnessLayout: RelativeLayout
    private lateinit var mPowerToughnessTextView: TextView
    private lateinit var mCardDescriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_details)

        mCard = intent.getSerializableExtra("card") as Card

        mSetTextView = findViewById(R.id.set_text_view)
        mCardImageView = findViewById(R.id.card_image_view)
        mCardCostLayout = findViewById(R.id.card_cost_layout)
        mRarityTextView = findViewById(R.id.rarity_text_view)
        mArtistTextView = findViewById(R.id.artist_text_view)
        mFlavorTextView = findViewById(R.id.flavor_text_view)
        mBackImageButton = findViewById(R.id.back_image_button)
        mCardNameTextView = findViewById(R.id.card_name_text_view)
        mCardTypeTextView = findViewById(R.id.card_type_text_view)
        mCardCostTextView = findViewById(R.id.card_cost_text_view)
        mPowerToughnessLayout = findViewById(R.id.power_toughness_layout)
        mPowerToughnessTextView = findViewById(R.id.power_toughness_text_view)
        mCardDescriptionTextView = findViewById(R.id.card_description_text_view)

        if (mCard.imageUrl.isNotEmpty()) {
            Picasso.get()
                .load(mCard.imageUrl.replace("http", "https"))
                .into(mCardImageView)
        }
        else {
            mCardImageView.setImageResource(R.drawable.card_placeholder)
        }

        mSetTextView.text = getString(R.string.set_template, mCard.set)
        mRarityTextView.text = mCard.rarity
        mArtistTextView.text = getString(R.string.artist_template, mCard.artist)
        mBackImageButton.setOnClickListener { finish() }
        mCardNameTextView.text = mCard.name
        mCardTypeTextView.text = mCard.type

        if (mCard.cmc <= 0) {
            mCardCostLayout.visibility = View.GONE
        }
        else {
            mCardCostTextView.text = getString(R.string.converted_mana_cost_template, mCard.cmc.toString())
        }

        if (mCard.power.isEmpty() || mCard.toughness.isEmpty()) {
            mPowerToughnessLayout.visibility = View.GONE
        }
        else {
            mPowerToughnessTextView.text = getString(R.string.power_toughness_template, mCard.power, mCard.toughness)
        }

        if (mCard.flavor.isEmpty()) {
            mFlavorTextView.visibility = View.GONE
        }
        else {
            mFlavorTextView.text = mCard.flavor
        }

        if (mCard.text.isEmpty()) {
            mCardDescriptionTextView.visibility = View.GONE
        }
        else {
            mCardDescriptionTextView.text = mCard.text
        }
    }
}