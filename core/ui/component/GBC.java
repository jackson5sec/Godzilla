/*    */ package core.ui.component;
/*    */ 
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ 
/*    */ public class GBC
/*    */   extends GridBagConstraints
/*    */ {
/*    */   public GBC(int gridx, int gridy) {
/* 10 */     this.gridx = gridx;
/* 11 */     this.gridy = gridy;
/*    */   }
/*    */   
/*    */   public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
/* 15 */     this.gridx = gridx;
/* 16 */     this.gridy = gridy;
/* 17 */     this.gridwidth = gridwidth;
/* 18 */     this.gridheight = gridheight;
/*    */   }
/*    */   
/*    */   public GBC setFill(int fill) {
/* 22 */     this.fill = fill;
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   public GBC setWeight(double weightx, double weighty) {
/* 27 */     this.weightx = weightx;
/* 28 */     this.weighty = weighty;
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   public GBC setAnchor(int anchor) {
/* 33 */     this.anchor = anchor;
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public GBC setInsets(int top, int left, int bottom, int right) {
/* 38 */     this.insets = new Insets(top, left, bottom, right);
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public GBC setIpad(int ipadx, int ipady) {
/* 43 */     this.ipadx = ipadx;
/* 44 */     this.ipady = ipady;
/* 45 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\GBC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */