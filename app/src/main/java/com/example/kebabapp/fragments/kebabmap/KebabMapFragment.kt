package com.example.kebabapp.fragments.kebabmap

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kebabapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class KebabMapFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val legnicaCity = LatLng(51.2070, 16.1753)
        googleMap.addMarker(MarkerOptions().position(legnicaCity).title("Marker in Legnica"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(legnicaCity,14f))
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.map_style_basic))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kebab_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

}