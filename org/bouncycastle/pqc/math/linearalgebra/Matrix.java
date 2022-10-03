package org.bouncycastle.pqc.math.linearalgebra;

public abstract class Matrix {
  protected int numRows;
  
  protected int numColumns;
  
  public static final char MATRIX_TYPE_ZERO = 'Z';
  
  public static final char MATRIX_TYPE_UNIT = 'I';
  
  public static final char MATRIX_TYPE_RANDOM_LT = 'L';
  
  public static final char MATRIX_TYPE_RANDOM_UT = 'U';
  
  public static final char MATRIX_TYPE_RANDOM_REGULAR = 'R';
  
  public int getNumRows() {
    return this.numRows;
  }
  
  public int getNumColumns() {
    return this.numColumns;
  }
  
  public abstract byte[] getEncoded();
  
  public abstract Matrix computeInverse();
  
  public abstract boolean isZero();
  
  public abstract Matrix rightMultiply(Matrix paramMatrix);
  
  public abstract Matrix rightMultiply(Permutation paramPermutation);
  
  public abstract Vector leftMultiply(Vector paramVector);
  
  public abstract Vector rightMultiply(Vector paramVector);
  
  public abstract String toString();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\math\linearalgebra\Matrix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */