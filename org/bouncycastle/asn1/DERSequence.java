package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class DERSequence extends ASN1Sequence {
  private int bodyLength = -1;
  
  public DERSequence() {}
  
  public DERSequence(ASN1Encodable paramASN1Encodable) {
    super(paramASN1Encodable);
  }
  
  public DERSequence(ASN1EncodableVector paramASN1EncodableVector) {
    super(paramASN1EncodableVector);
  }
  
  public DERSequence(ASN1Encodable[] paramArrayOfASN1Encodable) {
    super(paramArrayOfASN1Encodable);
  }
  
  private int getBodyLength() throws IOException {
    if (this.bodyLength < 0) {
      int i = 0;
      Enumeration<Object> enumeration = getObjects();
      while (enumeration.hasMoreElements()) {
        ASN1Encodable aSN1Encodable = (ASN1Encodable)enumeration.nextElement();
        i += ((ASN1Encodable)aSN1Encodable).toASN1Primitive().toDERObject().encodedLength();
      } 
      this.bodyLength = i;
    } 
    return this.bodyLength;
  }
  
  int encodedLength() throws IOException {
    int i = getBodyLength();
    return 1 + StreamUtil.calculateBodyLength(i) + i;
  }
  
  void encode(ASN1OutputStream paramASN1OutputStream) throws IOException {
    ASN1OutputStream aSN1OutputStream = paramASN1OutputStream.getDERSubStream();
    int i = getBodyLength();
    paramASN1OutputStream.write(48);
    paramASN1OutputStream.writeLength(i);
    Enumeration<Object> enumeration = getObjects();
    while (enumeration.hasMoreElements()) {
      ASN1Encodable aSN1Encodable = (ASN1Encodable)enumeration.nextElement();
      aSN1OutputStream.writeObject(aSN1Encodable);
    } 
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\DERSequence.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */