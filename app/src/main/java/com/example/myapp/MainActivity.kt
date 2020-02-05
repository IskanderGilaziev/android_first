package com.example.myapp

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    var questions: List<Question> = listOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)

    )

    var currentIndex = 0

    val TAG: String = "MainActivity"

    val currentIndexKey = "index"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if( savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(currentIndexKey, 0)
        }

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val positive: Button = findViewById(R.id.true_button)
        val negative: Button = findViewById(R.id.false_button)

//        val toast = Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT)

//        toast.setGravity(Gravity.TOP, 0, 0)

//        val toastIncorrect = Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT)

//        toastIncorrect.setGravity(Gravity.FILL_VERTICAL, 0, 0)

        positive.setSafeOnClickListener { checkAnswer(true) }
        negative.setSafeOnClickListener { checkAnswer(false)  }


        val textView: TextView = findViewById(R.id.question_text_view)

        val nextButton: ImageButton = findViewById(R.id.next_button)

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questions.size
            updateQuestion(textView)
        }

        val prevButton: ImageButton = findViewById(R.id.prev_button)

        prevButton.setOnClickListener {
            if (currentIndex != 0) {
                currentIndex = (currentIndex - 1) % questions.size
            } else {
                currentIndex = questions.size
            }
            removeQestion(textView)
        }

        textView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questions.size
            updateQuestion(textView)
        }

        updateQuestion(textView)
        Log.d(TAG, "call create")
    }


    //check double click button
    private fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        Log.d(TAG, "check double click")
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        outState.putInt(currentIndexKey, currentIndex)
    }

fun updateQuestion(textView: TextView) {
    val nextQuestion = questions[currentIndex].textResId
    textView.setText(nextQuestion)

}

    fun removeQestion(textView: TextView){
        val prevQuestion: Int
        if (currentIndex == 0) {
            prevQuestion = questions[currentIndex].textResId
        } else {
            prevQuestion = questions[currentIndex - 1].textResId
        }
        textView.setText(prevQuestion)

    }

    fun checkAnswer(result: Boolean) {
        val answerIsTrue = questions[currentIndex].answerTrue
        var messageResId = 0
        if (result == answerIsTrue) {
            messageResId = R.string.correct_toast
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT) .show()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "call restart")
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "call resume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "call destroy")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "call pause")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "call start")

    }


}
