package com.mcclellan.core.implicits

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.math.{Vector2 => GdxVector}
import language.implicitConversions

object VectorImplicits {
	implicit def toGdxVector[T](vec : Vector2[T]) = new GdxVector(vec.toFloat.x, vec.toFloat.y)
	implicit def toVector(vec : GdxVector) = new Vector2(vec.x, vec.y)
}