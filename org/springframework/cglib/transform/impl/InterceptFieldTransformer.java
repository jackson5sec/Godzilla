/*     */ package org.springframework.cglib.transform.impl;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.Local;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.core.TypeUtils;
/*     */ import org.springframework.cglib.transform.ClassEmitterTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InterceptFieldTransformer
/*     */   extends ClassEmitterTransformer
/*     */ {
/*     */   private static final String CALLBACK_FIELD = "$CGLIB_READ_WRITE_CALLBACK";
/*  29 */   private static final Type CALLBACK = TypeUtils.parseType("org.springframework.cglib.transform.impl.InterceptFieldCallback");
/*     */   
/*  31 */   private static final Type ENABLED = TypeUtils.parseType("org.springframework.cglib.transform.impl.InterceptFieldEnabled");
/*  32 */   private static final Signature ENABLED_SET = new Signature("setInterceptFieldCallback", Type.VOID_TYPE, new Type[] { CALLBACK });
/*     */   
/*  34 */   private static final Signature ENABLED_GET = new Signature("getInterceptFieldCallback", CALLBACK, new Type[0]);
/*     */   
/*     */   private InterceptFieldFilter filter;
/*     */ 
/*     */   
/*     */   public InterceptFieldTransformer(InterceptFieldFilter filter) {
/*  40 */     this.filter = filter;
/*     */   }
/*     */   
/*     */   public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String sourceFile) {
/*  44 */     if (!TypeUtils.isInterface(access)) {
/*  45 */       super.begin_class(version, access, className, superType, TypeUtils.add(interfaces, ENABLED), sourceFile);
/*     */       
/*  47 */       super.declare_field(130, "$CGLIB_READ_WRITE_CALLBACK", CALLBACK, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  53 */       CodeEmitter e = super.begin_method(1, ENABLED_GET, null);
/*  54 */       e.load_this();
/*  55 */       e.getfield("$CGLIB_READ_WRITE_CALLBACK");
/*  56 */       e.return_value();
/*  57 */       e.end_method();
/*     */       
/*  59 */       e = super.begin_method(1, ENABLED_SET, null);
/*  60 */       e.load_this();
/*  61 */       e.load_arg(0);
/*  62 */       e.putfield("$CGLIB_READ_WRITE_CALLBACK");
/*  63 */       e.return_value();
/*  64 */       e.end_method();
/*     */     } else {
/*  66 */       super.begin_class(version, access, className, superType, interfaces, sourceFile);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void declare_field(int access, String name, Type type, Object value) {
/*  71 */     super.declare_field(access, name, type, value);
/*  72 */     if (!TypeUtils.isStatic(access)) {
/*  73 */       if (this.filter.acceptRead(getClassType(), name)) {
/*  74 */         addReadMethod(name, type);
/*     */       }
/*  76 */       if (this.filter.acceptWrite(getClassType(), name)) {
/*  77 */         addWriteMethod(name, type);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addReadMethod(String name, Type type) {
/*  83 */     CodeEmitter e = super.begin_method(1, 
/*  84 */         readMethodSig(name, type.getDescriptor()), null);
/*     */     
/*  86 */     e.load_this();
/*  87 */     e.getfield(name);
/*  88 */     e.load_this();
/*  89 */     e.invoke_interface(ENABLED, ENABLED_GET);
/*  90 */     Label intercept = e.make_label();
/*  91 */     e.ifnonnull(intercept);
/*  92 */     e.return_value();
/*     */     
/*  94 */     e.mark(intercept);
/*  95 */     Local result = e.make_local(type);
/*  96 */     e.store_local(result);
/*  97 */     e.load_this();
/*  98 */     e.invoke_interface(ENABLED, ENABLED_GET);
/*  99 */     e.load_this();
/* 100 */     e.push(name);
/* 101 */     e.load_local(result);
/* 102 */     e.invoke_interface(CALLBACK, readCallbackSig(type));
/* 103 */     if (!TypeUtils.isPrimitive(type)) {
/* 104 */       e.checkcast(type);
/*     */     }
/* 106 */     e.return_value();
/* 107 */     e.end_method();
/*     */   }
/*     */   
/*     */   private void addWriteMethod(String name, Type type) {
/* 111 */     CodeEmitter e = super.begin_method(1, 
/* 112 */         writeMethodSig(name, type.getDescriptor()), null);
/*     */     
/* 114 */     e.load_this();
/* 115 */     e.dup();
/* 116 */     e.invoke_interface(ENABLED, ENABLED_GET);
/* 117 */     Label skip = e.make_label();
/* 118 */     e.ifnull(skip);
/*     */     
/* 120 */     e.load_this();
/* 121 */     e.invoke_interface(ENABLED, ENABLED_GET);
/* 122 */     e.load_this();
/* 123 */     e.push(name);
/* 124 */     e.load_this();
/* 125 */     e.getfield(name);
/* 126 */     e.load_arg(0);
/* 127 */     e.invoke_interface(CALLBACK, writeCallbackSig(type));
/* 128 */     if (!TypeUtils.isPrimitive(type)) {
/* 129 */       e.checkcast(type);
/*     */     }
/* 131 */     Label go = e.make_label();
/* 132 */     e.goTo(go);
/* 133 */     e.mark(skip);
/* 134 */     e.load_arg(0);
/* 135 */     e.mark(go);
/* 136 */     e.putfield(name);
/* 137 */     e.return_value();
/* 138 */     e.end_method();
/*     */   }
/*     */   
/*     */   public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
/* 142 */     return new CodeEmitter(super.begin_method(access, sig, exceptions)) {
/*     */         public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/* 144 */           Type towner = TypeUtils.fromInternalName(owner);
/* 145 */           switch (opcode) {
/*     */             case 180:
/* 147 */               if (InterceptFieldTransformer.this.filter.acceptRead(towner, name)) {
/* 148 */                 helper(towner, InterceptFieldTransformer.readMethodSig(name, desc));
/*     */                 return;
/*     */               } 
/*     */               break;
/*     */             case 181:
/* 153 */               if (InterceptFieldTransformer.this.filter.acceptWrite(towner, name)) {
/* 154 */                 helper(towner, InterceptFieldTransformer.writeMethodSig(name, desc));
/*     */                 return;
/*     */               } 
/*     */               break;
/*     */           } 
/* 159 */           super.visitFieldInsn(opcode, owner, name, desc);
/*     */         }
/*     */         
/*     */         private void helper(Type owner, Signature sig) {
/* 163 */           invoke_virtual(owner, sig);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static Signature readMethodSig(String name, String desc) {
/* 169 */     return new Signature("$cglib_read_" + name, "()" + desc);
/*     */   }
/*     */   
/*     */   private static Signature writeMethodSig(String name, String desc) {
/* 173 */     return new Signature("$cglib_write_" + name, "(" + desc + ")V");
/*     */   }
/*     */   
/*     */   private static Signature readCallbackSig(Type type) {
/* 177 */     Type remap = remap(type);
/* 178 */     return new Signature("read" + callbackName(remap), remap, new Type[] { Constants.TYPE_OBJECT, Constants.TYPE_STRING, remap });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Signature writeCallbackSig(Type type) {
/* 186 */     Type remap = remap(type);
/* 187 */     return new Signature("write" + callbackName(remap), remap, new Type[] { Constants.TYPE_OBJECT, Constants.TYPE_STRING, remap, remap });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Type remap(Type type) {
/* 196 */     switch (type.getSort()) {
/*     */       case 9:
/*     */       case 10:
/* 199 */         return Constants.TYPE_OBJECT;
/*     */     } 
/* 201 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String callbackName(Type type) {
/* 206 */     return (type == Constants.TYPE_OBJECT) ? "Object" : 
/*     */       
/* 208 */       TypeUtils.upperFirst(TypeUtils.getClassName(type));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\InterceptFieldTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */