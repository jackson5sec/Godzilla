/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.SecClass;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.util.EnumUtils;
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
/*     */ public class Constructor
/*     */   extends SafeConstructor
/*     */ {
/*     */   public Constructor() {
/*  49 */     this(Object.class);
/*     */   }
/*     */   
/*     */   public Constructor(LoaderOptions loadingConfig) {
/*  53 */     this(Object.class, loadingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor(Class<? extends Object> theRoot) {
/*  63 */     this(new TypeDescription(checkRoot(theRoot)));
/*     */   }
/*     */   
/*     */   public Constructor(Class<? extends Object> theRoot, LoaderOptions loadingConfig) {
/*  67 */     this(new TypeDescription(checkRoot(theRoot)), loadingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<? extends Object> checkRoot(Class<? extends Object> theRoot) {
/*  74 */     if (theRoot == null) {
/*  75 */       throw new NullPointerException("Root class must be provided.");
/*     */     }
/*  77 */     return theRoot;
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot) {
/*  81 */     this(theRoot, (Collection<TypeDescription>)null, new LoaderOptions());
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, LoaderOptions loadingConfig) {
/*  85 */     this(theRoot, (Collection<TypeDescription>)null, loadingConfig);
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs) {
/*  89 */     this(theRoot, moreTDs, new LoaderOptions());
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs, LoaderOptions loadingConfig) {
/*  93 */     super(loadingConfig);
/*  94 */     if (theRoot == null) {
/*  95 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/*  97 */     this.yamlConstructors.put(null, new ConstructYamlObject());
/*  98 */     if (!Object.class.equals(theRoot.getType())) {
/*  99 */       this.rootTag = new Tag(theRoot.getType());
/*     */     }
/* 101 */     this.yamlClassConstructors.put(NodeId.scalar, new ConstructScalar());
/* 102 */     this.yamlClassConstructors.put(NodeId.mapping, new ConstructMapping());
/* 103 */     this.yamlClassConstructors.put(NodeId.sequence, new ConstructSequence());
/* 104 */     addTypeDescription(theRoot);
/* 105 */     if (moreTDs != null) {
/* 106 */       for (TypeDescription td : moreTDs) {
/* 107 */         addTypeDescription(td);
/*     */       }
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
/*     */   public Constructor(String theRoot) throws ClassNotFoundException {
/* 122 */     this(SecClass.forName(check(theRoot)));
/*     */   }
/*     */   
/*     */   public Constructor(String theRoot, LoaderOptions loadingConfig) throws ClassNotFoundException {
/* 126 */     this(SecClass.forName(check(theRoot)), loadingConfig);
/*     */   }
/*     */   
/*     */   private static final String check(String s) {
/* 130 */     if (s == null) {
/* 131 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/* 133 */     if (s.trim().length() == 0) {
/* 134 */       throw new YAMLException("Root type must be provided.");
/*     */     }
/* 136 */     return s;
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
/*     */   protected class ConstructMapping
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 155 */       MappingNode mnode = (MappingNode)node;
/* 156 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 157 */         if (node.isTwoStepsConstruction()) {
/* 158 */           return Constructor.this.newMap(mnode);
/*     */         }
/* 160 */         return Constructor.this.constructMapping(mnode);
/*     */       } 
/* 162 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 163 */         if (node.isTwoStepsConstruction()) {
/* 164 */           return Constructor.this.newSet((CollectionNode<?>)mnode);
/*     */         }
/* 166 */         return Constructor.this.constructSet(mnode);
/*     */       } 
/*     */       
/* 169 */       Object obj = Constructor.this.newInstance((Node)mnode);
/* 170 */       if (node.isTwoStepsConstruction()) {
/* 171 */         return obj;
/*     */       }
/* 173 */       return constructJavaBean2ndStep(mnode, obj);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 180 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 181 */         Constructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/* 182 */       } else if (Set.class.isAssignableFrom(node.getType())) {
/* 183 */         Constructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 185 */         constructJavaBean2ndStep((MappingNode)node, object);
/*     */       } 
/*     */     }
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
/*     */     protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
/* 214 */       Constructor.this.flattenMapping(node);
/* 215 */       Class<? extends Object> beanType = node.getType();
/* 216 */       List<NodeTuple> nodeValue = node.getValue();
/* 217 */       for (NodeTuple tuple : nodeValue) {
/*     */         ScalarNode keyNode;
/* 219 */         if (tuple.getKeyNode() instanceof ScalarNode) {
/*     */           
/* 221 */           keyNode = (ScalarNode)tuple.getKeyNode();
/*     */         } else {
/* 223 */           throw new YAMLException("Keys must be scalars but found: " + tuple
/* 224 */               .getKeyNode());
/*     */         } 
/* 226 */         Node valueNode = tuple.getValueNode();
/*     */         
/* 228 */         keyNode.setType(String.class);
/* 229 */         String key = (String)Constructor.this.constructObject((Node)keyNode);
/*     */         try {
/* 231 */           TypeDescription memberDescription = Constructor.this.typeDefinitions.get(beanType);
/*     */           
/* 233 */           Property property = (memberDescription == null) ? getProperty(beanType, key) : memberDescription.getProperty(key);
/*     */           
/* 235 */           if (!property.isWritable()) {
/* 236 */             throw new YAMLException("No writable property '" + key + "' on class: " + beanType
/* 237 */                 .getName());
/*     */           }
/*     */           
/* 240 */           valueNode.setType(property.getType());
/*     */           
/* 242 */           boolean typeDetected = (memberDescription != null) ? memberDescription.setupPropertyType(key, valueNode) : false;
/*     */           
/* 244 */           if (!typeDetected && valueNode.getNodeId() != NodeId.scalar) {
/*     */             
/* 246 */             Class<?>[] arguments = property.getActualTypeArguments();
/* 247 */             if (arguments != null && arguments.length > 0)
/*     */             {
/*     */               
/* 250 */               if (valueNode.getNodeId() == NodeId.sequence) {
/* 251 */                 Class<?> t = arguments[0];
/* 252 */                 SequenceNode snode = (SequenceNode)valueNode;
/* 253 */                 snode.setListType(t);
/* 254 */               } else if (Set.class.isAssignableFrom(valueNode.getType())) {
/* 255 */                 Class<?> t = arguments[0];
/* 256 */                 MappingNode mnode = (MappingNode)valueNode;
/* 257 */                 mnode.setOnlyKeyType(t);
/* 258 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/* 259 */               } else if (Map.class.isAssignableFrom(valueNode.getType())) {
/* 260 */                 Class<?> keyType = arguments[0];
/* 261 */                 Class<?> valueType = arguments[1];
/* 262 */                 MappingNode mnode = (MappingNode)valueNode;
/* 263 */                 mnode.setTypes(keyType, valueType);
/* 264 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/*     */               } 
/*     */             }
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 271 */           Object value = (memberDescription != null) ? newInstance(memberDescription, key, valueNode) : Constructor.this.constructObject(valueNode);
/*     */ 
/*     */           
/* 274 */           if ((property.getType() == float.class || property.getType() == Float.class) && 
/* 275 */             value instanceof Double) {
/* 276 */             value = Float.valueOf(((Double)value).floatValue());
/*     */           }
/*     */ 
/*     */           
/* 280 */           if (property.getType() == String.class && Tag.BINARY.equals(valueNode.getTag()) && value instanceof byte[])
/*     */           {
/* 282 */             value = new String((byte[])value);
/*     */           }
/*     */           
/* 285 */           if (memberDescription == null || 
/* 286 */             !memberDescription.setProperty(object, key, value)) {
/* 287 */             property.set(object, value);
/*     */           }
/* 289 */         } catch (DuplicateKeyException e) {
/* 290 */           throw e;
/* 291 */         } catch (Exception e) {
/* 292 */           throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node
/*     */               
/* 294 */               .getStartMark(), e.getMessage(), valueNode.getStartMark(), e);
/*     */         } 
/*     */       } 
/* 297 */       return object;
/*     */     }
/*     */ 
/*     */     
/*     */     private Object newInstance(TypeDescription memberDescription, String propertyName, Node node) {
/* 302 */       Object newInstance = memberDescription.newInstance(propertyName, node);
/* 303 */       if (newInstance != null) {
/* 304 */         Constructor.this.constructedObjects.put(node, newInstance);
/* 305 */         return Constructor.this.constructObjectNoCheck(node);
/*     */       } 
/* 307 */       return Constructor.this.constructObject(node);
/*     */     }
/*     */     
/*     */     protected Property getProperty(Class<? extends Object> type, String name) {
/* 311 */       return Constructor.this.getPropertyUtils().getProperty(type, name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructYamlObject
/*     */     implements Construct
/*     */   {
/*     */     private Construct getConstructor(Node node) {
/* 324 */       Class<?> cl = Constructor.this.getClassForNode(node);
/* 325 */       node.setType(cl);
/*     */       
/* 327 */       Construct constructor = Constructor.this.yamlClassConstructors.get(node.getNodeId());
/* 328 */       return constructor;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/*     */       try {
/* 333 */         return getConstructor(node).construct(node);
/* 334 */       } catch (ConstructorException e) {
/* 335 */         throw e;
/* 336 */       } catch (Exception e) {
/* 337 */         throw new ConstructorException(null, null, "Can't construct a java object for " + node
/* 338 */             .getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/*     */       try {
/* 344 */         getConstructor(node).construct2ndStep(node, object);
/* 345 */       } catch (Exception e) {
/* 346 */         throw new ConstructorException(null, null, "Can't construct a second step for a java object for " + node
/*     */             
/* 348 */             .getTag() + "; exception=" + e.getMessage(), node
/* 349 */             .getStartMark(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructScalar
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node nnode) {
/* 360 */       ScalarNode node = (ScalarNode)nnode;
/* 361 */       Class<?> type = node.getType();
/*     */       
/*     */       try {
/* 364 */         return Constructor.this.newInstance(type, (Node)node, false);
/* 365 */       } catch (InstantiationException null) {
/*     */         Object result;
/*     */ 
/*     */         
/* 369 */         if (type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type) || type == Boolean.class || Date.class
/* 370 */           .isAssignableFrom(type) || type == Character.class || type == BigInteger.class || type == BigDecimal.class || Enum.class
/*     */           
/* 372 */           .isAssignableFrom(type) || Tag.BINARY
/* 373 */           .equals(node.getTag()) || Calendar.class.isAssignableFrom(type) || type == UUID.class) {
/*     */ 
/*     */           
/* 376 */           Object object = constructStandardJavaInstance(type, node);
/*     */         } else {
/*     */           Object argument;
/*     */           
/* 380 */           Constructor[] arrayOfConstructor = (Constructor[])type.getDeclaredConstructors();
/* 381 */           int oneArgCount = 0;
/* 382 */           Constructor<?> javaConstructor = null;
/* 383 */           for (Constructor<?> c : arrayOfConstructor) {
/* 384 */             if ((c.getParameterTypes()).length == 1) {
/* 385 */               oneArgCount++;
/* 386 */               javaConstructor = c;
/*     */             } 
/*     */           } 
/*     */           
/* 390 */           if (javaConstructor == null)
/*     */             try {
/* 392 */               return Constructor.this.newInstance(type, (Node)node, false);
/* 393 */             } catch (InstantiationException ie) {
/* 394 */               throw new YAMLException("No single argument constructor found for " + type + " : " + ie
/* 395 */                   .getMessage());
/*     */             }  
/* 397 */           if (oneArgCount == 1) {
/* 398 */             argument = constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */             
/* 407 */             argument = BaseConstructor.constructScalar(node);
/*     */             try {
/* 409 */               javaConstructor = type.getDeclaredConstructor(new Class[] { String.class });
/* 410 */             } catch (Exception e) {
/* 411 */               throw new YAMLException("Can't construct a java object for scalar " + node
/* 412 */                   .getTag() + "; No String constructor found. Exception=" + e
/* 413 */                   .getMessage(), e);
/*     */             } 
/*     */           } 
/*     */           try {
/* 417 */             javaConstructor.setAccessible(true);
/* 418 */             result = javaConstructor.newInstance(new Object[] { argument });
/* 419 */           } catch (Exception e) {
/* 420 */             throw new ConstructorException(null, null, "Can't construct a java object for scalar " + node
/* 421 */                 .getTag() + "; exception=" + e
/* 422 */                 .getMessage(), node
/* 423 */                 .getStartMark(), e);
/*     */           } 
/*     */         } 
/* 426 */         return result;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private Object constructStandardJavaInstance(Class<String> type, ScalarNode node) {
/*     */       Object result;
/* 433 */       if (type == String.class) {
/* 434 */         Construct stringConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 435 */         result = stringConstructor.construct((Node)node);
/* 436 */       } else if (type == Boolean.class || type == boolean.class) {
/* 437 */         Construct boolConstructor = Constructor.this.yamlConstructors.get(Tag.BOOL);
/* 438 */         result = boolConstructor.construct((Node)node);
/* 439 */       } else if (type == Character.class || type == char.class) {
/* 440 */         Construct charConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 441 */         String ch = (String)charConstructor.construct((Node)node);
/* 442 */         if (ch.length() == 0)
/* 443 */         { result = null; }
/* 444 */         else { if (ch.length() != 1) {
/* 445 */             throw new YAMLException("Invalid node Character: '" + ch + "'; length: " + ch
/* 446 */                 .length());
/*     */           }
/* 448 */           result = Character.valueOf(ch.charAt(0)); }
/*     */       
/* 450 */       } else if (Date.class.isAssignableFrom(type)) {
/* 451 */         Construct dateConstructor = Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
/* 452 */         Date date = (Date)dateConstructor.construct((Node)node);
/* 453 */         if (type == Date.class) {
/* 454 */           result = date;
/*     */         } else {
/*     */           try {
/* 457 */             Constructor<?> constr = type.getConstructor(new Class[] { long.class });
/* 458 */             result = constr.newInstance(new Object[] { Long.valueOf(date.getTime()) });
/* 459 */           } catch (RuntimeException e) {
/* 460 */             throw e;
/* 461 */           } catch (Exception e) {
/* 462 */             throw new YAMLException("Cannot construct: '" + type + "'");
/*     */           } 
/*     */         } 
/* 465 */       } else if (type == Float.class || type == Double.class || type == float.class || type == double.class || type == BigDecimal.class) {
/*     */         
/* 467 */         if (type == BigDecimal.class) {
/* 468 */           result = new BigDecimal(node.getValue());
/*     */         } else {
/* 470 */           Construct doubleConstructor = Constructor.this.yamlConstructors.get(Tag.FLOAT);
/* 471 */           result = doubleConstructor.construct((Node)node);
/* 472 */           if (type == Float.class || type == float.class) {
/* 473 */             result = Float.valueOf(((Double)result).floatValue());
/*     */           }
/*     */         } 
/* 476 */       } else if (type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == BigInteger.class || type == byte.class || type == short.class || type == int.class || type == long.class) {
/*     */ 
/*     */         
/* 479 */         Construct intConstructor = Constructor.this.yamlConstructors.get(Tag.INT);
/* 480 */         result = intConstructor.construct((Node)node);
/* 481 */         if (type == Byte.class || type == byte.class) {
/* 482 */           result = Byte.valueOf(Integer.valueOf(result.toString()).byteValue());
/* 483 */         } else if (type == Short.class || type == short.class) {
/* 484 */           result = Short.valueOf(Integer.valueOf(result.toString()).shortValue());
/* 485 */         } else if (type == Integer.class || type == int.class) {
/* 486 */           result = Integer.valueOf(Integer.parseInt(result.toString()));
/* 487 */         } else if (type == Long.class || type == long.class) {
/* 488 */           result = Long.valueOf(result.toString());
/*     */         } else {
/*     */           
/* 491 */           result = new BigInteger(result.toString());
/*     */         } 
/* 493 */       } else if (Enum.class.isAssignableFrom(type)) {
/* 494 */         String enumValueName = node.getValue();
/*     */         try {
/* 496 */           if (Constructor.this.loadingConfig.isEnumCaseSensitive()) {
/* 497 */             result = Enum.valueOf(type, enumValueName);
/*     */           } else {
/* 499 */             result = EnumUtils.findEnumInsensitiveCase(type, enumValueName);
/*     */           } 
/* 501 */         } catch (Exception ex) {
/* 502 */           throw new YAMLException("Unable to find enum value '" + enumValueName + "' for enum class: " + type
/* 503 */               .getName());
/*     */         } 
/* 505 */       } else if (Calendar.class.isAssignableFrom(type)) {
/* 506 */         SafeConstructor.ConstructYamlTimestamp contr = new SafeConstructor.ConstructYamlTimestamp();
/* 507 */         contr.construct((Node)node);
/* 508 */         result = contr.getCalendar();
/* 509 */       } else if (Number.class.isAssignableFrom(type)) {
/*     */         
/* 511 */         SafeConstructor.ConstructYamlFloat contr = new SafeConstructor.ConstructYamlFloat(Constructor.this);
/* 512 */         result = contr.construct((Node)node);
/* 513 */       } else if (UUID.class == type) {
/* 514 */         result = UUID.fromString(node.getValue());
/*     */       }
/* 516 */       else if (Constructor.this.yamlConstructors.containsKey(node.getTag())) {
/* 517 */         result = ((Construct)Constructor.this.yamlConstructors.get(node.getTag())).construct((Node)node);
/*     */       } else {
/* 519 */         throw new YAMLException("Unsupported class: " + type);
/*     */       } 
/*     */       
/* 522 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructSequence
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 533 */       SequenceNode snode = (SequenceNode)node;
/* 534 */       if (Set.class.isAssignableFrom(node.getType())) {
/* 535 */         if (node.isTwoStepsConstruction()) {
/* 536 */           throw new YAMLException("Set cannot be recursive.");
/*     */         }
/* 538 */         return Constructor.this.constructSet(snode);
/*     */       } 
/* 540 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 541 */         if (node.isTwoStepsConstruction()) {
/* 542 */           return Constructor.this.newList(snode);
/*     */         }
/* 544 */         return Constructor.this.constructSequence(snode);
/*     */       } 
/* 546 */       if (node.getType().isArray()) {
/* 547 */         if (node.isTwoStepsConstruction()) {
/* 548 */           return Constructor.this.createArray(node.getType(), snode.getValue().size());
/*     */         }
/* 550 */         return Constructor.this.constructArray(snode);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 555 */       List<Constructor<?>> possibleConstructors = new ArrayList<>(snode.getValue().size());
/* 556 */       for (Constructor<?> constructor : node.getType()
/* 557 */         .getDeclaredConstructors()) {
/* 558 */         if (snode.getValue().size() == (constructor.getParameterTypes()).length) {
/* 559 */           possibleConstructors.add(constructor);
/*     */         }
/*     */       } 
/* 562 */       if (!possibleConstructors.isEmpty()) {
/* 563 */         if (possibleConstructors.size() == 1) {
/* 564 */           Object[] arrayOfObject = new Object[snode.getValue().size()];
/* 565 */           Constructor<?> c = possibleConstructors.get(0);
/* 566 */           int i = 0;
/* 567 */           for (Node argumentNode : snode.getValue()) {
/* 568 */             Class<?> type = c.getParameterTypes()[i];
/*     */             
/* 570 */             argumentNode.setType(type);
/* 571 */             arrayOfObject[i++] = Constructor.this.constructObject(argumentNode);
/*     */           } 
/*     */           
/*     */           try {
/* 575 */             c.setAccessible(true);
/* 576 */             return c.newInstance(arrayOfObject);
/* 577 */           } catch (Exception e) {
/* 578 */             throw new YAMLException(e);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 583 */         List<Object> argumentList = (List)Constructor.this.constructSequence(snode);
/* 584 */         Class<?>[] parameterTypes = new Class[argumentList.size()];
/* 585 */         int index = 0;
/* 586 */         for (Object parameter : argumentList) {
/* 587 */           parameterTypes[index] = parameter.getClass();
/* 588 */           index++;
/*     */         } 
/*     */         
/* 591 */         for (Constructor<?> c : possibleConstructors) {
/* 592 */           Class<?>[] argTypes = c.getParameterTypes();
/* 593 */           boolean foundConstructor = true;
/* 594 */           for (int i = 0; i < argTypes.length; i++) {
/* 595 */             if (!wrapIfPrimitive(argTypes[i]).isAssignableFrom(parameterTypes[i])) {
/* 596 */               foundConstructor = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 600 */           if (foundConstructor) {
/*     */             try {
/* 602 */               c.setAccessible(true);
/* 603 */               return c.newInstance(argumentList.toArray());
/* 604 */             } catch (Exception e) {
/* 605 */               throw new YAMLException(e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 610 */       throw new YAMLException("No suitable constructor with " + 
/* 611 */           String.valueOf(snode.getValue().size()) + " arguments found for " + node
/* 612 */           .getType());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<? extends Object> wrapIfPrimitive(Class<?> clazz) {
/* 618 */       if (!clazz.isPrimitive()) {
/* 619 */         return (Class)clazz;
/*     */       }
/* 621 */       if (clazz == int.class) {
/* 622 */         return (Class)Integer.class;
/*     */       }
/* 624 */       if (clazz == float.class) {
/* 625 */         return (Class)Float.class;
/*     */       }
/* 627 */       if (clazz == double.class) {
/* 628 */         return (Class)Double.class;
/*     */       }
/* 630 */       if (clazz == boolean.class) {
/* 631 */         return (Class)Boolean.class;
/*     */       }
/* 633 */       if (clazz == long.class) {
/* 634 */         return (Class)Long.class;
/*     */       }
/* 636 */       if (clazz == char.class) {
/* 637 */         return (Class)Character.class;
/*     */       }
/* 639 */       if (clazz == short.class) {
/* 640 */         return (Class)Short.class;
/*     */       }
/* 642 */       if (clazz == byte.class) {
/* 643 */         return (Class)Byte.class;
/*     */       }
/* 645 */       throw new YAMLException("Unexpected primitive " + clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 650 */       SequenceNode snode = (SequenceNode)node;
/* 651 */       if (List.class.isAssignableFrom(node.getType())) {
/* 652 */         List<Object> list = (List<Object>)object;
/* 653 */         Constructor.this.constructSequenceStep2(snode, list);
/* 654 */       } else if (node.getType().isArray()) {
/* 655 */         Constructor.this.constructArrayStep2(snode, object);
/*     */       } else {
/* 657 */         throw new YAMLException("Immutable objects cannot be recursive.");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected Class<?> getClassForNode(Node node) {
/* 663 */     Class<? extends Object> classForTag = this.typeTags.get(node.getTag());
/* 664 */     if (classForTag == null) {
/* 665 */       Class<?> cl; String name = node.getTag().getClassName();
/*     */       
/*     */       try {
/* 668 */         cl = getClassForName(name);
/* 669 */       } catch (ClassNotFoundException e) {
/* 670 */         throw new YAMLException("Class not found: " + name);
/*     */       } 
/* 672 */       this.typeTags.put(node.getTag(), cl);
/* 673 */       return cl;
/*     */     } 
/* 675 */     return classForTag;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/*     */     try {
/* 681 */       return SecClass.forName(name, true, Thread.currentThread().getContextClassLoader());
/* 682 */     } catch (ClassNotFoundException e) {
/* 683 */       return SecClass.forName(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\constructor\Constructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */