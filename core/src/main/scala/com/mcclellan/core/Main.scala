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
import com.mcclellan.core.model.Projectile
import com.mcclellan.core.model.Projectile
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.{Vector2 => GdxVector}
import com.badlogic.gdx.physics.box2d.BodyDef

class Main(val processor : MappedInputProcessor) extends ApplicationListener with UserInputListener {
	import language.implicitConversions
	implicit def booleanToInt(b : Boolean) = if (b) 1 else -1

	var texture : Sprite = null
	var bullet : Sprite = null
	var batch : SpriteBatch = null
	val world = new World(new GdxVector(0, 0), true)
	val player = new Player(new Vector2(0f, 0f), 0, world)
	val enemy = new Player(new Vector2(100f, 100f), 0, world)
	var bullets = List(new Projectile(new Vector2(200, 0), new Vector2(120, 90)))
	var direction = new Vector2(0f, 0f)
	var target = new Vector2(0f, 0f)
	var firing = false

	override def create = {
		texture = new Sprite(new Texture(Gdx.files.classpath("Man.png")))
		bullet = new Sprite(new Texture(Gdx.files.classpath("Bullet.png")))
		batch = new SpriteBatch
		Gdx.input.setInputProcessor(processor)
		processor.game = this;
	}

	private def update(elapsed : Float) = {
		if(firing) {
				bullets = new Projectile(new Vector2[Float](player.position.x, player.position.y), 
						(new Vector2[Float](Math.sin(Math.toRadians(-player.rotation)).toFloat,Math.cos(Math.toRadians(player.rotation)).toFloat)) * 500) :: bullets
		}
		player.velocity = (direction.unit.toFloat * 600)
		bullets.foreach(_.update(elapsed))
		val diff = target - player.position
		player.rotation = Math.toDegrees(Math.atan2(-diff.toDouble.x, diff.toDouble.y)).toFloat
	}

	override def render = {
		val font = new BitmapFont
		update(Gdx.graphics.getDeltaTime)
		Gdx.gl.glClearColor(0, 0, 0, 0)
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

		batch.begin
		font.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond), 10, Gdx.graphics.getHeight - 20)
		texture.setPosition(player.position.x - texture.getWidth() / 2, player.position.y - texture.getHeight() / 2)
		
		bullets.foreach(bullet0 => {
			bullet.setPosition(bullet0.position.x, bullet0.position.y)
			bullet.setRotation(-bullet0.rotation)
			bullet.draw(batch)
		})
		texture.setRotation(player.rotation)
		texture.draw(batch)
		batch.end
	}

	override def resize(width : Int, height : Int) = Unit
	override def pause = Unit
	override def resume = Unit
	override def dispose = Unit

	override def userAction(action : Action) = {
		action match {
			case Down(s) => direction += new Vector2(0, -1 * !s)
			case Up(s) => direction += new Vector2(0, 1 * !s)
			case Left(s) => direction += new Vector2(-1 * !s, 0)
			case Right(s) => direction += new Vector2(1 * !s, 0)
			case AimAt(point) => target = new Vector2(point.x, Gdx.graphics.getHeight() - point.y)
			case Fire(fired) => firing = fired
		}
	}
}