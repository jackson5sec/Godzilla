package org.bouncycastle.cert.path.validations;

import org.bouncycastle.cert.path.CertPath;

public class CertificatePoliciesValidationBuilder {
  private boolean isExplicitPolicyRequired;
  
  private boolean isAnyPolicyInhibited;
  
  private boolean isPolicyMappingInhibited;
  
  public void setAnyPolicyInhibited(boolean paramBoolean) {
    this.isAnyPolicyInhibited = paramBoolean;
  }
  
  public void setExplicitPolicyRequired(boolean paramBoolean) {
    this.isExplicitPolicyRequired = paramBoolean;
  }
  
  public void setPolicyMappingInhibited(boolean paramBoolean) {
    this.isPolicyMappingInhibited = paramBoolean;
  }
  
  public CertificatePoliciesValidation build(int paramInt) {
    return new CertificatePoliciesValidation(paramInt, this.isExplicitPolicyRequired, this.isAnyPolicyInhibited, this.isPolicyMappingInhibited);
  }
  
  public CertificatePoliciesValidation build(CertPath paramCertPath) {
    return build(paramCertPath.length());
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\path\validations\CertificatePoliciesValidationBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */