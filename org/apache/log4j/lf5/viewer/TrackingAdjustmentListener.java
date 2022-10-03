/*    */ package org.apache.log4j.lf5.viewer;
/*    */ 
/*    */ import java.awt.Adjustable;
/*    */ import java.awt.event.AdjustmentEvent;
/*    */ import java.awt.event.AdjustmentListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TrackingAdjustmentListener
/*    */   implements AdjustmentListener
/*    */ {
/* 46 */   protected int _lastMaximum = -1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void adjustmentValueChanged(AdjustmentEvent e) {
/* 61 */     Adjustable bar = e.getAdjustable();
/* 62 */     int currentMaximum = bar.getMaximum();
/* 63 */     if (bar.getMaximum() == this._lastMaximum) {
/*    */       return;
/*    */     }
/* 66 */     int bottom = bar.getValue() + bar.getVisibleAmount();
/*    */     
/* 68 */     if (bottom + bar.getUnitIncrement() >= this._lastMaximum) {
/* 69 */       bar.setValue(bar.getMaximum());
/*    */     }
/* 71 */     this._lastMaximum = currentMaximum;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\lf5\viewer\TrackingAdjustmentListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */