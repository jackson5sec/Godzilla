/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.asm.ClassWriter;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Opcodes;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompiledExpression;
/*     */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SpelCompiler
/*     */   implements Opcodes
/*     */ {
/*     */   private static final int CLASSES_DEFINED_LIMIT = 100;
/*  73 */   private static final Log logger = LogFactory.getLog(SpelCompiler.class);
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final Map<ClassLoader, SpelCompiler> compilers = (Map<ClassLoader, SpelCompiler>)new ConcurrentReferenceHashMap();
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile ChildClassLoader childClassLoader;
/*     */ 
/*     */   
/*  84 */   private final AtomicInteger suffixId = new AtomicInteger(1);
/*     */ 
/*     */   
/*     */   private SpelCompiler(@Nullable ClassLoader classloader) {
/*  88 */     this.childClassLoader = new ChildClassLoader(classloader);
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
/*     */   @Nullable
/*     */   public CompiledExpression compile(SpelNodeImpl expression) {
/* 103 */     if (expression.isCompilable()) {
/* 104 */       if (logger.isDebugEnabled()) {
/* 105 */         logger.debug("SpEL: compiling " + expression.toStringAST());
/*     */       }
/* 107 */       Class<? extends CompiledExpression> clazz = createExpressionClass(expression);
/* 108 */       if (clazz != null) {
/*     */         try {
/* 110 */           return ReflectionUtils.accessibleConstructor(clazz, new Class[0]).newInstance(new Object[0]);
/*     */         }
/* 112 */         catch (Throwable ex) {
/* 113 */           throw new IllegalStateException("Failed to instantiate CompiledExpression", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 118 */     if (logger.isDebugEnabled()) {
/* 119 */       logger.debug("SpEL: unable to compile " + expression.toStringAST());
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   private int getNextSuffix() {
/* 125 */     return this.suffixId.incrementAndGet();
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
/*     */   @Nullable
/*     */   private Class<? extends CompiledExpression> createExpressionClass(SpelNodeImpl expressionToCompile) {
/* 138 */     String className = "spel/Ex" + getNextSuffix();
/* 139 */     String evaluationContextClass = "org/springframework/expression/EvaluationContext";
/* 140 */     ClassWriter cw = new ExpressionClassWriter();
/* 141 */     cw.visit(52, 1, className, null, "org/springframework/expression/spel/CompiledExpression", null);
/*     */ 
/*     */     
/* 144 */     MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
/* 145 */     mv.visitCode();
/* 146 */     mv.visitVarInsn(25, 0);
/* 147 */     mv.visitMethodInsn(183, "org/springframework/expression/spel/CompiledExpression", "<init>", "()V", false);
/*     */     
/* 149 */     mv.visitInsn(177);
/* 150 */     mv.visitMaxs(1, 1);
/* 151 */     mv.visitEnd();
/*     */ 
/*     */     
/* 154 */     mv = cw.visitMethod(1, "getValue", "(Ljava/lang/Object;L" + evaluationContextClass + ";)Ljava/lang/Object;", null, new String[] { "org/springframework/expression/EvaluationException" });
/*     */ 
/*     */     
/* 157 */     mv.visitCode();
/*     */     
/* 159 */     CodeFlow cf = new CodeFlow(className, cw);
/*     */ 
/*     */     
/*     */     try {
/* 163 */       expressionToCompile.generateCode(mv, cf);
/*     */     }
/* 165 */     catch (IllegalStateException ex) {
/* 166 */       if (logger.isDebugEnabled()) {
/* 167 */         logger.debug(expressionToCompile.getClass().getSimpleName() + ".generateCode opted out of compilation: " + ex
/* 168 */             .getMessage());
/*     */       }
/* 170 */       return null;
/*     */     } 
/*     */     
/* 173 */     CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor());
/* 174 */     if ("V".equals(cf.lastDescriptor())) {
/* 175 */       mv.visitInsn(1);
/*     */     }
/* 177 */     mv.visitInsn(176);
/*     */     
/* 179 */     mv.visitMaxs(0, 0);
/* 180 */     mv.visitEnd();
/* 181 */     cw.visitEnd();
/*     */     
/* 183 */     cf.finish();
/*     */     
/* 185 */     byte[] data = cw.toByteArray();
/*     */ 
/*     */     
/* 188 */     return loadClass(StringUtils.replace(className, "/", "."), data);
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
/*     */   private Class<? extends CompiledExpression> loadClass(String name, byte[] bytes) {
/* 202 */     ChildClassLoader ccl = this.childClassLoader;
/* 203 */     if (ccl.getClassesDefinedCount() >= 100) {
/* 204 */       synchronized (this) {
/* 205 */         ChildClassLoader currentCcl = this.childClassLoader;
/* 206 */         if (ccl == currentCcl) {
/*     */           
/* 208 */           ccl = new ChildClassLoader(ccl.getParent());
/* 209 */           this.childClassLoader = ccl;
/*     */         }
/*     */         else {
/*     */           
/* 213 */           ccl = currentCcl;
/*     */         } 
/*     */       } 
/*     */     }
/* 217 */     return (Class)ccl.defineClass(name, bytes);
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
/*     */   public static SpelCompiler getCompiler(@Nullable ClassLoader classLoader) {
/* 229 */     ClassLoader clToUse = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
/*     */     
/* 231 */     SpelCompiler compiler = compilers.get(clToUse);
/* 232 */     if (compiler == null)
/*     */     {
/* 234 */       synchronized (compilers) {
/* 235 */         compiler = compilers.get(clToUse);
/* 236 */         if (compiler == null) {
/* 237 */           compiler = new SpelCompiler(clToUse);
/* 238 */           compilers.put(clToUse, compiler);
/*     */         } 
/*     */       } 
/*     */     }
/* 242 */     return compiler;
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
/*     */   public static boolean compile(Expression expression) {
/* 254 */     return (expression instanceof SpelExpression && ((SpelExpression)expression).compileExpression());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void revertToInterpreted(Expression expression) {
/* 263 */     if (expression instanceof SpelExpression) {
/* 264 */       ((SpelExpression)expression).revertToInterpreted();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ChildClassLoader
/*     */     extends URLClassLoader
/*     */   {
/* 274 */     private static final URL[] NO_URLS = new URL[0];
/*     */     
/* 276 */     private final AtomicInteger classesDefinedCount = new AtomicInteger(0);
/*     */     
/*     */     public ChildClassLoader(@Nullable ClassLoader classLoader) {
/* 279 */       super(NO_URLS, classLoader);
/*     */     }
/*     */     
/*     */     public Class<?> defineClass(String name, byte[] bytes) {
/* 283 */       Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
/* 284 */       this.classesDefinedCount.incrementAndGet();
/* 285 */       return clazz;
/*     */     }
/*     */     
/*     */     public int getClassesDefinedCount() {
/* 289 */       return this.classesDefinedCount.get();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ExpressionClassWriter
/*     */     extends ClassWriter
/*     */   {
/*     */     public ExpressionClassWriter() {
/* 300 */       super(3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected ClassLoader getClassLoader() {
/* 305 */       return SpelCompiler.this.childClassLoader;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\standard\SpelCompiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */