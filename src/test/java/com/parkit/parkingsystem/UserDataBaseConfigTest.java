package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.config.UserDataBaseConfig;

public class UserDataBaseConfigTest {

	private static UserDataBaseConfig userDataBaseConfig;

	@Test
	public void userDataBaseConfigMustReadTheTwoLinesOfTheFile() {
		userDataBaseConfig = new UserDataBaseConfig("src/test/resources/userdbconfigTest.txt");
		assertThat(userDataBaseConfig.getName()).isEqualTo("NAME_OF_USER");
		assertThat(userDataBaseConfig.getPsw()).isEqualTo("PSW_OF_USER");
	}

	@Test
	public void userDataBaseConfigMustCatchExceptionIfFileNotFound() {
		userDataBaseConfig = new UserDataBaseConfig("src/test/resources/filenotfound.txt");
		assertThat(userDataBaseConfig.getName()).isEqualTo("");
		assertThat(userDataBaseConfig.getPsw()).isEqualTo("");
	}

}
