package com.mcclellan.core

import com.mcclellan.core.model.Player

trait GameConnector {
	def player : Player
}

object GameConnector {
	private class GameConnectorImpl(val player : Player) extends GameConnector {
		
	}
	
	def forPlayer(player : Player) : GameConnector = new GameConnectorImpl(player)
}