package org.bouncycastle.crypto.engines;

public class AESWrapEngine extends RFC3394WrapEngine {
  public AESWrapEngine() {
    super(new AESEngine());
  }
  
  public AESWrapEngine(boolean paramBoolean) {
    super(new AESEngine(), paramBoolean);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\engines\AESWrapEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */