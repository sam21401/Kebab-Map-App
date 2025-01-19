package com.example.kebabapp.fragments.kebablist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kebabapp.KebabDetailPageViewModel
import com.example.kebabapp.KebabPlaceViewModel
import com.example.kebabapp.KebabPlaces
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentKebabListBinding
import com.example.kebabapp.utilities.KebabService
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class KebabListFragment : Fragment(), AdapterClass.OnLogoClickListener {
    private lateinit var binding: FragmentKebabListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var kebabDetailPageViewModel: KebabDetailPageViewModel
    private lateinit var sortSpinner: Spinner
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var btnOpenDrawer: ImageView
    private lateinit var radioButtonClosed: RadioButton
    private lateinit var radioButtonOpen: RadioButton
    private lateinit var checkboxCraft: CheckBox
    private lateinit var checkboxStall: CheckBox
    private lateinit var checkboxChain: CheckBox
    private lateinit var checkboxMeatChicken: CheckBox
    private lateinit var checkboxMeatBeef: CheckBox
    private lateinit var checkboxMeatFalafel: CheckBox
    private lateinit var checkboxSauceMild: CheckBox
    private lateinit var checkboxSauceGarlic: CheckBox
    private lateinit var checkboxSauceSpicy: CheckBox
    private lateinit var checkboxSauceKetchup: CheckBox
    private lateinit var checkboxSauceMixed: CheckBox
    private lateinit var checkboxOrderingOnSite: CheckBox
    private lateinit var checkboxOrderingDelivery: CheckBox
    private lateinit var checkboxOrderingTakeaway: CheckBox
    private lateinit var checkboxOrderingGlovo: CheckBox
    private lateinit var checkboxOrderingPyszne: CheckBox
    private lateinit var checkboxOrderingUberEats: CheckBox
    private lateinit var checkboxOrderingWebsite: CheckBox
    private lateinit var checkboxOrderingApp: CheckBox
    private lateinit var checkboxOrderingPhone: CheckBox
    private lateinit var buttonFilter: Button
    private lateinit var buttonClear: Button
    private lateinit var kebabTotal: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentKebabListBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_kebab_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        kebabDetailPageViewModel = ViewModelProvider(requireActivity()).get(KebabDetailPageViewModel::class.java)
        sortSpinner = view.findViewById(R.id.sortSpinner)
        radioButtonClosed = view.findViewById(R.id.radioButton1)
        radioButtonOpen = view.findViewById(R.id.radioButton2)
        checkboxCraft = view.findViewById(R.id.checkboxCraft)
        checkboxStall = view.findViewById(R.id.checkboxStall)
        checkboxChain = view.findViewById(R.id.checkboxChain)
        checkboxMeatChicken = view.findViewById(R.id.checkboxMeatChicken)
        checkboxMeatBeef = view.findViewById(R.id.checkboxMeatBeef)
        checkboxMeatFalafel = view.findViewById(R.id.checkboxMeatFalafel)
        checkboxSauceMild = view.findViewById(R.id.checkboxSauceMild)
        checkboxSauceGarlic = view.findViewById(R.id.checkboxSauceGarlic)
        checkboxSauceSpicy = view.findViewById(R.id.checkboxSauceSpicy)
        checkboxSauceKetchup = view.findViewById(R.id.checkboxSauceKetchup)
        checkboxSauceMixed = view.findViewById(R.id.checkboxSauceMixed)
        //Ordering options
        checkboxOrderingOnSite = view.findViewById(R.id.checkboxOrderingOnSite)
        checkboxOrderingDelivery = view.findViewById(R.id.checkboxOrderingDelivery)
        checkboxOrderingTakeaway = view.findViewById(R.id.checkboxOrderingTakeaway)
        checkboxOrderingGlovo = view.findViewById(R.id.checkboxOrderingGLOVO)
        checkboxOrderingPyszne = view.findViewById(R.id.checkboxOrderingPyszne)
        checkboxOrderingUberEats = view.findViewById(R.id.checkboxOrderingUber)
        checkboxOrderingWebsite = view.findViewById(R.id.checkboxOrderingWebsite)
        checkboxOrderingApp = view.findViewById(R.id.checkboxOrderingApp)
        checkboxOrderingPhone = view.findViewById(R.id.checkboxOrderingPhone)
        buttonFilter = view.findViewById(R.id.btnFilter)
        buttonClear = view.findViewById(R.id.btnFilterClear)
        getData()
        setupSortSpinner()
        drawerLayout = view.findViewById(R.id.drawer_layout)
        navView = view.findViewById(R.id.nav_view)
        btnOpenDrawer = view.findViewById(R.id.btn_open_drawer)
        kebabTotal = view.findViewById(R.id.kebabTotal)
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        btnOpenDrawer.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }
        buttonFilter.setOnClickListener {
            applyFilters()
            setTotalKebabs()
        }

        buttonClear.setOnClickListener {
            kebabViewModel.clearFilteredKebabPlaces()
            getData()
            setTotalKebabs()
        }
        setTotalKebabs()
        return view
    }

    private fun setTotalKebabs() {
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        val kebabsTotalAmount =  kebabViewModel.getKebabsAmount().toString()
        kebabTotal.text = "Total kebab amount: " + kebabsTotalAmount
    }

    private fun applyFilters() {
        val filters = mutableMapOf<String, String>()
        filters["status"] = when {
            radioButtonClosed.isChecked -> "CLOSED"
            radioButtonOpen.isChecked -> "OPEN"
            else -> ""
        }
        filters["is_craft"] = when {
            checkboxCraft.isChecked -> "true"
            else -> ""
        }
        filters["is_in_stall"] = when {
            checkboxStall.isChecked -> "true"
            else -> ""
        }
        filters["is_chain_member"] = when {
            checkboxChain.isChecked -> "true"
            else -> ""
        }
        val meatTypes = mutableListOf<String>()
        if (checkboxMeatChicken.isChecked) meatTypes.add("chicken")
        if (checkboxMeatBeef.isChecked) meatTypes.add("beef")
        if (checkboxMeatFalafel.isChecked) meatTypes.add("falafel")
        filters["meat_types"] = meatTypes.joinToString(",")
        val sauces = mutableListOf<String>()
        if (checkboxSauceMild.isChecked) sauces.add("mild")
        if (checkboxSauceGarlic.isChecked) sauces.add("garlic")
        if (checkboxSauceSpicy.isChecked) sauces.add("spicy")
        if (checkboxSauceKetchup.isChecked) sauces.add("ketchup")
        if (checkboxSauceMixed.isChecked) sauces.add("mixed")
        filters["sauces"] = sauces.joinToString(",")
        val orderingOptions = mutableListOf<String>()
        if (checkboxOrderingOnSite.isChecked) orderingOptions.add("on-site")
        if (checkboxOrderingDelivery.isChecked) orderingOptions.add("delivery")
        if (checkboxOrderingTakeaway.isChecked) orderingOptions.add("takeaway")
        if (checkboxOrderingGlovo.isChecked) orderingOptions.add("GLOVO")
        if (checkboxOrderingPyszne.isChecked) orderingOptions.add("Pyszne.pl")
        if (checkboxOrderingUberEats.isChecked) orderingOptions.add("UberEats")
        if (checkboxOrderingWebsite.isChecked) orderingOptions.add("website")
        if (checkboxOrderingApp.isChecked) orderingOptions.add("app")
        if (checkboxOrderingPhone.isChecked) orderingOptions.add("phone")
        filters["ordering_options"] = orderingOptions.joinToString(",")
        removeEmptyKeys(filters)
        val kebabService = RetrofitClient.retrofit.create(KebabService::class.java)
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        viewLifecycleOwner.lifecycleScope.launch {
            val filteredKebabs = getFilteredKebabs(filters, kebabService)
            if (filteredKebabs != null && filteredKebabs.isNotEmpty()) {
                kebabViewModel.setFilteredKebabPlaces(filteredKebabs)
                val adapter = AdapterClass(filteredKebabs, this@KebabListFragment)
                recyclerView.adapter = adapter
                setTotalKebabs()
            } else {
                Log.i("KebabListFragment", "No data found for the applied filters.")
            }
        }
    }

    private suspend fun getFilteredKebabs(filters: Map<String,String>, kebabService: KebabService): KebabPlaces?
    {
        return try {
            val response = kebabService.getFilteredKebabs(filters)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                Toast.makeText(context, "Kebabs not found :(", Toast.LENGTH_LONG).show();
                null
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
            null
        }
    }

    private fun removeEmptyKeys(map: MutableMap<String, String>) {
        val iterator = map.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.isEmpty()) {
                iterator.remove()
            }
        }
    }

    private fun getData() {
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        if(kebabViewModel.getFilteredKebabPlaces().isEmpty())
        {
            val adapter = AdapterClass(kebabViewModel.getKebabPlaces(), this)
            recyclerView.adapter = adapter
        }
        else {
            val adapter = AdapterClass(kebabViewModel.getFilteredKebabPlaces(), this)
            recyclerView.adapter = adapter
        }

    }

    private fun setupSortSpinner() {
        sortSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
                    var unsortedList: KebabPlaces
                    if(kebabViewModel.getFilteredKebabPlaces().isEmpty())
                    {
                        unsortedList = kebabViewModel.getKebabPlaces()
                    }
                    else
                    {
                        unsortedList = kebabViewModel.getFilteredKebabPlaces()
                    }
                    val sortedList =
                        when (position) {
                            0 -> unsortedList.sortedBy { it.name }
                            1 -> unsortedList.sortedByDescending { it.name }
                            2 -> unsortedList.sortedByDescending { it.year_opened }
                            3 -> unsortedList.sortedBy { it.year_opened }
                            else -> unsortedList
                        }
                    val arraylist = KebabPlaces()
                    sortedList.toCollection(arraylist)
                    recyclerView.adapter = AdapterClass(arraylist, this@KebabListFragment)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
    }

    override fun onLogoClick(itemId: Int) {
        kebabDetailPageViewModel.setKebabId(itemId)
        Log.i("ADAPTER", kebabDetailPageViewModel.getKebabId().toString())
        findNavController().navigate(R.id.action_navigation_list_to_navigation_kebab_detail_page)
    }
}
