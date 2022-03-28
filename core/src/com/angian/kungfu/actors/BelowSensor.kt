package com.angian.kungfu.actors

import com.angian.kungfu.util.Collider
import com.angian.kungfu.util.makeBoundaryRectangle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.scenes.scene2d.Actor

class BelowSensor(private val parent: Fighter): Actor(), Collider {
    private val texture = Texture("white.png")
    override val relativeBoundary: Polygon

    init {
        this.setSize(parent.width - 8f, 6f)

        relativeBoundary = makeBoundaryRectangle()
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

}