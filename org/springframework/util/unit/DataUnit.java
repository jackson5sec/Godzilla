/*    */ package org.springframework.util.unit;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DataUnit
/*    */ {
/* 47 */   BYTES("B", DataSize.ofBytes(1L)),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   KILOBYTES("KB", DataSize.ofKilobytes(1L)),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   MEGABYTES("MB", DataSize.ofMegabytes(1L)),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   GIGABYTES("GB", DataSize.ofGigabytes(1L)),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   TERABYTES("TB", DataSize.ofTerabytes(1L));
/*    */ 
/*    */   
/*    */   private final String suffix;
/*    */   
/*    */   private final DataSize size;
/*    */ 
/*    */   
/*    */   DataUnit(String suffix, DataSize size) {
/* 76 */     this.suffix = suffix;
/* 77 */     this.size = size;
/*    */   }
/*    */   
/*    */   DataSize size() {
/* 81 */     return this.size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataUnit fromSuffix(String suffix) {
/* 92 */     for (DataUnit candidate : values()) {
/* 93 */       if (candidate.suffix.equals(suffix)) {
/* 94 */         return candidate;
/*    */       }
/*    */     } 
/* 97 */     throw new IllegalArgumentException("Unknown data unit suffix '" + suffix + "'");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\uti\\unit\DataUnit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */