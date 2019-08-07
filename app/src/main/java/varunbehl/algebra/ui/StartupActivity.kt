package varunbehl.algebra.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import varunbehl.algebra.R
import varunbehl.algebra.adapter.HomeCardAdapter
import varunbehl.algebra.databinding.ActivityMainBinding
import varunbehl.algebra.ui.model.GameModel
import varunbehl.algebra.ui.notUsedNow.ColorActivity
import varunbehl.algebra.util.Constants
import varunbehl.algebra.util.Constants.ADDIITON
import varunbehl.algebra.util.Constants.COLORS
import varunbehl.algebra.util.Constants.COMPARE
import varunbehl.algebra.util.Constants.DIVISION
import varunbehl.algebra.util.Constants.EQUATIONS
import varunbehl.algebra.util.Constants.MULTIPLICATION
import varunbehl.algebra.util.Constants.SUBTRACTION
import java.util.*


class StartupActivity : AppCompatActivity(), HomeCardAdapter.HomeCardListener {
    override fun onCardClick(gameModel: GameModel) {
        if (gameModel.levelExists) {
            askForLevel(gameModel.id)
        } else {
            moveToGame(gameModel.id, -1)
        }
    }

    private var backButtonCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding? = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.recyclerView?.layoutManager = GridLayoutManager(this@StartupActivity, 2)
        binding?.recyclerView?.adapter = HomeCardAdapter(getGameModelList(), this)
    }

    private fun getGameModelList(): ArrayList<GameModel> {
        val gameModelList = ArrayList<GameModel>()

        gameModelList.add(GameModel("Addition", ADDIITON, R.drawable.add_icon, true))
        gameModelList.add(GameModel("Subtraction", SUBTRACTION, R.drawable.sub_icon, true))
        gameModelList.add(GameModel("Multiplication", MULTIPLICATION, R.drawable.multiply_icon, true))
        gameModelList.add(GameModel("EQUATIONS", EQUATIONS, R.drawable.multiply_icon))
        gameModelList.add(GameModel("Compare", COMPARE, R.drawable.multiply_icon))

        return gameModelList
    }

    private fun askForLevel(gameTypeId: Int) {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val convertView = layoutInflater.inflate(R.layout.activity_pause, null)
        builder.setView(convertView)
        builder.setCancelable(true)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        convertView.findViewById<View>(R.id.btnEasy).setOnClickListener {
            dialog.dismiss()
            moveToGame(gameTypeId, Constants.EASY)
        }

        convertView.findViewById<View>(R.id.btnMedium).setOnClickListener {
            dialog.dismiss()
            moveToGame(gameTypeId, Constants.MEDIUM)
        }

        convertView.findViewById<View>(R.id.btnHard).setOnClickListener {
            dialog.dismiss()
            moveToGame(gameTypeId, Constants.HARD)
        }

    }

    private fun moveToGame(gameType: Int, gameLevel: Int) {

        val finalActivity: Activity = when (gameType) {
            MULTIPLICATION -> BasicMathsActivity()
            ADDIITON -> BasicMathsActivity()
            SUBTRACTION -> BasicMathsActivity()
            DIVISION -> BasicMathsActivity()
            COLORS -> ColorActivity()
            EQUATIONS -> EquationSolverActivity()
            COMPARE -> CompareActivity()
            else -> BasicMathsActivity()
        }

        val intent = Intent(this@StartupActivity, finalActivity.javaClass)
        intent.putExtra("easy", gameLevel)
        intent.putExtra("gameType", gameType)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onBackPressed() {
        if (backButtonCount >= 1) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show()
            backButtonCount++
        }
    }


}
