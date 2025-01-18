package com.example.kebabapp.fragments.kebablist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kebabapp.KebabDetailPageViewModel
import com.example.kebabapp.KebabPlaceViewModel
import com.example.kebabapp.KebabPlaces
import com.example.kebabapp.R

class KebabListFragment : Fragment(), AdapterClass.OnLogoClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var kebabDetailPageViewModel: KebabDetailPageViewModel
    private lateinit var sortSpinner: Spinner
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kebab_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        kebabDetailPageViewModel = ViewModelProvider(requireActivity()).get(KebabDetailPageViewModel::class.java)
        sortSpinner = view.findViewById(R.id.sortSpinner)
        getData()
        setupSortSpinner()



        return view
    }

    private fun getData() {
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        val adapter = AdapterClass(kebabViewModel.getKebabPlaces(), this)
        recyclerView.adapter = adapter
    }
    private fun setupSortSpinner() {
        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
                val unsortedList = kebabViewModel.getKebabPlaces()

                var sortedList = when (position) {
                    0 -> unsortedList.sortedBy { it.name }
                    1 -> unsortedList.sortedByDescending { it.year_opened }
                    else -> unsortedList
                }
                val arraylist = KebabPlaces()
                sortedList.toCollection(arraylist)
                recyclerView.adapter = AdapterClass(arraylist, this@KebabListFragment)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }
    }
    override fun onLogoClick(itemId: Int) {
        kebabDetailPageViewModel.setKebabId(itemId)
        Log.i("ADAPTER", kebabDetailPageViewModel.getKebabId().toString())
        findNavController().navigate(R.id.action_navigation_list_to_navigation_kebab_detail_page)
    }
}
