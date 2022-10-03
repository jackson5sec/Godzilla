package org.bouncycastle.openssl.jcajce;

import java.security.PrivateKey;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.util.io.pem.PemGenerationException;

public class JcaPKCS8Generator extends PKCS8Generator {
  public JcaPKCS8Generator(PrivateKey paramPrivateKey, OutputEncryptor paramOutputEncryptor) throws PemGenerationException {
    super(PrivateKeyInfo.getInstance(paramPrivateKey.getEncoded()), paramOutputEncryptor);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\openssl\jcajce\JcaPKCS8Generator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */