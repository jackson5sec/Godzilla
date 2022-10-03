package com.google.common.graph;

import com.google.common.annotations.Beta;

@Beta
public interface SuccessorsFunction<N> {
  Iterable<? extends N> successors(N paramN);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\SuccessorsFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */