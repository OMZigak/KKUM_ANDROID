package com.teamkkumul.feature.home.meetupdetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamkkumul.feature.home.meetupdetail.lateperson.LatePersonFragment
import com.teamkkumul.feature.home.meetupdetail.meetupinfo.MeetUpInfoFragment
import com.teamkkumul.feature.home.meetupdetail.readystatus.ReadyStatusFragment

class MeetUpDetailVpAdapter(fr: Fragment) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int = TOTAL_TAG_NUM

    override fun createFragment(position: Int): Fragment { // 포지션에 따라 어떤 프레그먼트를 보여줄것인지
        return when (position) {
            0 -> MeetUpInfoFragment()
            1 -> ReadyStatusFragment()
            2 -> LatePersonFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    companion object {
        const val TOTAL_TAG_NUM = 3
    }
}
