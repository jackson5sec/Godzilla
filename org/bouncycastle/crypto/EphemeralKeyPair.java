package org.bouncycastle.crypto;

public class EphemeralKeyPair {
  private AsymmetricCipherKeyPair keyPair;
  
  private KeyEncoder publicKeyEncoder;
  
  public EphemeralKeyPair(AsymmetricCipherKeyPair paramAsymmetricCipherKeyPair, KeyEncoder paramKeyEncoder) {
    this.keyPair = paramAsymmetricCipherKeyPair;
    this.publicKeyEncoder = paramKeyEncoder;
  }
  
  public AsymmetricCipherKeyPair getKeyPair() {
    return this.keyPair;
  }
  
  public byte[] getEncodedPublicKey() {
    return this.publicKeyEncoder.getEncoded(this.keyPair.getPublic());
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\EphemeralKeyPair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */