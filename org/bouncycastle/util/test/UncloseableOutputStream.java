package org.bouncycastle.util.test;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UncloseableOutputStream extends FilterOutputStream {
  public UncloseableOutputStream(OutputStream paramOutputStream) {
    super(paramOutputStream);
  }
  
  public void close() {
    throw new RuntimeException("close() called on UncloseableOutputStream");
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.out.write(paramArrayOfbyte, paramInt1, paramInt2);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\test\UncloseableOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */