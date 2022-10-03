/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.ClassVisitor;
/*     */ import org.springframework.asm.FieldVisitor;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.transform.ClassTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassEmitter
/*     */   extends ClassTransformer
/*     */ {
/*     */   private ClassInfo classInfo;
/*     */   private Map fieldInfo;
/*     */   private static int hookCounter;
/*     */   private MethodVisitor rawStaticInit;
/*     */   private CodeEmitter staticInit;
/*     */   private CodeEmitter staticHook;
/*     */   private Signature staticHookSig;
/*     */   
/*     */   public ClassEmitter(ClassVisitor cv) {
/*  42 */     setTarget(cv);
/*     */   }
/*     */   
/*     */   public ClassEmitter() {
/*  46 */     super(Constants.ASM_API);
/*     */   }
/*     */   
/*     */   public void setTarget(ClassVisitor cv) {
/*  50 */     this.cv = cv;
/*  51 */     this.fieldInfo = new HashMap<Object, Object>();
/*     */ 
/*     */     
/*  54 */     this.staticInit = this.staticHook = null;
/*  55 */     this.staticHookSig = null;
/*     */   }
/*     */   
/*     */   private static synchronized int getNextHook() {
/*  59 */     return ++hookCounter;
/*     */   }
/*     */   
/*     */   public ClassInfo getClassInfo() {
/*  63 */     return this.classInfo;
/*     */   }
/*     */   
/*     */   public void begin_class(int version, final int access, String className, final Type superType, final Type[] interfaces, String source) {
/*  67 */     final Type classType = Type.getType("L" + className.replace('.', '/') + ";");
/*  68 */     this.classInfo = new ClassInfo() {
/*     */         public Type getType() {
/*  70 */           return classType;
/*     */         }
/*     */         public Type getSuperType() {
/*  73 */           return (superType != null) ? superType : Constants.TYPE_OBJECT;
/*     */         }
/*     */         public Type[] getInterfaces() {
/*  76 */           return interfaces;
/*     */         }
/*     */         public int getModifiers() {
/*  79 */           return access;
/*     */         }
/*     */       };
/*  82 */     this.cv.visit(version, access, this.classInfo
/*     */         
/*  84 */         .getType().getInternalName(), null, this.classInfo
/*     */         
/*  86 */         .getSuperType().getInternalName(), 
/*  87 */         TypeUtils.toInternalNames(interfaces));
/*  88 */     if (source != null)
/*  89 */       this.cv.visitSource(source, null); 
/*  90 */     init();
/*     */   }
/*     */   
/*     */   public CodeEmitter getStaticHook() {
/*  94 */     if (TypeUtils.isInterface(getAccess())) {
/*  95 */       throw new IllegalStateException("static hook is invalid for this class");
/*     */     }
/*  97 */     if (this.staticHook == null) {
/*  98 */       this.staticHookSig = new Signature("CGLIB$STATICHOOK" + getNextHook(), "()V");
/*  99 */       this.staticHook = begin_method(8, this.staticHookSig, null);
/*     */ 
/*     */       
/* 102 */       if (this.staticInit != null) {
/* 103 */         this.staticInit.invoke_static_this(this.staticHookSig);
/*     */       }
/*     */     } 
/* 106 */     return this.staticHook;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {}
/*     */   
/*     */   public int getAccess() {
/* 113 */     return this.classInfo.getModifiers();
/*     */   }
/*     */   
/*     */   public Type getClassType() {
/* 117 */     return this.classInfo.getType();
/*     */   }
/*     */   
/*     */   public Type getSuperType() {
/* 121 */     return this.classInfo.getSuperType();
/*     */   }
/*     */   
/*     */   public void end_class() {
/* 125 */     if (this.staticHook != null && this.staticInit == null)
/*     */     {
/* 127 */       begin_static();
/*     */     }
/* 129 */     if (this.staticInit != null) {
/* 130 */       this.staticHook.return_value();
/* 131 */       this.staticHook.end_method();
/* 132 */       this.rawStaticInit.visitInsn(177);
/* 133 */       this.rawStaticInit.visitMaxs(0, 0);
/* 134 */       this.staticInit = this.staticHook = null;
/* 135 */       this.staticHookSig = null;
/*     */     } 
/* 137 */     this.cv.visitEnd();
/*     */   }
/*     */   
/*     */   public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
/* 141 */     if (this.classInfo == null)
/* 142 */       throw new IllegalStateException("classInfo is null! " + this); 
/* 143 */     MethodVisitor v = this.cv.visitMethod(access, sig
/* 144 */         .getName(), sig
/* 145 */         .getDescriptor(), null, 
/*     */         
/* 147 */         TypeUtils.toInternalNames(exceptions));
/* 148 */     if (sig.equals(Constants.SIG_STATIC) && !TypeUtils.isInterface(getAccess())) {
/* 149 */       this.rawStaticInit = v;
/* 150 */       MethodVisitor wrapped = new MethodVisitor(Constants.ASM_API, v)
/*     */         {
/*     */           public void visitMaxs(int maxStack, int maxLocals) {}
/*     */           
/*     */           public void visitInsn(int insn) {
/* 155 */             if (insn != 177) {
/* 156 */               super.visitInsn(insn);
/*     */             }
/*     */           }
/*     */         };
/* 160 */       this.staticInit = new CodeEmitter(this, wrapped, access, sig, exceptions);
/* 161 */       if (this.staticHook == null) {
/*     */         
/* 163 */         getStaticHook();
/*     */       } else {
/* 165 */         this.staticInit.invoke_static_this(this.staticHookSig);
/*     */       } 
/* 167 */       return this.staticInit;
/* 168 */     }  if (sig.equals(this.staticHookSig)) {
/* 169 */       return new CodeEmitter(this, v, access, sig, exceptions) {
/*     */           public boolean isStaticHook() {
/* 171 */             return true;
/*     */           }
/*     */         };
/*     */     }
/* 175 */     return new CodeEmitter(this, v, access, sig, exceptions);
/*     */   }
/*     */ 
/*     */   
/*     */   public CodeEmitter begin_static() {
/* 180 */     return begin_method(8, Constants.SIG_STATIC, null);
/*     */   }
/*     */   
/*     */   public void declare_field(int access, String name, Type type, Object value) {
/* 184 */     FieldInfo existing = (FieldInfo)this.fieldInfo.get(name);
/* 185 */     FieldInfo info = new FieldInfo(access, name, type, value);
/* 186 */     if (existing != null) {
/* 187 */       if (!info.equals(existing)) {
/* 188 */         throw new IllegalArgumentException("Field \"" + name + "\" has been declared differently");
/*     */       }
/*     */     } else {
/* 191 */       this.fieldInfo.put(name, info);
/* 192 */       this.cv.visitField(access, name, type.getDescriptor(), null, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isFieldDeclared(String name) {
/* 198 */     return (this.fieldInfo.get(name) != null);
/*     */   }
/*     */   
/*     */   FieldInfo getFieldInfo(String name) {
/* 202 */     FieldInfo field = (FieldInfo)this.fieldInfo.get(name);
/* 203 */     if (field == null) {
/* 204 */       throw new IllegalArgumentException("Field " + name + " is not declared in " + getClassType().getClassName());
/*     */     }
/* 206 */     return field;
/*     */   }
/*     */   
/*     */   static class FieldInfo {
/*     */     int access;
/*     */     String name;
/*     */     Type type;
/*     */     Object value;
/*     */     
/*     */     public FieldInfo(int access, String name, Type type, Object value) {
/* 216 */       this.access = access;
/* 217 */       this.name = name;
/* 218 */       this.type = type;
/* 219 */       this.value = value;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 223 */       if (o == null)
/* 224 */         return false; 
/* 225 */       if (!(o instanceof FieldInfo))
/* 226 */         return false; 
/* 227 */       FieldInfo other = (FieldInfo)o;
/* 228 */       if (this.access != other.access || 
/* 229 */         !this.name.equals(other.name) || 
/* 230 */         !this.type.equals(other.type)) {
/* 231 */         return false;
/*     */       }
/* 233 */       if ((((this.value == null) ? 1 : 0) ^ ((other.value == null) ? 1 : 0)) != 0)
/* 234 */         return false; 
/* 235 */       if (this.value != null && !this.value.equals(other.value))
/* 236 */         return false; 
/* 237 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 241 */       return this.access ^ this.name.hashCode() ^ this.type.hashCode() ^ ((this.value == null) ? 0 : this.value.hashCode());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 251 */     begin_class(version, access, name
/*     */         
/* 253 */         .replace('/', '.'), 
/* 254 */         TypeUtils.fromInternalName(superName), 
/* 255 */         TypeUtils.fromInternalNames(interfaces), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd() {
/* 260 */     end_class();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 268 */     declare_field(access, name, Type.getType(desc), value);
/* 269 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 277 */     return begin_method(access, new Signature(name, desc), 
/*     */         
/* 279 */         TypeUtils.fromInternalNames(exceptions));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\ClassEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */