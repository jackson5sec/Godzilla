/*    */ package org.fife.rsta.ac.js.completion;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*    */ import org.fife.rsta.ac.js.IconFactory;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ 
/*    */ 
/*    */ public class JSConstructorCompletion
/*    */   extends JSFunctionCompletion
/*    */ {
/*    */   public JSConstructorCompletion(CompletionProvider provider, MethodInfo method) {
/* 13 */     super(provider, method);
/*    */   }
/*    */ 
/*    */   
/*    */   public Icon getIcon() {
/* 18 */     return IconFactory.getIcon("function");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSConstructorCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */