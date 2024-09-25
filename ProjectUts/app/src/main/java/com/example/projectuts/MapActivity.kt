package com.example.projectuts

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.projectuts.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mumayank.com.airlocationlibrary.AirLocation

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab -> {
                // Hapus baris ini
                // b.chip.isChecked = false
                liveUpdate = b.chip.isChecked
                gMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(ll, 16.0f)
                )
                b.editText.setText(
                    "Posisi saya : LAT=${ll.latitude}, LNG=${ll.longitude}"
                )
            }
            R.id.chip -> {
                liveUpdate = b.chip.isChecked
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        val markerKota = mapOf(

            "Clarice Beauty" to LatLng(-7.84937,112.03005)
        )

        markerKota.forEach { (kota, koordinat) ->
            gMap.addMarker(
                MarkerOptions()
                    .position(koordinat)
                    .title("Kota "+kota)
            )
        }

        airLoc = AirLocation(this, object : AirLocation.Callback {
            override fun onFailure(locationFailedEnum: AirLocation.LocationFailedEnum) {
                Toast.makeText(
                    this@MapActivity, "Gagal Mendapatkan posisis saat ini",
                    Toast.LENGTH_SHORT
                ).show()
                b.editText.setText(
                    "Gagal Mendapatkan posisi saat ini"
                )
            }

            override fun onSuccess(locations: ArrayList<Location>) {
                if (liveUpdate) {
                    // untuk menghapus old marker biar gambar marker-nya tidak selalu nambah
                    if (marker != null) marker!!.remove()
                    val location = locations.get(locations.size - 1)
                    ll = LatLng(location.latitude, location.longitude)
                    marker = gMap.addMarker(
                        MarkerOptions().position(ll).title("Posisi saya")
                    )
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(ll))
                    b.editText.setText(
                        "Posisi saya : LAT=${location.latitude}, " +
                                "LNG=${location.longitude}"
                    )
                }
            }
        })
        airLoc.start()
    }

    lateinit var airLoc : AirLocation
    lateinit var gMap : GoogleMap
    private lateinit var mapFragment : SupportMapFragment
    lateinit var b: ActivityMapBinding
    lateinit var ll : LatLng
    var marker : Marker? = null
    var liveUpdate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMapBinding.inflate(layoutInflater)
        setContentView(b.root)
        mapFragment =
            supportFragmentManager.findFragmentById(
                R.id.fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        b.fab.setOnClickListener(this)
        b.chip.setOnClickListener(this)
        b.chip.isChecked = true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int,data: Intent?) {
        airLoc?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        airLoc?.onRequestPermissionsResult(requestCode,
            permissions, grantResults)
        super.onRequestPermissionsResult(requestCode,
            permissions, grantResults)
    }

}