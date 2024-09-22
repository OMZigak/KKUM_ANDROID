package com.teamkkumul.feature.meetup

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamkkumul.feature.meetup.lateperson.LatePersonFragment
import com.teamkkumul.feature.meetup.meetupdetail.MeetUpDetailFragment
import com.teamkkumul.feature.meetup.readystatus.readystatus.ReadyStatusFragment

class MeetUpContainerVpAdapter(
    fragment: Fragment,
    private val promiseId: Int,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = TOTAL_TAG_NUM

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MeetUpDetailFragment.newInstance(promiseId)
            1 -> ReadyStatusFragment.newInstance(promiseId)
            2 -> LatePersonFragment.newInstance(promiseId)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    companion object {
        const val TOTAL_TAG_NUM = 3
    }
}
