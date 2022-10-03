package org.bouncycastle.math.ec.endo;

import org.bouncycastle.math.ec.ECPointMap;

public interface ECEndomorphism {
  ECPointMap getPointMap();
  
  boolean hasEfficientPointMap();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\ec\endo\ECEndomorphism.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */