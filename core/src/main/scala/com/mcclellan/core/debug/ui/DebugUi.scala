package com.mcclellan.core.debug.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.utils.Align
import com.mcclellan.core.GameContainer

class DebugUi {
	val table = new Table()
	table.setColor(new Color(1,1,0,1));
	val font = new BitmapFont
	font.setScale(1.1f)
	val labelStyle = new LabelStyle(font, new Color(1,1,1,1))
	val fpsLabel = new Label("",labelStyle)
	table.align(Align.top | Align.left)
	table.row.align(Align.left)
	table.add(fpsLabel)
	table.setSize(100, 100)
	table.setPosition(0, Gdx.graphics.getHeight() - 100)
	table.padLeft(15.0f)
	table.padTop(15.0f)
	table.setWidth(200)
	table.debug()
	
	val activeComponents = new Label("", labelStyle)
	val activeDrawables = new Label("", labelStyle)
	table.row.align(Align.left)
	table.add(activeComponents)
	table.row.align(Align.left)
	table.add(activeDrawables)
	val stage = new Stage()
	stage.addActor(table)
	stage.setViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true)
	
	def draw(elapsed : Float, game : GameContainer) = {
		stage.act(elapsed)
		stage.draw
		fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond())
		activeComponents.setText("Components: " + game.updateables.length)
		activeDrawables.setText("Drawables: " + game.drawables.length)
		
		Table.drawDebug(stage)
	}
}