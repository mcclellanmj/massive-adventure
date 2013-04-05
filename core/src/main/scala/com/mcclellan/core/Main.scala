package com.mcclellan.core

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.InputProcessor
import com.mcclellan.input.MappedInputProcessor
import com.mcclellan.input.UserInputListener
import com.mcclellan.input.Action
import com.mcclellan.input.actions._
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.mcclellan.core.model.Player
import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.graphics.g2d.Sprite

class Main(val processor : MappedInputProcessor) extends ApplicationListener with UserInputListener {
	import language.implicitConversions
	implicit def booleanToInt(b : Boolean) = if (b) 1 else -1

	var texture : Sprite = null
	var batch : SpriteBatch = null
	val player = new Player(new Vector2(0f, 0f))
	var direction = new Vector2(0f, 0f)
	var target = new Vector2(0f, 0f)
	var angle = 0f

	override def create = {
		texture = new Sprite(new Texture(Gdx.files.classpath("Man.png")))
		batch = new SpriteBatch
		Gdx.input.setInputProcessor(processor)
		processor.game = this;
	}

	private def update(elapsed : Float) = {
	  player.position += (direction.unit.toFloat * 600) * elapsed
	  val diff = target - player.position
	  angle = Math.toDegrees(Math.atan2(-diff.toDouble.x, diff.toDouble.y)).toFloat
	}
	
	override def render = {
		val font = new BitmapFont
		update(Gdx.graphics.getDeltaTime)
		Gdx.gl.glClearColor(0, 0, 0, 0)
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)
		
		batch.begin
		font.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond), 10, Gdx.graphics.getHeight - 20)
		texture.setPosition(player.position.x - texture.getWidth()/2, player.position.y - texture.getHeight()/2)
		texture.setRotation(angle)
		texture.draw(batch)
		batch.end
	}

	override def resize(width : Int, height : Int) = Unit
	override def pause = Unit
	override def resume = Unit
	override def dispose = Unit

	override def userAction(action : Action) = {
		action match {
			case Down(s) => direction += new Vector2(0,-1 * !s)
			case Up(s) => direction += new Vector2(0, 1 * !s)
			case Left(s) => direction += new Vector2(-1 * !s, 0)
			case Right(s) => direction += new Vector2(1 * !s,0)
			case AimAt(point) => target = new Vector2(point.x, Gdx.graphics.getHeight() - point.y)
		}
	}
}