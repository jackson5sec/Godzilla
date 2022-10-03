package org.bouncycastle.jcajce.provider.keystore;

import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;

public class BCFKS {
  private static final String PREFIX = "org.bouncycastle.jcajce.provider.keystore.bcfks.";
  
  public static class Mappings extends AsymmetricAlgorithmProvider {
    public void configure(ConfigurableProvider param1ConfigurableProvider) {
      param1ConfigurableProvider.addAlgorithm("KeyStore.BCFKS", "org.bouncycastle.jcajce.provider.keystore.bcfks.BcFKSKeyStoreSpi$Std");
      param1ConfigurableProvider.addAlgorithm("KeyStore.BCFKS-DEF", "org.bouncycastle.jcajce.provider.keystore.bcfks.BcFKSKeyStoreSpi$Def");
    }
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\provider\keystore\BCFKS.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */