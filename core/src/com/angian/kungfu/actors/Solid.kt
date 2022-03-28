package com.angian.kungfu.actors

import com.angian.kungfu.util.Collider
import com.angian.kungfu.util.makeBoundaryRectangle
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.scenes.scene2d.Actor

class Solid(x: Float, y: Float, width: Float, height: Float): Actor(), Collider {
    override val relativeBoundary: Polygon

    init {
        setPosition(x, y)
        setSize(width, height)

        relativeBoundary = makeBoundaryRectangle()
    }


}