package com.trevynmace.mtgplanesviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trevynmace.mtgplanesviewer.data.NetworkService
import com.trevynmace.mtgplanesviewer.data.model.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: CardRecyclerAdapter
    private lateinit var mCardProgressBar: View

    var mCardList: List<Card> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.cards_recycler_view)
        mRecyclerView.layoutManager = GridLayoutManager(this, 2)
        mCardProgressBar = findViewById(R.id.cards_progress_bar)
        mRecyclerAdapter = CardRecyclerAdapter(mCardList)
        mRecyclerView.adapter = mRecyclerAdapter

        GlobalScope.launch(Dispatchers.Main) {
            mCardList = NetworkService.getCardsAsync().await()
            mRecyclerView.visibility = View.VISIBLE
            mCardProgressBar.visibility = View.GONE

            //TODO: change this. it's wrong obv
            mRecyclerAdapter = CardRecyclerAdapter(mCardList)
            mRecyclerView.adapter = mRecyclerAdapter

//            mRecyclerAdapter.notifyDataSetChanged()
        }
    }
}