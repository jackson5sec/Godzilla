package org.bouncycastle.jcajce;

import java.io.OutputStream;
import java.security.KeyStore;

public class PKCS12StoreParameter implements KeyStore.LoadStoreParameter {
  private final OutputStream out;
  
  private final KeyStore.ProtectionParameter protectionParameter;
  
  private final boolean forDEREncoding;
  
  public PKCS12StoreParameter(OutputStream paramOutputStream, char[] paramArrayOfchar) {
    this(paramOutputStream, paramArrayOfchar, false);
  }
  
  public PKCS12StoreParameter(OutputStream paramOutputStream, KeyStore.ProtectionParameter paramProtectionParameter) {
    this(paramOutputStream, paramProtectionParameter, false);
  }
  
  public PKCS12StoreParameter(OutputStream paramOutputStream, char[] paramArrayOfchar, boolean paramBoolean) {
    this(paramOutputStream, new KeyStore.PasswordProtection(paramArrayOfchar), paramBoolean);
  }
  
  public PKCS12StoreParameter(OutputStream paramOutputStream, KeyStore.ProtectionParameter paramProtectionParameter, boolean paramBoolean) {
    this.out = paramOutputStream;
    this.protectionParameter = paramProtectionParameter;
    this.forDEREncoding = paramBoolean;
  }
  
  public OutputStream getOutputStream() {
    return this.out;
  }
  
  public KeyStore.ProtectionParameter getProtectionParameter() {
    return this.protectionParameter;
  }
  
  public boolean isForDEREncoding() {
    return this.forDEREncoding;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\PKCS12StoreParameter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */