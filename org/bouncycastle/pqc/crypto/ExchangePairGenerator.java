package org.bouncycastle.pqc.crypto;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public interface ExchangePairGenerator {
  ExchangePair GenerateExchange(AsymmetricKeyParameter paramAsymmetricKeyParameter);
  
  ExchangePair generateExchange(AsymmetricKeyParameter paramAsymmetricKeyParameter);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\ExchangePairGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */