package org.bouncycastle.x509;

import java.io.InputStream;
import java.util.Collection;
import org.bouncycastle.x509.util.StreamParsingException;

public abstract class X509StreamParserSpi {
  public abstract void engineInit(InputStream paramInputStream);
  
  public abstract Object engineRead() throws StreamParsingException;
  
  public abstract Collection engineReadAll() throws StreamParsingException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\x509\X509StreamParserSpi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */