/*package org.dronedudes.backend.Part;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dronedudes.backend.Blueprint.Blueprint;

import java.io.IOException;

public class PartJsonSerializer extends StdSerializer<Part> {
    public PartJsonSerializer() {
        this(null);
    }

    public PartJsonSerializer(Class<Part> t) {
        super(t);
    }

    @Override
    public void serialize(Part part, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", part.getId());
        jsonGenerator.writeStringField("name", part.getName());
        jsonGenerator.writeStringField("description", part.getDescription());
        jsonGenerator.writeStringField("specifications", part.getSpecifications());
        jsonGenerator.writeStringField("supplierDetails", part.getSupplierDetails());
        jsonGenerator.writeNumberField("price", part.getPrice());

        // Serialize blueprints as a list of IDs
        jsonGenerator.writeFieldName("blueprints");
        jsonGenerator.writeStartArray();
        for (Blueprint blueprint : part.getBlueprints()) {
            jsonGenerator.writeNumber(blueprint.getId());
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}


 */