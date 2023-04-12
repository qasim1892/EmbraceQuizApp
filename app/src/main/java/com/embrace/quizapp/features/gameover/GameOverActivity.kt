package com.embrace.quizapp.features.gameover

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.embrace.quizapp.R
import com.embrace.quizapp.base.BaseActivity
import com.embrace.quizapp.databinding.ActivityGameOverBinding
import com.embrace.quizapp.features.home.ui.HomeActivity
import com.embrace.quizapp.interfaces.OnClickHandler
import com.embrace.quizapp.utils.Utils

class GameOverActivity : BaseActivity(), OnClickHandler {

    private var _binding: ActivityGameOverBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        val score = intent.getIntExtra(Utils.FINAL_SCORE, 0)
        binding.tvScore.text = String.format(getString(R.string.your_score), score)
        binding.clickHandler = this
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_tryAgain -> {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            R.id.btn_cancel -> {
                finishAfinity()
            }
        }
    }

    private fun finishAfinity() {
        finishAffinity()
    }

    override fun onBackPressed() {
        finishAfinity()
    }

}