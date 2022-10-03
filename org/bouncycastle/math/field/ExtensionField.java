package org.bouncycastle.math.field;

public interface ExtensionField extends FiniteField {
  FiniteField getSubfield();
  
  int getDegree();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\field\ExtensionField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */