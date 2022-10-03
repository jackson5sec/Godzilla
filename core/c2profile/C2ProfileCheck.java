/*     */ package core.c2profile;
/*     */ 
/*     */ import core.c2profile.exception.UnsupportedOperationException;
/*     */ import java.io.InputStream;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.Yaml;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ 
/*     */ 
/*     */ public class C2ProfileCheck
/*     */ {
/*     */   public static boolean check(InputStream yamlStream) {
/*  14 */     DumperOptions dumperOptions = new DumperOptions();
/*  15 */     dumperOptions.setProcessComments(true);
/*  16 */     dumperOptions.setPrettyFlow(true);
/*  17 */     Yaml yaml = new Yaml(dumperOptions);
/*  18 */     yaml.setBeanAccess(BeanAccess.FIELD);
/*  19 */     C2Profile c2Profile = (C2Profile)yaml.loadAs(yamlStream, C2Profile.class);
/*  20 */     C2ProfileContext ctx = new C2ProfileContext();
/*  21 */     ctx.c2Profile = c2Profile;
/*  22 */     loadRequstChannel(ctx);
/*  23 */     loadResponseChannel(ctx);
/*  24 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadRequstChannel(C2ProfileContext ctx) throws UnsupportedOperationException {
/*  29 */     AtomicBoolean flag = new AtomicBoolean(false);
/*  30 */     ctx.c2Profile.request.requestUrlParameters.forEach((k, v) -> {
/*     */           if ("@@@CHANNEL".equalsIgnoreCase(v)) {
/*     */             RequestChannelType _r = new RequestChannelType(RequestChannelEnum.REQUEST_URI_PARAMETER, k);
/*     */             
/*     */             if (ctx.requestChannelType != null) {
/*     */               throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */             }
/*     */             ctx.requestChannelType = _r;
/*     */             flag.set(true);
/*     */           } 
/*     */         });
/*  41 */     if ("@@@CHANNEL".equalsIgnoreCase(ctx.c2Profile.request.requestQueryString)) {
/*  42 */       RequestChannelType _r = new RequestChannelType(RequestChannelEnum.REQUEST_QUERY_STRING, null);
/*  43 */       if (ctx.requestChannelType != null) {
/*  44 */         throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */       }
/*  46 */       ctx.requestChannelType = _r;
/*  47 */       flag.set(true);
/*     */     } 
/*     */     
/*  50 */     ctx.c2Profile.request.requestHeaders.forEach((k, v) -> {
/*     */           if ("@@@CHANNEL".equalsIgnoreCase(v)) {
/*     */             RequestChannelType _r = new RequestChannelType(RequestChannelEnum.REQUEST_HEADER, k);
/*     */             
/*     */             if (ctx.requestChannelType != null) {
/*     */               throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */             }
/*     */             ctx.requestChannelType = _r;
/*     */             flag.set(true);
/*     */           } 
/*     */         });
/*  61 */     ctx.c2Profile.request.requestCookies.forEach((k, v) -> {
/*     */           if ("@@@CHANNEL".equalsIgnoreCase(v)) {
/*     */             RequestChannelType _r = new RequestChannelType(RequestChannelEnum.REQUEST_COOKIE, k);
/*     */             
/*     */             if (ctx.requestChannelType != null) {
/*     */               throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */             }
/*     */             ctx.requestChannelType = _r;
/*     */             flag.set(true);
/*     */           } 
/*     */         });
/*  72 */     ctx.c2Profile.request.requestFormParameters.forEach((k, v) -> {
/*     */           if ("@@@CHANNEL".equalsIgnoreCase(v)) {
/*     */             RequestChannelType _r = new RequestChannelType(RequestChannelEnum.REQUEST_POST_FORM_PARAMETER, k);
/*     */             
/*     */             if (ctx.requestChannelType != null) {
/*     */               throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */             }
/*     */             ctx.requestChannelType = _r;
/*     */             flag.set(true);
/*     */           } 
/*     */         });
/*  83 */     if ("@@@CHANNEL".equalsIgnoreCase(ctx.c2Profile.request.requestMiddleBody)) {
/*  84 */       RequestChannelType _r = new RequestChannelType(RequestChannelEnum.REQUEST_RAW_BODY, null);
/*  85 */       if (ctx.requestChannelType != null) {
/*  86 */         throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */       }
/*  88 */       ctx.requestChannelType = _r;
/*  89 */       flag.set(true);
/*     */     } 
/*     */     
/*  92 */     if (!ctx.c2Profile.request.enabledRequestBody && (
/*  93 */       RequestChannelEnum.REQUEST_RAW_BODY == ctx.requestChannelType.requestChannelEnum || RequestChannelEnum.REQUEST_POST_FORM_PARAMETER == ctx.requestChannelType.requestChannelEnum)) {
/*  94 */       throw new UnsupportedOperationException("信道在请求Body内 但enabledRequestBody并未开启");
/*     */     }
/*     */ 
/*     */     
/*  98 */     if (!flag.get()) {
/*  99 */       throw new UnsupportedOperationException("未定义请求信道");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void loadResponseChannel(C2ProfileContext ctx) throws UnsupportedOperationException {
/* 104 */     AtomicBoolean flag = new AtomicBoolean(false);
/*     */     
/* 106 */     ctx.c2Profile.response.responseHeaders.forEach((k, v) -> {
/*     */           if ("@@@CHANNEL".equalsIgnoreCase(v)) {
/*     */             ResponseChannelType _r = new ResponseChannelType(ResponseChannelEnum.RESPONSE_HEADER, k);
/*     */             
/*     */             if (ctx.responseChannelType != null) {
/*     */               throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */             }
/*     */             ctx.responseChannelType = new ResponseChannelType(ResponseChannelEnum.RESPONSE_HEADER, k);
/*     */             flag.set(true);
/*     */           } 
/*     */         });
/* 117 */     ctx.c2Profile.response.responseCookies.forEach((k, v) -> {
/*     */           if ("@@@CHANNEL".equalsIgnoreCase(v)) {
/*     */             ResponseChannelType _r = new ResponseChannelType(ResponseChannelEnum.RESPONSE_COOKIE, k);
/*     */             
/*     */             if (ctx.responseChannelType != null) {
/*     */               throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */             }
/*     */             ctx.responseChannelType = _r;
/*     */             flag.set(true);
/*     */           } 
/*     */         });
/* 128 */     if ("@@@CHANNEL".equalsIgnoreCase(ctx.c2Profile.response.responseMiddleBody)) {
/* 129 */       ResponseChannelType _r = new ResponseChannelType(ResponseChannelEnum.RESPONSE_RAW_BODY, null);
/* 130 */       if (ctx.responseChannelType != null) {
/* 131 */         throw new UnsupportedOperationException("信道重复定义 已有%s 重复%s", new Object[] { ctx.requestChannelType, _r });
/*     */       }
/* 133 */       ctx.responseChannelType = _r;
/* 134 */       flag.set(true);
/*     */     } 
/*     */     
/* 137 */     if (!flag.get())
/* 138 */       throw new UnsupportedOperationException("未定义请求信道"); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\C2ProfileCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */