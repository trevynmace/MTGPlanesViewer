package com.trevynmace.mtgplanesviewer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trevynmace.mtgplanesviewer.data.model.Card

class CardRecyclerAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardRecyclerAdapter.CardHolder>() {
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

        }

        fun bindCard(card: Card) {
            this.card = card
            val cardName = view.findViewById<TextView>(R.id.card_name)
            val cardImage = view.findViewById<ImageView>(R.id.card_image)

            cardName.text = card.name
            Picasso.get()
                    .load(card.imageUrl.replace("http","https"))
                    .into(cardImage)
        }
    }
}

