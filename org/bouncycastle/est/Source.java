package org.bouncycastle.est;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Source<T> {
  InputStream getInputStream() throws IOException;
  
  OutputStream getOutputStream() throws IOException;
  
  T getSession();
  
  void close() throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\est\Source.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */