/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.TreeTraverser;
/*     */ import com.google.common.graph.SuccessorsFunction;
/*     */ import com.google.common.graph.Traverser;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public final class Files
/*     */ {
/*     */   private static final int TEMP_DIR_ATTEMPTS = 10000;
/*     */   
/*     */   @Beta
/*     */   public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
/*  87 */     Preconditions.checkNotNull(file);
/*  88 */     Preconditions.checkNotNull(charset);
/*  89 */     return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
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
/*     */   @Beta
/*     */   public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
/* 106 */     Preconditions.checkNotNull(file);
/* 107 */     Preconditions.checkNotNull(charset);
/* 108 */     return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource asByteSource(File file) {
/* 117 */     return new FileByteSource(file);
/*     */   }
/*     */   
/*     */   private static final class FileByteSource
/*     */     extends ByteSource {
/*     */     private final File file;
/*     */     
/*     */     private FileByteSource(File file) {
/* 125 */       this.file = (File)Preconditions.checkNotNull(file);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileInputStream openStream() throws IOException {
/* 130 */       return new FileInputStream(this.file);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 135 */       if (this.file.isFile()) {
/* 136 */         return Optional.of(Long.valueOf(this.file.length()));
/*     */       }
/* 138 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 144 */       if (!this.file.isFile()) {
/* 145 */         throw new FileNotFoundException(this.file.toString());
/*     */       }
/* 147 */       return this.file.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() throws IOException {
/* 152 */       Closer closer = Closer.create();
/*     */       try {
/* 154 */         FileInputStream in = closer.<FileInputStream>register(openStream());
/* 155 */         return ByteStreams.toByteArray(in, in.getChannel().size());
/* 156 */       } catch (Throwable e) {
/* 157 */         throw closer.rethrow(e);
/*     */       } finally {
/* 159 */         closer.close();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 165 */       return "Files.asByteSource(" + this.file + ")";
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
/*     */   public static ByteSink asByteSink(File file, FileWriteMode... modes) {
/* 178 */     return new FileByteSink(file, modes);
/*     */   }
/*     */   
/*     */   private static final class FileByteSink
/*     */     extends ByteSink {
/*     */     private final File file;
/*     */     private final ImmutableSet<FileWriteMode> modes;
/*     */     
/*     */     private FileByteSink(File file, FileWriteMode... modes) {
/* 187 */       this.file = (File)Preconditions.checkNotNull(file);
/* 188 */       this.modes = ImmutableSet.copyOf((Object[])modes);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileOutputStream openStream() throws IOException {
/* 193 */       return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 198 */       return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource asCharSource(File file, Charset charset) {
/* 209 */     return asByteSource(file).asCharSource(charset);
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
/*     */   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
/* 221 */     return asByteSink(file, modes).asCharSink(charset);
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
/*     */   @Beta
/*     */   public static byte[] toByteArray(File file) throws IOException {
/* 237 */     return asByteSource(file).read();
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static String toString(File file, Charset charset) throws IOException {
/* 254 */     return asCharSource(file, charset).read();
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
/*     */   @Beta
/*     */   public static void write(byte[] from, File to) throws IOException {
/* 269 */     asByteSink(to, new FileWriteMode[0]).write(from);
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static void write(CharSequence from, File to, Charset charset) throws IOException {
/* 286 */     asCharSink(to, charset, new FileWriteMode[0]).write(from);
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
/*     */   @Beta
/*     */   public static void copy(File from, OutputStream to) throws IOException {
/* 301 */     asByteSource(from).copyTo(to);
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
/*     */   @Beta
/*     */   public static void copy(File from, File to) throws IOException {
/* 325 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/* 326 */     asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static void copy(File from, Charset charset, Appendable to) throws IOException {
/* 344 */     asCharSource(from, charset).copyTo(to);
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static void append(CharSequence from, File to, Charset charset) throws IOException {
/* 362 */     asCharSink(to, charset, new FileWriteMode[] { FileWriteMode.APPEND }).write(from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static boolean equal(File file1, File file2) throws IOException {
/* 372 */     Preconditions.checkNotNull(file1);
/* 373 */     Preconditions.checkNotNull(file2);
/* 374 */     if (file1 == file2 || file1.equals(file2)) {
/* 375 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     long len1 = file1.length();
/* 384 */     long len2 = file2.length();
/* 385 */     if (len1 != 0L && len2 != 0L && len1 != len2) {
/* 386 */       return false;
/*     */     }
/* 388 */     return asByteSource(file1).contentEquals(asByteSource(file2));
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
/*     */   @Beta
/*     */   public static File createTempDir() {
/* 412 */     File baseDir = new File(System.getProperty("java.io.tmpdir"));
/*     */     
/* 414 */     String baseName = System.currentTimeMillis() + "-";
/*     */     
/* 416 */     for (int counter = 0; counter < 10000; counter++) {
/* 417 */       File tempDir = new File(baseDir, baseName + counter);
/* 418 */       if (tempDir.mkdir()) {
/* 419 */         return tempDir;
/*     */       }
/*     */     } 
/* 422 */     throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + '✏' + ')');
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
/*     */   @Beta
/*     */   public static void touch(File file) throws IOException {
/* 443 */     Preconditions.checkNotNull(file);
/* 444 */     if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
/* 445 */       throw new IOException("Unable to update modification time of " + file);
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
/*     */   
/*     */   @Beta
/*     */   public static void createParentDirs(File file) throws IOException {
/* 460 */     Preconditions.checkNotNull(file);
/* 461 */     File parent = file.getCanonicalFile().getParentFile();
/* 462 */     if (parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 471 */     parent.mkdirs();
/* 472 */     if (!parent.isDirectory()) {
/* 473 */       throw new IOException("Unable to create parent directories of " + file);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static void move(File from, File to) throws IOException {
/* 491 */     Preconditions.checkNotNull(from);
/* 492 */     Preconditions.checkNotNull(to);
/* 493 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/*     */     
/* 495 */     if (!from.renameTo(to)) {
/* 496 */       copy(from, to);
/* 497 */       if (!from.delete()) {
/* 498 */         if (!to.delete()) {
/* 499 */           throw new IOException("Unable to delete " + to);
/*     */         }
/* 501 */         throw new IOException("Unable to delete " + from);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static String readFirstLine(File file, Charset charset) throws IOException {
/* 522 */     return asCharSource(file, charset).readFirstLine();
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
/*     */   @Beta
/*     */   public static List<String> readLines(File file, Charset charset) throws IOException {
/* 545 */     return asCharSource(file, charset)
/* 546 */       .<List<String>>readLines(new LineProcessor<List<String>>()
/*     */         {
/* 548 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 552 */             this.result.add(line);
/* 553 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<String> getResult() {
/* 558 */             return this.result;
/*     */           }
/*     */         });
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
/* 581 */     return asCharSource(file, charset).readLines(callback);
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
/* 601 */     return asByteSource(file).read(processor);
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
/* 619 */     return asByteSource(file).hash(hashFunction);
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
/*     */   @Beta
/*     */   public static MappedByteBuffer map(File file) throws IOException {
/* 639 */     Preconditions.checkNotNull(file);
/* 640 */     return map(file, FileChannel.MapMode.READ_ONLY);
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
/*     */   @Beta
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
/* 662 */     return mapInternal(file, mode, -1L);
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
/*     */   @Beta
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws IOException {
/* 686 */     Preconditions.checkArgument((size >= 0L), "size (%s) may not be negative", size);
/* 687 */     return mapInternal(file, mode, size);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MappedByteBuffer mapInternal(File file, FileChannel.MapMode mode, long size) throws IOException {
/* 692 */     Preconditions.checkNotNull(file);
/* 693 */     Preconditions.checkNotNull(mode);
/*     */     
/* 695 */     Closer closer = Closer.create();
/*     */     
/*     */     try {
/* 698 */       RandomAccessFile raf = closer.<RandomAccessFile>register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
/* 699 */       FileChannel channel = closer.<FileChannel>register(raf.getChannel());
/* 700 */       return channel.map(mode, 0L, (size == -1L) ? channel.size() : size);
/* 701 */     } catch (Throwable e) {
/* 702 */       throw closer.rethrow(e);
/*     */     } finally {
/* 704 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static String simplifyPath(String pathname) {
/* 730 */     Preconditions.checkNotNull(pathname);
/* 731 */     if (pathname.length() == 0) {
/* 732 */       return ".";
/*     */     }
/*     */ 
/*     */     
/* 736 */     Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
/* 737 */     List<String> path = new ArrayList<>();
/*     */ 
/*     */     
/* 740 */     for (String component : components) {
/* 741 */       switch (component) {
/*     */         case ".":
/*     */           continue;
/*     */         case "..":
/* 745 */           if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
/* 746 */             path.remove(path.size() - 1); continue;
/*     */           } 
/* 748 */           path.add("..");
/*     */           continue;
/*     */       } 
/*     */       
/* 752 */       path.add(component);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 758 */     String result = Joiner.on('/').join(path);
/* 759 */     if (pathname.charAt(0) == '/') {
/* 760 */       result = "/" + result;
/*     */     }
/*     */     
/* 763 */     while (result.startsWith("/../")) {
/* 764 */       result = result.substring(3);
/*     */     }
/* 766 */     if (result.equals("/..")) {
/* 767 */       result = "/";
/* 768 */     } else if ("".equals(result)) {
/* 769 */       result = ".";
/*     */     } 
/*     */     
/* 772 */     return result;
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
/*     */   @Beta
/*     */   public static String getFileExtension(String fullName) {
/* 791 */     Preconditions.checkNotNull(fullName);
/* 792 */     String fileName = (new File(fullName)).getName();
/* 793 */     int dotIndex = fileName.lastIndexOf('.');
/* 794 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
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
/*     */   @Beta
/*     */   public static String getNameWithoutExtension(String file) {
/* 809 */     Preconditions.checkNotNull(file);
/* 810 */     String fileName = (new File(file)).getName();
/* 811 */     int dotIndex = fileName.lastIndexOf('.');
/* 812 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
/*     */   @Deprecated
/*     */   static TreeTraverser<File> fileTreeTraverser() {
/* 829 */     return FILE_TREE_TRAVERSER;
/*     */   }
/*     */   
/* 832 */   private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser<File>()
/*     */     {
/*     */       public Iterable<File> children(File file)
/*     */       {
/* 836 */         return Files.fileTreeChildren(file);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 841 */         return "Files.fileTreeTraverser()";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static Traverser<File> fileTraverser() {
/* 869 */     return Traverser.forTree(FILE_TREE);
/*     */   }
/*     */   
/* 872 */   private static final SuccessorsFunction<File> FILE_TREE = new SuccessorsFunction<File>()
/*     */     {
/*     */       public Iterable<File> successors(File file)
/*     */       {
/* 876 */         return Files.fileTreeChildren(file);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private static Iterable<File> fileTreeChildren(File file) {
/* 882 */     if (file.isDirectory()) {
/* 883 */       File[] files = file.listFiles();
/* 884 */       if (files != null) {
/* 885 */         return Collections.unmodifiableList(Arrays.asList(files));
/*     */       }
/*     */     } 
/*     */     
/* 889 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static Predicate<File> isDirectory() {
/* 899 */     return FilePredicate.IS_DIRECTORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static Predicate<File> isFile() {
/* 909 */     return FilePredicate.IS_FILE;
/*     */   }
/*     */   
/*     */   private enum FilePredicate implements Predicate<File> {
/* 913 */     IS_DIRECTORY
/*     */     {
/*     */       public boolean apply(File file) {
/* 916 */         return file.isDirectory();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 921 */         return "Files.isDirectory()";
/*     */       }
/*     */     },
/*     */     
/* 925 */     IS_FILE
/*     */     {
/*     */       public boolean apply(File file) {
/* 928 */         return file.isFile();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 933 */         return "Files.isFile()";
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\Files.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */