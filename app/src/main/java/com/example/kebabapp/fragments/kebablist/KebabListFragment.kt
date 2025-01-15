package com.example.kebabapp.fragments.kebablist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kebabapp.KebabDetailPageViewModel
import com.example.kebabapp.KebabPlaceViewModel
import com.example.kebabapp.R

class KebabListFragment : Fragment(), AdapterClass.OnLogoClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var kebabDetailPageViewModel: KebabDetailPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kebab_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        // Initialize ViewModel
        kebabDetailPageViewModel = ViewModelProvider(requireActivity()).get(KebabDetailPageViewModel::class.java)

        getData()
        return view
    }

    private fun getData() {
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        val adapter = AdapterClass(kebabViewModel.getKebabPlaces(), this)
        recyclerView.adapter = adapter
    }

    override fun onLogoClick(itemId: Int) {
        // Set the clicked item's ID in ViewModel
        kebabDetailPageViewModel.setKebabId(itemId)
        Log.i("ADAPTER",kebabDetailPageViewModel.getKebabId().toString())
        // Navigate to the detail page (assuming you have a navigation action set up)
        findNavController().navigate(R.id.action_navigation_list_to_navigation_kebab_detail_page)
    }
}
