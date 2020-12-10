package com.michona.wacamole

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.michona.wacamole.entities.Hammer


class MainScreen : ScreenAdapter() {

    private var _extendedViewport: ExtendViewport? = null
    private var _shareRenderer: ShapeRenderer? = null
    private var _spriteBatch: SpriteBatch? = null
    private var _font: BitmapFont? = null

    private var _moleController: MoleController? = null
    private var _hammer: Hammer? = null

    override fun show() {
        _extendedViewport = ExtendViewport(WORLD_SIZE, WORLD_SIZE)
        _shareRenderer = ShapeRenderer().apply {
            setAutoShapeType(true)
        }

        _spriteBatch = SpriteBatch()
        _font = BitmapFont().apply {
            data?.setScale(2f)
            region.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
        }

        _moleController = MoleController(viewport = _extendedViewport!!, initialTime = TimeUtils.nanoTime())
        _hammer = Hammer(
                viewport = _extendedViewport!!,
                molePositionCallback = _moleController!!::getMolePosition,
                onMoleHit = _moleController!!::onHit
        )

        Gdx.input.inputProcessor = _hammer
    }

    override fun render(delta: Float) {
        _extendedViewport?.apply()

        // Clear background
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT)

        _shareRenderer?.projectionMatrix = _extendedViewport?.camera?.combined

        // Render
        _shareRenderer?.apply {
            begin(ShapeRenderer.ShapeType.Filled)
            _moleController?.render(this)
            end()
        }

        _spriteBatch?.begin()
        _font?.draw(_spriteBatch, ScoreManager.getDisplayPoints(),
                100f, 50f)
        _spriteBatch?.end()
    }

    override fun resize(width: Int, height: Int) {
        _extendedViewport?.update(width, height, true)
        _moleController?.resize()
    }

    override fun dispose() {
        _shareRenderer?.dispose()
        _spriteBatch?.dispose()
        _font?.dispose()

        _spriteBatch = null
        _font = null
        _shareRenderer = null
        _extendedViewport = null
    }

    companion object {
        private const val WORLD_SIZE = 480f
    }
}