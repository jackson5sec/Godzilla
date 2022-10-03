/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public abstract class BaseConstructor
/*     */ {
/*  56 */   protected final Map<NodeId, Construct> yamlClassConstructors = new EnumMap<>(NodeId.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected final Map<Tag, Construct> yamlConstructors = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected final Map<String, Construct> yamlMultiConstructors = new HashMap<>();
/*     */   
/*     */   protected Composer composer;
/*     */   
/*     */   final Map<Node, Object> constructedObjects;
/*     */   
/*     */   private final Set<Node> recursiveObjects;
/*     */   
/*     */   private final ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>> maps2fill;
/*     */   
/*     */   private final ArrayList<RecursiveTuple<Set<Object>, Object>> sets2fill;
/*     */   protected Tag rootTag;
/*     */   private PropertyUtils propertyUtils;
/*     */   private boolean explicitPropertyUtils;
/*     */   private boolean allowDuplicateKeys = true;
/*     */   private boolean wrappedToRootException = false;
/*     */   private boolean enumCaseSensitive = false;
/*     */   protected final Map<Class<? extends Object>, TypeDescription> typeDefinitions;
/*     */   protected final Map<Class<? extends Object>, Construct> typeRedefinition;
/*     */   protected final Map<Tag, Class<? extends Object>> typeTags;
/*     */   protected LoaderOptions loadingConfig;
/*     */   
/*     */   public BaseConstructor() {
/*  93 */     this(new LoaderOptions());
/*     */   }
/*     */   
/*     */   public BaseConstructor(LoaderOptions loadingConfig) {
/*  97 */     this.constructedObjects = new HashMap<>();
/*  98 */     this.recursiveObjects = new HashSet<>();
/*  99 */     this.maps2fill = new ArrayList<>();
/* 100 */     this.sets2fill = new ArrayList<>();
/* 101 */     this.typeDefinitions = new HashMap<>();
/* 102 */     this.typeRedefinition = new HashMap<>();
/* 103 */     this.typeTags = new HashMap<>();
/*     */     
/* 105 */     this.rootTag = null;
/* 106 */     this.explicitPropertyUtils = false;
/*     */     
/* 108 */     this.typeDefinitions.put(SortedMap.class, new TypeDescription(SortedMap.class, Tag.OMAP, TreeMap.class));
/*     */     
/* 110 */     this.typeDefinitions.put(SortedSet.class, new TypeDescription(SortedSet.class, Tag.SET, TreeSet.class));
/*     */     
/* 112 */     this.typeRedefinition.put(byte[].class, new SafeConstructor.ConstructYamlBinary());
/* 113 */     this.loadingConfig = loadingConfig;
/*     */   }
/*     */   
/*     */   public void setComposer(Composer composer) {
/* 117 */     this.composer = composer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkData() {
/* 127 */     return this.composer.checkNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getData() throws NoSuchElementException {
/* 137 */     if (!this.composer.checkNode()) throw new NoSuchElementException("No document is available."); 
/* 138 */     Node node = this.composer.getNode();
/* 139 */     if (this.rootTag != null) {
/* 140 */       node.setTag(this.rootTag);
/*     */     }
/* 142 */     return constructDocument(node);
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
/*     */   public Object getSingleData(Class<?> type) {
/* 154 */     Node node = this.composer.getSingleNode();
/* 155 */     if (node != null && !Tag.NULL.equals(node.getTag())) {
/* 156 */       if (Object.class != type) {
/* 157 */         node.setTag(new Tag(type));
/* 158 */       } else if (this.rootTag != null) {
/* 159 */         node.setTag(this.rootTag);
/*     */       } 
/* 161 */       return constructDocument(node);
/*     */     } 
/* 163 */     Construct construct = this.yamlConstructors.get(Tag.NULL);
/* 164 */     return construct.construct(node);
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
/*     */   protected final Object constructDocument(Node node) {
/*     */     try {
/* 177 */       Object data = constructObject(node);
/* 178 */       fillRecursive();
/* 179 */       return data;
/* 180 */     } catch (RuntimeException e) {
/* 181 */       if (this.wrappedToRootException && !(e instanceof YAMLException)) {
/* 182 */         throw new YAMLException(e);
/*     */       }
/* 184 */       throw e;
/*     */     }
/*     */     finally {
/*     */       
/* 188 */       this.constructedObjects.clear();
/* 189 */       this.recursiveObjects.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillRecursive() {
/* 197 */     if (!this.maps2fill.isEmpty()) {
/* 198 */       for (RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>> entry : this.maps2fill) {
/* 199 */         RecursiveTuple<Object, Object> key_value = entry._2();
/* 200 */         ((Map)entry._1()).put(key_value._1(), key_value._2());
/*     */       } 
/* 202 */       this.maps2fill.clear();
/*     */     } 
/* 204 */     if (!this.sets2fill.isEmpty()) {
/* 205 */       for (RecursiveTuple<Set<Object>, Object> value : this.sets2fill) {
/* 206 */         ((Set)value._1()).add(value._2());
/*     */       }
/* 208 */       this.sets2fill.clear();
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
/*     */   protected Object constructObject(Node node) {
/* 220 */     if (this.constructedObjects.containsKey(node)) {
/* 221 */       return this.constructedObjects.get(node);
/*     */     }
/* 223 */     return constructObjectNoCheck(node);
/*     */   }
/*     */   
/*     */   protected Object constructObjectNoCheck(Node node) {
/* 227 */     if (this.recursiveObjects.contains(node)) {
/* 228 */       throw new ConstructorException(null, null, "found unconstructable recursive node", node
/* 229 */           .getStartMark());
/*     */     }
/* 231 */     this.recursiveObjects.add(node);
/* 232 */     Construct constructor = getConstructor(node);
/*     */     
/* 234 */     Object data = this.constructedObjects.containsKey(node) ? this.constructedObjects.get(node) : constructor.construct(node);
/*     */     
/* 236 */     finalizeConstruction(node, data);
/* 237 */     this.constructedObjects.put(node, data);
/* 238 */     this.recursiveObjects.remove(node);
/* 239 */     if (node.isTwoStepsConstruction()) {
/* 240 */       constructor.construct2ndStep(node, data);
/*     */     }
/* 242 */     return data;
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
/*     */   protected Construct getConstructor(Node node) {
/* 254 */     if (node.useClassConstructor()) {
/* 255 */       return this.yamlClassConstructors.get(node.getNodeId());
/*     */     }
/* 257 */     Construct constructor = this.yamlConstructors.get(node.getTag());
/* 258 */     if (constructor == null) {
/* 259 */       for (String prefix : this.yamlMultiConstructors.keySet()) {
/* 260 */         if (node.getTag().startsWith(prefix)) {
/* 261 */           return this.yamlMultiConstructors.get(prefix);
/*     */         }
/*     */       } 
/* 264 */       return this.yamlConstructors.get(null);
/*     */     } 
/* 266 */     return constructor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static String constructScalar(ScalarNode node) {
/* 271 */     return node.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Object> createDefaultList(int initSize) {
/* 276 */     return new ArrayList(initSize);
/*     */   }
/*     */   
/*     */   protected Set<Object> createDefaultSet(int initSize) {
/* 280 */     return new LinkedHashSet(initSize);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> createDefaultMap(int initSize) {
/* 285 */     return new LinkedHashMap<>(initSize);
/*     */   }
/*     */   
/*     */   protected Object createArray(Class<?> type, int size) {
/* 289 */     return Array.newInstance(type.getComponentType(), size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object finalizeConstruction(Node node, Object data) {
/* 295 */     Class<? extends Object> type = node.getType();
/* 296 */     if (this.typeDefinitions.containsKey(type)) {
/* 297 */       return ((TypeDescription)this.typeDefinitions.get(type)).finalizeConstruction(data);
/*     */     }
/* 299 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInstance(Node node) {
/*     */     try {
/* 305 */       return newInstance(Object.class, node);
/* 306 */     } catch (InstantiationException e) {
/* 307 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final Object newInstance(Class<?> ancestor, Node node) throws InstantiationException {
/* 312 */     return newInstance(ancestor, node, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInstance(Class<?> ancestor, Node node, boolean tryDefault) throws InstantiationException {
/* 317 */     Class<? extends Object> type = node.getType();
/* 318 */     if (this.typeDefinitions.containsKey(type)) {
/* 319 */       TypeDescription td = this.typeDefinitions.get(type);
/* 320 */       Object instance = td.newInstance(node);
/* 321 */       if (instance != null) {
/* 322 */         return instance;
/*     */       }
/*     */     } 
/*     */     
/* 326 */     if (this.typeRedefinition.containsKey(type)) {
/* 327 */       return ((Construct)this.typeRedefinition.get(type)).construct(node);
/*     */     }
/*     */     
/* 330 */     if (tryDefault)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 335 */       if (ancestor.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers())) {
/*     */         try {
/* 337 */           Constructor<?> c = type.getDeclaredConstructor(new Class[0]);
/* 338 */           c.setAccessible(true);
/* 339 */           return c.newInstance(new Object[0]);
/* 340 */         } catch (NoSuchMethodException e) {
/* 341 */           throw new InstantiationException("NoSuchMethodException:" + e
/* 342 */               .getLocalizedMessage());
/* 343 */         } catch (Exception e) {
/* 344 */           throw new YAMLException(e);
/*     */         } 
/*     */       }
/*     */     }
/* 348 */     throw new InstantiationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Set<Object> newSet(CollectionNode<?> node) {
/*     */     try {
/* 354 */       return (Set<Object>)newInstance(Set.class, (Node)node);
/* 355 */     } catch (InstantiationException e) {
/* 356 */       return createDefaultSet(node.getValue().size());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Object> newList(SequenceNode node) {
/*     */     try {
/* 363 */       return (List<Object>)newInstance(List.class, (Node)node);
/* 364 */     } catch (InstantiationException e) {
/* 365 */       return createDefaultList(node.getValue().size());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> newMap(MappingNode node) {
/*     */     try {
/* 372 */       return (Map<Object, Object>)newInstance(Map.class, (Node)node);
/* 373 */     } catch (InstantiationException e) {
/* 374 */       return createDefaultMap(node.getValue().size());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<? extends Object> constructSequence(SequenceNode node) {
/* 382 */     List<Object> result = newList(node);
/* 383 */     constructSequenceStep2(node, result);
/* 384 */     return result;
/*     */   }
/*     */   
/*     */   protected Set<? extends Object> constructSet(SequenceNode node) {
/* 388 */     Set<Object> result = newSet((CollectionNode<?>)node);
/* 389 */     constructSequenceStep2(node, result);
/* 390 */     return result;
/*     */   }
/*     */   
/*     */   protected Object constructArray(SequenceNode node) {
/* 394 */     return constructArrayStep2(node, createArray(node.getType(), node.getValue().size()));
/*     */   }
/*     */   
/*     */   protected void constructSequenceStep2(SequenceNode node, Collection<Object> collection) {
/* 398 */     for (Node child : node.getValue()) {
/* 399 */       collection.add(constructObject(child));
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object constructArrayStep2(SequenceNode node, Object array) {
/* 404 */     Class<?> componentType = node.getType().getComponentType();
/*     */     
/* 406 */     int index = 0;
/* 407 */     for (Node child : node.getValue()) {
/*     */       
/* 409 */       if (child.getType() == Object.class) {
/* 410 */         child.setType(componentType);
/*     */       }
/*     */       
/* 413 */       Object value = constructObject(child);
/*     */       
/* 415 */       if (componentType.isPrimitive()) {
/*     */         
/* 417 */         if (value == null) {
/* 418 */           throw new NullPointerException("Unable to construct element value for " + child);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 423 */         if (byte.class.equals(componentType)) {
/* 424 */           Array.setByte(array, index, ((Number)value).byteValue());
/*     */         }
/* 426 */         else if (short.class.equals(componentType)) {
/* 427 */           Array.setShort(array, index, ((Number)value).shortValue());
/*     */         }
/* 429 */         else if (int.class.equals(componentType)) {
/* 430 */           Array.setInt(array, index, ((Number)value).intValue());
/*     */         }
/* 432 */         else if (long.class.equals(componentType)) {
/* 433 */           Array.setLong(array, index, ((Number)value).longValue());
/*     */         }
/* 435 */         else if (float.class.equals(componentType)) {
/* 436 */           Array.setFloat(array, index, ((Number)value).floatValue());
/*     */         }
/* 438 */         else if (double.class.equals(componentType)) {
/* 439 */           Array.setDouble(array, index, ((Number)value).doubleValue());
/*     */         }
/* 441 */         else if (char.class.equals(componentType)) {
/* 442 */           Array.setChar(array, index, ((Character)value).charValue());
/*     */         }
/* 444 */         else if (boolean.class.equals(componentType)) {
/* 445 */           Array.setBoolean(array, index, ((Boolean)value).booleanValue());
/*     */         } else {
/*     */           
/* 448 */           throw new YAMLException("unexpected primitive type");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 453 */         Array.set(array, index, value);
/*     */       } 
/*     */       
/* 456 */       index++;
/*     */     } 
/* 458 */     return array;
/*     */   }
/*     */   
/*     */   protected Set<Object> constructSet(MappingNode node) {
/* 462 */     Set<Object> set = newSet((CollectionNode<?>)node);
/* 463 */     constructSet2ndStep(node, set);
/* 464 */     return set;
/*     */   }
/*     */   
/*     */   protected Map<Object, Object> constructMapping(MappingNode node) {
/* 468 */     Map<Object, Object> mapping = newMap(node);
/* 469 */     constructMapping2ndStep(node, mapping);
/* 470 */     return mapping;
/*     */   }
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 474 */     List<NodeTuple> nodeValue = node.getValue();
/* 475 */     for (NodeTuple tuple : nodeValue) {
/* 476 */       Node keyNode = tuple.getKeyNode();
/* 477 */       Node valueNode = tuple.getValueNode();
/* 478 */       Object key = constructObject(keyNode);
/* 479 */       if (key != null) {
/*     */         try {
/* 481 */           key.hashCode();
/* 482 */         } catch (Exception e) {
/* 483 */           throw new ConstructorException("while constructing a mapping", node
/* 484 */               .getStartMark(), "found unacceptable key " + key, tuple
/* 485 */               .getKeyNode().getStartMark(), e);
/*     */         } 
/*     */       }
/* 488 */       Object value = constructObject(valueNode);
/* 489 */       if (keyNode.isTwoStepsConstruction()) {
/* 490 */         if (this.loadingConfig.getAllowRecursiveKeys()) {
/* 491 */           postponeMapFilling(mapping, key, value); continue;
/*     */         } 
/* 493 */         throw new YAMLException("Recursive key for mapping is detected but it is not configured to be allowed.");
/*     */       } 
/*     */       
/* 496 */       mapping.put(key, value);
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
/*     */   protected void postponeMapFilling(Map<Object, Object> mapping, Object key, Object value) {
/* 508 */     this.maps2fill.add(0, new RecursiveTuple<>(mapping, new RecursiveTuple<>(key, value)));
/*     */   }
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 512 */     List<NodeTuple> nodeValue = node.getValue();
/* 513 */     for (NodeTuple tuple : nodeValue) {
/* 514 */       Node keyNode = tuple.getKeyNode();
/* 515 */       Object key = constructObject(keyNode);
/* 516 */       if (key != null) {
/*     */         try {
/* 518 */           key.hashCode();
/* 519 */         } catch (Exception e) {
/* 520 */           throw new ConstructorException("while constructing a Set", node.getStartMark(), "found unacceptable key " + key, tuple
/* 521 */               .getKeyNode().getStartMark(), e);
/*     */         } 
/*     */       }
/* 524 */       if (keyNode.isTwoStepsConstruction()) {
/* 525 */         postponeSetFilling(set, key); continue;
/*     */       } 
/* 527 */       set.add(key);
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
/*     */   protected void postponeSetFilling(Set<Object> set, Object key) {
/* 539 */     this.sets2fill.add(0, new RecursiveTuple<>(set, key));
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
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 557 */     this.propertyUtils = propertyUtils;
/* 558 */     this.explicitPropertyUtils = true;
/* 559 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/* 560 */     for (TypeDescription typeDescription : tds) {
/* 561 */       typeDescription.setPropertyUtils(propertyUtils);
/*     */     }
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 566 */     if (this.propertyUtils == null) {
/* 567 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 569 */     return this.propertyUtils;
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
/*     */   public TypeDescription addTypeDescription(TypeDescription definition) {
/* 582 */     if (definition == null) {
/* 583 */       throw new NullPointerException("TypeDescription is required.");
/*     */     }
/* 585 */     Tag tag = definition.getTag();
/* 586 */     this.typeTags.put(tag, definition.getType());
/* 587 */     definition.setPropertyUtils(getPropertyUtils());
/* 588 */     return this.typeDefinitions.put(definition.getType(), definition);
/*     */   }
/*     */   
/*     */   private static class RecursiveTuple<T, K> {
/*     */     private final T _1;
/*     */     private final K _2;
/*     */     
/*     */     public RecursiveTuple(T _1, K _2) {
/* 596 */       this._1 = _1;
/* 597 */       this._2 = _2;
/*     */     }
/*     */     
/*     */     public K _2() {
/* 601 */       return this._2;
/*     */     }
/*     */     
/*     */     public T _1() {
/* 605 */       return this._1;
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 610 */     return this.explicitPropertyUtils;
/*     */   }
/*     */   
/*     */   public boolean isAllowDuplicateKeys() {
/* 614 */     return this.allowDuplicateKeys;
/*     */   }
/*     */   
/*     */   public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
/* 618 */     this.allowDuplicateKeys = allowDuplicateKeys;
/*     */   }
/*     */   
/*     */   public boolean isWrappedToRootException() {
/* 622 */     return this.wrappedToRootException;
/*     */   }
/*     */   
/*     */   public void setWrappedToRootException(boolean wrappedToRootException) {
/* 626 */     this.wrappedToRootException = wrappedToRootException;
/*     */   }
/*     */   
/*     */   public boolean isEnumCaseSensitive() {
/* 630 */     return this.enumCaseSensitive;
/*     */   }
/*     */   
/*     */   public void setEnumCaseSensitive(boolean enumCaseSensitive) {
/* 634 */     this.enumCaseSensitive = enumCaseSensitive;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\constructor\BaseConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */