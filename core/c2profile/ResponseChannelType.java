/*    */ package core.c2profile;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ 
/*    */ public class ResponseChannelType {
/*    */   public ResponseChannelEnum responseChannelEnum;
/*    */   public String name;
/*    */   
/*    */   public ResponseChannelType(ResponseChannelEnum responseChannelEnum, String name) {
/* 10 */     this.responseChannelEnum = responseChannelEnum;
/* 11 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 16 */     return EasyI18N.getI18nString("通道位置: %s Name: %s", new Object[] { this.responseChannelEnum, this.name });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\ResponseChannelType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */