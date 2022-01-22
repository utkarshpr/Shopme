package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	@Test
	public void testEncvodePassword() {
		BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
		String rawPassword="utkarsh";
		String encodedPassword=pass.encode(rawPassword);
		System.out.println(encodedPassword);
		System.out.println(pass.matches(rawPassword, encodedPassword));
		
	}
}
