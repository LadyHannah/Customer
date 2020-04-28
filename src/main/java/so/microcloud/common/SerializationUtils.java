package so.microcloud.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SerializationUtils {
	public static byte[] serialize(Object obj)
	  {
	    ObjectOutputStream oos = null;
	    ByteArrayOutputStream baos = null;
	    try {
	      baos = new ByteArrayOutputStream();
	      oos = new ObjectOutputStream(baos);
	      oos.writeObject(obj);
	      return baos.toByteArray();
	    } catch (IOException e) {
	      e.printStackTrace();
	      return null;
	    } finally {
	      if (oos != null)
	        try {
	          oos.close();
	          baos.close();
	        }
	        catch (IOException localIOException3)
	        {
	        }
	    }
	  }

	 @SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bits)
	  {
	    if ((bits == null) || (bits.length == 0)) {
	      return null;
	    }
	    ObjectInputStream ois = null;
	    ByteArrayInputStream bais = null;
	    try {
	      bais = new ByteArrayInputStream(bits);
	      ois = new ObjectInputStream(new BufferedInputStream(bais));
	      return (T) ois.readObject();
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    } finally {
	      if (ois != null)
	        try {
	          ois.close();
	          bais.close();
	        } catch (IOException localIOException2) {
	        }
	    }
	  }

	  public static byte[][] serializeMany(Object[] objs) {
	    byte[][] many = new byte[objs.length][];
	    for (int i = 0; i < objs.length; ++i) {
	      many[i] = serialize(objs[i]);
	    }
	    return many;
	  }

	  @SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> deserializeMany(List<byte[]> bitslist) {
	    if ((bitslist == null) || (bitslist.size() < 1)) {
	      return null;
	    }
	    ArrayList ret = new ArrayList(bitslist.size());
	    for (byte[] bits : bitslist) {
	      Object t = deserialize(bits);
	      ret.add(t);
	    }
	    return ret;
	  }
}
