package varunbehl.algebra.view

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import varunbehl.algebra.R
import varunbehl.algebra.databinding.ScoreCorrectDetailBinding
import varunbehl.algebra.util.CountDownTimer

class ScoreTopView : LinearLayout {

    private lateinit var binding: ScoreCorrectDetailBinding

    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private var highestScore: Int = 0


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }


    private fun init(attrs: AttributeSet?, defStyle: Int) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.score_correct_detail, this,
                true)
        prefs = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        editor = prefs?.edit()
    }


    val totalTimeTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.txTotalTimeReverse?.text = "Total Time : " + millisUntilFinished / 1000

        }

        override fun onFinish() {

//            updateScore()

//            if (!isFinishing)
//                cond()
        }
    }

    private val questionTimer = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.txTimeReverse?.text = "Time : " + millisUntilFinished / 1000
        }

        override fun onFinish() {

        }
    }

    private val correctSolutionTimer = object : CountDownTimer(250, 250) {
        override fun onTick(millisUntilFinished: Long) {
            binding.ivCorrect?.visibility = View.VISIBLE
        }


        override fun onFinish() {
            binding.ivCorrect?.visibility = View.INVISIBLE
            binding.ivWrong?.visibility = View.INVISIBLE
        }
    }


    fun reset(correct: Int) {
        questionTimer.cancel()
        questionTimer.start()
        binding.ivWrong.visibility = View.INVISIBLE
        binding.txNumCorrect.text = "Correct : $correct"
    }

    fun resume() {
        totalTimeTimer.resume()
        questionTimer.resume()
    }

    private fun updateScore(correct: Int) {
        highestScore = prefs!!.getInt("key", 0)
        if (highestScore < correct) {
            editor!!.putInt("key", correct)
            editor!!.apply()
        }
    }

    override fun isInEditMode(): Boolean {
        return true
    }

    fun updateCorrectView(correct: Int) {
        correctSolutionTimer.start()
        updateScore(correct)
    }

    interface ScoreViewTimersListeners {
        fun onQuestionTimerFinish()
        fun onTotalTimerFinish()
    }

}