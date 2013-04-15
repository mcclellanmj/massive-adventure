package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2

class Projectile(var position : Vector2[Float], var velocity : Vector2[Float])
	extends Moveable with DerivedRotation with MoveableUpdate