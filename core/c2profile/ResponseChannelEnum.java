/*    */ package core.c2profile;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ import org.yaml.snakeyaml.annotation.YamlClass;
/*    */ 
/*    */ @YamlClass
/*    */ public enum ResponseChannelEnum {
/*  8 */   RESPONSE_HEADER,
/*  9 */   RESPONSE_COOKIE,
/* 10 */   RESPONSE_RAW_BODY;
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 15 */     switch (this) {
/*    */       case RESPONSE_HEADER:
/* 17 */         return EasyI18N.getI18nString("返回协议头");
/*    */       case RESPONSE_COOKIE:
/* 19 */         EasyI18N.getI18nString("返回Cookie");
/*    */       case RESPONSE_RAW_BODY:
/* 21 */         EasyI18N.getI18nString("返回体"); break;
/*    */     } 
/* 23 */     return EasyI18N.getI18nString("未定义的枚举项");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\ResponseChannelEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */