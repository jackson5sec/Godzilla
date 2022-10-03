/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public final class AliasToken
/*    */   extends Token
/*    */ {
/*    */   private final String value;
/*    */   
/*    */   public AliasToken(String value, Mark startMark, Mark endMark) {
/* 24 */     super(startMark, endMark);
/* 25 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 29 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 34 */     return Token.ID.Alias;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\tokens\AliasToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */