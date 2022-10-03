/*     */ package org.fife.rsta.ac.java.rjc.lang;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Type
/*     */ {
/*  37 */   private List<String> identifiers = new ArrayList<>(1);
/*  38 */   private List<List<TypeArgument>> typeArguments = new ArrayList<>(1);
/*     */   
/*     */   private int bracketPairCount;
/*     */   
/*     */   public Type(String identifier) {
/*  43 */     this();
/*  44 */     addIdentifier(identifier, null);
/*     */   }
/*     */   public Type() {}
/*     */   
/*     */   public Type(String identifier, int bracketPairCount) {
/*  49 */     this();
/*  50 */     addIdentifier(identifier, null);
/*  51 */     setBracketPairCount(bracketPairCount);
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
/*     */   public void addIdentifier(String identifier, List<TypeArgument> typeArgs) {
/*  63 */     this.identifiers.add(identifier);
/*  64 */     this.typeArguments.add(typeArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIdentifierCount() {
/*  69 */     return this.identifiers.size();
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
/*     */   public String getName(boolean fullyQualified) {
/*  83 */     return getName(fullyQualified, true);
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
/*     */   public String getName(boolean fullyQualified, boolean addTypeArgs) {
/*  99 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 101 */     int count = this.identifiers.size();
/* 102 */     int start = fullyQualified ? 0 : (count - 1); int i;
/* 103 */     for (i = start; i < count; i++) {
/* 104 */       sb.append(this.identifiers.get(i));
/* 105 */       if (addTypeArgs && this.typeArguments.get(i) != null) {
/* 106 */         List<TypeArgument> typeArgs = this.typeArguments.get(i);
/* 107 */         int typeArgCount = typeArgs.size();
/* 108 */         if (typeArgCount > 0) {
/* 109 */           sb.append('<');
/* 110 */           for (int j = 0; j < typeArgCount; j++) {
/* 111 */             TypeArgument typeArg = typeArgs.get(j);
/*     */             
/* 113 */             sb.append(typeArg.toString());
/* 114 */             if (j < typeArgCount - 1) {
/* 115 */               sb.append(", ");
/*     */             }
/*     */           } 
/* 118 */           sb.append('>');
/*     */         } 
/*     */       } 
/* 121 */       if (i < count - 1) {
/* 122 */         sb.append('.');
/*     */       }
/*     */     } 
/*     */     
/* 126 */     for (i = 0; i < this.bracketPairCount; i++) {
/* 127 */       sb.append("[]");
/*     */     }
/*     */     
/* 130 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TypeArgument> getTypeArguments(int index) {
/* 136 */     return this.typeArguments.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void incrementBracketPairCount(int count) {
/* 145 */     this.bracketPairCount += count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 155 */     return (this.bracketPairCount > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBasicType() {
/* 160 */     boolean basicType = false;
/* 161 */     if (!isArray() && this.identifiers.size() == 1 && this.typeArguments.get(0) == null) {
/* 162 */       String str = this.identifiers.get(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 169 */       basicType = ("byte".equals(str) || "float".equals(str) || "double".equals(str) || "int".equals(str) || "short".equals(str) || "long".equals(str) || "boolean".equals(str));
/*     */     } 
/* 171 */     return basicType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBracketPairCount(int count) {
/* 176 */     this.bracketPairCount = count;
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
/*     */   public String toString() {
/* 189 */     return getName(true);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\lang\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */