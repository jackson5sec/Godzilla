package org.bouncycastle.asn1;

import java.io.IOException;

public interface ASN1ApplicationSpecificParser extends ASN1Encodable, InMemoryRepresentable {
  ASN1Encodable readObject() throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\ASN1ApplicationSpecificParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */