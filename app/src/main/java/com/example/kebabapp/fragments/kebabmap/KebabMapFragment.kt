package com.example.kebabapp.fragments.kebabmap

import android.graphics.Bitmap
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.example.kebabapp.R
import android.graphics.Canvas
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.kebabapp.KebabPlaceViewModel
import com.example.kebabapp.KebabPlaces

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class KebabMapFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val legnicaCity = LatLng(51.2070, 16.1753)
        val ExampleKebabLatLng = LatLng( 51.204137,16.160657)
        val kebabViewModel = ViewModelProvider(requireActivity()).get(KebabPlaceViewModel::class.java)
        Log.i("XDDDDDDDD",kebabViewModel.getKebabPlaces().toString())
        /*val markerOptionKebab = MarkerOptions()
        markerOptionKebab.position(ExampleKebabLatLng)
        markerOptionKebab.title("Piri-Piri")
        markerOptionKebab.snippet("Craft Kebab!")
        markerOptionKebab.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.ic_kebab_marker)))*/
        var listOfMarkers = createListOfMarkers(kebabViewModel.getKebabPlaces())
        for(e in listOfMarkers)
        {
            googleMap.addMarker(e)
        }
       // googleMap.addMarker(markerOptionKebab)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(legnicaCity,14f))
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.map_style_basic))
    }

    private fun getBitmapFromDrawable(resId: Int): Bitmap? {
        var bitmap : Bitmap? = null
        val drawable = ResourcesCompat.getDrawable(resources,resId,null)
        if(drawable != null){
            bitmap = Bitmap.createBitmap(150,150,Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0,0,canvas.width,canvas.height)
            drawable.draw(canvas)

        }
        return bitmap
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
    private fun createListOfMarkers(kebabPlaces: KebabPlaces): MutableList<MarkerOptions> {
        var listOfMarkers: MutableList<MarkerOptions> = mutableListOf()
        for(item in kebabPlaces)
        {
            var markerOptions = MarkerOptions()
            markerOptions.title(item.kebabName)
            markerOptions.snippet(item.address)
            markerOptions.position(LatLng(item.lat,item.long))
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(R.drawable.ic_kebab_marker)))
            listOfMarkers.add(markerOptions)
        }
        return listOfMarkers
    }

}