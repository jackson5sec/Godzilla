/*     */ package org.springframework.cglib.proxy;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BridgeMethodResolver
/*     */ {
/*     */   private final Map declToBridge;
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   public BridgeMethodResolver(Map declToBridge, ClassLoader classLoader) {
/*  48 */     this.declToBridge = declToBridge;
/*  49 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map resolveAll() {
/*  57 */     Map<Object, Object> resolved = new HashMap<Object, Object>();
/*  58 */     for (Iterator<Map.Entry> entryIter = this.declToBridge.entrySet().iterator(); entryIter.hasNext(); ) {
/*  59 */       Map.Entry entry = entryIter.next();
/*  60 */       Class owner = (Class)entry.getKey();
/*  61 */       Set bridges = (Set)entry.getValue();
/*     */       try {
/*  63 */         InputStream is = this.classLoader.getResourceAsStream(owner.getName().replace('.', '/') + ".class");
/*  64 */         if (is == null) {
/*  65 */           return resolved;
/*     */         }
/*     */         try {
/*  68 */           (new ClassReader(is))
/*  69 */             .accept(new BridgedFinder(bridges, resolved), 6);
/*     */         } finally {
/*     */           
/*  72 */           is.close();
/*     */         } 
/*  74 */       } catch (IOException iOException) {}
/*     */     } 
/*  76 */     return resolved;
/*     */   }
/*     */   
/*     */   private static class BridgedFinder
/*     */     extends ClassVisitor {
/*     */     private Map resolved;
/*     */     private Set eligibleMethods;
/*  83 */     private Signature currentMethod = null;
/*     */     
/*     */     BridgedFinder(Set eligibleMethods, Map resolved) {
/*  86 */       super(Constants.ASM_API);
/*  87 */       this.resolved = resolved;
/*  88 */       this.eligibleMethods = eligibleMethods;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {}
/*     */ 
/*     */     
/*     */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/*  97 */       Signature sig = new Signature(name, desc);
/*  98 */       if (this.eligibleMethods.remove(sig)) {
/*  99 */         this.currentMethod = sig;
/* 100 */         return new MethodVisitor(Constants.ASM_API)
/*     */           {
/*     */             public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
/* 103 */               if ((opcode == 183 || (itf && opcode == 185)) && BridgeMethodResolver.BridgedFinder.this
/*     */                 
/* 105 */                 .currentMethod != null) {
/* 106 */                 Signature target = new Signature(name, desc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 113 */                 if (!target.equals(BridgeMethodResolver.BridgedFinder.this.currentMethod)) {
/* 114 */                   BridgeMethodResolver.BridgedFinder.this.resolved.put(BridgeMethodResolver.BridgedFinder.this.currentMethod, target);
/*     */                 }
/* 116 */                 BridgeMethodResolver.BridgedFinder.this.currentMethod = null;
/*     */               } 
/*     */             }
/*     */           };
/*     */       } 
/* 121 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\BridgeMethodResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */