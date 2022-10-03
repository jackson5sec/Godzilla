/*     */ package org.springframework.core.env;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.function.Predicate;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ final class ProfilesParser
/*     */ {
/*     */   static Profiles parse(String... expressions) {
/*  46 */     Assert.notEmpty((Object[])expressions, "Must specify at least one profile");
/*  47 */     Profiles[] parsed = new Profiles[expressions.length];
/*  48 */     for (int i = 0; i < expressions.length; i++) {
/*  49 */       parsed[i] = parseExpression(expressions[i]);
/*     */     }
/*  51 */     return new ParsedProfiles(expressions, parsed);
/*     */   }
/*     */   
/*     */   private static Profiles parseExpression(String expression) {
/*  55 */     Assert.hasText(expression, () -> "Invalid profile expression [" + expression + "]: must contain text");
/*  56 */     StringTokenizer tokens = new StringTokenizer(expression, "()&|!", true);
/*  57 */     return parseTokens(expression, tokens);
/*     */   }
/*     */   
/*     */   private static Profiles parseTokens(String expression, StringTokenizer tokens) {
/*  61 */     return parseTokens(expression, tokens, Context.NONE);
/*     */   }
/*     */   
/*     */   private static Profiles parseTokens(String expression, StringTokenizer tokens, Context context) {
/*  65 */     List<Profiles> elements = new ArrayList<>();
/*  66 */     Operator operator = null;
/*  67 */     while (tokens.hasMoreTokens()) {
/*  68 */       Profiles contents, merged; String token = tokens.nextToken().trim();
/*  69 */       if (token.isEmpty()) {
/*     */         continue;
/*     */       }
/*  72 */       switch (token) {
/*     */         case "(":
/*  74 */           contents = parseTokens(expression, tokens, Context.BRACKET);
/*  75 */           if (context == Context.INVERT) {
/*  76 */             return contents;
/*     */           }
/*  78 */           elements.add(contents);
/*     */           continue;
/*     */         case "&":
/*  81 */           assertWellFormed(expression, (operator == null || operator == Operator.AND));
/*  82 */           operator = Operator.AND;
/*     */           continue;
/*     */         case "|":
/*  85 */           assertWellFormed(expression, (operator == null || operator == Operator.OR));
/*  86 */           operator = Operator.OR;
/*     */           continue;
/*     */         case "!":
/*  89 */           elements.add(not(parseTokens(expression, tokens, Context.INVERT)));
/*     */           continue;
/*     */         case ")":
/*  92 */           merged = merge(expression, elements, operator);
/*  93 */           if (context == Context.BRACKET) {
/*  94 */             return merged;
/*     */           }
/*  96 */           elements.clear();
/*  97 */           elements.add(merged);
/*  98 */           operator = null;
/*     */           continue;
/*     */       } 
/* 101 */       Profiles value = equals(token);
/* 102 */       if (context == Context.INVERT) {
/* 103 */         return value;
/*     */       }
/* 105 */       elements.add(value);
/*     */     } 
/*     */     
/* 108 */     return merge(expression, elements, operator);
/*     */   }
/*     */   
/*     */   private static Profiles merge(String expression, List<Profiles> elements, @Nullable Operator operator) {
/* 112 */     assertWellFormed(expression, !elements.isEmpty());
/* 113 */     if (elements.size() == 1) {
/* 114 */       return elements.get(0);
/*     */     }
/* 116 */     Profiles[] profiles = elements.<Profiles>toArray(new Profiles[0]);
/* 117 */     return (operator == Operator.AND) ? and(profiles) : or(profiles);
/*     */   }
/*     */   
/*     */   private static void assertWellFormed(String expression, boolean wellFormed) {
/* 121 */     Assert.isTrue(wellFormed, () -> "Malformed profile expression [" + expression + "]");
/*     */   }
/*     */   
/*     */   private static Profiles or(Profiles... profiles) {
/* 125 */     return activeProfile -> Arrays.<Profiles>stream(profiles).anyMatch(isMatch(activeProfile));
/*     */   }
/*     */   
/*     */   private static Profiles and(Profiles... profiles) {
/* 129 */     return activeProfile -> Arrays.<Profiles>stream(profiles).allMatch(isMatch(activeProfile));
/*     */   }
/*     */   
/*     */   private static Profiles not(Profiles profiles) {
/* 133 */     return activeProfile -> !profiles.matches(activeProfile);
/*     */   }
/*     */   
/*     */   private static Profiles equals(String profile) {
/* 137 */     return activeProfile -> activeProfile.test(profile);
/*     */   }
/*     */   
/*     */   private static Predicate<Profiles> isMatch(Predicate<String> activeProfile) {
/* 141 */     return profiles -> profiles.matches(activeProfile);
/*     */   }
/*     */   
/*     */   private enum Operator {
/* 145 */     AND, OR; }
/*     */   
/*     */   private enum Context {
/* 148 */     NONE, INVERT, BRACKET;
/*     */   }
/*     */   
/*     */   private static class ParsedProfiles
/*     */     implements Profiles {
/* 153 */     private final Set<String> expressions = new LinkedHashSet<>();
/*     */     
/*     */     private final Profiles[] parsed;
/*     */     
/*     */     ParsedProfiles(String[] expressions, Profiles[] parsed) {
/* 158 */       Collections.addAll(this.expressions, expressions);
/* 159 */       this.parsed = parsed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Predicate<String> activeProfiles) {
/* 164 */       for (Profiles candidate : this.parsed) {
/* 165 */         if (candidate.matches(activeProfiles)) {
/* 166 */           return true;
/*     */         }
/*     */       } 
/* 169 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 174 */       return this.expressions.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 179 */       if (this == obj) {
/* 180 */         return true;
/*     */       }
/* 182 */       if (obj == null) {
/* 183 */         return false;
/*     */       }
/* 185 */       if (getClass() != obj.getClass()) {
/* 186 */         return false;
/*     */       }
/* 188 */       ParsedProfiles that = (ParsedProfiles)obj;
/* 189 */       return this.expressions.equals(that.expressions);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 194 */       return StringUtils.collectionToDelimitedString(this.expressions, " or ");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\ProfilesParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */