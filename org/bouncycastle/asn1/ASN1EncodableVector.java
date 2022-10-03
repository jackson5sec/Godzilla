package org.bouncycastle.asn1;

import java.util.Enumeration;
import java.util.Vector;

public class ASN1EncodableVector {
  private final Vector v = new Vector();
  
  public void add(ASN1Encodable paramASN1Encodable) {
    this.v.addElement(paramASN1Encodable);
  }
  
  public void addAll(ASN1EncodableVector paramASN1EncodableVector) {
    Enumeration enumeration = paramASN1EncodableVector.v.elements();
    while (enumeration.hasMoreElements())
      this.v.addElement(enumeration.nextElement()); 
  }
  
  public ASN1Encodable get(int paramInt) {
    return this.v.elementAt(paramInt);
  }
  
  public int size() {
    return this.v.size();
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\ASN1EncodableVector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */