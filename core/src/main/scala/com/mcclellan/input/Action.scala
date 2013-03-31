package com.mcclellan.input

abstract case class Action {
	def stop : Boolean
}

package actions {
	case class Up(val stop : Boolean) extends Action
	case class Left(val stop : Boolean) extends Action
	case class Right(val stop : Boolean) extends Action
	case class Down(val stop : Boolean) extends Action
}