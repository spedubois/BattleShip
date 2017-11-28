package com.example.spenserdubois.battleship

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.activity_begin.*
import java.io.FileInputStream
import java.io.ObjectInputStream

class BeginActivity : AppCompatActivity() {

    private lateinit var recyclerViewLayoutManager : LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_begin)

        val start = newBtn as Button

        start.setOnClickListener{
            startNewGame()
        }

        recyclerViewLayoutManager  = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = recyclerViewLayoutManager

        recyclerView.adapter = GameAdapter({
            val recyclerViewDataset: MutableList<GameAdapter.CustomAdapterItem> = mutableListOf()

            var count = 1
            for(f in filesDir.list())
            {
                var fis : FileInputStream = openFileInput(f.toString())
                var ois = ObjectInputStream(fis)
                var manager = ois.readObject() as GameManager
                recyclerViewDataset.add(GameAdapter.BSGame(manager))
                count++
            }
            recyclerViewDataset.toTypedArray()
    }()).apply {
        setOnCustomAdapterItemSelectedListener { customAdapterItem: GameAdapter.CustomAdapterItem->

            when(customAdapterItem){
                is GameAdapter.BSGame -> gotoChooseScreen(customAdapterItem.manager)

                else -> Log.ERROR
            }



        }
    }
    }
    fun gotoChooseScreen(manager: GameManager)
    {
        val intent = Intent(this@BeginActivity, ChooseActivity::class.java)
        intent.putExtra("manager", manager)
        setResult(0, intent)
        startActivity(intent)
    }

    fun startNewGame()
    {
        val intent = Intent(this@BeginActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
