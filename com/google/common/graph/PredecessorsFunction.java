package com.google.common.graph;

import com.google.common.annotations.Beta;

@Beta
public interface PredecessorsFunction<N> {
  Iterable<? extends N> predecessors(N paramN);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\graph\PredecessorsFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */