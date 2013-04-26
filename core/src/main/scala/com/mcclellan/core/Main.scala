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
import com.badlogic.gdx.math.{ Vector2 => GdxVector }
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer2
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.math.Vector3

class Main(val processor : MappedInputProcessor) extends ApplicationListener with UserInputListener {
	import language.implicitConversions
	// FIXME: Hacky, leads to hard to read code
	implicit def booleanToInt(b : Boolean) = if (b) 1 else -1

	// FIXME: These probably don't need to be here, only a few would
	val metersPerPixel = .01f
	var personTexture : Sprite = null
	var bulletTexture : Sprite = null
	var batch : SpriteBatch = null
	var uiBatch : SpriteBatch = null
	val world = new World(new GdxVector(0, 0), true)
	val player = new Player(new Vector2(2f, 2f), 0, world)
	val enemy = new Player(new Vector2(100f * metersPerPixel, 100f * metersPerPixel), 0, world)
	var bullets = List[Projectile]()
	var direction = new Vector2(0f, 0f)
	var target = new Vector2(0f, 0f)
	var firing = false
	var cam : Camera = null

	override def create = {
		personTexture = new Sprite(new Texture(Gdx.files.classpath("Man.png")))
		personTexture.setScale(metersPerPixel)
		bulletTexture = new Sprite(new Texture(Gdx.files.classpath("Bullet.png")))
		bulletTexture.setScale(metersPerPixel)
		batch = new SpriteBatch
		uiBatch = new SpriteBatch
		Gdx.input.setInputProcessor(processor)
		processor.game = this;
		cam = new OrthographicCamera(Gdx.graphics.getWidth() * metersPerPixel, Gdx.graphics.getHeight() * metersPerPixel)
		
		// TODO: Create wall object
		val wallDef = new BodyDef
		wallDef.`type` = BodyDef.BodyType.StaticBody
		val wall = world.createBody(wallDef)
		
		val fixtureDef = new FixtureDef
		val shape = new EdgeShape
		shape.set(new GdxVector(0, 0), new GdxVector(0, Gdx.graphics.getHeight() * metersPerPixel))
		fixtureDef.shape = shape
		fixtureDef.density = 100
		wall.createFixture(fixtureDef)
		
		val wallDef2 = new BodyDef
		wallDef2.`type` = BodyDef.BodyType.StaticBody
		val wall2 = world.createBody(wallDef2)
		
		val fixtureDef2 = new FixtureDef
		val shape2 = new EdgeShape
		shape2.set(new GdxVector(0, 0), new GdxVector(Gdx.graphics.getWidth() * metersPerPixel, 0))
		fixtureDef2.shape = shape2
		fixtureDef2.density = 100
		wall2.createFixture(fixtureDef2)
	}

	private def update(elapsed : Float) = {
		if (firing) {
			val playerDirection = new Vector2[Float](Math.sin(Math.toRadians(-player.rotation)).toFloat, Math.cos(Math.toRadians(player.rotation)).toFloat)
			val newBullet = new Projectile(new Vector2[Float](player.position.x + (playerDirection.x * (2f * metersPerPixel)), player.position.y + (playerDirection.y * (2f * metersPerPixel))),
				playerDirection * 8, world)
			bullets = newBullet :: bullets
		}
		val force = direction.unit.toFloat
		player.body.applyForceToCenter(new GdxVector(force.x, force.y), true)
		val diff = target - new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f)
		player.rotation = Math.toDegrees(Math.atan2(-diff.toDouble.x, diff.toDouble.y)).toFloat
		world.step(elapsed, 6, 2)
	}

	override def render = {
		val font = new BitmapFont
		update(Gdx.graphics.getDeltaTime)
		Gdx.gl.glClearColor(0, 0, 0, 0)
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

		batch.begin
		personTexture.setPosition(player.position.x - (personTexture.getWidth() / 2f), player.position.y - (personTexture.getHeight() / 2f))
		cam.position.set(player.position.x, player.position.y, 0)

		bullets.foreach(bullet0 => {
			bulletTexture.setPosition(bullet0.position.x- (bulletTexture.getWidth() / 2f), bullet0.position.y- (bulletTexture.getHeight() / 2f))
			bulletTexture.setRotation(-bullet0.rotation)
			bulletTexture.draw(batch)
		})
		personTexture.setRotation(player.rotation)
		personTexture.draw(batch)

		personTexture.setPosition(enemy.position.x - (personTexture.getWidth() / 2f), enemy.position.y - (personTexture.getHeight() / 2f))
		personTexture.setRotation(enemy.rotation)
		personTexture.draw(batch)
		batch.end

		cam.update()
		batch.setProjectionMatrix(cam.combined)

		// TODO: Create UI object
		uiBatch.begin()
		font.draw(uiBatch, String.valueOf(Gdx.graphics.getFramesPerSecond), 10, (Gdx.graphics.getHeight - 20))
		font.draw(uiBatch, "Bodies: " + world.getBodyCount(), 10, Gdx.graphics.getHeight() - 40)
		font.draw(uiBatch, "Collisions: " + world.getContactCount(), 10, Gdx.graphics.getHeight() - 60)
		uiBatch.end();
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