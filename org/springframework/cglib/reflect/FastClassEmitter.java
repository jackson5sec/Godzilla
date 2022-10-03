/*     */ package org.springframework.cglib.reflect;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.Block;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.CollectionUtils;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.MethodInfo;
/*     */ import org.springframework.cglib.core.MethodInfoTransformer;
/*     */ import org.springframework.cglib.core.ObjectSwitchCallback;
/*     */ import org.springframework.cglib.core.Predicate;
/*     */ import org.springframework.cglib.core.ProcessSwitchCallback;
/*     */ import org.springframework.cglib.core.ReflectUtils;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.core.Transformer;
/*     */ import org.springframework.cglib.core.TypeUtils;
/*     */ import org.springframework.cglib.core.VisibilityPredicate;
/*     */ 
/*     */ class FastClassEmitter extends ClassEmitter {
/*  27 */   private static final Signature CSTRUCT_CLASS = TypeUtils.parseConstructor("Class");
/*     */   
/*  29 */   private static final Signature METHOD_GET_INDEX = TypeUtils.parseSignature("int getIndex(String, Class[])");
/*  30 */   private static final Signature SIGNATURE_GET_INDEX = new Signature("getIndex", Type.INT_TYPE, new Type[] { Constants.TYPE_SIGNATURE });
/*     */ 
/*     */   
/*  33 */   private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
/*     */   
/*  35 */   private static final Signature CONSTRUCTOR_GET_INDEX = TypeUtils.parseSignature("int getIndex(Class[])");
/*     */   
/*  37 */   private static final Signature INVOKE = TypeUtils.parseSignature("Object invoke(int, Object, Object[])");
/*     */   
/*  39 */   private static final Signature NEW_INSTANCE = TypeUtils.parseSignature("Object newInstance(int, Object[])");
/*     */   
/*  41 */   private static final Signature GET_MAX_INDEX = TypeUtils.parseSignature("int getMaxIndex()");
/*     */   
/*  43 */   private static final Signature GET_SIGNATURE_WITHOUT_RETURN_TYPE = TypeUtils.parseSignature("String getSignatureWithoutReturnType(String, Class[])");
/*     */   
/*  45 */   private static final Type FAST_CLASS = TypeUtils.parseType("org.springframework.cglib.reflect.FastClass");
/*     */   
/*  47 */   private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
/*     */   
/*  49 */   private static final Type INVOCATION_TARGET_EXCEPTION = TypeUtils.parseType("java.lang.reflect.InvocationTargetException");
/*  50 */   private static final Type[] INVOCATION_TARGET_EXCEPTION_ARRAY = new Type[] { INVOCATION_TARGET_EXCEPTION }; private static final int TOO_MANY_METHODS = 100;
/*     */   
/*     */   public FastClassEmitter(ClassVisitor v, String className, Class type) {
/*  53 */     super(v);
/*     */     
/*  55 */     Type base = Type.getType(type);
/*  56 */     begin_class(52, 1, className, FAST_CLASS, null, "<generated>");
/*     */ 
/*     */     
/*  59 */     CodeEmitter e = begin_method(1, CSTRUCT_CLASS, null);
/*  60 */     e.load_this();
/*  61 */     e.load_args();
/*  62 */     e.super_invoke_constructor(CSTRUCT_CLASS);
/*  63 */     e.return_value();
/*  64 */     e.end_method();
/*     */     
/*  66 */     VisibilityPredicate vp = new VisibilityPredicate(type, false);
/*  67 */     List methods = ReflectUtils.addAllMethods(type, new ArrayList());
/*  68 */     CollectionUtils.filter(methods, (Predicate)vp);
/*  69 */     CollectionUtils.filter(methods, (Predicate)new DuplicatesPredicate());
/*  70 */     List constructors = new ArrayList(Arrays.asList((Object[])type.getDeclaredConstructors()));
/*  71 */     CollectionUtils.filter(constructors, (Predicate)vp);
/*     */ 
/*     */     
/*  74 */     emitIndexBySignature(methods);
/*     */ 
/*     */     
/*  77 */     emitIndexByClassArray(methods);
/*     */ 
/*     */     
/*  80 */     e = begin_method(1, CONSTRUCTOR_GET_INDEX, null);
/*  81 */     e.load_args();
/*  82 */     List info = CollectionUtils.transform(constructors, (Transformer)MethodInfoTransformer.getInstance());
/*  83 */     EmitUtils.constructor_switch(e, info, new GetIndexCallback(e, info));
/*  84 */     e.end_method();
/*     */ 
/*     */     
/*  87 */     e = begin_method(1, INVOKE, INVOCATION_TARGET_EXCEPTION_ARRAY);
/*  88 */     e.load_arg(1);
/*  89 */     e.checkcast(base);
/*  90 */     e.load_arg(0);
/*  91 */     invokeSwitchHelper(e, methods, 2, base);
/*  92 */     e.end_method();
/*     */ 
/*     */     
/*  95 */     e = begin_method(1, NEW_INSTANCE, INVOCATION_TARGET_EXCEPTION_ARRAY);
/*  96 */     e.new_instance(base);
/*  97 */     e.dup();
/*  98 */     e.load_arg(0);
/*  99 */     invokeSwitchHelper(e, constructors, 1, base);
/* 100 */     e.end_method();
/*     */ 
/*     */     
/* 103 */     e = begin_method(1, GET_MAX_INDEX, null);
/* 104 */     e.push(methods.size() - 1);
/* 105 */     e.return_value();
/* 106 */     e.end_method();
/*     */     
/* 108 */     end_class();
/*     */   }
/*     */ 
/*     */   
/*     */   private void emitIndexBySignature(List methods) {
/* 113 */     CodeEmitter e = begin_method(1, SIGNATURE_GET_INDEX, null);
/* 114 */     List signatures = CollectionUtils.transform(methods, new Transformer() {
/*     */           public Object transform(Object obj) {
/* 116 */             return ReflectUtils.getSignature((Method)obj).toString();
/*     */           }
/*     */         });
/* 119 */     e.load_arg(0);
/* 120 */     e.invoke_virtual(Constants.TYPE_OBJECT, TO_STRING);
/* 121 */     signatureSwitchHelper(e, signatures);
/* 122 */     e.end_method();
/*     */   }
/*     */ 
/*     */   
/*     */   private void emitIndexByClassArray(List methods) {
/* 127 */     CodeEmitter e = begin_method(1, METHOD_GET_INDEX, null);
/* 128 */     if (methods.size() > 100) {
/*     */       
/* 130 */       List signatures = CollectionUtils.transform(methods, new Transformer() {
/*     */             public Object transform(Object obj) {
/* 132 */               String s = ReflectUtils.getSignature((Method)obj).toString();
/* 133 */               return s.substring(0, s.lastIndexOf(')') + 1);
/*     */             }
/*     */           });
/* 136 */       e.load_args();
/* 137 */       e.invoke_static(FAST_CLASS, GET_SIGNATURE_WITHOUT_RETURN_TYPE);
/* 138 */       signatureSwitchHelper(e, signatures);
/*     */     } else {
/* 140 */       e.load_args();
/* 141 */       List info = CollectionUtils.transform(methods, (Transformer)MethodInfoTransformer.getInstance());
/* 142 */       EmitUtils.method_switch(e, info, new GetIndexCallback(e, info));
/*     */     } 
/* 144 */     e.end_method();
/*     */   }
/*     */   
/*     */   private void signatureSwitchHelper(final CodeEmitter e, final List signatures) {
/* 148 */     ObjectSwitchCallback callback = new ObjectSwitchCallback()
/*     */       {
/*     */         public void processCase(Object key, Label end) {
/* 151 */           e.push(signatures.indexOf(key));
/* 152 */           e.return_value();
/*     */         }
/*     */         public void processDefault() {
/* 155 */           e.push(-1);
/* 156 */           e.return_value();
/*     */         }
/*     */       };
/* 159 */     EmitUtils.string_switch(e, (String[])signatures
/* 160 */         .toArray((Object[])new String[signatures.size()]), 1, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void invokeSwitchHelper(final CodeEmitter e, List members, final int arg, final Type base) {
/* 166 */     final List info = CollectionUtils.transform(members, (Transformer)MethodInfoTransformer.getInstance());
/* 167 */     final Label illegalArg = e.make_label();
/* 168 */     Block block = e.begin_block();
/* 169 */     e.process_switch(getIntRange(info.size()), new ProcessSwitchCallback() {
/*     */           public void processCase(int key, Label end) {
/* 171 */             MethodInfo method = info.get(key);
/* 172 */             Type[] types = method.getSignature().getArgumentTypes();
/* 173 */             for (int i = 0; i < types.length; i++) {
/* 174 */               e.load_arg(arg);
/* 175 */               e.aaload(i);
/* 176 */               e.unbox(types[i]);
/*     */             } 
/*     */ 
/*     */             
/* 180 */             e.invoke(method, base);
/* 181 */             if (!TypeUtils.isConstructor(method)) {
/* 182 */               e.box(method.getSignature().getReturnType());
/*     */             }
/* 184 */             e.return_value();
/*     */           }
/*     */           public void processDefault() {
/* 187 */             e.goTo(illegalArg);
/*     */           }
/*     */         });
/* 190 */     block.end();
/* 191 */     EmitUtils.wrap_throwable(block, INVOCATION_TARGET_EXCEPTION);
/* 192 */     e.mark(illegalArg);
/* 193 */     e.throw_exception(ILLEGAL_ARGUMENT_EXCEPTION, "Cannot find matching method/constructor");
/*     */   }
/*     */   
/*     */   private static class GetIndexCallback implements ObjectSwitchCallback {
/*     */     private CodeEmitter e;
/* 198 */     private Map indexes = new HashMap<Object, Object>();
/*     */     
/*     */     public GetIndexCallback(CodeEmitter e, List methods) {
/* 201 */       this.e = e;
/* 202 */       int index = 0;
/* 203 */       for (Iterator it = methods.iterator(); it.hasNext();) {
/* 204 */         this.indexes.put(it.next(), new Integer(index++));
/*     */       }
/*     */     }
/*     */     
/*     */     public void processCase(Object key, Label end) {
/* 209 */       this.e.push(((Integer)this.indexes.get(key)).intValue());
/* 210 */       this.e.return_value();
/*     */     }
/*     */     
/*     */     public void processDefault() {
/* 214 */       this.e.push(-1);
/* 215 */       this.e.return_value();
/*     */     }
/*     */   }
/*     */   
/*     */   private static int[] getIntRange(int length) {
/* 220 */     int[] range = new int[length];
/* 221 */     for (int i = 0; i < length; i++) {
/* 222 */       range[i] = i;
/*     */     }
/* 224 */     return range;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\reflect\FastClassEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */