package com.app.jarimanis.ui.thread.detail


import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.jarimanis.data.datasource.models.thread.Doc
import com.app.jarimanis.utils.Key
import com.app.jarimanis.utils.Key.THREADID

class PagerDetailAdapter  (val docId : String?, val doc: Doc?, fm : FragmentManager): FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        val fragment = pages[position]
        val bundleof = bundleOf(THREADID to docId ,
            Key.THREAD to doc)
        fragment.arguments = bundleof

        return fragment
    }

    override fun getCount(): Int {
        return  pages.size
    }

    private val pages = listOf<Fragment>(
        KomentarFragment(),
        ImageFragment(),
        VideoFragment()


    )

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Komentar"
            1 -> "Images"
            else -> "Videos"
        }
    }




}