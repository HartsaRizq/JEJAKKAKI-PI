package com.folu.jejakkaki.ui

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.folu.jejakkaki.R
import com.folu.jejakkaki.model.Place
import com.folu.jejakkaki.model.PlaceData
import com.folu.jejakkaki.model.TamanData
import com.folu.jejakkaki.ui.detail.DetailActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {

    private lateinit var googleMap: GoogleMap
    private lateinit var fab: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        hideSystemUI()
        fab = findViewById(R.id.fabLanguage)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val selectedLanguage = sharedPreferences.getString("selectedLanguage", "en") ?: "en"

        // Set the FAB icon based on the selected language
        setFabIconBasedOnLanguage(selectedLanguage)

        fab.setOnClickListener {
            var dialog = LanguageFragment()
            dialog.show(supportFragmentManager, "changeLanguage")
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val indonesiaBounds = LatLngBounds.Builder()
            .include(LatLng(-10.817083, 93.750963)) // Southwest corner (bottom-left)
            .include(LatLng(7.814442, 141.563462)) // Northeast corner (top-right)
            .build()

        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.setLatLngBoundsForCameraTarget(indonesiaBounds)
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        if (screenWidth > 1920) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaBounds.center, 6.25f))
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaBounds.center, 4.25f))
        }

        for (place in PlaceData.list) {
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(place.coordinate)
                    .title(place.name)
                    .snippet("Klik lebih lanjut")
            )
            marker?.tag = place
        }
        setMapStyle()
        googleMap.setOnInfoWindowClickListener(this)

        // Set the custom info window adapter
        googleMap.setInfoWindowAdapter(this)

        val provinceCoordinates = listOf(
            LatLng(-8.980783, 109.704221),
            LatLng(-4.705845, 99.706663),
            LatLng(-4.333472, 112.318966),
            LatLng(-5.952847, 121.349727),
            LatLng(-9.392907, 115.043575)

        )
        val provinceNames = listOf(
            "JAWA",
            "SUMATRA",
            "KALIMANTAN",
            "SULAWESI",
            "BALI",
        )

        for (i in provinceCoordinates.indices) {
            val markerView = layoutInflater.inflate(R.layout.custom_marker_layout, null)
            val markerText = markerView.findViewById<TextView>(R.id.marker_text)
            markerText.text = provinceNames[i]

            val markerOptions = MarkerOptions()
                .position(provinceCoordinates[i])
                .title(provinceNames[i]) // Label nama provinsi
                .icon(getMarkerIconFromView(markerView)) // Gunakan custom view sebagai marker
            googleMap.addMarker(markerOptions)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun restartActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getMarkerIconFromView(view: View): BitmapDescriptor {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun createDrawableFromView(view: View): Bitmap {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    override fun onInfoWindowClick(marker: Marker) {
        // Retrieve the Place object associated with the clicked marker
        val place = marker.tag as? Place

        // Check if place is not null before passing its id to the DetailActivity
        place?.let {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("id", it.id)
            startActivity(intent)
        }
    }

    override fun getInfoContents(marker: Marker): View? {
        val view = layoutInflater.inflate(R.layout.custom_info_window, null)

        val infoWindowImage = view.findViewById<ImageView>(R.id.info_window_image)
        val infoWindowTitle = view.findViewById<TextView>(R.id.info_window_title)

        // Retrieve the Place object associated with the clicked marker
        val place = marker.tag as? Place

        // Check if place is not null before setting the info window contents
        place?.let {
            // Find the corresponding Taman object based on the Place's id
            val taman = TamanData.taman.find { t -> t.id == it.id }

            // Customize the info window contents
            if (taman != null) {
//                infoWindowImage.setImageResource(taman.bgPic)
                Picasso.get()
                    .load(taman.bgPic)
                    .resize(80, 45)
                    .placeholder(R.drawable.gunung_leuser)
                    .error(R.drawable.gunung_leuser)
                    .into(infoWindowImage, object : Callback {
                        override fun onSuccess() {
                            if (marker.isInfoWindowShown) {
                                marker.hideInfoWindow()
                                marker.showInfoWindow()
                            }
                        }

                        override fun onError(e: Exception?) {
                            e?.printStackTrace()
                            Log.e("Bruh", "Error loading thumbnail!");
                        }
                    })
                infoWindowTitle.text = it.name
            }
        }

        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    private fun setFabIconBasedOnLanguage(language: String) {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        when (language) {
            "in" -> if (screenWidth > 1920) {
                fab.setImageResource(R.drawable.flag_id_tab)
            } else {
                fab.setImageResource(R.drawable.flag_id)
            }
            "en" -> if (screenWidth > 1920) {
                fab.setImageResource(R.drawable.flag_usa_tab)
            } else {
                fab.setImageResource(R.drawable.flag_usa)
            }
            // Add more cases for other languages if needed
            else -> fab.setImageResource(R.drawable.flag_usa_tab) // Default to English
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun setMapStyle() {
        try {
            val success =
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}
