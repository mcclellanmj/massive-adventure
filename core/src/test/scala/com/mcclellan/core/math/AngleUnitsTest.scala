package com.mcclellan.core.math

import org.scalatest.FunSuite
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AngleUnitsTest extends FunSpec {
	describe("An AngleUnit") {
		it("should convert from radians to degrees") {
			val angle = Radians(1)
			assert(angle.radians === 1)
			assert(angle.degrees === 57.2957795f)
		}
		
		it("should convert from degrees to radians") {
			val angle = Degrees(90)
			assert(angle.degrees === 90)
			assert(angle.radians === 1.57079633f)
		}
	}
}