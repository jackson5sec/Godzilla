/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Opcodes;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SpelNodeImpl
/*     */   implements SpelNode, Opcodes
/*     */ {
/*  47 */   private static final SpelNodeImpl[] NO_CHILDREN = new SpelNodeImpl[0];
/*     */ 
/*     */   
/*     */   private final int startPos;
/*     */   
/*     */   private final int endPos;
/*     */   
/*  54 */   protected SpelNodeImpl[] children = NO_CHILDREN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private SpelNodeImpl parent;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected volatile String exitTypeDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpelNodeImpl(int startPos, int endPos, SpelNodeImpl... operands) {
/*  73 */     this.startPos = startPos;
/*  74 */     this.endPos = endPos;
/*  75 */     if (!ObjectUtils.isEmpty((Object[])operands)) {
/*  76 */       this.children = operands;
/*  77 */       for (SpelNodeImpl operand : operands) {
/*  78 */         Assert.notNull(operand, "Operand must not be null");
/*  79 */         operand.parent = this;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean nextChildIs(Class<?>... classes) {
/*  89 */     if (this.parent != null) {
/*  90 */       SpelNodeImpl[] peers = this.parent.children;
/*  91 */       for (int i = 0, max = peers.length; i < max; i++) {
/*  92 */         if (this == peers[i]) {
/*  93 */           if (i + 1 >= max) {
/*  94 */             return false;
/*     */           }
/*  96 */           Class<?> peerClass = peers[i + 1].getClass();
/*  97 */           for (Class<?> desiredClass : classes) {
/*  98 */             if (peerClass == desiredClass) {
/*  99 */               return true;
/*     */             }
/*     */           } 
/* 102 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Object getValue(ExpressionState expressionState) throws EvaluationException {
/* 112 */     return getValueInternal(expressionState).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public final TypedValue getTypedValue(ExpressionState expressionState) throws EvaluationException {
/* 117 */     return getValueInternal(expressionState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWritable(ExpressionState expressionState) throws EvaluationException {
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(ExpressionState expressionState, @Nullable Object newValue) throws EvaluationException {
/* 128 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.SETVALUE_NOT_SUPPORTED, new Object[] { getClass() });
/*     */   }
/*     */ 
/*     */   
/*     */   public SpelNode getChild(int index) {
/* 133 */     return this.children[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getChildCount() {
/* 138 */     return this.children.length;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getObjectClass(@Nullable Object obj) {
/* 144 */     if (obj == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     return (obj instanceof Class) ? (Class)obj : obj.getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartPosition() {
/* 152 */     return this.startPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndPosition() {
/* 157 */     return this.endPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompilable() {
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf) {
/* 178 */     throw new IllegalStateException(getClass().getName() + " has no generateCode(..) method");
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getExitDescriptor() {
/* 183 */     return this.exitTypeDescriptor;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected final <T> T getValue(ExpressionState state, Class<T> desiredReturnType) throws EvaluationException {
/* 188 */     return (T)ExpressionUtils.convertTypedValue(state.getEvaluationContext(), getValueInternal(state), desiredReturnType);
/*     */   }
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
/* 192 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.NOT_ASSIGNABLE, new Object[] { toStringAST() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypedValue getValueInternal(ExpressionState paramExpressionState) throws EvaluationException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void generateCodeForArguments(MethodVisitor mv, CodeFlow cf, Member member, SpelNodeImpl[] arguments) {
/* 208 */     String[] paramDescriptors = null;
/* 209 */     boolean isVarargs = false;
/* 210 */     if (member instanceof Constructor) {
/* 211 */       Constructor<?> ctor = (Constructor)member;
/* 212 */       paramDescriptors = CodeFlow.toDescriptors(ctor.getParameterTypes());
/* 213 */       isVarargs = ctor.isVarArgs();
/*     */     } else {
/*     */       
/* 216 */       Method method = (Method)member;
/* 217 */       paramDescriptors = CodeFlow.toDescriptors(method.getParameterTypes());
/* 218 */       isVarargs = method.isVarArgs();
/*     */     } 
/* 220 */     if (isVarargs) {
/*     */ 
/*     */       
/* 223 */       int p = 0;
/* 224 */       int childCount = arguments.length;
/*     */ 
/*     */       
/* 227 */       for (p = 0; p < paramDescriptors.length - 1; p++) {
/* 228 */         generateCodeForArgument(mv, cf, arguments[p], paramDescriptors[p]);
/*     */       }
/*     */       
/* 231 */       SpelNodeImpl lastChild = (childCount == 0) ? null : arguments[childCount - 1];
/* 232 */       String arrayType = paramDescriptors[paramDescriptors.length - 1];
/*     */ 
/*     */       
/* 235 */       if (lastChild != null && arrayType.equals(lastChild.getExitDescriptor())) {
/* 236 */         generateCodeForArgument(mv, cf, lastChild, paramDescriptors[p]);
/*     */       } else {
/*     */         
/* 239 */         arrayType = arrayType.substring(1);
/*     */         
/* 241 */         CodeFlow.insertNewArrayCode(mv, childCount - p, arrayType);
/*     */         
/* 243 */         int arrayindex = 0;
/* 244 */         while (p < childCount) {
/* 245 */           SpelNodeImpl child = arguments[p];
/* 246 */           mv.visitInsn(89);
/* 247 */           CodeFlow.insertOptimalLoad(mv, arrayindex++);
/* 248 */           generateCodeForArgument(mv, cf, child, arrayType);
/* 249 */           CodeFlow.insertArrayStore(mv, arrayType);
/* 250 */           p++;
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 255 */       for (int i = 0; i < paramDescriptors.length; i++) {
/* 256 */         generateCodeForArgument(mv, cf, arguments[i], paramDescriptors[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void generateCodeForArgument(MethodVisitor mv, CodeFlow cf, SpelNodeImpl argument, String paramDesc) {
/* 266 */     cf.enterCompilationScope();
/* 267 */     argument.generateCode(mv, cf);
/* 268 */     String lastDesc = cf.lastDescriptor();
/* 269 */     Assert.state((lastDesc != null), "No last descriptor");
/* 270 */     boolean primitiveOnStack = CodeFlow.isPrimitive(lastDesc);
/*     */     
/* 272 */     if (primitiveOnStack && paramDesc.charAt(0) == 'L') {
/* 273 */       CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
/*     */     }
/* 275 */     else if (paramDesc.length() == 1 && !primitiveOnStack) {
/* 276 */       CodeFlow.insertUnboxInsns(mv, paramDesc.charAt(0), lastDesc);
/*     */     }
/* 278 */     else if (!paramDesc.equals(lastDesc)) {
/*     */       
/* 280 */       CodeFlow.insertCheckCast(mv, paramDesc);
/*     */     } 
/* 282 */     cf.exitCompilationScope();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\SpelNodeImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */