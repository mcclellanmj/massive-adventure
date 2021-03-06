package com.mcclellan.desktop.input

import com.mcclellan.input.actions._
import com.mcclellan.input.MappedInputProcessor
import com.mcclellan.input.UserInputListener
import com.mcclellan.core.math.Vector2

class DesktopInput extends MappedInputProcessor {
	import language.implicitConversions
	var game : UserInputListener = null

	implicit def forceTrue(fn : => Unit) : Boolean = {
		fn
		true
	}

	def keyDown(keycode : Int) : Boolean = {
		if (keycode == 19) {
			game.userAction(Up(false))
		} else if (keycode == 21) {
			game.userAction(Left(false))
		} else if (keycode == 22) {
			game.userAction(Right(false))
		} else if (keycode == 20) {
			game.userAction(Down(false))
		} else println(keycode)
	}
	def keyUp(keycode : Int) : Boolean = {
		if (keycode == 19) {
			game.userAction(Up(true))
		} else if (keycode == 21) {
			game.userAction(Left(true))
		} else if (keycode == 22) {
			game.userAction(Right(true))
		} else if (keycode == 20) {
			game.userAction(Down(true))
		} else println(keycode)
	}
	def keyTyped(character : Char) : Boolean = true

	def touchDown(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean = button match {
		case 0 => game.userAction(Fire(true))
		case 1 => game.userAction(SecondaryFire(true))
	}
	
	def touchUp(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean = button match {
		case 0 => game.userAction(Fire(false))
		case 1 => game.userAction(SecondaryFire(false))
	}
	
	def touchDragged(screenX : Int, screenY : Int, pointer : Int) : Boolean = mouseMoved(screenX, screenY)

	def mouseMoved(screenX : Int, screenY : Int) : Boolean = game.userAction(AimAt(new Vector2(screenX, screenY)))
	def scrolled(amount : Int) : Boolean = true
}