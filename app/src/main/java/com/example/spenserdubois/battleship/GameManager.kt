package com.example.spenserdubois.battleship

import android.util.Log
import java.io.Serializable
import java.util.*

/**
 * Created by Spenser DuBois on 11/4/2017.
 */
class GameManager : Serializable {
    var state : String
    private var name : String
    var players : ArrayList<Player>
    var winner : String
    var turn : Int
    var playerTurn : String

    constructor(player1 : Player, player2 : Player)
    {
        playerTurn = "Player 1"
        state = "Started"
        players = ArrayList<Player>()
        players.add(player1)
        players.add(player2)
        winner = ""
        var date = Date()
        Log.e("GameManager", date.toString())
        name = date.toString()
        turn = 0
    }
    fun updateState(s : String)
    {
        state = s
    }

    fun setWin(s : String)
    {
        winner = s
    }

    fun getName() : String
    {
        return name
    }

}