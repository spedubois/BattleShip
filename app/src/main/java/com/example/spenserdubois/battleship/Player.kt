package com.example.spenserdubois.battleship

import android.graphics.Path
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Spenser DuBois on 11/2/2017.
 */
class Player : Serializable{
    var ships = Array(10, {IntArray(10)})
    var shots = Array(10, {IntArray(10)})
    var miniW : Float = 0f
    var miniH : Float = 0f
    var missPath = MyPath()
    var hitPath = MyPath()
    var boatsPath = MyPath()
    var sunkPath = MyPath()
    var miniHitPath = MyPath()
    var miniMissPath = MyPath()
    var scale : Float
    var carrier : Boat
    var battle : Boat
    var cruise : Boat
    var sub : Boat
    var destroyer : Boat
    var hits : Int
    var name : String = ""
    var boats = ArrayList<Boat>()
    var boatsLeft : Int


    constructor(scale : Int)
    {
        boatsLeft = 5
        carrier = Boat(5, 5)
        battle = Boat(4 ,4)
        cruise = Boat(3, 3)
        sub = Boat(3, 2)
        destroyer = Boat(2, 1)
        boats.add(carrier)
        boats.add(cruise)
        boats.add(sub)
        boats.add(destroyer)
        boats.add(battle)
        this.scale = scale + 0f
        placeShip(carrier)
        placeShip(battle)
        placeShip(cruise)
        placeShip(sub)
        placeShip(destroyer)
        hits = 0
    }


    fun placeShip(boat : Boat)
    {
        var rand = Random()
        while(true)
        {
            var x = rand.nextInt(10)
            var y = rand.nextInt(10)
            if(ships[x][y] != 1)
            {
                var dirRand = Random()
                var dir = dirRand.nextInt(5)
                if(!checkPlacement(x, y, boat.life, boat.id, boat, dir))
                    continue
                else
                    break

            }

        }
    }

    fun checkPlacement (x : Int, y : Int, size : Int, id : Int, boat: Boat, dir : Int) : Boolean
    {
        when(dir)
        {
            0 -> {
                if((x + size) > 9)
                    return false
                for(i in 1 until size)
                {
                    if(ships[x+i][y] > 0)
                        return false
                }
                for(i in 0 until size)
                {
                    ships[x+i][y] = id
                    boat.coords.add(Coord(y,x+i))
                }
                return true
            }
            1 -> {
                if((y - size) < 0)
                    return false
                for(i in 1 until size)
                {
                    if(ships[x][y-i] > 0)
                        return false
                }
                for(i in 0 until size)
                {
                    ships[x][y-i] = id
                    boat.coords.add(Coord(y-i,x))
                }
                return true
            }
            2 -> {
                if((x - size) < 0)
                    return false
                for(i in 1 until size)
                {
                    if(ships[x-i][y] > 0)
                        return false
                }
                for(i in 0 until size)
                {
                    ships[x-i][y] = id
                    boat.coords.add(Coord(y,x-i))
                }
                return true
            }
            3 -> {
                if((y + size) > 9)
                    return false
                for(i in 1 until size)
                {
                    if(ships[x][y+i] > 0)
                        return false
                }
                for(i in 0 until size)
                {
                    ships[x][y+i] = id
                    boat.coords.add(Coord(y+i,x))
                }
                return true
            }
        }
        return false
    }

    fun hitBoat(id : Int) : Int
    {
        when(id)
        {
            5 -> {
                carrier.life--
                return carrier.life
            }
            4 -> {
                battle.life--
                return battle.life
            }
            3 -> {
                cruise.life--
                return cruise.life
            }
            2 ->
            {
                sub.life--
                return sub.life
            }
            1 -> {
                destroyer.life--
                return destroyer.life
            }
        }
        return -1
    }

    fun getBoat(id : Int) : Boat
    {
        when(id)
        {
            5 -> {
                return carrier
            }
            4 -> {
                return battle
            }
            3 -> {
                return cruise
            }
            2 ->
            {
                return sub
            }
            1 -> {

                return destroyer
            }
        }
        return carrier
    }

}