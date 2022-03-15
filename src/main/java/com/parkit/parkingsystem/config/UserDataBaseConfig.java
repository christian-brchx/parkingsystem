package com.parkit.parkingsystem.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

public class UserDataBaseConfig {
	// private static String name = "usertest";
	// private static String psw = "User_test";
	private String name = "";
	private String psw = "";

	public UserDataBaseConfig(String fileOfUserDbConfig) {
		BufferedReader reader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileOfUserDbConfig, Charset.forName("UTF-8"));
			reader = new BufferedReader(fileReader);
			this.name = reader.readLine();
			this.psw = reader.readLine();
		} catch (FileNotFoundException e) {
			System.err.println("Problème ouverture fichier " + fileOfUserDbConfig);
		} catch (IOException e) {
			System.err.println("Problème lecture fichier " + fileOfUserDbConfig);
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (fileReader != null)
					fileReader.close();
			} catch (IOException e) {
				System.err.println("Problème fermeture fichier " + fileOfUserDbConfig);
			}
		}

	}

	public String getName() {
		return name;
	}

	public String getPsw() {
		return psw;
	}

}
