/*     */ package javassist.compiler;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.AttributeInfo;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.ExceptionsAttribute;
/*     */ import javassist.bytecode.FieldInfo;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.SyntheticAttribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AccessorMaker
/*     */ {
/*     */   private CtClass clazz;
/*     */   private int uniqueNumber;
/*     */   private Map<String, Object> accessors;
/*     */   static final String lastParamType = "javassist.runtime.Inner";
/*     */   
/*     */   public AccessorMaker(CtClass c) {
/*  48 */     this.clazz = c;
/*  49 */     this.uniqueNumber = 1;
/*  50 */     this.accessors = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConstructor(CtClass c, String desc, MethodInfo orig) throws CompileError {
/*  56 */     String key = "<init>:" + desc;
/*  57 */     String consDesc = (String)this.accessors.get(key);
/*  58 */     if (consDesc != null) {
/*  59 */       return consDesc;
/*     */     }
/*  61 */     consDesc = Descriptor.appendParameter("javassist.runtime.Inner", desc);
/*  62 */     ClassFile cf = this.clazz.getClassFile();
/*     */     try {
/*  64 */       ConstPool cp = cf.getConstPool();
/*  65 */       ClassPool pool = this.clazz.getClassPool();
/*  66 */       MethodInfo minfo = new MethodInfo(cp, "<init>", consDesc);
/*     */       
/*  68 */       minfo.setAccessFlags(0);
/*  69 */       minfo.addAttribute((AttributeInfo)new SyntheticAttribute(cp));
/*  70 */       ExceptionsAttribute ea = orig.getExceptionsAttribute();
/*  71 */       if (ea != null) {
/*  72 */         minfo.addAttribute(ea.copy(cp, null));
/*     */       }
/*  74 */       CtClass[] params = Descriptor.getParameterTypes(desc, pool);
/*  75 */       Bytecode code = new Bytecode(cp);
/*  76 */       code.addAload(0);
/*  77 */       int regno = 1;
/*  78 */       for (int i = 0; i < params.length; i++)
/*  79 */         regno += code.addLoad(regno, params[i]); 
/*  80 */       code.setMaxLocals(regno + 1);
/*  81 */       code.addInvokespecial(this.clazz, "<init>", desc);
/*     */       
/*  83 */       code.addReturn(null);
/*  84 */       minfo.setCodeAttribute(code.toCodeAttribute());
/*  85 */       cf.addMethod(minfo);
/*     */     }
/*  87 */     catch (CannotCompileException e) {
/*  88 */       throw new CompileError(e);
/*     */     }
/*  90 */     catch (NotFoundException e) {
/*  91 */       throw new CompileError(e);
/*     */     } 
/*     */     
/*  94 */     this.accessors.put(key, consDesc);
/*  95 */     return consDesc;
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
/*     */   public String getMethodAccessor(String name, String desc, String accDesc, MethodInfo orig) throws CompileError {
/* 115 */     String key = name + ":" + desc;
/* 116 */     String accName = (String)this.accessors.get(key);
/* 117 */     if (accName != null) {
/* 118 */       return accName;
/*     */     }
/* 120 */     ClassFile cf = this.clazz.getClassFile();
/* 121 */     accName = findAccessorName(cf);
/*     */     try {
/* 123 */       ConstPool cp = cf.getConstPool();
/* 124 */       ClassPool pool = this.clazz.getClassPool();
/* 125 */       MethodInfo minfo = new MethodInfo(cp, accName, accDesc);
/*     */       
/* 127 */       minfo.setAccessFlags(8);
/* 128 */       minfo.addAttribute((AttributeInfo)new SyntheticAttribute(cp));
/* 129 */       ExceptionsAttribute ea = orig.getExceptionsAttribute();
/* 130 */       if (ea != null) {
/* 131 */         minfo.addAttribute(ea.copy(cp, null));
/*     */       }
/* 133 */       CtClass[] params = Descriptor.getParameterTypes(accDesc, pool);
/* 134 */       int regno = 0;
/* 135 */       Bytecode code = new Bytecode(cp);
/* 136 */       for (int i = 0; i < params.length; i++) {
/* 137 */         regno += code.addLoad(regno, params[i]);
/*     */       }
/* 139 */       code.setMaxLocals(regno);
/* 140 */       if (desc == accDesc) {
/* 141 */         code.addInvokestatic(this.clazz, name, desc);
/*     */       } else {
/* 143 */         code.addInvokevirtual(this.clazz, name, desc);
/*     */       } 
/* 145 */       code.addReturn(Descriptor.getReturnType(desc, pool));
/* 146 */       minfo.setCodeAttribute(code.toCodeAttribute());
/* 147 */       cf.addMethod(minfo);
/*     */     }
/* 149 */     catch (CannotCompileException e) {
/* 150 */       throw new CompileError(e);
/*     */     }
/* 152 */     catch (NotFoundException e) {
/* 153 */       throw new CompileError(e);
/*     */     } 
/*     */     
/* 156 */     this.accessors.put(key, accName);
/* 157 */     return accName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getFieldGetter(FieldInfo finfo, boolean is_static) throws CompileError {
/* 166 */     String fieldName = finfo.getName();
/* 167 */     String key = fieldName + ":getter";
/* 168 */     Object res = this.accessors.get(key);
/* 169 */     if (res != null) {
/* 170 */       return (MethodInfo)res;
/*     */     }
/* 172 */     ClassFile cf = this.clazz.getClassFile();
/* 173 */     String accName = findAccessorName(cf); try {
/*     */       String accDesc;
/* 175 */       ConstPool cp = cf.getConstPool();
/* 176 */       ClassPool pool = this.clazz.getClassPool();
/* 177 */       String fieldType = finfo.getDescriptor();
/*     */       
/* 179 */       if (is_static) {
/* 180 */         accDesc = "()" + fieldType;
/*     */       } else {
/* 182 */         accDesc = "(" + Descriptor.of(this.clazz) + ")" + fieldType;
/*     */       } 
/* 184 */       MethodInfo minfo = new MethodInfo(cp, accName, accDesc);
/* 185 */       minfo.setAccessFlags(8);
/* 186 */       minfo.addAttribute((AttributeInfo)new SyntheticAttribute(cp));
/* 187 */       Bytecode code = new Bytecode(cp);
/* 188 */       if (is_static) {
/* 189 */         code.addGetstatic(Bytecode.THIS, fieldName, fieldType);
/*     */       } else {
/*     */         
/* 192 */         code.addAload(0);
/* 193 */         code.addGetfield(Bytecode.THIS, fieldName, fieldType);
/* 194 */         code.setMaxLocals(1);
/*     */       } 
/*     */       
/* 197 */       code.addReturn(Descriptor.toCtClass(fieldType, pool));
/* 198 */       minfo.setCodeAttribute(code.toCodeAttribute());
/* 199 */       cf.addMethod(minfo);
/* 200 */       this.accessors.put(key, minfo);
/* 201 */       return minfo;
/*     */     }
/* 203 */     catch (CannotCompileException e) {
/* 204 */       throw new CompileError(e);
/*     */     }
/* 206 */     catch (NotFoundException e) {
/* 207 */       throw new CompileError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getFieldSetter(FieldInfo finfo, boolean is_static) throws CompileError {
/* 217 */     String fieldName = finfo.getName();
/* 218 */     String key = fieldName + ":setter";
/* 219 */     Object res = this.accessors.get(key);
/* 220 */     if (res != null) {
/* 221 */       return (MethodInfo)res;
/*     */     }
/* 223 */     ClassFile cf = this.clazz.getClassFile();
/* 224 */     String accName = findAccessorName(cf); try {
/*     */       String accDesc; int reg;
/* 226 */       ConstPool cp = cf.getConstPool();
/* 227 */       ClassPool pool = this.clazz.getClassPool();
/* 228 */       String fieldType = finfo.getDescriptor();
/*     */       
/* 230 */       if (is_static) {
/* 231 */         accDesc = "(" + fieldType + ")V";
/*     */       } else {
/* 233 */         accDesc = "(" + Descriptor.of(this.clazz) + fieldType + ")V";
/*     */       } 
/* 235 */       MethodInfo minfo = new MethodInfo(cp, accName, accDesc);
/* 236 */       minfo.setAccessFlags(8);
/* 237 */       minfo.addAttribute((AttributeInfo)new SyntheticAttribute(cp));
/* 238 */       Bytecode code = new Bytecode(cp);
/*     */       
/* 240 */       if (is_static) {
/* 241 */         reg = code.addLoad(0, Descriptor.toCtClass(fieldType, pool));
/* 242 */         code.addPutstatic(Bytecode.THIS, fieldName, fieldType);
/*     */       } else {
/*     */         
/* 245 */         code.addAload(0);
/* 246 */         reg = code.addLoad(1, Descriptor.toCtClass(fieldType, pool)) + 1;
/*     */         
/* 248 */         code.addPutfield(Bytecode.THIS, fieldName, fieldType);
/*     */       } 
/*     */       
/* 251 */       code.addReturn(null);
/* 252 */       code.setMaxLocals(reg);
/* 253 */       minfo.setCodeAttribute(code.toCodeAttribute());
/* 254 */       cf.addMethod(minfo);
/* 255 */       this.accessors.put(key, minfo);
/* 256 */       return minfo;
/*     */     }
/* 258 */     catch (CannotCompileException e) {
/* 259 */       throw new CompileError(e);
/*     */     }
/* 261 */     catch (NotFoundException e) {
/* 262 */       throw new CompileError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String findAccessorName(ClassFile cf) {
/*     */     while (true) {
/* 269 */       String accName = "access$" + this.uniqueNumber++;
/* 270 */       if (cf.getMethod(accName) == null)
/* 271 */         return accName; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\AccessorMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */