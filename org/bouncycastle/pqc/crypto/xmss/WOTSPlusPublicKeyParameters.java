package org.bouncycastle.pqc.crypto.xmss;

final class WOTSPlusPublicKeyParameters {
  private final byte[][] publicKey;
  
  protected WOTSPlusPublicKeyParameters(WOTSPlusParameters paramWOTSPlusParameters, byte[][] paramArrayOfbyte) {
    if (paramWOTSPlusParameters == null)
      throw new NullPointerException("params == null"); 
    if (paramArrayOfbyte == null)
      throw new NullPointerException("publicKey == null"); 
    if (XMSSUtil.hasNullPointer(paramArrayOfbyte))
      throw new NullPointerException("publicKey byte array == null"); 
    if (paramArrayOfbyte.length != paramWOTSPlusParameters.getLen())
      throw new IllegalArgumentException("wrong publicKey size"); 
    for (byte b = 0; b < paramArrayOfbyte.length; b++) {
      if ((paramArrayOfbyte[b]).length != paramWOTSPlusParameters.getDigestSize())
        throw new IllegalArgumentException("wrong publicKey format"); 
    } 
    this.publicKey = XMSSUtil.cloneArray(paramArrayOfbyte);
  }
  
  protected byte[][] toByteArray() {
    return XMSSUtil.cloneArray(this.publicKey);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\xmss\WOTSPlusPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */