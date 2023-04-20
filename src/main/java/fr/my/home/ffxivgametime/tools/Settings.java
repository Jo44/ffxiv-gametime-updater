package fr.my.home.ffxivgametime.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Settings
 * 
 * @version 1.2
 */
public class Settings {
	private static Logger logger = LogManager.getLogger(Settings.class);

	private static final File SETTINGS_FILE = new File(System.getenv("APPDATA") + "\\ffxiv-gametime\\settings.cfg");
	private static String appVersion = "0.0";
	private static Properties properties;

	/**
	 * Load settings from file
	 */
	static {
		properties = new Properties();
		logger.info("Chargement des parametres ..");
		try {
			// Check settings file
			if (SETTINGS_FILE != null && SETTINGS_FILE.exists() && SETTINGS_FILE.isFile() && SETTINGS_FILE.canRead()) {
				// Load settings from file
				FileInputStream fis = new FileInputStream(SETTINGS_FILE.getAbsolutePath());
				properties.load(fis);
				fis.close();
				// Check settings from properties
				checkSettingsFromProperties();
				logger.info("-> fichier settings.cfg charge");
			} else {
				logger.error("-> fichier settings.cfg inexistant");
				throw new IOException();
			}
		} catch (Exception ex) {
			logger.error("-> fichier settings.cfg non valide");
			// Write default settings file
			try {
				writeSettings("Final Fantasy XIV", "F5", "Espace", "F6", "F7", "Echap", "Num 0", false, 1706, 897, 18, 6, "", "", "", "", "");
				logger.info("-> nouveau fichier settings.cfg cree");
			} catch (IOException ioe) {
				logger.error("-> impossible de creer le nouveau fichier settings.cfg");
			}
		}
	}

	/**
	 * Save settings
	 * 
	 * @param appVersion
	 * @return boolean
	 */
	public static boolean saveSettings(String newVersion) {
		boolean saved;
		try {
			// Update
			appVersion = newVersion;
			// Write current settings with new app version
			writeSettings(getAppFocus(), getKeybindAntiAfkExec(), getKeybindAntiAfkAction(), getKeybindMacroExec(), getKeybindMacroMousePos(),
					getKeybindClose(), getKeybindConfirm(), getGearMod(), getGearFromX(), getGearFromY(), getGearOffsetX(), getGearOffsetY(),
					getCraftFavFile(), getSetUpFavFile(), getFoodFavFile(), getRepairFavFile(), getMateriaFavFile());
			saved = true;
		} catch (IOException ioe) {
			logger.error("-> impossible de modifier le fichier settings.cfg");
			saved = false;
		}
		return saved;
	}

	/**
	 * Check all settings from properties
	 * 
	 * @throws InvalidParameterException
	 */
	private static void checkSettingsFromProperties() throws InvalidParameterException {
		// Check app version
		String appVersion = properties.getProperty("app.version");
		if (appVersion == null || appVersion.trim().isEmpty()) {
			throw new InvalidParameterException();
		}
		// Check integer parameters
		try {
			Integer.parseInt(properties.getProperty("gear.from.x"));
			Integer.parseInt(properties.getProperty("gear.from.y"));
			Integer.parseInt(properties.getProperty("gear.offset.x"));
			Integer.parseInt(properties.getProperty("gear.offset.y"));
		} catch (NumberFormatException nfe) {
			throw new InvalidParameterException();
		}
	}

	/**
	 * Write settings in file
	 * 
	 * @param appFocus
	 * @param kbAntiAfkExec
	 * @param kbAntiAfkAction
	 * @param kbMacroExec
	 * @param kbMacroMousePos
	 * @param kbClose
	 * @param kbConfirm
	 * @param gearMod
	 * @param gearFromX
	 * @param gearFromY
	 * @param gearOffsetX
	 * @param gearOffsetY
	 * @param craftFavFile
	 * @param setUpFavFile
	 * @param foodFavFile
	 * @param repairFavFile
	 * @param materiaFavFile
	 * @throws IOException
	 */
	private static void writeSettings(String appFocus, String kbAntiAfkExec, String kbAntiAfkAction, String kbMacroExec, String kbMacroMousePos,
			String kbClose, String kbConfirm, boolean gearMod, int gearFromX, int gearFromY, int gearOffsetX, int gearOffsetY, String craftFavFile,
			String setUpFavFile, String foodFavFile, String repairFavFile, String materiaFavFile) throws IOException {
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
		fileWriter.append("\ngear.mod=" + String.valueOf(gearMod));
		fileWriter.append("\ngear.from.x=" + String.valueOf(gearFromX));
		fileWriter.append("\ngear.from.y=" + String.valueOf(gearFromY));
		fileWriter.append("\ngear.offset.x=" + String.valueOf(gearOffsetX));
		fileWriter.append("\ngear.offset.y=" + String.valueOf(gearOffsetY));
		fileWriter.append("\ncraft.fav.file=" + craftFavFile);
		fileWriter.append("\nset.up.fav.file=" + setUpFavFile);
		fileWriter.append("\nfood.fav.file=" + foodFavFile);
		fileWriter.append("\nrepair.fav.file=" + repairFavFile);
		fileWriter.append("\nmateria.fav.file=" + materiaFavFile);
		fileWriter.close();
		logger.info("-> fichier settings.cfg cree/modifie avec succes");
		// Load values
		properties.setProperty("app.version", appVersion);
		properties.setProperty("app.focus", appFocus);
		properties.setProperty("keybind.antiafk.exec", kbAntiAfkExec);
		properties.setProperty("keybind.antiafk.action", kbAntiAfkAction);
		properties.setProperty("keybind.macro.exec", kbMacroExec);
		properties.setProperty("keybind.macro.mousepos", kbMacroMousePos);
		properties.setProperty("keybind.close", kbClose);
		properties.setProperty("keybind.confirm", kbConfirm);
		properties.setProperty("gear.mod", String.valueOf(gearMod));
		properties.setProperty("gear.from.x", String.valueOf(gearFromX));
		properties.setProperty("gear.from.y", String.valueOf(gearFromY));
		properties.setProperty("gear.offset.x", String.valueOf(gearOffsetX));
		properties.setProperty("gear.offset.y", String.valueOf(gearOffsetY));
		properties.setProperty("craft.fav.file", craftFavFile);
		properties.setProperty("set.up.fav.file", setUpFavFile);
		properties.setProperty("food.fav.file", foodFavFile);
		properties.setProperty("repair.fav.file", repairFavFile);
		properties.setProperty("materia.fav.file", materiaFavFile);
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
	 * Get gear mod
	 * 
	 * @return boolean
	 */
	private static boolean getGearMod() {
		return Boolean.parseBoolean(properties.getProperty("gear.mod"));
	}

	/**
	 * Get gear from X
	 * 
	 * @return int
	 */
	private static int getGearFromX() {
		return Integer.parseInt(properties.getProperty("gear.from.x"));
	}

	/**
	 * Get gear from Y
	 * 
	 * @return int
	 */
	private static int getGearFromY() {
		return Integer.parseInt(properties.getProperty("gear.from.y"));
	}

	/**
	 * Get gear offset X
	 * 
	 * @return int
	 */
	private static int getGearOffsetX() {
		return Integer.parseInt(properties.getProperty("gear.offset.x"));
	}

	/**
	 * Get gear offset Y
	 * 
	 * @return int
	 */
	private static int getGearOffsetY() {
		return Integer.parseInt(properties.getProperty("gear.offset.y"));
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
