package com.example.quizapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapplication.databinding.ActivityResultBinding
import kotlin.system.exitProcess

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val score = intent.getIntExtra("Score", 0)
        val totalNoOfQuestions = intent.getIntExtra("totalNoOfQuestions", 0)

        if (score != null) {
            if(score > totalNoOfQuestions/2){
                binding.animationView.setAnimation(R.raw.win)
                binding.scoreTextView.text = "Congratulations ${intent.getStringExtra("playerName")}🥳🥳\nYou have secured ${score} out of ${totalNoOfQuestions}"
            }
            else{
                binding.animationView.setAnimation(R.raw.loss)
                binding.scoreTextView.text = "Better luck next time ${intent.getStringExtra("playerName")}!!\nYou have secured ${score} out of ${totalNoOfQuestions}"
            }
        } else {
            Toast.makeText(this, "Some error has occurred!! ", Toast.LENGTH_SHORT).show()
        }

        binding.replayGameButton.setOnClickListener {
            startActivity(Intent(this@ResultActivity, MainActivity::class.java))
            finish();
        }

        binding.exitGameButton.setOnClickListener {
            finishAffinity();
            exitProcess(0)
        }

    }
}