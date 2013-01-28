package com.mcclellan.models
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

object VectorSpecification extends Properties("Vector2d") {
	val vectorGen : Gen[Vector2d] = for {
		x <- Gen.choose(-1, 10.0)
		y <- Gen.choose(-1, 10.0)
	} yield(new Vector2d(x, y))
	implicit val arbVectorGen : Arbitrary[Vector2d] = Arbitrary(vectorGen)

	property("add") = forAll((a: Double, b: Double) => {
		val vector1 = new Vector2d(a, a)
		val vector2 = new Vector2d(b, b)
		val sum = vector1 + vector2
		(sum.x == (vector1.x + vector2.x)) && (sum.y == (vector1.y + vector2.y))
	})

	property("subtract") = forAll((a: Double, b: Double) => {
		val vector1 = new Vector2d(a, a)
		val vector2 = new Vector2d(b, b)
		val sum = vector1 - vector2
		(sum.x == (vector1.x - vector2.x)) && (sum.y == (vector1.y - vector2.y))
	})

	property("scale") = forAll((testVector: Vector2d, scale: Double) => {
		val scaled = testVector * scale
		scaled.x == (testVector.x * scale) && scaled.y == (testVector.y * scale)
	}) 

	property("negate") = forAll((testVector: Vector2d) => {
		val negated = -testVector
		negated.x == -testVector.x && negated.y == -testVector.y && -negated == testVector
	})

	property("perpendicular") = forAll((testVector: Vector2d) => {
		val perp = testVector.perpendicular
		perp.x == -testVector.y && perp.y == testVector.x && perp.perpendicular.perpendicular.perpendicular == testVector
	})
	
	property("unit") = forAll((testVector : Vector2d) => {
	  testVector.unit
	  println(testVector)
	  println(testVector.magnitude)
	  println(testVector.unit.magnitude)
	  println(testVector.unit)
	  testVector match {
	    case Vector2d.zero => 0.0 == testVector.unit.magnitude
	    case _ => 1.0 - testVector.unit.magnitude < 0.000001
	  }
	  
	})
}