package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
interface PatternCompiler {
  CommonPattern compile(String paramString);
  
  boolean isPcreLike();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\PatternCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */