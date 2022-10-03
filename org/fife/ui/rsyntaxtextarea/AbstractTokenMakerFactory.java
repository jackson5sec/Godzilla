/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public abstract class AbstractTokenMakerFactory
/*     */   extends TokenMakerFactory
/*     */ {
/*     */   private Map<String, Object> tokenMakerMap;
/*     */   
/*     */   protected AbstractTokenMakerFactory() {
/*  38 */     this.tokenMakerMap = new HashMap<>();
/*  39 */     initTokenMakerMap();
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
/*     */   protected TokenMaker getTokenMakerImpl(String key) {
/*  52 */     TokenMakerCreator tmc = (TokenMakerCreator)this.tokenMakerMap.get(key);
/*  53 */     if (tmc != null) {
/*     */       try {
/*  55 */         return tmc.create();
/*  56 */       } catch (RuntimeException re) {
/*  57 */         throw re;
/*  58 */       } catch (Exception e) {
/*  59 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void initTokenMakerMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/*  80 */     return this.tokenMakerMap.keySet();
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
/*     */   public void putMapping(String key, String className) {
/*  93 */     putMapping(key, className, null);
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
/*     */   
/*     */   public void putMapping(String key, String className, ClassLoader cl) {
/* 107 */     this.tokenMakerMap.put(key, new TokenMakerCreator(className, cl));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TokenMakerCreator
/*     */   {
/*     */     private String className;
/*     */     
/*     */     private ClassLoader cl;
/*     */ 
/*     */     
/*     */     public TokenMakerCreator(String className, ClassLoader cl) {
/* 120 */       this.className = className;
/* 121 */       this.cl = (cl != null) ? cl : getClass().getClassLoader();
/*     */     }
/*     */     
/*     */     public TokenMaker create() throws Exception {
/* 125 */       return (TokenMaker)Class.forName(this.className, true, this.cl).newInstance();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\AbstractTokenMakerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */