package com.trevynmace.mtgplanesviewer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trevynmace.mtgplanesviewer.data.model.Card

class CardRecyclerAdapter() : RecyclerView.Adapter<CardRecyclerAdapter.CardHolder>() {
    var cards: List<Card> = emptyList()

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
                Picasso.get()
                        .load(card.imageUrl.replace("http", "https"))
                        .into(cardImage)
            }
            else {
                cardImage.setImageResource(R.drawable.card_placeholder)
            }
        }
    }
}

