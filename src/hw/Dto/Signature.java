package hw.Dto;

import java.io.Serializable;

public class Signature implements Serializable{

	private byte[] originMessage;
	private byte[] signature;
	private String fileName;
	
	public Signature(byte[] originMessage, byte[] signature, String fileName) {
		this.originMessage = originMessage;
		this.signature = signature;
		this.fileName = fileName;
	}

	
	public byte[] getOriginMessage() {
		return originMessage;
	}

	public byte[] getSignature() {
		return signature;
	}
	public String getFileName() {
		return fileName;
	}
	
}
