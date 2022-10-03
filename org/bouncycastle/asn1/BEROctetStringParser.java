package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.util.io.Streams;

public class BEROctetStringParser implements ASN1OctetStringParser {
  private ASN1StreamParser _parser;
  
  BEROctetStringParser(ASN1StreamParser paramASN1StreamParser) {
    this._parser = paramASN1StreamParser;
  }
  
  public InputStream getOctetStream() {
    return new ConstructedOctetStream(this._parser);
  }
  
  public ASN1Primitive getLoadedObject() throws IOException {
    return new BEROctetString(Streams.readAll(getOctetStream()));
  }
  
  public ASN1Primitive toASN1Primitive() {
    try {
      return getLoadedObject();
    } catch (IOException iOException) {
      throw new ASN1ParsingException("IOException converting stream to byte array: " + iOException.getMessage(), iOException);
    } 
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\BEROctetStringParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */