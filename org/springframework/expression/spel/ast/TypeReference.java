/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
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
/*     */ 
/*     */ public class TypeReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final int dimensions;
/*     */   @Nullable
/*     */   private transient Class<?> type;
/*     */   
/*     */   public TypeReference(int startPos, int endPos, SpelNodeImpl qualifiedId) {
/*  45 */     this(startPos, endPos, qualifiedId, 0);
/*     */   }
/*     */   
/*     */   public TypeReference(int startPos, int endPos, SpelNodeImpl qualifiedId, int dims) {
/*  49 */     super(startPos, endPos, new SpelNodeImpl[] { qualifiedId });
/*  50 */     this.dimensions = dims;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/*  57 */     String typeName = (String)this.children[0].getValueInternal(state).getValue();
/*  58 */     Assert.state((typeName != null), "No type name");
/*  59 */     if (!typeName.contains(".") && Character.isLowerCase(typeName.charAt(0))) {
/*  60 */       TypeCode tc = TypeCode.valueOf(typeName.toUpperCase());
/*  61 */       if (tc != TypeCode.OBJECT) {
/*     */         
/*  63 */         Class<?> clazz1 = makeArrayIfNecessary(tc.getType());
/*  64 */         this.exitTypeDescriptor = "Ljava/lang/Class";
/*  65 */         this.type = clazz1;
/*  66 */         return new TypedValue(clazz1);
/*     */       } 
/*     */     } 
/*  69 */     Class<?> clazz = state.findType(typeName);
/*  70 */     clazz = makeArrayIfNecessary(clazz);
/*  71 */     this.exitTypeDescriptor = "Ljava/lang/Class";
/*  72 */     this.type = clazz;
/*  73 */     return new TypedValue(clazz);
/*     */   }
/*     */   
/*     */   private Class<?> makeArrayIfNecessary(Class<?> clazz) {
/*  77 */     if (this.dimensions != 0) {
/*  78 */       for (int i = 0; i < this.dimensions; i++) {
/*  79 */         Object array = Array.newInstance(clazz, 0);
/*  80 */         clazz = array.getClass();
/*     */       } 
/*     */     }
/*  83 */     return clazz;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toStringAST() {
/*  88 */     StringBuilder sb = new StringBuilder("T(");
/*  89 */     sb.append(getChild(0).toStringAST());
/*  90 */     for (int d = 0; d < this.dimensions; d++) {
/*  91 */       sb.append("[]");
/*     */     }
/*  93 */     sb.append(')');
/*  94 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/*  99 */     return (this.exitTypeDescriptor != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 105 */     Assert.state((this.type != null), "No type available");
/* 106 */     if (this.type.isPrimitive()) {
/* 107 */       if (this.type == boolean.class) {
/* 108 */         mv.visitFieldInsn(178, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 110 */       else if (this.type == byte.class) {
/* 111 */         mv.visitFieldInsn(178, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 113 */       else if (this.type == char.class) {
/* 114 */         mv.visitFieldInsn(178, "java/lang/Character", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 116 */       else if (this.type == double.class) {
/* 117 */         mv.visitFieldInsn(178, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 119 */       else if (this.type == float.class) {
/* 120 */         mv.visitFieldInsn(178, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 122 */       else if (this.type == int.class) {
/* 123 */         mv.visitFieldInsn(178, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 125 */       else if (this.type == long.class) {
/* 126 */         mv.visitFieldInsn(178, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
/*     */       }
/* 128 */       else if (this.type == short.class) {
/* 129 */         mv.visitFieldInsn(178, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
/*     */       } 
/*     */     } else {
/*     */       
/* 133 */       mv.visitLdcInsn(Type.getType(this.type));
/*     */     } 
/* 135 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\TypeReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */