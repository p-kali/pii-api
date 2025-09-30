package com.piiencrypt.pii.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class MaskingSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveType type;

    public MaskingSerializer() {}

    public MaskingSerializer(SensitiveType type) {
        this.type = type;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        switch (type) {
            case SSN:
                gen.writeString(maskSSN(value));
                break;
            case DOB:
                gen.writeString("****-**-**");
                break;
            default:
                gen.writeString("****");
        }
    }

    private String maskSSN(String ssn) {
        if (ssn.length() == 9) {
            return "*****" + ssn.substring(5);
        }
        return "*****";
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property != null) {
            Sensitive ann = property.getAnnotation(Sensitive.class);
            if (ann != null) {
                return new MaskingSerializer(ann.type());
            }
        }
        return this;
    }
}
