package org.bouncycastle.asn1;

import java.io.IOException;

public class DERSequenceParser implements ASN1SequenceParser {
  private ASN1StreamParser _parser;
  
  DERSequenceParser(ASN1StreamParser paramASN1StreamParser) {
    this._parser = paramASN1StreamParser;
  }
  
  public ASN1Encodable readObject() throws IOException {
    return this._parser.readObject();
  }
  
  public ASN1Primitive getLoadedObject() throws IOException {
    return new DERSequence(this._parser.readVector());
  }
  
  public ASN1Primitive toASN1Primitive() {
    try {
      return getLoadedObject();
    } catch (IOException iOException) {
      throw new IllegalStateException(iOException.getMessage());
    } 
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\DERSequenceParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */