package com.mcclellan.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.mcclellan.core.Main
import com.badlogic.gdx.InputProcessor
import com.mcclellan.input.MappedInputProcessor
import com.badlogic.gdx.ApplicationListener
import com.mcclellan.input.UserInputListener
import com.mcclellan.input.actions._
import com.mcclellan.desktop.input.DesktopInput
import com.badlogic.gdx.utils.GdxNativesLoader

object MainDesktop extends App {
	GdxNativesLoader.load()
	val config = new LwjglApplicationConfiguration
	config.useGL20 = true
	config.width = 800
	config.height = 600
	val app = new LwjglApplication(new Main(new DesktopInput), config)
}