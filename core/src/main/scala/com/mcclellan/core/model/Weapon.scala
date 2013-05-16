package com.mcclellan.core.model

import com.mcclellan.core.physics.WorldConnector
import com.mcclellan.core.math.Vector2
import com.mcclellan.core.GameContainer
import com.mcclellan.core.math.Angle
import com.mcclellan.core.math.Degrees
import com.mcclellan.core.implicits.VectorImplicits.toGdxVector

trait Weapon extends Updateable {
	def update(elapsed:Float)
}

class Shotgun(implicit val game : GameContainer) extends Weapon {
	private var elapsedSinceFire = Float.MaxValue
	private var randomChaos = (Math.random() /8) - 1/16
	
	def update(elapsed:Float) = {
		elapsedSinceFire += elapsed
		if(elapsedSinceFire > .7 + randomChaos) {
			createPellets(game.player.rotation)
			elapsedSinceFire = 0
			randomChaos= (Math.random() /8) - 1/16
		}
	}
	
	def createPellets(direction : Angle) = {
		implicit val worldConnector = game.world
		val dir = Vector2.fromAngle(direction)
		// TODO: Obtain bullet spawn point external resource
		val position = game.player.position + (dir * .15f)
		
		// TODO: Add new bullets to the game so they update and draw, currently only in physics world
		val newProjectiles = for{i <- 1 to 44
			rotatedVector = dir.rotate(Degrees((Math.random() * 8 - 4).toFloat))
		} yield new Projectile(position + (rotatedVector * (Math.random().toFloat / 6f)), rotatedVector * 3, 1)
		newProjectiles.foreach(game.addComponent(_))
		game.player.body.applyForceToCenter(-dir * .3f, true)
	}
}

class AssaultRifle(implicit val game : GameContainer) extends Weapon {
	private var elapsedSinceFire = Float.MaxValue
	private var timeSinceLastBurst = Float.MaxValue
	private var bursts = 0
	private var isBursting = false
	
	def update(elapsed:Float) = {
		if(isBursting) {
			timeSinceLastBurst += elapsed
			if(timeSinceLastBurst > .02) {
				createProjectile(game.player.rotation)
				timeSinceLastBurst = 0
				bursts += 1
			} else if(bursts > 2) {
				isBursting = false
				bursts = 0
			}
			
		} else if(elapsedSinceFire > .4) {
			isBursting = true
			elapsedSinceFire = 0
		}
		elapsedSinceFire += elapsed
	}
	
	def createProjectile(direction : Angle) = {
		implicit val worldConnector = game.world
		val dir = Vector2.fromAngle(direction)
		// TODO: Obtain bullet spawn point external resource
		val position = game.player.position + (dir * .15f)
		
		game.addComponent(new Projectile(position, dir.rotate(Degrees(((Math.random * 2) - 1).toFloat)) * 4, 15))
	}
}