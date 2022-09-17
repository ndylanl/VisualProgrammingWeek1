package com.example.vpw1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vpw1.Adapter.ListDataRVAdapter
import com.example.vpw1.Database.GlobalVar
import com.example.vpw1.Interface.CardListener
import com.example.vpw1.databinding.ActivityRecyclerViewBinding


class RecyclerViewActivity : AppCompatActivity(), CardListener {

    private lateinit var viewBind: ActivityRecyclerViewBinding
    private val adapter = ListDataRVAdapter(GlobalVar.listDataHewan, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        viewBind = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(viewBind.root)

        CheckPermissions()
        setupRecyclerView()
        listener()
    }

    private fun listener() {
        viewBind.addHewanFAB.setOnClickListener{
            val myIntent = Intent(this, FormActivity::class.java)
            startActivity(myIntent)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(baseContext)
        viewBind.rvHewan.layoutManager = layoutManager   // Set layout
        viewBind.rvHewan.adapter = adapter   // Set adapter
    }

    private fun CheckPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), GlobalVar.STORAGE_PERMISSION_CODE)
        } else {
            Toast.makeText(this, "Storage Permission already granted", Toast.LENGTH_SHORT).show()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GlobalVar.STORAGE_PERMISSION_CODE)
        } else {
            Toast.makeText(this, "Storage Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GlobalVar.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onEdit(position: Int) {
        val myIntent = Intent(this, FormActivity::class.java).apply {
            putExtra("position", position)
        }
        startActivity(myIntent)
    }

    override fun onDelete(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to Delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                GlobalVar.listDataHewan.removeAt(position)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Deletion Successful", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }


}