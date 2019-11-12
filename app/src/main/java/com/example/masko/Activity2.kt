package com.example.masko

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
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_2.*


class Activity2 : AppCompatActivity() {
    class Cislo {
        var fronta = ArrayList<QueueItem>()
        var frontaForOutput = ArrayList<QueueItem>()
        var obsluzene = ArrayList<ObsluzeneItem>()
    }
    var poleCisiel = ArrayList<Cislo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        findViewById<TextView>(R.id.textView).text = MainActivity.data.predmet
        val myRadioGroup = findViewById<RadioGroup>(R.id.myRadioGroup)
        myRadioGroup.check(findViewById<RadioButton>(R.id.radioButton2).id)
        for (i in 1..5) {
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
            startObsluhy.isEnabled = true
            endObsluhy.isEnabled = false
            println(poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].id.toString() + poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].casZaciatku + poleCisiel[index].obsluzene[poleCisiel[index].obsluzene.size - 1].casKonca)
        }
        myRadioGroup.setOnCheckedChangeListener { _, _ ->
            val index = myRadioGroup.indexOfChild(findViewById(myRadioGroup.checkedRadioButtonId)) - 1
            queueLengthText.text = "Dlzka fronty: " + poleCisiel[index].fronta.size.toString()
        }
    }
}
