package com.angian.kungfu.screens

import com.angian.kungfu.Fighter
import com.angian.kungfu.GameConstants
import com.angian.kungfu.KungFuGame
import com.angian.kungfu.util.log
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.app.KtxScreen

class PlayScreen(private val gameApp: KungFuGame): KtxScreen, InputAdapter() {
    private val bkg: Image
    private val fighter: Fighter

    init {
        log("PlayScreen")
        gameApp.im.addProcessor(this)

        bkg = Image(Texture("white.png"))
        bkg.setSize(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT)
        bkg.color = Color.SLATE

        gameApp.stg.addActor(bkg)

        fighter = Fighter()
        gameApp.stg.addActor(fighter)

        bkg.toBack()
    }


    override fun render(delta: Float) {
        gameApp.stg.act()
        gameApp.stg.draw()
    }


    override fun dispose() {
        gameApp.im.removeProcessor(this)
        super.dispose()
    }


    override fun keyDown(keyCode: Int): Boolean {
        if (keyCode == Input.Keys.S) {
            fighter.punch()
        }

        return false
    }

}
