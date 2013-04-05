package com.mcclellan.input

import com.mcclellan.core.math.Vector2
sealed trait Action {
}

package actions {
	case class Up(val stop : Boolean) extends Action
	case class Left(val stop : Boolean) extends Action
	case class Right(val stop : Boolean) extends Action
	case class Down(val stop : Boolean) extends Action
	case class AimAt(point : Vector2[Float]) extends Action
}