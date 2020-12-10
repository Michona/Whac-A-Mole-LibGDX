package com.michona.wacamole

import com.badlogic.gdx.Game

class WacAMoleGame : Game() {
    override fun create() {
        setScreen(MainScreen())
    }
}