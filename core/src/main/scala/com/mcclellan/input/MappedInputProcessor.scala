package com.mcclellan.input

import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.InputProcessor

trait MappedInputProcessor extends InputProcessor {
	var game : UserInputListener
}