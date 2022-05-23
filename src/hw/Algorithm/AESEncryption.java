package hw.Algorithm;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import hw.Dto.EncryptedAESkey;

public class AESEncryption {

	private static String padding = "AES/CBC/PKCS5Padding";
	private final String key = "01234567890123456789012345678901";
	private final String iv = key.substring(0, 16); // 16byte
	private Cipher cipher;
	private SecretKeySpec secretKey;
	private IvParameterSpec IV;
	private RSAEncryption rsa;
	//in construction set the IV, secretKey
	public AESEncryption() {
		try {
			cipher = Cipher.getInstance(padding);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.secretKey = new SecretKeySpec(key.getBytes(), "AES");
		this.IV = new IvParameterSpec(iv.getBytes());
	}
	//if encrypted secretKey is sent, it is decrypted with RSA
	public AESEncryption(EncryptedAESkey encrypt, RSAEncryption rsa) {
		try {
			cipher = Cipher.getInstance(padding);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.rsa = rsa;
		this.secretKey = new SecretKeySpec(rsa.decrypt(encrypt.getEncryptedSecretKey()), "AES");
		this.IV = new IvParameterSpec(rsa.decrypt(encrypt.getEncryptedIv()));
	}

	public byte[] encrypt(String text) {
		byte [] ciphertext = null;
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV);
			ciphertext = cipher.doFinal(text.getBytes("UTF-8"));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ciphertext;

	}
	public byte[] encrypt(byte[] text) {

		byte [] ciphertext = null;
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV);
			ciphertext = cipher.doFinal(text);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ciphertext;
	}

	public byte[] decrypt(String cipherText) {

		byte[] plaintext = null;
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, secretKey, IV);
			plaintext = cipher.doFinal(cipherText.getBytes("UTF-8"));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return plaintext;
	}
	public byte[] decrypt(byte[] cipherText) {

		byte[] plaintext = null;
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, secretKey, IV);
			plaintext = cipher.doFinal(cipherText);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return plaintext;
	}


	public String getIv() {
		return iv;
	}

	public SecretKeySpec getSecretKey() {
		return secretKey;
	}

}