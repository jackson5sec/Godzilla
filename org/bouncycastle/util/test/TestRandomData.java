package org.bouncycastle.util.test;

import org.bouncycastle.util.encoders.Hex;

public class TestRandomData extends FixedSecureRandom {
  public TestRandomData(String paramString) {
    super(new FixedSecureRandom.Source[] { new FixedSecureRandom.Data(Hex.decode(paramString)) });
  }
  
  public TestRandomData(byte[] paramArrayOfbyte) {
    super(new FixedSecureRandom.Source[] { new FixedSecureRandom.Data(paramArrayOfbyte) });
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastl\\util\test\TestRandomData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */