package com.mcclellan.models
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

object VectorSpecification extends Properties("Vector2f") {
	val vectorGen : Gen[Vector2f] = for {
		x <- Gen.choose(-1, 10.0f)
		y <- Gen.choose(-1, 10.0f)
	} yield(new Vector2f(x, y))
	implicit val arbVectorGen : Arbitrary[Vector2f] = Arbitrary(vectorGen)

	property("add") = forAll((a: Float, b: Float) => {
		val vector1 = new Vector2f(a, a)
		val vector2 = new Vector2f(b, b)
		val sum = vector1 + vector2
		(sum.x == (vector1.x + vector2.x)) && (sum.y == (vector1.y + vector2.y))
	})

	property("subtract") = forAll((a: Float, b: Float) => {
		val vector1 = new Vector2f(a, a)
		val vector2 = new Vector2f(b, b)
		val sum = vector1 - vector2
		(sum.x == (vector1.x - vector2.x)) && (sum.y == (vector1.y - vector2.y))
	})

	property("scale") = forAll((testVector: Vector2f, scale: Float) => {
		val scaled = testVector * scale
		scaled.x == (testVector.x * scale) && scaled.y == (testVector.y * scale)
	}) 

	property("negate") = forAll((testVector: Vector2f) => {
		val negated = -testVector
		negated.x == -testVector.x && negated.y == -testVector.y && -negated == testVector
	})

	property("perpendicular") = forAll((testVector: Vector2f) => {
		val perp = testVector.perpendicular
		perp.x == -testVector.y && perp.y == testVector.x && perp.perpendicular.perpendicular.perpendicular == testVector
	})
	
	property("unit") = forAll((testVector : Vector2f) => {
	  testVector.unit
	  println(testVector)
	  println(testVector.magnitude)
	  println(testVector.unit.magnitude)
	  println(testVector.unit)
	  testVector match {
	    case Vector2f.zero => 0.0 == testVector.unit.magnitude
	    case _ => 1.0 - testVector.unit.magnitude < 0.000001
	  }
	  
	})
}