package com.angian.kungfu.actors

import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.scenes.scene2d.Actor

class Solid(x: Float, y: Float, width: Float, height: Float): Actor() {
    private val boundary: Polygon

    init {
        setPosition(x, y)
        setSize(width, height)

        boundary = boundaryRectangle()
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