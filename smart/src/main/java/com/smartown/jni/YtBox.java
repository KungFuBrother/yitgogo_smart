package com.smartown.jni;

public class YtBox {

	public static String encode(String keyString, String originalString) {
		byte[] keyBytes = keyString.getBytes();

		byte[] originalBytes = originalString.getBytes();
		int length = ((originalBytes.length / 16) + (originalBytes.length % 16 > 0 ? 1 : 0)) * 16;

		byte[] encodedBytes = new byte[length];
		Encrypt(originalBytes, keyBytes, encodedBytes, originalBytes.length);

		return Base64.encode(encodedBytes);
	}

	public static String decode(String keyString, String encodedString) {
		byte[] keyBytes = keyString.getBytes();

		byte[] encodedBytes = Base64.decode(encodedString);
		int length = ((encodedBytes.length / 16) + (encodedBytes.length % 16 > 0 ? 1 : 0)) * 16;

		byte[] originalBytes = new byte[length];
		Decrypt(encodedBytes, keyBytes, originalBytes, encodedBytes.length);
		return new String(originalBytes);
	}

	public static native int Encrypt(byte[] msg, byte[] key, byte[] cipher, int length);

	public static native int Decrypt(byte[] cipher, byte[] key, byte[] result, int length);

	static {
		System.loadLibrary("AES");
	}

}
