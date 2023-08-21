package com.trevynmace.mtgplanesviewer

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trevynmace.mtgplanesviewer.data.model.Card
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CardRecyclerAdapter : RecyclerView.Adapter<CardRecyclerAdapter.CardHolder>() {
    var cards: MutableList<Card> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.card_recycler_view_item, parent, false)
        return CardHolder(cardView)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bindCard(cards[position])
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    class CardHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var card: Card? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(view.context, CardDetailsActivity::class.java)
            intent.putExtra("card", card)
            startActivity(view.context, intent, null)
        }

        fun bindCard(card: Card) {
            this.card = card
            val cardName = view.findViewById<TextView>(R.id.card_name)
            val cardImage = view.findViewById<ImageView>(R.id.card_image)

            if (card.name.isNotEmpty()) {
                cardName.text = card.name
            }

            if (card.imageUrl.isNotEmpty()) {
                val fixed = card.imageUrl.replaceFirst("http", "https")
                Picasso.get()
                        .load(fixed)
                        .placeholder(R.drawable.card_placeholder)
                        .into(cardImage)

            }
            else {
                cardImage.setImageResource(R.drawable.card_placeholder)
            }
        }
    }
}

