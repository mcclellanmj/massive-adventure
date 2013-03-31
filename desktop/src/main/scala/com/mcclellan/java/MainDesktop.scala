package com.mcclellan.java

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.mcclellan.core.Main
import com.badlogic.gdx.InputProcessor
import com.mcclellan.input.MappedInputProcessor
import com.badlogic.gdx.ApplicationListener
import com.mcclellan.input.GameListener
import com.mcclellan.input.actions._

class DesktopInput extends MappedInputProcessor {
  var game: GameListener = null

  implicit def forceTrue(fn: => Unit): Boolean = {
    fn
    true
  }

  def keyDown(keycode: Int): Boolean = {
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
  def keyUp(keycode: Int): Boolean = {
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
  def keyTyped(character: Char): Boolean = println("Char " + character)

  def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = true
  def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = true
  def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = true

  def mouseMoved(screenX: Int, screenY: Int): Boolean = true
  def scrolled(amount: Int): Boolean = true
}

object MainDesktop extends App {
  val config = new LwjglApplicationConfiguration
  config.useGL20 = true
  config.width = 800
  config.height = 600
  val app = new LwjglApplication(new Main(new DesktopInput), config)
}