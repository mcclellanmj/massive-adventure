package com.mcclellan.core.model

import com.mcclellan.core.math.Vector2


class Player(var position : Vector2[Float], var velocity : Vector2[Float], var rotation : Float) 
	extends Moveable with MoveableUpdate 