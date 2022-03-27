package com.angian.kungfu.actors

import com.angian.kungfu.GameConstants
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Actor


class Level(filename: String): Actor() {
    private val tiledMap = TmxMapLoader().load(filename)
    private val tiledCamera: OrthographicCamera
    private val tiledMapRenderer: OrthoCachedTiledMapRenderer

    init {
        val tileWidth = tiledMap.properties["tilewidth"] as Int
        val tileHeight = tiledMap.properties["tileheight"] as Int
        val numTilesHorizontal = tiledMap.properties["width"] as Int
        val numTilesVertical = tiledMap.properties["height"] as Int
        val mapWidth = tileWidth * numTilesHorizontal
        val mapHeight = tileHeight * numTilesVertical

        require((mapWidth.toFloat()) == GameConstants.WORLD_WIDTH)
        require((mapHeight.toFloat()) == GameConstants.WORLD_HEIGHT)

        tiledMapRenderer = OrthoCachedTiledMapRenderer(tiledMap)
        tiledMapRenderer.setBlending(true)
        tiledCamera = OrthographicCamera()
        //TODO: support windowed mode
        tiledCamera.setToOrtho(false, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT)
        tiledCamera.update()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        //sync with main camera
        val mainCamera = stage.camera
        tiledCamera.position.x = mainCamera.position.x
        tiledCamera.position.y = mainCamera.position.y
        tiledCamera.update()
        tiledMapRenderer.setView(tiledCamera)

        //force batch order
        batch.end()
        tiledMapRenderer.render()
        batch.begin()
    }

    fun getRectangleList(name: String): List<MapObject> {
        val list: MutableList<MapObject> = ArrayList()
        for (layer in tiledMap.layers) {
            for (obj in layer.objects) {
                if (obj !is RectangleMapObject) continue
                val props = obj.getProperties()
                if (props.containsKey("name") && props["name"] == name) list.add(obj)
            }
        }
        return list
    }

    fun getTileList(name: String): List<MapObject> {
        val list: MutableList<MapObject> = ArrayList()
        for (layer in tiledMap.layers) {
            for (obj in layer.objects) {
                if (obj !is TiledMapTileMapObject) continue
                val props = obj.getProperties()

                // Default MapProperties are stored within associated Tile object
                // Instance-specific overrides are stored in MapObject
                val t = obj.tile
                val defaultProps = t.properties
                if (defaultProps.containsKey("name") && defaultProps["name"] == name) list.add(obj)
                val propKeys = defaultProps.keys
                while (propKeys.hasNext()) {
                    val key = propKeys.next()
                    if (!props.containsKey(key)) {
                        //use default
                        props.put(key, defaultProps[key])
                    }
                }
            }
        }

        return list
    }

    fun initSolids() {
        for (obj in getRectangleList("Solid")) {
            val props = obj.properties

            val solid = Solid(
                props["x"] as Float, props["y"] as Float,
                props["width"] as Float, props["height"] as Float
            )

            this.stage.addActor(solid)
        }

    }
}
