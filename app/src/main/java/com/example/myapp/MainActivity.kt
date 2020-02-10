package com.example.myapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import com.example.myapp.CheatActivity as CheatActivity

class MainActivity : AppCompatActivity() {

    private var questions: List<Question> = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)

    )

    private var currentIndex = 0

    private val TAG: String = "MainActivity"

    private val currentIndexKey = "index"
    private val answerCountkey = "answerCountKey"
    private val successCountkey = "successCountkey"
    private val REQUEST_CODE_CHEATS = 0

    private var successAnswerCount:Double = 0.0
    private var answerCount = questions.size
    private var isCheater: Boolean = false // признак того, что ответчик подсмотрел ответ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if( savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(currentIndexKey, 0)
            answerCount = savedInstanceState.getInt(answerCountkey, questions.size)
            successAnswerCount = savedInstanceState.getDouble(successCountkey, 0.0)
        }


        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val positive: Button = findViewById(R.id.true_button)
        val negative: Button = findViewById(R.id.false_button)

        positive.setSafeOnClickListener { checkAnswer(true) }
        negative.setSafeOnClickListener { checkAnswer(false)  }


        val textView: TextView = findViewById(R.id.question_text_view)

        val nextButton: ImageButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            if (answerCount != 0) {
                currentIndex = (currentIndex + 1) % questions.size
                isCheater = false // при выводе след запроса, считаем что клиент еще не подсмотрел ответ
                updateQuestion(textView)
            } else {
                showSuccessAnswers(textView)
            }
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

        val cheatsButton: Button = findViewById(R.id.cheat_button)

        cheatsButton.setOnClickListener {
           val answerIsTrue = questions[currentIndex].answerTrue
            // получить информацию от дочерней активности
            startActivityForResult(CheatActivity.newIntent(this, answerIsTrue), REQUEST_CODE_CHEATS)
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
        outState.putInt(answerCountkey, answerCount)
        outState.putDouble(successCountkey, successAnswerCount)
    }

    private fun updateQuestion(textView: TextView) {
//        Log.d(TAG, "Update question text", Exception())
        val nextQuestion = questions[currentIndex].textResId
        textView.setText(nextQuestion)
        answerCount--
    }

    private fun showSuccessAnswers(textView: TextView) {
        val procents:Int = ((successAnswerCount/questions.size) * 100).toInt()
        val text = "Success answers: $procents %"
        textView.text = text
    }

    private fun removeQestion(textView: TextView){
        val prevQuestion: Int = if (currentIndex == 0) {
            questions[currentIndex].textResId
        } else {
            questions[currentIndex - 1].textResId
        }
        textView.setText(prevQuestion)

    }

    private fun checkAnswer(result: Boolean) {
        val answerIsTrue = questions[currentIndex].answerTrue
        var messageResId = 0
        if (isCheater) {
            messageResId = R.string.judgment_toast
        } else {
            if (result == answerIsTrue) {
                messageResId = R.string.correct_toast
                successAnswerCount++
            } else {
                messageResId = R.string.incorrect_toast
            }
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

    // Получение данных от дочерней активности
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEATS) {
            if (data == null) {
                return
            }
            isCheater = CheatActivity.wasAnswerShown(data) // получение от дочерней активности результата подсмотра ответа
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
