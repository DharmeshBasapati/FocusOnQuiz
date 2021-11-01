package com.app.focusonquiz

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.focusonquiz.room.builder.DatabaseBuilder
import com.app.focusonquiz.room.entity.Quiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private var ctimer: CountDownTimer? = null
    private var btnA: Button? = null
    private var btnB: Button? = null
    private var btnC: Button? = null
    private var btnD: Button? = null
    private lateinit var questionsList: List<Quiz>
    private var tvCounter: TextView? = null
    private var tvQuestion: TextView? = null
    private var questionNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCounter = findViewById<TextView>(R.id.tvCounter)
        tvQuestion = findViewById<TextView>(R.id.tvQuestion)

        btnA = findViewById<Button>(R.id.btnA)
        btnB = findViewById<Button>(R.id.btnB)
        btnC = findViewById<Button>(R.id.btnC)
        btnD = findViewById<Button>(R.id.btnD)

        openQuestionsDB()

        btnA?.setOnClickListener {
            ctimer?.cancel()
            questionNum += 1
            startCounter()
        }
        btnB?.setOnClickListener {
            ctimer?.cancel()
            questionNum += 1
            startCounter()
        }
        btnC?.setOnClickListener {
            ctimer?.cancel()
            questionNum += 1
            startCounter()
        }
        btnD?.setOnClickListener {
            ctimer?.cancel()
            questionNum += 1
            startCounter()
        }
    }

    private fun openQuestionsDB() {

        GlobalScope.launch(Dispatchers.IO) {
            questionsList =
                DatabaseBuilder.getDBInstance(applicationContext).focusDao().getQuestions()
            Log.d("TAG", "openQuestionsDB: $questionsList")
        }.invokeOnCompletion {
            GlobalScope.launch(Dispatchers.Main) {
                startCounter()
            }
        }


    }

    private fun startCounter() {

        if (questionNum < 6) {

            tvQuestion?.text = questionsList[questionNum].question
            btnA?.text = questionsList[questionNum].opt1
            btnB?.text = questionsList[questionNum].opt2
            btnC?.text = questionsList[questionNum].opt3
            btnD?.text = questionsList[questionNum].opt4


            ctimer = object : CountDownTimer(11000, 1000) {

                override fun onTick(millisUntilFinished: Long) {

                    val f: NumberFormat = DecimalFormat("00")
                    val hour = millisUntilFinished / 3600000 % 24
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    tvCounter?.text = f.format(sec)

                }

                override fun onFinish() {
                    tvCounter?.text = "10"
                    questionNum += 1
                    startCounter()
                }

            }.start()

        } else {

            tvQuestion?.text = "Your score is 5 out of 5"
            btnA?.visibility = View.GONE
            btnB?.visibility = View.GONE
            btnC?.visibility = View.GONE
            btnD?.visibility = View.GONE


        }


    }
}