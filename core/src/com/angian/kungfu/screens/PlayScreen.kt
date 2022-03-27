package com.angian.kungfu.screens

import com.angian.kungfu.GameConstants
import com.angian.kungfu.KungFuGame
import com.angian.kungfu.actors.Fighter
import com.angian.kungfu.actors.Level
import com.angian.kungfu.util.log
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.app.KtxScreen


class PlayScreen(private val gameApp: KungFuGame): KtxScreen, InputAdapter() {
    private val bkg: Image
    private val level: Level
    private val fighter: Fighter

    init {
        log("PlayScreen")
        gameApp.im.addProcessor(this)

        bkg = Image(Texture("white.png"))
        bkg.setSize(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT)
        bkg.color = Color.SLATE
        gameApp.stg.addActor(bkg)

        level = Level("level_00.tmx")
        gameApp.stg.addActor(level)
        level.initSolids()

        fighter = Fighter()
        gameApp.stg.addActor(fighter)
        gameApp.stg.addActor(fighter.belowSensor)

        level.toBack()
        bkg.toBack()
    }


    override fun render(delta: Float) {
        gameApp.stg.act()

        update(delta)

        gameApp.stg.draw()
    }


    private fun update(delta: Float) {
        /*
        for (platform in platforms) {
            if (bobby.overlaps(platform)) {
                val offset: Vector2 = bobby.preventOverlap(platform)
                if (offset != null) {
                    if (Math.abs(offset.x) > Math.abs(offset.y)) bobby.velocityVec.x = 0 else bobby.velocityVec.y = 0
                }
            }
        }
        */

        if (fighter.y < 0f) {
            fighter.y = 0f
            fighter.velocityVec.y = 0f
        }
    }


    override fun dispose() {
        gameApp.im.removeProcessor(this)
        super.dispose()
    }


    override fun keyDown(keyCode: Int): Boolean {
        if (keyCode == Input.Keys.S) {
            fighter.punch()
        } else if (keyCode == Input.Keys.UP) {
            fighter.jump()
        }

        return false
    }

}
