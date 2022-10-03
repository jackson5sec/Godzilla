package com.kichik.pecoff4j.io;

import java.io.IOException;

public interface IDataWriter {
  void writeByte(int paramInt) throws IOException;
  
  void writeByte(int paramInt1, int paramInt2) throws IOException;
  
  void writeWord(int paramInt) throws IOException;
  
  void writeDoubleWord(int paramInt) throws IOException;
  
  void writeLong(long paramLong) throws IOException;
  
  void writeBytes(byte[] paramArrayOfbyte) throws IOException;
  
  void writeUtf(String paramString) throws IOException;
  
  void writeUtf(String paramString, int paramInt) throws IOException;
  
  int getPosition();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\io\IDataWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */