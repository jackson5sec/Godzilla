package org.bouncycastle.jcajce.provider.symmetric;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.ChaCha7539Engine;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseStreamCipher;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public final class ChaCha {
  public static class Base extends BaseStreamCipher {
    public Base() {
      super((StreamCipher)new ChaChaEngine(), 8);
    }
  }
  
  public static class Base7539 extends BaseStreamCipher {
    public Base7539() {
      super((StreamCipher)new ChaCha7539Engine(), 12);
    }
  }
  
  public static class KeyGen extends BaseKeyGenerator {
    public KeyGen() {
      super("ChaCha", 128, new CipherKeyGenerator());
    }
  }
  
  public static class KeyGen7539 extends BaseKeyGenerator {
    public KeyGen7539() {
      super("ChaCha7539", 256, new CipherKeyGenerator());
    }
  }
  
  public static class Mappings extends AlgorithmProvider {
    private static final String PREFIX = ChaCha.class.getName();
    
    public void configure(ConfigurableProvider param1ConfigurableProvider) {
      param1ConfigurableProvider.addAlgorithm("Cipher.CHACHA", PREFIX + "$Base");
      param1ConfigurableProvider.addAlgorithm("KeyGenerator.CHACHA", PREFIX + "$KeyGen");
      param1ConfigurableProvider.addAlgorithm("Cipher.CHACHA7539", PREFIX + "$Base7539");
      param1ConfigurableProvider.addAlgorithm("KeyGenerator.CHACHA7539", PREFIX + "$KeyGen7539");
    }
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\provider\symmetric\ChaCha.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */