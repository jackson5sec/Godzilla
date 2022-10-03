/*     */ package javassist.tools.reflect;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Metaobject
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected ClassMetaobject classmetaobject;
/*     */   protected Metalevel baseobject;
/*     */   protected Method[] methods;
/*     */   
/*     */   public Metaobject(Object self, Object[] args) {
/*  64 */     this.baseobject = (Metalevel)self;
/*  65 */     this.classmetaobject = this.baseobject._getClass();
/*  66 */     this.methods = this.classmetaobject.getReflectiveMethods();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Metaobject() {
/*  75 */     this.baseobject = null;
/*  76 */     this.classmetaobject = null;
/*  77 */     this.methods = null;
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  81 */     out.writeObject(this.baseobject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  87 */     this.baseobject = (Metalevel)in.readObject();
/*  88 */     this.classmetaobject = this.baseobject._getClass();
/*  89 */     this.methods = this.classmetaobject.getReflectiveMethods();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClassMetaobject getClassMetaobject() {
/*  98 */     return this.classmetaobject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getObject() {
/* 105 */     return this.baseobject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setObject(Object self) {
/* 114 */     this.baseobject = (Metalevel)self;
/* 115 */     this.classmetaobject = this.baseobject._getClass();
/* 116 */     this.methods = this.classmetaobject.getReflectiveMethods();
/*     */ 
/*     */     
/* 119 */     this.baseobject._setMetaobject(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getMethodName(int identifier) {
/*     */     char c;
/* 127 */     String mname = this.methods[identifier].getName();
/* 128 */     int j = 3;
/*     */     do {
/* 130 */       c = mname.charAt(j++);
/* 131 */     } while (c >= '0' && '9' >= c);
/*     */ 
/*     */ 
/*     */     
/* 135 */     return mname.substring(j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?>[] getParameterTypes(int identifier) {
/* 144 */     return this.methods[identifier].getParameterTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<?> getReturnType(int identifier) {
/* 152 */     return this.methods[identifier].getReturnType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object trapFieldRead(String name) {
/* 163 */     Class<?> jc = getClassMetaobject().getJavaClass();
/*     */     try {
/* 165 */       return jc.getField(name).get(getObject());
/*     */     }
/* 167 */     catch (NoSuchFieldException e) {
/* 168 */       throw new RuntimeException(e.toString());
/*     */     }
/* 170 */     catch (IllegalAccessException e) {
/* 171 */       throw new RuntimeException(e.toString());
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
/*     */   public void trapFieldWrite(String name, Object value) {
/* 183 */     Class<?> jc = getClassMetaobject().getJavaClass();
/*     */     try {
/* 185 */       jc.getField(name).set(getObject(), value);
/*     */     }
/* 187 */     catch (NoSuchFieldException e) {
/* 188 */       throw new RuntimeException(e.toString());
/*     */     }
/* 190 */     catch (IllegalAccessException e) {
/* 191 */       throw new RuntimeException(e.toString());
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
/*     */   public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
/*     */     try {
/* 232 */       return this.methods[identifier].invoke(getObject(), args);
/*     */     }
/* 234 */     catch (InvocationTargetException e) {
/* 235 */       throw e.getTargetException();
/*     */     }
/* 237 */     catch (IllegalAccessException e) {
/* 238 */       throw new CannotInvokeException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\reflect\Metaobject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */