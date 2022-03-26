package com.angian.kungfu

import com.angian.kungfu.GameConstants.ANIM_FRAME_DURATION
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import kotlin.math.abs


const val EPSILON = 0.001f


class Fighter: Actor() {
    private val walkAnimation: Animation<TextureRegion>
    private val punchAnimation: Animation<TextureRegion>
    private var currAnimation: Animation<TextureRegion>

    private var velocityVec = Vector2(0f, 0f)
    private var elapsedTime = 0f
    private var animationPaused = true
    private var currAnimationStart = 0f

    private val isRunning: Boolean
    get() {
        return abs(velocityVec.x) >= EPSILON
    }

    init {
        val walkFrames = loadFrames("fighter_idle.png", "fighter_walk1.png", "fighter_walk2.png")
        walkAnimation = Animation<TextureRegion>(ANIM_FRAME_DURATION, walkFrames, Animation.PlayMode.LOOP)

        val punchFrames = loadFrames("fighter_happy.png")
        punchAnimation = Animation<TextureRegion>(3 * ANIM_FRAME_DURATION, punchFrames, Animation.PlayMode.NORMAL)

        currAnimation = walkAnimation

        this.width = walkAnimation.getKeyFrame(0f).regionWidth.toFloat()
        this.height = walkAnimation.getKeyFrame(0f).regionHeight.toFloat()
        this.originX = this.width / 2
        this.originY = this.height / 2
    }


    override fun draw(batch: Batch?, parentAlpha: Float) {
        val localColor = color
        batch?.setColor(localColor.r, localColor.g, localColor.b, localColor.a * parentAlpha)

        val textureRegion = currAnimation.getKeyFrame(elapsedTime - currAnimationStart)
        batch?.draw(textureRegion, x, y, originX, originY, width, height, scaleX, scaleY, rotation)

        batch?.setColor(localColor.r, localColor.g, localColor.b, 1f)

        super.draw(batch, parentAlpha)
    }


    override fun act(dt: Float) {
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

        animationPaused = ((currAnimation == walkAnimation) && (!isRunning))
        if (!animationPaused) {
            elapsedTime += dt

            if (currAnimation.isAnimationFinished(elapsedTime - currAnimationStart)) {
                //switch back to walk
                currAnimationStart = elapsedTime
                currAnimation = walkAnimation
            }
        }

    }

    private fun applyPhysics(dt: Float) {
        //velocityVec.y -= gravity * dt
        velocityVec.y = 0f
        moveBy(velocityVec.x * dt, velocityVec.y * dt)
        //belowSensor.setPosition(x + 4, y - 6)
    }

    private fun loadFrames(vararg filenames: String): Array<TextureRegion> {
        val res = Array<TextureRegion>()

        for (filename in filenames) {
            res.add(TextureRegion(Texture(filename)))
        }

        return res
    }


    fun punch() {
        currAnimation = punchAnimation
        currAnimationStart = elapsedTime
    }
}