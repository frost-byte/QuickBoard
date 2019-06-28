package me.tade.quickboard;

import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author The_TadeSK
 */
class PluginUpdater {

	private QuickBoard plugin;
	private boolean needUpdate;
	private String[] updateInfo;

	PluginUpdater(QuickBoard plugin) {
		this.plugin = plugin;

		new BukkitRunnable(){
			public void run(){
				doUpdate();
			}
		}.runTaskTimerAsynchronously(plugin, 20, 1800 * 20);
	}

	private void doUpdate(){
		String response = getResponse();

		if(response == null){
			System.out.println("Some sort of error happened! Can't get new version of QuickBoard!");
			return;
		}
		updateInfo = response.split(";");
		System.out.println("Current QuickBoard version: " + plugin.getDescription().getVersion());
		System.out.println("Web QuickBoard version: " + updateInfo[0]);

		if(plugin.getDescription().getVersion().equalsIgnoreCase(updateInfo[0]))
			return;

		System.out.println(" ");
		System.out.println("QuickBoard I got new Update!");

		needUpdate = true;

		plugin.sendUpdateMessage();
	}

	private String getResponse(){
		try {
			System.out.println("Trying to get new version of QuickBoard...");
			URL post = new URL("https://raw.githubusercontent.com/frost-byte/QuickBoard/master/VERSION");

			return get(post);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private String get(URL url){
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(url.openStream()));

			String line;

			StringBuilder sb = new StringBuilder();

			while ((line = br.readLine()) != null) {

				sb.append(line);
				sb.append(System.lineSeparator());
			}

			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	boolean needUpdate() {
		return needUpdate;
	}

	String[] getUpdateInfo() {
		return updateInfo;
	}
}
