package com.angian.kungfu

import com.angian.kungfu.screens.StartScreen
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.app.KtxGame


class KungFuGame: KtxGame<Screen>() {

    var width = 0f
    var height = 0f

    private lateinit var sb: SpriteBatch
    private lateinit var view: StretchViewport

    lateinit var cam: OrthographicCamera
    lateinit var stg: Stage
    lateinit var im: InputMultiplexer


    override fun create() {
        width = Gdx.graphics.width.toFloat()
        height = Gdx.graphics.height.toFloat()

        this.sb = SpriteBatch()

        this.cam = OrthographicCamera(this.width, this.height)
        this.view = StretchViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, this.cam)
        this.stg = Stage(this.view, this.sb)

        this.im = InputMultiplexer()
        im.addProcessor(this.stg)
        Gdx.input.inputProcessor = im

        addScreen(StartScreen(this))
        setScreen<StartScreen>()
    }


    override fun dispose() {
        this.sb.dispose()
        this.stg.dispose()
        super.dispose()
    }
}