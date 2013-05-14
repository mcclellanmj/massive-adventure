package com.mcclellan.core.model

import com.mcclellan.core.physics.WorldConnector

trait Weapon {
	def update(elapsed:Float, isFiring:Boolean)
}

class Shotgun(implicit world : WorldConnector) extends Weapon {
	private var lastFire : Long = 0
	
	def update(elapsed:Float, isFiring:Boolean) = {
		if(isFiring) {
			lastFire = System.currentTimeMillis()
		}
	}
}

class AssaultRifle(implicit world : WorldConnector) extends Weapon {
	def update(elapsed:Float, isFiring:Boolean) = Unit
}