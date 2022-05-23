package hw.Algorithm;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAEncryption {

	private KeyPairGenerator generator;
	private KeyPair keyPair;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private byte[] pubk, prik;
	private Cipher cipher;

	public RSAEncryption() {
		try {
			this.generator = KeyPairGenerator.getInstance("RSA");
			this.cipher = Cipher.getInstance("RSA");
			this.generator.initialize(2048);
			this.keyPair = generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		this.publicKey = this.keyPair.getPublic();
		this.privateKey = this.keyPair.getPrivate();
		this.pubk = this.publicKey.getEncoded();
		this.prik = this.privateKey.getEncoded();
	}

	public RSAEncryption(byte[] pubk) {

		try {
			this.cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.generator.initialize(2048);

		this.pubk = pubk;
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(pubk);
		try {
			this.publicKey = keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public KeyPair getKey() {
		return keyPair;
	}

	public PublicKey getPublicKey() {

		return this.publicKey;
	}

	public PrivateKey getPrivateKey() {

		return this.privateKey;
	}

	public byte[] getBytePublicKey() {

		return this.pubk;
	}

	public byte[] getBytePrivateKey() {

		return this.prik;
	}

	public byte[] encrypt(byte[] plaintext) {

		byte[] ciphertext = null;
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			ciphertext = this.cipher.doFinal(plaintext);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return ciphertext;
	}

	public byte[] encrypt(byte[] plaintext, PublicKey pubKey) {

		byte[] ciphertext = null;
		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			ciphertext = this.cipher.doFinal(plaintext);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return ciphertext;
	}

	public byte[] decrypt(byte[] ciphertext) {
		byte[] plaintext = null;
		try {
			this.cipher.init(Cipher.DECRYPT_MODE, privateKey);
			plaintext = this.cipher.doFinal(ciphertext);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return plaintext;
	}

	public void setKeyPair(String publicKey, String privateKey) {
		this.pubk = publicKey.getBytes();
		this.prik = privateKey.getBytes();
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubk);
		X509EncodedKeySpec priKeySpec = new X509EncodedKeySpec(prik);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			this.publicKey = keyFactory.generatePublic(pubKeySpec);
			this.privateKey = keyFactory.generatePrivate(priKeySpec);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}