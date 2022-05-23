package hw.Dto;

import java.io.Serializable;

public class EncryptedAESkey implements Serializable{
	private byte[] EncryptedSecretKey;
	private byte[] EncryptedIv;
	
	public EncryptedAESkey(byte[] EncryptedSecretKey, byte[] EncryptedIv) {
		this.EncryptedSecretKey = EncryptedSecretKey;
		this.EncryptedIv = EncryptedIv;
	}

	public byte[] getEncryptedSecretKey() {
		return EncryptedSecretKey;
	}

	public byte[] getEncryptedIv() {
		return EncryptedIv;
	}
	
	
}
