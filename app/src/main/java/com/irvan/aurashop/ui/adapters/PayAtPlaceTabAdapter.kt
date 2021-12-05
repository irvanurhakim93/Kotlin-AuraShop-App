package com.irvan.aurashop.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.irvan.aurashop.ui.fragments.ConfirmedDeliveryFragment
import com.irvan.aurashop.ui.fragments.EstimatedDeliveryFragment

@Suppress("DEPRECATION")
internal class PayAtPlaceTabAdapter (
        var context:Context,
        fm: FragmentManager,
        var totaltabs:Int
        ) :

        FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return  totaltabs
    }

    override fun getItem(position: Int): Fragment {
        return  when (position){
            0 -> {
                EstimatedDeliveryFragment()
            }
            1 -> {
                ConfirmedDeliveryFragment()
            }
            else -> getItem(position)
        }
    }
}