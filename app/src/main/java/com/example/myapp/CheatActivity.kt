package com.example.myapp

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.core.animation.addListener

class CheatActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = "com.example.myapp.answer_is_true"
        private const val  EXTRA_ANSWER_SHOWN = "com.example.myapp.answer_shown"
        private const val cheaterKey = "isCheaterActivity"
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            val intent = Intent(packageContext, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            return intent
        }

        fun wasAnswerShown(intent: Intent): Boolean {
            return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }

    private var isAnswerShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_cheat)
        if (savedInstanceState != null) {
            isAnswerShown = savedInstanceState.getBoolean(cheaterKey, false)
        }

        val isTrueAnswer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        val textAnswer: TextView = findViewById(R.id.answer_text_view)
        val showAnswerButton : Button = findViewById(R.id.show_answer_button)
        // вывод версии сборки на экран ==>
        val versionSdk: TextView = findViewById(R.id.show_version)
        val text: String ="API level: " + Build.VERSION.SDK_INT.toString()
        versionSdk.text = text
        //<==
        showAnswerButton.setOnClickListener {
            if (isTrueAnswer) {
                textAnswer.setText(R.string.true_button)
            } else {
                textAnswer.setText(R.string.false_button)
            }
            isAnswerShown = true
            setAnswerShownResult(isAnswerShown)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // проверка на версионность платформы, если меньше версия, то код не выполняется
                val x = showAnswerButton.width / 2
                val y = showAnswerButton.height / 2
                val radius: Float = showAnswerButton.width.toFloat()
                val animator: Animator =
                    ViewAnimationUtils.createCircularReveal(showAnswerButton, x, y, radius, 0f)
                animator.addListener { showAnswerButton.visibility = View.VISIBLE }
                animator.start()
            } else {
                showAnswerButton.visibility = View.INVISIBLE
            }

        }
        if (isAnswerShown) { // проверка на случай, если был поворот экрана и необходимо сохранить состояние просмотра ответа
            setAnswerShownResult(true)
        }
    }

    // Сохранение результата дочерней активности
    // о том, что отвечающий подсмотрел ответ
    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val intent = Intent()
        intent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("CheatActivity", "onSaveInstanceState")
        outState.putBoolean(cheaterKey, isAnswerShown)
    }

}
