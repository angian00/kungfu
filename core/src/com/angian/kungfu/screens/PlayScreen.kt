package com.angian.kungfu.screens

import com.angian.kungfu.GameConstants
import com.angian.kungfu.KungFuGame
import com.angian.kungfu.actors.Fighter
import com.angian.kungfu.actors.Level
import com.angian.kungfu.actors.Solid
import com.angian.kungfu.util.log
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.app.KtxScreen
import kotlin.math.abs


class PlayScreen(private val gameApp: KungFuGame): KtxScreen, InputAdapter() {
    private val bkg: Image
    private val level: Level
    private val fighter: Fighter
    val solids: Set<Solid>

    init {
        log("PlayScreen")
        gameApp.im.addProcessor(this)

        bkg = Image(Texture("white.png"))
        bkg.setSize(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT)
        bkg.color = Color.SLATE
        gameApp.stg.addActor(bkg)

        level = Level("level_00.tmx")
        gameApp.stg.addActor(level)
        solids = initSolids(level)

        fighter = Fighter(this)
        fighter.setPosition(0f, 80f)
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
        for (solid in solids) {
            if (fighter.overlaps(solid)) {
                val offset = fighter.preventOverlap(solid)
                if (offset != null) {
                    if (abs(offset.x) > abs(offset.y))
                        fighter.velocityVec.x = 0f
                    else {
                        fighter.velocityVec.y = 0f
                        fighter.stopJump()
                    }
                }
            }
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


    private fun initSolids(level: Level): Set<Solid> {
        val solids = mutableSetOf<Solid>()

        for (obj in level.getRectangleList("Solid")) {
            val props = obj.properties

            val solid = Solid(
                props["x"] as Float, props["y"] as Float,
                props["width"] as Float, props["height"] as Float
            )

            solids.add(solid)

            gameApp.stg.addActor(solid)
        }

        return solids
    }
}
