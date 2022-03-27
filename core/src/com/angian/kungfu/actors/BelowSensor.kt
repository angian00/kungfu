package com.angian.kungfu.actors

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.scenes.scene2d.Actor

class BelowSensor(private val parent: Fighter): Actor() {
    private val texture = Texture("white.png")
    private val boundary: Polygon

    init {
        this.setSize(parent.width - 8f, 6f)

        boundary = boundaryRectangle()
    }


    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (!isVisible)
            return

        val localColor = color
        batch?.setColor(localColor.r, localColor.g, localColor.b, localColor.a * parentAlpha)

        batch?.draw(texture, x, y, width, height)

        batch?.setColor(localColor.r, localColor.g, localColor.b, 1f)

        super.draw(batch, parentAlpha)
    }

    fun boundaryRectangle(): Polygon {
        val vertices = floatArrayOf(
            0f, 0f,
            width, 0f,
            width, height,
            0f, height
        )

        return Polygon(vertices)
    }
}