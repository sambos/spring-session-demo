package org.rsol.poc.springsession.hazlecast;

import java.io.*;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

/**
 * A {@link StreamSerializer} that uses Java serialization to persist the
 * session. This is certainly not the most efficient way to persist sessions,
 * but the example is intended to demonstrate using minimal dependencies. For
 * better serialization methods try using <a
 * href="https://github.com/EsotericSoftware/kryo">Kryo</a>.
 *
 *
 *
 */
public class ObjectStreamSerializer implements StreamSerializer<Object> {
    public int getTypeId() {
        return 2;
    }

    public void write(ObjectDataOutput objectDataOutput, Object object)
           throws IOException {
        ObjectOutputStream out = new ObjectOutputStream((OutputStream) objectDataOutput);
        out.writeObject(object);
        out.flush();
    }

    public Object read(ObjectDataInput objectDataInput)
           throws IOException {
        ObjectInputStream in = new ObjectInputStream((InputStream) objectDataInput);
        try {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    public void destroy() {
    }

}