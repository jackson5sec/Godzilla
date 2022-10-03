/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Set;
/*     */ import org.fife.ui.rsyntaxtextarea.modes.PlainTextTokenMaker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TokenMakerFactory
/*     */ {
/*     */   public static final String PROPERTY_DEFAULT_TOKEN_MAKER_FACTORY = "TokenMakerFactory";
/*     */   private static TokenMakerFactory DEFAULT_INSTANCE;
/*     */   
/*     */   public static synchronized TokenMakerFactory getDefaultInstance() {
/*  46 */     if (DEFAULT_INSTANCE == null) {
/*     */       String clazz;
/*     */       try {
/*  49 */         clazz = System.getProperty("TokenMakerFactory");
/*  50 */       } catch (AccessControlException ace) {
/*  51 */         clazz = null;
/*     */       } 
/*  53 */       if (clazz == null) {
/*  54 */         clazz = "org.fife.ui.rsyntaxtextarea.DefaultTokenMakerFactory";
/*     */       }
/*     */       
/*     */       try {
/*  58 */         DEFAULT_INSTANCE = Class.forName(clazz).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*  59 */       } catch (RuntimeException re) {
/*  60 */         throw re;
/*  61 */       } catch (Exception e) {
/*  62 */         e.printStackTrace();
/*  63 */         throw new InternalError("Cannot find TokenMakerFactory: " + clazz);
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     return DEFAULT_INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TokenMaker getTokenMaker(String key) {
/*     */     PlainTextTokenMaker plainTextTokenMaker;
/*  79 */     TokenMaker tm = getTokenMakerImpl(key);
/*  80 */     if (tm == null) {
/*  81 */       plainTextTokenMaker = new PlainTextTokenMaker();
/*     */     }
/*  83 */     return (TokenMaker)plainTextTokenMaker;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract TokenMaker getTokenMakerImpl(String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Set<String> keySet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void setDefaultInstance(TokenMakerFactory tmf) {
/* 117 */     if (tmf == null) {
/* 118 */       throw new IllegalArgumentException("tmf cannot be null");
/*     */     }
/* 120 */     DEFAULT_INSTANCE = tmf;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\TokenMakerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */