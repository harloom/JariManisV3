package com.app.jarimanis.ui.dashboard

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.viewpager.widget.ViewPager
import com.app.jarimanis.R
import com.app.jarimanis.data.datasource.local.MenuData
import com.app.jarimanis.data.datasource.models.profile.Result
import com.bumptech.glide.Glide
import com.takusemba.spotlight.OnSpotlightStateChangedListener
import com.takusemba.spotlight.OnTargetStateChangedListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle
import com.takusemba.spotlight.target.SimpleTarget
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {



    private val  vm: DashboardViewModel by viewModel()
    private lateinit var viewPager: ViewPager
    private lateinit var adpaterPager: PagerDashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!MenuData.sportLineDashboard){
            guidLineSpot()
        }

    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title ="Akun Saya"
        vm.myProfile.observe(this@DashboardFragment,subcribeProfile)
        buttonProfileOnclick()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPager()
    }


    private fun initPager(){
        adpaterPager = PagerDashboard(childFragmentManager)
        viewPager = pager_detail
        val offset = 4
        viewPager.offscreenPageLimit  =offset
        viewPager.adapter = adpaterPager
        viewPager.addOnPageChangeListener(pageOnScrool)
        val tabLayout = tab_layout
        tabLayout.setupWithViewPager(viewPager)
    }

    private val pageOnScrool = object  : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {

        }

    }





    /*   init Profile   */

    private fun buttonProfileOnclick() {
        btnPengaturanUser.setOnClickListener {
          findNavController().navigate(R.id.action_navigation_dashboard_to_pengaturanAkunFragment)
        }
    }
    private val subcribeProfile = Observer<Result> {
        initProfile(it)
    }

    private  fun initProfile(items : Result){
        try {
            tv_user.text = items.nameUser!!.capitalize()
            Glide.with(this@DashboardFragment).load(items.thumbail).placeholder(R.drawable.rounded_shimmer).into(iv_thumbail)
        }catch (e: Exception){
            Toast.makeText(context!!,"Something Erorr, please refress!" , Toast.LENGTH_LONG).show()
        }
    }

    private fun guidLineSpot(){
        val display = activity!!.windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        // Display : X :  1080 , Y 1794
        val homeTarget = SimpleTarget.Builder(activity!!)
            .setPoint((point.x /2).toFloat(), (point.y /2).toFloat())
            .setShape(  RoundedRectangle(400f,(point.x /1.1).toFloat(), 5.0F)) // or RoundedRectangle()
            .setTitle("Info")
            .setDescription("Tahan Click untuk mengedit atau menghapus")
            .setOverlayPoint(100f, 100f)
            .setOnSpotlightStartedListener(object  : OnTargetStateChangedListener<SimpleTarget> {
                override fun onStarted(target: SimpleTarget?) {
                }
                override fun onEnded(target: SimpleTarget?) {
                }

            })
            .build()





        Spotlight.with(activity!!)
            .setOverlayColor(R.color.background)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .setTargets(homeTarget)
            .setClosedOnTouchedOutside(true)
            .setOnSpotlightStateListener(object  : OnSpotlightStateChangedListener {
                override fun onStarted() {
                    Toast.makeText(context, "spotlight is started", Toast.LENGTH_SHORT).show()
                }

                override fun onEnded() {
                    MenuData.sportLineDashboard = true
                }

            })
            .start()

    }


    /*     */

    override fun onDestroyView() {
        super.onDestroyView()

    }
}