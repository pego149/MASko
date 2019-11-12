package com.example.masko

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {
    object data{
        var predmet = ""
        var pocet = ""
        var castDna = ""
        var meno = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener {
            // Handler code here.
            data.predmet = (findViewById<EditText>(R.id.editText2)).text.toString()
            data.pocet = (findViewById<EditText>(R.id.editText3)).text.toString()
            data.castDna = (findViewById<EditText>(R.id.editText5)).text.toString()
            data.meno = (findViewById<EditText>(R.id.editText6)).text.toString()
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }
    }
}
