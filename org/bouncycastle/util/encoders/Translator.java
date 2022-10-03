package org.bouncycastle.util.encoders;

public interface Translator {
  int getEncodedBlockSize();
  
  int encode(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3);
  
  int getDecodedBlockSize();
  
  int decode(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\encoders\Translator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */