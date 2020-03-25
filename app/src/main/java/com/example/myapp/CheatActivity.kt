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
        private const val hintCountKey = "hintCountKey"
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, hintCount: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(hintCountKey, hintCount)
            }
        }

        fun wasAnswerShown(intent: Intent): Boolean {
            return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }

        fun getHintCount(intent: Intent): Int {
            return intent.getIntExtra(hintCountKey, 3)
        }
    }


    private lateinit var textAnswer: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var versionSdk: TextView
    private var isAnswerShown = false
    private var hintCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_cheat)
        if (savedInstanceState != null) {
            isAnswerShown = savedInstanceState.getBoolean(cheaterKey, false)
        }

        hintCount = intent.getIntExtra(hintCountKey, 3)
        val isTrueAnswer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        textAnswer = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        // вывод версии сборки на экран ==>
        versionSdk = findViewById(R.id.show_version)
        val text: String ="API level: " + Build.VERSION.SDK_INT.toString()
        versionSdk.text = text
        //<==
        showAnswerButton.setOnClickListener {
            --hintCount
            val answerText = when {
                isTrueAnswer -> R.string.true_button
                else -> R.string.false_button
            }
            textAnswer.setText(answerText)
            isAnswerShown = true
            setAnswerShownResult(isAnswerShown, hintCount)
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
            setAnswerShownResult(true, hintCount)
        }
    }

    // Сохранение результата дочерней активности
    // о том, что отвечающий подсмотрел ответ
    private fun setAnswerShownResult(isAnswerShown: Boolean, hintCount: Int) {
        val intent = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(hintCountKey, hintCount)
        }
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("CheatActivity", "onSaveInstanceState")
        outState.putBoolean(cheaterKey, isAnswerShown)
        outState.putInt(hintCountKey, hintCount)
    }

}
