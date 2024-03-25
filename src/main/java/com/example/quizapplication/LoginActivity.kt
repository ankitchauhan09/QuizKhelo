package com.example.quizapplication

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


data class Questions(
    var correctAnswer: String = "",
    var option1: String = "",
    var option2: String = "",
    var option3: String = "",
    var option4: String = "",
    var question: String = ""
)


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var dbRef: DatabaseReference
    private var questionList: MutableList<Questions> = mutableListOf()
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbRef = FirebaseDatabase.getInstance().getReference("questions");
        binding.loginButton.setOnClickListener {

            if (!binding.playerNameTextField.text.toString().trim().equals("")) {
                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (questionSnapshot in snapshot.children) {
                            val question: Questions? =
                                questionSnapshot.getValue(Questions::class.java)
                            if (question != null) {
                                questionList.add(question)
                            }
                        }
                        var intent = Intent(this@LoginActivity, QuestionsActivity::class.java)
                        val questionListJson = gson.toJson(questionList)
                        intent.putExtra("questionsList", questionListJson)
                        intent.putExtra("playerName", binding.playerNameTextField.text.toString())
                        startActivity(intent)
                        finish();
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            } else {
                MotionToast.darkToast(
                    this,
                    "Fill all the details",
                    "Player name cannot be left empty..",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
                )
            }
        }
    }
}