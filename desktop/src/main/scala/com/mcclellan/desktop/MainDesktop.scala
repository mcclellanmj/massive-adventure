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
import java.awt.GraphicsEnvironment

object MainDesktop extends App {
	GdxNativesLoader.load()
	val config = new LwjglApplicationConfiguration
	// FIXME: Fix textures so it can render using GL10
	config.useGL20 = true
	config.vSyncEnabled = false
	val screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
	config.width = screen.getDisplayMode().getWidth()
	config.height = screen.getDisplayMode().getHeight()
	val app = new LwjglApplication(new Main(new DesktopInput), config)
}