package com.michona.wacamole

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.Viewport
import com.michona.wacamole.entities.Mole
import com.michona.wacamole.util.LevelUtils


class MoleController(private val viewport: Viewport, initialTime: Long) {

    private var holes = listOf<Vector2>()

    /** Lets whac-a-mole :)  */
    private val mole = Mole()

    private var lastMoleChangeTime: Long = initialTime

    /** Called every frame from [MainScreen.render] */
    fun render(renderer: ShapeRenderer) {
        if (holes.isEmpty()) {
            computeVector2Holes()
        }

        renderer.apply {
            set(ShapeType.Filled)
            color = Color.RED
        }
        // Render holes
        holes.forEach { position ->
            renderer.circle(position.x, position.y, HOLE_RADIUS)
        }

        // Render mole
        checkMoleState()
        mole.render(renderer)
    }

    /** Called when viewport dimens change. [MainScreen.resize] */
    fun resize() {
        computeVector2Holes()
    }

    /** Called when hammer hits a mole. */
    fun onHit() {
        mole.changeState(randomPosition = getRandomMolePosition(), isHit = true)
        lastMoleChangeTime = TimeUtils.nanoTime()
    }

    fun getMolePosition() = mole.getCurrentPosition()

    /**
     * Checks if we need to change the state of [Mole] i.e show/hide. Based on time elapsed.
     * @see [ScoreManager.getAscensionSpeed]
     *  */
    private fun checkMoleState() {
        val timePassedInNano = TimeUtils.nanoTime() - lastMoleChangeTime
        val timePassedInMilli = MathUtils.nanoToSec * timePassedInNano * 1000
        if (timePassedInMilli > ScoreManager.getAscensionSpeed()) {
            mole.changeState(getRandomMolePosition())
            lastMoleChangeTime = TimeUtils.nanoTime()
        }
    }

    private fun computeVector2Holes() {
        holes = LevelUtils.RAW_HOLE_LIST.asSequence().map { (x, y) ->
            Vector2(x * viewport.worldWidth / 4, y * viewport.worldHeight / 4)
        }.toList()
    }

    private fun getRandomMolePosition(): Vector2 {
        val randomIndex = (0..LevelUtils.RAW_HOLE_LIST.lastIndex).random()
        val (randomX, randomY) = LevelUtils.RAW_HOLE_LIST[randomIndex]

        return Vector2(randomX * viewport.worldWidth / 4, randomY * viewport.worldHeight / 4)
    }

    companion object {
        private const val HOLE_RADIUS = 30f
    }
}