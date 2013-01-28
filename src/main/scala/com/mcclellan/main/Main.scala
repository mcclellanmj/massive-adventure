package com.mcclellan.main

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.AppGameContainer
import org.newdawn.slick.geom.Shape
import org.newdawn.slick.geom.Polygon
import com.mcclellan.models.Vector2d
import org.newdawn.slick.BasicGame

object Main {
  def main(args: Array[String]) = {
    new AppGameContainer(new MassiveAdventure("Abc")).start
    println("done")
  }
}

class MassiveAdventure(val title: String) extends BasicGame(title) {
  private var start = new Vector2d(0, 100)
  val acceleration = new Vector2d(22, 13)

  override def init(container: GameContainer) {}
  override def update(container: GameContainer, delta: Int) =
    start += acceleration * (delta / 1000.0)
    
  override def render(container: GameContainer, graphics: Graphics) =
    graphics.drawString("Hello world", start.x.toInt, start.y.toInt)
}