package org.fife.rsta.ac.java.buildpath;

import java.io.IOException;
import org.fife.rsta.ac.java.classreader.ClassFile;
import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;

public interface SourceLocation {
  CompilationUnit getCompilationUnit(ClassFile paramClassFile) throws IOException;
  
  String getLocationAsString();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\buildpath\SourceLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */