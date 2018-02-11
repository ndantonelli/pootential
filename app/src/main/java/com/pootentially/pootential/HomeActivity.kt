package com.pootentially.pootential

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.pootentially.pootential.viewModels.RestroomViewModel
import mu.KotlinLogging
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.pootentially.pootential.utility.Models.ChangedRestroom
import com.pootentially.pootential.utility.Models.Enumerations
import android.widget.Toast




class HomeActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var restroomViewModel: RestroomViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, intent.getStringExtra("mapboxKey"))
        setContentView(R.layout.activity_home)
        mapView = findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        restroomViewModel = RestroomViewModel.create(this)
        Application.appComponent.inject(restroomViewModel!!)
        mapView?.getMapAsync(this)
    }

    override fun onMapReady(map: MapboxMap){
        mapboxMap = map
        mapboxMap?.uiSettings?.isTiltGesturesEnabled = false
        mapboxMap?.uiSettings?.isRotateGesturesEnabled = false
        restroomViewModel?.getChanges()?.observe(this, Observer<List<ChangedRestroom>> { changeList ->
            changeList?.forEach {
                when(it.status){
                    Enumerations.ChangedStatus.ADDED -> {
                        if(it.restroom?.getMarkerOptions() != null){
                            it.restroom?.marker = mapboxMap?.addMarker(it.restroom?.getMarkerOptions()!!)
                        }
                    }
                    Enumerations.ChangedStatus.REMOVED -> {
                        if(it.restroom?.marker != null){
                            mapboxMap?.removeMarker(it.restroom?.marker!!)
                        }
                    }
                }
            }
        })
        restroomViewModel?.updateVisibleRegion(mapboxMap?.projection?.visibleRegion!!)
        mapboxMap?.addOnCameraIdleListener {
            restroomViewModel?.updateVisibleRegion(mapboxMap?.projection?.visibleRegion!!)
        }
    }

    public override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState!!)
    }
}
