package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class DERSet extends ASN1Set {
  private int bodyLength = -1;
  
  public DERSet() {}
  
  public DERSet(ASN1Encodable paramASN1Encodable) {
    super(paramASN1Encodable);
  }
  
  public DERSet(ASN1EncodableVector paramASN1EncodableVector) {
    super(paramASN1EncodableVector, true);
  }
  
  public DERSet(ASN1Encodable[] paramArrayOfASN1Encodable) {
    super(paramArrayOfASN1Encodable, true);
  }
  
  DERSet(ASN1EncodableVector paramASN1EncodableVector, boolean paramBoolean) {
    super(paramASN1EncodableVector, paramBoolean);
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
    paramASN1OutputStream.write(49);
    paramASN1OutputStream.writeLength(i);
    Enumeration<Object> enumeration = getObjects();
    while (enumeration.hasMoreElements()) {
      ASN1Encodable aSN1Encodable = (ASN1Encodable)enumeration.nextElement();
      aSN1OutputStream.writeObject(aSN1Encodable);
    } 
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\DERSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */