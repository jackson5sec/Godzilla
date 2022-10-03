/*     */ package org.fife.rsta.ac.java.rjc.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*     */ import org.fife.rsta.ac.java.rjc.lang.TypeParameter;
/*     */ import org.fife.rsta.ac.java.rjc.lexer.Scanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NormalClassDeclaration
/*     */   extends AbstractTypeDeclarationNode
/*     */ {
/*     */   private List<TypeParameter> typeParams;
/*     */   private Type extendedType;
/*     */   private List<Type> implementedList;
/*     */   
/*     */   public NormalClassDeclaration(Scanner s, int offs, String className) {
/*  42 */     super(className, s.createOffset(offs), s.createOffset(offs + className.length()));
/*  43 */     this.implementedList = new ArrayList<>(0);
/*     */ 
/*     */ 
/*     */     
/*  47 */     this.extendedType = new Type("java.lang.Object");
/*     */   }
/*     */ 
/*     */   
/*     */   public void addImplemented(Type implemented) {
/*  52 */     this.implementedList.add(implemented);
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getExtendedType() {
/*  57 */     return this.extendedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getImplementedCount() {
/*  62 */     return this.implementedList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Type> getImplementedIterator() {
/*  67 */     return this.implementedList.iterator();
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
/*     */   public Method getMethodContainingOffset(int offs) {
/*  79 */     for (Iterator<Method> i = getMethodIterator(); i.hasNext(); ) {
/*  80 */       Method method = i.next();
/*  81 */       if (method.getBodyContainsOffset(offs)) {
/*  82 */         return method;
/*     */       }
/*     */     } 
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<TypeParameter> getTypeParameters() {
/*  90 */     return this.typeParams;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeString() {
/*  96 */     return "class";
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
/*     */   private boolean isTypeCompatible(Type type, String typeName) {
/* 111 */     String typeName2 = type.getName(false);
/*     */ 
/*     */ 
/*     */     
/* 115 */     int lt = typeName2.indexOf('<');
/* 116 */     if (lt > -1) {
/* 117 */       String arrayDepth = null;
/* 118 */       int brackets = typeName2.indexOf('[', lt);
/* 119 */       if (brackets > -1) {
/* 120 */         arrayDepth = typeName2.substring(brackets);
/*     */       }
/* 122 */       typeName2 = typeName2.substring(lt);
/* 123 */       if (arrayDepth != null) {
/* 124 */         typeName2 = typeName2 + arrayDepth;
/*     */       }
/*     */     } 
/*     */     
/* 128 */     return typeName2.equalsIgnoreCase(typeName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtendedType(Type type) {
/* 134 */     this.extendedType = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTypeParameters(List<TypeParameter> typeParams) {
/* 139 */     this.typeParams = typeParams;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\NormalClassDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */