package varunbehl.algebra.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import varunbehl.algebra.R
import varunbehl.algebra.databinding.ActivityCompareBinding
import varunbehl.algebra.util.CountDownTimer
import java.util.*

/**
 * Created by varunbehl on 19/08/15.
 */
class CompareActivity : AppCompatActivity() {

    private var right: Int = 1
    private lateinit var operatorRight: String
    private lateinit var operatorLeft: String
    private var correct: Int = 0
    private lateinit var binding: ActivityCompareBinding
    private val intValue = 0
    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var newResult: Int = 0
    private var leftVal: Int = 0
    private var rightVal: Int = 0
    private var score: Int = 0

    private val correctSolutionTimer = object : CountDownTimer(250, 250) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {
            binding.scoreLayout.ivCorrect!!.visibility = View.INVISIBLE
            binding.scoreLayout.ivWrong!!.visibility = View.INVISIBLE
        }
    }
    private val questionTimer = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.scoreLayout.txTimeReverse.text = "Time : " + millisUntilFinished / 1000
        }

        override fun onFinish() {
            compare()
        }
    }
    private val totalTimeTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.scoreLayout.txTotalTimeReverse.text = "Total Time : " + millisUntilFinished / 1000

        }

        override fun onFinish() {

            score = prefs!!.getInt("key", 0)
            val correct = 0
            if (score < correct) {
                editor!!.putInt("key", correct)
                editor!!.apply()
            }
            if (!isFinishing)
                cond()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compare)

        val settings = getSharedPreferences("MyPrefs", 0)

        if (settings.getBoolean("is_first_time_add", true)) {
            //the app is being launched for first time, do something
            Log.d("TAG", "First time")

            // first time task
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("is_first_time_add", false).apply()
        } else {
            //second time launch..
            this.totalTimeTimer.start()

            prefs = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            editor = prefs!!.edit()


            binding.bLessThan.setOnClickListener {
                if (leftVal < rightVal) {
                    checkForCorrect()
                } else {
                    checkForWrong()
                }
                compare()
            }

            binding.bEqual.setOnClickListener {
                if (leftVal == rightVal) {
                    checkForCorrect()
                } else {
                    checkForWrong()
                }
                compare()

            }

            binding.bGreaterThan.setOnClickListener {
                if (leftVal > rightVal) {
                    checkForCorrect()
                } else {
                    checkForWrong()
                }
                compare()

            }


            compare()

        }
    }

    private fun checkForCorrect() {
        this.correctSolutionTimer.start()
        correct += 10
        binding.scoreLayout.txNumCorrect!!.text = "Correct :$correct"
        binding.scoreLayout.ivCorrect!!.visibility = View.VISIBLE
    }

    private fun checkForWrong() {
        this.correctSolutionTimer.start()
        binding.scoreLayout.ivWrong!!.visibility = View.VISIBLE
    }

    private fun compare() {
        this.questionTimer.cancel()
        this.questionTimer.start()
        val rnd1 = Random()
        var left = rnd1.nextInt(2 - 1 + 1) + 1

        if (left == 1) {
            left = -1
            right = 1
        } else {
            right = -1
            left = 1
        }

        if (left == 1) {
            operatorLeft = "+"
            operatorRight = "-"
        } else {
            operatorLeft = "-"
            operatorRight = "+"
        }


        val rnd = Random()
        val left1 = rnd.nextInt(10)
        val left2 = rnd.nextInt(10)
        val right1 = rnd.nextInt(10)
        val right2 = rnd.nextInt(10)

        leftVal = left1 + (left2 * left)
        rightVal = right1 + (right2 * right)

        binding.txLeft!!.text = left1.toString() + "$operatorLeft" + left2.toString()
        binding.txRight!!.text = right1.toString() + "$operatorRight" + right2.toString()

    }

    private fun cond1() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
                this@CompareActivity)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Main Menu") { dialog, which ->
            val iMain = Intent(this@CompareActivity, StartupActivity::class.java)
            iMain.putExtra("easy", intValue)
            startActivity(iMain)
            finish()
        }

        alertDialog.setNegativeButton("Resume") { dialog, which ->
            this.totalTimeTimer.resume()
            this.questionTimer.resume()
        }

        alertDialog.show()
    }

    private fun cond() {
        this.totalTimeTimer.pause()
        this.questionTimer.pause()
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
                this@CompareActivity)

        val inflater = layoutInflater
        val convertView = inflater.inflate(
                R.layout.result, null)
        alertDialog.setView(convertView)
        val score1 = convertView.findViewById<TextView>(R.id.diaResult)

        score1.text = "Current Score: $newResult"

        val hScore = convertView.findViewById<TextView>(R.id.totalScore)
        score = prefs!!.getInt("key", 0)
        hScore.text = "High Score:  $score"

        alertDialog.setPositiveButton("Main Menu") { dialog, which ->
            val iMain = Intent(this@CompareActivity, StartupActivity::class.java)
            startActivity(iMain)
        }
        alertDialog.setNegativeButton("Retry") { dialog, which ->
            val iMain = Intent(this@CompareActivity, CompareActivity::class.java)
            //   iMain.putExtra("easy", intValue);
            startActivity(iMain)
        }
        alertDialog.show()


    }

    override fun onBackPressed() {
        cond1()
    }


}

