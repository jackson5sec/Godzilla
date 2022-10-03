/*    */ package org.fife.rsta.ac.java;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.Icon;
/*    */ import org.fife.rsta.ac.java.rjc.ast.LocalVariable;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
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
/*    */ class LocalVariableCompletion
/*    */   extends AbstractJavaSourceCompletion
/*    */ {
/*    */   private LocalVariable localVar;
/*    */   private static final int RELEVANCE = 4;
/*    */   
/*    */   public LocalVariableCompletion(CompletionProvider provider, LocalVariable localVar) {
/* 32 */     super(provider, localVar.getName());
/* 33 */     this.localVar = localVar;
/* 34 */     setRelevance(4);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 40 */     return (obj instanceof LocalVariableCompletion && ((LocalVariableCompletion)obj)
/* 41 */       .getReplacementText()
/* 42 */       .equals(getReplacementText()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon() {
/* 48 */     return IconFactory.get().getIcon("localVariableIcon");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getToolTipText() {
/* 54 */     return this.localVar.getType() + " " + this.localVar.getName();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 60 */     return getReplacementText().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void rendererText(Graphics g, int x, int y, boolean selected) {
/* 66 */     StringBuilder sb = new StringBuilder();
/* 67 */     sb.append(this.localVar.getName());
/* 68 */     sb.append(" : ");
/* 69 */     sb.append(this.localVar.getType());
/* 70 */     g.drawString(sb.toString(), x, y);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\LocalVariableCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */