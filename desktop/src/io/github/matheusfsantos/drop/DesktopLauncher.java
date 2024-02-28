package io.github.matheusfsantos.drop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.matheusfsantos.drop.DropGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		DesktopLauncher.config(config);
		new Lwjgl3Application(new DropGame(), config);
	}

	public static void config(Lwjgl3ApplicationConfiguration config) {
		config.setTitle("Drop");
		config.useVsync(true);
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.setWindowedMode(800, 480);
	}
}
