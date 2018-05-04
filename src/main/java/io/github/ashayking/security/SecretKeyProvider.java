package io.github.ashayking.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Ashay S Patil
 *
 */
@Component
public class SecretKeyProvider {

	public String getKey() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		return new String(getKeyPair().getPublic().getEncoded(), "UTF-8");
	}

	private KeyPair getKeyPair() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			UnrecoverableKeyException {
		FileInputStream is = new FileInputStream("keys.jks");
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(is, "abracadabra".toCharArray());

		String alias = "keys";
		Key key = keystore.getKey(alias, "abracadabra".toCharArray());
		if (key instanceof PrivateKey) {
			// Get certificate of public key
			Certificate cert = keystore.getCertificate(alias);

			// Get public key
			PublicKey publicKey = cert.getPublicKey();

			// Return a key pair
			return new KeyPair(publicKey, (PrivateKey) key);
		} else
			throw new UnrecoverableKeyException();
	}
}
