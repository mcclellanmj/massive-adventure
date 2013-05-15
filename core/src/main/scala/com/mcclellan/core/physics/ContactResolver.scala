package com.mcclellan.core.physics

import scala.collection.mutable.{ Set => MutableSet }
import com.mcclellan.core.model.DynamicBody
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Contact
import com.mcclellan.core.model.Projectile
import com.mcclellan.core.model.Player
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.mcclellan.core.model.MyBody
import com.mcclellan.core.model.MyBody
import com.mcclellan.core.model.Wall
import com.mcclellan.core.model.Projectile
import scala.language.existentials
import com.mcclellan.core.model.Enemy

case class ContactHandler[T, F](code : (T, F, MutableSet[DynamicBody]) => Any)(implicit t : Manifest[T], f : Manifest[F]) {
	private val pair = (t.runtimeClass, f.runtimeClass)
	
	def getHandler[A, B](a : A, b : B) : Option[(MutableSet[DynamicBody]) => Any] = {
		val classPair = (a.getClass(), b.getClass())
		if (this.pair == classPair) {
			Some(code(a.asInstanceOf[T], b.asInstanceOf[F], _))
		} else if (this.pair.swap == classPair) {
			Some(code(b.asInstanceOf[T], a.asInstanceOf[F], _))
		} else None
	}
}

object Handlers {
	lazy val wallToProjectile = ContactHandler((a : Wall, b : Projectile, queue) => queue += b)
			
	lazy val playerToProjectile = ContactHandler((a : Enemy, b : Projectile, queue) => {
		queue += b
		a.health -= 1
	})
	
	lazy val wallToPlayer = ContactHandler((a:Wall, b:Player, queue) => b.health-=10)
	
	lazy val allHandlers = Seq(wallToProjectile, playerToProjectile, wallToPlayer)
}

class ContactResolver(queue : MutableSet[DynamicBody], handlers : Seq[ContactHandler[_, _]]) extends ContactListener {
	def beginContact(contact : Contact) = {
		val a = contact.getFixtureA.getBody.getUserData
		val b = contact.getFixtureB.getBody.getUserData
		handlers.foreach(_.getHandler(a, b) match {
			case Some(code) => code(queue)
			case _ =>
		})
	}
	def endContact(contact : Contact) = Unit

	def preSolve(contact : Contact, manifold : Manifold) = Unit
	def postSolve(contact : Contact, impulse : ContactImpulse) = Unit

}