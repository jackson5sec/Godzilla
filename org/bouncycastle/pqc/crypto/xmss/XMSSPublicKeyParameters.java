package org.bouncycastle.pqc.crypto.xmss;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public final class XMSSPublicKeyParameters extends AsymmetricKeyParameter implements XMSSStoreableObjectInterface {
  private final XMSSParameters params;
  
  private final byte[] root;
  
  private final byte[] publicSeed;
  
  private XMSSPublicKeyParameters(Builder paramBuilder) {
    super(false);
    this.params = paramBuilder.params;
    if (this.params == null)
      throw new NullPointerException("params == null"); 
    int i = this.params.getDigestSize();
    byte[] arrayOfByte = paramBuilder.publicKey;
    if (arrayOfByte != null) {
      int j = i;
      int k = i;
      int m = j + k;
      if (arrayOfByte.length != m)
        throw new IllegalArgumentException("public key has wrong size"); 
      int n = 0;
      this.root = XMSSUtil.extractBytesAtOffset(arrayOfByte, n, j);
      n += j;
      this.publicSeed = XMSSUtil.extractBytesAtOffset(arrayOfByte, n, k);
    } else {
      byte[] arrayOfByte1 = paramBuilder.root;
      if (arrayOfByte1 != null) {
        if (arrayOfByte1.length != i)
          throw new IllegalArgumentException("length of root must be equal to length of digest"); 
        this.root = arrayOfByte1;
      } else {
        this.root = new byte[i];
      } 
      byte[] arrayOfByte2 = paramBuilder.publicSeed;
      if (arrayOfByte2 != null) {
        if (arrayOfByte2.length != i)
          throw new IllegalArgumentException("length of publicSeed must be equal to length of digest"); 
        this.publicSeed = arrayOfByte2;
      } else {
        this.publicSeed = new byte[i];
      } 
    } 
  }
  
  public byte[] toByteArray() {
    int i = this.params.getDigestSize();
    int j = i;
    int k = i;
    int m = j + k;
    byte[] arrayOfByte = new byte[m];
    int n = 0;
    XMSSUtil.copyBytesAtOffset(arrayOfByte, this.root, n);
    n += j;
    XMSSUtil.copyBytesAtOffset(arrayOfByte, this.publicSeed, n);
    return arrayOfByte;
  }
  
  public byte[] getRoot() {
    return XMSSUtil.cloneArray(this.root);
  }
  
  public byte[] getPublicSeed() {
    return XMSSUtil.cloneArray(this.publicSeed);
  }
  
  public XMSSParameters getParameters() {
    return this.params;
  }
  
  public static class Builder {
    private final XMSSParameters params;
    
    private byte[] root = null;
    
    private byte[] publicSeed = null;
    
    private byte[] publicKey = null;
    
    public Builder(XMSSParameters param1XMSSParameters) {
      this.params = param1XMSSParameters;
    }
    
    public Builder withRoot(byte[] param1ArrayOfbyte) {
      this.root = XMSSUtil.cloneArray(param1ArrayOfbyte);
      return this;
    }
    
    public Builder withPublicSeed(byte[] param1ArrayOfbyte) {
      this.publicSeed = XMSSUtil.cloneArray(param1ArrayOfbyte);
      return this;
    }
    
    public Builder withPublicKey(byte[] param1ArrayOfbyte) {
      this.publicKey = XMSSUtil.cloneArray(param1ArrayOfbyte);
      return this;
    }
    
    public XMSSPublicKeyParameters build() {
      return new XMSSPublicKeyParameters(this);
    }
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\xmss\XMSSPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */