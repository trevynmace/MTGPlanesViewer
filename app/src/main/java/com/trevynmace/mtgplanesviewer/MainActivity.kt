package com.trevynmace.mtgplanesviewer

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.trevynmace.mtgplanesviewer.data.NetworkService
import com.trevynmace.mtgplanesviewer.data.model.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val SHARED_PREFERENCES_KEY = "com.trevynmace.mtgplanesviewer.main"

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: CardRecyclerAdapter
    private lateinit var mCardProgressBar: View
    private lateinit var mSearchEditText: EditText

    //private var mSets: List<MTGSet> = emptyList()

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.cards_recycler_view)
        val gridLayout = GridLayoutManager(this, 2)
        mRecyclerView.layoutManager = gridLayout

        mRecyclerAdapter = CardRecyclerAdapter()
        mRecyclerView.adapter = mRecyclerAdapter

        mSearchEditText = findViewById(R.id.search_edit_text)
        mSearchEditText.addTextChangedListener(textWatcher)

        mCardProgressBar = findViewById(R.id.cards_progress_bar)

        if (isCacheValid()) {
            populateAdapterFromCache()
        }
        else {
            getCards()
        }
    }

    override fun onPause() {
        super.onPause()

        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val json = Gson().toJson(mRecyclerAdapter.cards)
        editor.putString("cards", json)

        val millis = System.currentTimeMillis()
        editor.putLong("update_time", millis)

        editor.apply()
    }

    private fun isCacheValid(): Boolean {
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val updateTime = sharedPrefs.getLong("update_time", 0)

        val timeSinceUpdate = System.currentTimeMillis() - updateTime
        val oneDayMillis = 86400000
        if (timeSinceUpdate >= oneDayMillis) {
            return false
        }

        return true
    }

    private fun populateAdapterFromCache() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val cardsJson = sharedPrefs.getString("cards", "")
        val cards = Gson().fromJson(cardsJson, Array<Card>::class.java).toList()

        mRecyclerAdapter.cards = cards
        toggleProgressBar(false)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
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

//    private fun getSets() {
//        GlobalScope.launch(Dispatchers.Main) {
//            mSets = NetworkService.getSetsAsync().await()
//        }
//    }

    private fun getCards(searchString: String = "") {
        GlobalScope.launch(Dispatchers.Main) {
            val cards = NetworkService.getCardsAsync(10, searchString).await()
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