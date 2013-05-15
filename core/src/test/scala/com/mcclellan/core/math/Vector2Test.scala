package com.mcclellan.core.math

import org.scalatest.FunSuite
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class Vector2Test extends FunSpec {
	describe("A Vector2") {
		it("should be addable") {
			val vec1 = new Vector2(1, 1)
			val vec2 = new Vector2(1, 1)
			val vec3 = vec1 + vec2
			assert(vec3.x === 2)
			assert(vec3.y === 2)
		}

		it("should handle addition with negatives") {
			val vec1 = new Vector2(-1, -1)
			val vec2 = new Vector2(-1, -1)
			val vec3 = vec1 + vec2
			assert(vec3.x === -2)
			assert(vec3.y === -2)
		}

		it("should negate") {
			val neg = -(new Vector2(1, 1))
			assert(neg.x === -1)
			assert(neg.y === -1)
		}

		it("should be subtractable") {
			val vec1 = new Vector2(1, 1)
			val vec2 = new Vector2(1, 1)
			val vec3 = vec1 - vec2
			assert(vec3.x === 0)
			assert(vec3.y === 0)
		}

		it("should scale") {
			val vec = new Vector2(3, 2) * 2
			assert(vec.x === 6)
			assert(vec.y === 4)
		}

		it("should have a magnitude") {
			val vec = new Vector2(3, 4)
			assert(vec.magnitude === 5)
		}

		it("should have an angle") {
			val vec = new Vector2(20, 20)
			assert(135 === vec.angle.degrees)
		}

		it("should have an angle negated") {
			val vec = new Vector2(-20, 20)
			assert(-135 === vec.angle.degrees)
		}

		it("should have a unit vector with a 1 magnitude") {
			val vec = new Vector2(3, 4)
			val unit = vec.unit
			assert(1 === unit.magnitude)
		}

		it("should compute dot product") {
			val vec1 = new Vector2(2, 4)
			val vec2 = new Vector2(1, 5)
			val dot = vec1*vec2
			assert(dot === 22)
		}
		
		it("should compute return 0 for orthogonal") {
			val vec1 = new Vector2(35, 14000)
			val vec2 = vec1.unit
			val dot = vec1*vec2
			val preAngle = dot / (vec1.magnitude * vec2.magnitude)
			val angle = Math.acos(if(preAngle > 1) 1 else preAngle)
			assert(Math.toDegrees(angle) === 0)
		}
		
		it("should be equal") {
			val vec1 = new Vector2(0, 0)
			val vec2 = new Vector2(0.0f, 0.0f)
			assert(vec1 === vec2)
		}
	}
}