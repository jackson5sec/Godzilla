package org.bouncycastle.pqc.crypto.gmss;

public class GMSSPublicKeyParameters extends GMSSKeyParameters {
  private byte[] gmssPublicKey;
  
  public GMSSPublicKeyParameters(byte[] paramArrayOfbyte, GMSSParameters paramGMSSParameters) {
    super(false, paramGMSSParameters);
    this.gmssPublicKey = paramArrayOfbyte;
  }
  
  public byte[] getPublicKey() {
    return this.gmssPublicKey;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\gmss\GMSSPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */