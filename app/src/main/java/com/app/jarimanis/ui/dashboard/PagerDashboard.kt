package com.app.jarimanis.ui.dashboard


import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.Key.THREADID

class PagerDashboard  ( fm : FragmentManager): FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val fragment = pages[position]
        return fragment
    }

    override fun getCount(): Int {
        return  pages.size
    }

    private val pages = listOf<Fragment>(
        ThreadMeFragment()


    )

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Thread Saya"
            else -> "Undefined"
        }
    }




}