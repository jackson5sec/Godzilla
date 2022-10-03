/*     */ package javassist;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import javassist.bytecode.Descriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassMap
/*     */   extends HashMap<String, String>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private ClassMap parent;
/*     */   
/*     */   public ClassMap() {
/*  60 */     this.parent = null;
/*     */   } ClassMap(ClassMap map) {
/*  62 */     this.parent = map;
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
/*     */   public void put(CtClass oldname, CtClass newname) {
/*  75 */     put(oldname.getName(), newname.getName());
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
/*     */   public String put(String oldname, String newname) {
/*  98 */     if (oldname == newname) {
/*  99 */       return oldname;
/*     */     }
/* 101 */     String oldname2 = toJvmName(oldname);
/* 102 */     String s = get(oldname2);
/* 103 */     if (s == null || !s.equals(oldname2))
/* 104 */       return super.put(oldname2, toJvmName(newname)); 
/* 105 */     return s;
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
/*     */   public void putIfNone(String oldname, String newname) {
/* 118 */     if (oldname == newname) {
/*     */       return;
/*     */     }
/* 121 */     String oldname2 = toJvmName(oldname);
/* 122 */     String s = get(oldname2);
/* 123 */     if (s == null)
/* 124 */       super.put(oldname2, toJvmName(newname)); 
/*     */   }
/*     */   
/*     */   protected final String put0(String oldname, String newname) {
/* 128 */     return super.put(oldname, newname);
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
/*     */   public String get(Object jvmClassName) {
/* 143 */     String found = super.get(jvmClassName);
/* 144 */     if (found == null && this.parent != null)
/* 145 */       return this.parent.get(jvmClassName); 
/* 146 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fix(CtClass clazz) {
/* 152 */     fix(clazz.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fix(String name) {
/* 159 */     String name2 = toJvmName(name);
/* 160 */     super.put(name2, name2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJvmName(String classname) {
/* 168 */     return Descriptor.toJvmName(classname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJavaName(String classname) {
/* 176 */     return Descriptor.toJavaName(classname);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\ClassMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */