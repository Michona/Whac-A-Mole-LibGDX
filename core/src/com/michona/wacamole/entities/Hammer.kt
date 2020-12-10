package com.michona.wacamole.entities

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.michona.wacamole.ScoreManager

/** It relies too much on [MoleController]. Idk if I like that. */
class Hammer(private val viewport: Viewport,
             private val molePositionCallback: () -> Vector2?,
             private val onMoleHit: () -> Unit) : InputAdapter() {

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val molePos = molePositionCallback.invoke() ?: return true
        val worldClick = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))

        if (worldClick.dst(molePos) < Mole.MOLE_RADIUS) {
            onMoleHit.invoke()
            ScoreManager.hit()
        }

        return true
    }
}