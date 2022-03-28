package com.angian.kungfu.util

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor


fun Actor.makeBoundaryRectangle(): Polygon {
    val vertices = floatArrayOf(
        0f, 0f,
        width, 0f,
        width, height,
        0f, height
    )

    return Polygon(vertices)
}


interface Collider {
    val relativeBoundary: Polygon

    val absoluteBoundary: Polygon
        get() {
            require(this is Actor)
            relativeBoundary.setPosition(x, y)

            return relativeBoundary
        }

    fun overlaps(other: Collider): Boolean {
        val poly1 = this.absoluteBoundary
        val poly2 = other.absoluteBoundary

        // initial simpler test to improve performance
        return if (!poly1.boundingRectangle.overlaps(poly2.boundingRectangle))
            false
        else
            Intersector.overlapConvexPolygons(poly1, poly2)
    }

    fun preventOverlap(other: Collider): Vector2? {
        require(this is Actor)

        val poly1 = this.absoluteBoundary
        val poly2 = other.absoluteBoundary

        // initial simpler test to improve performance
        if (!poly1.boundingRectangle.overlaps(poly2.boundingRectangle))
            return null

        val mtv = MinimumTranslationVector()

        val polygonsOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv)
        if (!polygonsOverlap)
            return null

        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth)

        return mtv.normal
    }
}
