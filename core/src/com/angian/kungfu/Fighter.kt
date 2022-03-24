package com.angian.kungfu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.math.abs


class Fighter: Actor() {
    private val texture: Texture
    private val textureRegion: TextureRegion
    private var velocityVec = Vector2(0f, 0f)


    init {
        val filename = "fighter_idle.png"
        //walk animation: idle-walk1-walk2

        texture = Texture(filename)
        textureRegion = TextureRegion(texture)

        this.width = texture.width.toFloat()
        this.height = texture.height.toFloat()
    }


    override fun draw(batch: Batch?, parentAlpha: Float) {
        val localColor = color
        batch?.setColor(localColor.r, localColor.g, localColor.b, localColor.a * parentAlpha)

        batch?.draw(textureRegion, x, y, width, height)

        batch?.setColor(localColor.r, localColor.g, localColor.b, 1f)

        super.draw(batch, parentAlpha)
    }


    override fun act(dt: Float)
    {
        applyPhysics(dt)

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocityVec.x = -GameConstants.FIGHTER_WALK_SPEED
            //flip the sprite horizontally
            scaleX = -abs(scaleX)

        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocityVec.x = GameConstants.FIGHTER_WALK_SPEED
            scaleX = +abs(scaleX)
        } else {
            velocityVec.x = 0f
        }

        super.act(dt)
    }

    private fun applyPhysics(dt: Float) {
        //velocityVec.y -= gravity * dt
        velocityVec.y = 0f
        moveBy(velocityVec.x * dt, velocityVec.y * dt)
        //belowSensor.setPosition(x + 4, y - 6)
    }

}