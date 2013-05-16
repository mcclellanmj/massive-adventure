package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2
import com.mcclellan.core.math.Angle
import com.badlogic.gdx.graphics.g2d.Sprite

trait Updateable {
	def update(elapsed : Float)
}

trait Drawable {
	def position : Vector2
	def rotation : Angle
}