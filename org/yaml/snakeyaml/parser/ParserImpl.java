/*     */ package org.yaml.snakeyaml.parser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.comments.CommentType;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.CommentEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*     */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.scanner.Scanner;
/*     */ import org.yaml.snakeyaml.scanner.ScannerImpl;
/*     */ import org.yaml.snakeyaml.tokens.AliasToken;
/*     */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*     */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*     */ import org.yaml.snakeyaml.tokens.CommentToken;
/*     */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*     */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*     */ import org.yaml.snakeyaml.tokens.TagToken;
/*     */ import org.yaml.snakeyaml.tokens.TagTuple;
/*     */ import org.yaml.snakeyaml.tokens.Token;
/*     */ import org.yaml.snakeyaml.util.ArrayStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParserImpl
/*     */   implements Parser
/*     */ {
/* 122 */   private static final Map<String, String> DEFAULT_TAGS = new HashMap<>(); protected final Scanner scanner;
/*     */   static {
/* 124 */     DEFAULT_TAGS.put("!", "!");
/* 125 */     DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
/*     */   }
/*     */ 
/*     */   
/*     */   private Event currentEvent;
/*     */   private final ArrayStack<Production> states;
/*     */   private final ArrayStack<Mark> marks;
/*     */   private Production state;
/*     */   private VersionTagsTuple directives;
/*     */   
/*     */   public ParserImpl(StreamReader reader) {
/* 136 */     this((Scanner)new ScannerImpl(reader));
/*     */   }
/*     */   
/*     */   public ParserImpl(StreamReader reader, boolean parseComments) {
/* 140 */     this((Scanner)(new ScannerImpl(reader)).setParseComments(parseComments));
/*     */   }
/*     */   
/*     */   public ParserImpl(Scanner scanner) {
/* 144 */     this.scanner = scanner;
/* 145 */     this.currentEvent = null;
/* 146 */     this.directives = new VersionTagsTuple(null, new HashMap<>(DEFAULT_TAGS));
/* 147 */     this.states = new ArrayStack(100);
/* 148 */     this.marks = new ArrayStack(10);
/* 149 */     this.state = new ParseStreamStart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkEvent(Event.ID choice) {
/* 156 */     peekEvent();
/* 157 */     return (this.currentEvent != null && this.currentEvent.is(choice));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event peekEvent() {
/* 164 */     if (this.currentEvent == null && 
/* 165 */       this.state != null) {
/* 166 */       this.currentEvent = this.state.produce();
/*     */     }
/*     */     
/* 169 */     return this.currentEvent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event getEvent() {
/* 176 */     peekEvent();
/* 177 */     Event value = this.currentEvent;
/* 178 */     this.currentEvent = null;
/* 179 */     return value;
/*     */   }
/*     */   
/*     */   private CommentEvent produceCommentEvent(CommentToken token) {
/* 183 */     Mark startMark = token.getStartMark();
/* 184 */     Mark endMark = token.getEndMark();
/* 185 */     String value = token.getValue();
/* 186 */     CommentType type = token.getCommentType();
/*     */ 
/*     */ 
/*     */     
/* 190 */     return new CommentEvent(type, value, startMark, endMark);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseStreamStart
/*     */     implements Production
/*     */   {
/*     */     private ParseStreamStart() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 203 */       StreamStartToken token = (StreamStartToken)ParserImpl.this.scanner.getToken();
/* 204 */       StreamStartEvent streamStartEvent = new StreamStartEvent(token.getStartMark(), token.getEndMark());
/*     */       
/* 206 */       ParserImpl.this.state = new ParserImpl.ParseImplicitDocumentStart();
/* 207 */       return (Event)streamStartEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseImplicitDocumentStart implements Production { private ParseImplicitDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/* 214 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment }))
/* 215 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken()); 
/* 216 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.StreamEnd })) {
/* 217 */         ParserImpl.this.directives = new VersionTagsTuple(null, ParserImpl.DEFAULT_TAGS);
/* 218 */         Token token = ParserImpl.this.scanner.peekToken();
/* 219 */         Mark startMark = token.getStartMark();
/* 220 */         Mark endMark = startMark;
/* 221 */         DocumentStartEvent documentStartEvent = new DocumentStartEvent(startMark, endMark, false, null, null);
/*     */         
/* 223 */         ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd());
/* 224 */         ParserImpl.this.state = new ParserImpl.ParseBlockNode();
/* 225 */         return (Event)documentStartEvent;
/*     */       } 
/* 227 */       Production p = new ParserImpl.ParseDocumentStart();
/* 228 */       return p.produce();
/*     */     } }
/*     */   
/*     */   private class ParseDocumentStart implements Production {
/*     */     private ParseDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/* 235 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 236 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       }
/*     */       
/* 239 */       while (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 240 */         ParserImpl.this.scanner.getToken();
/*     */       }
/* 242 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 243 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       }
/*     */ 
/*     */       
/* 247 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.StreamEnd })) {
/* 248 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 249 */         Mark startMark = token1.getStartMark();
/* 250 */         VersionTagsTuple tuple = ParserImpl.this.processDirectives();
/* 251 */         while (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment }))
/*     */         {
/* 253 */           ParserImpl.this.scanner.getToken();
/*     */         }
/* 255 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.StreamEnd })) {
/* 256 */           if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentStart })) {
/* 257 */             throw new ParserException(null, null, "expected '<document start>', but found '" + ParserImpl.this.scanner
/* 258 */                 .peekToken().getTokenId() + "'", ParserImpl.this.scanner.peekToken().getStartMark());
/*     */           }
/* 260 */           token1 = ParserImpl.this.scanner.getToken();
/* 261 */           Mark endMark = token1.getEndMark();
/*     */           
/* 263 */           DocumentStartEvent documentStartEvent = new DocumentStartEvent(startMark, endMark, true, tuple.getVersion(), tuple.getTags());
/* 264 */           ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd());
/* 265 */           ParserImpl.this.state = new ParserImpl.ParseDocumentContent();
/* 266 */           return (Event)documentStartEvent;
/*     */         } 
/*     */       } 
/*     */       
/* 270 */       StreamEndToken token = (StreamEndToken)ParserImpl.this.scanner.getToken();
/* 271 */       StreamEndEvent streamEndEvent = new StreamEndEvent(token.getStartMark(), token.getEndMark());
/* 272 */       if (!ParserImpl.this.states.isEmpty()) {
/* 273 */         throw new YAMLException("Unexpected end of stream. States left: " + ParserImpl.this.states);
/*     */       }
/* 275 */       if (!ParserImpl.this.marks.isEmpty()) {
/* 276 */         throw new YAMLException("Unexpected end of stream. Marks left: " + ParserImpl.this.marks);
/*     */       }
/* 278 */       ParserImpl.this.state = null;
/*     */       
/* 280 */       return (Event)streamEndEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseDocumentEnd implements Production { private ParseDocumentEnd() {}
/*     */     
/*     */     public Event produce() {
/* 287 */       Token token = ParserImpl.this.scanner.peekToken();
/* 288 */       Mark startMark = token.getStartMark();
/* 289 */       Mark endMark = startMark;
/* 290 */       boolean explicit = false;
/* 291 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 292 */         token = ParserImpl.this.scanner.getToken();
/* 293 */         endMark = token.getEndMark();
/* 294 */         explicit = true;
/*     */       } 
/* 296 */       DocumentEndEvent documentEndEvent = new DocumentEndEvent(startMark, endMark, explicit);
/*     */       
/* 298 */       ParserImpl.this.state = new ParserImpl.ParseDocumentStart();
/* 299 */       return (Event)documentEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseDocumentContent implements Production { private ParseDocumentContent() {}
/*     */     
/*     */     public Event produce() {
/* 305 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 306 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       }
/*     */       
/* 309 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.DocumentEnd, Token.ID.StreamEnd })) {
/*     */         
/* 311 */         Event event = ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
/* 312 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 313 */         return event;
/*     */       } 
/* 315 */       Production p = new ParserImpl.ParseBlockNode();
/* 316 */       return p.produce();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VersionTagsTuple processDirectives() {
/* 323 */     DumperOptions.Version yamlVersion = null;
/* 324 */     HashMap<String, String> tagHandles = new HashMap<>();
/* 325 */     while (this.scanner.checkToken(new Token.ID[] { Token.ID.Directive })) {
/*     */       
/* 327 */       DirectiveToken token = (DirectiveToken)this.scanner.getToken();
/* 328 */       if (token.getName().equals("YAML")) {
/* 329 */         if (yamlVersion != null) {
/* 330 */           throw new ParserException(null, null, "found duplicate YAML directive", token
/* 331 */               .getStartMark());
/*     */         }
/* 333 */         List<Integer> value = token.getValue();
/* 334 */         Integer major = value.get(0);
/* 335 */         if (major.intValue() != 1) {
/* 336 */           throw new ParserException(null, null, "found incompatible YAML document (version 1.* is required)", token
/*     */               
/* 338 */               .getStartMark());
/*     */         }
/* 340 */         Integer minor = value.get(1);
/* 341 */         switch (minor.intValue()) {
/*     */           case 0:
/* 343 */             yamlVersion = DumperOptions.Version.V1_0;
/*     */             continue;
/*     */         } 
/*     */         
/* 347 */         yamlVersion = DumperOptions.Version.V1_1;
/*     */         continue;
/*     */       } 
/* 350 */       if (token.getName().equals("TAG")) {
/* 351 */         List<String> value = token.getValue();
/* 352 */         String handle = value.get(0);
/* 353 */         String prefix = value.get(1);
/* 354 */         if (tagHandles.containsKey(handle)) {
/* 355 */           throw new ParserException(null, null, "duplicate tag handle " + handle, token
/* 356 */               .getStartMark());
/*     */         }
/* 358 */         tagHandles.put(handle, prefix);
/*     */       } 
/*     */     } 
/* 361 */     if (yamlVersion != null || !tagHandles.isEmpty()) {
/*     */       
/* 363 */       for (String key : DEFAULT_TAGS.keySet()) {
/*     */         
/* 365 */         if (!tagHandles.containsKey(key)) {
/* 366 */           tagHandles.put(key, DEFAULT_TAGS.get(key));
/*     */         }
/*     */       } 
/* 369 */       this.directives = new VersionTagsTuple(yamlVersion, tagHandles);
/*     */     } 
/* 371 */     return this.directives;
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
/*     */   private class ParseBlockNode
/*     */     implements Production
/*     */   {
/*     */     private ParseBlockNode() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 396 */       return ParserImpl.this.parseNode(true, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private Event parseFlowNode() {
/* 401 */     return parseNode(false, false);
/*     */   }
/*     */   
/*     */   private Event parseBlockNodeOrIndentlessSequence() {
/* 405 */     return parseNode(true, true);
/*     */   }
/*     */   
/*     */   private Event parseNode(boolean block, boolean indentlessSequence) {
/*     */     ScalarEvent scalarEvent;
/* 410 */     Mark startMark = null;
/* 411 */     Mark endMark = null;
/* 412 */     Mark tagMark = null;
/* 413 */     if (this.scanner.checkToken(new Token.ID[] { Token.ID.Alias })) {
/* 414 */       AliasToken token = (AliasToken)this.scanner.getToken();
/* 415 */       AliasEvent aliasEvent = new AliasEvent(token.getValue(), token.getStartMark(), token.getEndMark());
/* 416 */       this.state = (Production)this.states.pop();
/*     */     } else {
/* 418 */       String anchor = null;
/* 419 */       TagTuple tagTokenTag = null;
/* 420 */       if (this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 421 */         AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 422 */         startMark = token.getStartMark();
/* 423 */         endMark = token.getEndMark();
/* 424 */         anchor = token.getValue();
/* 425 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag })) {
/* 426 */           TagToken tagToken = (TagToken)this.scanner.getToken();
/* 427 */           tagMark = tagToken.getStartMark();
/* 428 */           endMark = tagToken.getEndMark();
/* 429 */           tagTokenTag = tagToken.getValue();
/*     */         } 
/*     */       } else {
/* 432 */         TagToken tagToken = (TagToken)this.scanner.getToken();
/* 433 */         startMark = tagToken.getStartMark();
/* 434 */         tagMark = startMark;
/* 435 */         endMark = tagToken.getEndMark();
/* 436 */         tagTokenTag = tagToken.getValue();
/* 437 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag }) && this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 438 */           AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 439 */           endMark = token.getEndMark();
/* 440 */           anchor = token.getValue();
/*     */         } 
/*     */       } 
/* 443 */       String tag = null;
/* 444 */       if (tagTokenTag != null) {
/* 445 */         String handle = tagTokenTag.getHandle();
/* 446 */         String suffix = tagTokenTag.getSuffix();
/* 447 */         if (handle != null) {
/* 448 */           if (!this.directives.getTags().containsKey(handle)) {
/* 449 */             throw new ParserException("while parsing a node", startMark, "found undefined tag handle " + handle, tagMark);
/*     */           }
/*     */           
/* 452 */           tag = (String)this.directives.getTags().get(handle) + suffix;
/*     */         } else {
/* 454 */           tag = suffix;
/*     */         } 
/*     */       } 
/* 457 */       if (startMark == null) {
/* 458 */         startMark = this.scanner.peekToken().getStartMark();
/* 459 */         endMark = startMark;
/*     */       } 
/* 461 */       Event event = null;
/* 462 */       boolean implicit = (tag == null || tag.equals("!"));
/* 463 */       if (indentlessSequence && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 464 */         endMark = this.scanner.peekToken().getEndMark();
/* 465 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 467 */         this.state = new ParseIndentlessSequenceEntryKey();
/*     */       }
/* 469 */       else if (this.scanner.checkToken(new Token.ID[] { Token.ID.Scalar })) {
/* 470 */         ImplicitTuple implicitValues; ScalarToken token = (ScalarToken)this.scanner.getToken();
/* 471 */         endMark = token.getEndMark();
/*     */         
/* 473 */         if ((token.getPlain() && tag == null) || "!".equals(tag)) {
/* 474 */           implicitValues = new ImplicitTuple(true, false);
/* 475 */         } else if (tag == null) {
/* 476 */           implicitValues = new ImplicitTuple(false, true);
/*     */         } else {
/* 478 */           implicitValues = new ImplicitTuple(false, false);
/*     */         } 
/*     */         
/* 481 */         scalarEvent = new ScalarEvent(anchor, tag, implicitValues, token.getValue(), startMark, endMark, token.getStyle());
/* 482 */         this.state = (Production)this.states.pop();
/* 483 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 484 */         CommentEvent commentEvent = produceCommentEvent((CommentToken)this.scanner.getToken());
/* 485 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceStart })) {
/* 486 */         endMark = this.scanner.peekToken().getEndMark();
/* 487 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.FLOW);
/*     */         
/* 489 */         this.state = new ParseFlowSequenceFirstEntry();
/* 490 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingStart })) {
/* 491 */         endMark = this.scanner.peekToken().getEndMark();
/* 492 */         MappingStartEvent mappingStartEvent = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.FLOW);
/*     */         
/* 494 */         this.state = new ParseFlowMappingFirstKey();
/* 495 */       } else if (block && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockSequenceStart })) {
/* 496 */         endMark = this.scanner.peekToken().getStartMark();
/* 497 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 499 */         this.state = new ParseBlockSequenceFirstEntry();
/* 500 */       } else if (block && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockMappingStart })) {
/* 501 */         endMark = this.scanner.peekToken().getStartMark();
/* 502 */         MappingStartEvent mappingStartEvent = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 504 */         this.state = new ParseBlockMappingFirstKey();
/* 505 */       } else if (anchor != null || tag != null) {
/*     */ 
/*     */         
/* 508 */         scalarEvent = new ScalarEvent(anchor, tag, new ImplicitTuple(implicit, false), "", startMark, endMark, DumperOptions.ScalarStyle.PLAIN);
/*     */         
/* 510 */         this.state = (Production)this.states.pop();
/*     */       } else {
/*     */         String node;
/* 513 */         if (block) {
/* 514 */           node = "block";
/*     */         } else {
/* 516 */           node = "flow";
/*     */         } 
/* 518 */         Token token = this.scanner.peekToken();
/* 519 */         throw new ParserException("while parsing a " + node + " node", startMark, "expected the node content, but found '" + token
/* 520 */             .getTokenId() + "'", token
/* 521 */             .getStartMark());
/*     */       } 
/*     */     } 
/*     */     
/* 525 */     return (Event)scalarEvent;
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceFirstEntry
/*     */     implements Production {
/*     */     private ParseBlockSequenceFirstEntry() {}
/*     */     
/*     */     public Event produce() {
/* 533 */       Token token = ParserImpl.this.scanner.getToken();
/* 534 */       ParserImpl.this.marks.push(token.getStartMark());
/* 535 */       return (new ParserImpl.ParseBlockSequenceEntryKey()).produce();
/*     */     } }
/*     */   
/*     */   private class ParseBlockSequenceEntryKey implements Production { private ParseBlockSequenceEntryKey() {}
/*     */     
/*     */     public Event produce() {
/* 541 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 542 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       }
/* 544 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 545 */         BlockEntryToken blockEntryToken = (BlockEntryToken)ParserImpl.this.scanner.getToken();
/* 546 */         return (new ParserImpl.ParseBlockSequenceEntryValue(blockEntryToken)).produce();
/*     */       } 
/* 548 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 549 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 550 */         throw new ParserException("while parsing a block collection", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found '" + token1
/* 551 */             .getTokenId() + "'", token1
/* 552 */             .getStartMark());
/*     */       } 
/* 554 */       Token token = ParserImpl.this.scanner.getToken();
/* 555 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 556 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 557 */       ParserImpl.this.marks.pop();
/* 558 */       return (Event)sequenceEndEvent;
/*     */     } }
/*     */ 
/*     */   
/*     */   private class ParseBlockSequenceEntryValue implements Production {
/*     */     BlockEntryToken token;
/*     */     
/*     */     public ParseBlockSequenceEntryValue(BlockEntryToken token) {
/* 566 */       this.token = token;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 570 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 571 */         ParserImpl.this.state = new ParseBlockSequenceEntryValue(this.token);
/* 572 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 574 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.BlockEnd })) {
/* 575 */         ParserImpl.this.states.push(new ParserImpl.ParseBlockSequenceEntryKey());
/* 576 */         return (new ParserImpl.ParseBlockNode()).produce();
/*     */       } 
/* 578 */       ParserImpl.this.state = new ParserImpl.ParseBlockSequenceEntryKey();
/* 579 */       return ParserImpl.this.processEmptyScalar(this.token.getEndMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseIndentlessSequenceEntryKey
/*     */     implements Production {
/*     */     private ParseIndentlessSequenceEntryKey() {}
/*     */     
/*     */     public Event produce() {
/* 588 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 589 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       }
/* 591 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 592 */         BlockEntryToken blockEntryToken = (BlockEntryToken)ParserImpl.this.scanner.getToken();
/* 593 */         return (new ParserImpl.ParseIndentlessSequenceEntryValue(blockEntryToken)).produce();
/*     */       } 
/* 595 */       Token token = ParserImpl.this.scanner.peekToken();
/* 596 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 597 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 598 */       return (Event)sequenceEndEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseIndentlessSequenceEntryValue implements Production {
/*     */     BlockEntryToken token;
/*     */     
/*     */     public ParseIndentlessSequenceEntryValue(BlockEntryToken token) {
/* 606 */       this.token = token;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 610 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 611 */         ParserImpl.this.state = new ParseIndentlessSequenceEntryValue(this.token);
/* 612 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       } 
/* 614 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/*     */         
/* 616 */         ParserImpl.this.states.push(new ParserImpl.ParseIndentlessSequenceEntryKey());
/* 617 */         return (new ParserImpl.ParseBlockNode()).produce();
/*     */       } 
/* 619 */       ParserImpl.this.state = new ParserImpl.ParseIndentlessSequenceEntryKey();
/* 620 */       return ParserImpl.this.processEmptyScalar(this.token.getEndMark());
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockMappingFirstKey implements Production { private ParseBlockMappingFirstKey() {}
/*     */     
/*     */     public Event produce() {
/* 627 */       Token token = ParserImpl.this.scanner.getToken();
/* 628 */       ParserImpl.this.marks.push(token.getStartMark());
/* 629 */       return (new ParserImpl.ParseBlockMappingKey()).produce();
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingKey implements Production { private ParseBlockMappingKey() {}
/*     */     
/*     */     public Event produce() {
/* 635 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 636 */         return (Event)ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/*     */       }
/* 638 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 639 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 640 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 641 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingValue());
/* 642 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         } 
/* 644 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingValue();
/* 645 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 648 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 649 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 650 */         throw new ParserException("while parsing a block mapping", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found '" + token1
/* 651 */             .getTokenId() + "'", token1
/* 652 */             .getStartMark());
/*     */       } 
/* 654 */       Token token = ParserImpl.this.scanner.getToken();
/* 655 */       MappingEndEvent mappingEndEvent = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 656 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 657 */       ParserImpl.this.marks.pop();
/* 658 */       return (Event)mappingEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingValue implements Production { private ParseBlockMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 664 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 665 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 666 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 667 */           ParserImpl.this.state = new ParserImpl.ParseBlockMappingValueComment();
/* 668 */           return ParserImpl.this.state.produce();
/* 669 */         }  if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 670 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey());
/* 671 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         } 
/* 673 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey();
/* 674 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/* 676 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Scalar })) {
/* 677 */         ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey());
/* 678 */         return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */       } 
/* 680 */       ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey();
/* 681 */       Token token = ParserImpl.this.scanner.peekToken();
/* 682 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */ 
/*     */   
/*     */   private class ParseBlockMappingValueComment implements Production {
/* 687 */     List<CommentToken> tokens = new LinkedList<>();
/*     */     
/*     */     public Event produce() {
/* 690 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 691 */         this.tokens.add((CommentToken)ParserImpl.this.scanner.getToken());
/* 692 */         return produce();
/* 693 */       }  if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 694 */         if (!this.tokens.isEmpty()) {
/* 695 */           return (Event)ParserImpl.this.produceCommentEvent(this.tokens.remove(0));
/*     */         }
/* 697 */         ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey());
/* 698 */         return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */       } 
/* 700 */       ParserImpl.this.state = new ParserImpl.ParseBlockMappingValueCommentList(this.tokens);
/* 701 */       return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
/*     */     }
/*     */     
/*     */     private ParseBlockMappingValueComment() {} }
/*     */   
/*     */   private class ParseBlockMappingValueCommentList implements Production {
/*     */     List<CommentToken> tokens;
/*     */     
/*     */     public ParseBlockMappingValueCommentList(List<CommentToken> tokens) {
/* 710 */       this.tokens = tokens;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 714 */       if (!this.tokens.isEmpty()) {
/* 715 */         return (Event)ParserImpl.this.produceCommentEvent(this.tokens.remove(0));
/*     */       }
/* 717 */       return (new ParserImpl.ParseBlockMappingKey()).produce();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseFlowSequenceFirstEntry
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowSequenceFirstEntry() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 736 */       Token token = ParserImpl.this.scanner.getToken();
/* 737 */       ParserImpl.this.marks.push(token.getStartMark());
/* 738 */       return (new ParserImpl.ParseFlowSequenceEntry(true)).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntry implements Production {
/*     */     private boolean first = false;
/*     */     
/*     */     public ParseFlowSequenceEntry(boolean first) {
/* 746 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 750 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 751 */         if (!this.first) {
/* 752 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 753 */             ParserImpl.this.scanner.getToken();
/*     */           } else {
/* 755 */             Token token1 = ParserImpl.this.scanner.peekToken();
/* 756 */             throw new ParserException("while parsing a flow sequence", (Mark)ParserImpl.this.marks.pop(), "expected ',' or ']', but got " + token1
/* 757 */                 .getTokenId(), token1
/* 758 */                 .getStartMark());
/*     */           } 
/*     */         }
/* 761 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 762 */           Token token1 = ParserImpl.this.scanner.peekToken();
/*     */           
/* 764 */           MappingStartEvent mappingStartEvent = new MappingStartEvent(null, null, true, token1.getStartMark(), token1.getEndMark(), DumperOptions.FlowStyle.FLOW);
/* 765 */           ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingKey();
/* 766 */           return (Event)mappingStartEvent;
/* 767 */         }  if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 768 */           ParserImpl.this.states.push(new ParseFlowSequenceEntry(false));
/* 769 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/*     */       } 
/* 772 */       Token token = ParserImpl.this.scanner.getToken();
/* 773 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 774 */       ParserImpl.this.marks.pop();
/* 775 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 776 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/*     */       } else {
/* 778 */         ParserImpl.this.state = new ParserImpl.ParseFlowEndComment();
/*     */       } 
/* 780 */       return (Event)sequenceEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseFlowEndComment implements Production { private ParseFlowEndComment() {}
/*     */     
/*     */     public Event produce() {
/* 786 */       CommentEvent commentEvent = ParserImpl.this.produceCommentEvent((CommentToken)ParserImpl.this.scanner.getToken());
/* 787 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 788 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/*     */       }
/* 790 */       return (Event)commentEvent;
/*     */     } }
/*     */ 
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingKey implements Production { public Event produce() {
/* 795 */       Token token = ParserImpl.this.scanner.getToken();
/* 796 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 797 */         ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingValue());
/* 798 */         return ParserImpl.this.parseFlowNode();
/*     */       } 
/* 800 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingValue();
/* 801 */       return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */     }
/*     */     private ParseFlowSequenceEntryMappingKey() {} }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingValue implements Production { private ParseFlowSequenceEntryMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 808 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 809 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 810 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 811 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingEnd());
/* 812 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/* 814 */         ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd();
/* 815 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 818 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd();
/* 819 */       Token token = ParserImpl.this.scanner.peekToken();
/* 820 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingEnd implements Production {
/*     */     private ParseFlowSequenceEntryMappingEnd() {}
/*     */     
/*     */     public Event produce() {
/* 827 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntry(false);
/* 828 */       Token token = ParserImpl.this.scanner.peekToken();
/* 829 */       return (Event)new MappingEndEvent(token.getStartMark(), token.getEndMark());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseFlowMappingFirstKey
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowMappingFirstKey() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 844 */       Token token = ParserImpl.this.scanner.getToken();
/* 845 */       ParserImpl.this.marks.push(token.getStartMark());
/* 846 */       return (new ParserImpl.ParseFlowMappingKey(true)).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowMappingKey implements Production {
/*     */     private boolean first = false;
/*     */     
/*     */     public ParseFlowMappingKey(boolean first) {
/* 854 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 858 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 859 */         if (!this.first) {
/* 860 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 861 */             ParserImpl.this.scanner.getToken();
/*     */           } else {
/* 863 */             Token token1 = ParserImpl.this.scanner.peekToken();
/* 864 */             throw new ParserException("while parsing a flow mapping", (Mark)ParserImpl.this.marks.pop(), "expected ',' or '}', but got " + token1
/* 865 */                 .getTokenId(), token1
/* 866 */                 .getStartMark());
/*     */           } 
/*     */         }
/* 869 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 870 */           Token token1 = ParserImpl.this.scanner.getToken();
/* 871 */           if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/*     */             
/* 873 */             ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingValue());
/* 874 */             return ParserImpl.this.parseFlowNode();
/*     */           } 
/* 876 */           ParserImpl.this.state = new ParserImpl.ParseFlowMappingValue();
/* 877 */           return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */         } 
/* 879 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 880 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingEmptyValue());
/* 881 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/*     */       } 
/* 884 */       Token token = ParserImpl.this.scanner.getToken();
/* 885 */       MappingEndEvent mappingEndEvent = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 886 */       ParserImpl.this.marks.pop();
/* 887 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Comment })) {
/* 888 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/*     */       } else {
/* 890 */         ParserImpl.this.state = new ParserImpl.ParseFlowEndComment();
/*     */       } 
/* 892 */       return (Event)mappingEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseFlowMappingValue implements Production { private ParseFlowMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 898 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 899 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 900 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/* 901 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingKey(false));
/* 902 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/* 904 */         ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 905 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 908 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 909 */       Token token = ParserImpl.this.scanner.peekToken();
/* 910 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */   
/*     */   private class ParseFlowMappingEmptyValue implements Production {
/*     */     private ParseFlowMappingEmptyValue() {}
/*     */     
/*     */     public Event produce() {
/* 917 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 918 */       return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
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
/*     */   private Event processEmptyScalar(Mark mark) {
/* 931 */     return (Event)new ScalarEvent(null, null, new ImplicitTuple(true, false), "", mark, mark, DumperOptions.ScalarStyle.PLAIN);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\parser\ParserImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */