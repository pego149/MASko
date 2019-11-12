package com.example.masko

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_2.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
private val CSV_HEADER = "idfronta,dlzkafronty,casfronty"
private val CSV_HEADER_SERV = "idserv,casprichodu,casodchodu"

class Activity2 : AppCompatActivity() {
    class Cislo {
        var fronta = ArrayList<QueueItem>()
        var frontaForOutput = ArrayList<QueueItem>()
        var obsluzene = ArrayList<ObsluzeneItem>()
        var prebiehaObsluha = false
    }
    var poleCisiel = ArrayList<Cislo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        findViewById<TextView>(R.id.textView).text = MainActivity.data.predmet
        val myRadioGroup = findViewById<RadioGroup>(R.id.myRadioGroup)
        myRadioGroup.check(findViewById<RadioButton>(R.id.radioButton2).id)
        for (i in 1..6) {
            poleCisiel.add(Cislo())
        }
        val queueLengthText = findViewById<TextView>(R.id.queueLength)
        val index = myRadioGroup.indexOfChild(findViewById(myRadioGroup.checkedRadioButtonId)) - 1
        queueLengthText.text = "Dlzka fronty: " + poleCisiel[index].fronta.size.toString()
        val addToQueue = findViewById<Button>(R.id.addToQueue)
        addToQueue.setOnClickListener {
            val index = myRadioGroup.indexOfChild(findViewById(myRadioGroup.checkedRadioButtonId)) - 1
            print(index)
            val item = QueueItem(poleCisiel[index].frontaForOutput.size, poleCisiel[index].fronta.size + 1, Calendar.getInstance().time.toString())
            poleCisiel[index].fronta.add(item)
            poleCisiel[index].frontaForOutput.add(item)
            queueLengthText.text = "Dlzka fronty: " + poleCisiel[index].fronta.size.toString()
            println(poleCisiel[index].frontaForOutput[poleCisiel[index].frontaForOutput.size - 1].id.toString() + poleCisiel[index].frontaForOutput[poleCisiel[index].frontaForOutput.size - 1].length.toString() + poleCisiel[index].frontaForOutput[poleCisiel[index].frontaForOutput.size - 1].time)
        }
        val removeFromQueue = findViewById<Button>(R.id.removeFromQueue)
        removeFromQueue.setOnClickListener {
            val index = myRadioGroup.indexOfChild(findViewById(myRadioGroup.checkedRadioButtonId)) - 1
            if (poleCisiel[index].fronta.size > 0) {
                val item = QueueItem(poleCisiel[index].frontaForOutput.size, poleCisiel[index].fronta.size - 1, Calendar.getInstance().time.toString())
                poleCisiel[index].fronta.removeAt(0)
                poleCisiel[index].frontaForOutput.add(item)
                queueLengthText.text = "Dlzka fronty: " + poleCisiel[index].fronta.size.toString()
                println(poleCisiel[index].frontaForOutput[poleCisiel[index].frontaForOutput.size - 1].id.toString() + poleCisiel[index].frontaForOutput[poleCisiel[index].frontaForOutput.size - 1].length.toString() + poleCisiel[index].frontaForOutput[poleCisiel[index].frontaForOutput.size - 1].time)
            }
        }
        val startObsluhy = findViewById<Button>(R.id.startObsluhy)
        val endObsluhy = findViewById<Button>(R.id.endObsluhy)
        startObsluhy.setOnClickListener {
            val index = myRadioGroup.indexOfChild(findViewById(myRadioGroup.checkedRadioButtonId)) - 1
            if (poleCisiel[index].fronta.size > 0) {
                poleCisiel[index].obsluzene.add(ObsluzeneItem(poleCisiel[index].obsluzene.size, Calendar.getInstance().time.toString()))
                poleCisiel[index].prebiehaObsluha = true
                startObsluhy.isEnabled = false
                endObsluhy.isEnabled = true
                poleCisiel[index].fronta.removeAt(0)
                queueLengthText.text = "Dlzka fronty: " + poleCisiel[index].fronta.size.toString()
                println(poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].id.toString() + poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].casZaciatku + poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].casKonca)
            }
        }
        endObsluhy.setOnClickListener {
            val index = myRadioGroup.indexOfChild(findViewById(myRadioGroup.checkedRadioButtonId)) - 1
            poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].casKonca = Calendar.getInstance().time.toString()
            poleCisiel[index].prebiehaObsluha = false
            startObsluhy.isEnabled = true
            endObsluhy.isEnabled = false
            println(poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].id.toString() + poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].casZaciatku + poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].casKonca)
        }
        myRadioGroup.setOnCheckedChangeListener { _, _ ->
            val index = myRadioGroup.indexOfChild(findViewById(myRadioGroup.checkedRadioButtonId)) - 1
            queueLengthText.text = "Dlzka fronty: " + poleCisiel[index].fronta.size.toString()
            startObsluhy.isEnabled = !poleCisiel[index].prebiehaObsluha
            endObsluhy.isEnabled = poleCisiel[index].prebiehaObsluha
        }
        val export = findViewById<Button>(R.id.export)
        export.setOnClickListener {
            var fileWriter: FileWriter? = null
            try {
                fileWriter = FileWriter(File(this.getExternalFilesDir(null), "fronta.csv"))
                fileWriter.append(CSV_HEADER)
                fileWriter.append('\n')

                for (cislo in poleCisiel) {
                    for (pole in cislo.frontaForOutput) {
                        fileWriter.append(pole.id.toString())
                        fileWriter.append(',')
                        fileWriter.append(pole.length.toString())
                        fileWriter.append(',')
                        fileWriter.append(pole.time)
                        fileWriter.append('\n')
                    }
                    fileWriter.append('\n')
                }

                println("Write CSV successfully!")
            } catch (e: Exception) {
                println("Writing CSV error!")
                e.printStackTrace()
            } finally {
                try {
                    fileWriter!!.flush()
                    fileWriter.close()
                } catch (e: IOException) {
                    println("Flushing/closing error!")
                    e.printStackTrace()
                }
            }
            try {
                fileWriter = FileWriter(File(this.getExternalFilesDir(null), "serv.csv"))
                fileWriter.append(CSV_HEADER_SERV)
                fileWriter.append('\n')

                for (cislo in poleCisiel) {
                    for (pole in cislo.obsluzene) {
                        fileWriter.append(pole.id.toString())
                        fileWriter.append(',')
                        fileWriter.append(pole.casZaciatku)
                        fileWriter.append(',')
                        fileWriter.append(pole.casKonca)
                        fileWriter.append('\n')
                    }
                    fileWriter.append('\n')
                }

                println("Write CSV successfully!")
            } catch (e: Exception) {
                println("Writing CSV error!")
                e.printStackTrace()
            } finally {
                try {
                    fileWriter!!.flush()
                    fileWriter.close()
                } catch (e: IOException) {
                    println("Flushing/closing error!")
                    e.printStackTrace()
                }
            }
        }
    }

    fun getPrivateAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the directory for the app's private pictures directory.
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_DOWNLOADS), albumName)
        if (!file?.mkdirs()) {
            Log.e("123", "Directory not created")
        }
        return file
    }
}
