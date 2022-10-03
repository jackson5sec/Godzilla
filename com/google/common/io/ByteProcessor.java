package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;

@Beta
@GwtIncompatible
public interface ByteProcessor<T> {
  @CanIgnoreReturnValue
  boolean processBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  T getResult();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\ByteProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */