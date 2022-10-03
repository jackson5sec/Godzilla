package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.x509.ReasonFlags;

class ReasonsMask {
  private int _reasons;
  
  static final ReasonsMask allReasons = new ReasonsMask(33023);
  
  ReasonsMask(ReasonFlags paramReasonFlags) {
    this._reasons = paramReasonFlags.intValue();
  }
  
  private ReasonsMask(int paramInt) {
    this._reasons = paramInt;
  }
  
  ReasonsMask() {
    this(0);
  }
  
  void addReasons(ReasonsMask paramReasonsMask) {
    this._reasons |= paramReasonsMask.getReasons();
  }
  
  boolean isAllReasons() {
    return (this._reasons == allReasons._reasons);
  }
  
  ReasonsMask intersect(ReasonsMask paramReasonsMask) {
    ReasonsMask reasonsMask = new ReasonsMask();
    reasonsMask.addReasons(new ReasonsMask(this._reasons & paramReasonsMask.getReasons()));
    return reasonsMask;
  }
  
  boolean hasNewReasons(ReasonsMask paramReasonsMask) {
    return ((this._reasons | paramReasonsMask.getReasons() ^ this._reasons) != 0);
  }
  
  int getReasons() {
    return this._reasons;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\provider\ReasonsMask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */