package com.trevynmace.mtgplanesviewer

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trevynmace.mtgplanesviewer.data.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: CardRecyclerAdapter
    private lateinit var mCardProgressBar: View
    private lateinit var mSearchEditText: EditText

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCards()

        mRecyclerView = findViewById(R.id.cards_recycler_view)
        val gridLayout = GridLayoutManager(this, 2)
        mRecyclerView.layoutManager = gridLayout

        mRecyclerAdapter = CardRecyclerAdapter()
        mRecyclerView.adapter = mRecyclerAdapter

        mSearchEditText = findViewById(R.id.search_edit_text)
        mSearchEditText.addTextChangedListener(textWatcher)

        mCardProgressBar = findViewById(R.id.cards_progress_bar)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(searchString: CharSequence?, start: Int, before: Int, count: Int) {
            timer?.cancel()

            timer = object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    toggleProgressBar(true)
                    getCards(searchString.toString())
                }
            }

            timer?.start()
        }
    }

    private fun getCards(searchString: String = "") {
        GlobalScope.launch(Dispatchers.Main) {
            val cards = NetworkService.getCardsAsync(3, searchString).await()
            mRecyclerAdapter.cards = cards
            mRecyclerAdapter.notifyDataSetChanged()

            toggleProgressBar(false)
        }
    }

    private fun toggleProgressBar(shouldShow: Boolean) {
        if (shouldShow) {
            mRecyclerView.visibility = View.GONE
            mCardProgressBar.visibility = View.VISIBLE
        }
        else {
            mRecyclerView.visibility = View.VISIBLE
            mCardProgressBar.visibility = View.GONE
        }
    }
}