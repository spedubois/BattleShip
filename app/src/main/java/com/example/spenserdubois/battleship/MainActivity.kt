package com.example.spenserdubois.battleship

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.io.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var player1 = Player(0)
    var player2 = Player(0)
    var turn = 1
    var manager : GameManager = GameManager(player1, player2)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val gameView = findViewById(R.id.boardView) as com.example.spenserdubois.battleship.boardView
        val miniView = findViewById(R.id.miniView) as com.example.spenserdubois.battleship.MiniView
        val hitMiss = findViewById(R.id.HitMiss) as TextView
        val passBtn = findViewById(R.id.Pass) as Button
        val readyBtn = findViewById(R.id.ready) as Button
        val readyPhrase = findViewById(R.id.readyPhrase) as TextView

        gameView.layoutParams.height = gameView.layoutParams.width

        gameView.genFrame(gameView.layoutParams.width, gameView.layoutParams.height)
        miniView.genFrame(miniView.layoutParams.width, gameView.layoutParams.width)
        player1 = Player(miniView.layoutParams.width/10)
        player2 = Player(miniView.layoutParams.width/10)
        player1.name = "Player 1"
        player2.name = "Player 2"
        manager = GameManager(player1, player2)
        miniView.drawBoats(player2.boats)
        miniView.invalidate()
        save(manager);



        gameView.setOnNewShotListener { _, x, y ->
            var tempPlayer : Player
            var otherPlayer : Player
            if(++manager.turn > 0 || manager.turn > 17)
                manager.updateState("In Progress")
            if(turn%2 == 0) {
                manager.playerTurn = "Player 1"
                tempPlayer = player2
                otherPlayer = player1
            }
            else {
                manager.playerTurn="Player 2"
                tempPlayer = player1
                otherPlayer = player2
            }
            var shot = tempPlayer.ships[y][x]
            if( shot > 0)
            {
                if(tempPlayer.hitBoat(shot) == 0)
                {
                    otherPlayer.boatsLeft--
                    if(++tempPlayer.hits == 17)
                    {
                        miniView.visibility = View.INVISIBLE
                        gameView.visibility = View.INVISIBLE
                        hitMiss.visibility = View.INVISIBLE
                        passBtn.visibility = View.INVISIBLE
                        manager.setWin(tempPlayer.name)
                        manager.updateState("Completed")
                        gotoWinScreen()
                    }
                    hitMiss.setTextColor(Color.GREEN)
                    hitMiss.text = "SUNK!"
                    miniView.boatPath = tempPlayer.boatsPath
                    miniView.setHitPath(tempPlayer.miniHitPath)
                    gameView.addHit(x + 0f, y + 0f)
                    miniView.addHit(x + 0f, y + 0f)
                    tempPlayer.miniHitPath = miniView.getHitPath()
                    tempPlayer.hitPath = gameView.getHitPath()
                    tempPlayer.boatsPath = miniView.boatPath
                    gameView.canClick = false
                    passBtn.visibility = View.VISIBLE
                    gameView.addSunkPath(tempPlayer.getBoat(shot).coords)
                    tempPlayer.sunkPath = gameView.getSunkPath()
                }
                else
                {
                    tempPlayer.hits++
                    hitMiss.setTextColor(Color.RED)
                    hitMiss.text = "HIT!"
                    miniView.boatPath = tempPlayer.boatsPath
                    miniView.setHitPath(tempPlayer.miniHitPath)
                    gameView.addHit(x + 0f, y + 0f)
                    miniView.addHit(x + 0f, y + 0f)
                    tempPlayer.boatsPath = miniView.boatPath
                    tempPlayer.miniHitPath = miniView.getHitPath()
                    tempPlayer.hitPath = gameView.getHitPath()
                    gameView.canClick = false
                    passBtn.visibility = View.VISIBLE
                }
            }
            else
            {
                hitMiss.setTextColor(Color.DKGRAY)
                hitMiss.text = "MISS!"
                miniView.boatPath = tempPlayer.boatsPath
                miniView.setMissPath(tempPlayer.miniMissPath)
                gameView.addMiss(x+0f,y+0f)
                miniView.addMiss(x+0f,y+0f)
                tempPlayer.boatsPath = miniView.boatPath
                tempPlayer.miniMissPath = miniView.getMissPath()
                tempPlayer.missPath = gameView.getMissPath()
                gameView.canClick = false
                passBtn.visibility = View.VISIBLE
            }
            save(manager)
        }

        passBtn.setOnClickListener{
            hitMiss.text = ""
            gameView.visibility = View.INVISIBLE
            miniView.visibility = View.INVISIBLE
            passBtn.visibility = View.INVISIBLE
            if(turn % 2 == 0)
                readyPhrase.text = "\n\nIs Player 1\nReady?"
            else
                readyPhrase.text = "\n\nIs Player 2\nReady?"
            readyBtn.visibility = View.VISIBLE
            readyPhrase.visibility = View.VISIBLE
        }
        readyBtn.setOnClickListener{
            gameView.visibility = View.VISIBLE
            miniView.visibility = View.VISIBLE
            readyBtn.visibility = View.INVISIBLE
            readyPhrase.visibility = View.INVISIBLE
            hitMiss.text = ""
            gameView.canClick = true
            passBtn.visibility = View.INVISIBLE
            turn++
            var tempPlayer : Player
            var otherPlayer : Player
            if(turn%2 == 0) {
                tempPlayer = player2
                otherPlayer = player1
            }
            else {
                tempPlayer = player1
                otherPlayer = player2
            }
            miniView.setHitPath(otherPlayer.miniHitPath)
            miniView.setMissPath(otherPlayer.miniMissPath)
            miniView.drawBoats(otherPlayer.boats)
            miniView.invalidate()
            gameView.setSunkPath(tempPlayer.sunkPath)
            gameView.setHitPath(tempPlayer.hitPath)
            gameView.setMissPath(tempPlayer.missPath)
            gameView.invalidate()
        }
    }
    fun save(manager: GameManager)
    {
        var fileName = manager.getName()
        val file = File(filesDir, fileName)
        val fos : FileOutputStream = openFileOutput(fileName, Context.CONTEXT_IGNORE_SECURITY)
        val os = ObjectOutputStream(fos)
        os.writeObject(manager)
        os.flush()
        os.close()
    }

    fun gotoWinScreen()
    {
        val intent = Intent(this@MainActivity, WinActivity::class.java)
        intent.putExtra("winner", manager.winner)
        setResult(0, intent)
        startActivity(intent)
    }
    override fun onActivityResult(requestCode : Int, resultCode: Int, data: Intent?) {
            val gameView = boardView as com.example.spenserdubois.battleship.boardView
            super.onActivityReenter(resultCode, data)
            if(data !is Intent)
                return
            manager = data.getSerializableExtra("manager") as GameManager
            var tempPlayer : Player
            var otherPlayer : Player
            if(manager.playerTurn.equals("Player 2")) {
                tempPlayer = player2
                otherPlayer = player1
            }
            else {
                tempPlayer = player1
                otherPlayer = player2
            }
            miniView.setHitPath(otherPlayer.miniHitPath)
            miniView.setMissPath(otherPlayer.miniMissPath)
            miniView.drawBoats(otherPlayer.boats)
            miniView.invalidate()
            gameView.setSunkPath(tempPlayer.sunkPath)
            gameView.setHitPath(tempPlayer.hitPath)
            gameView.setMissPath(tempPlayer.missPath)
            gameView.invalidate()

    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        val gameView = boardView as com.example.spenserdubois.battleship.boardView
        super.onActivityReenter(resultCode, data)
        if(data !is Intent)
            return
        manager = data.getSerializableExtra("manager") as GameManager
        var tempPlayer : Player
        var otherPlayer : Player
        if(manager.playerTurn.equals("Player 2")) {
            tempPlayer = player2
            otherPlayer = player1
        }
        else {
            tempPlayer = player1
            otherPlayer = player2
        }
        miniView.setHitPath(otherPlayer.miniHitPath)
        miniView.setMissPath(otherPlayer.miniMissPath)
        miniView.drawBoats(otherPlayer.boats)
        miniView.invalidate()
        gameView.setSunkPath(tempPlayer.sunkPath)
        gameView.setHitPath(tempPlayer.hitPath)
        gameView.setMissPath(tempPlayer.missPath)
        gameView.invalidate()

    }
}
