package com.mcclellan.core

import com.mcclellan.core.model.Player
import com.mcclellan.core.model.Enemy
import com.badlogic.gdx.physics.box2d.Body

trait GameState {
	def player : Player
}

object GameState {
	private class GameStateImpl(val player : Player) extends GameState {
	}
	
	def forPlayer(player : Player) : GameState = new GameStateImpl(player)
}