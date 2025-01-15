package com.example.kebabapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.kebabapp.databinding.KebabDetailPageBinding
import com.example.kebabapp.utilities.KebabService
import kotlinx.coroutines.launch


class KebabDetailPage : Fragment() {
    private lateinit var binding: KebabDetailPageBinding
    private lateinit var kebabDetail: KebabDetailPageViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = KebabDetailPageBinding.inflate(layoutInflater)
        val kebabService = RetrofitClient.retrofit.create(KebabService::class.java)
        kebabDetail = ViewModelProvider(requireActivity()).get(KebabDetailPageViewModel::class.java)
        val kebabId = kebabDetail.getKebabId().toString()
        viewLifecycleOwner.lifecycleScope.launch {
            val kebabBasicItem = getBasicKebabInfo(kebabService,kebabId)
            val kebabDetailItem = getDetailKebabInfo(kebabService,kebabId)
            binding.name.text = kebabBasicItem?.name.toString()
            binding.address.text = kebabBasicItem?.address.toString()
            binding.meatTypes.text = kebabDetailItem?.meat_types.toString()
            binding.status.text = kebabDetailItem?.status.toString()
            binding.sauces.text = kebabDetailItem?.sauces.toString()
            binding.craftIndicator.text = kebabDetailItem?.is_craft.toString()
            binding.chainMemberIndicator.text = kebabDetailItem?.is_chain_member.toString()
            binding.stallIndicator.text = kebabDetailItem?.is_in_stall.toString()
            binding.orderingOptions.text = kebabDetailItem?.ordering_options.toString()
            binding.openingHours.text = kebabDetailItem?.opening_hours.toString()
        }
        return binding.root
    }

    private suspend fun getBasicKebabInfo(kebabService: KebabService, kebabId: String): KebabPlaceItem?{
        return try {
            val response = kebabService.getBasicKebabById(kebabId)
            if (response.isSuccessful) {

                response.body()?.data
            } else {
                Log.e("Profile", "Error: ${response.code()} - ${response.message()} ${response.toString()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
            null
        }
    }

    private suspend fun getDetailKebabInfo(kebabService: KebabService, kebabId: String): KebabPlaceDetailItem?{
        return try {
            val response = kebabService.getDetailsKebabById(kebabId)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                Log.e("Profile", "Error: ${response.code()} - ${response.message()} ${response.toString()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Profile", "Failure: ${e.message}")
            null
        }
    }

}