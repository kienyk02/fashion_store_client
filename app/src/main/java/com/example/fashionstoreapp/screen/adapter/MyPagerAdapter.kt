package com.example.fashionstoreapp.screen.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fragment: FragmentActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}