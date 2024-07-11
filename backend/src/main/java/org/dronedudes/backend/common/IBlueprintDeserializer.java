package org.dronedudes.backend.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.dronedudes.backend.Blueprint.Blueprint;
import org.dronedudes.backend.Part.Part;

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

                // Check if partNode only contains an ID or other fields as well
                if (partNode.has("id")) {
                    part.setId(partNode.get("id").asLong());
                } else {
                    throw new IllegalArgumentException("Missing required field: part id");
                }

                if (partNode.has("name")) {
                    part.setName(partNode.get("name").asText());
                }

                if (partNode.has("description")) {
                    part.setDescription(partNode.get("description").asText());
                }

                if (partNode.has("specifications")) {
                    part.setSpecifications(partNode.get("specifications").asText());
                }

                if (partNode.has("supplierDetails")) {
                    part.setSupplierDetails(partNode.get("supplierDetails").asText());
                }

                if (partNode.has("price")) {
                    part.setPrice(partNode.get("price").asLong());
                }

                blueprint.addPart(part);
            }
        } else {
            throw new IllegalArgumentException("Missing required field: parts");
        }

        return blueprint;
    }
}
