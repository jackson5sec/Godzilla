/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.stream.Collectors;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public abstract class MimeTypeUtils
/*     */ {
/*  47 */   private static final byte[] BOUNDARY_CHARS = new byte[] { 45, 95, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static final Comparator<MimeType> SPECIFICITY_COMPARATOR = new MimeType.SpecificityComparator<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType ALL;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ALL_VALUE = "*/*";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType APPLICATION_JSON;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String APPLICATION_JSON_VALUE = "application/json";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType APPLICATION_OCTET_STREAM;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType APPLICATION_XML;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String APPLICATION_XML_VALUE = "application/xml";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType IMAGE_GIF;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String IMAGE_GIF_VALUE = "image/gif";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType IMAGE_JPEG;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String IMAGE_JPEG_VALUE = "image/jpeg";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType IMAGE_PNG;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String IMAGE_PNG_VALUE = "image/png";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType TEXT_HTML;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TEXT_HTML_VALUE = "text/html";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType TEXT_PLAIN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TEXT_PLAIN_VALUE = "text/plain";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final MimeType TEXT_XML;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TEXT_XML_VALUE = "text/xml";
/*     */ 
/*     */ 
/*     */   
/* 159 */   private static final ConcurrentLruCache<String, MimeType> cachedMimeTypes = new ConcurrentLruCache<>(64, MimeTypeUtils::parseMimeTypeInternal);
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static volatile Random random;
/*     */ 
/*     */   
/*     */   static {
/* 167 */     ALL = new MimeType("*", "*");
/* 168 */     APPLICATION_JSON = new MimeType("application", "json");
/* 169 */     APPLICATION_OCTET_STREAM = new MimeType("application", "octet-stream");
/* 170 */     APPLICATION_XML = new MimeType("application", "xml");
/* 171 */     IMAGE_GIF = new MimeType("image", "gif");
/* 172 */     IMAGE_JPEG = new MimeType("image", "jpeg");
/* 173 */     IMAGE_PNG = new MimeType("image", "png");
/* 174 */     TEXT_HTML = new MimeType("text", "html");
/* 175 */     TEXT_PLAIN = new MimeType("text", "plain");
/* 176 */     TEXT_XML = new MimeType("text", "xml");
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
/*     */   public static MimeType parseMimeType(String mimeType) {
/* 188 */     if (!StringUtils.hasLength(mimeType)) {
/* 189 */       throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
/*     */     }
/*     */     
/* 192 */     if (mimeType.startsWith("multipart")) {
/* 193 */       return parseMimeTypeInternal(mimeType);
/*     */     }
/* 195 */     return cachedMimeTypes.get(mimeType);
/*     */   }
/*     */   
/*     */   private static MimeType parseMimeTypeInternal(String mimeType) {
/* 199 */     int index = mimeType.indexOf(';');
/* 200 */     String fullType = ((index >= 0) ? mimeType.substring(0, index) : mimeType).trim();
/* 201 */     if (fullType.isEmpty()) {
/* 202 */       throw new InvalidMimeTypeException(mimeType, "'mimeType' must not be empty");
/*     */     }
/*     */ 
/*     */     
/* 206 */     if ("*".equals(fullType)) {
/* 207 */       fullType = "*/*";
/*     */     }
/* 209 */     int subIndex = fullType.indexOf('/');
/* 210 */     if (subIndex == -1) {
/* 211 */       throw new InvalidMimeTypeException(mimeType, "does not contain '/'");
/*     */     }
/* 213 */     if (subIndex == fullType.length() - 1) {
/* 214 */       throw new InvalidMimeTypeException(mimeType, "does not contain subtype after '/'");
/*     */     }
/* 216 */     String type = fullType.substring(0, subIndex);
/* 217 */     String subtype = fullType.substring(subIndex + 1);
/* 218 */     if ("*".equals(type) && !"*".equals(subtype)) {
/* 219 */       throw new InvalidMimeTypeException(mimeType, "wildcard type is legal only in '*/*' (all mime types)");
/*     */     }
/*     */     
/* 222 */     Map<String, String> parameters = null;
/*     */     do {
/* 224 */       int nextIndex = index + 1;
/* 225 */       boolean quoted = false;
/* 226 */       while (nextIndex < mimeType.length()) {
/* 227 */         char ch = mimeType.charAt(nextIndex);
/* 228 */         if (ch == ';') {
/* 229 */           if (!quoted) {
/*     */             break;
/*     */           }
/*     */         }
/* 233 */         else if (ch == '"') {
/* 234 */           quoted = !quoted;
/*     */         } 
/* 236 */         nextIndex++;
/*     */       } 
/* 238 */       String parameter = mimeType.substring(index + 1, nextIndex).trim();
/* 239 */       if (parameter.length() > 0) {
/* 240 */         if (parameters == null) {
/* 241 */           parameters = new LinkedHashMap<>(4);
/*     */         }
/* 243 */         int eqIndex = parameter.indexOf('=');
/* 244 */         if (eqIndex >= 0) {
/* 245 */           String attribute = parameter.substring(0, eqIndex).trim();
/* 246 */           String value = parameter.substring(eqIndex + 1).trim();
/* 247 */           parameters.put(attribute, value);
/*     */         } 
/*     */       } 
/* 250 */       index = nextIndex;
/*     */     }
/* 252 */     while (index < mimeType.length());
/*     */     
/*     */     try {
/* 255 */       return new MimeType(type, subtype, parameters);
/*     */     }
/* 257 */     catch (UnsupportedCharsetException ex) {
/* 258 */       throw new InvalidMimeTypeException(mimeType, "unsupported charset '" + ex.getCharsetName() + "'");
/*     */     }
/* 260 */     catch (IllegalArgumentException ex) {
/* 261 */       throw new InvalidMimeTypeException(mimeType, ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<MimeType> parseMimeTypes(String mimeTypes) {
/* 272 */     if (!StringUtils.hasLength(mimeTypes)) {
/* 273 */       return Collections.emptyList();
/*     */     }
/* 275 */     return (List<MimeType>)tokenize(mimeTypes).stream()
/* 276 */       .filter(StringUtils::hasText)
/* 277 */       .map(MimeTypeUtils::parseMimeType)
/* 278 */       .collect(Collectors.toList());
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
/*     */   public static List<String> tokenize(String mimeTypes) {
/* 290 */     if (!StringUtils.hasLength(mimeTypes)) {
/* 291 */       return Collections.emptyList();
/*     */     }
/* 293 */     List<String> tokens = new ArrayList<>();
/* 294 */     boolean inQuotes = false;
/* 295 */     int startIndex = 0;
/* 296 */     int i = 0;
/* 297 */     while (i < mimeTypes.length()) {
/* 298 */       switch (mimeTypes.charAt(i)) {
/*     */         case '"':
/* 300 */           inQuotes = !inQuotes;
/*     */           break;
/*     */         case ',':
/* 303 */           if (!inQuotes) {
/* 304 */             tokens.add(mimeTypes.substring(startIndex, i));
/* 305 */             startIndex = i + 1;
/*     */           } 
/*     */           break;
/*     */         case '\\':
/* 309 */           i++;
/*     */           break;
/*     */       } 
/* 312 */       i++;
/*     */     } 
/* 314 */     tokens.add(mimeTypes.substring(startIndex));
/* 315 */     return tokens;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Collection<? extends MimeType> mimeTypes) {
/* 325 */     StringBuilder builder = new StringBuilder();
/* 326 */     for (Iterator<? extends MimeType> iterator = mimeTypes.iterator(); iterator.hasNext(); ) {
/* 327 */       MimeType mimeType = iterator.next();
/* 328 */       mimeType.appendTo(builder);
/* 329 */       if (iterator.hasNext()) {
/* 330 */         builder.append(", ");
/*     */       }
/*     */     } 
/* 333 */     return builder.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortBySpecificity(List<MimeType> mimeTypes) {
/* 361 */     Assert.notNull(mimeTypes, "'mimeTypes' must not be null");
/* 362 */     if (mimeTypes.size() > 1) {
/* 363 */       mimeTypes.sort(SPECIFICITY_COMPARATOR);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Random initRandom() {
/* 372 */     Random randomToUse = random;
/* 373 */     if (randomToUse == null) {
/* 374 */       synchronized (MimeTypeUtils.class) {
/* 375 */         randomToUse = random;
/* 376 */         if (randomToUse == null) {
/* 377 */           randomToUse = new SecureRandom();
/* 378 */           random = randomToUse;
/*     */         } 
/*     */       } 
/*     */     }
/* 382 */     return randomToUse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] generateMultipartBoundary() {
/* 389 */     Random randomToUse = initRandom();
/* 390 */     byte[] boundary = new byte[randomToUse.nextInt(11) + 30];
/* 391 */     for (int i = 0; i < boundary.length; i++) {
/* 392 */       boundary[i] = BOUNDARY_CHARS[randomToUse.nextInt(BOUNDARY_CHARS.length)];
/*     */     }
/* 394 */     return boundary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String generateMultipartBoundaryString() {
/* 401 */     return new String(generateMultipartBoundary(), StandardCharsets.US_ASCII);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\MimeTypeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */