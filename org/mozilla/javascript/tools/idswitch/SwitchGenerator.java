/*     */ package org.mozilla.javascript.tools.idswitch;
/*     */ 
/*     */ import org.mozilla.javascript.EvaluatorException;
/*     */ import org.mozilla.javascript.tools.ToolErrorReporter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SwitchGenerator
/*     */ {
/*  13 */   String v_switch_label = "L0";
/*  14 */   String v_label = "L";
/*  15 */   String v_s = "s";
/*  16 */   String v_c = "c";
/*  17 */   String v_guess = "X";
/*  18 */   String v_id = "id";
/*  19 */   String v_length_suffix = "_length";
/*     */   
/*  21 */   int use_if_threshold = 3;
/*  22 */   int char_tail_test_threshold = 2;
/*     */   
/*     */   private IdValuePair[] pairs;
/*     */   private String default_value;
/*     */   private int[] columns;
/*     */   private boolean c_was_defined;
/*     */   private CodePrinter P;
/*     */   private ToolErrorReporter R;
/*     */   private String source_file;
/*     */   
/*     */   public CodePrinter getCodePrinter() {
/*  33 */     return this.P; } public void setCodePrinter(CodePrinter value) {
/*  34 */     this.P = value;
/*     */   }
/*  36 */   public ToolErrorReporter getReporter() { return this.R; } public void setReporter(ToolErrorReporter value) {
/*  37 */     this.R = value;
/*     */   }
/*  39 */   public String getSourceFileName() { return this.source_file; } public void setSourceFileName(String value) {
/*  40 */     this.source_file = value;
/*     */   }
/*     */   public void generateSwitch(String[] pairs, String default_value) {
/*  43 */     int N = pairs.length / 2;
/*  44 */     IdValuePair[] id_pairs = new IdValuePair[N];
/*  45 */     for (int i = 0; i != N; i++) {
/*  46 */       id_pairs[i] = new IdValuePair(pairs[2 * i], pairs[2 * i + 1]);
/*     */     }
/*  48 */     generateSwitch(id_pairs, default_value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateSwitch(IdValuePair[] pairs, String default_value) {
/*  53 */     int begin = 0;
/*  54 */     int end = pairs.length;
/*  55 */     if (begin == end)
/*  56 */       return;  this.pairs = pairs;
/*  57 */     this.default_value = default_value;
/*     */     
/*  59 */     generate_body(begin, end, 2);
/*     */   }
/*     */   
/*     */   private void generate_body(int begin, int end, int indent_level) {
/*  63 */     this.P.indent(indent_level);
/*  64 */     this.P.p(this.v_switch_label); this.P.p(": { ");
/*  65 */     this.P.p(this.v_id); this.P.p(" = "); this.P.p(this.default_value);
/*  66 */     this.P.p("; String "); this.P.p(this.v_guess); this.P.p(" = null;");
/*     */     
/*  68 */     this.c_was_defined = false;
/*  69 */     int c_def_begin = this.P.getOffset();
/*  70 */     this.P.p(" int "); this.P.p(this.v_c); this.P.p(';');
/*  71 */     int c_def_end = this.P.getOffset();
/*  72 */     this.P.nl();
/*     */     
/*  74 */     generate_length_switch(begin, end, indent_level + 1);
/*     */     
/*  76 */     if (!this.c_was_defined) {
/*  77 */       this.P.erase(c_def_begin, c_def_end);
/*     */     }
/*     */     
/*  80 */     this.P.indent(indent_level + 1);
/*  81 */     this.P.p("if ("); this.P.p(this.v_guess); this.P.p("!=null && ");
/*  82 */     this.P.p(this.v_guess); this.P.p("!="); this.P.p(this.v_s);
/*  83 */     this.P.p(" && !"); this.P.p(this.v_guess); this.P.p(".equals("); this.P.p(this.v_s); this.P.p(")) ");
/*  84 */     this.P.p(this.v_id); this.P.p(" = "); this.P.p(this.default_value); this.P.p(";"); this.P.nl();
/*     */ 
/*     */     
/*  87 */     this.P.indent(indent_level + 1);
/*  88 */     this.P.p("break "); this.P.p(this.v_switch_label); this.P.p(";"); this.P.nl();
/*     */     
/*  90 */     this.P.line(indent_level, "}");
/*     */   }
/*     */   
/*     */   private void generate_length_switch(int begin, int end, int indent_level) {
/*     */     boolean use_if;
/*  95 */     sort_pairs(begin, end, -1);
/*     */     
/*  97 */     check_all_is_different(begin, end);
/*     */     
/*  99 */     int lengths_count = count_different_lengths(begin, end);
/*     */     
/* 101 */     this.columns = new int[(this.pairs[end - 1]).idLength];
/*     */ 
/*     */     
/* 104 */     if (lengths_count <= this.use_if_threshold) {
/* 105 */       use_if = true;
/* 106 */       if (lengths_count != 1) {
/* 107 */         this.P.indent(indent_level);
/* 108 */         this.P.p("int "); this.P.p(this.v_s); this.P.p(this.v_length_suffix);
/* 109 */         this.P.p(" = "); this.P.p(this.v_s); this.P.p(".length();");
/* 110 */         this.P.nl();
/*     */       } 
/*     */     } else {
/*     */       
/* 114 */       use_if = false;
/* 115 */       this.P.indent(indent_level);
/* 116 */       this.P.p(this.v_label); this.P.p(": switch (");
/* 117 */       this.P.p(this.v_s); this.P.p(".length()) {");
/* 118 */       this.P.nl();
/*     */     } 
/*     */     
/* 121 */     int same_length_begin = begin;
/* 122 */     int cur_l = (this.pairs[begin]).idLength, l = 0;
/* 123 */     int i = begin; while (true) {
/* 124 */       i++;
/* 125 */       if (i == end || (l = (this.pairs[i]).idLength) != cur_l) {
/*     */         int next_indent;
/* 127 */         if (use_if) {
/* 128 */           this.P.indent(indent_level);
/* 129 */           if (same_length_begin != begin) this.P.p("else "); 
/* 130 */           this.P.p("if (");
/* 131 */           if (lengths_count == 1) {
/* 132 */             this.P.p(this.v_s); this.P.p(".length()==");
/*     */           } else {
/*     */             
/* 135 */             this.P.p(this.v_s); this.P.p(this.v_length_suffix); this.P.p("==");
/*     */           } 
/* 137 */           this.P.p(cur_l);
/* 138 */           this.P.p(") {");
/* 139 */           next_indent = indent_level + 1;
/*     */         } else {
/*     */           
/* 142 */           this.P.indent(indent_level);
/* 143 */           this.P.p("case "); this.P.p(cur_l); this.P.p(":");
/* 144 */           next_indent = indent_level + 1;
/*     */         } 
/* 146 */         generate_letter_switch(same_length_begin, i, next_indent, !use_if, use_if);
/*     */         
/* 148 */         if (use_if) {
/* 149 */           this.P.p("}"); this.P.nl();
/*     */         } else {
/*     */           
/* 152 */           this.P.p("break "); this.P.p(this.v_label); this.P.p(";"); this.P.nl();
/*     */         } 
/*     */         
/* 155 */         if (i == end)
/* 156 */           break;  same_length_begin = i;
/* 157 */         cur_l = l;
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     if (!use_if) {
/* 162 */       this.P.indent(indent_level); this.P.p("}"); this.P.nl();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generate_letter_switch(int begin, int end, int indent_level, boolean label_was_defined, boolean inside_if) {
/* 171 */     int L = (this.pairs[begin]).idLength;
/*     */     
/* 173 */     for (int i = 0; i != L; i++) {
/* 174 */       this.columns[i] = i;
/*     */     }
/*     */     
/* 177 */     generate_letter_switch_r(begin, end, L, indent_level, label_was_defined, inside_if);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean generate_letter_switch_r(int begin, int end, int L, int indent_level, boolean label_was_defined, boolean inside_if) {
/* 186 */     boolean use_if, next_is_unreachable = false;
/* 187 */     if (begin + 1 == end) {
/* 188 */       this.P.p(' ');
/* 189 */       IdValuePair pair = this.pairs[begin];
/* 190 */       if (L > this.char_tail_test_threshold) {
/* 191 */         this.P.p(this.v_guess); this.P.p("="); this.P.qstring(pair.id); this.P.p(";");
/* 192 */         this.P.p(this.v_id); this.P.p("="); this.P.p(pair.value); this.P.p(";");
/*     */       
/*     */       }
/* 195 */       else if (L == 0) {
/* 196 */         next_is_unreachable = true;
/* 197 */         this.P.p(this.v_id); this.P.p("="); this.P.p(pair.value);
/* 198 */         this.P.p("; break "); this.P.p(this.v_switch_label); this.P.p(";");
/*     */       } else {
/*     */         
/* 201 */         this.P.p("if (");
/* 202 */         int column = this.columns[0];
/* 203 */         this.P.p(this.v_s); this.P.p(".charAt("); this.P.p(column); this.P.p(")==");
/* 204 */         this.P.qchar(pair.id.charAt(column));
/* 205 */         for (int j = 1; j != L; j++) {
/* 206 */           this.P.p(" && ");
/* 207 */           column = this.columns[j];
/* 208 */           this.P.p(this.v_s); this.P.p(".charAt("); this.P.p(column); this.P.p(")==");
/* 209 */           this.P.qchar(pair.id.charAt(column));
/*     */         } 
/* 211 */         this.P.p(") {");
/* 212 */         this.P.p(this.v_id); this.P.p("="); this.P.p(pair.value);
/* 213 */         this.P.p("; break "); this.P.p(this.v_switch_label); this.P.p(";}");
/*     */       } 
/*     */       
/* 216 */       this.P.p(' ');
/* 217 */       return next_is_unreachable;
/*     */     } 
/*     */     
/* 220 */     int max_column_index = find_max_different_column(begin, end, L);
/* 221 */     int max_column = this.columns[max_column_index];
/* 222 */     int count = count_different_chars(begin, end, max_column);
/*     */     
/* 224 */     this.columns[max_column_index] = this.columns[L - 1];
/*     */     
/* 226 */     if (inside_if) { this.P.nl(); this.P.indent(indent_level); }
/* 227 */     else { this.P.p(' '); }
/*     */ 
/*     */     
/* 230 */     if (count <= this.use_if_threshold) {
/* 231 */       use_if = true;
/* 232 */       this.c_was_defined = true;
/* 233 */       this.P.p(this.v_c); this.P.p("="); this.P.p(this.v_s);
/* 234 */       this.P.p(".charAt("); this.P.p(max_column); this.P.p(");");
/*     */     } else {
/*     */       
/* 237 */       use_if = false;
/* 238 */       if (!label_was_defined) {
/* 239 */         label_was_defined = true;
/* 240 */         this.P.p(this.v_label); this.P.p(": ");
/*     */       } 
/* 242 */       this.P.p("switch ("); this.P.p(this.v_s);
/* 243 */       this.P.p(".charAt("); this.P.p(max_column); this.P.p(")) {");
/*     */     } 
/*     */     
/* 246 */     int same_char_begin = begin;
/* 247 */     int cur_ch = (this.pairs[begin]).id.charAt(max_column), ch = 0;
/* 248 */     int i = begin; while (true) {
/* 249 */       i++;
/* 250 */       if (i == end || (ch = (this.pairs[i]).id.charAt(max_column)) != cur_ch) {
/*     */         int next_indent;
/* 252 */         if (use_if) {
/* 253 */           this.P.nl(); this.P.indent(indent_level);
/* 254 */           if (same_char_begin != begin) this.P.p("else "); 
/* 255 */           this.P.p("if ("); this.P.p(this.v_c); this.P.p("==");
/* 256 */           this.P.qchar(cur_ch); this.P.p(") {");
/* 257 */           next_indent = indent_level + 1;
/*     */         } else {
/*     */           
/* 260 */           this.P.nl(); this.P.indent(indent_level);
/* 261 */           this.P.p("case "); this.P.qchar(cur_ch); this.P.p(":");
/* 262 */           next_indent = indent_level + 1;
/*     */         } 
/* 264 */         boolean after_unreachable = generate_letter_switch_r(same_char_begin, i, L - 1, next_indent, label_was_defined, use_if);
/*     */ 
/*     */         
/* 267 */         if (use_if) {
/* 268 */           this.P.p("}");
/*     */         
/*     */         }
/* 271 */         else if (!after_unreachable) {
/* 272 */           this.P.p("break "); this.P.p(this.v_label); this.P.p(";");
/*     */         } 
/*     */         
/* 275 */         if (i == end)
/* 276 */           break;  same_char_begin = i;
/* 277 */         cur_ch = ch;
/*     */       } 
/*     */     } 
/*     */     
/* 281 */     if (use_if) {
/* 282 */       this.P.nl();
/* 283 */       if (inside_if) { this.P.indent(indent_level - 1); }
/* 284 */       else { this.P.indent(indent_level); }
/*     */     
/*     */     } else {
/* 287 */       this.P.nl(); this.P.indent(indent_level); this.P.p("}");
/* 288 */       if (inside_if) { this.P.nl(); this.P.indent(indent_level - 1); }
/* 289 */       else { this.P.p(' '); }
/*     */     
/*     */     } 
/* 292 */     this.columns[max_column_index] = max_column;
/*     */     
/* 294 */     return next_is_unreachable;
/*     */   }
/*     */ 
/*     */   
/*     */   private int count_different_lengths(int begin, int end) {
/* 299 */     int lengths_count = 0;
/* 300 */     int cur_l = -1;
/* 301 */     for (; begin != end; begin++) {
/* 302 */       int l = (this.pairs[begin]).idLength;
/* 303 */       if (cur_l != l) {
/* 304 */         lengths_count++; cur_l = l;
/*     */       } 
/*     */     } 
/* 307 */     return lengths_count;
/*     */   }
/*     */   
/*     */   private int find_max_different_column(int begin, int end, int L) {
/* 311 */     int max_count = 0;
/* 312 */     int max_index = 0;
/*     */     
/* 314 */     for (int i = 0; i != L; i++) {
/* 315 */       int column = this.columns[i];
/* 316 */       sort_pairs(begin, end, column);
/* 317 */       int count = count_different_chars(begin, end, column);
/* 318 */       if (count == end - begin) return i; 
/* 319 */       if (max_count < count) {
/* 320 */         max_count = count;
/* 321 */         max_index = i;
/*     */       } 
/*     */     } 
/*     */     
/* 325 */     if (max_index != L - 1) {
/* 326 */       sort_pairs(begin, end, this.columns[max_index]);
/*     */     }
/*     */     
/* 329 */     return max_index;
/*     */   }
/*     */   
/*     */   private int count_different_chars(int begin, int end, int column) {
/* 333 */     int chars_count = 0;
/* 334 */     int cur_ch = -1;
/* 335 */     for (; begin != end; begin++) {
/* 336 */       int ch = (this.pairs[begin]).id.charAt(column);
/* 337 */       if (ch != cur_ch) {
/* 338 */         chars_count++; cur_ch = ch;
/*     */       } 
/*     */     } 
/* 341 */     return chars_count;
/*     */   }
/*     */   
/*     */   private void check_all_is_different(int begin, int end) {
/* 345 */     if (begin != end) {
/* 346 */       IdValuePair prev = this.pairs[begin];
/* 347 */       while (++begin != end) {
/* 348 */         IdValuePair current = this.pairs[begin];
/* 349 */         if (prev.id.equals(current.id)) {
/* 350 */           throw on_same_pair_fail(prev, current);
/*     */         }
/* 352 */         prev = current;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private EvaluatorException on_same_pair_fail(IdValuePair a, IdValuePair b) {
/* 358 */     int line1 = a.getLineNumber(), line2 = b.getLineNumber();
/* 359 */     if (line2 > line1) { int tmp = line1; line1 = line2; line2 = tmp; }
/* 360 */      String error_text = ToolErrorReporter.getMessage("msg.idswitch.same_string", a.id, new Integer(line2));
/*     */     
/* 362 */     return this.R.runtimeError(error_text, this.source_file, line1, null, 0);
/*     */   }
/*     */   
/*     */   private void sort_pairs(int begin, int end, int comparator) {
/* 366 */     heap4Sort(this.pairs, begin, end - begin, comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean bigger(IdValuePair a, IdValuePair b, int comparator) {
/* 372 */     if (comparator < 0) {
/*     */ 
/*     */ 
/*     */       
/* 376 */       int diff = a.idLength - b.idLength;
/* 377 */       if (diff != 0) return (diff > 0); 
/* 378 */       return (a.id.compareTo(b.id) > 0);
/*     */     } 
/*     */     
/* 381 */     return (a.id.charAt(comparator) > b.id.charAt(comparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void heap4Sort(IdValuePair[] array, int offset, int size, int comparator) {
/* 388 */     if (size <= 1)
/* 389 */       return;  makeHeap4(array, offset, size, comparator);
/* 390 */     while (size > 1) {
/* 391 */       size--;
/* 392 */       IdValuePair v1 = array[offset + size];
/* 393 */       IdValuePair v2 = array[offset + 0];
/* 394 */       array[offset + size] = v2;
/* 395 */       array[offset + 0] = v1;
/* 396 */       heapify4(array, offset, size, 0, comparator);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void makeHeap4(IdValuePair[] array, int offset, int size, int comparator) {
/* 403 */     for (int i = size + 2 >> 2; i != 0; ) {
/* 404 */       i--;
/* 405 */       heapify4(array, offset, size, i, comparator);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void heapify4(IdValuePair[] array, int offset, int size, int i, int comparator) {
/*     */     int new_i1, new_i2, new_i3;
/* 413 */     IdValuePair i_val = array[offset + i];
/*     */     while (true) {
/* 415 */       int base = i << 2;
/* 416 */       new_i1 = base | 0x1;
/* 417 */       new_i2 = base | 0x2;
/* 418 */       new_i3 = base | 0x3;
/* 419 */       int new_i4 = base + 4;
/* 420 */       if (new_i4 >= size)
/* 421 */         break;  IdValuePair val1 = array[offset + new_i1];
/* 422 */       IdValuePair val2 = array[offset + new_i2];
/* 423 */       IdValuePair val3 = array[offset + new_i3];
/* 424 */       IdValuePair val4 = array[offset + new_i4];
/* 425 */       if (bigger(val2, val1, comparator)) {
/* 426 */         val1 = val2; new_i1 = new_i2;
/*     */       } 
/* 428 */       if (bigger(val4, val3, comparator)) {
/* 429 */         val3 = val4; new_i3 = new_i4;
/*     */       } 
/* 431 */       if (bigger(val3, val1, comparator)) {
/* 432 */         val1 = val3; new_i1 = new_i3;
/*     */       } 
/* 434 */       if (bigger(i_val, val1, comparator))
/* 435 */         return;  array[offset + i] = val1;
/* 436 */       array[offset + new_i1] = i_val;
/* 437 */       i = new_i1;
/*     */     } 
/* 439 */     if (new_i1 < size) {
/* 440 */       IdValuePair val1 = array[offset + new_i1];
/* 441 */       if (new_i2 != size) {
/* 442 */         IdValuePair val2 = array[offset + new_i2];
/* 443 */         if (bigger(val2, val1, comparator)) {
/* 444 */           val1 = val2; new_i1 = new_i2;
/*     */         } 
/* 446 */         if (new_i3 != size) {
/* 447 */           IdValuePair val3 = array[offset + new_i3];
/* 448 */           if (bigger(val3, val1, comparator)) {
/* 449 */             val1 = val3; new_i1 = new_i3;
/*     */           } 
/*     */         } 
/*     */       } 
/* 453 */       if (bigger(val1, i_val, comparator)) {
/* 454 */         array[offset + i] = val1;
/* 455 */         array[offset + new_i1] = i_val;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\idswitch\SwitchGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */