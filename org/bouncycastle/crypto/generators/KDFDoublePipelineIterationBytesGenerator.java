package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.MacDerivationFunction;
import org.bouncycastle.crypto.params.KDFDoublePipelineIterationParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class KDFDoublePipelineIterationBytesGenerator implements MacDerivationFunction {
  private static final BigInteger INTEGER_MAX = BigInteger.valueOf(2147483647L);
  
  private static final BigInteger TWO = BigInteger.valueOf(2L);
  
  private final Mac prf;
  
  private final int h;
  
  private byte[] fixedInputData;
  
  private int maxSizeExcl;
  
  private byte[] ios;
  
  private boolean useCounter;
  
  private int generatedBytes;
  
  private byte[] a;
  
  private byte[] k;
  
  public KDFDoublePipelineIterationBytesGenerator(Mac paramMac) {
    this.prf = paramMac;
    this.h = paramMac.getMacSize();
    this.a = new byte[this.h];
    this.k = new byte[this.h];
  }
  
  public void init(DerivationParameters paramDerivationParameters) {
    if (!(paramDerivationParameters instanceof KDFDoublePipelineIterationParameters))
      throw new IllegalArgumentException("Wrong type of arguments given"); 
    KDFDoublePipelineIterationParameters kDFDoublePipelineIterationParameters = (KDFDoublePipelineIterationParameters)paramDerivationParameters;
    this.prf.init((CipherParameters)new KeyParameter(kDFDoublePipelineIterationParameters.getKI()));
    this.fixedInputData = kDFDoublePipelineIterationParameters.getFixedInputData();
    int i = kDFDoublePipelineIterationParameters.getR();
    this.ios = new byte[i / 8];
    if (kDFDoublePipelineIterationParameters.useCounter()) {
      BigInteger bigInteger = TWO.pow(i).multiply(BigInteger.valueOf(this.h));
      this.maxSizeExcl = (bigInteger.compareTo(INTEGER_MAX) == 1) ? Integer.MAX_VALUE : bigInteger.intValue();
    } else {
      this.maxSizeExcl = Integer.MAX_VALUE;
    } 
    this.useCounter = kDFDoublePipelineIterationParameters.useCounter();
    this.generatedBytes = 0;
  }
  
  public Mac getMac() {
    return this.prf;
  }
  
  public int generateBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws DataLengthException, IllegalArgumentException {
    int i = this.generatedBytes + paramInt2;
    if (i < 0 || i >= this.maxSizeExcl)
      throw new DataLengthException("Current KDFCTR may only be used for " + this.maxSizeExcl + " bytes"); 
    if (this.generatedBytes % this.h == 0)
      generateNext(); 
    int j = paramInt2;
    int k = this.generatedBytes % this.h;
    int m = this.h - this.generatedBytes % this.h;
    int n = Math.min(m, j);
    System.arraycopy(this.k, k, paramArrayOfbyte, paramInt1, n);
    this.generatedBytes += n;
    j -= n;
    for (paramInt1 += n; j > 0; paramInt1 += n) {
      generateNext();
      n = Math.min(this.h, j);
      System.arraycopy(this.k, 0, paramArrayOfbyte, paramInt1, n);
      this.generatedBytes += n;
      j -= n;
    } 
    return paramInt2;
  }
  
  private void generateNext() {
    if (this.generatedBytes == 0) {
      this.prf.update(this.fixedInputData, 0, this.fixedInputData.length);
      this.prf.doFinal(this.a, 0);
    } else {
      this.prf.update(this.a, 0, this.a.length);
      this.prf.doFinal(this.a, 0);
    } 
    this.prf.update(this.a, 0, this.a.length);
    if (this.useCounter) {
      int i = this.generatedBytes / this.h + 1;
      switch (this.ios.length) {
        case 4:
          this.ios[0] = (byte)(i >>> 24);
        case 3:
          this.ios[this.ios.length - 3] = (byte)(i >>> 16);
        case 2:
          this.ios[this.ios.length - 2] = (byte)(i >>> 8);
        case 1:
          this.ios[this.ios.length - 1] = (byte)i;
          break;
        default:
          throw new IllegalStateException("Unsupported size of counter i");
      } 
      this.prf.update(this.ios, 0, this.ios.length);
    } 
    this.prf.update(this.fixedInputData, 0, this.fixedInputData.length);
    this.prf.doFinal(this.k, 0);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\generators\KDFDoublePipelineIterationBytesGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */