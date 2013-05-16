package com.mcclellan.core.debug

import com.mcclellan.core.graphics.camera.Bounds
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mcclellan.core.graphics.camera.ScaledOrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.mcclellan.core.implicits.GdxPimps._
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.math.{ Vector2 => GdxVector }

object Box2dRenderer {
	val shapes = new ShapeRenderer
	def renderWorld(world : World, cam : ScaledOrthographicCamera) = {
		shapes.setProjectionMatrix(cam.projectionMatrix)
		shapes.begin(ShapeType.Line)
		shapes.setColor(1, 1, 1, 1)

		world.queryAABB((fixture) => {
			val body = fixture.getBody();
			fixture.getShape() match {
				case x : CircleShape => {
					if (!body.isAwake()) shapes.setColor(1, 0, 0, 1) else shapes.setColor(1, 1, 0, 1)
					shapes.circle(body.getPosition.x + x.getPosition.x, body.getPosition.y + x.getPosition.y, x.getRadius(), 8)
				}
				case edge : EdgeShape => {
					if (!body.isAwake()) shapes.setColor(1, 0, 0, 1) else shapes.setColor(1, 1, 0, 1)
					val point1 = new GdxVector
					val point2 = new GdxVector
					edge.getVertex1(point1)
					edge.getVertex2(point2)
					shapes.line(point1.x, point1.y, point2.x, point2.y)
				}
			}
			true
		}, cam.bounds)
		shapes.end()
	}
}