/*     */ package org.yaml.snakeyaml.nodes;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.SecClass;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.util.UriEncoder;
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
/*     */ public final class Tag
/*     */ {
/*     */   public static final String PREFIX = "tag:yaml.org,2002:";
/*  33 */   public static final Tag YAML = new Tag("tag:yaml.org,2002:yaml");
/*  34 */   public static final Tag MERGE = new Tag("tag:yaml.org,2002:merge");
/*  35 */   public static final Tag SET = new Tag("tag:yaml.org,2002:set");
/*  36 */   public static final Tag PAIRS = new Tag("tag:yaml.org,2002:pairs");
/*  37 */   public static final Tag OMAP = new Tag("tag:yaml.org,2002:omap");
/*  38 */   public static final Tag BINARY = new Tag("tag:yaml.org,2002:binary");
/*  39 */   public static final Tag INT = new Tag("tag:yaml.org,2002:int");
/*  40 */   public static final Tag FLOAT = new Tag("tag:yaml.org,2002:float");
/*  41 */   public static final Tag TIMESTAMP = new Tag("tag:yaml.org,2002:timestamp");
/*  42 */   public static final Tag BOOL = new Tag("tag:yaml.org,2002:bool");
/*  43 */   public static final Tag NULL = new Tag("tag:yaml.org,2002:null");
/*  44 */   public static final Tag STR = new Tag("tag:yaml.org,2002:str");
/*  45 */   public static final Tag SEQ = new Tag("tag:yaml.org,2002:seq");
/*  46 */   public static final Tag MAP = new Tag("tag:yaml.org,2002:map");
/*     */   
/*  48 */   public static final Tag COMMENT = new Tag("tag:yaml.org,2002:comment");
/*     */ 
/*     */   
/*  51 */   protected static final Map<Tag, Set<Class<?>>> COMPATIBILITY_MAP = new HashMap<>(); static {
/*  52 */     Set<Class<?>> floatSet = new HashSet<>();
/*  53 */     floatSet.add(Double.class);
/*  54 */     floatSet.add(Float.class);
/*  55 */     floatSet.add(BigDecimal.class);
/*  56 */     COMPATIBILITY_MAP.put(FLOAT, floatSet);
/*     */     
/*  58 */     Set<Class<?>> intSet = new HashSet<>();
/*  59 */     intSet.add(Integer.class);
/*  60 */     intSet.add(Long.class);
/*  61 */     intSet.add(BigInteger.class);
/*  62 */     COMPATIBILITY_MAP.put(INT, intSet);
/*     */     
/*  64 */     Set<Class<?>> timestampSet = new HashSet<>();
/*  65 */     timestampSet.add(Date.class);
/*     */ 
/*     */     
/*     */     try {
/*  69 */       timestampSet.add(SecClass.forName("java.sql.Date"));
/*  70 */       timestampSet.add(SecClass.forName("java.sql.Timestamp"));
/*  71 */     } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */ 
/*     */     
/*  75 */     COMPATIBILITY_MAP.put(TIMESTAMP, timestampSet);
/*     */   }
/*     */   
/*     */   private final String value;
/*     */   private boolean secondary = false;
/*     */   
/*     */   public Tag(String tag) {
/*  82 */     if (tag == null)
/*  83 */       throw new NullPointerException("Tag must be provided."); 
/*  84 */     if (tag.length() == 0)
/*  85 */       throw new IllegalArgumentException("Tag must not be empty."); 
/*  86 */     if (tag.trim().length() != tag.length()) {
/*  87 */       throw new IllegalArgumentException("Tag must not contain leading or trailing spaces.");
/*     */     }
/*  89 */     this.value = UriEncoder.encode(tag);
/*  90 */     this.secondary = !tag.startsWith("tag:yaml.org,2002:");
/*     */   }
/*     */   
/*     */   public Tag(Class<? extends Object> clazz) {
/*  94 */     if (clazz == null) {
/*  95 */       throw new NullPointerException("Class for tag must be provided.");
/*     */     }
/*  97 */     this.value = "tag:yaml.org,2002:" + UriEncoder.encode(clazz.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag(URI uri) {
/* 106 */     if (uri == null) {
/* 107 */       throw new NullPointerException("URI for tag must be provided.");
/*     */     }
/* 109 */     this.value = uri.toASCIIString();
/*     */   }
/*     */   
/*     */   public boolean isSecondary() {
/* 113 */     return this.secondary;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 117 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean startsWith(String prefix) {
/* 121 */     return this.value.startsWith(prefix);
/*     */   }
/*     */   
/*     */   public String getClassName() {
/* 125 */     if (!this.value.startsWith("tag:yaml.org,2002:")) {
/* 126 */       throw new YAMLException("Invalid tag: " + this.value);
/*     */     }
/* 128 */     return UriEncoder.decode(this.value.substring("tag:yaml.org,2002:".length()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 133 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 138 */     if (obj instanceof Tag) {
/* 139 */       return this.value.equals(((Tag)obj).getValue());
/*     */     }
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 146 */     return this.value.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompatible(Class<?> clazz) {
/* 157 */     Set<Class<?>> set = COMPATIBILITY_MAP.get(this);
/* 158 */     if (set != null) {
/* 159 */       return set.contains(clazz);
/*     */     }
/* 161 */     return false;
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
/*     */   public boolean matches(Class<? extends Object> clazz) {
/* 173 */     return this.value.equals("tag:yaml.org,2002:" + clazz.getName());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\nodes\Tag.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */