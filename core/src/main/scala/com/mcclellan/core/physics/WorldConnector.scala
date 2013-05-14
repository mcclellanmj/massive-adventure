package com.mcclellan.core.physics

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.Body

class WorldConnectorImpl(val world : World) extends WorldConnector {
	def createBody(body : BodyDef) = world.createBody(body)
}

trait WorldConnector {
	def createBody(body : BodyDef) : Body
}