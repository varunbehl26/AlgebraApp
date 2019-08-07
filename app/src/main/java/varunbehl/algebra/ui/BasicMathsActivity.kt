package varunbehl.algebra.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import varunbehl.algebra.R
import varunbehl.algebra.databinding.ActivityAdditionBinding
import varunbehl.algebra.util.Constants
import varunbehl.algebra.view.ScoreTopView
import java.util.*

/**
 * Created by varunbehl on 19/08/15.
 */
class BasicMathsActivity : AppCompatActivity() ,ScoreTopView.ScoreViewTimersListeners{


    private lateinit var binding: ActivityAdditionBinding

    private var gameLevel = 0
    private var gameType: Int = 0

    private var actualSolution: Int = 0

    private var correct = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addition)
        val settings = getSharedPreferences("MyPrefs", 0)
        val mIntent = intent
        gameLevel = mIntent.getIntExtra("easy", 0)
        gameType = mIntent.getIntExtra("gameType", 0)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        if (settings.getBoolean("is_first_time_add", true)) {
            //the app is being launched for first time, do something
            Log.d("TAG", "First time")

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("is_first_time_add", false).apply()
        } else {
            //second time launch..
            binding.scoreLayout.totalTimeTimer.start()

            binding.etSolution.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    if (s.toString() != "") {
                        if (actualSolution == Integer.parseInt(s.toString())) {
                            checkForCorrect()
                        } else {
                            Handler().postDelayed({
                                binding.etSolution.startAnimation(shakeError())
                            }, 1000)
                        }
                    }
                }
            })

            initUI()
        }
    }

    private fun checkForCorrect() {
        correct += 10

        binding.scoreLayout.updateCorrectView(correct)

        initUI()

        binding.etSolution.text.clear()

    }

    private fun shakeError(): Animation {
        val shake = TranslateAnimation(0f, 10f, 0f, 0f)
        shake.duration = 500
        shake.interpolator = CycleInterpolator(7f)
        return shake
    }

    private fun onPausePressed() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
                this@BasicMathsActivity)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Main Menu") { dialog, which ->
            val iMain = Intent(this@BasicMathsActivity, StartupActivity::class.java)
            iMain.putExtra("easy", gameLevel)
            startActivity(iMain)
            finish()
        }

        alertDialog.setNegativeButton("Resume") { dialog, which ->
            binding.scoreLayout.resume()

        }

        alertDialog.show()
    }

    private fun initUI() {
        binding.etSolution.setText("")
        binding.scoreLayout.reset(correct)

        binding.etSolution?.requestFocus()

        when (gameType) {
            Constants.SUBTRACTION -> {
                subtraction()
            }
            Constants.MULTIPLICATION -> {
                multiplication()
            }
            Constants.DIVISION -> {
                division()

            }
            else -> {
                addition()
            }
        }
    }

    private fun addition() {
        binding.txOperationSymbol.text = "+"
        val (i1, i2) = getStartEnd()
        binding.txQ1.text = i1.toString()
        binding.txQ2.text = i2.toString()
        actualSolution = i1 + i2
    }

    private fun subtraction() {
        binding.txOperationSymbol.text = "-"
        val (i1, i2) = getStartEnd()
        if (i1 > i2) {
            binding.txQ1.text = i1.toString()
            binding.txQ2.text = i2.toString()
            actualSolution = i1 - i2
        } else {
            binding.txQ1.text = i2.toString()
            binding.txQ2.text = i1.toString()
            actualSolution = i2 - i1
        }
    }

    private fun multiplication() {
        binding.txOperationSymbol.text = "*"
        val (i1, i2) = getStartEnd()
        binding.txQ1.text = i1.toString()
        binding.txQ2.text = i2.toString()
        actualSolution = i1 * i2
    }

    private fun division() {
        binding.txOperationSymbol.text = "/"
        val (i1, i2) = getStartEnd()
        binding.txQ1.text = i1.toString()
        binding.txQ2.text = i2.toString()
        actualSolution = i1 / i2

    }

    private fun getStartEnd(): Pair<Int, Int> {
        val r = Random()
        val start: Int
        val end: Int
        when (gameLevel) {
            Constants.EASY -> {
                start = 10
                end = 2
            }
            Constants.MEDIUM -> {
                start = 50
                end = 10
            }
            Constants.HARD -> {
                start = 100
                end = 10
            }
            else -> {
                start = 10
                end = 1
            }
        }
        val i1 = r.nextInt(start - end) + end
        val i2 = r.nextInt(start - end) + end
        return Pair(i1, i2)
    }


    override fun onQuestionTimerFinish() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTotalTimerFinish() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onBackPressed() {
        onPausePressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onPausePressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

