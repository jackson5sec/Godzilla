/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.CtField;
/*    */ import javassist.compiler.CompileError;
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
/*    */ public class Member
/*    */   extends Symbol
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private CtField field;
/*    */   
/*    */   public Member(String name) {
/* 33 */     super(name);
/* 34 */     this.field = null;
/*    */   }
/*    */   public void setField(CtField f) {
/* 37 */     this.field = f;
/*    */   } public CtField getField() {
/* 39 */     return this.field;
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 42 */     v.atMember(this);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\ast\Member.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */