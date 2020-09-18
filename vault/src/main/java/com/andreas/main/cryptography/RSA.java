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

import com.andreas.main.FileUtils;

import org.jdom2.Element;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * An RSA algorithm utility class.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class RSA {

	/**
	 * This method generates a random RSA key pair of any size.
	 * @param size 
	 * @return
	 */
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

	/**
     * This method encrypts <code>byte[]</code> RSA data.
     * @param data The data to encrypt.
     * @param key The public key you want the data to encrypt with.
     * @return Returns the encrypted cipher.
     */
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
	
	/**
     * This method encrypts <code>String</code> RSA data.
     * @param data The data to encrypt.
     * @param key The public key for encrypting the data.
     * @return Returns the encrypted cipher.
     */
	public static byte[] encrypt(String data, PublicKey key) {
		return encrypt(data.getBytes(StandardCharsets.UTF_8), key);
	}
	
	/**
     * This method decrypts a <code>byte[]</code> RSA cipher to <code>byte[]</code>.
     * @param cipher The cipher to decrypt.
     * @param key The private key for decrypting the cipher.
     * @return Returns the decrypted data as <code>byte[]</code>.
     */
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
	
	/**
     * This method decrypts a <code>byte[]</code> RSA cipher to <code>String</code>.
     * @param cipher The cipher to decrypt.
     * @param key The private key for decrypting the cipher.
     * @return Returns the decrypted data as <code>String</code>.
     */
	public static String decrypt(byte[] cipher, PrivateKey key) {
		byte[] decrypted = decryptToBytes(cipher, key);
		return new String(decrypted, StandardCharsets.UTF_8);
	}

	/**
	 * This method creates a SHA256 with RSA signature.
	 * @param data The data as <code>byte[]</code> to sign.
	 * @param key The private key to sign the data.
	 * @return Returns the signature.
	 */
	public static byte[] sign(byte[] data, PrivateKey key) {
	    Signature signature = null;
		try {
			signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(key, new SecureRandom());
			byte[] message = data;
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

	/**
	 * This method creates a SHA256 with RSA signature.
	 * @param data The data as <code>String</code> to sign.
	 * @param key The private key to sign the data.
	 * @return Returns the signature.
	 */
	public static byte[] sign(String data, PrivateKey key) {
		return sign(data.getBytes(StandardCharsets.UTF_8), key);
	}
	
	/**
	 * This method verifies a SHA256 with RSA signature.
	 * @param data The data that needs to be verified.
	 * @param signatureBytes The signature you want the data to verify with.
	 * @param key The public key verifying.
	 * @return Returns <code>true</code> if the data is verified.
	 */
	public static boolean verify(String data, byte[] signatureBytes, PublicKey key) {
		
		Signature signature;
		try {
			signature = Signature.getInstance("SHA256withRSA");
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

	/**
	 * This method reads an .xml element and converts it to an RSA key pair.
	 * @param element The .xml to convert.
	 * @return Returns the RSA key pair.
	 * @see {@link #keyPairToXmlElement(KeyPair)}.
	 */
	public static KeyPair xmlElementToKeyPair(Element element) {

		Element publicElement = element.getChild("public"), privateElement = element.getChild("private");

		byte[] publicKeyBytes = FileUtils.hexToBytes(publicElement.getText()),
				privateKeyBytes = FileUtils.hexToBytes(privateElement.getText());

		PublicKey publicKey = getPublicKeyFromByteArray(publicKeyBytes);
		PrivateKey privateKey = getPrivateKeyFromByteArray(privateKeyBytes);

		return new KeyPair(publicKey, privateKey);
	}

	/**
	 * This method reads an .xml file and converts it to an RSA key pair.
	 * @param path The path where the .xml file is located.
	 * @return Returns the key pair of the .xml file.
	 * @see {@link #writeKeyPair(KeyPair, String)}.
	 */
	public static KeyPair readKeyPair(String path) {

		Element root = FileUtils.readXmlFile(path);
		return xmlElementToKeyPair(root);
	}
	
	/**
	 * This method converts an RSA key pair to an .xml element.
	 * @param keyPair The key pair to convert.
	 * @return Returns the .xml element.
	 */
	public static Element keyPairToXmlElement(KeyPair keyPair) {

		byte[] publicKeyBytes = RSA.getBytesFromPublicKey(keyPair.getPublic());
		byte[] privateKeyBytes = RSA.getBytesFromPrivateKey(keyPair.getPrivate());
		
		Element keys = new Element("keys");

		Element publicKey = new Element("public").setText(FileUtils.bytesToHex(publicKeyBytes));
	    keys.addContent(publicKey);
		Element privateKey = new Element("private").setText(FileUtils.bytesToHex(privateKeyBytes));
		keys.addContent(privateKey);

		return keys;
	}

	/**
	 * This method writes an RSA key pair to an .xml file.
	 * @param keyPair The key pair to convert.
	 * @param path The path where the file should be located.
	 * @return Returns <code>true</code> if the creation was successfull.
	 */
    public static boolean writeKeyPair(KeyPair keyPair, String path) {
		Element keys = keyPairToXmlElement(keyPair);
		return FileUtils.createXmlFile(path, keys);
	}

	/**
	 * This method converts an RSA public key to <code>byte[]</code>.
	 * @param key The public key to convert.
	 * @return Returns the converted public key <code>byte[]</code>.
	 */
	public static byte[] getBytesFromPublicKey(PublicKey key) {
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(key.getEncoded());
		return publicKeySpec.getEncoded();
	}

	/**
	 * This method converts an RSA private key to <code>byte[]</code>.
	 * @param key The private key to convert.
	 * @return Returns the converted private key <code>byte[]</code>.
	 */
	public static byte[] getBytesFromPrivateKey(PrivateKey key) {
		X509EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(key.getEncoded());
		return privateKeySpec.getEncoded();
	}

	/**
	 * This method converts <code>byte[]</code> to an RSA private key.
	 * @param byteArray The <code>byte[]</code> you want to convert.
	 * @return Returns the converted private key.
	 * @see {@link #getBytesFromPrivateKey(PrivateKey)}.
	 */
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

	/**
	 * This method <code>byte[]</code> to an RSA public key.
	 * @param byteArray The <code>byte[]</code> you want to convert.
	 * @return
	 * @see {@link #getBytesFromPublicKey(PublicKey)}.
	 */
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