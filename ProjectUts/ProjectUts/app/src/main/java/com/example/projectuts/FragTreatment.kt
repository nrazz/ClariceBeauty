package com.example.projectuts

import android.app.AlertDialog
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.projectuts.databinding.ActivityFragTreatmentBinding


class FragTreatment : Fragment(), View.OnClickListener , AdapterView.OnItemClickListener,
    AdapterView.OnItemSelectedListener {
    lateinit var thisParent: MainActivity
    lateinit var v: View
    private lateinit var b: ActivityFragTreatmentBinding
    lateinit var db: SQLiteDatabase
    lateinit var adapter: ListAdapter
    lateinit var builder: AlertDialog.Builder
    lateinit var spAdapter:SimpleCursorAdapter
    var idTreat: Int = 0
    var var2=""
    var var1=""
    var var3=""
    var checkboxFinal = ""
    var customer=""

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
        true
    }

     override fun onItemSelected(parent: AdapterView<*>?, view:View?, position:Int, id:Long) {
        val c = spAdapter.getItem(position) as Cursor
        customer= c.getString(c.getColumnIndexOrThrow("_id")).toString()
    }
    override fun onNothingSelected(parent: AdapterView<*>?){
        b.sp1.setSelection(0,true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        thisParent = activity as MainActivity
        b = ActivityFragTreatmentBinding.inflate(layoutInflater)
        db = thisParent.getDbObject()
        v = b.root

        b.btnUpdate.setOnClickListener(this)
        b.btnAdd.setOnClickListener(this)
        b.btnDelete.setOnClickListener(this)
        builder = AlertDialog.Builder(thisParent)
        b.sp1.onItemSelectedListener = this
        b.lv1.setOnItemClickListener(itemClick)
        return v
    }
    fun checkBox(){
        if(b.cb1.isChecked) var1 = "Buy Product-" else var1 = " "
        if(b.cb2.isChecked) var2 = "Consultation-" else var2 = " "
        if(b.cb3.isChecked) var3 = "Facial-" else var3 = " "

        checkboxFinal = "$var1$var2$var3";
    }
    fun resetCheckBox(){
        if(b.cb1.isChecked){
            b.cb1.setChecked(false);
        }
        if(b.cb2.isChecked){
            b.cb2.setChecked(false);
        }
        if(b.cb3.isChecked){
            b.cb3.setChecked(false);
        }
    }

     fun showDataTreatment(id : String){
        var sql=""

            sql = "select id_treatment as _id,customer,treatment from treatment order by _id asc"
        val c : Cursor = db.rawQuery(sql, null)
        adapter = SimpleCursorAdapter(thisParent,
            R.layout.itemdatatreat, c,
            arrayOf("_id", "customer", "treatment"),
            intArrayOf(R.id.txidTreat, R.id.txcustomer, R.id.txtreat),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        b.lv1.adapter = adapter
    }

    fun showDataReservasi(){
        val c : Cursor = db.rawQuery("select name as _id from reservasi order by _id asc", null)
        spAdapter = SimpleCursorAdapter(thisParent,
            android.R.layout.simple_spinner_item, c, arrayOf("_id"),
            intArrayOf(android.R.id.text1),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        spAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        b.sp1.adapter = spAdapter
        b.sp1.setSelection(0)
    }
    override fun onStart() {
        super.onStart()
        showDataReservasi()
        showDataTreatment("")
    }


    val itemClick = AdapterView.OnItemClickListener { parent, view, position, id ->
        val c : Cursor = parent.adapter.getItem(position) as Cursor
        idTreat = c.getInt(c.getColumnIndexOrThrow("_id"))
        var str = c.getInt(c.getColumnIndexOrThrow("_id")).toString()
        var str2 = c.getString(c.getColumnIndexOrThrow("treatment")).toString()
        var str3 = c.getString(c.getColumnIndexOrThrow("customer")).toString()
        var delimiter = "-"


        var part = str2.split(delimiter).toTypedArray()

        if((part.size.toString() == "2")){
            b.cb1.setChecked(false);
            b.cb2.setChecked(false);
            b.cb3.setChecked(false);

            b.cb1.setChecked(true);
        }else if((part.size.toString() == "3")){
            b.cb1.setChecked(false);
            b.cb2.setChecked(false);
            b.cb3.setChecked(false);

            b.cb1.setChecked(true);
            b.cb2.setChecked(true);
        }else{
            b.cb1.setChecked(false);
            b.cb2.setChecked(false);
            b.cb3.setChecked(false);

            b.cb1.setChecked(true);
            b.cb2.setChecked(true);
            b.cb3.setChecked(true);
        }

    }

    fun insertDataTreat(treatment: String) {
        var sql =
            "insert into treatment(treatment, customer) values (?,?)"
        db.execSQL(sql, arrayOf(treatment, customer))
        showDataTreatment("")
    }
    val btnAddDialog = DialogInterface.OnClickListener{
            dialog, which ->
        checkBox()
        insertDataTreat(checkboxFinal)
        resetCheckBox()
       Toast.makeText(thisParent,"berhasil insert",Toast.LENGTH_SHORT).show()
    }
    fun deleteDataMhs(){
        db.delete("treatment", "id_treatment = $idTreat", null)
        showDataTreatment("")
        showDataReservasi()
        resetCheckBox()
    }

    fun updateDataTreat(treatment: String) {
        var sql =
            "update treatment set treatment=? where id_treatment = $idTreat"
        db.execSQL(sql, arrayOf(treatment))
        showDataTreatment("")
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }
    val btnDeleteDialog = DialogInterface.OnClickListener{
            dialog, which ->
        deleteDataMhs()
    }

    val btnUpdateDialog = DialogInterface.OnClickListener{ dialog, which ->
        var sql ="select id_treatment from treatment where id_treatment= $idTreat"
        checkBox()
        updateDataTreat(checkboxFinal.toString())
        resetCheckBox()
        showDataReservasi()
    }
}
