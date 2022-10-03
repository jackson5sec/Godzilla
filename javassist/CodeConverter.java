/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.convert.TransformAccessArrayField;
/*     */ import javassist.convert.TransformAfter;
/*     */ import javassist.convert.TransformBefore;
/*     */ import javassist.convert.TransformCall;
/*     */ import javassist.convert.TransformCallToStatic;
/*     */ import javassist.convert.TransformFieldAccess;
/*     */ import javassist.convert.TransformNew;
/*     */ import javassist.convert.TransformNewClass;
/*     */ import javassist.convert.TransformReadField;
/*     */ import javassist.convert.TransformWriteField;
/*     */ import javassist.convert.Transformer;
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
/*     */ public class CodeConverter
/*     */ {
/*  65 */   protected Transformer transformers = null;
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
/*     */   public void replaceNew(CtClass newClass, CtClass calledClass, String calledMethod) {
/* 111 */     this
/* 112 */       .transformers = (Transformer)new TransformNew(this.transformers, newClass.getName(), calledClass.getName(), calledMethod);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceNew(CtClass oldClass, CtClass newClass) {
/* 137 */     this
/* 138 */       .transformers = (Transformer)new TransformNewClass(this.transformers, oldClass.getName(), newClass.getName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void redirectFieldAccess(CtField field, CtClass newClass, String newFieldname) {
/* 160 */     this
/* 161 */       .transformers = (Transformer)new TransformFieldAccess(this.transformers, field, newClass.getName(), newFieldname);
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
/*     */   public void replaceFieldRead(CtField field, CtClass calledClass, String calledMethod) {
/* 200 */     this
/* 201 */       .transformers = (Transformer)new TransformReadField(this.transformers, field, calledClass.getName(), calledMethod);
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
/*     */   public void replaceFieldWrite(CtField field, CtClass calledClass, String calledMethod) {
/* 241 */     this
/* 242 */       .transformers = (Transformer)new TransformWriteField(this.transformers, field, calledClass.getName(), calledMethod);
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
/*     */   public void replaceArrayAccess(CtClass calledClass, ArrayAccessReplacementMethodNames names) throws NotFoundException {
/* 344 */     this.transformers = (Transformer)new TransformAccessArrayField(this.transformers, calledClass.getName(), names);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void redirectMethodCall(CtMethod origMethod, CtMethod substMethod) throws CannotCompileException {
/* 366 */     String d1 = origMethod.getMethodInfo2().getDescriptor();
/* 367 */     String d2 = substMethod.getMethodInfo2().getDescriptor();
/* 368 */     if (!d1.equals(d2)) {
/* 369 */       throw new CannotCompileException("signature mismatch: " + substMethod
/* 370 */           .getLongName());
/*     */     }
/* 372 */     int mod1 = origMethod.getModifiers();
/* 373 */     int mod2 = substMethod.getModifiers();
/* 374 */     if (Modifier.isStatic(mod1) != Modifier.isStatic(mod2) || (
/* 375 */       Modifier.isPrivate(mod1) && !Modifier.isPrivate(mod2)) || origMethod
/* 376 */       .getDeclaringClass().isInterface() != substMethod
/* 377 */       .getDeclaringClass().isInterface()) {
/* 378 */       throw new CannotCompileException("invoke-type mismatch " + substMethod
/* 379 */           .getLongName());
/*     */     }
/* 381 */     this.transformers = (Transformer)new TransformCall(this.transformers, origMethod, substMethod);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void redirectMethodCall(String oldMethodName, CtMethod newMethod) throws CannotCompileException {
/* 406 */     this.transformers = (Transformer)new TransformCall(this.transformers, oldMethodName, newMethod);
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
/*     */   public void redirectMethodCallToStatic(CtMethod origMethod, CtMethod staticMethod) {
/* 442 */     this.transformers = (Transformer)new TransformCallToStatic(this.transformers, origMethod, staticMethod);
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
/*     */   public void insertBeforeMethod(CtMethod origMethod, CtMethod beforeMethod) throws CannotCompileException {
/*     */     try {
/* 485 */       this.transformers = (Transformer)new TransformBefore(this.transformers, origMethod, beforeMethod);
/*     */     
/*     */     }
/* 488 */     catch (NotFoundException e) {
/* 489 */       throw new CannotCompileException(e);
/*     */     } 
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
/*     */   public void insertAfterMethod(CtMethod origMethod, CtMethod afterMethod) throws CannotCompileException {
/*     */     try {
/* 533 */       this.transformers = (Transformer)new TransformAfter(this.transformers, origMethod, afterMethod);
/*     */     
/*     */     }
/* 536 */     catch (NotFoundException e) {
/* 537 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doit(CtClass clazz, MethodInfo minfo, ConstPool cp) throws CannotCompileException {
/* 548 */     CodeAttribute codeAttr = minfo.getCodeAttribute();
/* 549 */     if (codeAttr == null || this.transformers == null)
/*     */       return;  Transformer t;
/* 551 */     for (t = this.transformers; t != null; t = t.getNext()) {
/* 552 */       t.initialize(cp, clazz, minfo);
/*     */     }
/* 554 */     CodeIterator iterator = codeAttr.iterator();
/* 555 */     while (iterator.hasNext()) {
/*     */       try {
/* 557 */         int pos = iterator.next();
/* 558 */         for (t = this.transformers; t != null; t = t.getNext()) {
/* 559 */           pos = t.transform(clazz, pos, iterator, cp);
/*     */         }
/* 561 */       } catch (BadBytecode e) {
/* 562 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 566 */     int locals = 0;
/* 567 */     int stack = 0;
/* 568 */     for (t = this.transformers; t != null; t = t.getNext()) {
/* 569 */       int s = t.extraLocals();
/* 570 */       if (s > locals) {
/* 571 */         locals = s;
/*     */       }
/* 573 */       s = t.extraStack();
/* 574 */       if (s > stack) {
/* 575 */         stack = s;
/*     */       }
/*     */     } 
/* 578 */     for (t = this.transformers; t != null; t = t.getNext()) {
/* 579 */       t.clean();
/*     */     }
/* 581 */     if (locals > 0) {
/* 582 */       codeAttr.setMaxLocals(codeAttr.getMaxLocals() + locals);
/*     */     }
/* 584 */     if (stack > 0) {
/* 585 */       codeAttr.setMaxStack(codeAttr.getMaxStack() + stack);
/*     */     }
/*     */     try {
/* 588 */       minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz
/* 589 */           .getClassFile2());
/*     */     }
/* 591 */     catch (BadBytecode b) {
/* 592 */       throw new CannotCompileException(b.getMessage(), b);
/*     */     } 
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
/*     */   public static class DefaultArrayAccessReplacementMethodNames
/*     */     implements ArrayAccessReplacementMethodNames
/*     */   {
/*     */     public String byteOrBooleanRead() {
/* 722 */       return "arrayReadByteOrBoolean";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String byteOrBooleanWrite() {
/* 732 */       return "arrayWriteByteOrBoolean";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String charRead() {
/* 742 */       return "arrayReadChar";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String charWrite() {
/* 752 */       return "arrayWriteChar";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String doubleRead() {
/* 762 */       return "arrayReadDouble";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String doubleWrite() {
/* 772 */       return "arrayWriteDouble";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String floatRead() {
/* 782 */       return "arrayReadFloat";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String floatWrite() {
/* 792 */       return "arrayWriteFloat";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String intRead() {
/* 802 */       return "arrayReadInt";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String intWrite() {
/* 812 */       return "arrayWriteInt";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String longRead() {
/* 822 */       return "arrayReadLong";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String longWrite() {
/* 832 */       return "arrayWriteLong";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String objectRead() {
/* 842 */       return "arrayReadObject";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String objectWrite() {
/* 852 */       return "arrayWriteObject";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String shortRead() {
/* 862 */       return "arrayReadShort";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String shortWrite() {
/* 872 */       return "arrayWriteShort";
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ArrayAccessReplacementMethodNames {
/*     */     String byteOrBooleanRead();
/*     */     
/*     */     String byteOrBooleanWrite();
/*     */     
/*     */     String charRead();
/*     */     
/*     */     String charWrite();
/*     */     
/*     */     String doubleRead();
/*     */     
/*     */     String doubleWrite();
/*     */     
/*     */     String floatRead();
/*     */     
/*     */     String floatWrite();
/*     */     
/*     */     String intRead();
/*     */     
/*     */     String intWrite();
/*     */     
/*     */     String longRead();
/*     */     
/*     */     String longWrite();
/*     */     
/*     */     String objectRead();
/*     */     
/*     */     String objectWrite();
/*     */     
/*     */     String shortRead();
/*     */     
/*     */     String shortWrite();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CodeConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */