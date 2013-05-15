package com.mcclellan.core.implicits

import com.mcclellan.core.math.Vector2
import com.badlogic.gdx.math.{Vector2 => GdxVector}
import language.implicitConversions

object VectorImplicits {
	implicit def toGdxVector(vec : Vector2) = new GdxVector(vec.x, vec.y)
	implicit def toVector(vec : GdxVector) = new Vector2(vec.x, vec.y)
}