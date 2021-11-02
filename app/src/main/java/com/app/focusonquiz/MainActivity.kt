package com.app.focusonquiz

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.focusonquiz.databinding.ActivityMainBinding
import com.app.focusonquiz.room.builder.DatabaseBuilder
import com.app.focusonquiz.room.entity.Quiz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var ctimer: CountDownTimer? = null

    private lateinit var questionsList: List<Quiz>
    private var questionNum = 0
    private var totalScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            btnStartRestart.setOnClickListener {
                openQuestionsDB()
            }

            btnA.setOnClickListener {
                checkAnswerAndCalculatePoints(btnA.text.toString(), false)
                jumpToNextQuestion()
            }

            btnB.setOnClickListener {
                checkAnswerAndCalculatePoints(btnB.text.toString(), false)
                jumpToNextQuestion()
            }

            btnC.setOnClickListener {
                checkAnswerAndCalculatePoints(btnC.text.toString(), false)
                jumpToNextQuestion()
            }

            btnD.setOnClickListener {
                checkAnswerAndCalculatePoints(btnD.text.toString(), false)
                jumpToNextQuestion()
            }

        }

    }

    private fun checkAnswerAndCalculatePoints(answer: String, isSkipped: Boolean) {

        if (isSkipped) {
            Log.d("###KBC", "IS SKIPPED")
            totalScore -= 5
        } else {
            if (answer == questionsList[questionNum].opt1) {
                // Add 20
                Log.d("###KBC", "CORRECT ANSWER")
                totalScore += 20
            } else {
                // Minus 10
                Log.d("###KBC", "WRONG ANSWER")
                totalScore -= 10
            }
        }

        Log.d("###KBC", "Current Total Score = $totalScore ")
    }

    private fun jumpToNextQuestion() {
        binding.tvCounter.setTextColor(Color.WHITE)
        binding.tvCounter.text = "10"
        ctimer?.cancel()
        questionNum += 1
        startCounter()
    }

    private fun openQuestionsDB() {

        GlobalScope.launch(Dispatchers.IO) {

            questionsList =
                DatabaseBuilder.getDBInstance(applicationContext).focusDao().getQuestions()
            Log.d("TAG", "openQuestionsDB: $questionsList")

        }.invokeOnCompletion {

            GlobalScope.launch(Dispatchers.Main) {

                binding.apply {
                    btnStartRestart.visibility = View.GONE
                    tvQuestion.visibility = View.VISIBLE
                    tvCounter.visibility = View.VISIBLE
                    btnA.visibility = View.VISIBLE
                    btnB.visibility = View.VISIBLE
                    btnC.visibility = View.VISIBLE
                    btnD.visibility = View.VISIBLE
                }

                questionNum = 0
                totalScore = 0

                startCounter()

            }
        }


    }

    private fun startCounter() {

        if (questionNum < questionsList.size) {

            binding.apply {

                tvQuestion.text = "${questionNum + 1}. " + questionsList[questionNum].question

                when (arrayListOf("A", "B", "C", "D").random()) {

                    "A" -> {
                        btnA.text = questionsList[questionNum].opt1
                        btnB.text = questionsList[questionNum].opt2
                        btnC.text = questionsList[questionNum].opt3
                        btnD.text = questionsList[questionNum].opt4
                    }
                    "B" -> {
                        btnB.text = questionsList[questionNum].opt1
                        btnA.text = questionsList[questionNum].opt2
                        btnC.text = questionsList[questionNum].opt3
                        btnD.text = questionsList[questionNum].opt4
                    }
                    "C" -> {
                        btnC.text = questionsList[questionNum].opt1
                        btnB.text = questionsList[questionNum].opt2
                        btnA.text = questionsList[questionNum].opt3
                        btnD.text = questionsList[questionNum].opt4
                    }
                    "D" -> {
                        btnD.text = questionsList[questionNum].opt1
                        btnB.text = questionsList[questionNum].opt2
                        btnC.text = questionsList[questionNum].opt3
                        btnA.text = questionsList[questionNum].opt4
                    }
                }

            }

            ctimer = object : CountDownTimer(11000, 1000) {

                override fun onTick(millisUntilFinished: Long) {

                    val f: NumberFormat = DecimalFormat("00")
                    val hour = millisUntilFinished / 3600000 % 24
                    val min = millisUntilFinished / 60000 % 60
                    val sec = millisUntilFinished / 1000 % 60
                    if (Integer.parseInt(f.format(sec)) < 4) {
                        binding.tvCounter.setTextColor(Color.RED)
                    }
                    binding.tvCounter.text = f.format(sec)

                }

                override fun onFinish() {
                    checkAnswerAndCalculatePoints("", true)
                    jumpToNextQuestion()
                }

            }.start()

        } else {

            binding.apply {
                tvQuestion.text = "Your total score is $totalScore out of 400"
                tvCounter.visibility = View.GONE
                btnA.visibility = View.GONE
                btnB.visibility = View.GONE
                btnC.visibility = View.GONE
                btnD.visibility = View.GONE
                btnStartRestart.visibility = View.VISIBLE
                btnStartRestart.text = "RESTART QUIZ"

            }

        }


    }
}