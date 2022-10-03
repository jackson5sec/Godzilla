/*      */ package org.yaml.snakeyaml.scanner;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.comments.CommentType;
/*      */ import org.yaml.snakeyaml.error.Mark;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.tokens.AliasToken;
/*      */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEndToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.CommentToken;
/*      */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentEndToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.KeyToken;
/*      */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*      */ import org.yaml.snakeyaml.tokens.TagToken;
/*      */ import org.yaml.snakeyaml.tokens.TagTuple;
/*      */ import org.yaml.snakeyaml.tokens.Token;
/*      */ import org.yaml.snakeyaml.tokens.ValueToken;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ import org.yaml.snakeyaml.util.UriEncoder;
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
/*      */ public final class ScannerImpl
/*      */   implements Scanner
/*      */ {
/*   91 */   private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
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
/*  102 */   public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<>();
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
/*  118 */   public static final Map<Character, Integer> ESCAPE_CODES = new HashMap<>();
/*      */   private final StreamReader reader;
/*      */   
/*      */   static {
/*  122 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('0'), "\000");
/*      */     
/*  124 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('a'), "\007");
/*      */     
/*  126 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('b'), "\b");
/*      */     
/*  128 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('t'), "\t");
/*      */     
/*  130 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('n'), "\n");
/*      */     
/*  132 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('v'), "\013");
/*      */     
/*  134 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('f'), "\f");
/*      */     
/*  136 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('r'), "\r");
/*      */     
/*  138 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('e'), "\033");
/*      */     
/*  140 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), " ");
/*      */     
/*  142 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*      */     
/*  144 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*      */     
/*  146 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('N'), "");
/*      */     
/*  148 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('_'), " ");
/*      */     
/*  150 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('L'), " ");
/*      */     
/*  152 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('P'), " ");
/*      */ 
/*      */     
/*  155 */     ESCAPE_CODES.put(Character.valueOf('x'), Integer.valueOf(2));
/*      */     
/*  157 */     ESCAPE_CODES.put(Character.valueOf('u'), Integer.valueOf(4));
/*      */     
/*  159 */     ESCAPE_CODES.put(Character.valueOf('U'), Integer.valueOf(8));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean done = false;
/*      */ 
/*      */   
/*  167 */   private int flowLevel = 0;
/*      */ 
/*      */   
/*      */   private List<Token> tokens;
/*      */ 
/*      */   
/*      */   private Token lastToken;
/*      */ 
/*      */   
/*  176 */   private int tokensTaken = 0;
/*      */ 
/*      */   
/*  179 */   private int indent = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayStack<Integer> indents;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean parseComments;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowSimpleKey = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<Integer, SimpleKey> possibleSimpleKeys;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScannerImpl(StreamReader reader) {
/*  223 */     this.parseComments = false;
/*  224 */     this.reader = reader;
/*  225 */     this.tokens = new ArrayList<>(100);
/*  226 */     this.indents = new ArrayStack(10);
/*      */     
/*  228 */     this.possibleSimpleKeys = new LinkedHashMap<>();
/*  229 */     fetchStreamStart();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScannerImpl setParseComments(boolean parseComments) {
/*  238 */     this.parseComments = parseComments;
/*  239 */     return this;
/*      */   }
/*      */   
/*      */   public boolean isParseComments() {
/*  243 */     return this.parseComments;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkToken(Token.ID... choices) {
/*  250 */     while (needMoreTokens()) {
/*  251 */       fetchMoreTokens();
/*      */     }
/*  253 */     if (!this.tokens.isEmpty()) {
/*  254 */       if (choices.length == 0) {
/*  255 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  259 */       Token.ID first = ((Token)this.tokens.get(0)).getTokenId();
/*  260 */       for (int i = 0; i < choices.length; i++) {
/*  261 */         if (first == choices[i]) {
/*  262 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  266 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token peekToken() {
/*  273 */     while (needMoreTokens()) {
/*  274 */       fetchMoreTokens();
/*      */     }
/*  276 */     return this.tokens.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getToken() {
/*  283 */     this.tokensTaken++;
/*  284 */     return this.tokens.remove(0);
/*      */   }
/*      */   
/*      */   private void addToken(Token token) {
/*  288 */     this.lastToken = token;
/*  289 */     this.tokens.add(token);
/*      */   }
/*      */   
/*      */   private void addToken(int index, Token token) {
/*  293 */     if (index == this.tokens.size()) {
/*  294 */       this.lastToken = token;
/*      */     }
/*  296 */     this.tokens.add(index, token);
/*      */   }
/*      */   
/*      */   private void addAllTokens(List<Token> tokens) {
/*  300 */     this.lastToken = tokens.get(tokens.size() - 1);
/*  301 */     this.tokens.addAll(tokens);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreTokens() {
/*  310 */     if (this.done) {
/*  311 */       return false;
/*      */     }
/*      */     
/*  314 */     if (this.tokens.isEmpty()) {
/*  315 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  319 */     stalePossibleSimpleKeys();
/*  320 */     return (nextPossibleSimpleKey() == this.tokensTaken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchMoreTokens() {
/*  328 */     scanToNextToken();
/*      */     
/*  330 */     stalePossibleSimpleKeys();
/*      */ 
/*      */     
/*  333 */     unwindIndent(this.reader.getColumn());
/*      */ 
/*      */     
/*  336 */     int c = this.reader.peek();
/*  337 */     switch (c) {
/*      */       
/*      */       case 0:
/*  340 */         fetchStreamEnd();
/*      */         return;
/*      */       
/*      */       case 37:
/*  344 */         if (checkDirective()) {
/*  345 */           fetchDirective();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 45:
/*  351 */         if (checkDocumentStart()) {
/*  352 */           fetchDocumentStart();
/*      */           return;
/*      */         } 
/*  355 */         if (checkBlockEntry()) {
/*  356 */           fetchBlockEntry();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 46:
/*  362 */         if (checkDocumentEnd()) {
/*  363 */           fetchDocumentEnd();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 91:
/*  370 */         fetchFlowSequenceStart();
/*      */         return;
/*      */       
/*      */       case 123:
/*  374 */         fetchFlowMappingStart();
/*      */         return;
/*      */       
/*      */       case 93:
/*  378 */         fetchFlowSequenceEnd();
/*      */         return;
/*      */       
/*      */       case 125:
/*  382 */         fetchFlowMappingEnd();
/*      */         return;
/*      */       
/*      */       case 44:
/*  386 */         fetchFlowEntry();
/*      */         return;
/*      */ 
/*      */       
/*      */       case 63:
/*  391 */         if (checkKey()) {
/*  392 */           fetchKey();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 58:
/*  398 */         if (checkValue()) {
/*  399 */           fetchValue();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 42:
/*  405 */         fetchAlias();
/*      */         return;
/*      */       
/*      */       case 38:
/*  409 */         fetchAnchor();
/*      */         return;
/*      */       
/*      */       case 33:
/*  413 */         fetchTag();
/*      */         return;
/*      */       
/*      */       case 124:
/*  417 */         if (this.flowLevel == 0) {
/*  418 */           fetchLiteral();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 62:
/*  424 */         if (this.flowLevel == 0) {
/*  425 */           fetchFolded();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 39:
/*  431 */         fetchSingle();
/*      */         return;
/*      */       
/*      */       case 34:
/*  435 */         fetchDouble();
/*      */         return;
/*      */     } 
/*      */     
/*  439 */     if (checkPlain()) {
/*  440 */       fetchPlain();
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  446 */     String chRepresentation = String.valueOf(Character.toChars(c));
/*  447 */     for (Character s : ESCAPE_REPLACEMENTS.keySet()) {
/*  448 */       String v = ESCAPE_REPLACEMENTS.get(s);
/*  449 */       if (v.equals(chRepresentation)) {
/*  450 */         chRepresentation = "\\" + s;
/*      */         break;
/*      */       } 
/*      */     } 
/*  454 */     if (c == 9) {
/*  455 */       chRepresentation = chRepresentation + "(TAB)";
/*      */     }
/*  457 */     String text = String.format("found character '%s' that cannot start any token. (Do not use %s for indentation)", new Object[] { chRepresentation, chRepresentation });
/*      */     
/*  459 */     throw new ScannerException("while scanning for the next token", null, text, this.reader
/*  460 */         .getMark());
/*      */   }
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
/*      */   private int nextPossibleSimpleKey() {
/*  474 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  475 */       return ((SimpleKey)this.possibleSimpleKeys.values().iterator().next()).getTokenNumber();
/*      */     }
/*  477 */     return -1;
/*      */   }
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
/*      */   private void stalePossibleSimpleKeys() {
/*  491 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  492 */       Iterator<SimpleKey> iterator = this.possibleSimpleKeys.values().iterator();
/*  493 */       while (iterator.hasNext()) {
/*  494 */         SimpleKey key = iterator.next();
/*  495 */         if (key.getLine() != this.reader.getLine() || this.reader
/*  496 */           .getIndex() - key.getIndex() > 1024) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  501 */           if (key.isRequired())
/*      */           {
/*      */             
/*  504 */             throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader
/*  505 */                 .getMark());
/*      */           }
/*  507 */           iterator.remove();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
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
/*      */   private void savePossibleSimpleKey() {
/*  526 */     boolean required = (this.flowLevel == 0 && this.indent == this.reader.getColumn());
/*      */     
/*  528 */     if (this.allowSimpleKey || !required) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  538 */       if (this.allowSimpleKey) {
/*  539 */         removePossibleSimpleKey();
/*  540 */         int tokenNumber = this.tokensTaken + this.tokens.size();
/*      */         
/*  542 */         SimpleKey key = new SimpleKey(tokenNumber, required, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
/*  543 */         this.possibleSimpleKeys.put(Integer.valueOf(this.flowLevel), key);
/*      */       } 
/*      */       return;
/*      */     } 
/*      */     throw new YAMLException("A simple key is required only if it is the first token in the current line");
/*      */   }
/*      */   
/*      */   private void removePossibleSimpleKey() {
/*  551 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  552 */     if (key != null && key.isRequired()) {
/*  553 */       throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader
/*  554 */           .getMark());
/*      */     }
/*      */   }
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
/*      */   private void unwindIndent(int col) {
/*  583 */     if (this.flowLevel != 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  588 */     while (this.indent > col) {
/*  589 */       Mark mark = this.reader.getMark();
/*  590 */       this.indent = ((Integer)this.indents.pop()).intValue();
/*  591 */       addToken((Token)new BlockEndToken(mark, mark));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean addIndent(int column) {
/*  599 */     if (this.indent < column) {
/*  600 */       this.indents.push(Integer.valueOf(this.indent));
/*  601 */       this.indent = column;
/*  602 */       return true;
/*      */     } 
/*  604 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchStreamStart() {
/*  615 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  618 */     StreamStartToken streamStartToken = new StreamStartToken(mark, mark);
/*  619 */     addToken((Token)streamStartToken);
/*      */   }
/*      */ 
/*      */   
/*      */   private void fetchStreamEnd() {
/*  624 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  627 */     removePossibleSimpleKey();
/*  628 */     this.allowSimpleKey = false;
/*  629 */     this.possibleSimpleKeys.clear();
/*      */ 
/*      */     
/*  632 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  635 */     StreamEndToken streamEndToken = new StreamEndToken(mark, mark);
/*  636 */     addToken((Token)streamEndToken);
/*      */ 
/*      */     
/*  639 */     this.done = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDirective() {
/*  651 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  654 */     removePossibleSimpleKey();
/*  655 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  658 */     List<Token> tok = scanDirective();
/*  659 */     addAllTokens(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentStart() {
/*  666 */     fetchDocumentIndicator(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentEnd() {
/*  673 */     fetchDocumentIndicator(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentIndicator(boolean isDocumentStart) {
/*      */     DocumentEndToken documentEndToken;
/*  682 */     unwindIndent(-1);
/*      */ 
/*      */ 
/*      */     
/*  686 */     removePossibleSimpleKey();
/*  687 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  690 */     Mark startMark = this.reader.getMark();
/*  691 */     this.reader.forward(3);
/*  692 */     Mark endMark = this.reader.getMark();
/*      */     
/*  694 */     if (isDocumentStart) {
/*  695 */       DocumentStartToken documentStartToken = new DocumentStartToken(startMark, endMark);
/*      */     } else {
/*  697 */       documentEndToken = new DocumentEndToken(startMark, endMark);
/*      */     } 
/*  699 */     addToken((Token)documentEndToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceStart() {
/*  703 */     fetchFlowCollectionStart(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingStart() {
/*  707 */     fetchFlowCollectionStart(true);
/*      */   }
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
/*      */   private void fetchFlowCollectionStart(boolean isMappingStart) {
/*      */     FlowSequenceStartToken flowSequenceStartToken;
/*  724 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  727 */     this.flowLevel++;
/*      */ 
/*      */     
/*  730 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  733 */     Mark startMark = this.reader.getMark();
/*  734 */     this.reader.forward(1);
/*  735 */     Mark endMark = this.reader.getMark();
/*      */     
/*  737 */     if (isMappingStart) {
/*  738 */       FlowMappingStartToken flowMappingStartToken = new FlowMappingStartToken(startMark, endMark);
/*      */     } else {
/*  740 */       flowSequenceStartToken = new FlowSequenceStartToken(startMark, endMark);
/*      */     } 
/*  742 */     addToken((Token)flowSequenceStartToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceEnd() {
/*  746 */     fetchFlowCollectionEnd(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingEnd() {
/*  750 */     fetchFlowCollectionEnd(true);
/*      */   }
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
/*      */   private void fetchFlowCollectionEnd(boolean isMappingEnd) {
/*      */     FlowSequenceEndToken flowSequenceEndToken;
/*  765 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  768 */     this.flowLevel--;
/*      */ 
/*      */     
/*  771 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  774 */     Mark startMark = this.reader.getMark();
/*  775 */     this.reader.forward();
/*  776 */     Mark endMark = this.reader.getMark();
/*      */     
/*  778 */     if (isMappingEnd) {
/*  779 */       FlowMappingEndToken flowMappingEndToken = new FlowMappingEndToken(startMark, endMark);
/*      */     } else {
/*  781 */       flowSequenceEndToken = new FlowSequenceEndToken(startMark, endMark);
/*      */     } 
/*  783 */     addToken((Token)flowSequenceEndToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowEntry() {
/*  794 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  797 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  800 */     Mark startMark = this.reader.getMark();
/*  801 */     this.reader.forward();
/*  802 */     Mark endMark = this.reader.getMark();
/*  803 */     FlowEntryToken flowEntryToken = new FlowEntryToken(startMark, endMark);
/*  804 */     addToken((Token)flowEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockEntry() {
/*  814 */     if (this.flowLevel == 0) {
/*      */       
/*  816 */       if (!this.allowSimpleKey) {
/*  817 */         throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader
/*  818 */             .getMark());
/*      */       }
/*      */ 
/*      */       
/*  822 */       if (addIndent(this.reader.getColumn())) {
/*  823 */         Mark mark = this.reader.getMark();
/*  824 */         addToken((Token)new BlockSequenceStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  831 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  834 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  837 */     Mark startMark = this.reader.getMark();
/*  838 */     this.reader.forward();
/*  839 */     Mark endMark = this.reader.getMark();
/*  840 */     BlockEntryToken blockEntryToken = new BlockEntryToken(startMark, endMark);
/*  841 */     addToken((Token)blockEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchKey() {
/*  851 */     if (this.flowLevel == 0) {
/*      */       
/*  853 */       if (!this.allowSimpleKey) {
/*  854 */         throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader
/*  855 */             .getMark());
/*      */       }
/*      */       
/*  858 */       if (addIndent(this.reader.getColumn())) {
/*  859 */         Mark mark = this.reader.getMark();
/*  860 */         addToken((Token)new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */     
/*  864 */     this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */     
/*  867 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  870 */     Mark startMark = this.reader.getMark();
/*  871 */     this.reader.forward();
/*  872 */     Mark endMark = this.reader.getMark();
/*  873 */     KeyToken keyToken = new KeyToken(startMark, endMark);
/*  874 */     addToken((Token)keyToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchValue() {
/*  884 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  885 */     if (key != null) {
/*      */       
/*  887 */       addToken(key.getTokenNumber() - this.tokensTaken, (Token)new KeyToken(key.getMark(), key.getMark()));
/*      */ 
/*      */ 
/*      */       
/*  891 */       if (this.flowLevel == 0 && 
/*  892 */         addIndent(key.getColumn())) {
/*  893 */         addToken(key.getTokenNumber() - this.tokensTaken, (Token)new BlockMappingStartToken(key
/*  894 */               .getMark(), key.getMark()));
/*      */       }
/*      */ 
/*      */       
/*  898 */       this.allowSimpleKey = false;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  904 */       if (this.flowLevel == 0)
/*      */       {
/*      */ 
/*      */         
/*  908 */         if (!this.allowSimpleKey) {
/*  909 */           throw new ScannerException(null, null, "mapping values are not allowed here", this.reader
/*  910 */               .getMark());
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  917 */       if (this.flowLevel == 0 && 
/*  918 */         addIndent(this.reader.getColumn())) {
/*  919 */         Mark mark = this.reader.getMark();
/*  920 */         addToken((Token)new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  925 */       this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */       
/*  928 */       removePossibleSimpleKey();
/*      */     } 
/*      */     
/*  931 */     Mark startMark = this.reader.getMark();
/*  932 */     this.reader.forward();
/*  933 */     Mark endMark = this.reader.getMark();
/*  934 */     ValueToken valueToken = new ValueToken(startMark, endMark);
/*  935 */     addToken((Token)valueToken);
/*      */   }
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
/*      */   private void fetchAlias() {
/*  950 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  953 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  956 */     Token tok = scanAnchor(false);
/*  957 */     addToken(tok);
/*      */   }
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
/*      */   private void fetchAnchor() {
/*  971 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  974 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  977 */     Token tok = scanAnchor(true);
/*  978 */     addToken(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchTag() {
/*  988 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  991 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  994 */     Token tok = scanTag();
/*  995 */     addToken(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchLiteral() {
/* 1006 */     fetchBlockScalar('|');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFolded() {
/* 1016 */     fetchBlockScalar('>');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockScalar(char style) {
/* 1028 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/* 1031 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/* 1034 */     List<Token> tok = scanBlockScalar(style);
/* 1035 */     addAllTokens(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchSingle() {
/* 1042 */     fetchFlowScalar('\'');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDouble() {
/* 1049 */     fetchFlowScalar('"');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowScalar(char style) {
/* 1061 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/* 1064 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1067 */     Token tok = scanFlowScalar(style);
/* 1068 */     addToken(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchPlain() {
/* 1076 */     savePossibleSimpleKey();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1081 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1084 */     Token tok = scanPlain();
/* 1085 */     addToken(tok);
/*      */   }
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
/*      */   private boolean checkDirective() {
/* 1098 */     return (this.reader.getColumn() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentStart() {
/* 1107 */     if (this.reader.getColumn() == 0 && 
/* 1108 */       "---".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
/* 1109 */       return true;
/*      */     }
/*      */     
/* 1112 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentEnd() {
/* 1121 */     if (this.reader.getColumn() == 0 && 
/* 1122 */       "...".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
/* 1123 */       return true;
/*      */     }
/*      */     
/* 1126 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkBlockEntry() {
/* 1134 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkKey() {
/* 1142 */     if (this.flowLevel != 0) {
/* 1143 */       return true;
/*      */     }
/*      */     
/* 1146 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkValue() {
/* 1155 */     if (this.flowLevel != 0) {
/* 1156 */       return true;
/*      */     }
/*      */     
/* 1159 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
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
/*      */   private boolean checkPlain() {
/* 1183 */     int c = this.reader.peek();
/*      */ 
/*      */     
/* 1186 */     return (Constant.NULL_BL_T_LINEBR.hasNo(c, "-?:,[]{}#&*!|>'\"%@`") || (Constant.NULL_BL_T_LINEBR
/* 1187 */       .hasNo(this.reader.peek(1)) && (c == 45 || (this.flowLevel == 0 && "?:"
/* 1188 */       .indexOf(c) != -1))));
/*      */   }
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
/*      */   private void scanToNextToken() {
/* 1217 */     if (this.reader.getIndex() == 0 && this.reader.peek() == 65279) {
/* 1218 */       this.reader.forward();
/*      */     }
/* 1220 */     boolean found = false;
/* 1221 */     int inlineStartColumn = -1;
/* 1222 */     while (!found) {
/* 1223 */       Mark startMark = this.reader.getMark();
/* 1224 */       boolean commentSeen = false;
/* 1225 */       int ff = 0;
/*      */ 
/*      */       
/* 1228 */       while (this.reader.peek(ff) == 32) {
/* 1229 */         ff++;
/*      */       }
/* 1231 */       if (ff > 0) {
/* 1232 */         this.reader.forward(ff);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1238 */       if (this.reader.peek() == 35) {
/* 1239 */         CommentType type; commentSeen = true;
/*      */         
/* 1241 */         if (startMark.getColumn() != 0 && (this.lastToken == null || this.lastToken
/* 1242 */           .getTokenId() != Token.ID.BlockEntry)) {
/* 1243 */           type = CommentType.IN_LINE;
/* 1244 */           inlineStartColumn = this.reader.getColumn();
/* 1245 */         } else if (inlineStartColumn == this.reader.getColumn()) {
/* 1246 */           type = CommentType.IN_LINE;
/*      */         } else {
/* 1248 */           inlineStartColumn = -1;
/* 1249 */           type = CommentType.BLOCK;
/*      */         } 
/* 1251 */         CommentToken token = scanComment(type);
/* 1252 */         if (this.parseComments) {
/* 1253 */           addToken((Token)token);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1258 */       String breaks = scanLineBreak();
/* 1259 */       if (breaks.length() != 0) {
/* 1260 */         if (this.parseComments && !commentSeen && 
/* 1261 */           startMark.getColumn() == 0) {
/* 1262 */           Mark endMark = this.reader.getMark();
/* 1263 */           addToken((Token)new CommentToken(CommentType.BLANK_LINE, breaks, startMark, endMark));
/*      */         } 
/*      */         
/* 1266 */         if (this.flowLevel == 0)
/*      */         {
/*      */           
/* 1269 */           this.allowSimpleKey = true; } 
/*      */         continue;
/*      */       } 
/* 1272 */       found = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CommentToken scanComment(CommentType type) {
/* 1279 */     Mark startMark = this.reader.getMark();
/* 1280 */     this.reader.forward();
/* 1281 */     int length = 0;
/* 1282 */     while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
/* 1283 */       length++;
/*      */     }
/* 1285 */     String value = this.reader.prefixForward(length);
/* 1286 */     Mark endMark = this.reader.getMark();
/* 1287 */     return new CommentToken(type, value, startMark, endMark);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private List<Token> scanDirective() {
/* 1293 */     Mark endMark, startMark = this.reader.getMark();
/*      */     
/* 1295 */     this.reader.forward();
/* 1296 */     String name = scanDirectiveName(startMark);
/* 1297 */     List<?> value = null;
/* 1298 */     if ("YAML".equals(name)) {
/* 1299 */       value = scanYamlDirectiveValue(startMark);
/* 1300 */       endMark = this.reader.getMark();
/* 1301 */     } else if ("TAG".equals(name)) {
/* 1302 */       value = scanTagDirectiveValue(startMark);
/* 1303 */       endMark = this.reader.getMark();
/*      */     } else {
/* 1305 */       endMark = this.reader.getMark();
/* 1306 */       int ff = 0;
/* 1307 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1308 */         ff++;
/*      */       }
/* 1310 */       if (ff > 0) {
/* 1311 */         this.reader.forward(ff);
/*      */       }
/*      */     } 
/* 1314 */     CommentToken commentToken = scanDirectiveIgnoredLine(startMark);
/* 1315 */     DirectiveToken token = new DirectiveToken(name, value, startMark, endMark);
/* 1316 */     return makeTokenList(new Token[] { (Token)token, (Token)commentToken });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanDirectiveName(Mark startMark) {
/* 1327 */     int length = 0;
/*      */ 
/*      */ 
/*      */     
/* 1331 */     int c = this.reader.peek(length);
/* 1332 */     while (Constant.ALPHA.has(c)) {
/* 1333 */       length++;
/* 1334 */       c = this.reader.peek(length);
/*      */     } 
/*      */     
/* 1337 */     if (length == 0) {
/* 1338 */       String s = String.valueOf(Character.toChars(c));
/* 1339 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1341 */           .getMark());
/*      */     } 
/* 1343 */     String value = this.reader.prefixForward(length);
/* 1344 */     c = this.reader.peek();
/* 1345 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1346 */       String s = String.valueOf(Character.toChars(c));
/* 1347 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1349 */           .getMark());
/*      */     } 
/* 1351 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<Integer> scanYamlDirectiveValue(Mark startMark) {
/* 1356 */     while (this.reader.peek() == 32) {
/* 1357 */       this.reader.forward();
/*      */     }
/* 1359 */     Integer major = scanYamlDirectiveNumber(startMark);
/* 1360 */     int c = this.reader.peek();
/* 1361 */     if (c != 46) {
/* 1362 */       String s = String.valueOf(Character.toChars(c));
/* 1363 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1365 */           .getMark());
/*      */     } 
/* 1367 */     this.reader.forward();
/* 1368 */     Integer minor = scanYamlDirectiveNumber(startMark);
/* 1369 */     c = this.reader.peek();
/* 1370 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1371 */       String s = String.valueOf(Character.toChars(c));
/* 1372 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1374 */           .getMark());
/*      */     } 
/* 1376 */     List<Integer> result = new ArrayList<>(2);
/* 1377 */     result.add(major);
/* 1378 */     result.add(minor);
/* 1379 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Integer scanYamlDirectiveNumber(Mark startMark) {
/* 1391 */     int c = this.reader.peek();
/* 1392 */     if (!Character.isDigit(c)) {
/* 1393 */       String s = String.valueOf(Character.toChars(c));
/* 1394 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + s + "(" + c + ")", this.reader
/* 1395 */           .getMark());
/*      */     } 
/* 1397 */     int length = 0;
/* 1398 */     while (Character.isDigit(this.reader.peek(length))) {
/* 1399 */       length++;
/*      */     }
/* 1401 */     Integer value = Integer.valueOf(Integer.parseInt(this.reader.prefixForward(length)));
/* 1402 */     return value;
/*      */   }
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
/*      */   private List<String> scanTagDirectiveValue(Mark startMark) {
/* 1419 */     while (this.reader.peek() == 32) {
/* 1420 */       this.reader.forward();
/*      */     }
/* 1422 */     String handle = scanTagDirectiveHandle(startMark);
/* 1423 */     while (this.reader.peek() == 32) {
/* 1424 */       this.reader.forward();
/*      */     }
/* 1426 */     String prefix = scanTagDirectivePrefix(startMark);
/* 1427 */     List<String> result = new ArrayList<>(2);
/* 1428 */     result.add(handle);
/* 1429 */     result.add(prefix);
/* 1430 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectiveHandle(Mark startMark) {
/* 1442 */     String value = scanTagHandle("directive", startMark);
/* 1443 */     int c = this.reader.peek();
/* 1444 */     if (c != 32) {
/* 1445 */       String s = String.valueOf(Character.toChars(c));
/* 1446 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader
/* 1447 */           .getMark());
/*      */     } 
/* 1449 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectivePrefix(Mark startMark) {
/* 1459 */     String value = scanTagUri("directive", startMark);
/* 1460 */     int c = this.reader.peek();
/* 1461 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1462 */       String s = String.valueOf(Character.toChars(c));
/* 1463 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1465 */           .getMark());
/*      */     } 
/* 1467 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private CommentToken scanDirectiveIgnoredLine(Mark startMark) {
/* 1472 */     while (this.reader.peek() == 32) {
/* 1473 */       this.reader.forward();
/*      */     }
/* 1475 */     CommentToken commentToken = null;
/* 1476 */     if (this.reader.peek() == 35) {
/* 1477 */       CommentToken comment = scanComment(CommentType.IN_LINE);
/* 1478 */       if (this.parseComments) {
/* 1479 */         commentToken = comment;
/*      */       }
/*      */     } 
/* 1482 */     int c = this.reader.peek();
/* 1483 */     String lineBreak = scanLineBreak();
/* 1484 */     if (lineBreak.length() == 0 && c != 0) {
/* 1485 */       String s = String.valueOf(Character.toChars(c));
/* 1486 */       throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1488 */           .getMark());
/*      */     } 
/* 1490 */     return commentToken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Token scanAnchor(boolean isAnchor) {
/*      */     AliasToken aliasToken;
/* 1502 */     Mark startMark = this.reader.getMark();
/* 1503 */     int indicator = this.reader.peek();
/* 1504 */     String name = (indicator == 42) ? "alias" : "anchor";
/* 1505 */     this.reader.forward();
/* 1506 */     int length = 0;
/* 1507 */     int c = this.reader.peek(length);
/* 1508 */     while (Constant.NULL_BL_T_LINEBR.hasNo(c, ":,[]{}/.*&")) {
/* 1509 */       length++;
/* 1510 */       c = this.reader.peek(length);
/*      */     } 
/* 1512 */     if (length == 0) {
/* 1513 */       String s = String.valueOf(Character.toChars(c));
/* 1514 */       throw new ScannerException("while scanning an " + name, startMark, "unexpected character found " + s + "(" + c + ")", this.reader
/* 1515 */           .getMark());
/*      */     } 
/* 1517 */     String value = this.reader.prefixForward(length);
/* 1518 */     c = this.reader.peek();
/* 1519 */     if (Constant.NULL_BL_T_LINEBR.hasNo(c, "?:,]}%@`")) {
/* 1520 */       String s = String.valueOf(Character.toChars(c));
/* 1521 */       throw new ScannerException("while scanning an " + name, startMark, "unexpected character found " + s + "(" + c + ")", this.reader
/* 1522 */           .getMark());
/*      */     } 
/* 1524 */     Mark endMark = this.reader.getMark();
/*      */     
/* 1526 */     if (isAnchor) {
/* 1527 */       AnchorToken anchorToken = new AnchorToken(value, startMark, endMark);
/*      */     } else {
/* 1529 */       aliasToken = new AliasToken(value, startMark, endMark);
/*      */     } 
/* 1531 */     return (Token)aliasToken;
/*      */   }
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
/*      */   private Token scanTag() {
/* 1569 */     Mark startMark = this.reader.getMark();
/*      */ 
/*      */     
/* 1572 */     int c = this.reader.peek(1);
/* 1573 */     String handle = null;
/* 1574 */     String suffix = null;
/*      */     
/* 1576 */     if (c == 60) {
/*      */ 
/*      */       
/* 1579 */       this.reader.forward(2);
/* 1580 */       suffix = scanTagUri("tag", startMark);
/* 1581 */       c = this.reader.peek();
/* 1582 */       if (c != 62) {
/*      */ 
/*      */         
/* 1585 */         String s = String.valueOf(Character.toChars(c));
/* 1586 */         throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + s + "' (" + c + ")", this.reader
/*      */             
/* 1588 */             .getMark());
/*      */       } 
/* 1590 */       this.reader.forward();
/* 1591 */     } else if (Constant.NULL_BL_T_LINEBR.has(c)) {
/*      */ 
/*      */       
/* 1594 */       suffix = "!";
/* 1595 */       this.reader.forward();
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1601 */       int length = 1;
/* 1602 */       boolean useHandle = false;
/* 1603 */       while (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1604 */         if (c == 33) {
/* 1605 */           useHandle = true;
/*      */           break;
/*      */         } 
/* 1608 */         length++;
/* 1609 */         c = this.reader.peek(length);
/*      */       } 
/*      */ 
/*      */       
/* 1613 */       if (useHandle) {
/* 1614 */         handle = scanTagHandle("tag", startMark);
/*      */       } else {
/* 1616 */         handle = "!";
/* 1617 */         this.reader.forward();
/*      */       } 
/* 1619 */       suffix = scanTagUri("tag", startMark);
/*      */     } 
/* 1621 */     c = this.reader.peek();
/*      */ 
/*      */     
/* 1624 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1625 */       String s = String.valueOf(Character.toChars(c));
/* 1626 */       throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + s + "' (" + c + ")", this.reader
/* 1627 */           .getMark());
/*      */     } 
/* 1629 */     TagTuple value = new TagTuple(handle, suffix);
/* 1630 */     Mark endMark = this.reader.getMark();
/* 1631 */     return (Token)new TagToken(value, startMark, endMark);
/*      */   }
/*      */   
/*      */   private List<Token> scanBlockScalar(char style) {
/*      */     boolean folded;
/*      */     String breaks;
/*      */     int indent;
/*      */     Mark mark1;
/* 1639 */     if (style == '>') {
/* 1640 */       folded = true;
/*      */     } else {
/* 1642 */       folded = false;
/*      */     } 
/* 1644 */     StringBuilder chunks = new StringBuilder();
/* 1645 */     Mark startMark = this.reader.getMark();
/*      */     
/* 1647 */     this.reader.forward();
/* 1648 */     Chomping chompi = scanBlockScalarIndicators(startMark);
/* 1649 */     int increment = chompi.getIncrement();
/* 1650 */     CommentToken commentToken = scanBlockScalarIgnoredLine(startMark);
/*      */ 
/*      */     
/* 1653 */     int minIndent = this.indent + 1;
/* 1654 */     if (minIndent < 1) {
/* 1655 */       minIndent = 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1661 */     if (increment == -1) {
/* 1662 */       Object[] brme = scanBlockScalarIndentation();
/* 1663 */       breaks = (String)brme[0];
/* 1664 */       int maxIndent = ((Integer)brme[1]).intValue();
/* 1665 */       mark1 = (Mark)brme[2];
/* 1666 */       indent = Math.max(minIndent, maxIndent);
/*      */     } else {
/* 1668 */       indent = minIndent + increment - 1;
/* 1669 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1670 */       breaks = (String)brme[0];
/* 1671 */       mark1 = (Mark)brme[1];
/*      */     } 
/*      */     
/* 1674 */     String lineBreak = "";
/*      */ 
/*      */     
/* 1677 */     while (this.reader.getColumn() == indent && this.reader.peek() != 0) {
/* 1678 */       chunks.append(breaks);
/* 1679 */       boolean leadingNonSpace = (" \t".indexOf(this.reader.peek()) == -1);
/* 1680 */       int length = 0;
/* 1681 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
/* 1682 */         length++;
/*      */       }
/* 1684 */       chunks.append(this.reader.prefixForward(length));
/* 1685 */       lineBreak = scanLineBreak();
/* 1686 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1687 */       breaks = (String)brme[0];
/* 1688 */       mark1 = (Mark)brme[1];
/* 1689 */       if (this.reader.getColumn() == indent && this.reader.peek() != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1694 */         if (folded && "\n".equals(lineBreak) && leadingNonSpace && " \t"
/* 1695 */           .indexOf(this.reader.peek()) == -1) {
/* 1696 */           if (breaks.length() == 0)
/* 1697 */             chunks.append(" "); 
/*      */           continue;
/*      */         } 
/* 1700 */         chunks.append(lineBreak);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1709 */     if (chompi.chompTailIsNotFalse()) {
/* 1710 */       chunks.append(lineBreak);
/*      */     }
/* 1712 */     CommentToken blankLineCommentToken = null;
/* 1713 */     if (chompi.chompTailIsTrue()) {
/* 1714 */       if (this.parseComments) {
/* 1715 */         blankLineCommentToken = new CommentToken(CommentType.BLANK_LINE, breaks, startMark, mark1);
/*      */       }
/* 1717 */       chunks.append(breaks);
/*      */     } 
/*      */     
/* 1720 */     ScalarToken scalarToken = new ScalarToken(chunks.toString(), false, startMark, mark1, DumperOptions.ScalarStyle.createStyle(Character.valueOf(style)));
/* 1721 */     return makeTokenList(new Token[] { (Token)commentToken, (Token)scalarToken, (Token)blankLineCommentToken });
/*      */   }
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
/*      */   private Chomping scanBlockScalarIndicators(Mark startMark) {
/* 1741 */     Boolean chomping = null;
/* 1742 */     int increment = -1;
/* 1743 */     int c = this.reader.peek();
/* 1744 */     if (c == 45 || c == 43) {
/* 1745 */       if (c == 43) {
/* 1746 */         chomping = Boolean.TRUE;
/*      */       } else {
/* 1748 */         chomping = Boolean.FALSE;
/*      */       } 
/* 1750 */       this.reader.forward();
/* 1751 */       c = this.reader.peek();
/* 1752 */       if (Character.isDigit(c)) {
/* 1753 */         String s = String.valueOf(Character.toChars(c));
/* 1754 */         increment = Integer.parseInt(s);
/* 1755 */         if (increment == 0) {
/* 1756 */           throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader
/*      */               
/* 1758 */               .getMark());
/*      */         }
/* 1760 */         this.reader.forward();
/*      */       } 
/* 1762 */     } else if (Character.isDigit(c)) {
/* 1763 */       String s = String.valueOf(Character.toChars(c));
/* 1764 */       increment = Integer.parseInt(s);
/* 1765 */       if (increment == 0) {
/* 1766 */         throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader
/*      */             
/* 1768 */             .getMark());
/*      */       }
/* 1770 */       this.reader.forward();
/* 1771 */       c = this.reader.peek();
/* 1772 */       if (c == 45 || c == 43) {
/* 1773 */         if (c == 43) {
/* 1774 */           chomping = Boolean.TRUE;
/*      */         } else {
/* 1776 */           chomping = Boolean.FALSE;
/*      */         } 
/* 1778 */         this.reader.forward();
/*      */       } 
/*      */     } 
/* 1781 */     c = this.reader.peek();
/* 1782 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1783 */       String s = String.valueOf(Character.toChars(c));
/* 1784 */       throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1786 */           .getMark());
/*      */     } 
/* 1788 */     return new Chomping(chomping, increment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CommentToken scanBlockScalarIgnoredLine(Mark startMark) {
/* 1799 */     while (this.reader.peek() == 32) {
/* 1800 */       this.reader.forward();
/*      */     }
/*      */ 
/*      */     
/* 1804 */     CommentToken commentToken = null;
/* 1805 */     if (this.reader.peek() == 35) {
/* 1806 */       commentToken = scanComment(CommentType.IN_LINE);
/*      */     }
/*      */ 
/*      */     
/* 1810 */     int c = this.reader.peek();
/* 1811 */     String lineBreak = scanLineBreak();
/* 1812 */     if (lineBreak.length() == 0 && c != 0) {
/* 1813 */       String s = String.valueOf(Character.toChars(c));
/* 1814 */       throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1816 */           .getMark());
/*      */     } 
/* 1818 */     return commentToken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarIndentation() {
/* 1830 */     StringBuilder chunks = new StringBuilder();
/* 1831 */     int maxIndent = 0;
/* 1832 */     Mark endMark = this.reader.getMark();
/*      */ 
/*      */ 
/*      */     
/* 1836 */     while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
/* 1837 */       if (this.reader.peek() != 32) {
/*      */ 
/*      */         
/* 1840 */         chunks.append(scanLineBreak());
/* 1841 */         endMark = this.reader.getMark();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1846 */       this.reader.forward();
/* 1847 */       if (this.reader.getColumn() > maxIndent) {
/* 1848 */         maxIndent = this.reader.getColumn();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1853 */     return new Object[] { chunks.toString(), Integer.valueOf(maxIndent), endMark };
/*      */   }
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarBreaks(int indent) {
/* 1858 */     StringBuilder chunks = new StringBuilder();
/* 1859 */     Mark endMark = this.reader.getMark();
/* 1860 */     int col = this.reader.getColumn();
/*      */ 
/*      */     
/* 1863 */     while (col < indent && this.reader.peek() == 32) {
/* 1864 */       this.reader.forward();
/* 1865 */       col++;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1870 */     String lineBreak = null;
/* 1871 */     while ((lineBreak = scanLineBreak()).length() != 0) {
/* 1872 */       chunks.append(lineBreak);
/* 1873 */       endMark = this.reader.getMark();
/*      */ 
/*      */       
/* 1876 */       col = this.reader.getColumn();
/* 1877 */       while (col < indent && this.reader.peek() == 32) {
/* 1878 */         this.reader.forward();
/* 1879 */         col++;
/*      */       } 
/*      */     } 
/*      */     
/* 1883 */     return new Object[] { chunks.toString(), endMark };
/*      */   }
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
/*      */   private Token scanFlowScalar(char style) {
/*      */     boolean _double;
/* 1906 */     if (style == '"') {
/* 1907 */       _double = true;
/*      */     } else {
/* 1909 */       _double = false;
/*      */     } 
/* 1911 */     StringBuilder chunks = new StringBuilder();
/* 1912 */     Mark startMark = this.reader.getMark();
/* 1913 */     int quote = this.reader.peek();
/* 1914 */     this.reader.forward();
/* 1915 */     chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/* 1916 */     while (this.reader.peek() != quote) {
/* 1917 */       chunks.append(scanFlowScalarSpaces(startMark));
/* 1918 */       chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/*      */     } 
/* 1920 */     this.reader.forward();
/* 1921 */     Mark endMark = this.reader.getMark();
/* 1922 */     return (Token)new ScalarToken(chunks.toString(), false, startMark, endMark, DumperOptions.ScalarStyle.createStyle(Character.valueOf(style)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarNonSpaces(boolean doubleQuoted, Mark startMark) {
/* 1930 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1934 */       int length = 0;
/* 1935 */       while (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length), "'\"\\")) {
/* 1936 */         length++;
/*      */       }
/* 1938 */       if (length != 0) {
/* 1939 */         chunks.append(this.reader.prefixForward(length));
/*      */       }
/*      */ 
/*      */       
/* 1943 */       int c = this.reader.peek();
/* 1944 */       if (!doubleQuoted && c == 39 && this.reader.peek(1) == 39) {
/* 1945 */         chunks.append("'");
/* 1946 */         this.reader.forward(2); continue;
/* 1947 */       }  if ((doubleQuoted && c == 39) || (!doubleQuoted && "\"\\".indexOf(c) != -1)) {
/* 1948 */         chunks.appendCodePoint(c);
/* 1949 */         this.reader.forward(); continue;
/* 1950 */       }  if (doubleQuoted && c == 92) {
/* 1951 */         this.reader.forward();
/* 1952 */         c = this.reader.peek();
/* 1953 */         if (!Character.isSupplementaryCodePoint(c) && ESCAPE_REPLACEMENTS.containsKey(Character.valueOf((char)c))) {
/*      */ 
/*      */ 
/*      */           
/* 1957 */           chunks.append(ESCAPE_REPLACEMENTS.get(Character.valueOf((char)c)));
/* 1958 */           this.reader.forward(); continue;
/* 1959 */         }  if (!Character.isSupplementaryCodePoint(c) && ESCAPE_CODES.containsKey(Character.valueOf((char)c))) {
/*      */ 
/*      */           
/* 1962 */           length = ((Integer)ESCAPE_CODES.get(Character.valueOf((char)c))).intValue();
/* 1963 */           this.reader.forward();
/* 1964 */           String hex = this.reader.prefix(length);
/* 1965 */           if (NOT_HEXA.matcher(hex).find()) {
/* 1966 */             throw new ScannerException("while scanning a double-quoted scalar", startMark, "expected escape sequence of " + length + " hexadecimal numbers, but found: " + hex, this.reader
/*      */ 
/*      */                 
/* 1969 */                 .getMark());
/*      */           }
/* 1971 */           int decimal = Integer.parseInt(hex, 16);
/* 1972 */           String unicode = new String(Character.toChars(decimal));
/* 1973 */           chunks.append(unicode);
/* 1974 */           this.reader.forward(length); continue;
/* 1975 */         }  if (scanLineBreak().length() != 0) {
/* 1976 */           chunks.append(scanFlowScalarBreaks(startMark)); continue;
/*      */         } 
/* 1978 */         String s = String.valueOf(Character.toChars(c));
/* 1979 */         throw new ScannerException("while scanning a double-quoted scalar", startMark, "found unknown escape character " + s + "(" + c + ")", this.reader
/*      */             
/* 1981 */             .getMark());
/*      */       }  break;
/*      */     } 
/* 1984 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarSpaces(Mark startMark) {
/* 1991 */     StringBuilder chunks = new StringBuilder();
/* 1992 */     int length = 0;
/*      */ 
/*      */     
/* 1995 */     while (" \t".indexOf(this.reader.peek(length)) != -1) {
/* 1996 */       length++;
/*      */     }
/* 1998 */     String whitespaces = this.reader.prefixForward(length);
/* 1999 */     int c = this.reader.peek();
/* 2000 */     if (c == 0)
/*      */     {
/* 2002 */       throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", this.reader
/* 2003 */           .getMark());
/*      */     }
/*      */     
/* 2006 */     String lineBreak = scanLineBreak();
/* 2007 */     if (lineBreak.length() != 0) {
/* 2008 */       String breaks = scanFlowScalarBreaks(startMark);
/* 2009 */       if (!"\n".equals(lineBreak)) {
/* 2010 */         chunks.append(lineBreak);
/* 2011 */       } else if (breaks.length() == 0) {
/* 2012 */         chunks.append(" ");
/*      */       } 
/* 2014 */       chunks.append(breaks);
/*      */     } else {
/* 2016 */       chunks.append(whitespaces);
/*      */     } 
/* 2018 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String scanFlowScalarBreaks(Mark startMark) {
/* 2023 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 2027 */       String prefix = this.reader.prefix(3);
/* 2028 */       if (("---".equals(prefix) || "...".equals(prefix)) && Constant.NULL_BL_T_LINEBR
/* 2029 */         .has(this.reader.peek(3))) {
/* 2030 */         throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", this.reader
/* 2031 */             .getMark());
/*      */       }
/*      */       
/* 2034 */       while (" \t".indexOf(this.reader.peek()) != -1) {
/* 2035 */         this.reader.forward();
/*      */       }
/*      */ 
/*      */       
/* 2039 */       String lineBreak = scanLineBreak();
/* 2040 */       if (lineBreak.length() != 0) {
/* 2041 */         chunks.append(lineBreak); continue;
/*      */       }  break;
/* 2043 */     }  return chunks.toString();
/*      */   }
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
/*      */   private Token scanPlain() {
/* 2060 */     StringBuilder chunks = new StringBuilder();
/* 2061 */     Mark startMark = this.reader.getMark();
/* 2062 */     Mark endMark = startMark;
/* 2063 */     int indent = this.indent + 1;
/* 2064 */     String spaces = "";
/*      */     
/*      */     do {
/* 2067 */       int length = 0;
/*      */       
/* 2069 */       if (this.reader.peek() == 35) {
/*      */         break;
/*      */       }
/*      */       while (true) {
/* 2073 */         int c = this.reader.peek(length);
/* 2074 */         if (Constant.NULL_BL_T_LINEBR.has(c) || (c == 58 && Constant.NULL_BL_T_LINEBR
/* 2075 */           .has(this.reader.peek(length + 1), (this.flowLevel != 0) ? ",[]{}" : "")) || (this.flowLevel != 0 && ",?[]{}"
/* 2076 */           .indexOf(c) != -1)) {
/*      */           break;
/*      */         }
/* 2079 */         length++;
/*      */       } 
/* 2081 */       if (length == 0) {
/*      */         break;
/*      */       }
/* 2084 */       this.allowSimpleKey = false;
/* 2085 */       chunks.append(spaces);
/* 2086 */       chunks.append(this.reader.prefixForward(length));
/* 2087 */       endMark = this.reader.getMark();
/* 2088 */       spaces = scanPlainSpaces();
/*      */     }
/* 2090 */     while (spaces.length() != 0 && this.reader.peek() != 35 && (this.flowLevel != 0 || this.reader
/* 2091 */       .getColumn() >= indent));
/*      */ 
/*      */ 
/*      */     
/* 2095 */     return (Token)new ScalarToken(chunks.toString(), startMark, endMark, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean atEndOfPlain() {
/* 2102 */     int wsLength = 0;
/* 2103 */     int wsColumn = this.reader.getColumn();
/*      */     
/*      */     int c;
/* 2106 */     while ((c = this.reader.peek(wsLength)) != 0 && Constant.NULL_BL_T_LINEBR.has(c)) {
/* 2107 */       wsLength++;
/* 2108 */       if (!Constant.LINEBR.has(c) && (c != 13 || this.reader.peek(wsLength + 1) != 10) && c != 65279) {
/* 2109 */         wsColumn++; continue;
/*      */       } 
/* 2111 */       wsColumn = 0;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2118 */     if (this.reader.peek(wsLength) == 35 || this.reader.peek(wsLength + 1) == 0 || (this.flowLevel == 0 && wsColumn < this.indent))
/*      */     {
/* 2120 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2125 */     if (this.flowLevel == 0)
/*      */     {
/* 2127 */       for (int extra = 1; (c = this.reader.peek(wsLength + extra)) != 0 && !Constant.NULL_BL_T_LINEBR.has(c); extra++) {
/* 2128 */         if (c == 58 && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(wsLength + extra + 1))) {
/* 2129 */           return true;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 2135 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanPlainSpaces() {
/* 2143 */     int length = 0;
/* 2144 */     while (this.reader.peek(length) == 32 || this.reader.peek(length) == 9) {
/* 2145 */       length++;
/*      */     }
/* 2147 */     String whitespaces = this.reader.prefixForward(length);
/* 2148 */     String lineBreak = scanLineBreak();
/* 2149 */     if (lineBreak.length() != 0) {
/* 2150 */       this.allowSimpleKey = true;
/* 2151 */       String prefix = this.reader.prefix(3);
/* 2152 */       if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR
/* 2153 */         .has(this.reader.peek(3)))) {
/* 2154 */         return "";
/*      */       }
/* 2156 */       if (this.parseComments && atEndOfPlain()) {
/* 2157 */         return "";
/*      */       }
/* 2159 */       StringBuilder breaks = new StringBuilder();
/*      */       while (true) {
/* 2161 */         while (this.reader.peek() == 32) {
/* 2162 */           this.reader.forward();
/*      */         }
/* 2164 */         String lb = scanLineBreak();
/* 2165 */         if (lb.length() != 0) {
/* 2166 */           breaks.append(lb);
/* 2167 */           prefix = this.reader.prefix(3);
/* 2168 */           if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR
/* 2169 */             .has(this.reader.peek(3)))) {
/* 2170 */             return "";
/*      */           }
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 2177 */       if (!"\n".equals(lineBreak))
/* 2178 */         return lineBreak + breaks; 
/* 2179 */       if (breaks.length() == 0) {
/* 2180 */         return " ";
/*      */       }
/* 2182 */       return breaks.toString();
/*      */     } 
/* 2184 */     return whitespaces;
/*      */   }
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
/*      */   private String scanTagHandle(String name, Mark startMark) {
/* 2210 */     int c = this.reader.peek();
/* 2211 */     if (c != 33) {
/* 2212 */       String s = String.valueOf(Character.toChars(c));
/* 2213 */       throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader
/* 2214 */           .getMark());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2219 */     int length = 1;
/* 2220 */     c = this.reader.peek(length);
/* 2221 */     if (c != 32) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2226 */       while (Constant.ALPHA.has(c)) {
/* 2227 */         length++;
/* 2228 */         c = this.reader.peek(length);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2233 */       if (c != 33) {
/* 2234 */         this.reader.forward(length);
/* 2235 */         String s = String.valueOf(Character.toChars(c));
/* 2236 */         throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader
/* 2237 */             .getMark());
/*      */       } 
/* 2239 */       length++;
/*      */     } 
/* 2241 */     String value = this.reader.prefixForward(length);
/* 2242 */     return value;
/*      */   }
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
/*      */   private String scanTagUri(String name, Mark startMark) {
/* 2263 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/* 2267 */     int length = 0;
/* 2268 */     int c = this.reader.peek(length);
/* 2269 */     while (Constant.URI_CHARS.has(c)) {
/* 2270 */       if (c == 37) {
/* 2271 */         chunks.append(this.reader.prefixForward(length));
/* 2272 */         length = 0;
/* 2273 */         chunks.append(scanUriEscapes(name, startMark));
/*      */       } else {
/* 2275 */         length++;
/*      */       } 
/* 2277 */       c = this.reader.peek(length);
/*      */     } 
/*      */ 
/*      */     
/* 2281 */     if (length != 0) {
/* 2282 */       chunks.append(this.reader.prefixForward(length));
/*      */     }
/* 2284 */     if (chunks.length() == 0) {
/*      */       
/* 2286 */       String s = String.valueOf(Character.toChars(c));
/* 2287 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + s + "(" + c + ")", this.reader
/* 2288 */           .getMark());
/*      */     } 
/* 2290 */     return chunks.toString();
/*      */   }
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
/*      */   private String scanUriEscapes(String name, Mark startMark) {
/* 2307 */     int length = 1;
/* 2308 */     while (this.reader.peek(length * 3) == 37) {
/* 2309 */       length++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2315 */     Mark beginningMark = this.reader.getMark();
/* 2316 */     ByteBuffer buff = ByteBuffer.allocate(length);
/* 2317 */     while (this.reader.peek() == 37) {
/* 2318 */       this.reader.forward();
/*      */       try {
/* 2320 */         byte code = (byte)Integer.parseInt(this.reader.prefix(2), 16);
/* 2321 */         buff.put(code);
/* 2322 */       } catch (NumberFormatException nfe) {
/* 2323 */         int c1 = this.reader.peek();
/* 2324 */         String s1 = String.valueOf(Character.toChars(c1));
/* 2325 */         int c2 = this.reader.peek(1);
/* 2326 */         String s2 = String.valueOf(Character.toChars(c2));
/* 2327 */         throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + s1 + "(" + c1 + ") and " + s2 + "(" + c2 + ")", this.reader
/*      */ 
/*      */ 
/*      */             
/* 2331 */             .getMark());
/*      */       } 
/* 2333 */       this.reader.forward(2);
/*      */     } 
/* 2335 */     buff.flip();
/*      */     try {
/* 2337 */       return UriEncoder.decode(buff);
/* 2338 */     } catch (CharacterCodingException e) {
/* 2339 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e
/* 2340 */           .getMessage(), beginningMark);
/*      */     } 
/*      */   }
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
/*      */   private String scanLineBreak() {
/* 2362 */     int c = this.reader.peek();
/* 2363 */     if (c == 13 || c == 10 || c == 133) {
/* 2364 */       if (c == 13 && 10 == this.reader.peek(1)) {
/* 2365 */         this.reader.forward(2);
/*      */       } else {
/* 2367 */         this.reader.forward();
/*      */       } 
/* 2369 */       return "\n";
/* 2370 */     }  if (c == 8232 || c == 8233) {
/* 2371 */       this.reader.forward();
/* 2372 */       return String.valueOf(Character.toChars(c));
/*      */     } 
/* 2374 */     return "";
/*      */   }
/*      */   
/*      */   private List<Token> makeTokenList(Token... tokens) {
/* 2378 */     List<Token> tokenList = new ArrayList<>();
/* 2379 */     for (int ix = 0; ix < tokens.length; ix++) {
/* 2380 */       if (tokens[ix] != null)
/*      */       {
/*      */         
/* 2383 */         if (this.parseComments || !(tokens[ix] instanceof CommentToken))
/*      */         {
/*      */           
/* 2386 */           tokenList.add(tokens[ix]); }  } 
/*      */     } 
/* 2388 */     return tokenList;
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Chomping
/*      */   {
/*      */     private final Boolean value;
/*      */     
/*      */     private final int increment;
/*      */     
/*      */     public Chomping(Boolean value, int increment) {
/* 2399 */       this.value = value;
/* 2400 */       this.increment = increment;
/*      */     }
/*      */     
/*      */     public boolean chompTailIsNotFalse() {
/* 2404 */       return (this.value == null || this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public boolean chompTailIsTrue() {
/* 2408 */       return (this.value != null && this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public int getIncrement() {
/* 2412 */       return this.increment;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\scanner\ScannerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */