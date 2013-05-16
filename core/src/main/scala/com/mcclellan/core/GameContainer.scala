package com.mcclellan.core

import com.mcclellan.core.model.Player
import com.mcclellan.core.model.Enemy
import com.badlogic.gdx.physics.box2d.Body
import com.mcclellan.core.model.GameComponent
import com.mcclellan.core.physics.WorldConnector
import com.mcclellan.core.model.Drawable
import com.mcclellan.core.model.Updateable
import com.mcclellan.core.model.DynamicBody

trait GameContainer {
	def player : Player
	def world : WorldConnector
	def addComponent(component : GameComponent)
	def removeComponent(component : GameComponent)
	def drawables : Seq[Drawable]
	def updateables : Seq[Updateable]
	def step(elapsed : Float)
}

object GameContainer {
	private class GameContainerImpl(val player : Player, val world : WorldConnector) extends GameContainer {
		var components : Set[GameComponent] = Set()
		var deads : Set[DynamicBody] = Set()
		
		override def addComponent(component : GameComponent) = components += component
		
		override def removeComponent(component : GameComponent) = {
			components -= component
			component match {
				case body : DynamicBody => deads += body
				case _ =>
			}
		}
		
		override def drawables : Seq[Drawable] = components.collect {
			case x : Drawable => x
		}.toSeq
		
		override def updateables : Seq[Updateable] = components.collect {
			case x : Updateable => x
		}.toSeq
		
		override def step(elapsed : Float) = {
			world.step(elapsed)
			player.update(elapsed)
			updateables.foreach(_.update(elapsed))
			
			deads.foreach(dead => {
				dead.body.setUserData(null)
				world.deleteBody(dead.body)
			})
			deads = Set()
		}
	}
	
	def create(player:Player, world : WorldConnector) : GameContainer = new GameContainerImpl(player, world)
}