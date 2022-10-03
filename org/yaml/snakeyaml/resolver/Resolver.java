/*     */ package org.yaml.snakeyaml.resolver;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
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
/*     */ public class Resolver
/*     */ {
/*  32 */   public static final Pattern BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static final Pattern FLOAT = Pattern.compile("^([-+]?(?:[0-9][0-9_]*)\\.[0-9_]*(?:[eE][-+]?[0-9]+)?|[-+]?(?:[0-9][0-9_]*)(?:[eE][-+]?[0-9]+)|[-+]?\\.[0-9_]+(?:[eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static final Pattern INT = Pattern.compile("^(?:[-+]?0b_*[0-1]+[0-1_]*|[-+]?0_*[0-7]+[0-7_]*|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x_*[0-9a-fA-F]+[0-9a-fA-F_]*|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final Pattern MERGE = Pattern.compile("^(?:<<)$");
/*  56 */   public static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
/*  57 */   public static final Pattern EMPTY = Pattern.compile("^$");
/*     */   
/*  59 */   public static final Pattern TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
/*  60 */   public static final Pattern VALUE = Pattern.compile("^(?:=)$");
/*  61 */   public static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");
/*     */   
/*  63 */   protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers = new HashMap<>();
/*     */   
/*     */   protected void addImplicitResolvers() {
/*  66 */     addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     addImplicitResolver(Tag.INT, INT, "-+0123456789");
/*  73 */     addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
/*  74 */     addImplicitResolver(Tag.MERGE, MERGE, "<");
/*  75 */     addImplicitResolver(Tag.NULL, NULL, "~nN\000");
/*  76 */     addImplicitResolver(Tag.NULL, EMPTY, null);
/*  77 */     addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     addImplicitResolver(Tag.YAML, YAML, "!&*");
/*     */   }
/*     */   
/*     */   public Resolver() {
/*  86 */     addImplicitResolvers();
/*     */   }
/*     */   
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
/*  90 */     if (first == null) {
/*  91 */       List<ResolverTuple> curr = this.yamlImplicitResolvers.get(null);
/*  92 */       if (curr == null) {
/*  93 */         curr = new ArrayList<>();
/*  94 */         this.yamlImplicitResolvers.put(null, curr);
/*     */       } 
/*  96 */       curr.add(new ResolverTuple(tag, regexp));
/*     */     } else {
/*  98 */       char[] chrs = first.toCharArray();
/*  99 */       for (int i = 0, j = chrs.length; i < j; i++) {
/* 100 */         Character theC = Character.valueOf(chrs[i]);
/* 101 */         if (theC.charValue() == '\000')
/*     */         {
/* 103 */           theC = null;
/*     */         }
/* 105 */         List<ResolverTuple> curr = this.yamlImplicitResolvers.get(theC);
/* 106 */         if (curr == null) {
/* 107 */           curr = new ArrayList<>();
/* 108 */           this.yamlImplicitResolvers.put(theC, curr);
/*     */         } 
/* 110 */         curr.add(new ResolverTuple(tag, regexp));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Tag resolve(NodeId kind, String value, boolean implicit) {
/* 116 */     if (kind == NodeId.scalar && implicit) {
/*     */       List<ResolverTuple> resolvers;
/* 118 */       if (value.length() == 0) {
/* 119 */         resolvers = this.yamlImplicitResolvers.get(Character.valueOf(false));
/*     */       } else {
/* 121 */         resolvers = this.yamlImplicitResolvers.get(Character.valueOf(value.charAt(0)));
/*     */       } 
/* 123 */       if (resolvers != null) {
/* 124 */         for (ResolverTuple v : resolvers) {
/* 125 */           Tag tag = v.getTag();
/* 126 */           Pattern regexp = v.getRegexp();
/* 127 */           if (regexp.matcher(value).matches()) {
/* 128 */             return tag;
/*     */           }
/*     */         } 
/*     */       }
/* 132 */       if (this.yamlImplicitResolvers.containsKey(null)) {
/* 133 */         for (ResolverTuple v : this.yamlImplicitResolvers.get(null)) {
/* 134 */           Tag tag = v.getTag();
/* 135 */           Pattern regexp = v.getRegexp();
/* 136 */           if (regexp.matcher(value).matches()) {
/* 137 */             return tag;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 142 */     switch (kind) {
/*     */       case scalar:
/* 144 */         return Tag.STR;
/*     */       case sequence:
/* 146 */         return Tag.SEQ;
/*     */     } 
/* 148 */     return Tag.MAP;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\resolver\Resolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */