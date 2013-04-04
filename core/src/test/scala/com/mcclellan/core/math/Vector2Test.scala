package com.mcclellan.core.math

import org.scalatest.FunSuite
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class Vector2Test extends FunSpec {
	describe("A Vector2") {
		it("should be addable") {
			val vec1 = new Vector2[Int](1,1)
			val vec2 = new Vector2[Int](1,1)
			val vec3 = vec1 + vec2
			assert(vec3.x === 2)
			assert(vec3.y === 2)
		}
		
		it("should handle addition with negatives") {
			val vec1 = new Vector2[Int](-1, -1)
			val vec2 = new Vector2[Int](-1, -1)
			val vec3 = vec1 + vec2
			assert(vec3.x === -2)
			assert(vec3.y === -2)
		}
		
		it("should negate") {
			val neg = -(new Vector2[Int](1,1))
			assert(neg.x === -1)
			assert(neg.y === -1)
		}
		
		it("should be subtractable") {
			val vec1 = new Vector2[Int](1,1)
			val vec2 = new Vector2[Int](1,1)
			val vec3 = vec1 - vec2
			assert(vec3.x === 0)
			assert(vec3.y === 0)
		}
		
		it("should scale") {
			val vec = new Vector2[Int](3, 2) * 2
			assert(vec.x === 6)
			assert(vec.y === 4)
		}
		
		it("should have a magnitude") {
			val vec = new Vector2[Int](3,4)
			assert(vec.magnitude === 5)
		}
		
		it("should have an angle") {
			val vec = new Vector2[Int](20, 20)
			assert(45 === Math.toDegrees(vec.angle))
		}
		
		it("should have an angle negated") {
			val vec = new Vector2[Int](-20, 20)
			assert(-45 === Math.toDegrees(vec.angle))
		}
	}
}