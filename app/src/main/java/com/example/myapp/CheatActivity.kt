package com.example.myapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity() {

    companion object : Intent() {

        private var EXTRA_ANSWER_IS_TRUE = "com.example.myapp.answer_is_true"
        private val  EXTRA_ANSWER_SHOWN = "com.example.myapp.answer_shown"

        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            val intent = Intent(packageContext, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            return intent
        }

        fun wasAnswerShown(intent: Intent): Boolean {
            return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        val isTrueAnswer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        val textAnswer: TextView = findViewById(R.id.answer_text_view)
        val showAnswerButton : Button = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            if (isTrueAnswer) {
                textAnswer.setText(R.string.true_button)
            } else {
                textAnswer.setText(R.string.false_button)

            }
            setAnswerShownResult(true)
        }
    }

    fun setAnswerShownResult(isAnswerShown: Boolean) {
        val intent = Intent()
        intent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(Activity.RESULT_OK, intent)
    }

}
