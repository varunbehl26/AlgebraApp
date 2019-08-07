package varunbehl.algebra.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import varunbehl.algebra.R
import varunbehl.algebra.adapter.NumbersAdapter
import varunbehl.algebra.databinding.ActivityEquationSolverBinding
import varunbehl.algebra.util.CountDownTimer
import java.util.*


class EquationSolverActivity : AppCompatActivity(), NumbersAdapter.NumberListener {

    private lateinit var binding: ActivityEquationSolverBinding
    private var correct = 0
    private var z: Int = 0
    private var score: Int = 0
    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private val arr = mutableListOf(1, 2, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21)
    private var flag = false

    private val totalTimeTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.scoreLayout.txTotalTimeReverse.text = "Total Time : " + millisUntilFinished / 1000
        }

        override fun onFinish() {

            score = prefs!!.getInt("key", 0)
            if (score < correct) {
                editor!!.putInt("key", correct)
                editor!!.apply()
            }
            if (!isFinishing)
                cond()
        }
    }
    private val questionTimer = object : CountDownTimer(20000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            binding.scoreLayout.txTimeReverse.text = "Time : " + millisUntilFinished / 1000
        }

        override fun onFinish() {
            correctSolutionTimer.start()
            if (flag) {
                binding.scoreLayout.ivCorrect!!.visibility = View.VISIBLE
            } else {
                binding.scoreLayout.ivWrong!!.visibility = View.VISIBLE
            }
            FOO()

        }
    }
    private val correctSolutionTimer = object : CountDownTimer(250, 250) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            binding.scoreLayout.ivCorrect!!.visibility = View.INVISIBLE
            binding.scoreLayout.ivWrong!!.visibility = View.INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_equation_solver)
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
            this.totalTimeTimer.start()
            prefs = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            editor = prefs!!.edit()

            val rnd12 = Random()
            val k = rnd12.nextInt(7)
            rotate(arr, k)


            binding.bCheck.setOnClickListener { checkSolution() }
            binding.berase.setOnClickListener {
                if (binding.res1!!.text.toString().matches("".toRegex()) && binding.res2!!.text.toString().matches("".toRegex()) && binding.res3!!.text.toString().matches("".toRegex())) {
                    Toast.makeText(applicationContext, "No number to erase .", Toast.LENGTH_LONG).show()
                } else if (!binding.res3!!.text.toString().matches("".toRegex())) {
                    binding.res3!!.text?.clear()
                } else if (!binding.res2!!.text.toString().matches("".toRegex())) {

                    binding.res2!!.text?.clear()
                } else if (!binding.res1!!.text.toString().matches("".toRegex())) {

                    binding.res1!!.text?.clear()
                }
            }
            FOO()

            binding.numbersRecyclerView.layoutManager = GridLayoutManager(this@EquationSolverActivity, 4)
            binding.numbersRecyclerView.adapter = NumbersAdapter(arr, this)

            binding.res3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    checkSolution()
                }
            })
        }
    }

    private fun checkSolution() {
        if (binding.res1!!.text.toString().matches("".toRegex()) || binding.res2!!.text.toString().matches("".toRegex()) || binding.res3!!.text.toString().matches("".toRegex())) {
            Toast.makeText(applicationContext, "Please tap the number to add numbers.", Toast.LENGTH_LONG).show()
            Handler().postDelayed({
                binding.res1.startAnimation(shakeError())
                binding.res2.startAnimation(shakeError())
                binding.res3.startAnimation(shakeError())
            }, 1000)

        } else if (!binding.res1!!.text.toString().matches("".toRegex()) || !binding.res2!!.text.toString().matches("".toRegex()) || !binding.res3!!.text.toString().matches("".toRegex())) {
            run {
                val a = Integer.parseInt(binding.res1!!.text.toString().trim { it <= ' ' })
                val b = Integer.parseInt(binding.res2!!.text.toString().trim { it <= ' ' })
                val c = Integer.parseInt(binding.res3!!.text.toString().trim { it <= ' ' })
                if (z == a + b + c) {
                    flag = true

                    binding.scoreLayout.ivCorrect!!.visibility = View.VISIBLE
                    correctSolutionTimer.start()
                    correct += 10
                    binding.scoreLayout.txNumCorrect.text = "Correct : $correct"
                } else {
                    binding.scoreLayout.ivWrong!!.visibility = View.VISIBLE
                    correctSolutionTimer.start()
                    binding.res1!!.text?.clear()
                    binding.res2!!.text?.clear()
                    binding.res3!!.text?.clear()
                }
            }
            FOO()
        }
    }


    private fun shakeError(): Animation {
        val shake = TranslateAnimation(0f, 10f, 0f, 0f)
        shake.duration = 500
        shake.interpolator = CycleInterpolator(7f)
        return shake
    }

    private fun cond1() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@EquationSolverActivity)
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Main Menu") { dialog, which ->
            val iMain = Intent(this@EquationSolverActivity, StartupActivity::class.java)
            startActivity(iMain)
        }

        alertDialog.setNegativeButton("Resume") { dialog, which ->
            this.totalTimeTimer.resume()
//            up!!.visibility = View.VISIBLE
        }

        alertDialog.show()
    }

    private fun cond() {
        this.totalTimeTimer.pause()
        this.questionTimer.pause()
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(
                this@EquationSolverActivity)

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
            val iMain = Intent(this@EquationSolverActivity, StartupActivity::class.java)
            startActivity(iMain)
        }
        alertDialog.setNegativeButton("Retry") { dialog, which ->
            val iMain = Intent(this@EquationSolverActivity, EquationSolverActivity::class.java)
            //    iMain.putExtra("easy", intValue);
            startActivity(iMain)
        }
        //added if over here
        if (!isFinishing) {
            alertDialog.show()
        }

    }


    private fun FOO() {
        flag = false
        this.questionTimer.cancel()
        this.questionTimer.start()
        score = prefs!!.getInt("key", 0)
        if (score < correct) {
            editor!!.putInt("key", correct)
            editor!!.commit()
        }
        val rnd = Random()
        binding.txAns!!.text = ""
        binding.res1!!.text?.clear()
        binding.res2!!.text?.clear()
        binding.res3!!.text?.clear()
        var `var` = rnd.nextInt(36)
        if (`var` < 6) {
            `var` += 7
        }
         binding.txAns!!.text = `var`.toString()
        z = `var`
    }

    override fun onNumberClick(number: Int) {
        val s1 = binding.res1!!.text.toString()
        when {
            s1.matches("".toRegex()) -> binding.res1!!.text = Editable.Factory.getInstance().newEditable(number.toString())
            binding.res2!!.text.toString().matches("".toRegex()) -> binding.res2!!.text = Editable.Factory.getInstance().newEditable(number.toString())
            binding.res3!!.text.toString().matches("".toRegex()) -> binding.res3!!.text = Editable.Factory.getInstance().newEditable(number.toString())
        }
    }

    private fun rotate(arr: MutableList<Int>, k: Int) {
        var k = k
        if (k > arr.size)
            k %= arr.size

        val result = IntArray(arr.size)
        Collections.rotate(arr, k)

        for ((j, i) in (k until arr.size).withIndex()) {
            result[i] = arr[j]
        }

    }

    override fun onBackPressed() {
        cond1()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            cond1()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}


