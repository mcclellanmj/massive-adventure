package com.mcclellan.core.graphics.camera

import com.badlogic.gdx.graphics.OrthographicCamera
import com.mcclellan.core.math.Vector2
import com.mcclellan.core.implicits.VectorImplicits._

case class Bounds(val bottomLeft : Vector2, val topRight : Vector2)

class ScaledOrthographicCamera(val scale : Float, val viewWidth : Int, val viewHeight : Int) {
	val cam = new OrthographicCamera(viewWidth * scale, viewHeight * scale)
	def bounds : (Bounds) = Bounds(new Vector2(cam.frustum.planePoints(0).x, cam.frustum.planePoints(0).y), new Vector2(cam.frustum.planePoints(2).x, cam.frustum.planePoints(2).y))

	def position : Vector2 = new Vector2(cam.position.x, cam.position.y)
	def position_=(pos : Vector2) = {
		cam.position.x = pos.x
		cam.position.y = pos.y
		cam.update()
	}

	def projectionMatrix = cam.combined
}