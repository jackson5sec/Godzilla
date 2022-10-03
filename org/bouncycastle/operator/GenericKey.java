package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class GenericKey {
  private AlgorithmIdentifier algorithmIdentifier = null;
  
  private Object representation;
  
  public GenericKey(Object paramObject) {
    this.representation = paramObject;
  }
  
  public GenericKey(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfbyte) {
    this.representation = paramArrayOfbyte;
  }
  
  protected GenericKey(AlgorithmIdentifier paramAlgorithmIdentifier, Object paramObject) {
    this.representation = paramObject;
  }
  
  public AlgorithmIdentifier getAlgorithmIdentifier() {
    return this.algorithmIdentifier;
  }
  
  public Object getRepresentation() {
    return this.representation;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\GenericKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */