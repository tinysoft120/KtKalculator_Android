package com.midstatesrecycling.ktkalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.midstatesrecycling.ktkalculator.databinding.ActivityMainBinding
import com.midstatesrecycling.ktkalculator.fragments.*
import com.midstatesrecycling.ktkalculator.views.BottomNavigationBarTinted
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    val mainViewModel by viewModel<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragments: List<AbsMainFragment>
    private var active: AbsMainFragment? = null
    private val fm get() = supportFragmentManager
    private val bottomNavigationView: BottomNavigationBarTinted get() = binding.bottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationController()
    }

    override fun onResume() {
        super.onResume()
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

    private fun showFragment(fragment: AbsMainFragment, tag: String, position: Int) {
        if (fragment.isAdded) {
            fm.beginTransaction().hide(active!!).show(fragment).commit()
        } else {
            fm.beginTransaction().add(R.id.fragment_container, fragment, tag).commit()
        }
//        fm.beginTransaction()
//            .replace(R.id.fragment_container, fragment, tag)
//            .commit()
        //bottomNavigationView.menu.getItem(position).isChecked = true
        active = fragment
    }

    public fun activePage(position: Int) {
        val menuIds = listOf(
            R.id.action_kalculator,
            R.id.action_result,
            R.id.action_gold_price,
            R.id.action_about)

        if (position in 0 until menuIds.count() ) {
            bottomNavigationView.selectedItemId = menuIds[position]
        }
    }
}