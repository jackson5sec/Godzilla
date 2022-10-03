/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MemberBox
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 6358550398665688245L;
/*     */   private transient Member memberObject;
/*     */   transient Class<?>[] argTypes;
/*     */   transient Object delegateTo;
/*     */   transient boolean vararg;
/*     */   
/*     */   MemberBox(Method method) {
/*  32 */     init(method);
/*     */   }
/*     */ 
/*     */   
/*     */   MemberBox(Constructor<?> constructor) {
/*  37 */     init(constructor);
/*     */   }
/*     */ 
/*     */   
/*     */   private void init(Method method) {
/*  42 */     this.memberObject = method;
/*  43 */     this.argTypes = method.getParameterTypes();
/*  44 */     this.vararg = VMBridge.instance.isVarArgs(method);
/*     */   }
/*     */ 
/*     */   
/*     */   private void init(Constructor<?> constructor) {
/*  49 */     this.memberObject = constructor;
/*  50 */     this.argTypes = constructor.getParameterTypes();
/*  51 */     this.vararg = VMBridge.instance.isVarArgs(constructor);
/*     */   }
/*     */ 
/*     */   
/*     */   Method method() {
/*  56 */     return (Method)this.memberObject;
/*     */   }
/*     */ 
/*     */   
/*     */   Constructor<?> ctor() {
/*  61 */     return (Constructor)this.memberObject;
/*     */   }
/*     */ 
/*     */   
/*     */   Member member() {
/*  66 */     return this.memberObject;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isMethod() {
/*  71 */     return this.memberObject instanceof Method;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isCtor() {
/*  76 */     return this.memberObject instanceof Constructor;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isStatic() {
/*  81 */     return Modifier.isStatic(this.memberObject.getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   String getName() {
/*  86 */     return this.memberObject.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   Class<?> getDeclaringClass() {
/*  91 */     return this.memberObject.getDeclaringClass();
/*     */   }
/*     */ 
/*     */   
/*     */   String toJavaDeclaration() {
/*  96 */     StringBuilder sb = new StringBuilder();
/*  97 */     if (isMethod()) {
/*  98 */       Method method = method();
/*  99 */       sb.append(method.getReturnType());
/* 100 */       sb.append(' ');
/* 101 */       sb.append(method.getName());
/*     */     } else {
/* 103 */       Constructor<?> ctor = ctor();
/* 104 */       String name = ctor.getDeclaringClass().getName();
/* 105 */       int lastDot = name.lastIndexOf('.');
/* 106 */       if (lastDot >= 0) {
/* 107 */         name = name.substring(lastDot + 1);
/*     */       }
/* 109 */       sb.append(name);
/*     */     } 
/* 111 */     sb.append(JavaMembers.liveConnectSignature(this.argTypes));
/* 112 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     return this.memberObject.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   Object invoke(Object target, Object[] args) {
/* 123 */     Method method = method();
/*     */     
/*     */     try {
/* 126 */       return method.invoke(target, args);
/* 127 */     } catch (IllegalAccessException ex) {
/* 128 */       Method accessible = searchAccessibleMethod(method, this.argTypes);
/* 129 */       if (accessible != null) {
/* 130 */         this.memberObject = accessible;
/* 131 */         method = accessible;
/*     */       }
/* 133 */       else if (!VMBridge.instance.tryToMakeAccessible(method)) {
/* 134 */         throw Context.throwAsScriptRuntimeEx(ex);
/*     */       } 
/*     */ 
/*     */       
/* 138 */       return method.invoke(target, args);
/*     */     }
/* 140 */     catch (InvocationTargetException ite) {
/*     */       
/* 142 */       Throwable e = ite;
/*     */       while (true)
/* 144 */       { e = ((InvocationTargetException)e).getTargetException();
/* 145 */         if (!(e instanceof InvocationTargetException))
/* 146 */         { if (e instanceof ContinuationPending)
/* 147 */             throw (ContinuationPending)e; 
/* 148 */           throw Context.throwAsScriptRuntimeEx(e); }  } 
/* 149 */     } catch (Exception ex) {
/* 150 */       throw Context.throwAsScriptRuntimeEx(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Object newInstance(Object[] args) {
/* 156 */     Constructor<?> ctor = ctor();
/*     */     
/*     */     try {
/* 159 */       return ctor.newInstance(args);
/* 160 */     } catch (IllegalAccessException ex) {
/* 161 */       if (!VMBridge.instance.tryToMakeAccessible(ctor)) {
/* 162 */         throw Context.throwAsScriptRuntimeEx(ex);
/*     */       }
/*     */       
/* 165 */       return ctor.newInstance(args);
/* 166 */     } catch (Exception ex) {
/* 167 */       throw Context.throwAsScriptRuntimeEx(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Method searchAccessibleMethod(Method method, Class<?>[] params) {
/* 173 */     int modifiers = method.getModifiers();
/* 174 */     if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
/* 175 */       Class<?> c = method.getDeclaringClass();
/* 176 */       if (!Modifier.isPublic(c.getModifiers())) {
/* 177 */         String name = method.getName();
/* 178 */         Class<?>[] intfs = c.getInterfaces();
/* 179 */         for (int i = 0, N = intfs.length; i != N; i++) {
/* 180 */           Class<?> intf = intfs[i];
/* 181 */           if (Modifier.isPublic(intf.getModifiers())) {
/*     */             
/* 183 */             try { return intf.getMethod(name, params); }
/* 184 */             catch (NoSuchMethodException ex) {  }
/* 185 */             catch (SecurityException ex) {}
/*     */           }
/*     */         } 
/*     */         while (true) {
/* 189 */           c = c.getSuperclass();
/* 190 */           if (c == null)
/* 191 */             break;  if (Modifier.isPublic(c.getModifiers())) {
/*     */             
/* 193 */             try { Method m = c.getMethod(name, params);
/* 194 */               int mModifiers = m.getModifiers();
/* 195 */               if (Modifier.isPublic(mModifiers) && !Modifier.isStatic(mModifiers))
/*     */               {
/*     */                 
/* 198 */                 return m;
/*     */               } }
/* 200 */             catch (NoSuchMethodException ex) {  }
/* 201 */             catch (SecurityException ex) {}
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 212 */     in.defaultReadObject();
/* 213 */     Member member = readMember(in);
/* 214 */     if (member instanceof Method) {
/* 215 */       init((Method)member);
/*     */     } else {
/* 217 */       init((Constructor)member);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 224 */     out.defaultWriteObject();
/* 225 */     writeMember(out, this.memberObject);
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
/*     */   private static void writeMember(ObjectOutputStream out, Member member) throws IOException {
/* 238 */     if (member == null) {
/* 239 */       out.writeBoolean(false);
/*     */       return;
/*     */     } 
/* 242 */     out.writeBoolean(true);
/* 243 */     if (!(member instanceof Method) && !(member instanceof Constructor))
/* 244 */       throw new IllegalArgumentException("not Method or Constructor"); 
/* 245 */     out.writeBoolean(member instanceof Method);
/* 246 */     out.writeObject(member.getName());
/* 247 */     out.writeObject(member.getDeclaringClass());
/* 248 */     if (member instanceof Method) {
/* 249 */       writeParameters(out, ((Method)member).getParameterTypes());
/*     */     } else {
/* 251 */       writeParameters(out, ((Constructor)member).getParameterTypes());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Member readMember(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 261 */     if (!in.readBoolean())
/* 262 */       return null; 
/* 263 */     boolean isMethod = in.readBoolean();
/* 264 */     String name = (String)in.readObject();
/* 265 */     Class<?> declaring = (Class)in.readObject();
/* 266 */     Class<?>[] parms = readParameters(in);
/*     */     try {
/* 268 */       if (isMethod) {
/* 269 */         return declaring.getMethod(name, parms);
/*     */       }
/* 271 */       return declaring.getConstructor(parms);
/*     */     }
/* 273 */     catch (NoSuchMethodException e) {
/* 274 */       throw new IOException("Cannot find member: " + e);
/*     */     } 
/*     */   }
/*     */   
/* 278 */   private static final Class<?>[] primitives = new Class[] { boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class, void.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeParameters(ObjectOutputStream out, Class<?>[] parms) throws IOException {
/* 299 */     out.writeShort(parms.length);
/*     */     
/* 301 */     for (int i = 0; i < parms.length; i++) {
/* 302 */       Class<?> parm = parms[i];
/* 303 */       boolean primitive = parm.isPrimitive();
/* 304 */       out.writeBoolean(primitive);
/* 305 */       if (!primitive) {
/* 306 */         out.writeObject(parm);
/*     */       } else {
/*     */         
/* 309 */         int j = 0; while (true) { if (j < primitives.length) {
/* 310 */             if (parm.equals(primitives[j])) {
/* 311 */               out.writeByte(j); break;
/*     */             }  j++;
/*     */             continue;
/*     */           } 
/* 315 */           throw new IllegalArgumentException("Primitive " + parm + " not found"); }
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<?>[] readParameters(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 326 */     Class<?>[] result = new Class[in.readShort()];
/* 327 */     for (int i = 0; i < result.length; i++) {
/* 328 */       if (!in.readBoolean()) {
/* 329 */         result[i] = (Class)in.readObject();
/*     */       } else {
/*     */         
/* 332 */         result[i] = primitives[in.readByte()];
/*     */       } 
/* 334 */     }  return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\MemberBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */