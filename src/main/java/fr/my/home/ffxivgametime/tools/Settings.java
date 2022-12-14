package fr.my.home.ffxivgametime.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Settings
 * 
 * @version 1.1
 */
public class Settings {
	private static Logger logger = LogManager.getLogger(Settings.class);

	private static final File SETTINGS_FILE = new File(System.getenv("APPDATA") + "\\ffxiv-gametime\\settings.cfg");
	private static Properties properties;

	/**
	 * Load settings from file
	 */
	static {
		properties = new Properties();
		logger.info("Chargement des parametres ..");
		try {
			if (SETTINGS_FILE != null && SETTINGS_FILE.exists() && SETTINGS_FILE.isFile() && SETTINGS_FILE.canRead()) {
				// Load settings from file
				FileInputStream fis = new FileInputStream(SETTINGS_FILE.getAbsolutePath());
				properties.load(fis);
				fis.close();
				logger.info("-> fichier settings.cfg charge avec succes");
			} else {
				throw new IOException();
			}
		} catch (IOException ioe) {
			// Write default settings in file
			writeSettings("0.0", "Final Fantasy XIV", "F5", "Espace", "F6", "F7", "Echap", "Num 0", "", "", "", "", "");
		}
	}

	/**
	 * Write appVersion in file
	 * 
	 * @param appVersion
	 */
	public static void writeSettings(String appVersion) {
		// Write current settings with new app version
		writeSettings(appVersion, getAppFocus(), getKeybindAntiAfkExec(), getKeybindAntiAfkAction(), getKeybindMacroExec(), getKeybindMacroMousePos(),
				getKeybindClose(), getKeybindConfirm(), getCraftFavFile(), getSetUpFavFile(), getFoodFavFile(), getRepairFavFile(),
				getMateriaFavFile());
	}

	/**
	 * Write settings in file
	 * 
	 * @param appVersion
	 * @param appFocus
	 * @param kbAntiAfkExec
	 * @param kbAntiAfkAction
	 * @param kbMacroExec
	 * @param kbMacroMousePos
	 * @param kbClose
	 * @param kbConfirm
	 * @param craftFavFile
	 * @param setUpFavFile
	 * @param foodFavFile
	 * @param repairFavFile
	 * @param materiaFavFile
	 */
	private static void writeSettings(String appVersion, String appFocus, String kbAntiAfkExec, String kbAntiAfkAction, String kbMacroExec,
			String kbMacroMousePos, String kbClose, String kbConfirm, String craftFavFile, String setUpFavFile, String foodFavFile,
			String repairFavFile, String materiaFavFile) {
		try {
			// Create file settings.cfg
			FileWriter fileWriter = new FileWriter(SETTINGS_FILE.getAbsoluteFile());
			fileWriter.write("### FFXIV GameTime - Fichier de configuration ###");
			fileWriter.append("\napp.version=" + appVersion);
			fileWriter.append("\napp.focus=" + appFocus);
			fileWriter.append("\nkeybind.antiafk.exec=" + kbAntiAfkExec);
			fileWriter.append("\nkeybind.antiafk.action=" + kbAntiAfkAction);
			fileWriter.append("\nkeybind.macro.exec=" + kbMacroExec);
			fileWriter.append("\nkeybind.macro.mousepos=" + kbMacroMousePos);
			fileWriter.append("\nkeybind.close=" + kbClose);
			fileWriter.append("\nkeybind.confirm=" + kbConfirm);
			fileWriter.append("\ncraft.fav.file=" + craftFavFile);
			fileWriter.append("\nset.up.fav.file=" + setUpFavFile);
			fileWriter.append("\nfood.fav.file=" + foodFavFile);
			fileWriter.append("\nrepair.fav.file=" + repairFavFile);
			fileWriter.append("\nmateria.fav.file=" + materiaFavFile);
			fileWriter.close();
			logger.info("-> fichier settings.cfg cree/modifie avec succes");
		} catch (IOException ioe) {
			// Can't create/update file settings.cfg
			logger.error("-> impossible de creer/modifier le fichier settings.cfg");
		} finally {
			// Load values
			properties.setProperty("app.version", appVersion);
			properties.setProperty("app.focus", appFocus);
			properties.setProperty("keybind.antiafk.exec", kbAntiAfkExec);
			properties.setProperty("keybind.antiafk.action", kbAntiAfkAction);
			properties.setProperty("keybind.macro.exec", kbMacroExec);
			properties.setProperty("keybind.macro.mousepos", kbMacroMousePos);
			properties.setProperty("keybind.close", kbClose);
			properties.setProperty("keybind.confirm", kbConfirm);
			properties.setProperty("craft.fav.file", craftFavFile);
			properties.setProperty("set.up.fav.file", setUpFavFile);
			properties.setProperty("food.fav.file", foodFavFile);
			properties.setProperty("repair.fav.file", repairFavFile);
			properties.setProperty("materia.fav.file", materiaFavFile);
		}
	}

	/**
	 * Getters
	 */

	/**
	 * Get app version
	 * 
	 * @return String
	 */
	public static String getAppVersion() {
		return properties.getProperty("app.version");
	}

	/**
	 * Get app focus
	 * 
	 * @return String
	 */
	private static String getAppFocus() {
		return properties.getProperty("app.focus");
	}

	/**
	 * Get keybind antiafk : exec
	 * 
	 * @return String
	 */
	private static String getKeybindAntiAfkExec() {
		return properties.getProperty("keybind.antiafk.exec");
	}

	/**
	 * Get keybind antiafk : action
	 * 
	 * @return String
	 */
	private static String getKeybindAntiAfkAction() {
		return properties.getProperty("keybind.antiafk.action");
	}

	/**
	 * Get keybind macro : exec
	 * 
	 * @return String
	 */
	private static String getKeybindMacroExec() {
		return properties.getProperty("keybind.macro.exec");
	}

	/**
	 * Get keybind macro : mousepos
	 * 
	 * @return String
	 */
	private static String getKeybindMacroMousePos() {
		return properties.getProperty("keybind.macro.mousepos");
	}

	/**
	 * Get keybind close
	 * 
	 * @return String
	 */
	private static String getKeybindClose() {
		return properties.getProperty("keybind.close");
	}

	/**
	 * Get keybind confirm
	 * 
	 * @return String
	 */
	private static String getKeybindConfirm() {
		return properties.getProperty("keybind.confirm");
	}

	/**
	 * Get craft fav file
	 * 
	 * @return String
	 */
	private static String getCraftFavFile() {
		return properties.getProperty("craft.fav.file");
	}

	/**
	 * Get set up fav file
	 * 
	 * @return String
	 */
	private static String getSetUpFavFile() {
		return properties.getProperty("set.up.fav.file");
	}

	/**
	 * Get food fav file
	 * 
	 * @return String
	 */
	private static String getFoodFavFile() {
		return properties.getProperty("food.fav.file");
	}

	/**
	 * Get repair fav file
	 * 
	 * @return String
	 */
	private static String getRepairFavFile() {
		return properties.getProperty("repair.fav.file");
	}

	/**
	 * Get materia fav file
	 * 
	 * @return String
	 */
	private static String getMateriaFavFile() {
		return properties.getProperty("materia.fav.file");
	}

}
