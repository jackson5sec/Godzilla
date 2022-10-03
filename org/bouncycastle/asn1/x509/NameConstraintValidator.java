package org.bouncycastle.asn1.x509;

public interface NameConstraintValidator {
  void checkPermitted(GeneralName paramGeneralName) throws NameConstraintValidatorException;
  
  void checkExcluded(GeneralName paramGeneralName) throws NameConstraintValidatorException;
  
  void intersectPermittedSubtree(GeneralSubtree paramGeneralSubtree);
  
  void intersectPermittedSubtree(GeneralSubtree[] paramArrayOfGeneralSubtree);
  
  void intersectEmptyPermittedSubtree(int paramInt);
  
  void addExcludedSubtree(GeneralSubtree paramGeneralSubtree);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\x509\NameConstraintValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */