package com.mcclellan.core.graphics.texture

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.Gdx

class SpriteLoader {
	var loaded : Map[String, Sprite] = Map()
	var map : Map[Class[_], Sprite] = Map()
	def addTexture(to : Class[_], path : String) = {
		loaded.get(path) match {
			case Some(texture) => map += to -> texture
			case None => {
				val sprite = new Sprite(new Texture(Gdx.files.classpath(path)))
				sprite.setBounds(0, 0, sprite.getWidth() * .01f, sprite.getHeight() * .01f)
				sprite.setOrigin((sprite.getWidth() / 2), (sprite.getHeight() / 2))
				loaded += path -> sprite
				map += to -> sprite
			}
		}
	}

	def textureFor(to : Class[_]) = {
		map.get(to)
	}
}
