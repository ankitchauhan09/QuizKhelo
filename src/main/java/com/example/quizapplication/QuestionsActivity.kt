package com.example.quizapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapplication.databinding.ActivityQuestionsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class Answers(
    var correctAnswer: String,
    var choosenAnswer: String
)

class QuestionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionsBinding;
    private lateinit var dbRef: DatabaseReference
    private var questionsList: MutableList<Questions> = mutableListOf()
    private var currentQuestion: Int = 0;
    private var choosenAnswer: String = ""
    private var answersList: MutableList<Answers> = mutableListOf()
    private val gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.playerName.text = intent.getStringExtra("playerName")


        val questionlistJson = intent.getStringExtra("questionsList")
        questionsList =
            gson.fromJson(questionlistJson, object : TypeToken<MutableList<Questions>>() {}.type)
        binding.questionNoTextField.text =
            "${(currentQuestion + 1).toString()}/${questionsList.size}"

        initFirstQuestion(currentQuestion)

        val options =
            arrayOf<TextView>(binding.option1, binding.option2, binding.option3, binding.option4)

        for (option in options) {
            option.setOnClickListener {
                for (view in options) {
                    view.setBackgroundResource(R.drawable.question_options_background)
                }
                option.setBackgroundResource(R.drawable.correct_option_background)
                choosenAnswer = option.text.toString()
            }
        }

        binding.nextQuestionButton.setOnClickListener {

            for(option in options){
                option.setBackgroundResource(R.drawable.question_options_background)
            }

            if (currentQuestion <= questionsList.size - 1) {
                var correctAnswer = questionsList.get(currentQuestion).correctAnswer
                var choosenAnswer = choosenAnswer
                answersList.add(Answers(correctAnswer, choosenAnswer))
                currentQuestion++;
                if (currentQuestion <= questionsList.size - 1) {
                    nextQuestion(currentQuestion);
                }
                if (currentQuestion == questionsList.size - 1) {
                    binding.nextQuestionButton.text = "Submit"
                }
                binding.questionNoTextField.text =
                    "${(currentQuestion + 1).toString()}/${questionsList.size}"
            }
            if (currentQuestion == questionsList.size) {
                var totalScore: Int = 0;
                for (answer in answersList) {
                    if (answer.choosenAnswer.equals(answer.correctAnswer)) {
                        totalScore++;
                    }
                }
                var intent = Intent(this@QuestionsActivity, ResultActivity::class.java)
                intent.putExtra("Score", totalScore)
                intent.putExtra("playerName", binding.playerName.text.toString())
                intent.putExtra("totalNoOfQuestions", questionsList.size)
                startActivity(intent)
                finish()
            }
        }


    }

    private fun initFirstQuestion(questionPosition: Int) {
        if (questionsList.size >= 1) {
            val question = questionsList.get(questionPosition)
            if (question != null) {
                binding.questionTextField.text = question.question.toString()
                binding.option1.text = question.option1.toString()
                binding.option2.text = question.option2.toString()
                binding.option3.text = question.option3.toString()
                binding.option4.text = question.option4.toString()
            }
        }
    }

    private fun nextQuestion(questionPosition: Int) {
        if (questionsList.size >= 1) {
            val question = questionsList.get(questionPosition)
            if (question != null) {
                binding.questionTextField.text = question.question.toString()
                binding.option1.text = question.option1.toString()
                binding.option2.text = question.option2.toString()
                binding.option3.text = question.option3.toString()
                binding.option4.text = question.option4.toString()
            }
        }
    }
}