package com.example.spenserdubois.battleship

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(){

    private lateinit var btnReg : Button
    private lateinit var textEmail : EditText
    private lateinit var textPassword : EditText
    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnReg = registerBtn
        textEmail = editEmail
        textPassword = editPassword
        textView = textSignIn
    }
}
