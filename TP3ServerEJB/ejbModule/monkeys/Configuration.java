package monkeys;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class COnfiguration
 */
@Stateless
@LocalBean
public class Configuration implements IMap {

	public final String filePath = "/monkeys.properties";

	/**
	 * Default constructor.
	 */
	public Configuration() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[][] readFileMap() {
		int[][] map = null;
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("monkeys.properties");
			java.util.Properties properties = new java.util.Properties();
			properties.load(is);

			String value = properties.getProperty("MONKEYS_MAP");
			value = value.replaceAll(",", "");
			value = value.replaceAll("\"", "");

			int jSize = value.split(";").length;
			int iSize = 0;
			for (int k = 0; k < value.length(); k++) {
				if (value.charAt(k) == ';') {
					iSize++;
				}
			}
			map = new int[iSize + 1][jSize];

			int j = 0;
			int i = 0;
			for (int k = 0; k < value.length(); k++) {
				if (value.charAt(k) != ';') {
					map[i][j] = Character.getNumericValue(value.charAt(k));
					j++;
				} else {
					j = 0;
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	}

	public int readFileEnergy() {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("monkeys.properties");
			java.util.Properties properties = new java.util.Properties();
			properties.load(is);
			return Integer.parseInt(properties.getProperty("PIRATE_MAX_ENERGIE"));
		} catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}

	public int readRhumEnergy() {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("monkeys.properties");
			java.util.Properties properties = new java.util.Properties();
			properties.load(is);
			return Integer.parseInt(properties.getProperty("RHUM_ENERGIE"));
		} catch (Exception e) {
			System.out.println(e);
		}
		return -1;
	}

	public int readMonkeysNbr() {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("monkeys.properties");
			java.util.Properties properties = new java.util.Properties();
			properties.load(is);
			return Integer.parseInt(properties.getProperty("MONKEY_ERRATIC_NBR"));

		} catch (Exception e) {
			System.out.println(e);
			return -1;
		}
	}

}
