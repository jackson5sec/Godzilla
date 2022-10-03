package org.bouncycastle.pqc.crypto.xmss;

import java.io.Serializable;

public final class XMSSNode implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final int height;
  
  private final byte[] value;
  
  protected XMSSNode(int paramInt, byte[] paramArrayOfbyte) {
    this.height = paramInt;
    this.value = paramArrayOfbyte;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public byte[] getValue() {
    return XMSSUtil.cloneArray(this.value);
  }
  
  protected XMSSNode clone() {
    return new XMSSNode(getHeight(), getValue());
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\xmss\XMSSNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */