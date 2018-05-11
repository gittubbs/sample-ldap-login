package model;

import java.util.concurrent.TimeUnit;

import com.google.common.io.BaseEncoding;
import com.lochbridge.oath.otp.TOTP;

import model.bean.Response;
import model.bean.User;

public class OTPValidator {

	public static boolean validate(User user, Response resp, String clientTOTP){
		
		String secretKey32 = resp.getSecret();
		byte[] key = BaseEncoding.base32().decode(secretKey32);
		TOTP totp = TOTP.key(key).timeStep(TimeUnit.SECONDS.toMillis(20)).digits(8).hmacSha256().build();
		
		return totp.value().equals(clientTOTP);
	}
}
