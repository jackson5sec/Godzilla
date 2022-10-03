/*     */ package com.jgoodies.forms.layout;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.util.LayoutStyle;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
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
/*     */ public final class LayoutMap
/*     */ {
/*     */   private static final char VARIABLE_PREFIX_CHAR = '$';
/* 131 */   private static final Map<String, String> COLUMN_ALIASES = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   private static final Map<String, String> ROW_ALIASES = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   private static LayoutMap root = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final LayoutMap parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, String> columnMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, String> columnMapCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, String> rowMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, String> rowMapCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LayoutMap() {
/* 189 */     this(getRoot());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LayoutMap(LayoutMap parent) {
/* 199 */     this.parent = parent;
/* 200 */     this.columnMap = new HashMap<String, String>();
/* 201 */     this.rowMap = new HashMap<String, String>();
/* 202 */     this.columnMapCache = new HashMap<String, String>();
/* 203 */     this.rowMapCache = new HashMap<String, String>();
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
/*     */   public static synchronized LayoutMap getRoot() {
/* 216 */     if (root == null) {
/* 217 */       root = createRoot();
/*     */     }
/* 219 */     return root;
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
/*     */   public boolean columnContainsKey(String key) {
/* 238 */     String resolvedKey = resolveColumnKey(key);
/* 239 */     return (this.columnMap.containsKey(resolvedKey) || (this.parent != null && this.parent.columnContainsKey(resolvedKey)));
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
/*     */   public String columnGet(String key) {
/* 259 */     String resolvedKey = resolveColumnKey(key);
/* 260 */     String cachedValue = this.columnMapCache.get(resolvedKey);
/* 261 */     if (cachedValue != null) {
/* 262 */       return cachedValue;
/*     */     }
/* 264 */     String value = this.columnMap.get(resolvedKey);
/* 265 */     if (value == null && this.parent != null) {
/* 266 */       value = this.parent.columnGet(resolvedKey);
/*     */     }
/* 268 */     if (value == null) {
/* 269 */       return null;
/*     */     }
/* 271 */     String expandedString = expand(value, true);
/* 272 */     this.columnMapCache.put(resolvedKey, expandedString);
/* 273 */     return expandedString;
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
/*     */   public String columnPut(String key, String value) {
/* 298 */     Preconditions.checkNotNull(value, "The column expression value must not be null.");
/* 299 */     String resolvedKey = resolveColumnKey(key);
/* 300 */     this.columnMapCache.clear();
/* 301 */     return this.columnMap.put(resolvedKey, value.toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String columnPut(String key, ColumnSpec value) {
/* 308 */     return columnPut(key, value.encode());
/*     */   }
/*     */ 
/*     */   
/*     */   public String columnPut(String key, Size value) {
/* 313 */     return columnPut(key, value.encode());
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
/*     */   public String columnRemove(String key) {
/* 335 */     String resolvedKey = resolveColumnKey(key);
/* 336 */     this.columnMapCache.clear();
/* 337 */     return this.columnMap.remove(resolvedKey);
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
/*     */   public boolean rowContainsKey(String key) {
/* 356 */     String resolvedKey = resolveRowKey(key);
/* 357 */     return (this.rowMap.containsKey(resolvedKey) || (this.parent != null && this.parent.rowContainsKey(resolvedKey)));
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
/*     */   public String rowGet(String key) {
/* 377 */     String resolvedKey = resolveRowKey(key);
/* 378 */     String cachedValue = this.rowMapCache.get(resolvedKey);
/* 379 */     if (cachedValue != null) {
/* 380 */       return cachedValue;
/*     */     }
/* 382 */     String value = this.rowMap.get(resolvedKey);
/* 383 */     if (value == null && this.parent != null) {
/* 384 */       value = this.parent.rowGet(resolvedKey);
/*     */     }
/* 386 */     if (value == null) {
/* 387 */       return null;
/*     */     }
/* 389 */     String expandedString = expand(value, false);
/* 390 */     this.rowMapCache.put(resolvedKey, expandedString);
/* 391 */     return expandedString;
/*     */   }
/*     */ 
/*     */   
/*     */   public String rowPut(String key, String value) {
/* 396 */     Preconditions.checkNotNull(value, "The row expression value must not be null.");
/* 397 */     String resolvedKey = resolveRowKey(key);
/* 398 */     this.rowMapCache.clear();
/* 399 */     return this.rowMap.put(resolvedKey, value.toLowerCase(Locale.ENGLISH));
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
/*     */   public String rowPut(String key, RowSpec value) {
/* 425 */     return rowPut(key, value.encode());
/*     */   }
/*     */ 
/*     */   
/*     */   public String rowPut(String key, Size value) {
/* 430 */     return rowPut(key, value.encode());
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
/*     */   public String rowRemove(String key) {
/* 452 */     String resolvedKey = resolveRowKey(key);
/* 453 */     this.rowMapCache.clear();
/* 454 */     return this.rowMap.remove(resolvedKey);
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
/*     */   public String toString() {
/* 468 */     StringBuffer buffer = new StringBuffer(super.toString());
/* 469 */     buffer.append("\n  Column associations:");
/* 470 */     for (Map.Entry<String, String> entry : this.columnMap.entrySet()) {
/* 471 */       buffer.append("\n    ");
/* 472 */       buffer.append(entry.getKey());
/* 473 */       buffer.append("->");
/* 474 */       buffer.append(entry.getValue());
/*     */     } 
/* 476 */     buffer.append("\n  Row associations:");
/* 477 */     for (Map.Entry<String, String> entry : this.rowMap.entrySet()) {
/* 478 */       buffer.append("\n    ");
/* 479 */       buffer.append(entry.getKey());
/* 480 */       buffer.append("->");
/* 481 */       buffer.append(entry.getValue());
/*     */     } 
/* 483 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String expand(String expression, boolean horizontal) {
/* 490 */     int cursor = 0;
/* 491 */     int start = expression.indexOf('$', cursor);
/* 492 */     if (start == -1) {
/* 493 */       return expression;
/*     */     }
/* 495 */     StringBuffer buffer = new StringBuffer();
/*     */     while (true) {
/* 497 */       buffer.append(expression.substring(cursor, start));
/* 498 */       String variableName = nextVariableName(expression, start);
/* 499 */       buffer.append(expansion(variableName, horizontal));
/* 500 */       cursor = start + variableName.length() + 1;
/* 501 */       start = expression.indexOf('$', cursor);
/* 502 */       if (start == -1) {
/* 503 */         buffer.append(expression.substring(cursor));
/* 504 */         return buffer.toString();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private static String nextVariableName(String expression, int start) {
/* 509 */     int length = expression.length();
/* 510 */     if (length <= start) {
/* 511 */       FormSpecParser.fail(expression, start, "Missing variable name after variable char '$'.");
/*     */     }
/* 513 */     if (expression.charAt(start + 1) == '{') {
/* 514 */       int i = expression.indexOf('}', start + 1);
/* 515 */       if (i == -1) {
/* 516 */         FormSpecParser.fail(expression, start, "Missing closing brace '}' for variable.");
/*     */       }
/* 518 */       return expression.substring(start + 1, i + 1);
/*     */     } 
/* 520 */     int end = start + 1;
/*     */     
/* 522 */     while (end < length && Character.isUnicodeIdentifierPart(expression.charAt(end))) {
/* 523 */       end++;
/*     */     }
/* 525 */     return expression.substring(start + 1, end);
/*     */   }
/*     */ 
/*     */   
/*     */   private String expansion(String variableName, boolean horizontal) {
/* 530 */     String key = stripBraces(variableName);
/* 531 */     String expansion = horizontal ? columnGet(key) : rowGet(key);
/* 532 */     if (expansion == null) {
/* 533 */       String orientation = horizontal ? "column" : "row";
/* 534 */       throw new IllegalArgumentException("Unknown " + orientation + " layout variable \"" + key + "\"");
/*     */     } 
/* 536 */     return expansion;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String stripBraces(String variableName) {
/* 541 */     return (variableName.charAt(0) == '{') ? variableName.substring(1, variableName.length() - 1) : variableName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String resolveColumnKey(String key) {
/* 550 */     Preconditions.checkNotNull(key, "The column key must not be null.");
/* 551 */     String lowercaseKey = key.toLowerCase(Locale.ENGLISH);
/* 552 */     String defaultKey = COLUMN_ALIASES.get(lowercaseKey);
/* 553 */     return (defaultKey == null) ? lowercaseKey : defaultKey;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String resolveRowKey(String key) {
/* 558 */     Preconditions.checkNotNull(key, "The row key must not be null.");
/* 559 */     String lowercaseKey = key.toLowerCase(Locale.ENGLISH);
/* 560 */     String defaultKey = ROW_ALIASES.get(lowercaseKey);
/* 561 */     return (defaultKey == null) ? lowercaseKey : defaultKey;
/*     */   }
/*     */ 
/*     */   
/*     */   private static LayoutMap createRoot() {
/* 566 */     LayoutMap map = new LayoutMap(null);
/*     */ 
/*     */     
/* 569 */     map.columnPut("label-component-gap", new String[] { "lcg", "lcgap" }, FormSpecs.LABEL_COMPONENT_GAP_COLSPEC);
/*     */ 
/*     */ 
/*     */     
/* 573 */     map.columnPut("related-gap", new String[] { "rg", "rgap" }, FormSpecs.RELATED_GAP_COLSPEC);
/*     */ 
/*     */ 
/*     */     
/* 577 */     map.columnPut("unrelated-gap", new String[] { "ug", "ugap" }, FormSpecs.UNRELATED_GAP_COLSPEC);
/*     */ 
/*     */ 
/*     */     
/* 581 */     map.columnPut("button", new String[] { "b" }, FormSpecs.BUTTON_COLSPEC);
/*     */ 
/*     */ 
/*     */     
/* 585 */     map.columnPut("growing-button", new String[] { "gb" }, FormSpecs.GROWING_BUTTON_COLSPEC);
/*     */ 
/*     */ 
/*     */     
/* 589 */     map.columnPut("dialog-margin", new String[] { "dm", "dmargin" }, ColumnSpec.createGap(LayoutStyle.getCurrent().getDialogMarginX()));
/*     */ 
/*     */ 
/*     */     
/* 593 */     map.columnPut("tabbed-dialog-margin", new String[] { "tdm", "tdmargin" }, ColumnSpec.createGap(LayoutStyle.getCurrent().getTabbedDialogMarginX()));
/*     */ 
/*     */ 
/*     */     
/* 597 */     map.columnPut("glue", FormSpecs.GLUE_COLSPEC.toShortString());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 602 */     map.rowPut("label-component-gap", new String[] { "lcg", "lcgap" }, FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC);
/*     */ 
/*     */ 
/*     */     
/* 606 */     map.rowPut("related-gap", new String[] { "rg", "rgap" }, FormSpecs.RELATED_GAP_ROWSPEC);
/*     */ 
/*     */ 
/*     */     
/* 610 */     map.rowPut("unrelated-gap", new String[] { "ug", "ugap" }, FormSpecs.UNRELATED_GAP_ROWSPEC);
/*     */ 
/*     */ 
/*     */     
/* 614 */     map.rowPut("narrow-line-gap", new String[] { "nlg", "nlgap" }, FormSpecs.NARROW_LINE_GAP_ROWSPEC);
/*     */ 
/*     */ 
/*     */     
/* 618 */     map.rowPut("line-gap", new String[] { "lg", "lgap" }, FormSpecs.LINE_GAP_ROWSPEC);
/*     */ 
/*     */ 
/*     */     
/* 622 */     map.rowPut("paragraph-gap", new String[] { "pg", "pgap" }, FormSpecs.PARAGRAPH_GAP_ROWSPEC);
/*     */ 
/*     */ 
/*     */     
/* 626 */     map.rowPut("dialog-margin", new String[] { "dm", "dmargin" }, RowSpec.createGap(LayoutStyle.getCurrent().getDialogMarginY()));
/*     */ 
/*     */ 
/*     */     
/* 630 */     map.rowPut("tabbed-dialog-margin", new String[] { "tdm", "tdmargin" }, RowSpec.createGap(LayoutStyle.getCurrent().getTabbedDialogMarginY()));
/*     */ 
/*     */ 
/*     */     
/* 634 */     map.rowPut("button", new String[] { "b" }, FormSpecs.BUTTON_ROWSPEC);
/*     */ 
/*     */ 
/*     */     
/* 638 */     map.rowPut("glue", FormSpecs.GLUE_ROWSPEC);
/*     */ 
/*     */ 
/*     */     
/* 642 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   private void columnPut(String key, String[] aliases, ColumnSpec value) {
/* 647 */     ensureLowerCase(key);
/* 648 */     columnPut(key, value);
/* 649 */     for (String aliase : aliases) {
/* 650 */       ensureLowerCase(aliase);
/* 651 */       COLUMN_ALIASES.put(aliase, key);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void rowPut(String key, String[] aliases, RowSpec value) {
/* 657 */     ensureLowerCase(key);
/* 658 */     rowPut(key, value);
/* 659 */     for (String aliase : aliases) {
/* 660 */       ensureLowerCase(aliase);
/* 661 */       ROW_ALIASES.put(aliase, key);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void ensureLowerCase(String str) {
/* 667 */     String lowerCase = str.toLowerCase(Locale.ENGLISH);
/* 668 */     if (!lowerCase.equals(str))
/* 669 */       throw new IllegalArgumentException("The string \"" + str + "\" should be lower case."); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\LayoutMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */