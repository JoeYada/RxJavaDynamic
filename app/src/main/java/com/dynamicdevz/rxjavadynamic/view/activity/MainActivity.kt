package com.dynamicdevz.rxjavadynamic.view.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.dynamicdevz.rxjavadynamic.R
import com.dynamicdevz.rxjavadynamic.databinding.ActivityMainBinding
import com.dynamicdevz.rxjavadynamic.model.data.Result
import com.dynamicdevz.rxjavadynamic.view.adapter.HomeFragmentAdapter
import com.dynamicdevz.rxjavadynamic.view.fragment.RickyDetailsFragment
import com.dynamicdevz.rxjavadynamic.view.fragment.RickySelector
import com.dynamicdevz.rxjavadynamic.viewmodel.RickyViewModel

class MainActivity : AppCompatActivity(), RickySelector {

    private val FIRST_TIME = "FIRST_TIME"
    private val viewModel by viewModels<RickyViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var hFA: HomeFragmentAdapter

    private fun SharedPreferences.firstTimeUsage(): Boolean {
        val fTime = this.getBoolean(FIRST_TIME, true)
        this.edit().putBoolean(FIRST_TIME, false).apply()
        return fTime
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hFA = HomeFragmentAdapter(supportFragmentManager)
        binding.mainVp.adapter = hFA

        binding.mainVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.mainMenu.selectedItemId =
                    if (position == 0) R.id.list_menu_item else R.id.grid_menu_item
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        binding.mainMenu.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.list_menu_item -> {
                    binding.mainVp.currentItem = 0
                }
                else -> {
                    binding.mainVp.currentItem = 1
                }
            }
            true
        }

        if (getSharedPreferences(packageName, Context.MODE_PRIVATE).firstTimeUsage()) {
            displayTutorial()
        }
    }

    override fun openDetailsFragment(result: Result) {
        val fragment = RickyDetailsFragment.getInstance(result)

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            .add(R.id.details_frame, fragment)
            .addToBackStack(fragment.tag)
            .commit()
    }

    private fun displayTutorial() {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.Base_Theme_AppCompat))
            .setTitle(getString(R.string.tutorial_text))
            .setMessage(
                getString(R.string.tutorial_message_text)
            ).setPositiveButton(getString(R.string.ok_text)) { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }


}