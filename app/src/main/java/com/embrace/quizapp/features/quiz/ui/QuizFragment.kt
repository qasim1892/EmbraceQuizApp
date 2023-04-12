package com.embrace.quizapp.features.quiz.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.embrace.quizapp.R
import com.embrace.quizapp.base.BaseFragment
import com.embrace.quizapp.commons.FetchState
import com.embrace.quizapp.databinding.FragmentQuizBinding
import com.embrace.quizapp.extensions.*
import com.embrace.quizapp.features.gameover.GameOverActivity
import com.embrace.quizapp.features.quiz.data.model.AnswerOptions
import com.embrace.quizapp.features.quiz.data.model.QuizResponse
import com.embrace.quizapp.features.quiz.data.model.enums.AnswerStatus
import com.embrace.quizapp.features.quiz.presentation.QuizViewModel
import com.embrace.quizapp.network.response.ErrorResponse
import com.embrace.quizapp.utils.ImageLoader.loadImage
import com.embrace.quizapp.utils.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel


class QuizFragment : BaseFragment() {

    private val viewModel: QuizViewModel by viewModel()
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var quizResponse: QuizResponse
    lateinit var countDownTimer: CountDownTimer
    private var count: Int = 0
    private var score: Int = 0
    private lateinit var answersAdapter: AnswersAdapter
    private val answerOptionsList = ArrayList<AnswerOptions>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQuizBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getQuizData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.fetchQuizState.observe(viewLifecycleOwner) {
            when (it) {
                is FetchState.Success<*> -> {
                    //Cast the data to non-nullable response
                    it.data?.cast<QuizResponse> { quizResponse ->
                        stopShimmer()
                        setupQuizResponse(quizResponse)
                    }
                }
                is FetchState.ErrorApi -> {
                    //we don't have error response for this api
                    //So we will just show the logs.
                    handleError(it.error)
                }
                is FetchState.Loading -> {
                    startShimmer()
                }
            }
        }
    }

    private fun setupQuizResponse(response: QuizResponse) {
        quizResponse = response
        setTimer()
        setUpQuestionAndAnswer()
    }

    private fun setUpQuestionAndAnswer() {
        answerOptionsList.clear()
        val questions = quizResponse.questions
        setupViews(questions)
    }

    private fun setupViews(questions: List<QuizResponse.Question>) {
        with(binding) {
            val questionsNumber = String.format(
                resources.getString(R.string.num_question), (count + 1), questions.size
            )
            tvQuestion.setTextAsHtml(questions[count].question)
            tvNumQuestions.text = questionsNumber.safeUnwrap()
            loadImage(
                imageView = imgQuestion, url = questions[count].questionImageUrl.safeUnwrap()
            )
            tvCurrentScore.text = String.format(
                resources.getString(R.string.your_score), score.toString().safeUnwrap()
            )
            questions[count].answers.entries.forEach {
                answerOptionsList.add(
                    AnswerOptions(
                        AnswerStatus.NONE,
                        it.key,
                        it.value,
                    )
                )
            }
            answersAdapter = AnswersAdapter { position, answers ->
                checkAnswer(position, answers)
            }
            answersAdapter.isClickable = true
            recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            recyclerview.adapter = answersAdapter
            answersAdapter.differ.submitList(answerOptionsList)
        }
    }

    private fun checkAnswer(position: Int, item: AnswerOptions) {
        countDownTimer.cancel()
        answersAdapter.isClickable = false
        val questions = quizResponse.questions
        /**
         *  As we have multiple-choices
         *  So here we check if the clicked value
         *  contains the any of that choice
         */
        if (item.key in questions[count].correctAnswer) {
            score += questions[count].score
            answerOptionsList[position].isCorrect = AnswerStatus.CORRECT
        } else {
            answerOptionsList[position].isCorrect = AnswerStatus.WRONG
        }
        count++
        answersAdapter.notifyItemChanged(position)
        //2 seconds delay to show next question
        delayOnLifecycle(2000L) {
            if (isCompleted()) {
                gameOver()
            } else {
                countDownTimer.start()
                setUpQuestionAndAnswer()
            }
        }
    }

    //Countdown timer to set the question time
    private fun setTimer() {
        countDownTimer = object : CountDownTimer(MILLIS_IN_FUTURE, 1000) {
            override fun onTick(p0: Long) {
                with(binding) {
                    val timeLeft = (p0 / 1000)
                    progressbarTimer.progress = timeLeft.toInt().safeUnwrap()
                    tvTimer.text = String.format(
                        resources.getString(R.string.time_left), timeLeft.toString().safeUnwrap()
                    )
                }
            }

            override fun onFinish() {
                count++
                if (isCompleted()) {
                    gameOver()
                } else {
                    setUpQuestionAndAnswer()
                    countDownTimer.start()
                }
            }
        }.start()
    }

    //to check if the last question is shown then mark as completed
    private fun isCompleted(): Boolean = count == (quizResponse.questions.size)

    private fun gameOver() {
        //If score is highest then we will store it in Realm DB
        if (score.safeUnwrap() > viewModel.getHighestScore().safeUnwrap()) {
            viewModel.updateScore(
                score = score
            )
        }
        //show GameOver Screen
        context?.openActivity(GameOverActivity::class.java) {
            putInt(Utils.FINAL_SCORE, score)
        }
    }

    private fun handleError(error: ErrorResponse) {
        stopShimmer()
        Log.d(TAG, error.message.toString().safeUnwrap())
    }

    private fun startShimmer() {
        with(binding) {
            homeShimmerEffect.visible()
            homeShimmerEffect.startShimmer()
            cardLayoutQuestion.gone()
        }
    }

    private fun stopShimmer() {
        with(binding) {
            homeShimmerEffect.gone()
            homeShimmerEffect.stopShimmer()
            cardLayoutQuestion.visible()
            root.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), R.color.background_main
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        _binding = null
    }

    companion object {
        val TAG: String = Companion::class.java.name
        const val MILLIS_IN_FUTURE: Long = 11 * 1000 //10 seconds delay
    }
}