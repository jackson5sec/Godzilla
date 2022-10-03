/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiArrayType
/*     */   extends Type
/*     */ {
/*     */   private MultiType component;
/*     */   private int dims;
/*     */   
/*     */   public MultiArrayType(MultiType component, int dims) {
/*  32 */     super(null);
/*  33 */     this.component = component;
/*  34 */     this.dims = dims;
/*     */   }
/*     */ 
/*     */   
/*     */   public CtClass getCtClass() {
/*  39 */     CtClass clazz = this.component.getCtClass();
/*  40 */     if (clazz == null) {
/*  41 */       return null;
/*     */     }
/*  43 */     ClassPool pool = clazz.getClassPool();
/*  44 */     if (pool == null) {
/*  45 */       pool = ClassPool.getDefault();
/*     */     }
/*  47 */     String name = arrayName(clazz.getName(), this.dims);
/*     */     
/*     */     try {
/*  50 */       return pool.get(name);
/*  51 */     } catch (NotFoundException e) {
/*  52 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean popChanged() {
/*  58 */     return this.component.popChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDimensions() {
/*  63 */     return this.dims;
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getComponent() {
/*  68 */     return (this.dims == 1) ? this.component : new MultiArrayType(this.component, this.dims - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/*  73 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAssignableFrom(Type type) {
/*  83 */     throw new UnsupportedOperationException("Not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReference() {
/*  88 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isAssignableTo(Type type) {
/*  92 */     if (eq(type.getCtClass(), Type.OBJECT.getCtClass())) {
/*  93 */       return true;
/*     */     }
/*  95 */     if (eq(type.getCtClass(), Type.CLONEABLE.getCtClass())) {
/*  96 */       return true;
/*     */     }
/*  98 */     if (eq(type.getCtClass(), Type.SERIALIZABLE.getCtClass())) {
/*  99 */       return true;
/*     */     }
/* 101 */     if (!type.isArray()) {
/* 102 */       return false;
/*     */     }
/* 104 */     Type typeRoot = getRootComponent(type);
/* 105 */     int typeDims = type.getDimensions();
/*     */     
/* 107 */     if (typeDims > this.dims) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (typeDims < this.dims) {
/* 111 */       if (eq(typeRoot.getCtClass(), Type.OBJECT.getCtClass())) {
/* 112 */         return true;
/*     */       }
/* 114 */       if (eq(typeRoot.getCtClass(), Type.CLONEABLE.getCtClass())) {
/* 115 */         return true;
/*     */       }
/* 117 */       if (eq(typeRoot.getCtClass(), Type.SERIALIZABLE.getCtClass())) {
/* 118 */         return true;
/*     */       }
/* 120 */       return false;
/*     */     } 
/*     */     
/* 123 */     return this.component.isAssignableTo(typeRoot);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 129 */     return this.component.hashCode() + this.dims;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 134 */     if (!(o instanceof MultiArrayType))
/* 135 */       return false; 
/* 136 */     MultiArrayType multi = (MultiArrayType)o;
/*     */     
/* 138 */     return (this.component.equals(multi.component) && this.dims == multi.dims);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     return arrayName(this.component.toString(), this.dims);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\MultiArrayType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */