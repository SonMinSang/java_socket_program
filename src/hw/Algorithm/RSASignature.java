package hw.Algorithm;

import java.security.*;

public class RSASignature {
	
	private String algorithm = "SHA512WithRSA";
	private Signature sig;
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public RSASignature(PublicKey publicKey, PrivateKey privateKey) {
		try {
			this.sig = Signature.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public RSASignature(PublicKey publicKey) {
		try {
			this.sig = Signature.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.publicKey = publicKey;
	}
	
	public RSASignature(PrivateKey privateKey) {
		try {
			this.sig = Signature.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.privateKey = privateKey;
	}

	public byte[] RSAsign(byte[] data) {

		byte[] sign = null;
		try {
			sig.initSign(this.privateKey);
			sig.update(data);
			sign = sig.sign();
		} catch (InvalidKeyException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sign;
	}

	public boolean RSAverify(byte[] data, byte[] signature) {

		boolean verification = true;
		try {
			sig.initVerify(this.publicKey);
			sig.update(data);
			verification = sig.verify(signature);
		} catch (InvalidKeyException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return verification;
	}
}
