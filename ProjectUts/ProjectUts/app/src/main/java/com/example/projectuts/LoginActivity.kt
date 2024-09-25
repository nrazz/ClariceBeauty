package com.example.projectuts

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.projectuts.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var b: ActivityLoginBinding
    lateinit var db: SQLiteDatabase
    private lateinit var usr: EditText
    private lateinit var pw: EditText

    override fun onClick(p0: View?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        val view = b.root
//        var DBOpenHelper = DBOpenHelper(applicationContext)
//        var db = DBOpenHelper.writableDatabase
        setContentView(view)
        usr = findViewById(R.id.edUser)
        pw = findViewById(R.id.edPw)

        b.btnLogin.setOnClickListener {
            val usr = usr.text.toString()
            val pw = pw.text.toString()
// bikin akunnya langsung di register
            db = DBOpenHelper(this).writableDatabase
//
//            val cv = ContentValues()
//            cv.put("username", "ema")
//            cv.put("password", "111")
//            db.insert("user", null, cv)

            val sql =
                "SELECT username, password FROM user WHERE username = '$usr' AND password = '$pw'"
            val c: Cursor = db.rawQuery(sql, null)
            if (c.count > 0) {
                c.moveToFirst()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                b.edPw.setText("")
                b.edPw.setText("")
            } else {
                Toast.makeText(
                    this,
                    "Maaf password atau username anda salah !!",

                    Toast.LENGTH_SHORT
                ).show()

            }

        }
        b.tvRegis.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }
    }
}