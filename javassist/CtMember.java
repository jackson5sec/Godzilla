/*     */ package javassist;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CtMember
/*     */ {
/*     */   CtMember next;
/*     */   protected CtClass declaringClass;
/*     */   
/*     */   static class Cache
/*     */     extends CtMember
/*     */   {
/*     */     private CtMember methodTail;
/*     */     private CtMember consTail;
/*     */     private CtMember fieldTail;
/*     */     
/*     */     protected void extendToString(StringBuffer buffer) {}
/*     */     
/*     */     public boolean hasAnnotation(String clz) {
/*  35 */       return false;
/*     */     }
/*     */     public Object getAnnotation(Class<?> clz) throws ClassNotFoundException {
/*  38 */       return null;
/*     */     }
/*     */     public Object[] getAnnotations() throws ClassNotFoundException {
/*  41 */       return null;
/*     */     } public byte[] getAttribute(String name) {
/*  43 */       return null;
/*     */     } public Object[] getAvailableAnnotations() {
/*  45 */       return null;
/*     */     } public int getModifiers() {
/*  47 */       return 0;
/*     */     } public String getName() {
/*  49 */       return null;
/*     */     } public String getSignature() {
/*  51 */       return null;
/*     */     }
/*     */     public void setAttribute(String name, byte[] data) {}
/*     */     public void setModifiers(int mod) {}
/*     */     
/*     */     public String getGenericSignature() {
/*  57 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setGenericSignature(String sig) {}
/*     */ 
/*     */     
/*     */     Cache(CtClassType decl) {
/*  66 */       super(decl);
/*  67 */       this.methodTail = this;
/*  68 */       this.consTail = this;
/*  69 */       this.fieldTail = this;
/*  70 */       this.fieldTail.next = this;
/*     */     }
/*     */     
/*  73 */     CtMember methodHead() { return this; }
/*  74 */     CtMember lastMethod() { return this.methodTail; }
/*  75 */     CtMember consHead() { return this.methodTail; }
/*  76 */     CtMember lastCons() { return this.consTail; }
/*  77 */     CtMember fieldHead() { return this.consTail; } CtMember lastField() {
/*  78 */       return this.fieldTail;
/*     */     }
/*     */     void addMethod(CtMember method) {
/*  81 */       method.next = this.methodTail.next;
/*  82 */       this.methodTail.next = method;
/*  83 */       if (this.methodTail == this.consTail) {
/*  84 */         this.consTail = method;
/*  85 */         if (this.methodTail == this.fieldTail) {
/*  86 */           this.fieldTail = method;
/*     */         }
/*     */       } 
/*  89 */       this.methodTail = method;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void addConstructor(CtMember cons) {
/*  95 */       cons.next = this.consTail.next;
/*  96 */       this.consTail.next = cons;
/*  97 */       if (this.consTail == this.fieldTail) {
/*  98 */         this.fieldTail = cons;
/*     */       }
/* 100 */       this.consTail = cons;
/*     */     }
/*     */     
/*     */     void addField(CtMember field) {
/* 104 */       field.next = this;
/* 105 */       this.fieldTail.next = field;
/* 106 */       this.fieldTail = field;
/*     */     }
/*     */     
/*     */     static int count(CtMember head, CtMember tail) {
/* 110 */       int n = 0;
/* 111 */       while (head != tail) {
/* 112 */         n++;
/* 113 */         head = head.next;
/*     */       } 
/*     */       
/* 116 */       return n;
/*     */     }
/*     */     
/*     */     void remove(CtMember mem) {
/* 120 */       CtMember m = this;
/*     */       CtMember node;
/* 122 */       while ((node = m.next) != this) {
/* 123 */         if (node == mem) {
/* 124 */           m.next = node.next;
/* 125 */           if (node == this.methodTail) {
/* 126 */             this.methodTail = m;
/*     */           }
/* 128 */           if (node == this.consTail) {
/* 129 */             this.consTail = m;
/*     */           }
/* 131 */           if (node == this.fieldTail) {
/* 132 */             this.fieldTail = m;
/*     */           }
/*     */           break;
/*     */         } 
/* 136 */         m = m.next;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected CtMember(CtClass clazz) {
/* 142 */     this.declaringClass = clazz;
/* 143 */     this.next = null;
/*     */   }
/*     */   final CtMember next() {
/* 146 */     return this.next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void nameReplaced() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     StringBuffer buffer = new StringBuffer(getClass().getName());
/* 159 */     buffer.append("@");
/* 160 */     buffer.append(Integer.toHexString(hashCode()));
/* 161 */     buffer.append("[");
/* 162 */     buffer.append(Modifier.toString(getModifiers()));
/* 163 */     extendToString(buffer);
/* 164 */     buffer.append("]");
/* 165 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void extendToString(StringBuffer paramStringBuffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getDeclaringClass() {
/* 180 */     return this.declaringClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visibleFrom(CtClass clazz) {
/*     */     boolean visible;
/* 186 */     int mod = getModifiers();
/* 187 */     if (Modifier.isPublic(mod))
/* 188 */       return true; 
/* 189 */     if (Modifier.isPrivate(mod)) {
/* 190 */       return (clazz == this.declaringClass);
/*     */     }
/* 192 */     String declName = this.declaringClass.getPackageName();
/* 193 */     String fromName = clazz.getPackageName();
/*     */     
/* 195 */     if (declName == null) {
/* 196 */       visible = (fromName == null);
/*     */     } else {
/* 198 */       visible = declName.equals(fromName);
/*     */     } 
/* 200 */     if (!visible && Modifier.isProtected(mod)) {
/* 201 */       return clazz.subclassOf(this.declaringClass);
/*     */     }
/* 203 */     return visible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getModifiers();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setModifiers(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(Class<?> clz) {
/* 231 */     return hasAnnotation(clz.getName());
/*     */   }
/*     */   
/*     */   public abstract boolean hasAnnotation(String paramString);
/*     */   
/*     */   public abstract Object getAnnotation(Class<?> paramClass) throws ClassNotFoundException;
/*     */   
/*     */   public abstract Object[] getAnnotations() throws ClassNotFoundException;
/*     */   
/*     */   public abstract Object[] getAvailableAnnotations();
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract String getSignature();
/*     */   
/*     */   public abstract String getGenericSignature();
/*     */   
/*     */   public abstract void setGenericSignature(String paramString);
/*     */   
/*     */   public abstract byte[] getAttribute(String paramString);
/*     */   
/*     */   public abstract void setAttribute(String paramString, byte[] paramArrayOfbyte);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */