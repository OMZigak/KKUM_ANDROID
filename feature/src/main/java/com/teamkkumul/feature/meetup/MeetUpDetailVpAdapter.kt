package com.teamkkumul.feature.meetup

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamkkumul.feature.meetup.lateperson.LatePersonFragment
import com.teamkkumul.feature.meetup.meetupdetail.MeetUpDetailFragment
import com.teamkkumul.feature.meetup.readystatus.ReadyStatusFragment

class MeetUpDetailVpAdapter(fr: Fragment) : FragmentStateAdapter(fr) {
    override fun getItemCount(): Int = TOTAL_TAG_NUM

    override fun createFragment(position: Int): Fragment { // 포지션에 따라 어떤 프레그먼트를 보여줄것인지
        return when (position) {
            0 -> MeetUpDetailFragment()
            1 -> ReadyStatusFragment()
            2 -> LatePersonFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    companion object {
        const val TOTAL_TAG_NUM = 3
    }
}
