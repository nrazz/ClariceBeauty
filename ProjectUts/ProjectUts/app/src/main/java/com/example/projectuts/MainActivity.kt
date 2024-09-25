package com.example.projectuts

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentTransaction
import com.example.projectuts.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), View.OnClickListener, NavigationBarView.OnItemSelectedListener{
    private lateinit var b: ActivityMainBinding
    lateinit var db: SQLiteDatabase
    lateinit var fragReser : FragReservation
    lateinit var fragTreat: FragTreatment
    lateinit var ft : FragmentTransaction

    override fun onClick(v: View?) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemDashboard-> b.framelayout.visibility = View.GONE
            R.id.itemReservation -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, fragReser).commit()
                b.framelayout.setBackgroundColor(Color.argb(255,255,255,225))
                b.framelayout.visibility = View.VISIBLE
            }
            R.id.itemTreatment -> {
                ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, fragTreat).commit()
                b.framelayout.setBackgroundColor(Color.argb(255,225,255,225))
                b.framelayout.visibility = View.VISIBLE
            }
        }
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.bottomNavigationView.setOnItemSelectedListener(this)
        fragReser = FragReservation()
        fragTreat = FragTreatment()
        db = DBOpenHelper(this).writableDatabase
    }
    fun getDbObject() : SQLiteDatabase{
        return db
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var mnuInflater = menuInflater
        mnuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.itemExit -> {
                finishAffinity()
                return true
            }
            R.id.itemCheck -> {
                startActivity(Intent(this, CheckActivity::class.java))
                return true
            }
            R.id.itemLokasi -> {
                startActivity(Intent(this, MapActivity::class.java))
                return true
            }
            R.id.itemCamera -> {
                startActivity(Intent(this, ActivityCamera::class.java))
                return true
            }
        }
        return super.onContextItemSelected(item)    }

}