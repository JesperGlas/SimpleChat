package Encryption;

import java.security.*;

public class RSAKeyGen {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAKeyGen() throws NoSuchAlgorithmException {
        generateNewKeys();
    }

    public void generateNewKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair keyPair = keyGen.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
