/*     */ package org.fife.rsta.ac.js.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.FunctionNode;
/*     */ import org.mozilla.javascript.ast.Name;
/*     */ import org.mozilla.javascript.ast.PropertyGet;
/*     */ import org.mozilla.javascript.ast.StringLiteral;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RhinoUtil
/*     */ {
/*     */   public static String getFunctionArgsString(FunctionNode fn) {
/*  50 */     StringBuilder sb = new StringBuilder("(");
/*  51 */     int paramCount = fn.getParamCount();
/*  52 */     if (paramCount > 0) {
/*  53 */       List<AstNode> fnParams = fn.getParams();
/*  54 */       for (int i = 0; i < paramCount; i++) {
/*     */         String paramName;
/*  56 */         AstNode paramNode = fnParams.get(i);
/*  57 */         switch (paramNode.getType()) {
/*     */           case 39:
/*  59 */             paramName = ((Name)paramNode).getIdentifier();
/*     */             break;
/*     */           default:
/*  62 */             System.out.println("Unhandled class for param: " + paramNode
/*  63 */                 .getClass());
/*  64 */             paramName = "?";
/*     */             break;
/*     */         } 
/*  67 */         sb.append(paramName);
/*  68 */         if (i < paramCount - 1) {
/*  69 */           sb.append(", ");
/*     */         }
/*     */       } 
/*     */     } 
/*  73 */     sb.append(')');
/*  74 */     return sb.toString();
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
/*     */   public static String getPropertyName(AstNode propKeyNode) {
/*  89 */     return (propKeyNode instanceof Name) ? ((Name)propKeyNode)
/*  90 */       .getIdentifier() : ((StringLiteral)propKeyNode)
/*  91 */       .getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getPrototypeClazz(List<AstNode> nodes) {
/*  96 */     return getPrototypeClazz(nodes, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPrototypeClazz(List<AstNode> nodes, int depth) {
/* 102 */     if (depth < 0) {
/* 103 */       depth = nodes.size();
/*     */     }
/* 105 */     StringBuilder sb = new StringBuilder();
/* 106 */     for (int i = 0; i < depth; i++) {
/*     */ 
/*     */       
/* 109 */       sb.append(((AstNode)nodes.get(i)).toSource());
/* 110 */       if (i < depth - 1) {
/* 111 */         sb.append('.');
/*     */       }
/*     */     } 
/* 114 */     return sb.toString();
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
/*     */   private static boolean isName(AstNode node, String value) {
/* 128 */     return (node instanceof Name && value.equals(((Name)node).getIdentifier()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPrototypeNameNode(AstNode node) {
/* 133 */     return (node instanceof Name && "prototype"
/* 134 */       .equals(((Name)node).getIdentifier()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPrototypePropertyGet(PropertyGet pg) {
/* 139 */     return (pg != null && pg.getLeft() instanceof Name && 
/* 140 */       isPrototypeNameNode(pg.getRight()));
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
/*     */   public static boolean isSimplePropertyGet(PropertyGet pg, String expectedObj, String expectedField) {
/* 155 */     return (pg != null && isName(pg.getLeft(), expectedObj) && 
/* 156 */       isName(pg.getRight(), expectedField));
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<AstNode> toList(AstNode... nodes) {
/* 161 */     List<AstNode> list = new ArrayList<>();
/* 162 */     Collections.addAll(list, nodes);
/* 163 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\j\\util\RhinoUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */