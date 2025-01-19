package com.example.kebabapp.fragments.kebablist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kebabapp.KebabDetailPageViewModel
import com.example.kebabapp.KebabPlaceViewModel
import com.example.kebabapp.KebabPlaces
import com.example.kebabapp.R
import com.example.kebabapp.databinding.FragmentKebabListBinding
import com.example.kebabapp.utilities.KebabService
import kotlinx.coroutines.launch

class KebabListFragment : Fragment(), AdapterClass.OnLogoClickListener {
    private var _binding: FragmentKebabListBinding? = null
    private val binding get() = _binding!!

    private lateinit var kebabDetailPageViewModel: KebabDetailPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentKebabListBinding.inflate(inflater, container, false)
        val view = binding.root

        setupUI()
        setupSortSpinner()
        getData()
        setTotalKebabs()

        return view
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.setHasFixedSize(true)

        kebabDetailPageViewModel = ViewModelProvider(requireActivity()).get(KebabDetailPageViewModel::class.java)
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)

        binding.btnOpenDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }

        binding.btnFilter.setOnClickListener {
            applyFilters()
            setTotalKebabs()
        }

        binding.btnFilterClear.setOnClickListener {
            kebabViewModel.clearFilteredKebabPlaces()
            getData()
            setTotalKebabs()
            resetDrawerSelections()
        }
    }

    private fun setTotalKebabs() {
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        val kebabsTotalAmount = kebabViewModel.getKebabsAmount().toString()
        binding.kebabTotal.text = "Total kebab amount: $kebabsTotalAmount"
    }

    private fun resetDrawerSelections() {
        binding.checkboxCraft.isChecked = false
        binding.checkboxStall.isChecked = false
        binding.checkboxChain.isChecked = false
        binding.checkboxMeatChicken.isChecked = false
        binding.checkboxMeatBeef.isChecked = false
        binding.checkboxMeatFalafel.isChecked = false
        binding.checkboxSauceMild.isChecked = false
        binding.checkboxSauceGarlic.isChecked = false
        binding.checkboxSauceSpicy.isChecked = false
        binding.checkboxSauceKetchup.isChecked = false
        binding.checkboxSauceMixed.isChecked = false
        binding.checkboxOrderingOnSite.isChecked = false
        binding.checkboxOrderingDelivery.isChecked = false
        binding.checkboxOrderingTakeaway.isChecked = false
        binding.checkboxOrderingGLOVO.isChecked = false
        binding.checkboxOrderingPyszne.isChecked = false
        binding.checkboxOrderingUber.isChecked = false
        binding.checkboxOrderingWebsite.isChecked = false
        binding.checkboxOrderingApp.isChecked = false
        binding.checkboxOrderingPhone.isChecked = false
        binding.radioGroupStatus.clearCheck()
    }

    private fun applyFilters() {
        val filters = mutableMapOf<String, String>()

        filters["status"] =
            when {
                binding.radioButton1.isChecked -> "CLOSED temporarily"
                binding.radioButton2.isChecked -> "OPEN"
                else -> ""
            }

        filters["is_craft"] = if (binding.checkboxCraft.isChecked) "true" else ""
        filters["is_in_stall"] = if (binding.checkboxStall.isChecked) "true" else ""
        filters["is_chain_member"] = if (binding.checkboxChain.isChecked) "true" else ""

        val meatTypes = mutableListOf<String>()
        if (binding.checkboxMeatChicken.isChecked) meatTypes.add("chicken")
        if (binding.checkboxMeatBeef.isChecked) meatTypes.add("beef")
        if (binding.checkboxMeatFalafel.isChecked) meatTypes.add("falafel")
        filters["meat_types"] = meatTypes.joinToString(",")

        val sauces = mutableListOf<String>()
        if (binding.checkboxSauceMild.isChecked) sauces.add("mild")
        if (binding.checkboxSauceGarlic.isChecked) sauces.add("garlic")
        if (binding.checkboxSauceSpicy.isChecked) sauces.add("spicy")
        if (binding.checkboxSauceKetchup.isChecked) sauces.add("ketchup")
        if (binding.checkboxSauceMixed.isChecked) sauces.add("mixed")
        filters["sauces"] = sauces.joinToString(",")

        val orderingOptions = mutableListOf<String>()
        if (binding.checkboxOrderingOnSite.isChecked) orderingOptions.add("on-site")
        if (binding.checkboxOrderingDelivery.isChecked) orderingOptions.add("delivery")
        if (binding.checkboxOrderingTakeaway.isChecked) orderingOptions.add("takeaway")
        if (binding.checkboxOrderingGLOVO.isChecked) orderingOptions.add("GLOVO")
        if (binding.checkboxOrderingPyszne.isChecked) orderingOptions.add("Pyszne.pl")
        if (binding.checkboxOrderingUber.isChecked) orderingOptions.add("UberEats")
        if (binding.checkboxOrderingWebsite.isChecked) orderingOptions.add("website")
        if (binding.checkboxOrderingApp.isChecked) orderingOptions.add("app")
        if (binding.checkboxOrderingPhone.isChecked) orderingOptions.add("phone")
        filters["ordering_options"] = orderingOptions.joinToString(",")

        removeEmptyKeys(filters)

        val kebabService = RetrofitClient.retrofit.create(KebabService::class.java)
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launch {
            val filteredKebabs = getFilteredKebabs(filters, kebabService)
            if (filteredKebabs != null && filteredKebabs.isNotEmpty()) {
                kebabViewModel.setFilteredKebabPlaces(filteredKebabs)
                val adapter = AdapterClass(filteredKebabs, this@KebabListFragment)
                binding.recyclerView.adapter = adapter
                setTotalKebabs()
            } else {
                Log.i("KebabListFragment", "No data found for the applied filters.")
            }
        }
    }

    private suspend fun getFilteredKebabs(
        filters: Map<String, String>,
        kebabService: KebabService,
    ): KebabPlaces? {
        return try {
            val response = kebabService.getFilteredKebabs(filters)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                Toast.makeText(context, "Kebabs not found :(", Toast.LENGTH_LONG).show()
                null
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
            null
        }
    }

    private fun removeEmptyKeys(map: MutableMap<String, String>) {
        map.entries.removeIf { it.value.isEmpty() }
    }

    private fun getData() {
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        val adapter =
            if (kebabViewModel.getFilteredKebabPlaces().isEmpty()) {
                AdapterClass(kebabViewModel.getKebabPlaces(), this)
            } else {
                AdapterClass(kebabViewModel.getFilteredKebabPlaces(), this)
            }
        binding.recyclerView.adapter = adapter
    }

    private fun setupSortSpinner() {
        binding.sortSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
                    val unsortedList =
                        if (kebabViewModel.getFilteredKebabPlaces().isEmpty()) {
                            kebabViewModel.getKebabPlaces()
                        } else {
                            kebabViewModel.getFilteredKebabPlaces()
                        }
                    val sortedList =
                        when (position) {
                            0 -> unsortedList.sortedBy { it.name }
                            1 -> unsortedList.sortedByDescending { it.name }
                            2 -> unsortedList.sortedByDescending { it.year_opened }
                            3 -> unsortedList.sortedBy { it.year_opened }
                            else -> unsortedList
                        }
                    val arrayList = KebabPlaces()
                    sortedList.toCollection(arrayList)
                    binding.recyclerView.adapter = AdapterClass(arrayList, this@KebabListFragment)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
