package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.dh.BCDHPublicKey;

public class DHUtil {
  public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey paramPublicKey) throws InvalidKeyException {
    if (paramPublicKey instanceof BCDHPublicKey)
      return (AsymmetricKeyParameter)((BCDHPublicKey)paramPublicKey).engineGetKeyParameters(); 
    if (paramPublicKey instanceof DHPublicKey) {
      DHPublicKey dHPublicKey = (DHPublicKey)paramPublicKey;
      return (AsymmetricKeyParameter)new DHPublicKeyParameters(dHPublicKey.getY(), new DHParameters(dHPublicKey.getParams().getP(), dHPublicKey.getParams().getG(), null, dHPublicKey.getParams().getL()));
    } 
    throw new InvalidKeyException("can't identify DH public key.");
  }
  
  public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey paramPrivateKey) throws InvalidKeyException {
    if (paramPrivateKey instanceof DHPrivateKey) {
      DHPrivateKey dHPrivateKey = (DHPrivateKey)paramPrivateKey;
      return (AsymmetricKeyParameter)new DHPrivateKeyParameters(dHPrivateKey.getX(), new DHParameters(dHPrivateKey.getParams().getP(), dHPrivateKey.getParams().getG(), null, dHPrivateKey.getParams().getL()));
    } 
    throw new InvalidKeyException("can't identify DH private key.");
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\provider\asymmetri\\util\DHUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */