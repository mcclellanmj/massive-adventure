package com.mcclellan.core.model

import com.mcclellan.core.physics.WorldConnector
import com.mcclellan.core.math.Vector2
import com.mcclellan.core.GameConnector
import com.mcclellan.core.math.Angle
import com.mcclellan.core.math.Degrees

trait Weapon {
	def update(elapsed:Float, isFiring:Boolean)
}

class Shotgun(implicit world : WorldConnector, game : GameConnector) extends Weapon {
	private var elapsedSinceFire = Float.MaxValue
	private var randomChaos = (Math.random() /8) - 1/16
	
	def update(elapsed:Float, isFiring:Boolean) = {
		elapsedSinceFire += elapsed
		if(isFiring && elapsedSinceFire > .7 + randomChaos) {
			createPellets(game.player.rotation)
			elapsedSinceFire = 0
			randomChaos= (Math.random() /8) - 1/16
			
		}
	}
	
	def createPellets(direction : Angle) = {
		val dir = Vector2.fromAngle(direction)
		// TODO: Obtain bullet spawn point external resource
		val position = Vector2(game.player.position.x + (dir.x * .15f), game.player.position.y + (dir.y * .15f))
		
		val pellets = (1 to 37).map(n => {
			dir.rotate(Degrees((Math.random() * 8 - 4).toFloat))
		}).foreach(pellet => {
			val newBullet = new Projectile(position + (pellet * (Math.random().toFloat / 6f)), (pellet * 7))
		})
	}
}

class AssaultRifle(implicit world : WorldConnector, game : GameConnector) extends Weapon {
	def update(elapsed:Float, isFiring:Boolean) = Unit
}