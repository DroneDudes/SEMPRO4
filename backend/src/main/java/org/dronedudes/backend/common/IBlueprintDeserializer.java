package org.dronedudes.backend.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.dronedudes.backend.Blueprint.Blueprint;
import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.common.IBlueprint;

import java.io.IOException;

public class IBlueprintDeserializer extends JsonDeserializer<IBlueprint> {
    @Override
    public IBlueprint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Blueprint blueprint = new Blueprint();
        blueprint.setId(node.get("id").asLong());
        blueprint.setProductTitle(node.get("productTitle").asText());
        blueprint.setDescription(node.get("description").asText());

        JsonNode partsNode = node.get("parts");
        if (partsNode != null && partsNode.isArray()) {
            for (JsonNode partNode : partsNode) {
                Part part = new Part();
                part.setId(partNode.get("id").asLong());
                part.setName(partNode.get("name").asText());
                part.setDescription(partNode.get("description").asText());
                part.setSpecifications(partNode.get("specifications").asText());
                part.setSupplierDetails(partNode.get("supplierDetails").asText());
                part.setPrice(partNode.get("price").asLong());
                blueprint.addPart(part);
            }
        }

        return blueprint;
    }
}