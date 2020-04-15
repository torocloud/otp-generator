package service

import io.toro.gloop.annotation.GloopObjectParameter
import io.toro.gloop.annotation.GloopParameter
import io.toro.gloop.object.property.GloopModel

import java.util.concurrent.TimeUnit

import com.warrenstrange.googleauth.GoogleAuthenticator
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig
import com.warrenstrange.googleauth.GoogleAuthenticatorKey
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator
import com.warrenstrange.googleauth.ICredentialRepository

class TOTPAuthenticator {

	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateSecretKey() {
		return generateSecretKey( getDefaultAuthenticatorConfig() )
	}

	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateSecretKey(
			GloopModel authenticatorConfig) {

		GoogleAuthenticator authenticator = getAuthenticator( authenticatorConfig )
		GoogleAuthenticatorKey key = authenticator.createCredentials()

		GloopModel otpAuthenticatorKey = new GloopModel( 'otpAuthenticatorKey' )
		otpAuthenticatorKey.setReference( 'model.OtpAuthenticatorKey' )
		otpAuthenticatorKey.put( 'secretKey', key.getKey() )
		
		if ( authenticatorConfig.getProperty( 'enableScratchCodes' ) ) {
			otpAuthenticatorKey.put( 'scratchCodes', key.getScratchCodes() )
		}

		GloopModel response = new GloopModel( 'response' )
		response.setReference( 'io.toro.martini.api.APIResponse' )
		response.setAllowExtraProperties( true )
		response.put( 'message', 'Successfully generated secret key' )
		response.addChild( otpAuthenticatorKey )

		def output = new GloopModel( 'output' )
		output.addChild( response )

		return output
	}
	
	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateOneTimePassword() {
		return generateOneTimePassword( getDefaultAuthenticatorConfig() )
	}
	
	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateOneTimePassword( @GloopObjectParameter("authenticatorConfig#model.AuthenticatorConfig*{\n}\n")
			GloopModel authenticatorConfig) {
		
		try {
			GoogleAuthenticator authenticator = getAuthenticator( authenticatorConfig )
			GoogleAuthenticatorKey key = authenticator.createCredentials()
			String oneTimePassword = authenticator.getTotpPassword( key.getKey() )
			
			GloopModel otpAuthenticatorKey = new GloopModel( 'otpAuthenticatorKey' )
			otpAuthenticatorKey.setReference( 'model.OtpAuthenticatorKey' )
			otpAuthenticatorKey.put( 'secretKey', key.getKey() )
			otpAuthenticatorKey.put( 'oneTimePassword', oneTimePassword )
			
			if ( authenticatorConfig.getProperty( 'enableScratchCodes' ) ) {
				otpAuthenticatorKey.put( 'scratchCodes', key.getScratchCodes() )
			}
	
			GloopModel response = new GloopModel( 'response' )
			response.setReference( 'io.toro.martini.api.APIResponse' )
			response.setAllowExtraProperties( true )
			response.put( 'message', 'Successfully generated OTP Auth TOTP URL' )
			response.addChild( otpAuthenticatorKey )
	
			def output = new GloopModel( 'output' )
			output.addChild( response )
	
			return output
		} catch ( Exception ex ) {
			return wrapException( ex )
		}
	}
	
	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateOneTimePassword( String secretKey) {
		return generateOneTimePassword( getDefaultAuthenticatorConfig(), secretKey )
	}
	
	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateOneTimePassword( @GloopObjectParameter("authenticatorConfig#model.AuthenticatorConfig*{\n}\n")
			GloopModel authenticatorConfig, String secretKey) {
		
		try {
			GoogleAuthenticator authenticator = getAuthenticator( authenticatorConfig )
			String oneTimePassword = authenticator.getTotpPassword( secretKey )
			
			GloopModel otpAuthenticatorKey = new GloopModel( 'otpAuthenticatorKey' )
			otpAuthenticatorKey.setReference( 'model.OtpAuthenticatorKey' )
			otpAuthenticatorKey.put( 'secretKey', secretKey )
			otpAuthenticatorKey.put( 'oneTimePassword', oneTimePassword )
	
			GloopModel response = new GloopModel( 'response' )
			response.setReference( 'io.toro.martini.api.APIResponse' )
			response.setAllowExtraProperties( true )
			response.put( 'message', 'Successfully generated OTP Auth TOTP URL' )
			response.addChild( otpAuthenticatorKey )
	
			def output = new GloopModel( 'output' )
			output.addChild( response )
	
			return output
		} catch ( Exception ex ) {
			return wrapException( ex )
		}
	}

	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateOtpAuthTotpURL( String issuer, String accountName) {
		return generateOtpAuthTotpURL( getDefaultAuthenticatorConfig(), issuer, accountName )
	}

	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateOtpAuthTotpURL( @GloopObjectParameter("authenticatorConfig#model.AuthenticatorConfig*{\n}\n")
			GloopModel authenticatorConfig, String issuer, String accountName) {

		GoogleAuthenticator authenticator = getAuthenticator( authenticatorConfig )
		GoogleAuthenticatorKey key = authenticator.createCredentials()
		String otpAuthTotpUrl = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL( issuer, accountName, key )

		GloopModel otpAuthenticatorKey = new GloopModel( 'otpAuthenticatorKey' )
		otpAuthenticatorKey.setReference( 'model.OtpAuthenticatorKey' )
		otpAuthenticatorKey.put( 'secretKey', key.getKey() )
		otpAuthenticatorKey.put( 'otpAuthTotpUrl', otpAuthTotpUrl )
		
		if ( authenticatorConfig.getProperty( 'enableScratchCodes' ) ) {
			otpAuthenticatorKey.put( 'scratchCodes', key.getScratchCodes() )
		}

		GloopModel response = new GloopModel( 'response' )
		response.setReference( 'io.toro.martini.api.APIResponse' )
		response.setAllowExtraProperties( true )
		response.put( 'message', 'Successfully generated OTP Auth TOTP URL' )
		response.addChild( otpAuthenticatorKey )

		def output = new GloopModel( 'output' )
		output.addChild( response )
		return output
	}

	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateOtpAuthTotpURL( String issuer, String accountName, String secretKey) {
		return generateOtpAuthTotpURL( getDefaultAuthenticatorConfig(), issuer, accountName, secretKey)
	}

	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n    otpAuthenticatorKey#model.OtpAuthenticatorKey{\n    }\n  }\n}\n")
	public static GloopModel generateOtpAuthTotpURL( @GloopObjectParameter("authenticatorConfig#model.AuthenticatorConfig*{\n}\n")
			GloopModel authenticatorConfig, String issuer, String accountName, String secretKey) {
		
		GoogleAuthenticator authenticator = getAuthenticator( authenticatorConfig )
		GoogleAuthenticatorKey key = new GoogleAuthenticatorKey.Builder( secretKey )
			.setConfig( authenticator.getConfig() )
			.build()
		String otpAuthTotpUrl = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL( issuer, accountName, key )
		
		GloopModel otpAuthenticatorKey = new GloopModel( 'otpAuthenticatorKey' )
		otpAuthenticatorKey.setReference( 'model.OtpAuthenticatorKey' )
		otpAuthenticatorKey.put( 'secretKey', key.getKey() )
		otpAuthenticatorKey.put( 'otpAuthTotpUrl', otpAuthTotpUrl )
		
		if ( authenticatorConfig.getProperty( 'enableScratchCodes' ) ) {
			otpAuthenticatorKey.put( 'scratchCodes', key.getScratchCodes() )
		}
		
		GloopModel response = new GloopModel( 'response' )
		response.setReference( 'io.toro.martini.api.APIResponse' )
		response.setAllowExtraProperties( true )
		response.put( 'message', "Successfully generated OTP Auth TOTP URL with provided 'secretKey'" )
		response.addChild( otpAuthenticatorKey )

		def output = new GloopModel( 'output' )
		output.addChild( response )
		return output
	}

	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse{\n  }\n}\n")
	public static GloopModel authorize( String secretKey, int verificationCode) {
		return authorize( getDefaultAuthenticatorConfig(), secretKey, verificationCode )
	}

	@GloopObjectParameter("output{\n  response#io.toro.martini.api.APIResponse*{\n  }\n}\n")
	public static GloopModel authorize( @GloopObjectParameter("authenticatorConfig#model.AuthenticatorConfig*{\n}\n")
			GloopModel authenticatorConfig, String secretKey, int verificationCode) {
		try {
			GoogleAuthenticator authenticator = getAuthenticator( authenticatorConfig )
			boolean isAuthorized = authenticator.authorize( secretKey, verificationCode )

			GloopModel response = new GloopModel( 'response' )
			response.setReference( 'io.toro.martini.api.APIResponse' )
			response.put( 'result', isAuthorized ? 'OK' : 'ERROR' )
			response.put( 'message', isAuthorized ? 'OK' : 'Invalid verification code' )
			
			def output = new GloopModel( 'output' )
			output.addChild( response )
			return output
		} catch ( Exception ex ) {
			return wrapException( ex )
		}
	}

	private static GoogleAuthenticatorKey generateSecretKeyFrom( String accountName ) {
		
		GoogleAuthenticator authenticator = getAuthenticator()
		UserCredsRepository userCredsRepo = new UserCredsRepository()
		authenticator.setCredentialRepository( userCredsRepo )
		
		GoogleAuthenticatorKey key = authenticator.createCredentials( accountName )
		return key
	}

	private static GloopModel wrapException( Exception ex ) {
		ex.printStackTrace()

		GloopModel response = new GloopModel( 'response' )
		response.setReference( 'io.toro.martini.api.APIResponse' )
		response.setAllowExtraProperties(true)
		response.put( 'result', 'ERROR' )
		response.put( 'message', ex.getMessage() )

		def output = new GloopModel( 'output' )
		output.addChild( response )		
		return output
	}
	
	private static Object getAuthenticator( @GloopObjectParameter("authenticatorConfig#model.AuthenticatorConfig*{\n}\n")
			GloopModel authenticatorConfig) {
			
		GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
			.setTimeStepSizeInMillis( TimeUnit.SECONDS.toMillis( authenticatorConfig.timeStepSizeInSeconds ) )
			.setNumberOfScratchCodes( authenticatorConfig.numberOfScratchCodes )
			.setWindowSize( authenticatorConfig.windowSize )
			.setCodeDigits( authenticatorConfig.codeDigitsLength )
			.build()
		
		final GoogleAuthenticator authenticator = new GoogleAuthenticator( config )
		return authenticator
	}
	
	private static GoogleAuthenticator getAuthenticator() {
		return getAuthenticator( getDefaultAuthenticatorConfig() )
	}

	private static GloopModel getDefaultAuthenticatorConfig() {
		
		GloopModel authenticatorConfig = new GloopModel( 'authenticatorConfig' )
		authenticatorConfig.setReference( 'model.AuthenticatorConfig' )

		def timeStepSizeInSeconds = Integer.valueOf( 'totp.timeStepSizeInSeconds'.getPackageProperty( '30' ) )
		def numberOfScratchCodes = Integer.valueOf( 'totp.numberOfScratchCodes'.getPackageProperty( '5' ) )
		def windowSize = Integer.valueOf( 'totp.windowSize'.getPackageProperty( '3' ) )
		def codeDigitsLength = Integer.valueOf( 'totp.codeDigitsLength'.getPackageProperty( '6' ) )
		def enableScratchCodes = Boolean.valueOf( 'totp.enableScratchCodes'.getPackageProperty( 'false' ) )
		
		authenticatorConfig.put( 'codeDigitsLength', codeDigitsLength )
		authenticatorConfig.put( 'numberOfScratchCodes', numberOfScratchCodes )
		authenticatorConfig.put( 'windowSize', windowSize )
		authenticatorConfig.put( 'timeStepSizeInSeconds', timeStepSizeInSeconds )
		authenticatorConfig.put( 'enableScratchCodes', enableScratchCodes )
		
		return authenticatorConfig
	}
	
	static class UserCredsRepository implements ICredentialRepository {
		
		String userName
		String secretKey
		int validationCode
		List<Integer> scratchCodes
		
		@Override
		String getSecretKey(String userName) {
			return this.userName == userName ? secretKey : null
		}

		@Override
		void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
			this.userName = userName
			this.secretKey = secretKey
			this.validationCode = validationCode
			this.scratchCodes = scratchCodes
		}
	}
}
