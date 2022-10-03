/*     */ package org.mozilla.javascript.tools.idswitch;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.mozilla.javascript.EvaluatorException;
/*     */ import org.mozilla.javascript.tools.ToolErrorReporter;
/*     */ 
/*     */ public class Main {
/*     */   private static final String SWITCH_TAG_STR = "string_id_map";
/*     */   private static final String GENERATED_TAG_STR = "generated";
/*     */   private static final String STRING_TAG_STR = "string";
/*     */   private static final int NORMAL_LINE = 0;
/*     */   private static final int SWITCH_TAG = 1;
/*     */   private static final int GENERATED_TAG = 2;
/*     */   private static final int STRING_TAG = 3;
/*  27 */   private final List<IdValuePair> all_pairs = new ArrayList<IdValuePair>();
/*     */   
/*     */   private ToolErrorReporter R;
/*     */   
/*     */   private CodePrinter P;
/*     */   
/*     */   private FileBody body;
/*     */   private String source_file;
/*     */   private int tag_definition_end;
/*     */   private int tag_value_start;
/*     */   private int tag_value_end;
/*     */   
/*     */   private static boolean is_value_type(int id) {
/*  40 */     if (id == 3) return true; 
/*  41 */     return false;
/*     */   }
/*     */   
/*     */   private static String tag_name(int id) {
/*  45 */     switch (id) { case 1:
/*  46 */         return "string_id_map";
/*  47 */       case -1: return "/string_id_map";
/*  48 */       case 2: return "generated";
/*  49 */       case -2: return "/generated"; }
/*     */     
/*  51 */     return "";
/*     */   }
/*     */   void process_file(String file_path) throws IOException {
/*     */     InputStream is;
/*  55 */     this.source_file = file_path;
/*     */     
/*  57 */     this.body = new FileBody();
/*     */ 
/*     */     
/*  60 */     if (file_path.equals("-")) {
/*  61 */       is = System.in;
/*     */     } else {
/*     */       
/*  64 */       is = new FileInputStream(file_path);
/*     */     } 
/*     */     try {
/*  67 */       Reader r = new InputStreamReader(is, "ASCII");
/*  68 */       this.body.readData(r);
/*     */     } finally {
/*  70 */       is.close();
/*     */     } 
/*  72 */     process_file();
/*     */     
/*  74 */     if (this.body.wasModified()) {
/*     */       OutputStream os;
/*  76 */       if (file_path.equals("-")) {
/*  77 */         os = System.out;
/*     */       } else {
/*     */         
/*  80 */         os = new FileOutputStream(file_path);
/*     */       } 
/*     */       
/*     */       try {
/*  84 */         Writer w = new OutputStreamWriter(os);
/*  85 */         this.body.writeData(w);
/*  86 */         w.flush();
/*     */       } finally {
/*  88 */         os.close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void process_file() {
/*  93 */     int cur_state = 0;
/*  94 */     char[] buffer = this.body.getBuffer();
/*     */     
/*  96 */     int generated_begin = -1, generated_end = -1;
/*  97 */     int time_stamp_begin = -1, time_stamp_end = -1;
/*     */     
/*  99 */     this.body.startLineLoop();
/* 100 */     while (this.body.nextLine()) {
/* 101 */       int begin = this.body.getLineBegin();
/* 102 */       int end = this.body.getLineEnd();
/*     */       
/* 104 */       int tag_id = extract_line_tag_id(buffer, begin, end);
/* 105 */       boolean bad_tag = false;
/* 106 */       switch (cur_state) {
/*     */         case 0:
/* 108 */           if (tag_id == 1) {
/* 109 */             cur_state = 1;
/* 110 */             this.all_pairs.clear();
/* 111 */             generated_begin = -1; break;
/*     */           } 
/* 113 */           if (tag_id == -1) {
/* 114 */             bad_tag = true;
/*     */           }
/*     */           break;
/*     */         case 1:
/* 118 */           if (tag_id == 0) {
/* 119 */             look_for_id_definitions(buffer, begin, end, false); break;
/*     */           } 
/* 121 */           if (tag_id == 3) {
/* 122 */             look_for_id_definitions(buffer, begin, end, true); break;
/*     */           } 
/* 124 */           if (tag_id == 2) {
/* 125 */             if (generated_begin >= 0) { bad_tag = true; break; }
/*     */             
/* 127 */             cur_state = 2;
/* 128 */             time_stamp_begin = this.tag_definition_end;
/* 129 */             time_stamp_end = end;
/*     */             break;
/*     */           } 
/* 132 */           if (tag_id == -1) {
/* 133 */             cur_state = 0;
/* 134 */             if (generated_begin >= 0 && !this.all_pairs.isEmpty()) {
/* 135 */               generate_java_code();
/* 136 */               String code = this.P.toString();
/* 137 */               boolean different = this.body.setReplacement(generated_begin, generated_end, code);
/*     */               
/* 139 */               if (different) {
/* 140 */                 String stamp = get_time_stamp();
/* 141 */                 this.body.setReplacement(time_stamp_begin, time_stamp_end, stamp);
/*     */               } 
/*     */             } 
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 149 */           bad_tag = true;
/*     */           break;
/*     */         
/*     */         case 2:
/* 153 */           if (tag_id == 0) {
/* 154 */             if (generated_begin < 0) generated_begin = begin;  break;
/*     */           } 
/* 156 */           if (tag_id == -2) {
/* 157 */             if (generated_begin < 0) generated_begin = begin; 
/* 158 */             cur_state = 1;
/* 159 */             generated_end = begin;
/*     */             break;
/*     */           } 
/* 162 */           bad_tag = true;
/*     */           break;
/*     */       } 
/*     */       
/* 166 */       if (bad_tag) {
/* 167 */         String text = ToolErrorReporter.getMessage("msg.idswitch.bad_tag_order", tag_name(tag_id));
/*     */         
/* 169 */         throw this.R.runtimeError(text, this.source_file, this.body.getLineNumber(), null, 0);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 174 */     if (cur_state != 0) {
/* 175 */       String text = ToolErrorReporter.getMessage("msg.idswitch.file_end_in_switch", tag_name(cur_state));
/*     */       
/* 177 */       throw this.R.runtimeError(text, this.source_file, this.body.getLineNumber(), null, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String get_time_stamp() {
/* 183 */     SimpleDateFormat f = new SimpleDateFormat(" 'Last update:' yyyy-MM-dd HH:mm:ss z");
/*     */     
/* 185 */     return f.format(new Date());
/*     */   }
/*     */ 
/*     */   
/*     */   private void generate_java_code() {
/* 190 */     this.P.clear();
/*     */     
/* 192 */     IdValuePair[] pairs = new IdValuePair[this.all_pairs.size()];
/* 193 */     this.all_pairs.toArray(pairs);
/*     */     
/* 195 */     SwitchGenerator g = new SwitchGenerator();
/* 196 */     g.char_tail_test_threshold = 2;
/* 197 */     g.setReporter(this.R);
/* 198 */     g.setCodePrinter(this.P);
/*     */     
/* 200 */     g.generateSwitch(pairs, "0");
/*     */   }
/*     */   
/*     */   private int extract_line_tag_id(char[] array, int cursor, int end) {
/* 204 */     int id = 0;
/* 205 */     cursor = skip_white_space(array, cursor, end);
/* 206 */     int after_leading_white_space = cursor;
/* 207 */     cursor = look_for_slash_slash(array, cursor, end);
/* 208 */     if (cursor != end) {
/* 209 */       boolean at_line_start = (after_leading_white_space + 2 == cursor);
/* 210 */       cursor = skip_white_space(array, cursor, end);
/* 211 */       if (cursor != end && array[cursor] == '#') {
/* 212 */         cursor++;
/*     */         
/* 214 */         boolean end_tag = false;
/* 215 */         if (cursor != end && array[cursor] == '/') {
/* 216 */           cursor++; end_tag = true;
/*     */         } 
/*     */         
/* 219 */         int tag_start = cursor;
/*     */         
/* 221 */         for (; cursor != end; cursor++) {
/* 222 */           int c = array[cursor];
/* 223 */           if (c == 35 || c == 61 || is_white_space(c))
/*     */             break; 
/*     */         } 
/* 226 */         if (cursor != end) {
/* 227 */           int tag_end = cursor;
/* 228 */           cursor = skip_white_space(array, cursor, end);
/* 229 */           if (cursor != end) {
/* 230 */             int c = array[cursor];
/* 231 */             if (c == 61 || c == 35) {
/* 232 */               id = get_tag_id(array, tag_start, tag_end, at_line_start);
/*     */               
/* 234 */               if (id != 0) {
/* 235 */                 String bad = null;
/* 236 */                 if (c == 35) {
/* 237 */                   if (end_tag) {
/* 238 */                     id = -id;
/* 239 */                     if (is_value_type(id)) {
/* 240 */                       bad = "msg.idswitch.no_end_usage";
/*     */                     }
/*     */                   } 
/* 243 */                   this.tag_definition_end = cursor + 1;
/*     */                 } else {
/*     */                   
/* 246 */                   if (end_tag) {
/* 247 */                     bad = "msg.idswitch.no_end_with_value";
/*     */                   }
/* 249 */                   else if (!is_value_type(id)) {
/* 250 */                     bad = "msg.idswitch.no_value_allowed";
/*     */                   } 
/* 252 */                   id = extract_tag_value(array, cursor + 1, end, id);
/*     */                 } 
/*     */                 
/* 255 */                 if (bad != null) {
/* 256 */                   String s = ToolErrorReporter.getMessage(bad, tag_name(id));
/*     */                   
/* 258 */                   throw this.R.runtimeError(s, this.source_file, this.body.getLineNumber(), null, 0);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 268 */     return id;
/*     */   }
/*     */ 
/*     */   
/*     */   private int look_for_slash_slash(char[] array, int cursor, int end) {
/* 273 */     while (cursor + 2 <= end) {
/* 274 */       int c = array[cursor++];
/* 275 */       if (c == 47) {
/* 276 */         c = array[cursor++];
/* 277 */         if (c == 47) {
/* 278 */           return cursor;
/*     */         }
/*     */       } 
/*     */     } 
/* 282 */     return end;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int extract_tag_value(char[] array, int cursor, int end, int id) {
/* 288 */     boolean found = false;
/* 289 */     cursor = skip_white_space(array, cursor, end);
/* 290 */     if (cursor != end) {
/* 291 */       int value_start = cursor;
/* 292 */       int value_end = cursor;
/* 293 */       while (cursor != end) {
/* 294 */         int c = array[cursor];
/* 295 */         if (is_white_space(c)) {
/* 296 */           int after_space = skip_white_space(array, cursor + 1, end);
/* 297 */           if (after_space != end && array[after_space] == '#') {
/* 298 */             value_end = cursor;
/* 299 */             cursor = after_space;
/*     */             break;
/*     */           } 
/* 302 */           cursor = after_space + 1; continue;
/*     */         } 
/* 304 */         if (c == 35) {
/* 305 */           value_end = cursor;
/*     */           
/*     */           break;
/*     */         } 
/* 309 */         cursor++;
/*     */       } 
/*     */       
/* 312 */       if (cursor != end) {
/*     */         
/* 314 */         found = true;
/* 315 */         this.tag_value_start = value_start;
/* 316 */         this.tag_value_end = value_end;
/* 317 */         this.tag_definition_end = cursor + 1;
/*     */       } 
/*     */     } 
/* 320 */     return found ? id : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int get_tag_id(char[] array, int begin, int end, boolean at_line_start) {
/* 326 */     if (at_line_start) {
/* 327 */       if (equals("string_id_map", array, begin, end)) {
/* 328 */         return 1;
/*     */       }
/* 330 */       if (equals("generated", array, begin, end)) {
/* 331 */         return 2;
/*     */       }
/*     */     } 
/* 334 */     if (equals("string", array, begin, end)) {
/* 335 */       return 3;
/*     */     }
/* 337 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void look_for_id_definitions(char[] array, int begin, int end, boolean use_tag_value_as_string) {
/* 346 */     int cursor = begin;
/*     */     
/* 348 */     cursor = skip_white_space(array, cursor, end);
/* 349 */     int id_start = cursor;
/* 350 */     int name_start = skip_matched_prefix("Id_", array, cursor, end);
/* 351 */     if (name_start >= 0) {
/*     */       
/* 353 */       cursor = name_start;
/* 354 */       cursor = skip_name_char(array, cursor, end);
/* 355 */       int name_end = cursor;
/* 356 */       if (name_start != name_end) {
/* 357 */         cursor = skip_white_space(array, cursor, end);
/* 358 */         if (cursor != end && 
/* 359 */           array[cursor] == '=') {
/* 360 */           int id_end = name_end;
/* 361 */           if (use_tag_value_as_string) {
/* 362 */             name_start = this.tag_value_start;
/* 363 */             name_end = this.tag_value_end;
/*     */           } 
/*     */           
/* 366 */           add_id(array, id_start, id_end, name_start, name_end);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void add_id(char[] array, int id_start, int id_end, int name_start, int name_end) {
/* 376 */     String name = new String(array, name_start, name_end - name_start);
/* 377 */     String value = new String(array, id_start, id_end - id_start);
/*     */     
/* 379 */     IdValuePair pair = new IdValuePair(name, value);
/*     */     
/* 381 */     pair.setLineNumber(this.body.getLineNumber());
/*     */     
/* 383 */     this.all_pairs.add(pair);
/*     */   }
/*     */   
/*     */   private static boolean is_white_space(int c) {
/* 387 */     return (c == 32 || c == 9);
/*     */   }
/*     */   
/*     */   private static int skip_white_space(char[] array, int begin, int end) {
/* 391 */     int cursor = begin;
/* 392 */     for (; cursor != end; cursor++) {
/* 393 */       int c = array[cursor];
/* 394 */       if (!is_white_space(c))
/*     */         break; 
/* 396 */     }  return cursor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int skip_matched_prefix(String prefix, char[] array, int begin, int end) {
/* 402 */     int cursor = -1;
/* 403 */     int prefix_length = prefix.length();
/* 404 */     if (prefix_length <= end - begin) {
/* 405 */       cursor = begin;
/* 406 */       for (int i = 0; i != prefix_length; i++, cursor++) {
/* 407 */         if (prefix.charAt(i) != array[cursor]) {
/* 408 */           cursor = -1; break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 412 */     return cursor;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean equals(String str, char[] array, int begin, int end) {
/* 417 */     if (str.length() == end - begin) {
/* 418 */       for (int i = begin, j = 0; i != end; i++, j++) {
/* 419 */         if (array[i] != str.charAt(j)) return false; 
/*     */       } 
/* 421 */       return true;
/*     */     } 
/* 423 */     return false;
/*     */   }
/*     */   
/*     */   private static int skip_name_char(char[] array, int begin, int end) {
/* 427 */     int cursor = begin;
/* 428 */     for (; cursor != end; cursor++) {
/* 429 */       int c = array[cursor];
/* 430 */       if ((97 > c || c > 122) && (65 > c || c > 90) && (
/* 431 */         48 > c || c > 57) && 
/* 432 */         c != 95) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 438 */     return cursor;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 442 */     Main self = new Main();
/* 443 */     int status = self.exec(args);
/* 444 */     System.exit(status);
/*     */   }
/*     */   
/*     */   private int exec(String[] args) {
/* 448 */     this.R = new ToolErrorReporter(true, System.err);
/*     */     
/* 450 */     int arg_count = process_options(args);
/*     */     
/* 452 */     if (arg_count == 0) {
/* 453 */       option_error(ToolErrorReporter.getMessage("msg.idswitch.no_file_argument"));
/*     */       
/* 455 */       return -1;
/*     */     } 
/* 457 */     if (arg_count > 1) {
/* 458 */       option_error(ToolErrorReporter.getMessage("msg.idswitch.too_many_arguments"));
/*     */       
/* 460 */       return -1;
/*     */     } 
/*     */     
/* 463 */     this.P = new CodePrinter();
/* 464 */     this.P.setIndentStep(4);
/* 465 */     this.P.setIndentTabSize(0);
/*     */     
/*     */     try {
/* 468 */       process_file(args[0]);
/*     */     }
/* 470 */     catch (IOException ex) {
/* 471 */       print_error(ToolErrorReporter.getMessage("msg.idswitch.io_error", ex.toString()));
/*     */       
/* 473 */       return -1;
/*     */     }
/* 475 */     catch (EvaluatorException ex) {
/* 476 */       return -1;
/*     */     } 
/* 478 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private int process_options(String[] args) {
/* 483 */     int status = 1;
/*     */     
/* 485 */     boolean show_usage = false;
/* 486 */     boolean show_version = false;
/*     */     
/* 488 */     int N = args.length; int i;
/* 489 */     label40: for (i = 0; i != N; i++) {
/* 490 */       String arg = args[i];
/* 491 */       int arg_length = arg.length();
/* 492 */       if (arg_length >= 2 && 
/* 493 */         arg.charAt(0) == '-') {
/* 494 */         if (arg.charAt(1) == '-') {
/* 495 */           if (arg_length == 2) {
/* 496 */             args[i] = null; break;
/*     */           } 
/* 498 */           if (arg.equals("--help")) {
/* 499 */             show_usage = true;
/*     */           }
/* 501 */           else if (arg.equals("--version")) {
/* 502 */             show_version = true;
/*     */           } else {
/*     */             
/* 505 */             option_error(ToolErrorReporter.getMessage("msg.idswitch.bad_option", arg));
/*     */             
/* 507 */             status = -1;
/*     */             break;
/*     */           } 
/*     */         } else {
/* 511 */           for (int j = 1; j != arg_length; j++) {
/* 512 */             char c = arg.charAt(j);
/* 513 */             switch (c) { case 'h':
/* 514 */                 show_usage = true; break;
/*     */               default:
/* 516 */                 option_error(ToolErrorReporter.getMessage("msg.idswitch.bad_option_char", String.valueOf(c)));
/*     */ 
/*     */ 
/*     */                 
/* 520 */                 status = -1;
/*     */                 break label40; }
/*     */ 
/*     */           
/*     */           } 
/*     */         } 
/* 526 */         args[i] = null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 531 */     if (status == 1) {
/* 532 */       if (show_usage) { show_usage(); status = 0; }
/* 533 */        if (show_version) { show_version(); status = 0; }
/*     */     
/*     */     } 
/* 536 */     if (status != 1) System.exit(status);
/*     */     
/* 538 */     return remove_nulls(args);
/*     */   }
/*     */   
/*     */   private void show_usage() {
/* 542 */     System.out.println(ToolErrorReporter.getMessage("msg.idswitch.usage"));
/*     */     
/* 544 */     System.out.println();
/*     */   }
/*     */   
/*     */   private void show_version() {
/* 548 */     System.out.println(ToolErrorReporter.getMessage("msg.idswitch.version"));
/*     */   }
/*     */ 
/*     */   
/*     */   private void option_error(String str) {
/* 553 */     print_error(ToolErrorReporter.getMessage("msg.idswitch.bad_invocation", str));
/*     */   }
/*     */ 
/*     */   
/*     */   private void print_error(String text) {
/* 558 */     System.err.println(text);
/*     */   }
/*     */   
/*     */   private int remove_nulls(String[] array) {
/* 562 */     int N = array.length;
/* 563 */     int cursor = 0;
/* 564 */     for (; cursor != N && 
/* 565 */       array[cursor] != null; cursor++);
/*     */     
/* 567 */     int destination = cursor;
/* 568 */     if (cursor != N) {
/* 569 */       cursor++;
/* 570 */       for (; cursor != N; cursor++) {
/* 571 */         String elem = array[cursor];
/* 572 */         if (elem != null) {
/* 573 */           array[destination] = elem; destination++;
/*     */         } 
/*     */       } 
/*     */     } 
/* 577 */     return destination;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\idswitch\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */