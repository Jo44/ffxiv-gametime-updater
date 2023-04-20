package fr.my.home.ffxivgametime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.ffxivgametime.tools.Settings;

/**
 * FFXIV GameTime Updater
 * 
 * @version 1.1
 */
public class MyUpdater {
	private static Logger logger = LogManager.getLogger(MyUpdater.class);

	private static final String TMP_DIR = "tmp";
	private static final String VERSION_FILE = "ffxiv-gametime.version";
	private static final String VERSION_URL = "https://raw.githubusercontent.com/Jo44/ffxiv-gametime/main/distrib/ffxiv-gametime.version";
	private static final String UPDATE_FILE = "FFXIV-GameTime.exe";
	private static final String UPDATE_URL = "https://raw.githubusercontent.com/Jo44/ffxiv-gametime/main/distrib/FFXIV-GameTime.exe";
	private static String currentVersion;
	private static String newerVersion;

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		logger.info("-> Updater <-");

		// Init settings
		new Settings();

		// Prepare update
		prepareUpdate();

		try {
			// Check update
			logger.info("Vérification de la version ...");
			boolean updateAvailable = checkUpdate();

			logger.info("-> Version actuelle : v" + currentVersion);
			logger.info("-> Version disponible : v" + newerVersion);

			// If update available
			if (updateAvailable) {

				// Download files
				logger.info("Téléchargement de la nouvelle version ...");
				boolean updateDownloaded = downloadUpdate();

				// If update downloaded
				if (updateDownloaded) {
					logger.info("-> Nouvelle version téléchargée");

					// Move files
					logger.info("Mise à jour vers la nouvelle version ...");
					moveFiles();
					logger.info("-> Mise à jour vers la nouvelle version exécutée avec succès !");

					// Update settings
					Settings.saveSettings(newerVersion);

				} else {
					logger.error("-> Impossible de télécharger la nouvelle version :/");
				}

			} else {
				// No update available
				logger.info("-> L'application est déjà à jour !");
			}

			// Start main app
			logger.info("Lancement de l'application ...");
			startMainApp();

		} catch (IOException | InterruptedException ex) {

			logger.error("Une erreur est survenue pendant la mise à jour !");
			logger.error(ex.getMessage());

		} finally {
			// Cleanup
			cleanup();

			// Close updater
			logger.info("Fermeture de l'updater.");
		}

	}

	/**
	 * Prepare update
	 */
	private static void prepareUpdate() {

		// Create tmp dir
		File tmpDir = new File(TMP_DIR);
		if (tmpDir != null && !tmpDir.exists()) {
			tmpDir.mkdir();
		}

	}

	/**
	 * Check if update available
	 * 
	 * @return boolean
	 * @throws IOException
	 */
	private static boolean checkUpdate() throws IOException {

		boolean updateRequired = false;
		// Download version file
		File versionFile = downloadFile(VERSION_URL, VERSION_FILE);
		// Get update version
		newerVersion = getNewerVersion(versionFile);
		// Compare to current version
		updateRequired = needUpdate(newerVersion);
		return updateRequired;

	}

	/**
	 * Download update
	 * 
	 * @return boolean
	 * @throws IOException
	 */
	private static boolean downloadUpdate() throws IOException {

		boolean updateDownloaded = false;
		// Download update exe
		File updateFile = downloadFile(UPDATE_URL, UPDATE_FILE);
		// Check file
		if (updateFile != null && updateFile.exists() && updateFile.canRead()) {
			updateDownloaded = true;
		}
		return updateDownloaded;

	}

	/**
	 * Move files
	 * 
	 * @throws IOException
	 */
	private static void moveFiles() throws IOException {

		// Prepare files
		File currentFile = new File(UPDATE_FILE);
		File tmpFile = new File(TMP_DIR + "\\" + UPDATE_FILE);
		// Move new exe from tmp
		Files.copy(tmpFile.toPath(), currentFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

	}

	/**
	 * Start main app
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void startMainApp() throws IOException, InterruptedException {

		// Exec runtime
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(UPDATE_FILE);
		process.waitFor();

	}

	/**
	 * Download file from url
	 * 
	 * @param link
	 * @param filename
	 * @return File
	 * @throws IOException
	 */
	private static File downloadFile(String link, String filename) throws IOException {

		File downloadFile = new File(TMP_DIR + "\\" + filename);
		// Create connection
		URL url = new URL(link);
		URLConnection con = url.openConnection();
		con.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; .NET CLR 1.2.30703)");
		// Get input stream
		InputStream input = con.getInputStream();
		byte[] buffer = new byte[4096];
		int n = -1;
		// Write into output stream
		OutputStream output = new FileOutputStream(downloadFile);
		while ((n = input.read(buffer)) != -1) {
			if (n > 0) {
				output.write(buffer, 0, n);
			}
		}
		output.close();
		// Return file
		return downloadFile;

	}

	/**
	 * Get newer version
	 * 
	 * @param updateFile
	 * @return String
	 * @throws IOException
	 */
	private static String getNewerVersion(File updateFile) throws IOException {
		newerVersion = "";
		Path path = Paths.get(updateFile.getAbsolutePath());
		String read = Files.readAllLines(path).get(0);
		if (read != null && !read.trim().isEmpty()) {
			newerVersion = read.substring(read.indexOf("app.version=") + 12);
		}
		return newerVersion;
	}

	/**
	 * Check if new update available
	 * 
	 * @param newVersion
	 * @return boolean
	 * @throws IOException
	 */
	private static boolean needUpdate(String newVersion) throws IOException {
		boolean updateAvailable = false;
		currentVersion = Settings.getAppVersion();
		if (!newVersion.equals(currentVersion)) {
			updateAvailable = true;
		}
		return updateAvailable;
	}

	/**
	 * Cleanup
	 */
	private static void cleanup() {

		// Delete tmp dir & files
		File tmpDir = new File(TMP_DIR);
		if (tmpDir != null && tmpDir.exists()) {
			File[] files = tmpDir.listFiles();
			if (files != null) {
				for (File file : files) {
					file.delete();
				}
			}
			tmpDir.delete();
		}

	}

}
