package com.folu.jejakkaki.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.folu.jejakkaki.R
import com.folu.jejakkaki.adapter.SectionsPagerAdapter
import com.folu.jejakkaki.databinding.ActivityDetailBinding
import com.folu.jejakkaki.model.TamanData
import com.folu.jejakkaki.ui.detail.fragments.InfoFragment

class DetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityDetailBinding
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideSystemUI()
        val backButton = binding.backButton

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 100
            this.state=BottomSheetBehavior.STATE_COLLAPSED
        }
        id = intent.getIntExtra("id", 0)
        setUpTabLayoutWithViewPager()
        val selectedTaman = TamanData.taman.find { it.id == id }
        if (selectedTaman != null) {
            Glide.with(this).load(selectedTaman.logo).into(binding.ivLogo)
            Glide.with(this).load(selectedTaman.bgPic).into(binding.ivBg)
            binding.tvNamaTaman.text = selectedTaman.namaTaman

            if (selectedTaman.logo2 != null) {
                Glide.with(this).load(selectedTaman.logo2).into(binding.ivLogo2)
                binding.ivLogo2.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpTabLayoutWithViewPager() {
        val titles = ArrayList(TAB_TITLES.keys)
        val icons = ArrayList(TAB_TITLES.values)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, id)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.isUserInputEnabled = false
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            val tabView = LayoutInflater.from(this).inflate(R.layout.tab_title, tabs,false)
            tabView.findViewById<TextView>(R.id.tab_title).text = resources.getString(titles[position])
            tabView.findViewById<ImageView>(R.id.tab_icon).setImageResource(icons[position])
            tab.customView = tabView
        }.attach()

        for (i in 0..2) {
            if(i==0) {
                tabs.getTabAt(i)?.customView?.findViewById<TextView>(R.id.tab_title)?.apply {
                    visibility = View.VISIBLE
                }
            } else {
                tabs.getTabAt(i)?.customView?.findViewById<TextView>(R.id.tab_title)?.apply {
                    visibility = View.GONE
                }
            }
        }

        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.tab_title)?.apply {
                    visibility = View.GONE
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.tab_title)?.apply {
                    visibility = View.VISIBLE
                }
            }
        })
        tabs.selectTab(tabs.getTabAt(0))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = mutableMapOf(
            R.string.info to R.drawable.ic_outline_info_40,
            R.string.keanekaragaman_hayati to R.drawable.ic_tabler_deer_40,
            R.string.aktifitas to R.drawable.ic_la_hiking_40,
        )
    }
}