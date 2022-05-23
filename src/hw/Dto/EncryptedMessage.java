package hw.Dto;

import java.io.Serializable;

public class EncryptedMessage implements Serializable{
	private byte[] encryptedMessage;
	
	public EncryptedMessage(byte[] encryptedMessage) {
		this.encryptedMessage = encryptedMessage;
	}

	public byte[] getEncryptedMessage() {
		return encryptedMessage;
	}
}
