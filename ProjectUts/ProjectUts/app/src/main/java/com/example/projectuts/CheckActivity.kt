package com.example.projectuts

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projectuts.databinding.ActivityCheckBinding

class CheckActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var b: ActivityCheckBinding
    lateinit var db: SQLiteDatabase

    override fun onClick(p0: View?) {
        // Implementasi aksi klik tombol di sini
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCheckBinding.inflate(layoutInflater)
        val view = b.root
        setContentView(view)

        b.btnVideo.setOnClickListener {
            startActivity(Intent(this, MediaHelper::class.java))
    }
    }

}
