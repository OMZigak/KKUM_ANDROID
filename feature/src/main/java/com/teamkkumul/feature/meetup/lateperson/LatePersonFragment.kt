package com.teamkkumul.feature.meetup.lateperson

import androidx.recyclerview.widget.GridLayoutManager
import com.teamkkumul.core.ui.base.BindingFragment
import com.teamkkumul.core.ui.util.context.pxToDp
import com.teamkkumul.feature.R
import com.teamkkumul.feature.databinding.FragmentLatePersonBinding
import com.teamkkumul.feature.utils.itemdecorator.GridSpacingItemDecoration
import com.teamkkumul.model.LatePerson

class LatePersonFragment :
    BindingFragment<FragmentLatePersonBinding>(R.layout.fragment_late_person) {

    private var _latePersonAdapter: LatePersonAdapter? = null
    private val latePersonAdapter get() = requireNotNull(_latePersonAdapter)
    override fun initView() {
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        _latePersonAdapter = LatePersonAdapter()
        binding.rvLatePerson.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = latePersonAdapter
            addItemDecoration(GridSpacingItemDecoration(3, requireContext().pxToDp(8)))
        }
    }

    private fun loadData() {
        val latePersons = listOf(
            LatePerson(1, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(2, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(3, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(4, "혜진 티엘", "https://example.com/image1.jpg"),
            LatePerson(5, "혜진 티엘", "https://example.com/image1.jpg"),
        )
        latePersonAdapter.submitList(latePersons)
    }
}
