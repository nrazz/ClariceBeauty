package com.example.projectuts
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.projectuts.databinding.ActivityRegisterBinding
class RegisterActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)
        var DBOpenHelper = DBOpenHelper(applicationContext)
        var db = DBOpenHelper.writableDatabase

        b.btnRegis.setOnClickListener {
            var usr = b.ediUser.text.toString()
            var psw = b.ediPw.text.toString()
            if (usr.isNotEmpty() && psw.isNotEmpty()) {
                var data = ContentValues()
                data.put("username", usr)
                data.put("password", psw)
                var rs: Long = db.insert("user", null, data)
                if (!rs.equals(-1)) {
                    var ad = AlertDialog.Builder(this)
                    ad.setTitle("Message")
                    ad.setMessage("Account registered successfully")
                    ad.setPositiveButton("Done", null)
                    ad.show()
                    b.ediUser.text.clear()
                    b.ediPw.text.clear()
                } else {
                    var ad = AlertDialog.Builder(this)
                    ad.setTitle("Message")
                    ad.setMessage("Record not addedd")
                    ad.setPositiveButton("Done", null)
                    ad.show()
                    b.ediUser.text.clear()
                    b.ediPw.text.clear()
                }
            } else {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            }
            b.tvLogin.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}