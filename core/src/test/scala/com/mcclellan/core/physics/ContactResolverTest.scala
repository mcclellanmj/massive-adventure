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

@RunWith(classOf[JUnitRunner])
class ContactResolverTest extends FunSpec {

	describe("A Collision") {
		it("should match in same order") {
		}
		
		it("should match in different order") {
		}
	}
}
