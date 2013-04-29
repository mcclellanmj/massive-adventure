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
import com.mcclellan.core.physics.ContactResolver
import scala.collection.mutable.Queue
import com.badlogic.gdx.physics.box2d.Body
import com.mcclellan.core.model.DynamicBody
import scala.collection.JavaConversions._
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.physics.box2d.PolygonShape

class Main(val processor : MappedInputProcessor) extends ApplicationListener with UserInputListener {
	import language.implicitConversions
	// FIXME: Hacky, leads to hard to read code
	implicit def booleanToInt(b : Boolean) = if (b) 1 else -1

	// FIXME: These probably don't need to be here, only a few would
	val metersPerPixel = .01f
	lazy val personTexture : Sprite = {
		val texture = new Sprite(new Texture(Gdx.files.classpath("Man.png")))
		texture.setScale(metersPerPixel)
		texture
	}

	lazy val bulletTexture : Sprite = {
		val bulletTexture = new Sprite(new Texture(Gdx.files.classpath("Bullet.png")))
		bulletTexture.setScale(metersPerPixel)
		bulletTexture
	}

	lazy val batch : SpriteBatch = new SpriteBatch
	lazy val uiBatch : SpriteBatch = new SpriteBatch
	val world = new World(new GdxVector(0, 0), true)
	val player = new Player(new Vector2(2f, 2f), 0, world)
	val enemy = new Player(new Vector2(100f * metersPerPixel, 100f * metersPerPixel), 0, world)
	lazy val font = new BitmapFont
	var bullets = List[Projectile]()
	var direction = new Vector2(0f, 0f)
	var target = new Vector2(0f, 0f)
	var firing = false
	lazy val cam : Camera = new OrthographicCamera(Gdx.graphics.getWidth() * metersPerPixel, Gdx.graphics.getHeight() * metersPerPixel)
	lazy val removals : Queue[DynamicBody] = Queue()

	override def create = {
		Gdx.input.setInputProcessor(processor)
		processor.game = this

		world.setContactListener(new ContactResolver(removals))

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
			val newBullet = new Projectile(new Vector2[Float](player.position.x + (playerDirection.x * (15f * metersPerPixel)), player.position.y + (playerDirection.y * (15f * metersPerPixel))),
				playerDirection * 4, world)
			bullets = newBullet :: bullets
		}
		// TODO: Create an implicit for Vector -> GdxVector and vice versa
		val force = direction.unit.toFloat
		player.body.applyForceToCenter(new GdxVector(force.x, force.y), true)
		val diff = target - new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f)
		player.rotation = Math.toDegrees(Math.atan2(-diff.toDouble.x, diff.toDouble.y)).toFloat

		world.step(elapsed, 6, 2)
	}

	override def render = {
		update(Gdx.graphics.getDeltaTime)
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

		batch.begin
		personTexture.setPosition(player.position.x - (personTexture.getWidth() / 2f), player.position.y - (personTexture.getHeight() / 2f))
		cam.position.set(player.position.x, player.position.y, 0)

		bullets.foreach(bullet0 => {
			bulletTexture.setPosition(bullet0.position.x - (bulletTexture.getWidth() / 2f), bullet0.position.y - (bulletTexture.getHeight() / 2f))
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

		val shapes = new ShapeRenderer
		shapes.setProjectionMatrix(cam.combined)
		shapes.begin(ShapeType.Line)
		shapes.setColor(1, 1, 1, 1)
		
		world.getBodies().foreach(body => {
			body.getFixtureList().foreach(fix => {
				fix.getShape() match {
					case x : CircleShape => {
						if(!body.isAwake()) shapes.setColor(1, 0, 0, 1) else shapes.setColor(1,1,0,1)
						shapes.circle(body.getPosition.x + x.getPosition.x, body.getPosition.y + x.getPosition.y, x.getRadius(), 8)
					}
					case edge : EdgeShape => {
						if(!body.isAwake()) shapes.setColor(1, 0, 0, 1) else shapes.setColor(1,1,0,1)
						val point1 = new GdxVector
						val point2 = new GdxVector
						edge.getVertex1(point1)
						edge.getVertex2(point2)
						shapes.line(point1.x, point1.y, point2.x, point2.y)
					}
				}
			})
		})
		shapes.end()
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