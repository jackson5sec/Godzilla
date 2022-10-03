/*     */ package org.fife.rsta.ac.java.classreader.attributes;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Signature
/*     */   extends AttributeInfo
/*     */ {
/*     */   private String signature;
/*     */   
/*     */   public Signature(ClassFile cf, String signature) {
/*  37 */     super(cf);
/*  38 */     this.signature = signature;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getClassParamTypes() {
/*  44 */     List<String> types = null;
/*     */     
/*  46 */     if (this.signature != null && this.signature.startsWith("<")) {
/*     */       
/*  48 */       types = new ArrayList<>(1);
/*  49 */       int afterMatchingGT = skipLtGt(this.signature, 1);
/*     */ 
/*     */ 
/*     */       
/*  53 */       String temp = this.signature.substring(1, afterMatchingGT - 1);
/*  54 */       int offs = 0;
/*  55 */       int colon = temp.indexOf(':', offs);
/*  56 */       while (offs < temp.length() && colon > -1) {
/*  57 */         String ident = temp.substring(offs, colon);
/*  58 */         int colonCount = 1;
/*  59 */         char ch = temp.charAt(colon + colonCount);
/*  60 */         if (ch == ':') {
/*  61 */           colonCount++;
/*  62 */           ch = temp.charAt(colon + colonCount);
/*     */         } 
/*  64 */         if (ch == 'L') {
/*  65 */           int semicolon = temp.indexOf(';', colon + colonCount + 1);
/*  66 */           if (semicolon > -1) {
/*     */ 
/*     */             
/*  69 */             types.add(ident);
/*  70 */             offs = semicolon + 1;
/*  71 */             colon = temp.indexOf(':', offs);
/*     */             continue;
/*     */           } 
/*  74 */           System.err.println("WARN: Can't parse signature (1): " + this.signature);
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/*  79 */         System.err.println("WARN: Can't parse signature (2): " + this.signature);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  85 */     return types;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int skipLtGt(String str, int start) {
/*  92 */     int ltCount = 1;
/*  93 */     int offs = start;
/*     */     
/*  95 */     while (offs < str.length() && ltCount > 0) {
/*  96 */       char ch = str.charAt(offs++);
/*  97 */       switch (ch) {
/*     */         case '<':
/*  99 */           ltCount++;
/*     */         
/*     */         case '>':
/* 102 */           ltCount--;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 107 */     return offs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getMethodParamTypes(MethodInfo mi, ClassFile cf, boolean qualified) {
/* 115 */     List<String> paramTypeList = null;
/* 116 */     String signature = this.signature;
/*     */     
/* 118 */     if (signature != null) {
/*     */       
/* 120 */       paramTypeList = new ArrayList<>();
/*     */ 
/*     */       
/* 123 */       Map<String, String> additionalTypeArgs = null;
/* 124 */       if (signature.charAt(0) == '<') {
/* 125 */         int afterMatchingGT = skipLtGt(signature, 1);
/* 126 */         String typeParams = signature.substring(1, afterMatchingGT - 1);
/* 127 */         additionalTypeArgs = parseAdditionalTypeArgs(typeParams);
/* 128 */         signature = signature.substring(afterMatchingGT);
/*     */       } 
/*     */       
/* 131 */       if (signature.charAt(0) == '(') {
/*     */         
/* 133 */         int rparen = signature.indexOf(')', 1);
/* 134 */         String paramDescriptors = signature.substring(1, rparen);
/* 135 */         ParamDescriptorResult res = new ParamDescriptorResult();
/*     */         
/* 137 */         while (paramDescriptors.length() > 0) {
/* 138 */           parseParamDescriptor(paramDescriptors, cf, additionalTypeArgs, mi, "Error parsing method signature for ", res, qualified);
/*     */           
/* 140 */           paramTypeList.add(res.type);
/* 141 */           if (paramDescriptors.length() > res.pos) {
/* 142 */             paramDescriptors = paramDescriptors.substring(res.pos);
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 151 */         System.out.println("TODO: Unhandled method signature for " + mi
/* 152 */             .getName() + ": " + signature);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 157 */     return paramTypeList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodReturnType(MethodInfo mi, ClassFile cf, boolean qualified) {
/* 164 */     String signature = this.signature;
/* 165 */     String sig = null;
/*     */     
/* 167 */     if (signature != null) {
/*     */ 
/*     */       
/* 170 */       Map<String, String> additionalTypeArgs = null;
/* 171 */       if (signature.charAt(0) == '<') {
/* 172 */         int afterMatchingGT = skipLtGt(signature, 1);
/* 173 */         String typeParams = signature.substring(1, afterMatchingGT - 1);
/* 174 */         additionalTypeArgs = parseAdditionalTypeArgs(typeParams);
/* 175 */         signature = signature.substring(afterMatchingGT);
/*     */       } 
/*     */       
/* 178 */       if (signature.charAt(0) == '(') {
/* 179 */         int rparen = signature.indexOf(')', 1);
/* 180 */         if (rparen > -1 && rparen < signature.length() - 3) {
/* 181 */           String afterRParen = signature.substring(rparen + 1);
/* 182 */           ParamDescriptorResult res = new ParamDescriptorResult();
/* 183 */           parseParamDescriptor(afterRParen, cf, additionalTypeArgs, mi, "Can't parse return type from method sig for ", res, qualified);
/*     */           
/* 185 */           sig = res.type;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 190 */         System.out.println("TODO: Unhandled method signature for " + mi
/* 191 */             .getName() + ": " + signature);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 196 */     return sig;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 202 */     return this.signature;
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
/*     */   private String getTypeArgument(String typeVar, ClassFile cf, Map<String, String> additionalTypeArgs) {
/* 220 */     String type = cf.getTypeArgument(typeVar);
/* 221 */     if (type == null && additionalTypeArgs != null)
/*     */     {
/* 223 */       type = typeVar;
/*     */     }
/* 225 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> parseAdditionalTypeArgs(String typeParams) {
/* 231 */     Map<String, String> additionalTypeArgs = new HashMap<>();
/* 232 */     int offs = 0;
/* 233 */     int colon = typeParams.indexOf(':', offs);
/*     */     
/* 235 */     while (offs < typeParams.length()) {
/* 236 */       String param = typeParams.substring(offs, colon);
/* 237 */       int semicolon = typeParams.indexOf(';', offs + 1);
/* 238 */       int lt = typeParams.indexOf('<', offs + 1);
/* 239 */       if (lt > -1 && lt < semicolon) {
/* 240 */         int afterMatchingGT = skipLtGt(typeParams, lt + 1);
/* 241 */         String typeArg = typeParams.substring(colon + 1, afterMatchingGT);
/* 242 */         additionalTypeArgs.put(param, typeArg);
/* 243 */         offs = afterMatchingGT + 1;
/*     */       } else {
/*     */         
/* 246 */         String typeArg = typeParams.substring(colon + 1, semicolon);
/* 247 */         additionalTypeArgs.put(param, typeArg);
/* 248 */         offs = semicolon + 1;
/*     */       } 
/* 250 */       colon = typeParams.indexOf(':', offs);
/*     */     } 
/*     */     
/* 253 */     return additionalTypeArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParamDescriptorResult parseParamDescriptor(String str, ClassFile cf, Map<String, String> additionalTypeArgs, MethodInfo mi, String errorDesc, ParamDescriptorResult res, boolean qualified) {
/*     */     String type;
/*     */     int semicolon, lt;
/*     */     String clazz, typeVar, temp;
/* 266 */     int braceCount = -1;
/* 267 */     while (str.charAt(++braceCount) == '[');
/* 268 */     int pos = braceCount;
/*     */     
/* 270 */     boolean extendingGenericType = false;
/*     */     
/* 272 */     switch (str.charAt(pos)) {
/*     */ 
/*     */       
/*     */       case 'B':
/* 276 */         type = "byte";
/* 277 */         pos++;
/*     */         break;
/*     */       case 'C':
/* 280 */         type = "char";
/* 281 */         pos++;
/*     */         break;
/*     */       case 'D':
/* 284 */         type = "double";
/* 285 */         pos++;
/*     */         break;
/*     */       case 'F':
/* 288 */         type = "float";
/* 289 */         pos++;
/*     */         break;
/*     */       case 'I':
/* 292 */         type = "int";
/* 293 */         pos++;
/*     */         break;
/*     */       case 'J':
/* 296 */         type = "long";
/* 297 */         pos++;
/*     */         break;
/*     */       case 'S':
/* 300 */         type = "short";
/* 301 */         pos++;
/*     */         break;
/*     */       case 'Z':
/* 304 */         type = "boolean";
/* 305 */         pos++;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 'L':
/* 310 */         semicolon = str.indexOf(';', pos + 1);
/* 311 */         lt = str.indexOf('<', pos + 1);
/* 312 */         if (lt > -1 && lt < semicolon) {
/* 313 */           int offs = skipLtGt(str, lt + 1);
/*     */           
/* 315 */           if (offs == str.length() || str.charAt(offs) != ';') {
/* 316 */             System.out.println("TODO: " + errorDesc + mi
/* 317 */                 .getName() + ": " + this.signature);
/* 318 */             type = "ERROR_PARSING_METHOD_SIG";
/*     */             
/*     */             break;
/*     */           } 
/* 322 */           type = str.substring(pos + 1, lt);
/*     */ 
/*     */           
/* 325 */           type = qualified ? type.replace('/', '.') : type.substring(type.lastIndexOf('/') + 1);
/*     */           
/* 327 */           String paramDescriptors = str.substring(lt + 1, offs - 1);
/* 328 */           ParamDescriptorResult res2 = new ParamDescriptorResult();
/* 329 */           List<String> paramTypeList = new ArrayList<>();
/*     */           
/* 331 */           while (paramDescriptors.length() > 0) {
/* 332 */             parseParamDescriptor(paramDescriptors, cf, additionalTypeArgs, mi, "Error parsing method signature for ", res2, qualified);
/*     */             
/* 334 */             paramTypeList.add(res2.type);
/* 335 */             if (paramDescriptors.length() > res2.pos) {
/* 336 */               paramDescriptors = paramDescriptors.substring(res2.pos);
/*     */             }
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 342 */           StringBuilder sb = (new StringBuilder(type)).append('<');
/* 343 */           for (int j = 0; j < paramTypeList.size(); j++) {
/* 344 */             sb.append(paramTypeList.get(j));
/* 345 */             if (j < paramTypeList.size() - 1) {
/* 346 */               sb.append(", ");
/*     */             }
/*     */           } 
/* 349 */           type = sb.append('>').toString();
/* 350 */           pos = offs + 1;
/*     */           
/*     */           break;
/*     */         } 
/* 354 */         clazz = str.substring(pos + 1, semicolon);
/*     */ 
/*     */         
/* 357 */         clazz = qualified ? clazz.replace('/', '.') : clazz.substring(clazz.lastIndexOf('/') + 1);
/* 358 */         type = clazz;
/* 359 */         pos += semicolon + 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case '+':
/* 364 */         extendingGenericType = true;
/* 365 */         pos++;
/*     */ 
/*     */       
/*     */       case 'T':
/* 369 */         semicolon = str.indexOf(';', pos + 1);
/* 370 */         typeVar = str.substring(pos + 1, semicolon);
/* 371 */         type = getTypeArgument(typeVar, cf, additionalTypeArgs);
/* 372 */         if (type == null) {
/* 373 */           type = "UNKNOWN_GENERIC_TYPE_" + typeVar;
/*     */         }
/* 375 */         else if (extendingGenericType) {
/* 376 */           type = "? extends " + type;
/*     */         } 
/* 378 */         pos = semicolon + 1;
/*     */         break;
/*     */       
/*     */       case '*':
/* 382 */         type = "?";
/* 383 */         pos++;
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 388 */         temp = "INVALID_TYPE_" + str;
/* 389 */         type = temp;
/* 390 */         pos += str.length();
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 395 */     for (int i = 0; i < braceCount; i++) {
/* 396 */       type = type + "[]";
/*     */     }
/*     */     
/* 399 */     return res.set(type, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 406 */     return "[Signature: signature=" + getSignature() + "]";
/*     */   }
/*     */   
/*     */   private static class ParamDescriptorResult {
/*     */     public String type;
/*     */     public int pos;
/*     */     
/*     */     private ParamDescriptorResult() {}
/*     */     
/*     */     public ParamDescriptorResult set(String type, int pos) {
/* 416 */       this.type = type;
/* 417 */       this.pos = pos;
/* 418 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\attributes\Signature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */