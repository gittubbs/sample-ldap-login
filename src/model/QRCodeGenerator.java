package model;

import java.io.File;
import java.io.IOException;

import com.lochbridge.oath.otp.keyprovisioning.OTPAuthURI;
import com.lochbridge.oath.otp.keyprovisioning.OTPAuthURIBuilder;
import com.lochbridge.oath.otp.keyprovisioning.OTPKey;
import com.lochbridge.oath.otp.keyprovisioning.OTPKey.OTPType;
import com.lochbridge.oath.otp.keyprovisioning.qrcode.QRCodeWriter;
import com.lochbridge.oath.otp.keyprovisioning.qrcode.QRCodeWriter.ErrorCorrectionLevel;

import model.bean.Response;
import model.bean.User;


public class QRCodeGenerator {

	private static final String ISSUER = "prova";
	private static final String ALGORITHM = "sha256";
	
	public static void generate(User user, Response resp, String path){
		File file = new File(path);
		OTPKey key = new OTPKey(resp.getSecret(), OTPType.TOTP);
		
		OTPAuthURI uri = OTPAuthURIBuilder.fromKey(key).label(user.getUsername()).issuer(ISSUER)
			    .digits(8).timeStep(20000L).algorithm(ALGORITHM).build();
		
		try {
		
			QRCodeWriter.fromURI(uri).width(300).height(300).errorCorrectionLevel(ErrorCorrectionLevel.H)
			    .margin(4).imageFormatName("PNG").write(file.toPath());
		
		} catch (IOException e) {
			System.out.println("I/O error. Couldn't save the image");
		}
	}
}
