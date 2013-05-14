package com.mcclellan.core.physics

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import com.mcclellan.core.model.Wall
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.BodyDef
import com.mcclellan.core.math.Vector2
import com.mcclellan.core.implicits.VectorImplicits._
import com.badlogic.gdx.utils.GdxNativesLoader
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Fixture
import com.mcclellan.core.model.Player
import com.mcclellan.core.model.MyBody

class TestFixture(val body : Body) extends Fixture(body, 1)

class TestBody extends Body(null, 1) {
	override def createFixture(fixtureDef : FixtureDef) = new TestFixture(this)
}

class FakeConnector extends WorldConnector {
	def createBody(body : BodyDef) = new TestBody
	def deleteBody(body : Body) = Unit
}

class Body1 extends MyBody {
	val body = new TestBody
}

class Body2 extends MyBody {
	val body = new TestBody
}

@RunWith(classOf[JUnitRunner])
class ContactResolverTest extends FunSpec {

	implicit val worldConnector = new FakeConnector
	describe("A Collision") {
		it("should match in same order") {
		}
		
		it("should match in different order") {
		}
	}
}
