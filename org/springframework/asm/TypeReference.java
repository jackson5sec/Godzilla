/*     */ package org.springframework.asm;
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
/*     */ public class TypeReference
/*     */ {
/*     */   public static final int CLASS_TYPE_PARAMETER = 0;
/*     */   public static final int METHOD_TYPE_PARAMETER = 1;
/*     */   public static final int CLASS_EXTENDS = 16;
/*     */   public static final int CLASS_TYPE_PARAMETER_BOUND = 17;
/*     */   public static final int METHOD_TYPE_PARAMETER_BOUND = 18;
/*     */   public static final int FIELD = 19;
/*     */   public static final int METHOD_RETURN = 20;
/*     */   public static final int METHOD_RECEIVER = 21;
/*     */   public static final int METHOD_FORMAL_PARAMETER = 22;
/*     */   public static final int THROWS = 23;
/*     */   public static final int LOCAL_VARIABLE = 64;
/*     */   public static final int RESOURCE_VARIABLE = 65;
/*     */   public static final int EXCEPTION_PARAMETER = 66;
/*     */   public static final int INSTANCEOF = 67;
/*     */   public static final int NEW = 68;
/*     */   public static final int CONSTRUCTOR_REFERENCE = 69;
/*     */   public static final int METHOD_REFERENCE = 70;
/*     */   public static final int CAST = 71;
/*     */   public static final int CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
/*     */   public static final int METHOD_INVOCATION_TYPE_ARGUMENT = 73;
/*     */   public static final int CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
/*     */   public static final int METHOD_REFERENCE_TYPE_ARGUMENT = 75;
/*     */   private final int targetTypeAndInfo;
/*     */   
/*     */   public TypeReference(int typeRef) {
/* 194 */     this.targetTypeAndInfo = typeRef;
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
/*     */   public static TypeReference newTypeReference(int sort) {
/* 206 */     return new TypeReference(sort << 24);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeReference newTypeParameterReference(int sort, int paramIndex) {
/* 217 */     return new TypeReference(sort << 24 | paramIndex << 16);
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
/*     */   public static TypeReference newTypeParameterBoundReference(int sort, int paramIndex, int boundIndex) {
/* 230 */     return new TypeReference(sort << 24 | paramIndex << 16 | boundIndex << 8);
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
/*     */   public static TypeReference newSuperTypeReference(int itfIndex) {
/* 242 */     return new TypeReference(0x10000000 | (itfIndex & 0xFFFF) << 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeReference newFormalParameterReference(int paramIndex) {
/* 252 */     return new TypeReference(0x16000000 | paramIndex << 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeReference newExceptionReference(int exceptionIndex) {
/* 262 */     return new TypeReference(0x17000000 | exceptionIndex << 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeReference newTryCatchReference(int tryCatchBlockIndex) {
/* 273 */     return new TypeReference(0x42000000 | tryCatchBlockIndex << 8);
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
/*     */   public static TypeReference newTypeArgumentReference(int sort, int argIndex) {
/* 287 */     return new TypeReference(sort << 24 | argIndex);
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
/*     */   public int getSort() {
/* 303 */     return this.targetTypeAndInfo >>> 24;
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
/*     */   public int getTypeParameterIndex() {
/* 315 */     return (this.targetTypeAndInfo & 0xFF0000) >> 16;
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
/*     */   public int getTypeParameterBoundIndex() {
/* 327 */     return (this.targetTypeAndInfo & 0xFF00) >> 8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSuperTypeIndex() {
/* 338 */     return (short)((this.targetTypeAndInfo & 0xFFFF00) >> 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFormalParameterIndex() {
/* 348 */     return (this.targetTypeAndInfo & 0xFF0000) >> 16;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExceptionIndex() {
/* 359 */     return (this.targetTypeAndInfo & 0xFFFF00) >> 8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTryCatchBlockIndex() {
/* 370 */     return (this.targetTypeAndInfo & 0xFFFF00) >> 8;
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
/*     */   public int getTypeArgumentIndex() {
/* 382 */     return this.targetTypeAndInfo & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/* 392 */     return this.targetTypeAndInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void putTarget(int targetTypeAndInfo, ByteVector output) {
/* 403 */     switch (targetTypeAndInfo >>> 24) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 22:
/* 407 */         output.putShort(targetTypeAndInfo >>> 16);
/*     */         return;
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/* 412 */         output.putByte(targetTypeAndInfo >>> 24);
/*     */         return;
/*     */       case 71:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/* 419 */         output.putInt(targetTypeAndInfo);
/*     */         return;
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 23:
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 69:
/*     */       case 70:
/* 430 */         output.put12(targetTypeAndInfo >>> 24, (targetTypeAndInfo & 0xFFFF00) >> 8);
/*     */         return;
/*     */     } 
/* 433 */     throw new IllegalArgumentException();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\asm\TypeReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */