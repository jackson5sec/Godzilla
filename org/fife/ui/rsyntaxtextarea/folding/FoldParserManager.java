/*     */ package org.fife.ui.rsyntaxtextarea.folding;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FoldParserManager
/*     */   implements SyntaxConstants
/*     */ {
/*  35 */   private static final FoldParserManager INSTANCE = new FoldParserManager();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private Map<String, FoldParser> foldParserMap = createFoldParserMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFoldParserMapping(String syntaxStyle, FoldParser parser) {
/*  59 */     this.foldParserMap.put(syntaxStyle, parser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, FoldParser> createFoldParserMap() {
/*  70 */     Map<String, FoldParser> map = new HashMap<>();
/*     */     
/*  72 */     map.put("text/actionscript", new CurlyFoldParser());
/*  73 */     map.put("text/asm6502", new LinesWithContentFoldParser());
/*  74 */     map.put("text/asm", new LinesWithContentFoldParser());
/*  75 */     map.put("text/c", new CurlyFoldParser());
/*  76 */     map.put("text/cpp", new CurlyFoldParser());
/*  77 */     map.put("text/cs", new CurlyFoldParser());
/*  78 */     map.put("text/clojure", new LispFoldParser());
/*  79 */     map.put("text/css", new CurlyFoldParser());
/*  80 */     map.put("text/d", new CurlyFoldParser());
/*  81 */     map.put("text/dart", new CurlyFoldParser());
/*  82 */     map.put("text/golang", new CurlyFoldParser());
/*  83 */     map.put("text/groovy", new CurlyFoldParser());
/*  84 */     map.put("text/htaccess", new XmlFoldParser());
/*  85 */     map.put("text/html", new HtmlFoldParser(-1));
/*  86 */     map.put("text/java", new CurlyFoldParser(true, true));
/*  87 */     map.put("text/javascript", new CurlyFoldParser());
/*  88 */     map.put("text/json", new JsonFoldParser());
/*  89 */     map.put("text/jshintrc", new JsonFoldParser());
/*  90 */     map.put("text/jsp", new HtmlFoldParser(1));
/*  91 */     map.put("text/kotlin", new CurlyFoldParser(true, true));
/*  92 */     map.put("text/latex", new LatexFoldParser());
/*  93 */     map.put("text/less", new CurlyFoldParser());
/*  94 */     map.put("text/lisp", new LispFoldParser());
/*  95 */     map.put("text/mxml", new XmlFoldParser());
/*  96 */     map.put("text/nsis", new NsisFoldParser());
/*  97 */     map.put("text/perl", new CurlyFoldParser());
/*  98 */     map.put("text/php", new HtmlFoldParser(0));
/*  99 */     map.put("text/python", new PythonFoldParser());
/* 100 */     map.put("text/scala", new CurlyFoldParser());
/* 101 */     map.put("text/typescript", new CurlyFoldParser());
/* 102 */     map.put("text/xml", new XmlFoldParser());
/* 103 */     map.put("text/yaml", new YamlFoldParser());
/*     */     
/* 105 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FoldParserManager get() {
/* 116 */     return INSTANCE;
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
/*     */   public FoldParser getFoldParser(String syntaxStyle) {
/* 130 */     return this.foldParserMap.get(syntaxStyle);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\FoldParserManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */