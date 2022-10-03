/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.StringJoiner;
/*     */ import org.springframework.asm.ClassWriter;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InlineList
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   @Nullable
/*     */   private TypedValue constant;
/*     */   
/*     */   public InlineList(int startPos, int endPos, SpelNodeImpl... args) {
/*  47 */     super(startPos, endPos, args);
/*  48 */     checkIfConstant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkIfConstant() {
/*  58 */     boolean isConstant = true;
/*  59 */     for (int c = 0, max = getChildCount(); c < max; c++) {
/*  60 */       SpelNode child = getChild(c);
/*  61 */       if (!(child instanceof Literal)) {
/*  62 */         if (child instanceof InlineList) {
/*  63 */           InlineList inlineList = (InlineList)child;
/*  64 */           if (!inlineList.isConstant()) {
/*  65 */             isConstant = false;
/*     */           }
/*     */         } else {
/*     */           
/*  69 */           isConstant = false;
/*     */         } 
/*     */       }
/*     */     } 
/*  73 */     if (isConstant) {
/*  74 */       List<Object> constantList = new ArrayList();
/*  75 */       int childcount = getChildCount();
/*  76 */       for (int i = 0; i < childcount; i++) {
/*  77 */         SpelNode child = getChild(i);
/*  78 */         if (child instanceof Literal) {
/*  79 */           constantList.add(((Literal)child).getLiteralValue().getValue());
/*     */         }
/*  81 */         else if (child instanceof InlineList) {
/*  82 */           constantList.add(((InlineList)child).getConstantValue());
/*     */         } 
/*     */       } 
/*  85 */       this.constant = new TypedValue(Collections.unmodifiableList(constantList));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState expressionState) throws EvaluationException {
/*  91 */     if (this.constant != null) {
/*  92 */       return this.constant;
/*     */     }
/*     */     
/*  95 */     int childCount = getChildCount();
/*  96 */     List<Object> returnValue = new ArrayList(childCount);
/*  97 */     for (int c = 0; c < childCount; c++) {
/*  98 */       returnValue.add(getChild(c).getValue(expressionState));
/*     */     }
/* 100 */     return new TypedValue(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/* 106 */     StringJoiner sj = new StringJoiner(",", "{", "}");
/*     */     
/* 108 */     int count = getChildCount();
/* 109 */     for (int c = 0; c < count; c++) {
/* 110 */       sj.add(getChild(c).toStringAST());
/*     */     }
/* 112 */     return sj.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/* 119 */     return (this.constant != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<Object> getConstantValue() {
/* 125 */     Assert.state((this.constant != null), "No constant");
/* 126 */     return (List<Object>)this.constant.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 131 */     return isConstant();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow codeflow) {
/* 136 */     String constantFieldName = "inlineList$" + codeflow.nextFieldId();
/* 137 */     String className = codeflow.getClassName();
/*     */     
/* 139 */     codeflow.registerNewField((cw, cflow) -> cw.visitField(26, constantFieldName, "Ljava/util/List;", null, null));
/*     */ 
/*     */     
/* 142 */     codeflow.registerNewClinit((mVisitor, cflow) -> generateClinitCode(className, constantFieldName, mVisitor, cflow, false));
/*     */ 
/*     */     
/* 145 */     mv.visitFieldInsn(178, className, constantFieldName, "Ljava/util/List;");
/* 146 */     codeflow.pushDescriptor("Ljava/util/List");
/*     */   }
/*     */   
/*     */   void generateClinitCode(String clazzname, String constantFieldName, MethodVisitor mv, CodeFlow codeflow, boolean nested) {
/* 150 */     mv.visitTypeInsn(187, "java/util/ArrayList");
/* 151 */     mv.visitInsn(89);
/* 152 */     mv.visitMethodInsn(183, "java/util/ArrayList", "<init>", "()V", false);
/* 153 */     if (!nested) {
/* 154 */       mv.visitFieldInsn(179, clazzname, constantFieldName, "Ljava/util/List;");
/*     */     }
/* 156 */     int childCount = getChildCount();
/* 157 */     for (int c = 0; c < childCount; c++) {
/* 158 */       if (!nested) {
/* 159 */         mv.visitFieldInsn(178, clazzname, constantFieldName, "Ljava/util/List;");
/*     */       } else {
/*     */         
/* 162 */         mv.visitInsn(89);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 167 */       if (this.children[c] instanceof InlineList) {
/* 168 */         ((InlineList)this.children[c]).generateClinitCode(clazzname, constantFieldName, mv, codeflow, true);
/*     */       } else {
/*     */         
/* 171 */         this.children[c].generateCode(mv, codeflow);
/* 172 */         String lastDesc = codeflow.lastDescriptor();
/* 173 */         if (CodeFlow.isPrimitive(lastDesc)) {
/* 174 */           CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */         }
/*     */       } 
/* 177 */       mv.visitMethodInsn(185, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
/* 178 */       mv.visitInsn(87);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\InlineList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */