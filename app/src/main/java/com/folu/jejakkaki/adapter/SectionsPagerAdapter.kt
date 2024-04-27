package com.folu.jejakkaki.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.folu.jejakkaki.ui.detail.fragments.AktivitasFragment
import com.folu.jejakkaki.ui.detail.fragments.BioFragment
import com.folu.jejakkaki.ui.detail.fragments.InfoFragment

class SectionsPagerAdapter(fragment: FragmentActivity, private val id: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment
        when (position) {
            0 -> {
                fragment = InfoFragment()
                // Pass the id to InfoFragment
                (fragment as InfoFragment).arguments = Bundle().apply {
                    putInt("id", id)
                }
            }
            1 -> {
                fragment = BioFragment()
                // Pass the id to HewanFragment
                (fragment as BioFragment).arguments = Bundle().apply {
                    putInt("id", id)
                }
            }
            else -> {
                fragment = AktivitasFragment()
                // Pass the id to AktifitasFragment
                (fragment as AktivitasFragment).arguments = Bundle().apply {
                    putInt("id", id)
                }
            }
        }
        return fragment
    }
}