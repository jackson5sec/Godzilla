/*     */ package org.fife.ui.rsyntaxtextarea.folding;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FoldCollapser
/*     */ {
/*     */   private List<Integer> typesToCollapse;
/*     */   
/*     */   public FoldCollapser() {
/*  32 */     this(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FoldCollapser(int typeToCollapse) {
/*  44 */     this.typesToCollapse = new ArrayList<>(3);
/*  45 */     addTypeToCollapse(typeToCollapse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTypeToCollapse(int typeToCollapse) {
/*  55 */     this.typesToCollapse.add(Integer.valueOf(typeToCollapse));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void collapseFolds(FoldManager fm) {
/*  65 */     for (int i = 0; i < fm.getFoldCount(); i++) {
/*  66 */       Fold fold = fm.getFold(i);
/*  67 */       collapseImpl(fold);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collapseImpl(Fold fold) {
/*  80 */     if (getShouldCollapse(fold)) {
/*  81 */       fold.setCollapsed(true);
/*     */     }
/*  83 */     for (int i = 0; i < fold.getChildCount(); i++) {
/*  84 */       collapseImpl(fold.getChild(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShouldCollapse(Fold fold) {
/*  96 */     int type = fold.getFoldType();
/*  97 */     for (Integer typeToCollapse : this.typesToCollapse) {
/*  98 */       if (type == typeToCollapse.intValue()) {
/*  99 */         return true;
/*     */       }
/*     */     } 
/* 102 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\FoldCollapser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */