package com.mcclellan.core

import scala.collection.mutable.{ Set => MutableSet }
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.mcclellan.core.debug.Box2dRenderer
import com.mcclellan.core.graphics.camera.ScaledOrthographicCamera
import com.mcclellan.core.implicits.GdxPimps.toPimpedSpriteBatch
import com.mcclellan.core.implicits.VectorImplicits.toGdxVector
import com.mcclellan.core.math.Degrees
import com.mcclellan.core.math.Radians
import com.mcclellan.core.math.Vector2
import com.mcclellan.core.model.DynamicBody
import com.mcclellan.core.model.Player
import com.mcclellan.core.model.Projectile
import com.mcclellan.core.model.Shotgun
import com.mcclellan.core.model.Wall
import com.mcclellan.core.physics.ContactResolver
import com.mcclellan.core.physics.Handlers
import com.mcclellan.core.physics.WorldConnectorImpl
import com.mcclellan.input.Action
import com.mcclellan.input.MappedInputProcessor
import com.mcclellan.input.UserInputListener
import com.mcclellan.input.actions.AimAt
import com.mcclellan.input.actions.Down
import com.mcclellan.input.actions.Fire
import com.mcclellan.input.actions.Left
import com.mcclellan.input.actions.Right
import com.mcclellan.input.actions.Up
import com.mcclellan.core.model.AssaultRifle
import com.mcclellan.input.actions.SecondaryFire
import com.mcclellan.core.model.Enemy
import com.mcclellan.core.model.Enemy

class SpriteLoader() {
	var loaded: Map[String, Sprite] = Map()
	var map : Map[Class[_], Sprite] = Map()
	def addTexture(to : Class[_], path : String) = {
		loaded.get(path) match {
			case Some(texture) => map += to -> texture
			case None => {
				val sprite = new Sprite(new Texture(Gdx.files.classpath(path)))
				sprite.setBounds(0, 0, sprite.getWidth() * .01f, sprite.getHeight() * .01f)
				sprite.setOrigin((sprite.getWidth() / 2), (sprite.getHeight() / 2))
				loaded += path -> sprite
				map += to -> sprite
			}
		}
	}
	
	def textureFor(to : Class[_]) = {
		map.get(to)
	}
}

class Main(val processor : MappedInputProcessor) extends ApplicationListener with UserInputListener {
	// FIXME: These probably don't need to be here, only a few would
	val metersPerPixel = .01f
	lazy val batch : SpriteBatch = new SpriteBatch
	lazy val uiBatch : SpriteBatch = new SpriteBatch
	lazy val spriteLoader : SpriteLoader = new SpriteLoader
	val fullWorld = new World(Vector2.zero, true)
	implicit val world = new WorldConnectorImpl(fullWorld)
	val player : Player = new Player(Vector2(2f, 2f), Degrees(0))

	implicit val game = GameContainer.create(player, world)
	lazy val font = new BitmapFont
	var bullets = Set[Projectile]()
	var direction = Vector2.zero
	var target = Vector2.zero
	lazy val cam = new ScaledOrthographicCamera(metersPerPixel, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
	val shotgun = new Shotgun
	val assaultRifle = new AssaultRifle

	override def create = {
		Gdx.input.setInputProcessor(processor)
		processor.game = this

		fullWorld.setContactListener(new ContactResolver(game, Handlers.allHandlers))
		val screenHeight = Gdx.graphics.getHeight() * metersPerPixel
		val screenWidth = Gdx.graphics.getWidth() * metersPerPixel
		
		player.primary = Some(new Shotgun)
		player.secondary = Some(new AssaultRifle)

		new Wall(Vector2.zero, Vector2(0, screenHeight))
		new Wall(Vector2.zero, Vector2(screenWidth, 0))
		new Wall(Vector2(screenWidth, 0), Vector2(screenWidth, screenHeight))
		new Wall(Vector2(0, screenHeight), Vector2(screenWidth, screenHeight))
		
		(1 to 10).map(x => new Enemy(Vector2(1f, x / 2f))).foreach(game.addComponent(_))
		spriteLoader.addTexture(classOf[Enemy], "Man.png")
		spriteLoader.addTexture(classOf[Player], "Man.png")
		spriteLoader.addTexture(classOf[Projectile], "Bullet.png")
	}

	private def update(elapsed : Float) = {
		game.step(elapsed)
		// enemies.foreach(_.update(elapsed))

		// TODO: Needs to be in the player class
		// assaultRifle.update(elapsed, firing)
		// shotgun.update(elapsed, secondaryFiring)

		val force = direction.unit * .4f
		player.body.applyForceToCenter(force.rotate(player.rotation), true)
		player.body.setLinearVelocity(player.body.getLinearVelocity().limit(3f))

		val diff = target - Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f)
		player.rotation = Radians(Math.atan2(-diff.x, diff.y).toFloat)

		fullWorld.step(elapsed, 2, 1)
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
					sprite.setPosition(x.position.x - .16f, x.position.y - .16f)
					sprite.setRotation(x.rotation.degrees - 90)
					sprite.draw(batch)
				}
				case None =>
			})
		}

			/*
			enemies.foreach(enemy => {
				personTexture.setPosition(enemy.position.x - .16f, enemy.position.y - .16f)
				personTexture.setRotation(enemy.rotation.degrees - 90)
				personTexture.draw(batch)
			})
			* 
			*/

		// TODO: Create UI object
		uiBatch.begin {
			font.draw(uiBatch, String.valueOf(Gdx.graphics.getFramesPerSecond), 10, (Gdx.graphics.getHeight - 20))
			font.draw(uiBatch, "Bodies: " + fullWorld.getBodyCount(), 10, Gdx.graphics.getHeight() - 40)
			font.draw(uiBatch, "Collisions: " + fullWorld.getContactCount(), 10, Gdx.graphics.getHeight() - 60)
			font.draw(uiBatch, "Player: " + player.health, 10, Gdx.graphics.getHeight() - 100)
		}

		Box2dRenderer.renderWorld(fullWorld, cam)
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
			case Down(s) => direction += Vector2(0, -1 * !s)
			case Up(s) => direction += Vector2(0, 1 * !s)
			case Left(s) => direction += Vector2(-1 * !s, 0)
			case Right(s) => direction += Vector2(1 * !s, 0)
			case AimAt(point) => target = Vector2(point.x, Gdx.graphics.getHeight() - point.y)
			case Fire(fired) => if(fired) player.arm(player.primary) else player.disarm
			case SecondaryFire(fired) => if(fired) player.arm(player.secondary) else player.disarm
		}
	}
}