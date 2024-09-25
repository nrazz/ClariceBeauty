package com.example.projectuts


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.projectuts.databinding.ActivityFragReservationBinding
import java.text.SimpleDateFormat
import java.util.*

class FragReservation : Fragment(), View.OnClickListener {
    lateinit var thisParent: MainActivity
    lateinit var v: View
    private lateinit var b: ActivityFragReservationBinding
    lateinit var db: SQLiteDatabase
    lateinit var adapter: ListAdapter
    lateinit var builder: AlertDialog.Builder
    var idReser: String = ""
    var var1=""

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                builder.setTitle("Konfirmasi").setMessage("Data yang akan dimasukkan sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya", btnAddDialog)
                    .setNegativeButton("Tidak", null)
                builder.show()
            }
            R.id.btnDelete -> {
                builder.setTitle("Konfirmasi").setMessage("Yakin akan menghapus data ini?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya", btnDeleteDialog)
                    .setNegativeButton("Tidak", null)
                builder.show()
            }
            R.id.btnUpdate -> {
                builder.setTitle("Konfirmasi").setMessage("Data yang akan dimasukkan sudah benar?")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("Ya", btnUpdateDialog)
                    .setNegativeButton("Tidak", null)
                builder.show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as MainActivity
        b = ActivityFragReservationBinding.inflate(layoutInflater)
        db = thisParent.getDbObject()
        v = b.root

        b.rg1.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId){
                R.id.rbMale -> var1 = "Male"
                R.id.rbFemale-> var1 = "Female"
            }
        }

        b.btnDPDialog1.setOnClickListener{
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager
            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                    resultKey, bundle -> if (resultKey == "REQUEST_KEY"){
                val date = bundle.getString("SELECTED_DATE")
                b.txtDT.text = date
            }
            }
            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }
        b.btnUpdate.setOnClickListener(this)
        b.btnAdd.setOnClickListener(this)
        b.btnDelete.setOnClickListener(this)
        builder = AlertDialog.Builder(thisParent)
        b.lsResevation.setOnItemClickListener(itemClick)

        return v
    }

    fun showDataReservasi() {
        val cursor: Cursor = db.query(
            "reservasi",
            arrayOf("name", "id_reservasi as _id", "gender", "dt", "address", "phone"),
            null,
            null,
            null,
            null,
            "name asc"
        )
        adapter = SimpleCursorAdapter(
            thisParent, R.layout.itemdatareser, cursor,
            arrayOf("_id", "name", "gender", "dt", "address", "phone"),
            intArrayOf(
                R.id.txidreser,
                R.id.txName,
                R.id.txGender,
                R.id.txDate,
                R.id.txAddress,
                R.id.txPhone
            ),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
        b.lsResevation.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        showDataReservasi()
    }


    val itemClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val c: Cursor = parent.adapter.getItem(position) as Cursor
        idReser = c.getString(c.getColumnIndexOrThrow("_id"))
        b.edNama.setText(c.getString(c.getColumnIndexOrThrow("name")))
        b.txtDT.setText(c.getString(c.getColumnIndexOrThrow("dt")))
        b.edAddress.setText(c.getString(c.getColumnIndexOrThrow("address")))
        b.edPhone.setText(c.getString(c.getColumnIndexOrThrow("phone")))
    }

    fun insertDataReser(name: String, gender:String, date:String, address:String, phone:String) {
        var sql =
            "insert into reservasi(name, gender, dt, address, phone) values (?,?,?,?,?)"
        db.execSQL(sql, arrayOf(name, gender, date, address, phone))
        showDataReservasi()
    }
    fun updateDataReser(name: String, gender:String, date:String, address:String, phone:String) {
        var sql =
            "update reservasi set name=?, gender=?, dt=?, address=?, phone=? where id_reservasi = $idReser"
        db.execSQL(sql, arrayOf(name, gender, date, address, phone))
        showDataReservasi()
    }

    fun deleteDataReser(idReser: String){
        db.delete("reservasi", "id_reservasi = $idReser", null)
        showDataReservasi()
    }
    val btnAddDialog = DialogInterface.OnClickListener{
            dialog, which ->
        insertDataReser(b.edNama.text.toString(), var1, b.txtDT.text.toString(),b.edAddress.text.toString(),b.edPhone.text.toString())
        b.edNama.setText("")
        b.txtDT.setText("")
        b.edAddress.setText("")
        b.edPhone.setText("")
        showDataReservasi()
    }

    val btnUpdateDialog = DialogInterface.OnClickListener{ dialog, which ->
        var sql ="select id_reservasi from reservasi where id_reservasi = $idReser"
        updateDataReser(b.edNama.text.toString(), var1, b.txtDT.text.toString(),b.edAddress.text.toString(),b.edPhone.text.toString())
        b.edNama.setText("")
        b.txtDT.setText("")
        b.edAddress.setText("")
        b.edPhone.setText("")
        showDataReservasi()
    }

    val btnDeleteDialog = DialogInterface.OnClickListener{ dialog, which ->
        var sql ="select id_reservasi from reservasi where id_reservasi = $idReser"
        deleteDataReser(idReser)
        b.edNama.setText("")
        b.txtDT.setText("")
        b.edAddress.setText("")
        b.edPhone.setText("")
        showDataReservasi()
    }

}

