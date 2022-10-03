/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.external.biz.binaryEscape.BinaryEscape;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
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
/*     */ class SafeRepresenter
/*     */   extends BaseRepresenter
/*     */ {
/*     */   protected Map<Class<? extends Object>, Tag> classTags;
/*  48 */   protected TimeZone timeZone = null;
/*     */   protected DumperOptions.NonPrintableStyle nonPrintableStyle;
/*     */   
/*     */   public SafeRepresenter() {
/*  52 */     this(new DumperOptions());
/*     */   }
/*     */   
/*     */   public SafeRepresenter(DumperOptions options) {
/*  56 */     this.nullRepresenter = new RepresentNull();
/*  57 */     this.representers.put(String.class, new RepresentString());
/*  58 */     this.representers.put(Boolean.class, new RepresentBoolean());
/*  59 */     this.representers.put(Character.class, new RepresentString());
/*  60 */     this.representers.put(UUID.class, new RepresentUuid());
/*  61 */     this.representers.put(byte[].class, new RepresentByteArray());
/*     */     
/*  63 */     Represent primitiveArray = new RepresentPrimitiveArray();
/*  64 */     this.representers.put(short[].class, primitiveArray);
/*  65 */     this.representers.put(int[].class, primitiveArray);
/*  66 */     this.representers.put(long[].class, primitiveArray);
/*  67 */     this.representers.put(float[].class, primitiveArray);
/*  68 */     this.representers.put(double[].class, primitiveArray);
/*  69 */     this.representers.put(char[].class, primitiveArray);
/*  70 */     this.representers.put(boolean[].class, primitiveArray);
/*     */     
/*  72 */     this.multiRepresenters.put(Number.class, new RepresentNumber());
/*  73 */     this.multiRepresenters.put(List.class, new RepresentList());
/*  74 */     this.multiRepresenters.put(Map.class, new RepresentMap());
/*  75 */     this.multiRepresenters.put(Set.class, new RepresentSet());
/*  76 */     this.multiRepresenters.put(Iterator.class, new RepresentIterator());
/*  77 */     this.multiRepresenters.put((new Object[0]).getClass(), new RepresentArray());
/*  78 */     this.multiRepresenters.put(Date.class, new RepresentDate());
/*  79 */     this.multiRepresenters.put(Enum.class, new RepresentEnum());
/*  80 */     this.multiRepresenters.put(Calendar.class, new RepresentDate());
/*  81 */     this.classTags = new HashMap<>();
/*  82 */     this.nonPrintableStyle = options.getNonPrintableStyle();
/*     */   }
/*     */   
/*     */   protected Tag getTag(Class<?> clazz, Tag defaultTag) {
/*  86 */     if (this.classTags.containsKey(clazz)) {
/*  87 */       return this.classTags.get(clazz);
/*     */     }
/*  89 */     return defaultTag;
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
/*     */   public Tag addClassTag(Class<? extends Object> clazz, Tag tag) {
/* 104 */     if (tag == null) {
/* 105 */       throw new NullPointerException("Tag must be provided.");
/*     */     }
/* 107 */     return this.classTags.put(clazz, tag);
/*     */   }
/*     */   
/*     */   protected class RepresentNull implements Represent {
/*     */     public Node representData(Object data) {
/* 112 */       return SafeRepresenter.this.representScalar(Tag.NULL, "null");
/*     */     }
/*     */   }
/*     */   
/* 116 */   private static final Pattern MULTILINE_PATTERN = Pattern.compile("\n|| | ");
/*     */   
/*     */   protected class RepresentString implements Represent {
/*     */     public Node representData(Object data) {
/* 120 */       Tag tag = Tag.STR;
/* 121 */       DumperOptions.ScalarStyle style = null;
/* 122 */       String value = data.toString();
/* 123 */       if (SafeRepresenter.this.nonPrintableStyle == DumperOptions.NonPrintableStyle.BINARY && !StreamReader.isPrintable(value)) {
/* 124 */         char[] binary; tag = Tag.BINARY;
/*     */         
/*     */         try {
/* 127 */           byte[] bytes = value.getBytes("UTF-8");
/*     */ 
/*     */ 
/*     */           
/* 131 */           String checkValue = new String(bytes, "UTF-8");
/* 132 */           if (!checkValue.equals(value)) {
/* 133 */             throw new YAMLException("invalid string value has occurred");
/*     */           }
/* 135 */           binary = Base64Coder.encode(bytes);
/* 136 */         } catch (UnsupportedEncodingException e) {
/* 137 */           throw new YAMLException(e);
/*     */         } 
/* 139 */         value = String.valueOf(binary);
/* 140 */         style = DumperOptions.ScalarStyle.LITERAL;
/*     */       } 
/*     */ 
/*     */       
/* 144 */       if (SafeRepresenter.this.defaultScalarStyle == DumperOptions.ScalarStyle.PLAIN && SafeRepresenter.MULTILINE_PATTERN.matcher(value).find()) {
/* 145 */         style = DumperOptions.ScalarStyle.LITERAL;
/*     */       }
/* 147 */       return SafeRepresenter.this.representScalar(tag, value, style);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentBoolean implements Represent {
/*     */     public Node representData(Object data) {
/*     */       String value;
/* 154 */       if (Boolean.TRUE.equals(data)) {
/* 155 */         value = "true";
/*     */       } else {
/* 157 */         value = "false";
/*     */       } 
/* 159 */       return SafeRepresenter.this.representScalar(Tag.BOOL, value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentNumber implements Represent {
/*     */     public Node representData(Object data) {
/*     */       Tag tag;
/*     */       String value;
/* 167 */       if (data instanceof Byte || data instanceof Short || data instanceof Integer || data instanceof Long || data instanceof java.math.BigInteger) {
/*     */         
/* 169 */         tag = Tag.INT;
/* 170 */         value = data.toString();
/*     */       } else {
/* 172 */         Number number = (Number)data;
/* 173 */         tag = Tag.FLOAT;
/* 174 */         if (number.equals(Double.valueOf(Double.NaN))) {
/* 175 */           value = ".NaN";
/* 176 */         } else if (number.equals(Double.valueOf(Double.POSITIVE_INFINITY))) {
/* 177 */           value = ".inf";
/* 178 */         } else if (number.equals(Double.valueOf(Double.NEGATIVE_INFINITY))) {
/* 179 */           value = "-.inf";
/*     */         } else {
/* 181 */           value = number.toString();
/*     */         } 
/*     */       } 
/* 184 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentList
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 191 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), (List)data, DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentIterator
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 198 */       Iterator<Object> iter = (Iterator<Object>)data;
/* 199 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), new SafeRepresenter.IteratorWrapper(iter), DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IteratorWrapper
/*     */     implements Iterable<Object> {
/*     */     private Iterator<Object> iter;
/*     */     
/*     */     public IteratorWrapper(Iterator<Object> iter) {
/* 208 */       this.iter = iter;
/*     */     }
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 212 */       return this.iter;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentArray implements Represent {
/*     */     public Node representData(Object data) {
/* 218 */       Object[] array = (Object[])data;
/* 219 */       List<Object> list = Arrays.asList(array);
/* 220 */       return SafeRepresenter.this.representSequence(Tag.SEQ, list, DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class RepresentPrimitiveArray
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/* 231 */       Class<?> type = data.getClass().getComponentType();
/*     */       
/* 233 */       if (byte.class == type)
/* 234 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asByteList(data), DumperOptions.FlowStyle.AUTO); 
/* 235 */       if (short.class == type)
/* 236 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asShortList(data), DumperOptions.FlowStyle.AUTO); 
/* 237 */       if (int.class == type)
/* 238 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asIntList(data), DumperOptions.FlowStyle.AUTO); 
/* 239 */       if (long.class == type)
/* 240 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asLongList(data), DumperOptions.FlowStyle.AUTO); 
/* 241 */       if (float.class == type)
/* 242 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asFloatList(data), DumperOptions.FlowStyle.AUTO); 
/* 243 */       if (double.class == type)
/* 244 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asDoubleList(data), DumperOptions.FlowStyle.AUTO); 
/* 245 */       if (char.class == type)
/* 246 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asCharList(data), DumperOptions.FlowStyle.AUTO); 
/* 247 */       if (boolean.class == type) {
/* 248 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asBooleanList(data), DumperOptions.FlowStyle.AUTO);
/*     */       }
/*     */       
/* 251 */       throw new YAMLException("Unexpected primitive '" + type.getCanonicalName() + "'");
/*     */     }
/*     */     
/*     */     private List<Byte> asByteList(Object in) {
/* 255 */       byte[] array = (byte[])in;
/* 256 */       List<Byte> list = new ArrayList<>(array.length);
/* 257 */       for (int i = 0; i < array.length; i++)
/* 258 */         list.add(Byte.valueOf(array[i])); 
/* 259 */       return list;
/*     */     }
/*     */     
/*     */     private List<Short> asShortList(Object in) {
/* 263 */       short[] array = (short[])in;
/* 264 */       List<Short> list = new ArrayList<>(array.length);
/* 265 */       for (int i = 0; i < array.length; i++)
/* 266 */         list.add(Short.valueOf(array[i])); 
/* 267 */       return list;
/*     */     }
/*     */     
/*     */     private List<Integer> asIntList(Object in) {
/* 271 */       int[] array = (int[])in;
/* 272 */       List<Integer> list = new ArrayList<>(array.length);
/* 273 */       for (int i = 0; i < array.length; i++)
/* 274 */         list.add(Integer.valueOf(array[i])); 
/* 275 */       return list;
/*     */     }
/*     */     
/*     */     private List<Long> asLongList(Object in) {
/* 279 */       long[] array = (long[])in;
/* 280 */       List<Long> list = new ArrayList<>(array.length);
/* 281 */       for (int i = 0; i < array.length; i++)
/* 282 */         list.add(Long.valueOf(array[i])); 
/* 283 */       return list;
/*     */     }
/*     */     
/*     */     private List<Float> asFloatList(Object in) {
/* 287 */       float[] array = (float[])in;
/* 288 */       List<Float> list = new ArrayList<>(array.length);
/* 289 */       for (int i = 0; i < array.length; i++)
/* 290 */         list.add(Float.valueOf(array[i])); 
/* 291 */       return list;
/*     */     }
/*     */     
/*     */     private List<Double> asDoubleList(Object in) {
/* 295 */       double[] array = (double[])in;
/* 296 */       List<Double> list = new ArrayList<>(array.length);
/* 297 */       for (int i = 0; i < array.length; i++)
/* 298 */         list.add(Double.valueOf(array[i])); 
/* 299 */       return list;
/*     */     }
/*     */     
/*     */     private List<Character> asCharList(Object in) {
/* 303 */       char[] array = (char[])in;
/* 304 */       List<Character> list = new ArrayList<>(array.length);
/* 305 */       for (int i = 0; i < array.length; i++)
/* 306 */         list.add(Character.valueOf(array[i])); 
/* 307 */       return list;
/*     */     }
/*     */     
/*     */     private List<Boolean> asBooleanList(Object in) {
/* 311 */       boolean[] array = (boolean[])in;
/* 312 */       List<Boolean> list = new ArrayList<>(array.length);
/* 313 */       for (int i = 0; i < array.length; i++)
/* 314 */         list.add(Boolean.valueOf(array[i])); 
/* 315 */       return list;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentMap
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 322 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.MAP), (Map<?, ?>)data, DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentSet
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/* 330 */       Map<Object, Object> value = new LinkedHashMap<>();
/* 331 */       Set<Object> set = (Set<Object>)data;
/* 332 */       for (Object key : set) {
/* 333 */         value.put(key, null);
/*     */       }
/* 335 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.SET), value, DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentDate
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/*     */       Calendar calendar;
/* 343 */       if (data instanceof Calendar) {
/* 344 */         calendar = (Calendar)data;
/*     */       } else {
/* 346 */         calendar = Calendar.getInstance((SafeRepresenter.this.getTimeZone() == null) ? TimeZone.getTimeZone("UTC") : SafeRepresenter.this.timeZone);
/*     */         
/* 348 */         calendar.setTime((Date)data);
/*     */       } 
/* 350 */       int years = calendar.get(1);
/* 351 */       int months = calendar.get(2) + 1;
/* 352 */       int days = calendar.get(5);
/* 353 */       int hour24 = calendar.get(11);
/* 354 */       int minutes = calendar.get(12);
/* 355 */       int seconds = calendar.get(13);
/* 356 */       int millis = calendar.get(14);
/* 357 */       StringBuilder buffer = new StringBuilder(String.valueOf(years));
/* 358 */       while (buffer.length() < 4)
/*     */       {
/* 360 */         buffer.insert(0, "0");
/*     */       }
/* 362 */       buffer.append("-");
/* 363 */       if (months < 10) {
/* 364 */         buffer.append("0");
/*     */       }
/* 366 */       buffer.append(String.valueOf(months));
/* 367 */       buffer.append("-");
/* 368 */       if (days < 10) {
/* 369 */         buffer.append("0");
/*     */       }
/* 371 */       buffer.append(String.valueOf(days));
/* 372 */       buffer.append("T");
/* 373 */       if (hour24 < 10) {
/* 374 */         buffer.append("0");
/*     */       }
/* 376 */       buffer.append(String.valueOf(hour24));
/* 377 */       buffer.append(":");
/* 378 */       if (minutes < 10) {
/* 379 */         buffer.append("0");
/*     */       }
/* 381 */       buffer.append(String.valueOf(minutes));
/* 382 */       buffer.append(":");
/* 383 */       if (seconds < 10) {
/* 384 */         buffer.append("0");
/*     */       }
/* 386 */       buffer.append(String.valueOf(seconds));
/* 387 */       if (millis > 0) {
/* 388 */         if (millis < 10) {
/* 389 */           buffer.append(".00");
/* 390 */         } else if (millis < 100) {
/* 391 */           buffer.append(".0");
/*     */         } else {
/* 393 */           buffer.append(".");
/*     */         } 
/* 395 */         buffer.append(String.valueOf(millis));
/*     */       } 
/*     */ 
/*     */       
/* 399 */       int gmtOffset = calendar.getTimeZone().getOffset(calendar.getTime().getTime());
/* 400 */       if (gmtOffset == 0) {
/* 401 */         buffer.append('Z');
/*     */       } else {
/* 403 */         if (gmtOffset < 0) {
/* 404 */           buffer.append('-');
/* 405 */           gmtOffset *= -1;
/*     */         } else {
/* 407 */           buffer.append('+');
/*     */         } 
/* 409 */         int minutesOffset = gmtOffset / 60000;
/* 410 */         int hoursOffset = minutesOffset / 60;
/* 411 */         int partOfHour = minutesOffset % 60;
/*     */         
/* 413 */         if (hoursOffset < 10) {
/* 414 */           buffer.append('0');
/*     */         }
/* 416 */         buffer.append(hoursOffset);
/* 417 */         buffer.append(':');
/* 418 */         if (partOfHour < 10) {
/* 419 */           buffer.append('0');
/*     */         }
/* 421 */         buffer.append(partOfHour);
/*     */       } 
/*     */       
/* 424 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), DumperOptions.ScalarStyle.PLAIN);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentEnum implements Represent {
/*     */     public Node representData(Object data) {
/* 430 */       Tag tag = new Tag(data.getClass());
/* 431 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), ((Enum)data).name());
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentByteArray implements Represent {
/*     */     public Node representData(Object data) {
/* 437 */       return SafeRepresenter.this.representScalar(Tag.BINARY, BinaryEscape.escape((byte[])data), DumperOptions.ScalarStyle.LITERAL);
/*     */     }
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 442 */     return this.timeZone;
/*     */   }
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 446 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */   protected class RepresentUuid implements Represent {
/*     */     public Node representData(Object data) {
/* 451 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), new Tag(UUID.class)), data.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\representer\SafeRepresenter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */