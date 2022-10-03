/*    */ package core.c2profile;
/*    */ 
/*    */ import core.EasyI18N;
/*    */ import org.yaml.snakeyaml.annotation.YamlClass;
/*    */ 
/*    */ @YamlClass
/*    */ public enum RequestChannelEnum {
/*  8 */   REQUEST_QUERY_STRING,
/*  9 */   REQUEST_URI_PARAMETER,
/* 10 */   REQUEST_HEADER,
/* 11 */   REQUEST_COOKIE,
/* 12 */   REQUEST_RAW_BODY,
/* 13 */   REQUEST_POST_FORM_PARAMETER;
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 18 */     switch (this) {
/*    */       case REQUEST_QUERY_STRING:
/* 20 */         return EasyI18N.getI18nString("请求查询字符串");
/*    */       case REQUEST_URI_PARAMETER:
/* 22 */         return EasyI18N.getI18nString("请求URI参数");
/*    */       case REQUEST_HEADER:
/* 24 */         return EasyI18N.getI18nString("请求协议头");
/*    */       case REQUEST_COOKIE:
/* 26 */         return EasyI18N.getI18nString("请求Cookie");
/*    */       case REQUEST_RAW_BODY:
/* 28 */         return EasyI18N.getI18nString("请求体");
/*    */       case REQUEST_POST_FORM_PARAMETER:
/* 30 */         return EasyI18N.getI18nString("请求表单参数");
/*    */     } 
/* 32 */     return EasyI18N.getI18nString("未定义的枚举项");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\RequestChannelEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */