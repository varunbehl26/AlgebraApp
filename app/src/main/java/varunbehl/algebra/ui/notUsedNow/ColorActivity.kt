package varunbehl.algebra.ui.notUsedNow

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import varunbehl.algebra.R
import varunbehl.algebra.ui.StartupActivity
import varunbehl.algebra.util.CountDownTimer
import java.util.*

/**
 * Created by varunbehl on 24/08/15.
 */
class ColorActivity : AppCompatActivity() {


    private var ivCorrect: ImageView? = null
    private var ivWrong: ImageView? = null
    private var iv: ImageView? = null
    private var score: Int = 0
    private var correct = 0
    private val intValue = 0
    private var col = 0
    private val total = 60000
    private var timer: TextView? = null
    private var numTimes: TextView? = null
    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private val colorsArray = arrayOf("Red", "Blue", "Black", "Purple", "Green", "Brown", "Yellow")

    private val totalTimeTimer = object : CountDownTimer(total.toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val time_rem = millisUntilFinished.toInt()
        }

        override fun onFinish() {

            score = prefs!!.getInt("key", 0)
            if (score < correct) {
                editor!!.putInt("key", correct)
                editor!!.commit()
            }
            if (!isFinishing)
                pauseFunctionality()
        }
    }

    private val questionTimer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timer!!.text = "Time:" + millisUntilFinished / 1000
        }

        override fun onFinish() {
            color()
        }
    }

    private val correctSolutionTimer = object : CountDownTimer(125, 125) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            ivCorrect!!.visibility = View.INVISIBLE
            ivWrong!!.visibility = View.INVISIBLE
        }
    }
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colorgame)
        val settings = getSharedPreferences("MyPrefs", 0)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        if (settings.getBoolean("is_first_time_add", true)) {
            //the app is being launched for first time, do something
            Log.d("TAG", "First time")

            // first time task
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("is_first_time_add", false).apply()
        } else {
            //second time launch..
            totalTimeTimer.start()
            timer = findViewById(R.id.tx_time_reverse)

            prefs = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            editor = prefs!!.edit()
            color()
            questionTimer.start()

            numTimes = findViewById(R.id.tx_num_correct)

            ivCorrect = findViewById(R.id.ivCorrect)

            ivWrong = findViewById(R.id.ivWrong)

            val ibRed = findViewById<ImageButton>(R.id.tv_red)
            ibRed.setOnClickListener {
                if (col == 0) {
                    correctUi()
                } else {
                    inCorrectUI()
                }
                color()

            }

            val ibBlue = findViewById<ImageButton>(R.id.tv_blue)
            ibBlue.setOnClickListener {
                if (col == 1) {
                    correctUi()
                } else {
                    inCorrectUI()
                }
                color()
            }

            val ibBlack = findViewById<ImageButton>(R.id.tv_black)
            ibBlack.setOnClickListener {
                if (col == 2) {
                    correctUi()
                } else {
                    inCorrectUI()
                }
                color()
            }
            val ibPurple = findViewById<ImageButton>(R.id.tv_purple)
            ibPurple.setOnClickListener {
                if (col == 3) {
                    correctUi()
                } else {
                    inCorrectUI()
                }
                color()
            }


            val ibGreen = findViewById<ImageButton>(R.id.tv_green)
            ibGreen.setOnClickListener {
                if (col == 4) {
                    correctUi()
                } else {
                    inCorrectUI()
                }
                color()
            }


            val ibBrown = findViewById<ImageButton>(R.id.tv_brown)
            ibBrown.setOnClickListener {
                if (col == 5) {
                    correctUi()
                } else {
                    inCorrectUI()
                }
                color()
            }


            val ibYellow = findViewById<ImageButton>(R.id.tv_yellow)
            ibYellow.setOnClickListener {
                if (col == 6) {
                    correctUi()
                } else {
                    inCorrectUI()
                }
                color()
            }
        }
    }

    private fun inCorrectUI() {
        correctSolutionTimer.start()
        ivWrong!!.visibility = View.VISIBLE
    }

    private fun correctUi() {
        correct += 10
        ivCorrect!!.visibility = View.VISIBLE
        correctSolutionTimer.start()
        numTimes!!.text = "Correct:$correct"
    }

    private fun findAndSetColor(col: Int, view: View?) {
        when (col) {
            0 -> view!!.setBackgroundColor(this.resources.getColor(R.color.red))
            1 -> view!!.setBackgroundColor(this.resources.getColor(R.color.blue))
            2 -> view!!.setBackgroundColor(this.resources.getColor(R.color.black))
            3 -> view!!.setBackgroundColor(this.resources.getColor(R.color.purple))
            4 -> view!!.setBackgroundColor(this.resources.getColor(R.color.green))
            5 -> view!!.setBackgroundColor(this.resources.getColor(R.color.brown))
            6 -> view!!.setBackgroundColor(this.resources.getColor(R.color.yellow))
            else -> view!!.setBackgroundColor(this.resources.getColor(R.color.white))
        }
    }

    private fun color() {
        questionTimer.cancel()
        questionTimer.start()
        val rnd = Random()
        val rnd2 = Random()
        val backGround = rnd2.nextInt(7)

        iv = findViewById(R.id.iVColor)
        findAndSetColor(backGround, iv)
        val tv = findViewById<TextView>(R.id.tvcolor)
        tv.text = colorsArray[rnd2.nextInt(6)]
        var textViewCol = rnd.nextInt(7)
        if (textViewCol == backGround)
            textViewCol--
        col = textViewCol
        findAndSetColor(col,tv)
        
        score = prefs!!.getInt("key", 0)
        if (score < correct) {
            editor!!.putInt("key", correct)
            editor!!.apply()
        }

    }

    private fun pauseFunctionality() {
        totalTimeTimer.pause()
        questionTimer.pause()
        val alertDialog = AlertDialog.Builder(
                this@ColorActivity)

        val inflater = layoutInflater
        val convertView = inflater.inflate(
                R.layout.result, null)
        alertDialog.setView(convertView)
        val score1 = convertView.findViewById<TextView>(R.id.diaResult)

        score1.text = "Current Score: $correct"

        val hScore = convertView.findViewById<TextView>(R.id.totalScore)
        score = prefs!!.getInt("key", 0)
        hScore.text = "High Score: $score"

        alertDialog.setPositiveButton("Main Menu") { dialog, which ->
            val iMain = Intent(this@ColorActivity, StartupActivity::class.java)
            startActivity(iMain)
        }
        alertDialog.setNegativeButton("Retry") { dialog, which ->
            val iMain = Intent(this@ColorActivity, ColorActivity::class.java)
            iMain.putExtra("easy", intValue)
            startActivity(iMain)
        }
        alertDialog.show()
    }


    override fun onBackPressed() {
        pauseFunctionality()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            pauseFunctionality()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

