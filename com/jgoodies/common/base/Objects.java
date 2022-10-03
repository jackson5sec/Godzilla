/*     */ package com.jgoodies.common.base;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
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
/*     */ public final class Objects
/*     */ {
/*     */   public static <T extends java.io.Serializable> T deepCopy(T original) {
/*  68 */     if (original == null) {
/*  69 */       return null;
/*     */     }
/*     */     try {
/*  72 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/*  73 */       ObjectOutputStream oas = new ObjectOutputStream(baos);
/*  74 */       oas.writeObject(original);
/*  75 */       oas.flush();
/*     */       
/*  77 */       ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
/*  78 */       ObjectInputStream ois = new ObjectInputStream(bais);
/*  79 */       return (T)ois.readObject();
/*  80 */     } catch (Throwable e) {
/*  81 */       throw new RuntimeException("Deep copy failed", e);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equals(Object o1, Object o2) {
/* 105 */     return (o1 == o2 || (o1 != null && o1.equals(o2)));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\base\Objects.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */