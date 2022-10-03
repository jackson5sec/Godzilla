package org.bouncycastle.x509.util;

import java.util.Collection;

public interface StreamParser {
  Object read() throws StreamParsingException;
  
  Collection readAll() throws StreamParsingException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\x50\\util\StreamParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */