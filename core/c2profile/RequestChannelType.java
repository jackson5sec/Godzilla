/*    */ package core.c2profile;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ 
/*    */ public class RequestChannelType {
/*    */   public RequestChannelEnum requestChannelEnum;
/*    */   public String name;
/*    */   
/*    */   public RequestChannelType(RequestChannelEnum requestChannelEnum, String name) {
/* 10 */     this.requestChannelEnum = requestChannelEnum;
/* 11 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 16 */     return EasyI18N.getI18nString("通道位置: %s Name: %s", new Object[] { this.requestChannelEnum, this.name });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\RequestChannelType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */