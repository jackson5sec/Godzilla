package org.bouncycastle.crypto.engines;

public class AESWrapPadEngine extends RFC5649WrapEngine {
  public AESWrapPadEngine() {
    super(new AESEngine());
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\engines\AESWrapPadEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */