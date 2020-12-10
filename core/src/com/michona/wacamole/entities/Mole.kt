package com.michona.wacamole.entities

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.michona.wacamole.ScoreManager

class Mole {

    private var _currentPosition: Vector2 = Vector2.Zero
    private var _state: State = State.HIDDEN

    /** Controlled by MoleController */
    fun changeState(randomPosition: Vector2, isHit: Boolean = false) {
        _currentPosition = randomPosition

        if (!isHit && _state == State.VISIBLE) {
            // User missed and mole hid.
            ScoreManager.miss()
        }

        _state = when (_state) {
            State.HIDDEN -> State.VISIBLE
            State.VISIBLE -> State.HIDDEN
        }

    }

    fun render(renderer: ShapeRenderer) {
        if (_state == State.VISIBLE) {
            renderer.apply {
                set(ShapeRenderer.ShapeType.Filled)
                color = Color.BLUE
                circle(_currentPosition.x, _currentPosition.y, MOLE_RADIUS)
            }
        }
    }

    /**
     * @return current position.
     * null if [_state] is [State.HIDDEN]
     * */
    fun getCurrentPosition(): Vector2? {
        return if (_state == State.HIDDEN) {
            null
        } else {
            _currentPosition
        }
    }

    enum class State {
        VISIBLE,
        HIDDEN
    }

    companion object {
        const val MOLE_RADIUS = 40f
    }
}