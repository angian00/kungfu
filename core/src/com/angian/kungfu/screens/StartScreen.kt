package com.angian.kungfu.screens

import com.angian.kungfu.KungFuGame
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.app.KtxScreen

class StartScreen(private val gameApp: KungFuGame): KtxScreen, InputAdapter() {
    private val bkg: Image

    init {
        gameApp.im.addProcessor(this)

        bkg = Image(Texture("start_background.png"))
        gameApp.stg.addActor(bkg)
    }


}