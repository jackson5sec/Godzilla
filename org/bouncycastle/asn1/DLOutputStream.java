package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class DLOutputStream extends ASN1OutputStream {
  public DLOutputStream(OutputStream paramOutputStream) {
    super(paramOutputStream);
  }
  
  public void writeObject(ASN1Encodable paramASN1Encodable) throws IOException {
    if (paramASN1Encodable != null) {
      paramASN1Encodable.toASN1Primitive().toDLObject().encode(this);
    } else {
      throw new IOException("null object detected");
    } 
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\DLOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */