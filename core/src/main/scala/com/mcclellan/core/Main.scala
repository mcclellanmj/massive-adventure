package com.mcclellan.core

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL10
import com.mcclellan.input.MappedInputProcessor
import com.mcclellan.input.UserInputListener
import com.mcclellan.input.Action
import com.mcclellan.input.actions._
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.mcclellan.core.model.Player
import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.graphics.g2d.Sprite
import com.mcclellan.core.model.Projectile
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.math.{ Vector2 => GdxVector }
import com.mcclellan.core.physics.ContactResolver
import scala.collection.mutable.{ Set => MutableSet }
import com.mcclellan.core.model.DynamicBody
import com.mcclellan.core.graphics.camera.ScaledOrthographicCamera
import scala.collection.JavaConversions._
import com.mcclellan.core.implicits.VectorImplicits._
import com.mcclellan.core.implicits.GdxPimps._
import com.mcclellan.core.graphics.camera.ScaledOrthographicCamera
import com.mcclellan.core.model.Wall
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.mcclellan.core.debug.Box2dRenderer

class Main(val processor : MappedInputProcessor) extends ApplicationListener with UserInputListener {
	import language.implicitConversions
	// FIXME: Hacky, leads to hard to read code
	implicit def booleanToInt(b : Boolean) = if (b) 1 else -1

	// FIXME: These probably don't need to be here, only a few would
	val metersPerPixel = .01f
	lazy val personTexture : Sprite = {
		val texture = new Sprite(new Texture(Gdx.files.classpath("Man.png")))
		texture.setBounds(0, 0, 32 * metersPerPixel, 32 * metersPerPixel)
		texture.setOrigin((texture.getWidth() / 2), (texture.getHeight() / 2))
		texture
	}

	lazy val bulletTexture : Sprite = {
		val bulletTexture = new Sprite(new Texture(Gdx.files.classpath("Bullet.png")))
		bulletTexture.setBounds(0, 0, 1 * metersPerPixel, 6 * metersPerPixel)
		bulletTexture.setOrigin(bulletTexture.getWidth() / 2f, bulletTexture.getHeight() / 2f)
		bulletTexture
	}

	lazy val batch : SpriteBatch = new SpriteBatch
	lazy val uiBatch : SpriteBatch = new SpriteBatch
	val world = new World(new GdxVector(0, 0), true)
	val player = new Player(new Vector2(2f, 2f), 0, world)
	val enemy = new Player(new Vector2(1f, 1f), 0, world)
	lazy val font = new BitmapFont
	var bullets = Set[Projectile]()
	var direction = new Vector2(0f, 0f)
	var target = new Vector2(0f, 0f)
	var firing = false
	lazy val cam = new ScaledOrthographicCamera(metersPerPixel, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
	lazy val removals : MutableSet[DynamicBody] = MutableSet()

	override def create = {
		Gdx.input.setInputProcessor(processor)
		processor.game = this

		world.setContactListener(new ContactResolver(removals))
		val screenHeight = Gdx.graphics.getHeight() * metersPerPixel
		val screenWidth = Gdx.graphics.getWidth() * metersPerPixel
		world.setWarmStarting(true)

		new Wall(new Vector2(0, 0), new Vector2(0, screenHeight))(world)
		new Wall(new Vector2(0, 0), new Vector2(screenWidth, 0))(world)
		new Wall(new Vector2(screenWidth, 0), new Vector2(screenWidth, screenHeight))(world)
		new Wall(new Vector2(0, screenHeight), new Vector2(screenWidth, screenHeight))(world)

	}

	private def update(elapsed : Float) = {
		if (firing) {
			val playerDirection = new Vector2[Float](Math.sin(Math.toRadians(-player.rotation)).toFloat, Math.cos(Math.toRadians(player.rotation)).toFloat)
			val newBullet = new Projectile(new Vector2[Float](player.position.x + (playerDirection.x * (15f * metersPerPixel)), player.position.y + (playerDirection.y * (15f * metersPerPixel))),
				playerDirection * 7, world)
			bullets = bullets + newBullet
		}

		val force = direction.unit.toFloat * .4f
		player.body.applyForceToCenter(force.rotate(Math.toRadians(player.rotation).toFloat), true)
		player.body.setLinearVelocity(player.body.getLinearVelocity().limit(3f))

		val diff = target - new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f)
		player.rotation = Math.toDegrees(Math.atan2(-diff.toDouble.x, diff.toDouble.y)).toFloat

		world.step(elapsed, 4, 1)
		cam.position = player.position

		removals.foreach {
			case x : Projectile => {
				world.destroyBody(x.body)
				bullets = bullets - x
				removals.remove(x)
			}
		}

	}

	override def render = {
		update(Gdx.graphics.getRawDeltaTime)
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

		batch.setProjectionMatrix(cam.projectionMatrix)
		batch.begin {
			bullets.foreach(bullet0 => {
				bulletTexture.setRotation(-bullet0.rotation)
				bulletTexture.setPosition(bullet0.position.x, bullet0.position.y)
				bulletTexture.draw(batch)
			})
			personTexture.setPosition(player.position.x, player.position.y)
			personTexture.setRotation(player.rotation)
			personTexture.draw(batch)
		
			personTexture.setPosition(enemy.position.x, enemy.position.y)
			personTexture.setRotation(enemy.rotation)
			personTexture.draw(batch)
		}

		// TODO: Create UI object
		uiBatch.begin {
			font.draw(uiBatch, String.valueOf(Gdx.graphics.getFramesPerSecond), 10, (Gdx.graphics.getHeight - 20))
			font.draw(uiBatch, "Bodies: " + world.getBodyCount(), 10, Gdx.graphics.getHeight() - 40)
			font.draw(uiBatch, "Collisions: " + world.getContactCount(), 10, Gdx.graphics.getHeight() - 60)
		}

		Box2dRenderer.renderWorld(world, cam)
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