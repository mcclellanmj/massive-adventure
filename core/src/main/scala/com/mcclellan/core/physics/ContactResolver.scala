package com.mcclellan.core.physics

import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.World
import com.mcclellan.core.model.Projectile
import com.mcclellan.core.model.Player
import scala.collection.mutable.Queue
import com.badlogic.gdx.physics.box2d.Body
import com.mcclellan.core.model.DynamicBody
import com.badlogic.gdx.Gdx
import scala.collection.mutable.{Set => MutableSet}

class ContactResolver(queue : MutableSet[DynamicBody]) extends ContactListener {
	def beginContact(contact : Contact) = {
		val a = contact.getFixtureA.getBody.getUserData()
		val b = contact.getFixtureB.getBody.getUserData()

		(a, b) match {
			case (a : Player, b : Projectile) => queue += b
			case (a : Projectile, b : Player) => queue += a
			case _ =>
		}
	}
	def endContact(contact : Contact) = Unit

	def preSolve(contact : Contact, manifold : Manifold) = Unit
	def postSolve(contact : Contact, impulse : ContactImpulse) = Unit

}