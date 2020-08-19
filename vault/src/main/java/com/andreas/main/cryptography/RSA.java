package com.andreas.main.cryptography;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.andreas.main.Utils;

import org.jdom2.Element;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class RSA {

	public static KeyPair gen(int size) {
		KeyPairGenerator keygen = null;
		try {
			keygen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keygen.initialize(size);
		return keygen.generateKeyPair();
	}

	public static byte[] encrypt(byte[] data, PublicKey key) {
		Cipher c = null;
		try {
			c = Cipher.getInstance("RSA");
			c.init(Cipher.ENCRYPT_MODE, key);
			return c.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static byte[] encrypt(String data, PublicKey key) {
		return encrypt(data.getBytes(StandardCharsets.UTF_8), key);
	}
	
	public static byte[] decryptToBytes(byte[] cipher, PrivateKey key) {
		
		Cipher c = null;
		try {
			c = Cipher.getInstance("RSA");
			c.init(Cipher.DECRYPT_MODE, key);
			return c.doFinal(cipher);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decrypt(byte[] cipher, PrivateKey key) {
		byte[] decrypted = decryptToBytes(cipher, key);
		return new String(decrypted, StandardCharsets.UTF_8);
	}

	public static byte[] sign(String data, PrivateKey key) {
	    Signature signature = null;
		try {
			signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(key, new SecureRandom());
			byte[] message = data.getBytes(StandardCharsets.UTF_8);
	        signature.update(message);
	        return signature.sign();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static boolean verify(String data, byte[] signatureBytes, PublicKey key) {
		
		Signature signature;
		try {
			signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(key);
			signature.update(data.getBytes(StandardCharsets.UTF_8));
			return signature.verify(signatureBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public static KeyPair xmlElementToKeyPair(Element element) {

		Element publicElement = element.getChild("public"), privateElement = element.getChild("private");

		byte[] publicKeyBytes = Utils.hexToBytes(publicElement.getText()),
				privateKeyBytes = Utils.hexToBytes(privateElement.getText());

		PublicKey publicKey = getPublicKeyFromByteArray(publicKeyBytes);
		PrivateKey privateKey = getPrivateKeyFromByteArray(privateKeyBytes);

		return new KeyPair(publicKey, privateKey);
	}

	public static KeyPair readKeyPair(String path) {

		Element root = Utils.readXmlFile(path);
		return xmlElementToKeyPair(root);
	}
	
	public static Element keyPairToXmlElement(KeyPair keyPair) {

		byte[] publicKeyBytes = RSA.getBytesFromPublicKey(keyPair.getPublic());
		byte[] privateKeyBytes = RSA.getBytesFromPrivateKey(keyPair.getPrivate());
		
		Element keys = new Element("keys");

		Element publicKey = new Element("public").setText(Utils.bytesToHex(publicKeyBytes));
	    keys.addContent(publicKey);
		Element privateKey = new Element("private").setText(Utils.bytesToHex(privateKeyBytes));
		keys.addContent(privateKey);

		return keys;
	}

    public static boolean writeKeyPair(KeyPair keyPair, String path) {
		
		Element keys = keyPairToXmlElement(keyPair);
		return Utils.createXmlFile(path, keys);
	}

	public static byte[] getBytesFromPublicKey(PublicKey key) {
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(key.getEncoded());
		return publicKeySpec.getEncoded();
	}

	public static byte[] getBytesFromPrivateKey(PrivateKey key) {
		X509EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(key.getEncoded());
		return privateKeySpec.getEncoded();
	}

	public static PrivateKey getPrivateKeyFromByteArray(byte[] byteArray) {
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(byteArray);
			return kf.generatePrivate(privateKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static PublicKey getPublicKeyFromByteArray(byte[] byteArray) {
		KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(byteArray);
			return kf.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return null;
	}
}