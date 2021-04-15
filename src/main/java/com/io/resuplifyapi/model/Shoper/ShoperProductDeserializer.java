package com.io.resuplifyapi.model.Shoper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ShoperProductDeserializer extends StdDeserializer<ShoperProduct> {

    public ShoperProductDeserializer() {
        this(null);
    }

    protected ShoperProductDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ShoperProduct deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        final int id = jsonNode.get("product_id").asInt();
        final String name = jsonNode.get("translations").get("pl_PL").get("name").asText();

        final JsonNode stockNode = jsonNode.get("stock");
        final int stock = stockNode.get("stock").asInt();
        final boolean active = parseActive(stockNode.get("active"));
        final int warnLevel = parseWarnLevel(stockNode.get("warn_level"));

        return new ShoperProduct(id, name, stock, active, warnLevel);
    }

    private boolean parseActive(JsonNode activeNode){
        return activeNode.asText().equals("1");
    }

    private int parseWarnLevel(JsonNode warnLvlNode){
        return warnLvlNode.isNull() ? 1 : warnLvlNode.asInt();
    }
}