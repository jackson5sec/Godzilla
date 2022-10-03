/*      */ package javassist.bytecode.stackmap;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javassist.ClassPool;
/*      */ import javassist.CtClass;
/*      */ import javassist.NotFoundException;
/*      */ import javassist.bytecode.BadBytecode;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class TypeData
/*      */ {
/*      */   public static TypeData[] make(int size) {
/*   39 */     TypeData[] array = new TypeData[size];
/*   40 */     for (int i = 0; i < size; i++) {
/*   41 */       array[i] = TypeTag.TOP;
/*      */     }
/*   43 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setType(TypeData td, String className, ClassPool cp) throws BadBytecode {
/*   57 */     td.setType(className, cp);
/*      */   }
/*      */   public abstract int getTypeTag();
/*      */   public abstract int getTypeData(ConstPool paramConstPool);
/*      */   
/*      */   public TypeData join() {
/*   63 */     return new TypeVar(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract BasicType isBasicType();
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean is2WordType();
/*      */ 
/*      */   
/*      */   public boolean isNullType() {
/*   76 */     return false;
/*      */   } public boolean isUninit() {
/*   78 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean eq(TypeData paramTypeData);
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract String getName();
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setType(String paramString, ClassPool paramClassPool) throws BadBytecode;
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract TypeData getArrayType(int paramInt) throws NotFoundException;
/*      */ 
/*      */   
/*      */   public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
/*   99 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeVar toTypeVar(int dim) {
/*  109 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void constructorCalled(int offset) {}
/*      */   
/*      */   public String toString() {
/*  116 */     return super.toString() + "(" + toString2(new HashSet<>()) + ")";
/*      */   }
/*      */ 
/*      */   
/*      */   abstract String toString2(Set<TypeData> paramSet);
/*      */   
/*      */   protected static class BasicType
/*      */     extends TypeData
/*      */   {
/*      */     private String name;
/*      */     private int typeTag;
/*      */     private char decodedName;
/*      */     
/*      */     public BasicType(String type, int tag, char decoded) {
/*  130 */       this.name = type;
/*  131 */       this.typeTag = tag;
/*  132 */       this.decodedName = decoded;
/*      */     }
/*      */     
/*      */     public int getTypeTag() {
/*  136 */       return this.typeTag;
/*      */     } public int getTypeData(ConstPool cp) {
/*  138 */       return 0;
/*      */     }
/*      */     
/*      */     public TypeData join() {
/*  142 */       if (this == TypeTag.TOP)
/*  143 */         return this; 
/*  144 */       return super.join();
/*      */     }
/*      */     
/*      */     public BasicType isBasicType() {
/*  148 */       return this;
/*      */     }
/*      */     
/*      */     public boolean is2WordType() {
/*  152 */       return (this.typeTag == 4 || this.typeTag == 3);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean eq(TypeData d) {
/*  157 */       return (this == d);
/*      */     }
/*      */     
/*      */     public String getName() {
/*  161 */       return this.name;
/*      */     }
/*      */     public char getDecodedName() {
/*  164 */       return this.decodedName;
/*      */     }
/*      */     
/*      */     public void setType(String s, ClassPool cp) throws BadBytecode {
/*  168 */       throw new BadBytecode("conflict: " + this.name + " and " + s);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeData getArrayType(int dim) throws NotFoundException {
/*  176 */       if (this == TypeTag.TOP)
/*  177 */         return this; 
/*  178 */       if (dim < 0)
/*  179 */         throw new NotFoundException("no element type: " + this.name); 
/*  180 */       if (dim == 0) {
/*  181 */         return this;
/*      */       }
/*  183 */       char[] name = new char[dim + 1];
/*  184 */       for (int i = 0; i < dim; i++) {
/*  185 */         name[i] = '[';
/*      */       }
/*  187 */       name[dim] = this.decodedName;
/*  188 */       return new TypeData.ClassName(new String(name));
/*      */     }
/*      */ 
/*      */     
/*      */     String toString2(Set<TypeData> set) {
/*  193 */       return this.name;
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class AbsTypeVar extends TypeData {
/*      */     public abstract void merge(TypeData param1TypeData);
/*      */     
/*      */     public int getTypeTag() {
/*  201 */       return 7;
/*      */     }
/*      */     
/*      */     public int getTypeData(ConstPool cp) {
/*  205 */       return cp.addClassInfo(getName());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean eq(TypeData d) {
/*  210 */       if (d.isUninit()) {
/*  211 */         return d.eq(this);
/*      */       }
/*  213 */       return getName().equals(d.getName());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class TypeVar
/*      */     extends AbsTypeVar
/*      */   {
/*      */     protected List<TypeData> lowers;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected List<TypeData> usedBy;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected List<String> uppers;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected String fixedType;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean is2WordType;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int visited;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int smallest;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean inList;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int dimension;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*      */       if (this.fixedType == null) {
/*      */         return ((TypeData)this.lowers.get(0)).getName();
/*      */       }
/*      */       return this.fixedType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeData.BasicType isBasicType() {
/*      */       if (this.fixedType == null) {
/*      */         return ((TypeData)this.lowers.get(0)).isBasicType();
/*      */       }
/*      */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean is2WordType() {
/*      */       if (this.fixedType == null) {
/*      */         return this.is2WordType;
/*      */       }
/*      */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeVar(TypeData t) {
/*  304 */       this.visited = 0;
/*  305 */       this.smallest = 0;
/*  306 */       this.inList = false;
/*  307 */       this.dimension = 0; this.uppers = null; this.lowers = new ArrayList<>(2); this.usedBy = new ArrayList<>(2); merge(t); this.fixedType = null;
/*      */       this.is2WordType = t.is2WordType();
/*      */     } public boolean isNullType() { if (this.fixedType == null)
/*      */         return ((TypeData)this.lowers.get(0)).isNullType(); 
/*  311 */       return false; } protected TypeVar toTypeVar(int dim) { this.dimension = dim;
/*  312 */       return this; } public boolean isUninit() { if (this.fixedType == null)
/*      */         return ((TypeData)this.lowers.get(0)).isUninit(); 
/*      */       return false; }
/*      */     public void merge(TypeData t) {
/*      */       this.lowers.add(t);
/*      */       if (t instanceof TypeVar)
/*      */         ((TypeVar)t).usedBy.add(this); 
/*      */     }
/*  320 */     public TypeData getArrayType(int dim) throws NotFoundException { if (dim == 0)
/*  321 */         return this; 
/*  322 */       TypeData.BasicType bt = isBasicType();
/*  323 */       if (bt == null) {
/*  324 */         if (isNullType()) {
/*  325 */           return new TypeData.NullType();
/*      */         }
/*  327 */         return (new TypeData.ClassName(getName())).getArrayType(dim);
/*  328 */       }  return bt.getArrayType(dim); }
/*      */     
/*      */     public int getTypeTag() {
/*      */       if (this.fixedType == null)
/*      */         return ((TypeData)this.lowers.get(0)).getTypeTag(); 
/*      */       return super.getTypeTag();
/*  334 */     } public int dfs(List<TypeData> preOrder, int index, ClassPool cp) throws NotFoundException { if (this.visited > 0) {
/*  335 */         return index;
/*      */       }
/*  337 */       this.visited = this.smallest = ++index;
/*  338 */       preOrder.add(this);
/*  339 */       this.inList = true;
/*  340 */       int n = this.lowers.size();
/*  341 */       for (int i = 0; i < n; i++) {
/*  342 */         TypeVar child = ((TypeData)this.lowers.get(i)).toTypeVar(this.dimension);
/*  343 */         if (child != null)
/*  344 */           if (child.visited == 0) {
/*  345 */             index = child.dfs(preOrder, index, cp);
/*  346 */             if (child.smallest < this.smallest) {
/*  347 */               this.smallest = child.smallest;
/*      */             }
/*  349 */           } else if (child.inList && 
/*  350 */             child.visited < this.smallest) {
/*  351 */             this.smallest = child.visited;
/*      */           }  
/*      */       } 
/*  354 */       if (this.visited == this.smallest) {
/*  355 */         List<TypeData> scc = new ArrayList<>();
/*      */         
/*      */         while (true) {
/*  358 */           TypeVar cv = (TypeVar)preOrder.remove(preOrder.size() - 1);
/*  359 */           cv.inList = false;
/*  360 */           scc.add(cv);
/*  361 */           if (cv == this) {
/*  362 */             fixTypes(scc, cp); break;
/*      */           } 
/*      */         } 
/*  365 */       }  return index; }
/*      */     public int getTypeData(ConstPool cp) { if (this.fixedType == null)
/*      */         return ((TypeData)this.lowers.get(0)).getTypeData(cp);  return super.getTypeData(cp); }
/*      */     public void setType(String typeName, ClassPool cp) throws BadBytecode { if (this.uppers == null)
/*  369 */         this.uppers = new ArrayList<>();  this.uppers.add(typeName); } private void fixTypes(List<TypeData> scc, ClassPool cp) throws NotFoundException { Set<String> lowersSet = new HashSet<>();
/*  370 */       boolean isBasicType = false;
/*  371 */       TypeData kind = null;
/*  372 */       int size = scc.size();
/*  373 */       for (int i = 0; i < size; i++) {
/*  374 */         TypeVar tvar = (TypeVar)scc.get(i);
/*  375 */         List<TypeData> tds = tvar.lowers;
/*  376 */         int size2 = tds.size();
/*  377 */         for (int j = 0; j < size2; j++) {
/*  378 */           TypeData td = tds.get(j);
/*  379 */           TypeData d = td.getArrayType(tvar.dimension);
/*  380 */           TypeData.BasicType bt = d.isBasicType();
/*  381 */           if (kind == null) {
/*  382 */             if (bt == null) {
/*  383 */               isBasicType = false;
/*  384 */               kind = d;
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  389 */               if (d.isUninit()) {
/*      */                 break;
/*      */               }
/*      */             } else {
/*  393 */               isBasicType = true;
/*  394 */               kind = bt;
/*      */             }
/*      */           
/*      */           }
/*  398 */           else if ((bt == null && isBasicType) || (bt != null && kind != bt)) {
/*  399 */             isBasicType = true;
/*  400 */             kind = TypeTag.TOP;
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  405 */           if (bt == null && !d.isNullType()) {
/*  406 */             lowersSet.add(d.getName());
/*      */           }
/*      */         } 
/*      */       } 
/*  410 */       if (isBasicType) {
/*  411 */         this.is2WordType = kind.is2WordType();
/*  412 */         fixTypes1(scc, kind);
/*      */       } else {
/*      */         
/*  415 */         String typeName = fixTypes2(scc, lowersSet, cp);
/*  416 */         fixTypes1(scc, new TypeData.ClassName(typeName));
/*      */       }  }
/*      */ 
/*      */     
/*      */     private void fixTypes1(List<TypeData> scc, TypeData kind) throws NotFoundException {
/*  421 */       int size = scc.size();
/*  422 */       for (int i = 0; i < size; i++) {
/*  423 */         TypeVar cv = (TypeVar)scc.get(i);
/*  424 */         TypeData kind2 = kind.getArrayType(-cv.dimension);
/*  425 */         if (kind2.isBasicType() == null) {
/*  426 */           cv.fixedType = kind2.getName();
/*      */         } else {
/*  428 */           cv.lowers.clear();
/*  429 */           cv.lowers.add(kind2);
/*  430 */           cv.is2WordType = kind2.is2WordType();
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private String fixTypes2(List<TypeData> scc, Set<String> lowersSet, ClassPool cp) throws NotFoundException {
/*  436 */       Iterator<String> it = lowersSet.iterator();
/*  437 */       if (lowersSet.size() == 0)
/*  438 */         return null; 
/*  439 */       if (lowersSet.size() == 1) {
/*  440 */         return it.next();
/*      */       }
/*  442 */       CtClass cc = cp.get(it.next());
/*  443 */       while (it.hasNext()) {
/*  444 */         cc = commonSuperClassEx(cc, cp.get(it.next()));
/*      */       }
/*  446 */       if (cc.getSuperclass() == null || isObjectArray(cc)) {
/*  447 */         cc = fixByUppers(scc, cp, new HashSet<>(), cc);
/*      */       }
/*  449 */       if (cc.isArray()) {
/*  450 */         return Descriptor.toJvmName(cc);
/*      */       }
/*  452 */       return cc.getName();
/*      */     }
/*      */ 
/*      */     
/*      */     private static boolean isObjectArray(CtClass cc) throws NotFoundException {
/*  457 */       return (cc.isArray() && cc.getComponentType().getSuperclass() == null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private CtClass fixByUppers(List<TypeData> users, ClassPool cp, Set<TypeData> visited, CtClass type) throws NotFoundException {
/*  463 */       if (users == null) {
/*  464 */         return type;
/*      */       }
/*  466 */       int size = users.size();
/*  467 */       for (int i = 0; i < size; i++) {
/*  468 */         TypeVar t = (TypeVar)users.get(i);
/*  469 */         if (!visited.add(t)) {
/*  470 */           return type;
/*      */         }
/*  472 */         if (t.uppers != null) {
/*  473 */           int s = t.uppers.size();
/*  474 */           for (int k = 0; k < s; k++) {
/*  475 */             CtClass cc = cp.get(t.uppers.get(k));
/*  476 */             if (cc.subtypeOf(type)) {
/*  477 */               type = cc;
/*      */             }
/*      */           } 
/*      */         } 
/*  481 */         type = fixByUppers(t.usedBy, cp, visited, type);
/*      */       } 
/*      */       
/*  484 */       return type;
/*      */     }
/*      */ 
/*      */     
/*      */     String toString2(Set<TypeData> hash) {
/*  489 */       hash.add(this);
/*  490 */       if (this.lowers.size() > 0) {
/*  491 */         TypeData e = this.lowers.get(0);
/*  492 */         if (e != null && !hash.contains(e)) {
/*  493 */           return e.toString2(hash);
/*      */         }
/*      */       } 
/*  496 */       return "?";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CtClass commonSuperClassEx(CtClass one, CtClass two) throws NotFoundException {
/*  505 */     if (one == two)
/*  506 */       return one; 
/*  507 */     if (one.isArray() && two.isArray()) {
/*  508 */       CtClass ele1 = one.getComponentType();
/*  509 */       CtClass ele2 = two.getComponentType();
/*  510 */       CtClass element = commonSuperClassEx(ele1, ele2);
/*  511 */       if (element == ele1)
/*  512 */         return one; 
/*  513 */       if (element == ele2) {
/*  514 */         return two;
/*      */       }
/*  516 */       return one.getClassPool().get((element == null) ? "java.lang.Object" : (
/*  517 */           element.getName() + "[]"));
/*      */     } 
/*  519 */     if (one.isPrimitive() || two.isPrimitive())
/*  520 */       return null; 
/*  521 */     if (one.isArray() || two.isArray()) {
/*  522 */       return one.getClassPool().get("java.lang.Object");
/*      */     }
/*  524 */     return commonSuperClass(one, two);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CtClass commonSuperClass(CtClass one, CtClass two) throws NotFoundException {
/*  532 */     CtClass deep = one;
/*  533 */     CtClass shallow = two;
/*  534 */     CtClass backupShallow = shallow;
/*  535 */     CtClass backupDeep = deep;
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  540 */       if (eq(deep, shallow) && deep.getSuperclass() != null) {
/*  541 */         return deep;
/*      */       }
/*  543 */       CtClass deepSuper = deep.getSuperclass();
/*  544 */       CtClass shallowSuper = shallow.getSuperclass();
/*      */       
/*  546 */       if (shallowSuper == null) {
/*      */         
/*  548 */         shallow = backupShallow;
/*      */         
/*      */         break;
/*      */       } 
/*  552 */       if (deepSuper == null) {
/*      */         
/*  554 */         deep = backupDeep;
/*  555 */         backupDeep = backupShallow;
/*  556 */         backupShallow = deep;
/*      */         
/*  558 */         deep = shallow;
/*  559 */         shallow = backupShallow;
/*      */         
/*      */         break;
/*      */       } 
/*  563 */       deep = deepSuper;
/*  564 */       shallow = shallowSuper;
/*      */     } 
/*      */ 
/*      */     
/*      */     while (true) {
/*  569 */       deep = deep.getSuperclass();
/*  570 */       if (deep == null) {
/*      */         break;
/*      */       }
/*  573 */       backupDeep = backupDeep.getSuperclass();
/*      */     } 
/*      */     
/*  576 */     deep = backupDeep;
/*      */ 
/*      */ 
/*      */     
/*  580 */     while (!eq(deep, shallow)) {
/*  581 */       deep = deep.getSuperclass();
/*  582 */       shallow = shallow.getSuperclass();
/*      */     } 
/*      */     
/*  585 */     return deep;
/*      */   }
/*      */   
/*      */   static boolean eq(CtClass one, CtClass two) {
/*  589 */     return (one == two || (one != null && two != null && one.getName().equals(two.getName())));
/*      */   }
/*      */   
/*      */   public static void aastore(TypeData array, TypeData value, ClassPool cp) throws BadBytecode {
/*  593 */     if (array instanceof AbsTypeVar && 
/*  594 */       !value.isNullType()) {
/*  595 */       ((AbsTypeVar)array).merge(ArrayType.make(value));
/*      */     }
/*  597 */     if (value instanceof AbsTypeVar)
/*  598 */       if (array instanceof AbsTypeVar) {
/*  599 */         ArrayElement.make(array);
/*  600 */       } else if (array instanceof ClassName) {
/*  601 */         if (!array.isNullType()) {
/*  602 */           String type = ArrayElement.typeName(array.getName());
/*  603 */           value.setType(type, cp);
/*      */         } 
/*      */       } else {
/*      */         
/*  607 */         throw new BadBytecode("bad AASTORE: " + array);
/*      */       }  
/*      */   }
/*      */   
/*      */   public static class ArrayType
/*      */     extends AbsTypeVar
/*      */   {
/*      */     private TypeData.AbsTypeVar element;
/*      */     
/*      */     private ArrayType(TypeData.AbsTypeVar elementType) {
/*  617 */       this.element = elementType;
/*      */     }
/*      */     
/*      */     static TypeData make(TypeData element) throws BadBytecode {
/*  621 */       if (element instanceof TypeData.ArrayElement)
/*  622 */         return ((TypeData.ArrayElement)element).arrayType(); 
/*  623 */       if (element instanceof TypeData.AbsTypeVar)
/*  624 */         return new ArrayType((TypeData.AbsTypeVar)element); 
/*  625 */       if (element instanceof TypeData.ClassName && 
/*  626 */         !element.isNullType()) {
/*  627 */         return new TypeData.ClassName(typeName(element.getName()));
/*      */       }
/*  629 */       throw new BadBytecode("bad AASTORE: " + element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void merge(TypeData t) {
/*      */       try {
/*  635 */         if (!t.isNullType()) {
/*  636 */           this.element.merge(TypeData.ArrayElement.make(t));
/*      */         }
/*  638 */       } catch (BadBytecode e) {
/*      */         
/*  640 */         throw new RuntimeException("fatal: " + e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String getName() {
/*  646 */       return typeName(this.element.getName());
/*      */     }
/*      */     public TypeData.AbsTypeVar elementType() {
/*  649 */       return this.element;
/*      */     }
/*      */     public TypeData.BasicType isBasicType() {
/*  652 */       return null;
/*      */     } public boolean is2WordType() {
/*  654 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static String typeName(String elementType) {
/*  660 */       if (elementType.charAt(0) == '[')
/*  661 */         return "[" + elementType; 
/*  662 */       return "[L" + elementType.replace('.', '/') + ";";
/*      */     }
/*      */ 
/*      */     
/*      */     public void setType(String s, ClassPool cp) throws BadBytecode {
/*  667 */       this.element.setType(TypeData.ArrayElement.typeName(s), cp);
/*      */     }
/*      */     
/*      */     protected TypeData.TypeVar toTypeVar(int dim) {
/*  671 */       return this.element.toTypeVar(dim + 1);
/*      */     }
/*      */     
/*      */     public TypeData getArrayType(int dim) throws NotFoundException {
/*  675 */       return this.element.getArrayType(dim + 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
/*  680 */       return this.element.dfs(order, index, cp);
/*      */     }
/*      */ 
/*      */     
/*      */     String toString2(Set<TypeData> set) {
/*  685 */       return "[" + this.element.toString2(set);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class ArrayElement
/*      */     extends AbsTypeVar
/*      */   {
/*      */     private TypeData.AbsTypeVar array;
/*      */     
/*      */     private ArrayElement(TypeData.AbsTypeVar a) {
/*  696 */       this.array = a;
/*      */     }
/*      */     
/*      */     public static TypeData make(TypeData array) throws BadBytecode {
/*  700 */       if (array instanceof TypeData.ArrayType)
/*  701 */         return ((TypeData.ArrayType)array).elementType(); 
/*  702 */       if (array instanceof TypeData.AbsTypeVar)
/*  703 */         return new ArrayElement((TypeData.AbsTypeVar)array); 
/*  704 */       if (array instanceof TypeData.ClassName && 
/*  705 */         !array.isNullType()) {
/*  706 */         return new TypeData.ClassName(typeName(array.getName()));
/*      */       }
/*  708 */       throw new BadBytecode("bad AASTORE: " + array);
/*      */     }
/*      */ 
/*      */     
/*      */     public void merge(TypeData t) {
/*      */       try {
/*  714 */         if (!t.isNullType()) {
/*  715 */           this.array.merge(TypeData.ArrayType.make(t));
/*      */         }
/*  717 */       } catch (BadBytecode e) {
/*      */         
/*  719 */         throw new RuntimeException("fatal: " + e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String getName() {
/*  725 */       return typeName(this.array.getName());
/*      */     }
/*      */     public TypeData.AbsTypeVar arrayType() {
/*  728 */       return this.array;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeData.BasicType isBasicType() {
/*  735 */       return null;
/*      */     }
/*      */     public boolean is2WordType() {
/*  738 */       return false;
/*      */     }
/*      */     private static String typeName(String arrayType) {
/*  741 */       if (arrayType.length() > 1 && arrayType.charAt(0) == '[') {
/*  742 */         char c = arrayType.charAt(1);
/*  743 */         if (c == 'L')
/*  744 */           return arrayType.substring(2, arrayType.length() - 1).replace('/', '.'); 
/*  745 */         if (c == '[') {
/*  746 */           return arrayType.substring(1);
/*      */         }
/*      */       } 
/*  749 */       return "java.lang.Object";
/*      */     }
/*      */ 
/*      */     
/*      */     public void setType(String s, ClassPool cp) throws BadBytecode {
/*  754 */       this.array.setType(TypeData.ArrayType.typeName(s), cp);
/*      */     }
/*      */     
/*      */     protected TypeData.TypeVar toTypeVar(int dim) {
/*  758 */       return this.array.toTypeVar(dim - 1);
/*      */     }
/*      */     
/*      */     public TypeData getArrayType(int dim) throws NotFoundException {
/*  762 */       return this.array.getArrayType(dim - 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public int dfs(List<TypeData> order, int index, ClassPool cp) throws NotFoundException {
/*  767 */       return this.array.dfs(order, index, cp);
/*      */     }
/*      */ 
/*      */     
/*      */     String toString2(Set<TypeData> set) {
/*  772 */       return "*" + this.array.toString2(set);
/*      */     } }
/*      */   
/*      */   public static class UninitTypeVar extends AbsTypeVar {
/*      */     protected TypeData type;
/*      */     
/*      */     public UninitTypeVar(TypeData.UninitData t) {
/*  779 */       this.type = t;
/*      */     } public int getTypeTag() {
/*  781 */       return this.type.getTypeTag();
/*      */     } public int getTypeData(ConstPool cp) {
/*  783 */       return this.type.getTypeData(cp);
/*      */     } public TypeData.BasicType isBasicType() {
/*  785 */       return this.type.isBasicType();
/*      */     } public boolean is2WordType() {
/*  787 */       return this.type.is2WordType();
/*      */     } public boolean isUninit() {
/*  789 */       return this.type.isUninit();
/*      */     } public boolean eq(TypeData d) {
/*  791 */       return this.type.eq(d);
/*      */     } public String getName() {
/*  793 */       return this.type.getName();
/*      */     }
/*      */     protected TypeData.TypeVar toTypeVar(int dim) {
/*  796 */       return null;
/*      */     } public TypeData join() {
/*  798 */       return this.type.join();
/*      */     }
/*      */     
/*      */     public void setType(String s, ClassPool cp) throws BadBytecode {
/*  802 */       this.type.setType(s, cp);
/*      */     }
/*      */ 
/*      */     
/*      */     public void merge(TypeData t) {
/*  807 */       if (!t.eq(this.type)) {
/*  808 */         this.type = TypeTag.TOP;
/*      */       }
/*      */     }
/*      */     
/*      */     public void constructorCalled(int offset) {
/*  813 */       this.type.constructorCalled(offset);
/*      */     }
/*      */     
/*      */     public int offset() {
/*  817 */       if (this.type instanceof TypeData.UninitData)
/*  818 */         return ((TypeData.UninitData)this.type).offset; 
/*  819 */       throw new RuntimeException("not available");
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeData getArrayType(int dim) throws NotFoundException {
/*  824 */       return this.type.getArrayType(dim);
/*      */     }
/*      */     
/*      */     String toString2(Set<TypeData> set) {
/*  828 */       return "";
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ClassName
/*      */     extends TypeData
/*      */   {
/*      */     private String name;
/*      */     
/*      */     public ClassName(String n) {
/*  838 */       this.name = n;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getName() {
/*  843 */       return this.name;
/*      */     }
/*      */     
/*      */     public TypeData.BasicType isBasicType() {
/*  847 */       return null;
/*      */     }
/*      */     public boolean is2WordType() {
/*  850 */       return false;
/*      */     }
/*      */     public int getTypeTag() {
/*  853 */       return 7;
/*      */     }
/*      */     
/*      */     public int getTypeData(ConstPool cp) {
/*  857 */       return cp.addClassInfo(getName());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean eq(TypeData d) {
/*  862 */       if (d.isUninit()) {
/*  863 */         return d.eq(this);
/*      */       }
/*  865 */       return this.name.equals(d.getName());
/*      */     }
/*      */ 
/*      */     
/*      */     public void setType(String typeName, ClassPool cp) throws BadBytecode {}
/*      */ 
/*      */     
/*      */     public TypeData getArrayType(int dim) throws NotFoundException {
/*  873 */       if (dim == 0)
/*  874 */         return this; 
/*  875 */       if (dim > 0) {
/*  876 */         char[] dimType = new char[dim];
/*  877 */         for (int j = 0; j < dim; j++) {
/*  878 */           dimType[j] = '[';
/*      */         }
/*  880 */         String elementType = getName();
/*  881 */         if (elementType.charAt(0) != '[') {
/*  882 */           elementType = "L" + elementType.replace('.', '/') + ";";
/*      */         }
/*  884 */         return new ClassName(new String(dimType) + elementType);
/*      */       } 
/*      */       
/*  887 */       for (int i = 0; i < -dim; i++) {
/*  888 */         if (this.name.charAt(i) != '[')
/*  889 */           throw new NotFoundException("no " + dim + " dimensional array type: " + getName()); 
/*      */       } 
/*  891 */       char type = this.name.charAt(-dim);
/*  892 */       if (type == '[')
/*  893 */         return new ClassName(this.name.substring(-dim)); 
/*  894 */       if (type == 'L')
/*  895 */         return new ClassName(this.name.substring(-dim + 1, this.name.length() - 1).replace('/', '.')); 
/*  896 */       if (type == TypeTag.DOUBLE.decodedName)
/*  897 */         return TypeTag.DOUBLE; 
/*  898 */       if (type == TypeTag.FLOAT.decodedName)
/*  899 */         return TypeTag.FLOAT; 
/*  900 */       if (type == TypeTag.LONG.decodedName) {
/*  901 */         return TypeTag.LONG;
/*      */       }
/*  903 */       return TypeTag.INTEGER;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     String toString2(Set<TypeData> set) {
/*  909 */       return this.name;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class NullType
/*      */     extends ClassName
/*      */   {
/*      */     public NullType() {
/*  920 */       super("null-type");
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTypeTag() {
/*  925 */       return 5;
/*      */     }
/*      */     
/*      */     public boolean isNullType() {
/*  929 */       return true;
/*      */     } public int getTypeData(ConstPool cp) {
/*  931 */       return 0;
/*      */     }
/*      */     public TypeData getArrayType(int dim) {
/*  934 */       return this;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class UninitData
/*      */     extends ClassName
/*      */   {
/*      */     int offset;
/*      */     boolean initialized;
/*      */     
/*      */     UninitData(int offset, String className) {
/*  945 */       super(className);
/*  946 */       this.offset = offset;
/*  947 */       this.initialized = false;
/*      */     }
/*      */     public UninitData copy() {
/*  950 */       return new UninitData(this.offset, getName());
/*      */     }
/*      */     
/*      */     public int getTypeTag() {
/*  954 */       return 8;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTypeData(ConstPool cp) {
/*  959 */       return this.offset;
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeData join() {
/*  964 */       if (this.initialized)
/*  965 */         return new TypeData.TypeVar(new TypeData.ClassName(getName())); 
/*  966 */       return new TypeData.UninitTypeVar(copy());
/*      */     }
/*      */     
/*      */     public boolean isUninit() {
/*  970 */       return true;
/*      */     }
/*      */     
/*      */     public boolean eq(TypeData d) {
/*  974 */       if (d instanceof UninitData) {
/*  975 */         UninitData ud = (UninitData)d;
/*  976 */         return (this.offset == ud.offset && getName().equals(ud.getName()));
/*      */       } 
/*  978 */       return false;
/*      */     }
/*      */     public int offset() {
/*  981 */       return this.offset;
/*      */     }
/*      */     
/*      */     public void constructorCalled(int offset) {
/*  985 */       if (offset == this.offset)
/*  986 */         this.initialized = true; 
/*      */     }
/*      */     
/*      */     String toString2(Set<TypeData> set) {
/*  990 */       return getName() + "," + this.offset;
/*      */     } }
/*      */   
/*      */   public static class UninitThis extends UninitData {
/*      */     UninitThis(String className) {
/*  995 */       super(-1, className);
/*      */     }
/*      */     
/*      */     public TypeData.UninitData copy() {
/*  999 */       return new UninitThis(getName());
/*      */     }
/*      */     
/*      */     public int getTypeTag() {
/* 1003 */       return 6;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTypeData(ConstPool cp) {
/* 1008 */       return 0;
/*      */     }
/*      */     
/*      */     String toString2(Set<TypeData> set) {
/* 1012 */       return "uninit:this";
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\stackmap\TypeData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */