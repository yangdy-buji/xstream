package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

import java.util.Iterator;
import java.util.Map;

public class MapConverter extends AbstractCollectionConverter {

    public MapConverter(ClassMapper classMapper,String classAttributeIdentifier) {
        super(classMapper,classAttributeIdentifier);
    }

    public boolean canConvert(Class type) {
        return Map.class.isAssignableFrom(type);
    }

    public void toXML(MarshallingContext context) {
        Map map = (Map) context.currentObject();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            context.xmlStartElement("entry");

            writeItem(entry.getKey(), context);
            writeItem(entry.getValue(), context);

            context.xmlEndElement();
        }
    }

    public Object fromXML(UnmarshallingContext context) {
        Map map = (Map) createCollection(context.getRequiredType());
        while (context.xmlNextChild()) {

            context.xmlNextChild();
            Object key = readItem(context);
            context.xmlPop();

            context.xmlNextChild();
            Object value = readItem(context);
            context.xmlPop();

            map.put(key, value);

            context.xmlPop();
        }
        return map;
    }

}
