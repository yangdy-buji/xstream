package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ArrayConverter extends AbstractCollectionConverter {

    public ArrayConverter(ClassMapper classMapper,String classAttributeIdentifier) {
        super(classMapper,classAttributeIdentifier);
    }

    public boolean canConvert(Class type) {
        return type.isArray();
    }

    public void toXML(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        int length = Array.getLength(source);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(source, i);
            writeItem(item, context, writer);
        }
    }

    public Object fromXML(HierarchicalStreamReader reader, UnmarshallingContext context) {
        // read the items from xml into a list
        List items = new LinkedList();
        while (reader.nextChild()) {
            Object item = readItem(reader, context);
            items.add(item);
            reader.pop();
        }
        // now convertAnother the list into an array
        // (this has to be done as a separate list as the array size is not
        //  known until all items have been read)
        Object array = Array.newInstance(context.getRequiredType().getComponentType(), items.size());
        int i = 0;
        for (Iterator iterator = items.iterator(); iterator.hasNext();) {
            Object item = (Object) iterator.next();
            Array.set(array, i, item);
            i++;
        }
        return array;
    }
}
