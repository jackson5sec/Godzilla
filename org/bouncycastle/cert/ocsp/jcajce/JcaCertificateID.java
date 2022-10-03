package org.bouncycastle.cert.ocsp.jcajce;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.operator.DigestCalculator;

public class JcaCertificateID extends CertificateID {
  public JcaCertificateID(DigestCalculator paramDigestCalculator, X509Certificate paramX509Certificate, BigInteger paramBigInteger) throws OCSPException, CertificateEncodingException {
    super(paramDigestCalculator, (X509CertificateHolder)new JcaX509CertificateHolder(paramX509Certificate), paramBigInteger);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\ocsp\jcajce\JcaCertificateID.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */