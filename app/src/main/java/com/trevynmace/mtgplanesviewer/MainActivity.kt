package com.trevynmace.mtgplanesviewer

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.trevynmace.mtgplanesviewer.data.NetworkService
import com.trevynmace.mtgplanesviewer.data.model.Card
import com.trevynmace.mtgplanesviewer.data.model.MTGSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val SHARED_PREFERENCES_KEY = "com.trevynmace.mtgplanesviewer.main"
    private val UPDATE_TIME_KEY = "update_time"
    private val CARDS_KEY = "cards"

    private val mPageSize = 10
    private var mSearchString = ""
    private var mSelectedSet: MTGSet = MTGSet()
    private var mSelectedColors = ArrayList<String>()

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mScrollListener: InfiniteScrollListener
    private lateinit var mRecyclerAdapter: CardRecyclerAdapter
    private lateinit var mCardProgressBar: View
    private lateinit var mSearchEditText: EditText
    private lateinit var mSettingsButton: Button

    private lateinit var mSettingsLayout: View
    private lateinit var mSettingsDialog: AlertDialog
    private lateinit var mSetSpinner: Spinner

    private var mTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCardProgressBar = findViewById(R.id.cards_progress_bar)
        mRecyclerView = findViewById(R.id.cards_recycler_view)

        toggleProgressBar(true)

        val gridLayout = GridLayoutManager(this, 2)
        mRecyclerView.layoutManager = gridLayout

        mRecyclerAdapter = CardRecyclerAdapter()
        mRecyclerView.adapter = mRecyclerAdapter

        mScrollListener = object : InfiniteScrollListener(mRecyclerView.layoutManager as GridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                getAndAppendCards(page)
            }
        }

        mRecyclerView.addOnScrollListener(mScrollListener)

        mSearchEditText = findViewById(R.id.search_edit_text)
        mSearchEditText.addTextChangedListener(textWatcher)

        mSettingsButton = findViewById(R.id.settings_button)
        mSettingsButton.setOnClickListener {
            mSettingsDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()

        buildSettingsModal()

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
        editor.putString(CARDS_KEY, json)

        val millis = System.currentTimeMillis()
        editor.putLong(UPDATE_TIME_KEY, millis)

        editor.apply()
    }

    private fun isCacheValid(): Boolean {
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val updateTime = sharedPrefs.getLong(UPDATE_TIME_KEY, 0)

        val timeSinceUpdate = System.currentTimeMillis() - updateTime
        val oneDayMillis = 86400000
        if (timeSinceUpdate >= oneDayMillis) {
            return false
        }

        return true
    }

    private fun populateAdapterFromCache() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val cardsJson = sharedPrefs.getString(CARDS_KEY, "")
        val cards = Gson().fromJson(cardsJson, Array<Card>::class.java).toList()

        mRecyclerAdapter.cards = cards.toMutableList()
        toggleProgressBar(false)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(searchString: CharSequence?, start: Int, before: Int, count: Int) {
            mTimer?.cancel()

            mTimer = object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    toggleProgressBar(true)
                    mScrollListener.resetState()
                    mSearchString = searchString.toString()
                    
                    getCards()
                }
            }

            mTimer?.start()
        }
    }

    private fun getSets() {
        GlobalScope.launch(Dispatchers.Main) {
            val sets = NetworkService.getSetsAsync().await()

            mSetSpinner = mSettingsLayout.findViewById<Spinner>(R.id.set_spinner)
            val spinnerList = ArrayList<MTGSet>()
            spinnerList.add(MTGSet())
            spinnerList.addAll(sets)

            ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, spinnerList)
                    .also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        mSetSpinner.adapter = adapter
                    }
        }
    }

    private fun getCards() {
        GlobalScope.launch(Dispatchers.Main) {
            val cards = NetworkService.getCardsAsync(mPageSize, mSearchString, mSelectedSet.code, mSelectedColors).await()

            mRecyclerAdapter.cards = cards.toMutableList()
            mRecyclerAdapter.notifyDataSetChanged()

            toggleProgressBar(false)
        }
    }

    private fun getAndAppendCards(pageNumber: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            val cards = NetworkService.getCardsAsync(pageNumber, mPageSize, mSearchString, mSelectedSet.code, mSelectedColors).await()

            val startPosition = mRecyclerAdapter.cards.size
            mRecyclerAdapter.cards.addAll(cards)

            val totalItemCount = mRecyclerAdapter.cards.size
            mRecyclerAdapter.notifyItemRangeInserted(startPosition, totalItemCount)
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

    private fun buildSettingsModal() {
        mSettingsLayout = LayoutInflater.from(this).inflate(R.layout.settings_dialog, null, false)

        getSets()

        val whiteCheckBox = mSettingsLayout.findViewById<CheckBox>(R.id.white_check_box)
        val blackCheckBox = mSettingsLayout.findViewById<CheckBox>(R.id.black_check_box)
        val redCheckBox = mSettingsLayout.findViewById<CheckBox>(R.id.red_check_box)
        val greenCheckBox = mSettingsLayout.findViewById<CheckBox>(R.id.green_check_box)
        val blueCheckBox = mSettingsLayout.findViewById<CheckBox>(R.id.blue_check_box)

        val settingsSaveButton = mSettingsLayout.findViewById<Button>(R.id.settings_save_button)
        settingsSaveButton.setOnClickListener {
            mSelectedColors.clear()
            if (whiteCheckBox.isChecked) { mSelectedColors.add("White") }
            if (blackCheckBox.isChecked) { mSelectedColors.add("Black") }
            if (redCheckBox.isChecked) { mSelectedColors.add("Red") }
            if (greenCheckBox.isChecked) { mSelectedColors.add("Green") }
            if (blueCheckBox.isChecked) { mSelectedColors.add("Blue") }

            mSelectedSet = mSetSpinner.selectedItem as MTGSet

            mSettingsDialog.dismiss()
            toggleProgressBar(true)
            mScrollListener.resetState()
            getCards()
        }

        mSettingsDialog = AlertDialog.Builder(this)
            .setView(mSettingsLayout)
            .create()
    }
}