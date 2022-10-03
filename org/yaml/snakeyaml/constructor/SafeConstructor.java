/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.binaryEscape.BinaryEscape;
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
/*     */ public class SafeConstructor
/*     */   extends BaseConstructor
/*     */ {
/*  49 */   public static final ConstructUndefined undefinedConstructor = new ConstructUndefined();
/*     */   
/*     */   public SafeConstructor() {
/*  52 */     this(new LoaderOptions());
/*     */   }
/*     */   
/*     */   public SafeConstructor(LoaderOptions loadingConfig) {
/*  56 */     super(loadingConfig);
/*  57 */     this.yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
/*  58 */     this.yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
/*  59 */     this.yamlConstructors.put(Tag.INT, new ConstructYamlInt());
/*  60 */     this.yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
/*  61 */     this.yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
/*  62 */     this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
/*  63 */     this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
/*  64 */     this.yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
/*  65 */     this.yamlConstructors.put(Tag.SET, new ConstructYamlSet());
/*  66 */     this.yamlConstructors.put(Tag.STR, new ConstructYamlStr());
/*  67 */     this.yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
/*  68 */     this.yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
/*  69 */     this.yamlConstructors.put(null, undefinedConstructor);
/*  70 */     this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
/*  71 */     this.yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
/*  72 */     this.yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flattenMapping(MappingNode node) {
/*  77 */     processDuplicateKeys(node);
/*  78 */     if (node.isMerged()) {
/*  79 */       node.setValue(mergeNode(node, true, new HashMap<>(), new ArrayList<>()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processDuplicateKeys(MappingNode node) {
/*  85 */     List<NodeTuple> nodeValue = node.getValue();
/*  86 */     Map<Object, Integer> keys = new HashMap<>(nodeValue.size());
/*  87 */     TreeSet<Integer> toRemove = new TreeSet<>();
/*  88 */     int i = 0;
/*  89 */     for (NodeTuple tuple : nodeValue) {
/*  90 */       Node keyNode = tuple.getKeyNode();
/*  91 */       if (!keyNode.getTag().equals(Tag.MERGE)) {
/*  92 */         Object key = constructObject(keyNode);
/*  93 */         if (key != null) {
/*     */           try {
/*  95 */             key.hashCode();
/*  96 */           } catch (Exception e) {
/*  97 */             throw new ConstructorException("while constructing a mapping", node
/*  98 */                 .getStartMark(), "found unacceptable key " + key, tuple
/*  99 */                 .getKeyNode().getStartMark(), e);
/*     */           } 
/*     */         }
/*     */         
/* 103 */         Integer prevIndex = keys.put(key, Integer.valueOf(i));
/* 104 */         if (prevIndex != null) {
/* 105 */           if (!isAllowDuplicateKeys()) {
/* 106 */             throw new DuplicateKeyException(node.getStartMark(), key, tuple
/* 107 */                 .getKeyNode().getStartMark());
/*     */           }
/* 109 */           toRemove.add(prevIndex);
/*     */         } 
/*     */       } 
/* 112 */       i++;
/*     */     } 
/*     */     
/* 115 */     Iterator<Integer> indices2remove = toRemove.descendingIterator();
/* 116 */     while (indices2remove.hasNext()) {
/* 117 */       nodeValue.remove(((Integer)indices2remove.next()).intValue());
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
/*     */   private List<NodeTuple> mergeNode(MappingNode node, boolean isPreffered, Map<Object, Integer> key2index, List<NodeTuple> values) {
/* 137 */     Iterator<NodeTuple> iter = node.getValue().iterator();
/* 138 */     while (iter.hasNext()) {
/* 139 */       NodeTuple nodeTuple = iter.next();
/* 140 */       Node keyNode = nodeTuple.getKeyNode();
/* 141 */       Node valueNode = nodeTuple.getValueNode();
/* 142 */       if (keyNode.getTag().equals(Tag.MERGE)) {
/* 143 */         MappingNode mn; SequenceNode sn; List<Node> vals; iter.remove();
/* 144 */         switch (valueNode.getNodeId()) {
/*     */           case mapping:
/* 146 */             mn = (MappingNode)valueNode;
/* 147 */             mergeNode(mn, false, key2index, values);
/*     */             continue;
/*     */           case sequence:
/* 150 */             sn = (SequenceNode)valueNode;
/* 151 */             vals = sn.getValue();
/* 152 */             for (Node subnode : vals) {
/* 153 */               if (!(subnode instanceof MappingNode)) {
/* 154 */                 throw new ConstructorException("while constructing a mapping", node
/* 155 */                     .getStartMark(), "expected a mapping for merging, but found " + subnode
/*     */                     
/* 157 */                     .getNodeId(), subnode
/* 158 */                     .getStartMark());
/*     */               }
/* 160 */               MappingNode mnode = (MappingNode)subnode;
/* 161 */               mergeNode(mnode, false, key2index, values);
/*     */             } 
/*     */             continue;
/*     */         } 
/* 165 */         throw new ConstructorException("while constructing a mapping", node
/* 166 */             .getStartMark(), "expected a mapping or list of mappings for merging, but found " + valueNode
/*     */             
/* 168 */             .getNodeId(), valueNode
/* 169 */             .getStartMark());
/*     */       } 
/*     */ 
/*     */       
/* 173 */       Object key = constructObject(keyNode);
/* 174 */       if (!key2index.containsKey(key)) {
/* 175 */         values.add(nodeTuple);
/*     */         
/* 177 */         key2index.put(key, Integer.valueOf(values.size() - 1)); continue;
/* 178 */       }  if (isPreffered)
/*     */       {
/*     */         
/* 181 */         values.set(((Integer)key2index.get(key)).intValue(), nodeTuple);
/*     */       }
/*     */     } 
/*     */     
/* 185 */     return values;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 190 */     flattenMapping(node);
/* 191 */     super.constructMapping2ndStep(node, mapping);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 196 */     flattenMapping(node);
/* 197 */     super.constructSet2ndStep(node, set);
/*     */   }
/*     */   
/*     */   public class ConstructYamlNull
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 203 */       if (node != null) BaseConstructor.constructScalar((ScalarNode)node); 
/* 204 */       return null;
/*     */     }
/*     */   }
/*     */   
/* 208 */   private static final Map<String, Boolean> BOOL_VALUES = new HashMap<>();
/*     */   static {
/* 210 */     BOOL_VALUES.put("yes", Boolean.TRUE);
/* 211 */     BOOL_VALUES.put("no", Boolean.FALSE);
/* 212 */     BOOL_VALUES.put("true", Boolean.TRUE);
/* 213 */     BOOL_VALUES.put("false", Boolean.FALSE);
/* 214 */     BOOL_VALUES.put("on", Boolean.TRUE);
/* 215 */     BOOL_VALUES.put("off", Boolean.FALSE);
/*     */   }
/*     */   
/*     */   public class ConstructYamlBool
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 221 */       String val = BaseConstructor.constructScalar((ScalarNode)node);
/* 222 */       return SafeConstructor.BOOL_VALUES.get(val.toLowerCase());
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlInt
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 229 */       String value = BaseConstructor.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 230 */       int sign = 1;
/* 231 */       char first = value.charAt(0);
/* 232 */       if (first == '-') {
/* 233 */         sign = -1;
/* 234 */         value = value.substring(1);
/* 235 */       } else if (first == '+') {
/* 236 */         value = value.substring(1);
/*     */       } 
/* 238 */       int base = 10;
/* 239 */       if ("0".equals(value))
/* 240 */         return Integer.valueOf(0); 
/* 241 */       if (value.startsWith("0b"))
/* 242 */       { value = value.substring(2);
/* 243 */         base = 2; }
/* 244 */       else if (value.startsWith("0x"))
/* 245 */       { value = value.substring(2);
/* 246 */         base = 16; }
/* 247 */       else if (value.startsWith("0"))
/* 248 */       { value = value.substring(1);
/* 249 */         base = 8; }
/* 250 */       else { if (value.indexOf(':') != -1) {
/* 251 */           String[] digits = value.split(":");
/* 252 */           int bes = 1;
/* 253 */           int val = 0;
/* 254 */           for (int i = 0, j = digits.length; i < j; i++) {
/* 255 */             val = (int)(val + Long.parseLong(digits[j - i - 1]) * bes);
/* 256 */             bes *= 60;
/*     */           } 
/* 258 */           return SafeConstructor.this.createNumber(sign, String.valueOf(val), 10);
/*     */         } 
/* 260 */         return SafeConstructor.this.createNumber(sign, value, 10); }
/*     */       
/* 262 */       return SafeConstructor.this.createNumber(sign, value, base);
/*     */     }
/*     */   }
/*     */   
/* 266 */   private static final int[][] RADIX_MAX = new int[17][2];
/*     */   static {
/* 268 */     int[] radixList = { 2, 8, 10, 16 };
/* 269 */     for (int radix : radixList) {
/* 270 */       (new int[2])[0] = maxLen(2147483647, radix); (new int[2])[1] = maxLen(Long.MAX_VALUE, radix); RADIX_MAX[radix] = new int[2];
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int maxLen(int max, int radix) {
/* 275 */     return Integer.toString(max, radix).length();
/*     */   }
/*     */   private static int maxLen(long max, int radix) {
/* 278 */     return Long.toString(max, radix).length();
/*     */   } private Number createNumber(int sign, String number, int radix) {
/*     */     Number result;
/* 281 */     int len = (number != null) ? number.length() : 0;
/* 282 */     if (sign < 0) {
/* 283 */       number = "-" + number;
/*     */     }
/* 285 */     int[] maxArr = (radix < RADIX_MAX.length) ? RADIX_MAX[radix] : null;
/* 286 */     if (maxArr != null) {
/* 287 */       boolean gtInt = (len > maxArr[0]);
/* 288 */       if (gtInt) {
/* 289 */         if (len > maxArr[1]) {
/* 290 */           return new BigInteger(number, radix);
/*     */         }
/* 292 */         return createLongOrBigInteger(number, radix);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 297 */       result = Integer.valueOf(number, radix);
/* 298 */     } catch (NumberFormatException e) {
/* 299 */       result = createLongOrBigInteger(number, radix);
/*     */     } 
/* 301 */     return result;
/*     */   }
/*     */   
/*     */   protected static Number createLongOrBigInteger(String number, int radix) {
/*     */     try {
/* 306 */       return Long.valueOf(number, radix);
/* 307 */     } catch (NumberFormatException e1) {
/* 308 */       return new BigInteger(number, radix);
/*     */     } 
/*     */   }
/*     */   
/*     */   public class ConstructYamlFloat
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 315 */       String value = BaseConstructor.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 316 */       int sign = 1;
/* 317 */       char first = value.charAt(0);
/* 318 */       if (first == '-') {
/* 319 */         sign = -1;
/* 320 */         value = value.substring(1);
/* 321 */       } else if (first == '+') {
/* 322 */         value = value.substring(1);
/*     */       } 
/* 324 */       String valLower = value.toLowerCase();
/* 325 */       if (".inf".equals(valLower))
/* 326 */         return 
/* 327 */           Double.valueOf((sign == -1) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY); 
/* 328 */       if (".nan".equals(valLower))
/* 329 */         return Double.valueOf(Double.NaN); 
/* 330 */       if (value.indexOf(':') != -1) {
/* 331 */         String[] digits = value.split(":");
/* 332 */         int bes = 1;
/* 333 */         double val = 0.0D;
/* 334 */         for (int i = 0, j = digits.length; i < j; i++) {
/* 335 */           val += Double.parseDouble(digits[j - i - 1]) * bes;
/* 336 */           bes *= 60;
/*     */         } 
/* 338 */         return Double.valueOf(sign * val);
/*     */       } 
/* 340 */       Double d = Double.valueOf(value);
/* 341 */       return Double.valueOf(d.doubleValue() * sign);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ConstructYamlBinary
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 350 */       String noWhiteSpaces = BaseConstructor.constructScalar((ScalarNode)node);
/* 351 */       byte[] decoded = BinaryEscape.unescapeToBytes(noWhiteSpaces);
/* 352 */       return decoded;
/*     */     }
/*     */   }
/*     */   
/* 356 */   private static final Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
/*     */ 
/*     */   
/* 359 */   private static final Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
/*     */   
/*     */   public static class ConstructYamlTimestamp extends AbstractConstruct {
/*     */     private Calendar calendar;
/*     */     
/*     */     public Calendar getCalendar() {
/* 365 */       return this.calendar;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/*     */       TimeZone timeZone;
/* 370 */       ScalarNode scalar = (ScalarNode)node;
/* 371 */       String nodeValue = scalar.getValue();
/* 372 */       Matcher match = SafeConstructor.YMD_REGEXP.matcher(nodeValue);
/* 373 */       if (match.matches()) {
/* 374 */         String str1 = match.group(1);
/* 375 */         String str2 = match.group(2);
/* 376 */         String str3 = match.group(3);
/* 377 */         this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/* 378 */         this.calendar.clear();
/* 379 */         this.calendar.set(1, Integer.parseInt(str1));
/*     */         
/* 381 */         this.calendar.set(2, Integer.parseInt(str2) - 1);
/* 382 */         this.calendar.set(5, Integer.parseInt(str3));
/* 383 */         return this.calendar.getTime();
/*     */       } 
/* 385 */       match = SafeConstructor.TIMESTAMP_REGEXP.matcher(nodeValue);
/* 386 */       if (!match.matches()) {
/* 387 */         throw new YAMLException("Unexpected timestamp: " + nodeValue);
/*     */       }
/* 389 */       String year_s = match.group(1);
/* 390 */       String month_s = match.group(2);
/* 391 */       String day_s = match.group(3);
/* 392 */       String hour_s = match.group(4);
/* 393 */       String min_s = match.group(5);
/*     */       
/* 395 */       String seconds = match.group(6);
/* 396 */       String millis = match.group(7);
/* 397 */       if (millis != null) {
/* 398 */         seconds = seconds + "." + millis;
/*     */       }
/* 400 */       double fractions = Double.parseDouble(seconds);
/* 401 */       int sec_s = (int)Math.round(Math.floor(fractions));
/* 402 */       int usec = (int)Math.round((fractions - sec_s) * 1000.0D);
/*     */       
/* 404 */       String timezoneh_s = match.group(8);
/* 405 */       String timezonem_s = match.group(9);
/*     */       
/* 407 */       if (timezoneh_s != null) {
/* 408 */         String time = (timezonem_s != null) ? (":" + timezonem_s) : "00";
/* 409 */         timeZone = TimeZone.getTimeZone("GMT" + timezoneh_s + time);
/*     */       } else {
/*     */         
/* 412 */         timeZone = TimeZone.getTimeZone("UTC");
/*     */       } 
/* 414 */       this.calendar = Calendar.getInstance(timeZone);
/* 415 */       this.calendar.set(1, Integer.parseInt(year_s));
/*     */       
/* 417 */       this.calendar.set(2, Integer.parseInt(month_s) - 1);
/* 418 */       this.calendar.set(5, Integer.parseInt(day_s));
/* 419 */       this.calendar.set(11, Integer.parseInt(hour_s));
/* 420 */       this.calendar.set(12, Integer.parseInt(min_s));
/* 421 */       this.calendar.set(13, sec_s);
/* 422 */       this.calendar.set(14, usec);
/* 423 */       return this.calendar.getTime();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class ConstructYamlOmap
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 433 */       Map<Object, Object> omap = new LinkedHashMap<>();
/* 434 */       if (!(node instanceof SequenceNode)) {
/* 435 */         throw new ConstructorException("while constructing an ordered map", node
/* 436 */             .getStartMark(), "expected a sequence, but found " + node.getNodeId(), node
/* 437 */             .getStartMark());
/*     */       }
/* 439 */       SequenceNode snode = (SequenceNode)node;
/* 440 */       for (Node subnode : snode.getValue()) {
/* 441 */         if (!(subnode instanceof MappingNode)) {
/* 442 */           throw new ConstructorException("while constructing an ordered map", node
/* 443 */               .getStartMark(), "expected a mapping of length 1, but found " + subnode
/* 444 */               .getNodeId(), subnode
/* 445 */               .getStartMark());
/*     */         }
/* 447 */         MappingNode mnode = (MappingNode)subnode;
/* 448 */         if (mnode.getValue().size() != 1) {
/* 449 */           throw new ConstructorException("while constructing an ordered map", node
/* 450 */               .getStartMark(), "expected a single mapping item, but found " + mnode
/* 451 */               .getValue().size() + " items", mnode
/* 452 */               .getStartMark());
/*     */         }
/* 454 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 455 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 456 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 457 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 458 */         omap.put(key, value);
/*     */       } 
/* 460 */       return omap;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class ConstructYamlPairs
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 469 */       if (!(node instanceof SequenceNode)) {
/* 470 */         throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a sequence, but found " + node
/* 471 */             .getNodeId(), node.getStartMark());
/*     */       }
/* 473 */       SequenceNode snode = (SequenceNode)node;
/* 474 */       List<Object[]> pairs = new ArrayList(snode.getValue().size());
/* 475 */       for (Node subnode : snode.getValue()) {
/* 476 */         if (!(subnode instanceof MappingNode)) {
/* 477 */           throw new ConstructorException("while constructingpairs", node.getStartMark(), "expected a mapping of length 1, but found " + subnode
/* 478 */               .getNodeId(), subnode
/* 479 */               .getStartMark());
/*     */         }
/* 481 */         MappingNode mnode = (MappingNode)subnode;
/* 482 */         if (mnode.getValue().size() != 1) {
/* 483 */           throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a single mapping item, but found " + mnode
/* 484 */               .getValue().size() + " items", mnode
/*     */               
/* 486 */               .getStartMark());
/*     */         }
/* 488 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 489 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 490 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 491 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 492 */         pairs.add(new Object[] { key, value });
/*     */       } 
/* 494 */       return pairs;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSet
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 501 */       if (node.isTwoStepsConstruction()) {
/* 502 */         return SafeConstructor.this.constructedObjects.containsKey(node) ? SafeConstructor.this.constructedObjects.get(node) : SafeConstructor.this
/* 503 */           .createDefaultSet(((MappingNode)node).getValue().size());
/*     */       }
/* 505 */       return SafeConstructor.this.constructSet((MappingNode)node);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 512 */       if (node.isTwoStepsConstruction()) {
/* 513 */         SafeConstructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 515 */         throw new YAMLException("Unexpected recursive set structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlStr
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 523 */       return BaseConstructor.constructScalar((ScalarNode)node);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSeq
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 530 */       SequenceNode seqNode = (SequenceNode)node;
/* 531 */       if (node.isTwoStepsConstruction()) {
/* 532 */         return SafeConstructor.this.newList(seqNode);
/*     */       }
/* 534 */       return SafeConstructor.this.constructSequence(seqNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object data) {
/* 541 */       if (node.isTwoStepsConstruction()) {
/* 542 */         SafeConstructor.this.constructSequenceStep2((SequenceNode)node, (List)data);
/*     */       } else {
/* 544 */         throw new YAMLException("Unexpected recursive sequence structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlMap
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 552 */       MappingNode mnode = (MappingNode)node;
/* 553 */       if (node.isTwoStepsConstruction()) {
/* 554 */         return SafeConstructor.this.createDefaultMap(mnode.getValue().size());
/*     */       }
/* 556 */       return SafeConstructor.this.constructMapping(mnode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 563 */       if (node.isTwoStepsConstruction()) {
/* 564 */         SafeConstructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/*     */       } else {
/* 566 */         throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ConstructUndefined
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 574 */       throw new ConstructorException(null, null, "could not determine a constructor for the tag " + node
/* 575 */           .getTag(), node
/* 576 */           .getStartMark());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\constructor\SafeConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */