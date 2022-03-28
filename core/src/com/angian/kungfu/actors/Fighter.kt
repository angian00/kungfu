package com.angian.kungfu.actors

import com.angian.kungfu.GameConstants
import com.angian.kungfu.GameConstants.ANIM_JUMP_DURATION
import com.angian.kungfu.GameConstants.ANIM_PUNCH_DURATION
import com.angian.kungfu.GameConstants.ANIM_WALK_FRAME_DURATION
import com.angian.kungfu.screens.PlayScreen
import com.angian.kungfu.util.Collider
import com.angian.kungfu.util.log
import com.angian.kungfu.util.makeBoundaryRectangle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import kotlin.math.abs


const val EPSILON = 0.001f


class Fighter(val screen: PlayScreen): Actor(), Collider {
    val belowSensor: BelowSensor
    var velocityVec = Vector2(0f, 0f)
    override val relativeBoundary: Polygon

    private val walkAnimation: Animation<TextureRegion>
    private val punchAnimation: Animation<TextureRegion>
    private val jumpAnimation: Animation<TextureRegion>
    private var currAnimation: Animation<TextureRegion>

    private var elapsedTime = 0f
    private var animationPaused = true
    private var currAnimationStart = 0f

    private val isRunning: Boolean
    get() {
        return abs(velocityVec.x) >= EPSILON
    }

    private val isJumping: Boolean
    get() {
        return velocityVec.y > EPSILON
    }

    private val isFalling: Boolean
    get() {
        return velocityVec.y < -EPSILON
    }

    private val isOnSolid: Boolean
    get() {
        for (solid in screen.solids) {
            if (belowSensor.overlaps(solid))
                return true
        }

        return false
    }

    init {
        val walkFrames = loadFrames("fighter_idle.png", "fighter_walk1.png", "fighter_walk2.png")
        walkAnimation = Animation<TextureRegion>(ANIM_WALK_FRAME_DURATION, walkFrames, Animation.PlayMode.LOOP)

        val punchFrames = loadFrames("fighter_happy.png")
        punchAnimation = Animation<TextureRegion>(ANIM_PUNCH_DURATION, punchFrames, Animation.PlayMode.NORMAL)

        val jumpFrames = loadFrames("fighter_jump.png")
        jumpAnimation = Animation<TextureRegion>(ANIM_JUMP_DURATION, jumpFrames, Animation.PlayMode.NORMAL)

        currAnimation = walkAnimation

        this.width = walkAnimation.getKeyFrame(0f).regionWidth.toFloat()
        this.height = walkAnimation.getKeyFrame(0f).regionHeight.toFloat()
        this.originX = this.width / 2
        this.originY = this.height / 2

        relativeBoundary = makeBoundaryRectangle()

        belowSensor = BelowSensor(this)
        belowSensor.isVisible = true //DEBUG
        //belowSensor.isVisible = false
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
        super.act(dt)

        if ( (!isFalling) && (!isJumping) ) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                velocityVec.x = -GameConstants.FIGHTER_WALK_SPEED
                //flip the sprite horizontally
                scaleX = -abs(scaleX)

            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velocityVec.x = GameConstants.FIGHTER_WALK_SPEED
                scaleX = +abs(scaleX)
            } else {
                if (!isJumping) {
                    velocityVec.x = 0f
                }
            }
        }

        applyPhysics(dt)

        if (this.isOnSolid) {
            belowSensor.color = Color.GREEN
        } else {
            belowSensor.color = Color.RED
        }

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
        velocityVec.y -= GameConstants.GRAVITY * dt
        moveBy(velocityVec.x * dt, velocityVec.y * dt)
        belowSensor.setPosition(x + 4, y - 6)
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

    fun jump() {
        if (isJumping || isFalling)
            return

        velocityVec.y = GameConstants.FIGHTER_JUMP_SPEED

        currAnimation = jumpAnimation
        currAnimationStart = elapsedTime
    }

    fun stopJump() {
        velocityVec.y = 0f

        if (currAnimation != walkAnimation) {
            currAnimation = walkAnimation
            currAnimationStart = elapsedTime
        }
    }
}