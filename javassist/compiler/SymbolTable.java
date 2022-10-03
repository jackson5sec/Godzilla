/*    */ package javassist.compiler;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import javassist.compiler.ast.Declarator;
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
/*    */ public final class SymbolTable
/*    */   extends HashMap<String, Declarator>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private SymbolTable parent;
/*    */   
/*    */   public SymbolTable() {
/* 28 */     this((SymbolTable)null);
/*    */   }
/*    */   
/*    */   public SymbolTable(SymbolTable p) {
/* 32 */     this.parent = p;
/*    */   }
/*    */   public SymbolTable getParent() {
/* 35 */     return this.parent;
/*    */   }
/*    */   public Declarator lookup(String name) {
/* 38 */     Declarator found = get(name);
/* 39 */     if (found == null && this.parent != null)
/* 40 */       return this.parent.lookup(name); 
/* 41 */     return found;
/*    */   }
/*    */   
/*    */   public void append(String name, Declarator value) {
/* 45 */     put(name, value);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\SymbolTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */