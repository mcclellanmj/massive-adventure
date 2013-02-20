package com.mcclellan.main

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.AppGameContainer
import org.newdawn.slick.geom.Shape
import org.newdawn.slick.geom.Polygon
import com.mcclellan.models.Vector2f
import org.newdawn.slick.BasicGame
import com.mcclellan.models.Images
import org.newdawn.slick.Image

object Main extends App{
    new AppGameContainer(new MassiveAdventure("Abc")).start
    println("done")
}

class MattInt(val value : Int) {
  def asDecimal : Float = value / 1000.0f
}

class MassiveAdventure(val title: String) extends BasicGame(title) {
  private var start = Vector2f(0, 100)
  private val acceleration = Vector2f(22, 13)
  lazy val character = Images.player
  
  implicit def asDecimal(x : Int) : MattInt = new MattInt(x)

  override def init(container: GameContainer)={
  }
  
  override def update(container: GameContainer, delta: Int) =
    start += acceleration * (delta asDecimal)
    
  override def render(container: GameContainer, graphics: Graphics) = {
    character.draw(start.x, start.y, .5f);
  }
}