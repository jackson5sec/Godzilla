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
/*    */ 
/*    */ public final class BlockSequenceStartToken
/*    */   extends Token
/*    */ {
/*    */   public BlockSequenceStartToken(Mark startMark, Mark endMark) {
/* 23 */     super(startMark, endMark);
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 28 */     return Token.ID.BlockSequenceStart;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\tokens\BlockSequenceStartToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */