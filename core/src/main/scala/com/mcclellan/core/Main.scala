package com.mcclellan.core

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.mcclellan.core.debug.Box2dRenderer
import com.mcclellan.core.graphics.camera.ScaledOrthographicCamera
import com.mcclellan.core.graphics.texture.SpriteLoader
import com.mcclellan.core.implicits.GdxPimps.toPimpedSpriteBatch
import com.mcclellan.core.implicits.VectorImplicits.toGdxVector
import com.mcclellan.core.math._
import com.mcclellan.core.model._
import com.mcclellan.core.physics.ContactResolver
import com.mcclellan.core.physics.Handlers
import com.mcclellan.core.physics.WorldConnectorImpl
import com.mcclellan.input.Action
import com.mcclellan.input.MappedInputProcessor
import com.mcclellan.input.UserInputListener
import com.mcclellan.input.actions._
import com.mcclellan.core.debug.ui.DebugUi

class Main(val processor : MappedInputProcessor) extends ApplicationListener with UserInputListener {
	// FIXME: These probably don't need to be here, only a few would
	val metersPerPixel = .01f
	lazy val batch : SpriteBatch = new SpriteBatch
	lazy val uiBatch : SpriteBatch = new SpriteBatch
	lazy val spriteLoader : SpriteLoader = new SpriteLoader
	lazy val debugUi = new DebugUi
	val fullWorld = new World(Vector2.zero, true)
	implicit val world = new WorldConnectorImpl(fullWorld)
	val player : Player = new Player(Vector2(2f, 2f), Degrees(0))

	implicit val game = GameContainer.create(player, world)
	lazy val font = new BitmapFont
	lazy val cam = new ScaledOrthographicCamera(metersPerPixel, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())

	override def create = {
		Gdx.input.setInputProcessor(processor)
		processor.game = this

		fullWorld.setContactListener(new ContactResolver(game, Handlers.allHandlers))
		val screenHeight = Gdx.graphics.getHeight() * metersPerPixel
		val screenWidth = Gdx.graphics.getWidth() * metersPerPixel

		player.primary = Some(new Shotgun)
		player.secondary = Some(new AssaultRifle)

		new Wall(Vector2.zero, Vector2(0, 12))
		new Wall(Vector2.zero, Vector2(12, 0))
		new Wall(Vector2(12, 0), Vector2(12, 12))
		new Wall(Vector2(0, 12), Vector2(12, 12))

		(1 to 50).map(x => new Enemy(Vector2((Math.random() * 12).toFloat, (Math.random() * 12).toFloat))).foreach(game.addComponent(_))
		spriteLoader.addTexture(classOf[Enemy], "Man.png")
		spriteLoader.addTexture(classOf[Player], "Man.png")
		spriteLoader.addTexture(classOf[Projectile], "Bullet.png")
	}

	private def update(elapsed : Float) = {
		game.step(elapsed)
		cam.position = player.position
	}

	override def render = {
		update(Gdx.graphics.getRawDeltaTime)
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

		batch.setProjectionMatrix(cam.projectionMatrix)
		batch.begin {
			// TODO: Fix magic number texture offset
			val personTexture = spriteLoader.textureFor(player.getClass()).get
			personTexture.setPosition(player.position.x - .16f, player.position.y - .16f)
			personTexture.setRotation(player.rotation.degrees)
			personTexture.draw(batch)

			game.drawables.foreach(x => spriteLoader.textureFor(x.getClass()) match {
				case Some(sprite : Sprite) => {
					sprite.setPosition(x.position.x - (sprite.getWidth() * sprite.getScaleX()) / 2f, x.position.y - (sprite.getWidth() * sprite.getScaleX()) / 2f)
					sprite.setRotation(x.rotation.degrees - 90)
					sprite.draw(batch)
				}
				case None =>
			})
		}
		

		Box2dRenderer.renderWorld(fullWorld, cam)
		debugUi.draw(Gdx.graphics.getRawDeltaTime(), game)
	}

	override def resize(width : Int, height : Int) = Unit
	override def pause = Unit
	override def resume = Unit
	override def dispose = Unit

	override def userAction(action : Action) = {
		import language.implicitConversions
		// FIXME: Hacky, leads to hard to read code
		implicit def booleanToInt(b : Boolean) = if (b) 1 else -1
		action match {
			case Down(s) => player.thrust += Vector2(0, -1 * !s)
			case Up(s) => player.thrust += Vector2(0, 1 * !s)
			case Left(s) => player.thrust += Vector2(-1 * !s, 0)
			case Right(s) => player.thrust += Vector2(1 * !s, 0)
			case AimAt(point) => player.target = Vector2(point.x, Gdx.graphics.getHeight() - point.y)
			case Fire(fired) => if (fired) player.arm(player.primary) else player.disarm
			case SecondaryFire(fired) => if (fired) player.arm(player.secondary) else player.disarm
		}
	}
}