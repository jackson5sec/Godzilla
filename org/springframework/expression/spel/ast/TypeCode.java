/*     */ package org.springframework.expression.spel.ast;
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
/*     */ public enum TypeCode
/*     */ {
/*  30 */   OBJECT(Object.class),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   BOOLEAN(boolean.class),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   BYTE(byte.class),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   CHAR(char.class),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   DOUBLE(double.class),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   FLOAT(float.class),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   INT(int.class),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   LONG(long.class),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   SHORT(short.class);
/*     */ 
/*     */   
/*     */   private Class<?> type;
/*     */ 
/*     */   
/*     */   TypeCode(Class<?> type) {
/*  77 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType() {
/*  82 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public static TypeCode forName(String name) {
/*  87 */     TypeCode[] tcs = values();
/*  88 */     for (int i = 1; i < tcs.length; i++) {
/*  89 */       if (tcs[i].name().equalsIgnoreCase(name)) {
/*  90 */         return tcs[i];
/*     */       }
/*     */     } 
/*  93 */     return OBJECT;
/*     */   }
/*     */   
/*     */   public static TypeCode forClass(Class<?> clazz) {
/*  97 */     TypeCode[] allValues = values();
/*  98 */     for (TypeCode typeCode : allValues) {
/*  99 */       if (clazz == typeCode.getType()) {
/* 100 */         return typeCode;
/*     */       }
/*     */     } 
/* 103 */     return OBJECT;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\TypeCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */