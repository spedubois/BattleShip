package com.example.spenserdubois.battleship

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class WinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        val winText = findViewById(R.id.winner) as TextView
        val againBtn = findViewById(R.id.againBtn) as Button

        againBtn.setOnClickListener{
            val intent = Intent(this@WinActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode : Int, resultCode: Int, data: Intent?) {
        val winText = findViewById(R.id.winner) as TextView
        super.onActivityReenter(resultCode, data)
        if(data !is Intent)
            return
        var winner = data.getStringExtra("winner")
        winText.text = "" + winner + "\nWon!!"
    }


}
