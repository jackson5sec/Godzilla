package org.bouncycastle.jcajce.provider.keystore;

import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;

public class PKCS12 {
  private static final String PREFIX = "org.bouncycastle.jcajce.provider.keystore.pkcs12.";
  
  public static class Mappings extends AsymmetricAlgorithmProvider {
    public void configure(ConfigurableProvider param1ConfigurableProvider) {
      param1ConfigurableProvider.addAlgorithm("KeyStore.PKCS12", "org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi$BCPKCS12KeyStore");
      param1ConfigurableProvider.addAlgorithm("KeyStore.BCPKCS12", "org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi$BCPKCS12KeyStore");
      param1ConfigurableProvider.addAlgorithm("KeyStore.PKCS12-DEF", "org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi$DefPKCS12KeyStore");
      param1ConfigurableProvider.addAlgorithm("KeyStore.PKCS12-3DES-40RC2", "org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi$BCPKCS12KeyStore");
      param1ConfigurableProvider.addAlgorithm("KeyStore.PKCS12-3DES-3DES", "org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi$BCPKCS12KeyStore3DES");
      param1ConfigurableProvider.addAlgorithm("KeyStore.PKCS12-DEF-3DES-40RC2", "org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi$DefPKCS12KeyStore");
      param1ConfigurableProvider.addAlgorithm("KeyStore.PKCS12-DEF-3DES-3DES", "org.bouncycastle.jcajce.provider.keystore.pkcs12.PKCS12KeyStoreSpi$DefPKCS12KeyStore3DES");
    }
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\provider\keystore\PKCS12.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */