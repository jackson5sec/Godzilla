package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HeartbeatExtension {
  protected short mode;
  
  public HeartbeatExtension(short paramShort) {
    if (!HeartbeatMode.isValid(paramShort))
      throw new IllegalArgumentException("'mode' is not a valid HeartbeatMode value"); 
    this.mode = paramShort;
  }
  
  public short getMode() {
    return this.mode;
  }
  
  public void encode(OutputStream paramOutputStream) throws IOException {
    TlsUtils.writeUint8(this.mode, paramOutputStream);
  }
  
  public static HeartbeatExtension parse(InputStream paramInputStream) throws IOException {
    short s = TlsUtils.readUint8(paramInputStream);
    if (!HeartbeatMode.isValid(s))
      throw new TlsFatalAlert((short)47); 
    return new HeartbeatExtension(s);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\HeartbeatExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */