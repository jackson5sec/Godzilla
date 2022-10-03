package org.bouncycastle.util.test;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;

public class TestRandomEntropySourceProvider implements EntropySourceProvider {
  private final SecureRandom _sr = new SecureRandom();
  
  private final boolean _predictionResistant;
  
  public TestRandomEntropySourceProvider(boolean paramBoolean) {
    this._predictionResistant = paramBoolean;
  }
  
  public EntropySource get(final int bitsRequired) {
    return new EntropySource() {
        public boolean isPredictionResistant() {
          return TestRandomEntropySourceProvider.this._predictionResistant;
        }
        
        public byte[] getEntropy() {
          byte[] arrayOfByte = new byte[(bitsRequired + 7) / 8];
          TestRandomEntropySourceProvider.this._sr.nextBytes(arrayOfByte);
          return arrayOfByte;
        }
        
        public int entropySize() {
          return bitsRequired;
        }
      };
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\test\TestRandomEntropySourceProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */