/*      */ package org.yaml.snakeyaml.emitter;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ArrayBlockingQueue;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.comments.CommentEventsCollector;
/*      */ import org.yaml.snakeyaml.comments.CommentLine;
/*      */ import org.yaml.snakeyaml.comments.CommentType;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.events.CollectionStartEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*      */ import org.yaml.snakeyaml.events.Event;
/*      */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*      */ import org.yaml.snakeyaml.events.NodeEvent;
/*      */ import org.yaml.snakeyaml.events.ScalarEvent;
/*      */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.scanner.Constant;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Emitter
/*      */   implements Emitable
/*      */ {
/*      */   public static final int MIN_INDENT = 1;
/*      */   public static final int MAX_INDENT = 10;
/*   74 */   private static final char[] SPACE = new char[] { ' ' };
/*      */   
/*   76 */   private static final Pattern SPACES_PATTERN = Pattern.compile("\\s");
/*   77 */   private static final Set<Character> INVALID_ANCHOR = new HashSet<>();
/*      */   static {
/*   79 */     INVALID_ANCHOR.add(Character.valueOf('['));
/*   80 */     INVALID_ANCHOR.add(Character.valueOf(']'));
/*   81 */     INVALID_ANCHOR.add(Character.valueOf('{'));
/*   82 */     INVALID_ANCHOR.add(Character.valueOf('}'));
/*   83 */     INVALID_ANCHOR.add(Character.valueOf(','));
/*   84 */     INVALID_ANCHOR.add(Character.valueOf('*'));
/*   85 */     INVALID_ANCHOR.add(Character.valueOf('&'));
/*      */   }
/*      */   
/*   88 */   private static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<>();
/*      */   static {
/*   90 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(false), "0");
/*   91 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\007'), "a");
/*   92 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\b'), "b");
/*   93 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\t'), "t");
/*   94 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\n'), "n");
/*   95 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\013'), "v");
/*   96 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\f'), "f");
/*   97 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\r'), "r");
/*   98 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\033'), "e");
/*   99 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*  100 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*  101 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(''), "N");
/*  102 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "_");
/*  103 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "L");
/*  104 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "P");
/*      */   }
/*      */   
/*  107 */   private static final Map<String, String> DEFAULT_TAG_PREFIXES = new LinkedHashMap<>();
/*      */   static {
/*  109 */     DEFAULT_TAG_PREFIXES.put("!", "!");
/*  110 */     DEFAULT_TAG_PREFIXES.put("tag:yaml.org,2002:", "!!");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final Writer stream;
/*      */ 
/*      */   
/*      */   private final ArrayStack<EmitterState> states;
/*      */   
/*      */   private EmitterState state;
/*      */   
/*      */   private final Queue<Event> events;
/*      */   
/*      */   private Event event;
/*      */   
/*      */   private final ArrayStack<Integer> indents;
/*      */   
/*      */   private Integer indent;
/*      */   
/*      */   private int flowLevel;
/*      */   
/*      */   private boolean rootContext;
/*      */   
/*      */   private boolean mappingContext;
/*      */   
/*      */   private boolean simpleKeyContext;
/*      */   
/*      */   private int column;
/*      */   
/*      */   private boolean whitespace;
/*      */   
/*      */   private boolean indention;
/*      */   
/*      */   private boolean openEnded;
/*      */   
/*      */   private final Boolean canonical;
/*      */   
/*      */   private final Boolean prettyFlow;
/*      */   
/*      */   private final boolean allowUnicode;
/*      */   
/*      */   private int bestIndent;
/*      */   
/*      */   private final int indicatorIndent;
/*      */   
/*      */   private final boolean indentWithIndicator;
/*      */   
/*      */   private int bestWidth;
/*      */   
/*      */   private final char[] bestLineBreak;
/*      */   
/*      */   private final boolean splitLines;
/*      */   
/*      */   private final int maxSimpleKeyLength;
/*      */   
/*      */   private final boolean emitComments;
/*      */   
/*      */   private Map<String, String> tagPrefixes;
/*      */   
/*      */   private String preparedAnchor;
/*      */   
/*      */   private String preparedTag;
/*      */   
/*      */   private ScalarAnalysis analysis;
/*      */   
/*      */   private DumperOptions.ScalarStyle style;
/*      */   
/*      */   private final CommentEventsCollector blockCommentsCollector;
/*      */   
/*      */   private final CommentEventsCollector inlineCommentsCollector;
/*      */ 
/*      */   
/*      */   public Emitter(Writer stream, DumperOptions opts) {
/*  184 */     this.stream = stream;
/*      */ 
/*      */     
/*  187 */     this.states = new ArrayStack(100);
/*  188 */     this.state = new ExpectStreamStart();
/*      */     
/*  190 */     this.events = new ArrayBlockingQueue<>(100);
/*  191 */     this.event = null;
/*      */     
/*  193 */     this.indents = new ArrayStack(10);
/*  194 */     this.indent = null;
/*      */     
/*  196 */     this.flowLevel = 0;
/*      */     
/*  198 */     this.mappingContext = false;
/*  199 */     this.simpleKeyContext = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  207 */     this.column = 0;
/*  208 */     this.whitespace = true;
/*  209 */     this.indention = true;
/*      */ 
/*      */     
/*  212 */     this.openEnded = false;
/*      */ 
/*      */     
/*  215 */     this.canonical = Boolean.valueOf(opts.isCanonical());
/*  216 */     this.prettyFlow = Boolean.valueOf(opts.isPrettyFlow());
/*  217 */     this.allowUnicode = opts.isAllowUnicode();
/*  218 */     this.bestIndent = 2;
/*  219 */     if (opts.getIndent() > 1 && opts.getIndent() < 10) {
/*  220 */       this.bestIndent = opts.getIndent();
/*      */     }
/*  222 */     this.indicatorIndent = opts.getIndicatorIndent();
/*  223 */     this.indentWithIndicator = opts.getIndentWithIndicator();
/*  224 */     this.bestWidth = 80;
/*  225 */     if (opts.getWidth() > this.bestIndent * 2) {
/*  226 */       this.bestWidth = opts.getWidth();
/*      */     }
/*  228 */     this.bestLineBreak = opts.getLineBreak().getString().toCharArray();
/*  229 */     this.splitLines = opts.getSplitLines();
/*  230 */     this.maxSimpleKeyLength = opts.getMaxSimpleKeyLength();
/*  231 */     this.emitComments = opts.isProcessComments();
/*      */ 
/*      */     
/*  234 */     this.tagPrefixes = new LinkedHashMap<>();
/*      */ 
/*      */     
/*  237 */     this.preparedAnchor = null;
/*  238 */     this.preparedTag = null;
/*      */ 
/*      */     
/*  241 */     this.analysis = null;
/*  242 */     this.style = null;
/*      */ 
/*      */     
/*  245 */     this.blockCommentsCollector = new CommentEventsCollector(this.events, new CommentType[] { CommentType.BLANK_LINE, CommentType.BLOCK });
/*      */     
/*  247 */     this.inlineCommentsCollector = new CommentEventsCollector(this.events, new CommentType[] { CommentType.IN_LINE });
/*      */   }
/*      */ 
/*      */   
/*      */   public void emit(Event event) throws IOException {
/*  252 */     this.events.add(event);
/*  253 */     while (!needMoreEvents()) {
/*  254 */       this.event = this.events.poll();
/*  255 */       this.state.expect();
/*  256 */       this.event = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreEvents() {
/*  263 */     if (this.events.isEmpty()) {
/*  264 */       return true;
/*      */     }
/*      */     
/*  267 */     Iterator<Event> iter = this.events.iterator();
/*  268 */     Event event = iter.next();
/*  269 */     while (event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  270 */       if (!iter.hasNext()) {
/*  271 */         return true;
/*      */       }
/*  273 */       event = iter.next();
/*      */     } 
/*      */     
/*  276 */     if (event instanceof DocumentStartEvent)
/*  277 */       return needEvents(iter, 1); 
/*  278 */     if (event instanceof SequenceStartEvent)
/*  279 */       return needEvents(iter, 2); 
/*  280 */     if (event instanceof MappingStartEvent)
/*  281 */       return needEvents(iter, 3); 
/*  282 */     if (event instanceof org.yaml.snakeyaml.events.StreamStartEvent)
/*  283 */       return needEvents(iter, 2); 
/*  284 */     if (event instanceof org.yaml.snakeyaml.events.StreamEndEvent)
/*  285 */       return false; 
/*  286 */     if (this.emitComments) {
/*  287 */       return needEvents(iter, 1);
/*      */     }
/*  289 */     return false;
/*      */   }
/*      */   
/*      */   private boolean needEvents(Iterator<Event> iter, int count) {
/*  293 */     int level = 0;
/*  294 */     int actualCount = 0;
/*  295 */     while (iter.hasNext()) {
/*  296 */       Event event = iter.next();
/*  297 */       if (event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*      */         continue;
/*      */       }
/*  300 */       actualCount++;
/*  301 */       if (event instanceof DocumentStartEvent || event instanceof CollectionStartEvent) {
/*  302 */         level++;
/*  303 */       } else if (event instanceof DocumentEndEvent || event instanceof org.yaml.snakeyaml.events.CollectionEndEvent) {
/*  304 */         level--;
/*  305 */       } else if (event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*  306 */         level = -1;
/*      */       } 
/*  308 */       if (level < 0) {
/*  309 */         return false;
/*      */       }
/*      */     } 
/*  312 */     return (actualCount < count);
/*      */   }
/*      */   
/*      */   private void increaseIndent(boolean flow, boolean indentless) {
/*  316 */     this.indents.push(this.indent);
/*  317 */     if (this.indent == null) {
/*  318 */       if (flow) {
/*  319 */         this.indent = Integer.valueOf(this.bestIndent);
/*      */       } else {
/*  321 */         this.indent = Integer.valueOf(0);
/*      */       } 
/*  323 */     } else if (!indentless) {
/*  324 */       Emitter emitter = this; emitter.indent = Integer.valueOf(emitter.indent.intValue() + this.bestIndent);
/*      */     } 
/*      */   }
/*      */   
/*      */   private class ExpectStreamStart
/*      */     implements EmitterState
/*      */   {
/*      */     private ExpectStreamStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  334 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamStartEvent) {
/*  335 */         Emitter.this.writeStreamStart();
/*  336 */         Emitter.this.state = new Emitter.ExpectFirstDocumentStart();
/*      */       } else {
/*  338 */         throw new EmitterException("expected StreamStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectNothing implements EmitterState {
/*      */     public void expect() throws IOException {
/*  345 */       throw new EmitterException("expecting nothing, but got " + Emitter.this.event);
/*      */     }
/*      */     
/*      */     private ExpectNothing() {} }
/*      */   
/*      */   private class ExpectFirstDocumentStart implements EmitterState { private ExpectFirstDocumentStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  353 */       (new Emitter.ExpectDocumentStart(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectDocumentStart implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectDocumentStart(boolean first) {
/*  361 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  365 */       if (Emitter.this.event instanceof DocumentStartEvent) {
/*  366 */         DocumentStartEvent ev = (DocumentStartEvent)Emitter.this.event;
/*  367 */         if ((ev.getVersion() != null || ev.getTags() != null) && Emitter.this.openEnded) {
/*  368 */           Emitter.this.writeIndicator("...", true, false, false);
/*  369 */           Emitter.this.writeIndent();
/*      */         } 
/*  371 */         if (ev.getVersion() != null) {
/*  372 */           String versionText = Emitter.this.prepareVersion(ev.getVersion());
/*  373 */           Emitter.this.writeVersionDirective(versionText);
/*      */         } 
/*  375 */         Emitter.this.tagPrefixes = (Map)new LinkedHashMap<>(Emitter.DEFAULT_TAG_PREFIXES);
/*  376 */         if (ev.getTags() != null) {
/*  377 */           Set<String> handles = new TreeSet<>(ev.getTags().keySet());
/*  378 */           for (String handle : handles) {
/*  379 */             String prefix = (String)ev.getTags().get(handle);
/*  380 */             Emitter.this.tagPrefixes.put(prefix, handle);
/*  381 */             String handleText = Emitter.this.prepareTagHandle(handle);
/*  382 */             String prefixText = Emitter.this.prepareTagPrefix(prefix);
/*  383 */             Emitter.this.writeTagDirective(handleText, prefixText);
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  389 */         boolean implicit = (this.first && !ev.getExplicit() && !Emitter.this.canonical.booleanValue() && ev.getVersion() == null && (ev.getTags() == null || ev.getTags().isEmpty()) && !Emitter.this.checkEmptyDocument());
/*  390 */         if (!implicit) {
/*  391 */           Emitter.this.writeIndent();
/*  392 */           Emitter.this.writeIndicator("---", true, false, false);
/*  393 */           if (Emitter.this.canonical.booleanValue()) {
/*  394 */             Emitter.this.writeIndent();
/*      */           }
/*      */         } 
/*  397 */         Emitter.this.state = new Emitter.ExpectDocumentRoot();
/*  398 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  404 */         Emitter.this.writeStreamEnd();
/*  405 */         Emitter.this.state = new Emitter.ExpectNothing();
/*  406 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  407 */         Emitter.this.blockCommentsCollector.collectEvents(Emitter.this.event);
/*  408 */         Emitter.this.writeBlockComment();
/*      */       } else {
/*      */         
/*  411 */         throw new EmitterException("expected DocumentStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentEnd implements EmitterState { private ExpectDocumentEnd() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  418 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  419 */       Emitter.this.writeBlockComment();
/*  420 */       if (Emitter.this.event instanceof DocumentEndEvent) {
/*  421 */         Emitter.this.writeIndent();
/*  422 */         if (((DocumentEndEvent)Emitter.this.event).getExplicit()) {
/*  423 */           Emitter.this.writeIndicator("...", true, false, false);
/*  424 */           Emitter.this.writeIndent();
/*      */         } 
/*  426 */         Emitter.this.flushStream();
/*  427 */         Emitter.this.state = new Emitter.ExpectDocumentStart(false);
/*      */       } else {
/*  429 */         throw new EmitterException("expected DocumentEndEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentRoot implements EmitterState { private ExpectDocumentRoot() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  436 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  437 */       if (!Emitter.this.blockCommentsCollector.isEmpty()) {
/*  438 */         Emitter.this.writeBlockComment();
/*  439 */         if (Emitter.this.event instanceof DocumentEndEvent) {
/*  440 */           (new Emitter.ExpectDocumentEnd()).expect();
/*      */           return;
/*      */         } 
/*      */       } 
/*  444 */       Emitter.this.states.push(new Emitter.ExpectDocumentEnd());
/*  445 */       Emitter.this.expectNode(true, false, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectNode(boolean root, boolean mapping, boolean simpleKey) throws IOException {
/*  452 */     this.rootContext = root;
/*  453 */     this.mappingContext = mapping;
/*  454 */     this.simpleKeyContext = simpleKey;
/*  455 */     if (this.event instanceof org.yaml.snakeyaml.events.AliasEvent) {
/*  456 */       expectAlias();
/*  457 */     } else if (this.event instanceof ScalarEvent || this.event instanceof CollectionStartEvent) {
/*  458 */       processAnchor("&");
/*  459 */       processTag();
/*  460 */       if (this.event instanceof ScalarEvent) {
/*  461 */         expectScalar();
/*  462 */       } else if (this.event instanceof SequenceStartEvent) {
/*  463 */         if (this.flowLevel != 0 || this.canonical.booleanValue() || ((SequenceStartEvent)this.event).isFlow() || 
/*  464 */           checkEmptySequence()) {
/*  465 */           expectFlowSequence();
/*      */         } else {
/*  467 */           expectBlockSequence();
/*      */         }
/*      */       
/*  470 */       } else if (this.flowLevel != 0 || this.canonical.booleanValue() || ((MappingStartEvent)this.event).isFlow() || 
/*  471 */         checkEmptyMapping()) {
/*  472 */         expectFlowMapping();
/*      */       } else {
/*  474 */         expectBlockMapping();
/*      */       } 
/*      */     } else {
/*      */       
/*  478 */       throw new EmitterException("expected NodeEvent, but got " + this.event);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void expectAlias() throws IOException {
/*  483 */     if (!(this.event instanceof org.yaml.snakeyaml.events.AliasEvent)) {
/*  484 */       throw new EmitterException("Alias must be provided");
/*      */     }
/*  486 */     processAnchor("*");
/*  487 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */   
/*      */   private void expectScalar() throws IOException {
/*  491 */     increaseIndent(true, false);
/*  492 */     processScalar();
/*  493 */     this.indent = (Integer)this.indents.pop();
/*  494 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowSequence() throws IOException {
/*  500 */     writeIndicator("[", true, true, false);
/*  501 */     this.flowLevel++;
/*  502 */     increaseIndent(true, false);
/*  503 */     if (this.prettyFlow.booleanValue()) {
/*  504 */       writeIndent();
/*      */     }
/*  506 */     this.state = new ExpectFirstFlowSequenceItem();
/*      */   }
/*      */   private class ExpectFirstFlowSequenceItem implements EmitterState { private ExpectFirstFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  511 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  512 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  513 */         Emitter.this.flowLevel--;
/*  514 */         Emitter.this.writeIndicator("]", false, false, false);
/*  515 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  516 */         Emitter.this.writeInlineComments();
/*  517 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*  518 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  519 */         Emitter.this.blockCommentsCollector.collectEvents(Emitter.this.event);
/*  520 */         Emitter.this.writeBlockComment();
/*      */       } else {
/*  522 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  523 */           Emitter.this.writeIndent();
/*      */         }
/*  525 */         Emitter.this.states.push(new Emitter.ExpectFlowSequenceItem());
/*  526 */         Emitter.this.expectNode(false, false, false);
/*  527 */         Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  528 */         Emitter.this.writeInlineComments();
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowSequenceItem implements EmitterState { private ExpectFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  535 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  536 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  537 */         Emitter.this.flowLevel--;
/*  538 */         if (Emitter.this.canonical.booleanValue()) {
/*  539 */           Emitter.this.writeIndicator(",", false, false, false);
/*  540 */           Emitter.this.writeIndent();
/*  541 */         } else if (Emitter.this.prettyFlow.booleanValue()) {
/*  542 */           Emitter.this.writeIndent();
/*      */         } 
/*  544 */         Emitter.this.writeIndicator("]", false, false, false);
/*  545 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  546 */         Emitter.this.writeInlineComments();
/*  547 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  548 */           Emitter.this.writeIndent();
/*      */         }
/*  550 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*  551 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  552 */         Emitter.this.event = Emitter.this.blockCommentsCollector.collectEvents(Emitter.this.event);
/*      */       } else {
/*  554 */         Emitter.this.writeIndicator(",", false, false, false);
/*  555 */         Emitter.this.writeBlockComment();
/*  556 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  557 */           Emitter.this.writeIndent();
/*      */         }
/*  559 */         Emitter.this.states.push(new ExpectFlowSequenceItem());
/*  560 */         Emitter.this.expectNode(false, false, false);
/*  561 */         Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  562 */         Emitter.this.writeInlineComments();
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowMapping() throws IOException {
/*  570 */     writeIndicator("{", true, true, false);
/*  571 */     this.flowLevel++;
/*  572 */     increaseIndent(true, false);
/*  573 */     if (this.prettyFlow.booleanValue()) {
/*  574 */       writeIndent();
/*      */     }
/*  576 */     this.state = new ExpectFirstFlowMappingKey();
/*      */   }
/*      */   private class ExpectFirstFlowMappingKey implements EmitterState { private ExpectFirstFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  581 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  582 */       Emitter.this.writeBlockComment();
/*  583 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  584 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  585 */         Emitter.this.flowLevel--;
/*  586 */         Emitter.this.writeIndicator("}", false, false, false);
/*  587 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  588 */         Emitter.this.writeInlineComments();
/*  589 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  591 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  592 */           Emitter.this.writeIndent();
/*      */         }
/*  594 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  595 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  596 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  598 */           Emitter.this.writeIndicator("?", true, false, false);
/*  599 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  600 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingKey implements EmitterState { private ExpectFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  608 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  609 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  610 */         Emitter.this.flowLevel--;
/*  611 */         if (Emitter.this.canonical.booleanValue()) {
/*  612 */           Emitter.this.writeIndicator(",", false, false, false);
/*  613 */           Emitter.this.writeIndent();
/*      */         } 
/*  615 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  616 */           Emitter.this.writeIndent();
/*      */         }
/*  618 */         Emitter.this.writeIndicator("}", false, false, false);
/*  619 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  620 */         Emitter.this.writeInlineComments();
/*  621 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  623 */         Emitter.this.writeIndicator(",", false, false, false);
/*  624 */         Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  625 */         Emitter.this.writeBlockComment();
/*  626 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  627 */           Emitter.this.writeIndent();
/*      */         }
/*  629 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  630 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  631 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  633 */           Emitter.this.writeIndicator("?", true, false, false);
/*  634 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  635 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingSimpleValue implements EmitterState { private ExpectFlowMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  643 */       Emitter.this.writeIndicator(":", false, false, false);
/*  644 */       Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  645 */       Emitter.this.writeInlineComments();
/*  646 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  647 */       Emitter.this.expectNode(false, true, false);
/*  648 */       Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  649 */       Emitter.this.writeInlineComments();
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingValue implements EmitterState { private ExpectFlowMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  655 */       if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  656 */         Emitter.this.writeIndent();
/*      */       }
/*  658 */       Emitter.this.writeIndicator(":", true, false, false);
/*  659 */       Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  660 */       Emitter.this.writeInlineComments();
/*  661 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  662 */       Emitter.this.expectNode(false, true, false);
/*  663 */       Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  664 */       Emitter.this.writeInlineComments();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectBlockSequence() throws IOException {
/*  671 */     boolean indentless = (this.mappingContext && !this.indention);
/*  672 */     increaseIndent(false, indentless);
/*  673 */     this.state = new ExpectFirstBlockSequenceItem();
/*      */   }
/*      */   private class ExpectFirstBlockSequenceItem implements EmitterState { private ExpectFirstBlockSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  678 */       (new Emitter.ExpectBlockSequenceItem(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockSequenceItem implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectBlockSequenceItem(boolean first) {
/*  686 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  690 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  691 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  692 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*  693 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.CommentEvent) {
/*  694 */         Emitter.this.blockCommentsCollector.collectEvents(Emitter.this.event);
/*      */       } else {
/*  696 */         Emitter.this.writeIndent();
/*  697 */         if (!Emitter.this.indentWithIndicator || this.first) {
/*  698 */           Emitter.this.writeWhitespace(Emitter.this.indicatorIndent);
/*      */         }
/*  700 */         Emitter.this.writeIndicator("-", true, false, true);
/*  701 */         if (Emitter.this.indentWithIndicator && this.first) {
/*  702 */           Emitter.this.indent = Integer.valueOf(Emitter.this.indent.intValue() + Emitter.this.indicatorIndent);
/*      */         }
/*  704 */         if (!Emitter.this.blockCommentsCollector.isEmpty()) {
/*  705 */           Emitter.this.increaseIndent(false, false);
/*  706 */           Emitter.this.writeBlockComment();
/*  707 */           if (Emitter.this.event instanceof ScalarEvent) {
/*  708 */             Emitter.this.analysis = Emitter.this.analyzeScalar(((ScalarEvent)Emitter.this.event).getValue());
/*  709 */             if (!Emitter.this.analysis.isEmpty()) {
/*  710 */               Emitter.this.writeIndent();
/*      */             }
/*      */           } 
/*  713 */           Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*      */         } 
/*  715 */         Emitter.this.states.push(new ExpectBlockSequenceItem(false));
/*  716 */         Emitter.this.expectNode(false, false, false);
/*  717 */         Emitter.this.inlineCommentsCollector.collectEvents();
/*  718 */         Emitter.this.writeInlineComments();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void expectBlockMapping() throws IOException {
/*  725 */     increaseIndent(false, false);
/*  726 */     this.state = new ExpectFirstBlockMappingKey();
/*      */   }
/*      */   private class ExpectFirstBlockMappingKey implements EmitterState { private ExpectFirstBlockMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  731 */       (new Emitter.ExpectBlockMappingKey(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockMappingKey implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectBlockMappingKey(boolean first) {
/*  739 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  743 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  744 */       Emitter.this.writeBlockComment();
/*  745 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  746 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  747 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  749 */         Emitter.this.writeIndent();
/*  750 */         if (Emitter.this.checkSimpleKey()) {
/*  751 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingSimpleValue());
/*  752 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  754 */           Emitter.this.writeIndicator("?", true, false, true);
/*  755 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingValue());
/*  756 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isFoldedOrLiteral(Event event) {
/*  763 */     if (!event.is(Event.ID.Scalar)) {
/*  764 */       return false;
/*      */     }
/*  766 */     ScalarEvent scalarEvent = (ScalarEvent)event;
/*  767 */     DumperOptions.ScalarStyle style = scalarEvent.getScalarStyle();
/*  768 */     return (style == DumperOptions.ScalarStyle.FOLDED || style == DumperOptions.ScalarStyle.LITERAL);
/*      */   }
/*      */   private class ExpectBlockMappingSimpleValue implements EmitterState { private ExpectBlockMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  773 */       Emitter.this.writeIndicator(":", false, false, false);
/*  774 */       Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  775 */       if (!Emitter.this.isFoldedOrLiteral(Emitter.this.event) && 
/*  776 */         Emitter.this.writeInlineComments()) {
/*  777 */         Emitter.this.increaseIndent(true, false);
/*  778 */         Emitter.this.writeIndent();
/*  779 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*      */       } 
/*      */       
/*  782 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  783 */       if (!Emitter.this.blockCommentsCollector.isEmpty()) {
/*  784 */         Emitter.this.increaseIndent(true, false);
/*  785 */         Emitter.this.writeBlockComment();
/*  786 */         Emitter.this.writeIndent();
/*  787 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*      */       } 
/*  789 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  790 */       Emitter.this.expectNode(false, true, false);
/*  791 */       Emitter.this.inlineCommentsCollector.collectEvents();
/*  792 */       Emitter.this.writeInlineComments();
/*      */     } }
/*      */   
/*      */   private class ExpectBlockMappingValue implements EmitterState { private ExpectBlockMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  798 */       Emitter.this.writeIndent();
/*  799 */       Emitter.this.writeIndicator(":", true, false, true);
/*  800 */       Emitter.this.event = Emitter.this.inlineCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  801 */       Emitter.this.writeInlineComments();
/*  802 */       Emitter.this.event = Emitter.this.blockCommentsCollector.collectEventsAndPoll(Emitter.this.event);
/*  803 */       Emitter.this.writeBlockComment();
/*  804 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  805 */       Emitter.this.expectNode(false, true, false);
/*  806 */       Emitter.this.inlineCommentsCollector.collectEvents(Emitter.this.event);
/*  807 */       Emitter.this.writeInlineComments();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkEmptySequence() {
/*  814 */     return (this.event instanceof SequenceStartEvent && !this.events.isEmpty() && this.events.peek() instanceof org.yaml.snakeyaml.events.SequenceEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyMapping() {
/*  818 */     return (this.event instanceof MappingStartEvent && !this.events.isEmpty() && this.events.peek() instanceof org.yaml.snakeyaml.events.MappingEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyDocument() {
/*  822 */     if (!(this.event instanceof DocumentStartEvent) || this.events.isEmpty()) {
/*  823 */       return false;
/*      */     }
/*  825 */     Event event = this.events.peek();
/*  826 */     if (event instanceof ScalarEvent) {
/*  827 */       ScalarEvent e = (ScalarEvent)event;
/*  828 */       return (e.getAnchor() == null && e.getTag() == null && e.getImplicit() != null && e
/*  829 */         .getValue().length() == 0);
/*      */     } 
/*  831 */     return false;
/*      */   }
/*      */   
/*      */   private boolean checkSimpleKey() {
/*  835 */     int length = 0;
/*  836 */     if (this.event instanceof NodeEvent && ((NodeEvent)this.event).getAnchor() != null) {
/*  837 */       if (this.preparedAnchor == null) {
/*  838 */         this.preparedAnchor = prepareAnchor(((NodeEvent)this.event).getAnchor());
/*      */       }
/*  840 */       length += this.preparedAnchor.length();
/*      */     } 
/*  842 */     String tag = null;
/*  843 */     if (this.event instanceof ScalarEvent) {
/*  844 */       tag = ((ScalarEvent)this.event).getTag();
/*  845 */     } else if (this.event instanceof CollectionStartEvent) {
/*  846 */       tag = ((CollectionStartEvent)this.event).getTag();
/*      */     } 
/*  848 */     if (tag != null) {
/*  849 */       if (this.preparedTag == null) {
/*  850 */         this.preparedTag = prepareTag(tag);
/*      */       }
/*  852 */       length += this.preparedTag.length();
/*      */     } 
/*  854 */     if (this.event instanceof ScalarEvent) {
/*  855 */       if (this.analysis == null) {
/*  856 */         this.analysis = analyzeScalar(((ScalarEvent)this.event).getValue());
/*      */       }
/*  858 */       length += this.analysis.getScalar().length();
/*      */     } 
/*  860 */     return (length < this.maxSimpleKeyLength && (this.event instanceof org.yaml.snakeyaml.events.AliasEvent || (this.event instanceof ScalarEvent && 
/*  861 */       !this.analysis.isEmpty() && !this.analysis.isMultiline()) || 
/*  862 */       checkEmptySequence() || checkEmptyMapping()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void processAnchor(String indicator) throws IOException {
/*  868 */     NodeEvent ev = (NodeEvent)this.event;
/*  869 */     if (ev.getAnchor() == null) {
/*  870 */       this.preparedAnchor = null;
/*      */       return;
/*      */     } 
/*  873 */     if (this.preparedAnchor == null) {
/*  874 */       this.preparedAnchor = prepareAnchor(ev.getAnchor());
/*      */     }
/*  876 */     writeIndicator(indicator + this.preparedAnchor, true, false, false);
/*  877 */     this.preparedAnchor = null;
/*      */   }
/*      */   
/*      */   private void processTag() throws IOException {
/*  881 */     String tag = null;
/*  882 */     if (this.event instanceof ScalarEvent) {
/*  883 */       ScalarEvent ev = (ScalarEvent)this.event;
/*  884 */       tag = ev.getTag();
/*  885 */       if (this.style == null) {
/*  886 */         this.style = chooseScalarStyle();
/*      */       }
/*  888 */       if ((!this.canonical.booleanValue() || tag == null) && ((this.style == null && ev.getImplicit()
/*  889 */         .canOmitTagInPlainScalar()) || (this.style != null && ev.getImplicit()
/*  890 */         .canOmitTagInNonPlainScalar()))) {
/*  891 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*  894 */       if (ev.getImplicit().canOmitTagInPlainScalar() && tag == null) {
/*  895 */         tag = "!";
/*  896 */         this.preparedTag = null;
/*      */       } 
/*      */     } else {
/*  899 */       CollectionStartEvent ev = (CollectionStartEvent)this.event;
/*  900 */       tag = ev.getTag();
/*  901 */       if ((!this.canonical.booleanValue() || tag == null) && ev.getImplicit()) {
/*  902 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*      */     } 
/*  906 */     if (tag == null) {
/*  907 */       throw new EmitterException("tag is not specified");
/*      */     }
/*  909 */     if (this.preparedTag == null) {
/*  910 */       this.preparedTag = prepareTag(tag);
/*      */     }
/*  912 */     writeIndicator(this.preparedTag, true, false, false);
/*  913 */     this.preparedTag = null;
/*      */   }
/*      */   
/*      */   private DumperOptions.ScalarStyle chooseScalarStyle() {
/*  917 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  918 */     if (this.analysis == null) {
/*  919 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  921 */     if ((!ev.isPlain() && ev.getScalarStyle() == DumperOptions.ScalarStyle.DOUBLE_QUOTED) || this.canonical.booleanValue()) {
/*  922 */       return DumperOptions.ScalarStyle.DOUBLE_QUOTED;
/*      */     }
/*  924 */     if (ev.isPlain() && ev.getImplicit().canOmitTagInPlainScalar() && (
/*  925 */       !this.simpleKeyContext || (!this.analysis.isEmpty() && !this.analysis.isMultiline())) && ((this.flowLevel != 0 && this.analysis
/*  926 */       .isAllowFlowPlain()) || (this.flowLevel == 0 && this.analysis.isAllowBlockPlain()))) {
/*  927 */       return null;
/*      */     }
/*      */     
/*  930 */     if (!ev.isPlain() && (ev.getScalarStyle() == DumperOptions.ScalarStyle.LITERAL || ev.getScalarStyle() == DumperOptions.ScalarStyle.FOLDED) && 
/*  931 */       this.flowLevel == 0 && !this.simpleKeyContext && this.analysis.isAllowBlock()) {
/*  932 */       return ev.getScalarStyle();
/*      */     }
/*      */     
/*  935 */     if ((ev.isPlain() || ev.getScalarStyle() == DumperOptions.ScalarStyle.SINGLE_QUOTED) && 
/*  936 */       this.analysis.isAllowSingleQuoted() && (!this.simpleKeyContext || !this.analysis.isMultiline())) {
/*  937 */       return DumperOptions.ScalarStyle.SINGLE_QUOTED;
/*      */     }
/*      */     
/*  940 */     return DumperOptions.ScalarStyle.DOUBLE_QUOTED;
/*      */   }
/*      */   
/*      */   private void processScalar() throws IOException {
/*  944 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  945 */     if (this.analysis == null) {
/*  946 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  948 */     if (this.style == null) {
/*  949 */       this.style = chooseScalarStyle();
/*      */     }
/*  951 */     boolean split = (!this.simpleKeyContext && this.splitLines);
/*  952 */     if (this.style == null) {
/*  953 */       writePlain(this.analysis.getScalar(), split);
/*      */     } else {
/*  955 */       switch (this.style) {
/*      */         case DOUBLE_QUOTED:
/*  957 */           writeDoubleQuoted(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case SINGLE_QUOTED:
/*  960 */           writeSingleQuoted(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case FOLDED:
/*  963 */           writeFolded(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case LITERAL:
/*  966 */           writeLiteral(this.analysis.getScalar());
/*      */           break;
/*      */         default:
/*  969 */           throw new YAMLException("Unexpected style: " + this.style);
/*      */       } 
/*      */     } 
/*  972 */     this.analysis = null;
/*  973 */     this.style = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String prepareVersion(DumperOptions.Version version) {
/*  979 */     if (version.major() != 1) {
/*  980 */       throw new EmitterException("unsupported YAML version: " + version);
/*      */     }
/*  982 */     return version.getRepresentation();
/*      */   }
/*      */   
/*  985 */   private static final Pattern HANDLE_FORMAT = Pattern.compile("^![-_\\w]*!$");
/*      */   
/*      */   private String prepareTagHandle(String handle) {
/*  988 */     if (handle.length() == 0)
/*  989 */       throw new EmitterException("tag handle must not be empty"); 
/*  990 */     if (handle.charAt(0) != '!' || handle.charAt(handle.length() - 1) != '!')
/*  991 */       throw new EmitterException("tag handle must start and end with '!': " + handle); 
/*  992 */     if (!"!".equals(handle) && !HANDLE_FORMAT.matcher(handle).matches()) {
/*  993 */       throw new EmitterException("invalid character in the tag handle: " + handle);
/*      */     }
/*  995 */     return handle;
/*      */   }
/*      */   
/*      */   private String prepareTagPrefix(String prefix) {
/*  999 */     if (prefix.length() == 0) {
/* 1000 */       throw new EmitterException("tag prefix must not be empty");
/*      */     }
/* 1002 */     StringBuilder chunks = new StringBuilder();
/* 1003 */     int start = 0;
/* 1004 */     int end = 0;
/* 1005 */     if (prefix.charAt(0) == '!') {
/* 1006 */       end = 1;
/*      */     }
/* 1008 */     while (end < prefix.length()) {
/* 1009 */       end++;
/*      */     }
/* 1011 */     if (start < end) {
/* 1012 */       chunks.append(prefix, start, end);
/*      */     }
/* 1014 */     return chunks.toString();
/*      */   }
/*      */   
/*      */   private String prepareTag(String tag) {
/* 1018 */     if (tag.length() == 0) {
/* 1019 */       throw new EmitterException("tag must not be empty");
/*      */     }
/* 1021 */     if ("!".equals(tag)) {
/* 1022 */       return tag;
/*      */     }
/* 1024 */     String handle = null;
/* 1025 */     String suffix = tag;
/*      */     
/* 1027 */     for (String prefix : this.tagPrefixes.keySet()) {
/* 1028 */       if (tag.startsWith(prefix) && ("!".equals(prefix) || prefix.length() < tag.length())) {
/* 1029 */         handle = prefix;
/*      */       }
/*      */     } 
/* 1032 */     if (handle != null) {
/* 1033 */       suffix = tag.substring(handle.length());
/* 1034 */       handle = this.tagPrefixes.get(handle);
/*      */     } 
/*      */     
/* 1037 */     int end = suffix.length();
/* 1038 */     String suffixText = (end > 0) ? suffix.substring(0, end) : "";
/*      */     
/* 1040 */     if (handle != null) {
/* 1041 */       return handle + suffixText;
/*      */     }
/* 1043 */     return "!<" + suffixText + ">";
/*      */   }
/*      */   
/*      */   static String prepareAnchor(String anchor) {
/* 1047 */     if (anchor.length() == 0) {
/* 1048 */       throw new EmitterException("anchor must not be empty");
/*      */     }
/* 1050 */     for (Character invalid : INVALID_ANCHOR) {
/* 1051 */       if (anchor.indexOf(invalid.charValue()) > -1) {
/* 1052 */         throw new EmitterException("Invalid character '" + invalid + "' in the anchor: " + anchor);
/*      */       }
/*      */     } 
/* 1055 */     Matcher matcher = SPACES_PATTERN.matcher(anchor);
/* 1056 */     if (matcher.find()) {
/* 1057 */       throw new EmitterException("Anchor may not contain spaces: " + anchor);
/*      */     }
/* 1059 */     return anchor;
/*      */   }
/*      */ 
/*      */   
/*      */   private ScalarAnalysis analyzeScalar(String scalar) {
/* 1064 */     if (scalar.length() == 0) {
/* 1065 */       return new ScalarAnalysis(scalar, true, false, false, true, true, false);
/*      */     }
/*      */     
/* 1068 */     boolean blockIndicators = false;
/* 1069 */     boolean flowIndicators = false;
/* 1070 */     boolean lineBreaks = false;
/* 1071 */     boolean specialCharacters = false;
/*      */ 
/*      */     
/* 1074 */     boolean leadingSpace = false;
/* 1075 */     boolean leadingBreak = false;
/* 1076 */     boolean trailingSpace = false;
/* 1077 */     boolean trailingBreak = false;
/* 1078 */     boolean breakSpace = false;
/* 1079 */     boolean spaceBreak = false;
/*      */ 
/*      */     
/* 1082 */     if (scalar.startsWith("---") || scalar.startsWith("...")) {
/* 1083 */       blockIndicators = true;
/* 1084 */       flowIndicators = true;
/*      */     } 
/*      */     
/* 1087 */     boolean preceededByWhitespace = true;
/* 1088 */     boolean followedByWhitespace = (scalar.length() == 1 || Constant.NULL_BL_T_LINEBR.has(scalar.codePointAt(1)));
/*      */     
/* 1090 */     boolean previousSpace = false;
/*      */ 
/*      */     
/* 1093 */     boolean previousBreak = false;
/*      */     
/* 1095 */     int index = 0;
/*      */     
/* 1097 */     while (index < scalar.length()) {
/* 1098 */       int c = scalar.codePointAt(index);
/*      */       
/* 1100 */       if (index == 0) {
/*      */         
/* 1102 */         if ("#,[]{}&*!|>'\"%@`".indexOf(c) != -1) {
/* 1103 */           flowIndicators = true;
/* 1104 */           blockIndicators = true;
/*      */         } 
/* 1106 */         if (c == 63 || c == 58) {
/* 1107 */           flowIndicators = true;
/* 1108 */           if (followedByWhitespace) {
/* 1109 */             blockIndicators = true;
/*      */           }
/*      */         } 
/* 1112 */         if (c == 45 && followedByWhitespace) {
/* 1113 */           flowIndicators = true;
/* 1114 */           blockIndicators = true;
/*      */         } 
/*      */       } else {
/*      */         
/* 1118 */         if (",?[]{}".indexOf(c) != -1) {
/* 1119 */           flowIndicators = true;
/*      */         }
/* 1121 */         if (c == 58) {
/* 1122 */           flowIndicators = true;
/* 1123 */           if (followedByWhitespace) {
/* 1124 */             blockIndicators = true;
/*      */           }
/*      */         } 
/* 1127 */         if (c == 35 && preceededByWhitespace) {
/* 1128 */           flowIndicators = true;
/* 1129 */           blockIndicators = true;
/*      */         } 
/*      */       } 
/*      */       
/* 1133 */       boolean isLineBreak = Constant.LINEBR.has(c);
/* 1134 */       if (isLineBreak) {
/* 1135 */         lineBreaks = true;
/*      */       }
/* 1137 */       if (c != 10 && (32 > c || c > 126)) {
/* 1138 */         if (c == 133 || (c >= 160 && c <= 55295) || (c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111)) {
/*      */ 
/*      */ 
/*      */           
/* 1142 */           if (!this.allowUnicode) {
/* 1143 */             specialCharacters = true;
/*      */           }
/*      */         } else {
/* 1146 */           specialCharacters = true;
/*      */         } 
/*      */       }
/*      */       
/* 1150 */       if (c == 32) {
/* 1151 */         if (index == 0) {
/* 1152 */           leadingSpace = true;
/*      */         }
/* 1154 */         if (index == scalar.length() - 1) {
/* 1155 */           trailingSpace = true;
/*      */         }
/* 1157 */         if (previousBreak) {
/* 1158 */           breakSpace = true;
/*      */         }
/* 1160 */         previousSpace = true;
/* 1161 */         previousBreak = false;
/* 1162 */       } else if (isLineBreak) {
/* 1163 */         if (index == 0) {
/* 1164 */           leadingBreak = true;
/*      */         }
/* 1166 */         if (index == scalar.length() - 1) {
/* 1167 */           trailingBreak = true;
/*      */         }
/* 1169 */         if (previousSpace) {
/* 1170 */           spaceBreak = true;
/*      */         }
/* 1172 */         previousSpace = false;
/* 1173 */         previousBreak = true;
/*      */       } else {
/* 1175 */         previousSpace = false;
/* 1176 */         previousBreak = false;
/*      */       } 
/*      */ 
/*      */       
/* 1180 */       index += Character.charCount(c);
/* 1181 */       preceededByWhitespace = (Constant.NULL_BL_T.has(c) || isLineBreak);
/* 1182 */       followedByWhitespace = true;
/* 1183 */       if (index + 1 < scalar.length()) {
/* 1184 */         int nextIndex = index + Character.charCount(scalar.codePointAt(index));
/* 1185 */         if (nextIndex < scalar.length()) {
/* 1186 */           followedByWhitespace = (Constant.NULL_BL_T.has(scalar.codePointAt(nextIndex)) || isLineBreak);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1191 */     boolean allowFlowPlain = true;
/* 1192 */     boolean allowBlockPlain = true;
/* 1193 */     boolean allowSingleQuoted = true;
/* 1194 */     boolean allowBlock = true;
/*      */     
/* 1196 */     if (leadingSpace || leadingBreak || trailingSpace || trailingBreak) {
/* 1197 */       allowFlowPlain = allowBlockPlain = false;
/*      */     }
/*      */     
/* 1200 */     if (trailingSpace) {
/* 1201 */       allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1205 */     if (breakSpace) {
/* 1206 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = false;
/*      */     }
/*      */ 
/*      */     
/* 1210 */     if (spaceBreak || specialCharacters) {
/* 1211 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1215 */     if (lineBreaks) {
/* 1216 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1219 */     if (flowIndicators) {
/* 1220 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1223 */     if (blockIndicators) {
/* 1224 */       allowBlockPlain = false;
/*      */     }
/*      */     
/* 1227 */     return new ScalarAnalysis(scalar, false, lineBreaks, allowFlowPlain, allowBlockPlain, allowSingleQuoted, allowBlock);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void flushStream() throws IOException {
/* 1234 */     this.stream.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeStreamStart() {}
/*      */ 
/*      */   
/*      */   void writeStreamEnd() throws IOException {
/* 1242 */     flushStream();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeIndicator(String indicator, boolean needWhitespace, boolean whitespace, boolean indentation) throws IOException {
/* 1247 */     if (!this.whitespace && needWhitespace) {
/* 1248 */       this.column++;
/* 1249 */       this.stream.write(SPACE);
/*      */     } 
/* 1251 */     this.whitespace = whitespace;
/* 1252 */     this.indention = (this.indention && indentation);
/* 1253 */     this.column += indicator.length();
/* 1254 */     this.openEnded = false;
/* 1255 */     this.stream.write(indicator);
/*      */   }
/*      */   
/*      */   void writeIndent() throws IOException {
/*      */     int indent;
/* 1260 */     if (this.indent != null) {
/* 1261 */       indent = this.indent.intValue();
/*      */     } else {
/* 1263 */       indent = 0;
/*      */     } 
/*      */     
/* 1266 */     if (!this.indention || this.column > indent || (this.column == indent && !this.whitespace)) {
/* 1267 */       writeLineBreak(null);
/*      */     }
/*      */     
/* 1270 */     writeWhitespace(indent - this.column);
/*      */   }
/*      */   
/*      */   private void writeWhitespace(int length) throws IOException {
/* 1274 */     if (length <= 0) {
/*      */       return;
/*      */     }
/* 1277 */     this.whitespace = true;
/* 1278 */     char[] data = new char[length];
/* 1279 */     for (int i = 0; i < data.length; i++) {
/* 1280 */       data[i] = ' ';
/*      */     }
/* 1282 */     this.column += length;
/* 1283 */     this.stream.write(data);
/*      */   }
/*      */   
/*      */   private void writeLineBreak(String data) throws IOException {
/* 1287 */     this.whitespace = true;
/* 1288 */     this.indention = true;
/* 1289 */     this.column = 0;
/* 1290 */     if (data == null) {
/* 1291 */       this.stream.write(this.bestLineBreak);
/*      */     } else {
/* 1293 */       this.stream.write(data);
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeVersionDirective(String versionText) throws IOException {
/* 1298 */     this.stream.write("%YAML ");
/* 1299 */     this.stream.write(versionText);
/* 1300 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void writeTagDirective(String handleText, String prefixText) throws IOException {
/* 1306 */     this.stream.write("%TAG ");
/* 1307 */     this.stream.write(handleText);
/* 1308 */     this.stream.write(SPACE);
/* 1309 */     this.stream.write(prefixText);
/* 1310 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */   
/*      */   private void writeSingleQuoted(String text, boolean split) throws IOException {
/* 1315 */     writeIndicator("'", true, false, false);
/* 1316 */     boolean spaces = false;
/* 1317 */     boolean breaks = false;
/* 1318 */     int start = 0, end = 0;
/*      */     
/* 1320 */     while (end <= text.length()) {
/* 1321 */       char ch = Character.MIN_VALUE;
/* 1322 */       if (end < text.length()) {
/* 1323 */         ch = text.charAt(end);
/*      */       }
/* 1325 */       if (spaces) {
/* 1326 */         if (ch == '\000' || ch != ' ') {
/* 1327 */           if (start + 1 == end && this.column > this.bestWidth && split && start != 0 && end != text
/* 1328 */             .length()) {
/* 1329 */             writeIndent();
/*      */           } else {
/* 1331 */             int len = end - start;
/* 1332 */             this.column += len;
/* 1333 */             this.stream.write(text, start, len);
/*      */           } 
/* 1335 */           start = end;
/*      */         } 
/* 1337 */       } else if (breaks) {
/* 1338 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1339 */           if (text.charAt(start) == '\n') {
/* 1340 */             writeLineBreak(null);
/*      */           }
/* 1342 */           String data = text.substring(start, end);
/* 1343 */           for (char br : data.toCharArray()) {
/* 1344 */             if (br == '\n') {
/* 1345 */               writeLineBreak(null);
/*      */             } else {
/* 1347 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1350 */           writeIndent();
/* 1351 */           start = end;
/*      */         }
/*      */       
/* 1354 */       } else if (Constant.LINEBR.has(ch, "\000 '") && 
/* 1355 */         start < end) {
/* 1356 */         int len = end - start;
/* 1357 */         this.column += len;
/* 1358 */         this.stream.write(text, start, len);
/* 1359 */         start = end;
/*      */       } 
/*      */ 
/*      */       
/* 1363 */       if (ch == '\'') {
/* 1364 */         this.column += 2;
/* 1365 */         this.stream.write("''");
/* 1366 */         start = end + 1;
/*      */       } 
/* 1368 */       if (ch != '\000') {
/* 1369 */         spaces = (ch == ' ');
/* 1370 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1372 */       end++;
/*      */     } 
/* 1374 */     writeIndicator("'", false, false, false);
/*      */   }
/*      */   
/*      */   private void writeDoubleQuoted(String text, boolean split) throws IOException {
/* 1378 */     writeIndicator("\"", true, false, false);
/* 1379 */     int start = 0;
/* 1380 */     int end = 0;
/* 1381 */     while (end <= text.length()) {
/* 1382 */       Character ch = null;
/* 1383 */       if (end < text.length()) {
/* 1384 */         ch = Character.valueOf(text.charAt(end));
/*      */       }
/* 1386 */       if (ch == null || "\"\\  ﻿".indexOf(ch.charValue()) != -1 || ' ' > ch
/* 1387 */         .charValue() || ch.charValue() > '~') {
/* 1388 */         if (start < end) {
/* 1389 */           int len = end - start;
/* 1390 */           this.column += len;
/* 1391 */           this.stream.write(text, start, len);
/* 1392 */           start = end;
/*      */         } 
/* 1394 */         if (ch != null) {
/*      */           String data;
/* 1396 */           if (ESCAPE_REPLACEMENTS.containsKey(ch)) {
/* 1397 */             data = "\\" + (String)ESCAPE_REPLACEMENTS.get(ch);
/* 1398 */           } else if (!this.allowUnicode || !StreamReader.isPrintable(ch.charValue())) {
/*      */ 
/*      */             
/* 1401 */             if (ch.charValue() <= 'ÿ') {
/* 1402 */               String s = "0" + Integer.toString(ch.charValue(), 16);
/* 1403 */               data = "\\x" + s.substring(s.length() - 2);
/* 1404 */             } else if (ch.charValue() >= '?' && ch.charValue() <= '?') {
/* 1405 */               if (end + 1 < text.length()) {
/* 1406 */                 Character ch2 = Character.valueOf(text.charAt(++end));
/* 1407 */                 String s = "000" + Long.toHexString(Character.toCodePoint(ch.charValue(), ch2.charValue()));
/* 1408 */                 data = "\\U" + s.substring(s.length() - 8);
/*      */               } else {
/* 1410 */                 String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1411 */                 data = "\\u" + s.substring(s.length() - 4);
/*      */               } 
/*      */             } else {
/* 1414 */               String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1415 */               data = "\\u" + s.substring(s.length() - 4);
/*      */             } 
/*      */           } else {
/* 1418 */             data = String.valueOf(ch);
/*      */           } 
/* 1420 */           this.column += data.length();
/* 1421 */           this.stream.write(data);
/* 1422 */           start = end + 1;
/*      */         } 
/*      */       } 
/* 1425 */       if (0 < end && end < text.length() - 1 && (ch.charValue() == ' ' || start >= end) && this.column + end - start > this.bestWidth && split) {
/*      */         String data;
/*      */         
/* 1428 */         if (start >= end) {
/* 1429 */           data = "\\";
/*      */         } else {
/* 1431 */           data = text.substring(start, end) + "\\";
/*      */         } 
/* 1433 */         if (start < end) {
/* 1434 */           start = end;
/*      */         }
/* 1436 */         this.column += data.length();
/* 1437 */         this.stream.write(data);
/* 1438 */         writeIndent();
/* 1439 */         this.whitespace = false;
/* 1440 */         this.indention = false;
/* 1441 */         if (text.charAt(start) == ' ') {
/* 1442 */           data = "\\";
/* 1443 */           this.column += data.length();
/* 1444 */           this.stream.write(data);
/*      */         } 
/*      */       } 
/* 1447 */       end++;
/*      */     } 
/* 1449 */     writeIndicator("\"", false, false, false);
/*      */   }
/*      */   
/*      */   private boolean writeCommentLines(List<CommentLine> commentLines) throws IOException {
/* 1453 */     boolean wroteComment = false;
/* 1454 */     if (this.emitComments) {
/* 1455 */       int indentColumns = 0;
/* 1456 */       boolean firstComment = true;
/* 1457 */       for (CommentLine commentLine : commentLines) {
/* 1458 */         if (commentLine.getCommentType() != CommentType.BLANK_LINE) {
/* 1459 */           if (firstComment) {
/* 1460 */             firstComment = false;
/* 1461 */             writeIndicator("#", (commentLine.getCommentType() == CommentType.IN_LINE), false, false);
/* 1462 */             indentColumns = (this.column > 0) ? (this.column - 1) : 0;
/*      */           } else {
/* 1464 */             writeWhitespace(indentColumns);
/* 1465 */             writeIndicator("#", false, false, false);
/*      */           } 
/* 1467 */           this.stream.write(commentLine.getValue());
/* 1468 */           writeLineBreak(null);
/*      */         } else {
/* 1470 */           writeLineBreak(null);
/* 1471 */           writeIndent();
/*      */         } 
/* 1473 */         wroteComment = true;
/*      */       } 
/*      */     } 
/* 1476 */     return wroteComment;
/*      */   }
/*      */   
/*      */   private void writeBlockComment() throws IOException {
/* 1480 */     if (!this.blockCommentsCollector.isEmpty()) {
/* 1481 */       writeIndent();
/* 1482 */       writeCommentLines(this.blockCommentsCollector.consume());
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean writeInlineComments() throws IOException {
/* 1487 */     return writeCommentLines(this.inlineCommentsCollector.consume());
/*      */   }
/*      */   
/*      */   private String determineBlockHints(String text) {
/* 1491 */     StringBuilder hints = new StringBuilder();
/* 1492 */     if (Constant.LINEBR.has(text.charAt(0), " ")) {
/* 1493 */       hints.append(this.bestIndent);
/*      */     }
/* 1495 */     char ch1 = text.charAt(text.length() - 1);
/* 1496 */     if (Constant.LINEBR.hasNo(ch1)) {
/* 1497 */       hints.append("-");
/* 1498 */     } else if (text.length() == 1 || Constant.LINEBR.has(text.charAt(text.length() - 2))) {
/* 1499 */       hints.append("+");
/*      */     } 
/* 1501 */     return hints.toString();
/*      */   }
/*      */   
/*      */   void writeFolded(String text, boolean split) throws IOException {
/* 1505 */     String hints = determineBlockHints(text);
/* 1506 */     writeIndicator(">" + hints, true, false, false);
/* 1507 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1508 */       this.openEnded = true;
/*      */     }
/* 1510 */     if (!writeInlineComments()) {
/* 1511 */       writeLineBreak(null);
/*      */     }
/* 1513 */     boolean leadingSpace = true;
/* 1514 */     boolean spaces = false;
/* 1515 */     boolean breaks = true;
/* 1516 */     int start = 0, end = 0;
/* 1517 */     while (end <= text.length()) {
/* 1518 */       char ch = Character.MIN_VALUE;
/* 1519 */       if (end < text.length()) {
/* 1520 */         ch = text.charAt(end);
/*      */       }
/* 1522 */       if (breaks) {
/* 1523 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1524 */           if (!leadingSpace && ch != '\000' && ch != ' ' && text.charAt(start) == '\n') {
/* 1525 */             writeLineBreak(null);
/*      */           }
/* 1527 */           leadingSpace = (ch == ' ');
/* 1528 */           String data = text.substring(start, end);
/* 1529 */           for (char br : data.toCharArray()) {
/* 1530 */             if (br == '\n') {
/* 1531 */               writeLineBreak(null);
/*      */             } else {
/* 1533 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1536 */           if (ch != '\000') {
/* 1537 */             writeIndent();
/*      */           }
/* 1539 */           start = end;
/*      */         } 
/* 1541 */       } else if (spaces) {
/* 1542 */         if (ch != ' ') {
/* 1543 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1544 */             writeIndent();
/*      */           } else {
/* 1546 */             int len = end - start;
/* 1547 */             this.column += len;
/* 1548 */             this.stream.write(text, start, len);
/*      */           } 
/* 1550 */           start = end;
/*      */         }
/*      */       
/* 1553 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1554 */         int len = end - start;
/* 1555 */         this.column += len;
/* 1556 */         this.stream.write(text, start, len);
/* 1557 */         if (ch == '\000') {
/* 1558 */           writeLineBreak(null);
/*      */         }
/* 1560 */         start = end;
/*      */       } 
/*      */       
/* 1563 */       if (ch != '\000') {
/* 1564 */         breaks = Constant.LINEBR.has(ch);
/* 1565 */         spaces = (ch == ' ');
/*      */       } 
/* 1567 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeLiteral(String text) throws IOException {
/* 1572 */     String hints = determineBlockHints(text);
/* 1573 */     writeIndicator("|" + hints, true, false, false);
/* 1574 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1575 */       this.openEnded = true;
/*      */     }
/* 1577 */     if (!writeInlineComments()) {
/* 1578 */       writeLineBreak(null);
/*      */     }
/* 1580 */     boolean breaks = true;
/* 1581 */     int start = 0, end = 0;
/* 1582 */     while (end <= text.length()) {
/* 1583 */       char ch = Character.MIN_VALUE;
/* 1584 */       if (end < text.length()) {
/* 1585 */         ch = text.charAt(end);
/*      */       }
/* 1587 */       if (breaks) {
/* 1588 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1589 */           String data = text.substring(start, end);
/* 1590 */           for (char br : data.toCharArray()) {
/* 1591 */             if (br == '\n') {
/* 1592 */               writeLineBreak(null);
/*      */             } else {
/* 1594 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1597 */           if (ch != '\000') {
/* 1598 */             writeIndent();
/*      */           }
/* 1600 */           start = end;
/*      */         }
/*      */       
/* 1603 */       } else if (ch == '\000' || Constant.LINEBR.has(ch)) {
/* 1604 */         this.stream.write(text, start, end - start);
/* 1605 */         if (ch == '\000') {
/* 1606 */           writeLineBreak(null);
/*      */         }
/* 1608 */         start = end;
/*      */       } 
/*      */       
/* 1611 */       if (ch != '\000') {
/* 1612 */         breaks = Constant.LINEBR.has(ch);
/*      */       }
/* 1614 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writePlain(String text, boolean split) throws IOException {
/* 1619 */     if (this.rootContext) {
/* 1620 */       this.openEnded = true;
/*      */     }
/* 1622 */     if (text.length() == 0) {
/*      */       return;
/*      */     }
/* 1625 */     if (!this.whitespace) {
/* 1626 */       this.column++;
/* 1627 */       this.stream.write(SPACE);
/*      */     } 
/* 1629 */     this.whitespace = false;
/* 1630 */     this.indention = false;
/* 1631 */     boolean spaces = false;
/* 1632 */     boolean breaks = false;
/* 1633 */     int start = 0, end = 0;
/* 1634 */     while (end <= text.length()) {
/* 1635 */       char ch = Character.MIN_VALUE;
/* 1636 */       if (end < text.length()) {
/* 1637 */         ch = text.charAt(end);
/*      */       }
/* 1639 */       if (spaces) {
/* 1640 */         if (ch != ' ') {
/* 1641 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1642 */             writeIndent();
/* 1643 */             this.whitespace = false;
/* 1644 */             this.indention = false;
/*      */           } else {
/* 1646 */             int len = end - start;
/* 1647 */             this.column += len;
/* 1648 */             this.stream.write(text, start, len);
/*      */           } 
/* 1650 */           start = end;
/*      */         } 
/* 1652 */       } else if (breaks) {
/* 1653 */         if (Constant.LINEBR.hasNo(ch)) {
/* 1654 */           if (text.charAt(start) == '\n') {
/* 1655 */             writeLineBreak(null);
/*      */           }
/* 1657 */           String data = text.substring(start, end);
/* 1658 */           for (char br : data.toCharArray()) {
/* 1659 */             if (br == '\n') {
/* 1660 */               writeLineBreak(null);
/*      */             } else {
/* 1662 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1665 */           writeIndent();
/* 1666 */           this.whitespace = false;
/* 1667 */           this.indention = false;
/* 1668 */           start = end;
/*      */         }
/*      */       
/* 1671 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1672 */         int len = end - start;
/* 1673 */         this.column += len;
/* 1674 */         this.stream.write(text, start, len);
/* 1675 */         start = end;
/*      */       } 
/*      */       
/* 1678 */       if (ch != '\000') {
/* 1679 */         spaces = (ch == ' ');
/* 1680 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1682 */       end++;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\emitter\Emitter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */