/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DuplicatesPredicate
/*     */   implements Predicate
/*     */ {
/*     */   private final Set unique;
/*     */   private final Set rejected;
/*     */   
/*     */   public DuplicatesPredicate() {
/*  41 */     this.unique = new HashSet();
/*  42 */     this.rejected = Collections.emptySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DuplicatesPredicate(List allMethods) {
/*  50 */     this.rejected = new HashSet();
/*  51 */     this.unique = new HashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     Map<Object, Object> scanned = new HashMap<Object, Object>();
/*  58 */     Map<Object, Object> suspects = new HashMap<Object, Object>();
/*  59 */     for (Object o : allMethods) {
/*  60 */       Method method = (Method)o;
/*  61 */       Object sig = MethodWrapper.create(method);
/*  62 */       Method existing = (Method)scanned.get(sig);
/*  63 */       if (existing == null) {
/*  64 */         scanned.put(sig, method); continue;
/*  65 */       }  if (!suspects.containsKey(sig) && existing.isBridge() && !method.isBridge())
/*     */       {
/*     */ 
/*     */         
/*  69 */         suspects.put(sig, existing);
/*     */       }
/*     */     } 
/*     */     
/*  73 */     if (!suspects.isEmpty()) {
/*  74 */       Set<Class<?>> classes = new HashSet();
/*  75 */       UnnecessaryBridgeFinder finder = new UnnecessaryBridgeFinder(this.rejected);
/*  76 */       for (Object o : suspects.values()) {
/*  77 */         Method m = (Method)o;
/*  78 */         classes.add(m.getDeclaringClass());
/*  79 */         finder.addSuspectMethod(m);
/*     */       } 
/*  81 */       for (Object<?> o : classes) {
/*  82 */         Class c = (Class)o;
/*     */         try {
/*  84 */           ClassLoader cl = getClassLoader(c);
/*  85 */           if (cl == null) {
/*     */             continue;
/*     */           }
/*  88 */           InputStream is = cl.getResourceAsStream(c.getName().replace('.', '/') + ".class");
/*  89 */           if (is == null) {
/*     */             continue;
/*     */           }
/*     */           try {
/*  93 */             (new ClassReader(is)).accept(finder, 6);
/*     */           } finally {
/*  95 */             is.close();
/*     */           } 
/*  97 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean evaluate(Object arg) {
/* 104 */     return (!this.rejected.contains(arg) && this.unique.add(MethodWrapper.create((Method)arg)));
/*     */   }
/*     */   
/*     */   private static ClassLoader getClassLoader(Class c) {
/* 108 */     ClassLoader cl = c.getClassLoader();
/* 109 */     if (cl == null) {
/* 110 */       cl = DuplicatesPredicate.class.getClassLoader();
/*     */     }
/* 112 */     if (cl == null) {
/* 113 */       cl = Thread.currentThread().getContextClassLoader();
/*     */     }
/* 115 */     return cl;
/*     */   }
/*     */   
/*     */   private static class UnnecessaryBridgeFinder
/*     */     extends ClassVisitor {
/*     */     private final Set rejected;
/* 121 */     private Signature currentMethodSig = null;
/* 122 */     private Map methods = new HashMap<Object, Object>();
/*     */     
/*     */     UnnecessaryBridgeFinder(Set rejected) {
/* 125 */       super(Constants.ASM_API);
/* 126 */       this.rejected = rejected;
/*     */     }
/*     */     
/*     */     void addSuspectMethod(Method m) {
/* 130 */       this.methods.put(ReflectUtils.getSignature(m), m);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 143 */       Signature sig = new Signature(name, desc);
/* 144 */       final Method currentMethod = (Method)this.methods.remove(sig);
/* 145 */       if (currentMethod != null) {
/* 146 */         this.currentMethodSig = sig;
/* 147 */         return new MethodVisitor(Constants.ASM_API)
/*     */           {
/*     */             public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
/* 150 */               if (opcode == 183 && DuplicatesPredicate.UnnecessaryBridgeFinder.this.currentMethodSig != null) {
/* 151 */                 Signature target = new Signature(name, desc);
/* 152 */                 if (target.equals(DuplicatesPredicate.UnnecessaryBridgeFinder.this.currentMethodSig)) {
/* 153 */                   DuplicatesPredicate.UnnecessaryBridgeFinder.this.rejected.add(currentMethod);
/*     */                 }
/* 155 */                 DuplicatesPredicate.UnnecessaryBridgeFinder.this.currentMethodSig = null;
/*     */               } 
/*     */             }
/*     */           };
/*     */       } 
/* 160 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\DuplicatesPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */