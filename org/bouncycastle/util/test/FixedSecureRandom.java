package org.bouncycastle.util.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.security.SecureRandom;
import org.bouncycastle.util.Pack;
import org.bouncycastle.util.encoders.Hex;

public class FixedSecureRandom extends SecureRandom {
  private static java.math.BigInteger REGULAR = new java.math.BigInteger("01020304ffffffff0506070811111111", 16);
  
  private static java.math.BigInteger ANDROID = new java.math.BigInteger("1111111105060708ffffffff01020304", 16);
  
  private static java.math.BigInteger CLASSPATH = new java.math.BigInteger("3020104ffffffff05060708111111", 16);
  
  private static final boolean isAndroidStyle;
  
  private static final boolean isClasspathStyle;
  
  private static final boolean isRegularStyle;
  
  private byte[] _data;
  
  private int _index;
  
  public FixedSecureRandom(byte[] paramArrayOfbyte) {
    this(new Source[] { new Data(paramArrayOfbyte) });
  }
  
  public FixedSecureRandom(byte[][] paramArrayOfbyte) {
    this((Source[])buildDataArray(paramArrayOfbyte));
  }
  
  private static Data[] buildDataArray(byte[][] paramArrayOfbyte) {
    Data[] arrayOfData = new Data[paramArrayOfbyte.length];
    for (byte b = 0; b != paramArrayOfbyte.length; b++)
      arrayOfData[b] = new Data(paramArrayOfbyte[b]); 
    return arrayOfData;
  }
  
  public FixedSecureRandom(Source[] paramArrayOfSource) {
    super(null, new DummyProvider());
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    if (isRegularStyle) {
      if (isClasspathStyle) {
        for (byte b = 0; b != paramArrayOfSource.length; b++) {
          try {
            if (paramArrayOfSource[b] instanceof BigInteger) {
              byte[] arrayOfByte = (paramArrayOfSource[b]).data;
              int i = arrayOfByte.length - arrayOfByte.length % 4;
              int j;
              for (j = arrayOfByte.length - i - 1; j >= 0; j--)
                byteArrayOutputStream.write(arrayOfByte[j]); 
              for (j = arrayOfByte.length - i; j < arrayOfByte.length; j += 4)
                byteArrayOutputStream.write(arrayOfByte, j, 4); 
            } else {
              byteArrayOutputStream.write((paramArrayOfSource[b]).data);
            } 
          } catch (IOException iOException) {
            throw new IllegalArgumentException("can't save value source.");
          } 
        } 
      } else {
        for (byte b = 0; b != paramArrayOfSource.length; b++) {
          try {
            byteArrayOutputStream.write((paramArrayOfSource[b]).data);
          } catch (IOException iOException) {
            throw new IllegalArgumentException("can't save value source.");
          } 
        } 
      } 
    } else if (isAndroidStyle) {
      for (byte b = 0; b != paramArrayOfSource.length; b++) {
        try {
          if (paramArrayOfSource[b] instanceof BigInteger) {
            byte[] arrayOfByte = (paramArrayOfSource[b]).data;
            int i = arrayOfByte.length - arrayOfByte.length % 4;
            byte b1;
            for (b1 = 0; b1 < i; b1 += 4)
              byteArrayOutputStream.write(arrayOfByte, arrayOfByte.length - b1 + 4, 4); 
            if (arrayOfByte.length - i != 0)
              for (b1 = 0; b1 != 4 - arrayOfByte.length - i; b1++)
                byteArrayOutputStream.write(0);  
            for (b1 = 0; b1 != arrayOfByte.length - i; b1++)
              byteArrayOutputStream.write(arrayOfByte[i + b1]); 
          } else {
            byteArrayOutputStream.write((paramArrayOfSource[b]).data);
          } 
        } catch (IOException iOException) {
          throw new IllegalArgumentException("can't save value source.");
        } 
      } 
    } else {
      throw new IllegalStateException("Unrecognized BigInteger implementation");
    } 
    this._data = byteArrayOutputStream.toByteArray();
  }
  
  public void nextBytes(byte[] paramArrayOfbyte) {
    System.arraycopy(this._data, this._index, paramArrayOfbyte, 0, paramArrayOfbyte.length);
    this._index += paramArrayOfbyte.length;
  }
  
  public byte[] generateSeed(int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    nextBytes(arrayOfByte);
    return arrayOfByte;
  }
  
  public int nextInt() {
    int i = 0;
    i |= nextValue() << 24;
    i |= nextValue() << 16;
    i |= nextValue() << 8;
    i |= nextValue();
    return i;
  }
  
  public long nextLong() {
    long l = 0L;
    l |= nextValue() << 56L;
    l |= nextValue() << 48L;
    l |= nextValue() << 40L;
    l |= nextValue() << 32L;
    l |= nextValue() << 24L;
    l |= nextValue() << 16L;
    l |= nextValue() << 8L;
    l |= nextValue();
    return l;
  }
  
  public boolean isExhausted() {
    return (this._index == this._data.length);
  }
  
  private int nextValue() {
    return this._data[this._index++] & 0xFF;
  }
  
  private static byte[] expandToBitLength(int paramInt, byte[] paramArrayOfbyte) {
    if ((paramInt + 7) / 8 > paramArrayOfbyte.length) {
      byte[] arrayOfByte = new byte[(paramInt + 7) / 8];
      System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, arrayOfByte.length - paramArrayOfbyte.length, paramArrayOfbyte.length);
      if (isAndroidStyle && paramInt % 8 != 0) {
        int i = Pack.bigEndianToInt(arrayOfByte, 0);
        Pack.intToBigEndian(i << 8 - paramInt % 8, arrayOfByte, 0);
      } 
      return arrayOfByte;
    } 
    if (isAndroidStyle && paramInt < paramArrayOfbyte.length * 8 && paramInt % 8 != 0) {
      int i = Pack.bigEndianToInt(paramArrayOfbyte, 0);
      Pack.intToBigEndian(i << 8 - paramInt % 8, paramArrayOfbyte, 0);
    } 
    return paramArrayOfbyte;
  }
  
  static {
    java.math.BigInteger bigInteger1 = new java.math.BigInteger(128, new RandomChecker());
    java.math.BigInteger bigInteger2 = new java.math.BigInteger(120, new RandomChecker());
    isAndroidStyle = bigInteger1.equals(ANDROID);
    isRegularStyle = bigInteger1.equals(REGULAR);
    isClasspathStyle = bigInteger2.equals(CLASSPATH);
  }
  
  public static class BigInteger extends Source {
    public BigInteger(byte[] param1ArrayOfbyte) {
      super(param1ArrayOfbyte);
    }
    
    public BigInteger(int param1Int, byte[] param1ArrayOfbyte) {
      super(FixedSecureRandom.expandToBitLength(param1Int, param1ArrayOfbyte));
    }
    
    public BigInteger(String param1String) {
      this(Hex.decode(param1String));
    }
    
    public BigInteger(int param1Int, String param1String) {
      super(FixedSecureRandom.expandToBitLength(param1Int, Hex.decode(param1String)));
    }
  }
  
  public static class Data extends Source {
    public Data(byte[] param1ArrayOfbyte) {
      super(param1ArrayOfbyte);
    }
  }
  
  private static class DummyProvider extends Provider {
    DummyProvider() {
      super("BCFIPS_FIXED_RNG", 1.0D, "BCFIPS Fixed Secure Random Provider");
    }
  }
  
  private static class RandomChecker extends SecureRandom {
    byte[] data = Hex.decode("01020304ffffffff0506070811111111");
    
    int index = 0;
    
    RandomChecker() {
      super(null, new FixedSecureRandom.DummyProvider());
    }
    
    public void nextBytes(byte[] param1ArrayOfbyte) {
      System.arraycopy(this.data, this.index, param1ArrayOfbyte, 0, param1ArrayOfbyte.length);
      this.index += param1ArrayOfbyte.length;
    }
  }
  
  public static class Source {
    byte[] data;
    
    Source(byte[] param1ArrayOfbyte) {
      this.data = param1ArrayOfbyte;
    }
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\test\FixedSecureRandom.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */