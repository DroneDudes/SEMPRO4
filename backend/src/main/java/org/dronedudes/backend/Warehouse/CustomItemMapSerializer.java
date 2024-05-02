package org.dronedudes.backend.Warehouse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Product.Product;
import org.dronedudes.backend.item.Item;

import java.io.IOException;
import java.util.Map;

public class CustomItemMapSerializer extends StdSerializer<Map<Long, Item>> {

    public CustomItemMapSerializer() {
        this(null);
    }

    public CustomItemMapSerializer(Class<Map<Long, Item>> t) {
        super(t);
    }

    @Override
    public void serialize(
            Map<Long, Item> items,
            JsonGenerator generator,
            SerializerProvider provider
    ) throws IOException {
        generator.writeStartObject();
        for (Map.Entry<Long, Item> entry : items.entrySet()) {
            generator.writeFieldName(entry.getKey().toString());
            Item item = entry.getValue();
            if (item instanceof Part) {
                serializePart((Part) item, generator, provider);
            } else if (item instanceof Product) {
                serializeProduct((Product) item, generator, provider);
            }
        }
        generator.writeEndObject();
    }

    private void serializePart(Part part, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", part.getId());
        gen.writeStringField("name", part.getName());
        gen.writeStringField("description", part.getDescription());
        gen.writeStringField("specifications", part.getSpecifications());
        gen.writeStringField("supplierDetails", part.getSupplierDetails());
        gen.writeNumberField("price", part.getPrice());
        gen.writeEndObject();
    }

    private void serializeProduct(Product product, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", product.getId());
        gen.writeStringField("name", product.getName());
        gen.writeStringField("description", product.getDescription());
        gen.writeEndObject();
    }
}

