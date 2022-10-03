package org.bouncycastle.asn1;

import java.io.IOException;

public class BERSetParser implements ASN1SetParser {
  private ASN1StreamParser _parser;
  
  BERSetParser(ASN1StreamParser paramASN1StreamParser) {
    this._parser = paramASN1StreamParser;
  }
  
  public ASN1Encodable readObject() throws IOException {
    return this._parser.readObject();
  }
  
  public ASN1Primitive getLoadedObject() throws IOException {
    return new BERSet(this._parser.readVector());
  }
  
  public ASN1Primitive toASN1Primitive() {
    try {
      return getLoadedObject();
    } catch (IOException iOException) {
      throw new ASN1ParsingException(iOException.getMessage(), iOException);
    } 
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\BERSetParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */