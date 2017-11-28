package com.example.spenserdubois.battleship

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_choose.*

class ChooseActivity : AppCompatActivity() {

    var manager : GameManager = GameManager(Player(0), Player(0))
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        val backBtn = backBtn as Button
        val conBtn = contBtn as Button
        val deleteBtn = deleteBtn as Button

        backBtn.setOnClickListener{
            val intent = Intent(this@ChooseActivity, BeginActivity::class.java)
            startActivity(intent)
        }
        conBtn.setOnClickListener{
            val intent = Intent(this@ChooseActivity, MainActivity::class.java)
            intent.putExtra("manager", manager)
            setResult(0, intent)
            startActivity(intent)
        }
        deleteBtn.setOnClickListener{
            for(f in filesDir.listFiles())
            {

                var path = filesDir.toString()+"/"+manager.getName()
                if(f.name.equals(path))
                {
                    f.delete()
                }
                manager.getName()
                if(f.name.equals(manager.getName()))
                    f.delete()
            }
        }
    }
    override fun onActivityResult(requestCode : Int, resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        if(data !is Intent)
            return
        manager = data.getSerializableExtra("manager") as GameManager
    }


}
