package org.bouncycastle.asn1.eac;

import java.util.Hashtable;

public class BidirectionalMap extends Hashtable {
  private static final long serialVersionUID = -7457289971962812909L;
  
  Hashtable reverseMap = new Hashtable<Object, Object>();
  
  public Object getReverse(Object paramObject) {
    return this.reverseMap.get(paramObject);
  }
  
  public Object put(Object paramObject1, Object paramObject2) {
    this.reverseMap.put(paramObject2, paramObject1);
    return super.put(paramObject1, paramObject2);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\asn1\eac\BidirectionalMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */