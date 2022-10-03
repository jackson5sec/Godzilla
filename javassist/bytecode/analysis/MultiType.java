/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javassist.CtClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiType
/*     */   extends Type
/*     */ {
/*     */   private Map<String, CtClass> interfaces;
/*     */   private Type resolved;
/*     */   private Type potentialClass;
/*     */   private MultiType mergeSource;
/*     */   private boolean changed = false;
/*     */   
/*     */   public MultiType(Map<String, CtClass> interfaces) {
/*  56 */     this(interfaces, (Type)null);
/*     */   }
/*     */   
/*     */   public MultiType(Map<String, CtClass> interfaces, Type potentialClass) {
/*  60 */     super(null);
/*  61 */     this.interfaces = interfaces;
/*  62 */     this.potentialClass = potentialClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getCtClass() {
/*  71 */     if (this.resolved != null) {
/*  72 */       return this.resolved.getCtClass();
/*     */     }
/*  74 */     return Type.OBJECT.getCtClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getComponent() {
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/*  90 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean popChanged() {
/* 106 */     boolean changed = this.changed;
/* 107 */     this.changed = false;
/* 108 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAssignableFrom(Type type) {
/* 113 */     throw new UnsupportedOperationException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean isAssignableTo(Type type) {
/* 117 */     if (this.resolved != null) {
/* 118 */       return type.isAssignableFrom(this.resolved);
/*     */     }
/* 120 */     if (Type.OBJECT.equals(type)) {
/* 121 */       return true;
/*     */     }
/* 123 */     if (this.potentialClass != null && !type.isAssignableFrom(this.potentialClass)) {
/* 124 */       this.potentialClass = null;
/*     */     }
/* 126 */     Map<String, CtClass> map = mergeMultiAndSingle(this, type);
/*     */     
/* 128 */     if (map.size() == 1 && this.potentialClass == null) {
/*     */       
/* 130 */       this.resolved = Type.get(map.values().iterator().next());
/* 131 */       propogateResolved();
/*     */       
/* 133 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     if (map.size() >= 1) {
/* 138 */       this.interfaces = map;
/* 139 */       propogateState();
/*     */       
/* 141 */       return true;
/*     */     } 
/*     */     
/* 144 */     if (this.potentialClass != null) {
/* 145 */       this.resolved = this.potentialClass;
/* 146 */       propogateResolved();
/*     */       
/* 148 */       return true;
/*     */     } 
/*     */     
/* 151 */     return false;
/*     */   }
/*     */   
/*     */   private void propogateState() {
/* 155 */     MultiType source = this.mergeSource;
/* 156 */     while (source != null) {
/* 157 */       source.interfaces = this.interfaces;
/* 158 */       source.potentialClass = this.potentialClass;
/* 159 */       source = source.mergeSource;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void propogateResolved() {
/* 164 */     MultiType source = this.mergeSource;
/* 165 */     while (source != null) {
/* 166 */       source.resolved = this.resolved;
/* 167 */       source = source.mergeSource;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReference() {
/* 178 */     return true;
/*     */   }
/*     */   
/*     */   private Map<String, CtClass> getAllMultiInterfaces(MultiType type) {
/* 182 */     Map<String, CtClass> map = new HashMap<>();
/*     */     
/* 184 */     for (CtClass intf : type.interfaces.values()) {
/* 185 */       map.put(intf.getName(), intf);
/* 186 */       getAllInterfaces(intf, map);
/*     */     } 
/*     */     
/* 189 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, CtClass> mergeMultiInterfaces(MultiType type1, MultiType type2) {
/* 194 */     Map<String, CtClass> map1 = getAllMultiInterfaces(type1);
/* 195 */     Map<String, CtClass> map2 = getAllMultiInterfaces(type2);
/*     */     
/* 197 */     return findCommonInterfaces(map1, map2);
/*     */   }
/*     */   
/*     */   private Map<String, CtClass> mergeMultiAndSingle(MultiType multi, Type single) {
/* 201 */     Map<String, CtClass> map1 = getAllMultiInterfaces(multi);
/* 202 */     Map<String, CtClass> map2 = getAllInterfaces(single.getCtClass(), null);
/*     */     
/* 204 */     return findCommonInterfaces(map1, map2);
/*     */   }
/*     */   
/*     */   private boolean inMergeSource(MultiType source) {
/* 208 */     while (source != null) {
/* 209 */       if (source == this) {
/* 210 */         return true;
/*     */       }
/* 212 */       source = source.mergeSource;
/*     */     } 
/*     */     
/* 215 */     return false;
/*     */   }
/*     */   
/*     */   public Type merge(Type type) {
/*     */     Map<String, CtClass> merged;
/* 220 */     if (this == type) {
/* 221 */       return this;
/*     */     }
/* 223 */     if (type == UNINIT) {
/* 224 */       return this;
/*     */     }
/* 226 */     if (type == BOGUS) {
/* 227 */       return BOGUS;
/*     */     }
/* 229 */     if (type == null) {
/* 230 */       return this;
/*     */     }
/* 232 */     if (this.resolved != null) {
/* 233 */       return this.resolved.merge(type);
/*     */     }
/* 235 */     if (this.potentialClass != null) {
/* 236 */       Type mergePotential = this.potentialClass.merge(type);
/* 237 */       if (!mergePotential.equals(this.potentialClass) || mergePotential.popChanged()) {
/* 238 */         this.potentialClass = Type.OBJECT.equals(mergePotential) ? null : mergePotential;
/* 239 */         this.changed = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 245 */     if (type instanceof MultiType) {
/* 246 */       MultiType multi = (MultiType)type;
/*     */       
/* 248 */       if (multi.resolved != null) {
/* 249 */         merged = mergeMultiAndSingle(this, multi.resolved);
/*     */       } else {
/* 251 */         merged = mergeMultiInterfaces(multi, this);
/* 252 */         if (!inMergeSource(multi))
/* 253 */           this.mergeSource = multi; 
/*     */       } 
/*     */     } else {
/* 256 */       merged = mergeMultiAndSingle(this, type);
/*     */     } 
/*     */ 
/*     */     
/* 260 */     if (merged.size() > 1 || (merged.size() == 1 && this.potentialClass != null)) {
/*     */       
/* 262 */       if (merged.size() != this.interfaces.size()) {
/* 263 */         this.changed = true;
/* 264 */       } else if (!this.changed) {
/* 265 */         for (String key : merged.keySet()) {
/* 266 */           if (!this.interfaces.containsKey(key))
/* 267 */             this.changed = true; 
/*     */         } 
/*     */       } 
/* 270 */       this.interfaces = merged;
/* 271 */       propogateState();
/*     */       
/* 273 */       return this;
/*     */     } 
/*     */     
/* 276 */     if (merged.size() == 1) {
/* 277 */       this.resolved = Type.get(merged.values().iterator().next());
/* 278 */     } else if (this.potentialClass != null) {
/* 279 */       this.resolved = this.potentialClass;
/*     */     } else {
/* 281 */       this.resolved = OBJECT;
/*     */     } 
/* 283 */     propogateResolved();
/*     */     
/* 285 */     return this.resolved;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 290 */     if (this.resolved != null) {
/* 291 */       return this.resolved.hashCode();
/*     */     }
/* 293 */     return this.interfaces.keySet().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 298 */     if (!(o instanceof MultiType)) {
/* 299 */       return false;
/*     */     }
/* 301 */     MultiType multi = (MultiType)o;
/* 302 */     if (this.resolved != null)
/* 303 */       return this.resolved.equals(multi.resolved); 
/* 304 */     if (multi.resolved != null) {
/* 305 */       return false;
/*     */     }
/* 307 */     return this.interfaces.keySet().equals(multi.interfaces.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 312 */     if (this.resolved != null) {
/* 313 */       return this.resolved.toString();
/*     */     }
/* 315 */     StringBuffer buffer = new StringBuffer("{");
/* 316 */     for (String key : this.interfaces.keySet())
/* 317 */       buffer.append(key).append(", "); 
/* 318 */     if (this.potentialClass != null) {
/* 319 */       buffer.append("*").append(this.potentialClass.toString());
/*     */     } else {
/* 321 */       buffer.setLength(buffer.length() - 2);
/* 322 */     }  buffer.append("}");
/* 323 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\MultiType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */