package com.mcclellan.core.implicits

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import language.implicitConversions
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.Fixture
import com.mcclellan.core.graphics.camera.Bounds
import com.badlogic.gdx.physics.box2d.QueryCallback

object GdxPimps {
	implicit def toPimpedWorld(world : World) : PimpedWorld = new PimpedWorld(world)
	implicit def toPimpedSpriteBatch(batch : SpriteBatch) : PimpedSpriteBatch = new PimpedSpriteBatch(batch)
}

class PimpedSpriteBatch(batch : SpriteBatch) {
	def begin[T](callback : => T) = {
		batch.begin
		val ret = callback
		batch.end
		ret
	}
}

class PimpedWorld(world : World) {
	def queryAABB(callback : Fixture => Boolean, bounds : Bounds) = {
		world.QueryAABB(new QueryCallback {
			def reportFixture(fixture : Fixture) = callback(fixture)
		}, bounds.bottomLeft.x, bounds.bottomLeft.y, bounds.topRight.x, bounds.topRight.y)
	} 
}