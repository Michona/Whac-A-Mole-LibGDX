package com.michona.wacamole

/** TODO Need some good solution for dependency injection. Having these objects is no good. Koin? */
object ScoreManager {

    private var _points: Int = 0

    fun hit() {
        _points += 2
    }

    fun miss() {
        if (_points > 0) {
            _points--
        }
    }

    fun getDisplayPoints() = "Score: $_points"

    /** @return the speed the mole hides/shows based on points. In milliseconds */
    fun getAscensionSpeed(): Long {
        return INITIAL_SPEED - (_points * DECREMENT)
    }

    private const val INITIAL_SPEED: Long = 1_500 // 2 seconds
    private const val DECREMENT: Long = 30 // in millies
}