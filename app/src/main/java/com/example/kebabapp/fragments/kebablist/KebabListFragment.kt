package com.example.kebabapp.fragments.kebablist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kebabapp.KebabPlaceViewModel
import com.example.kebabapp.R

class KebabListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kebab_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        getData()
        return view
    }

    private fun getData() {
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        recyclerView.adapter = AdapterClass(kebabViewModel.getKebabPlaces())
    }
}
