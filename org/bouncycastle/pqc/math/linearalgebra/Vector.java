package org.bouncycastle.pqc.math.linearalgebra;

public abstract class Vector {
  protected int length;
  
  public final int getLength() {
    return this.length;
  }
  
  public abstract byte[] getEncoded();
  
  public abstract boolean isZero();
  
  public abstract Vector add(Vector paramVector);
  
  public abstract Vector multiply(Permutation paramPermutation);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
  
  public abstract String toString();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\math\linearalgebra\Vector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */