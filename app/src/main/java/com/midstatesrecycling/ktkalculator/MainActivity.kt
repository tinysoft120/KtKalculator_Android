package com.midstatesrecycling.ktkalculator

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.midstatesrecycling.ktkalculator.databinding.ActivityMainBinding
import com.midstatesrecycling.ktkalculator.fragments.AboutFragment
import com.midstatesrecycling.ktkalculator.fragments.CalculatorFragment
import com.midstatesrecycling.ktkalculator.fragments.GoldPriceFragment
import com.midstatesrecycling.ktkalculator.fragments.ResultFragment
import com.midstatesrecycling.ktkalculator.util.ColorUtil
import com.midstatesrecycling.ktkalculator.views.BottomNavigationBarTinted
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    val mainViewModel by viewModel<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragments: List<Fragment>
    private var active: Fragment? = null
    private val fm get() = supportFragmentManager
    private val bottomNavigationView: BottomNavigationBarTinted get() = binding.bottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationController()
    }

    private fun setupNavigationController() {
        fragments = listOf(CalculatorFragment(), ResultFragment(), GoldPriceFragment(), AboutFragment())

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_kalculator -> showFragment(fragments[0], "0", 0)
                R.id.action_result -> showFragment(fragments[1], "1", 1)
                R.id.action_gold_price -> showFragment(fragments[2], "2", 2)
                R.id.action_about -> showFragment(fragments[3], "3", 3)
                else -> return@setOnItemSelectedListener false
            }
            return@setOnItemSelectedListener true
        }
        bottomNavigationView.setOnItemReselectedListener {
            // scroll to top
        }

        showFragment(fragments[0], "0", 0)
    }

    private fun showFragment(fragment: Fragment, tag: String, position: Int) {
        if (fragment.isAdded) {
            fm.beginTransaction().hide(active!!).show(fragment).commit();
        } else {
            fm.beginTransaction().add(R.id.fragment_container, fragment, tag).commit();
        }
//        fm.beginTransaction()
//            .replace(R.id.fragment_container, fragment, tag)
//            .commit()
        //bottomNavigationView.menu.getItem(position).isChecked = true
        active = fragment
    }


    private fun setLightStatusbar(enabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = this.window.decorView
            val systemUiVisibility = decorView.systemUiVisibility
            if (enabled) {
                decorView.systemUiVisibility =
                    systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility =
                    systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }

    fun setLightStatusbarAuto(bgColor: Int) {
        setLightStatusbar(ColorUtil.isColorLight(bgColor))
    }
    fun setBottomBarVisibility(visible: Boolean) {
        binding.bottomNavigationView.isVisible = visible
    }
}