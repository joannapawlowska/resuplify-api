package com.io.resuplifyapi.domain.externalAPI;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ProductModelDeserializer extends StdDeserializer<ProductModel> {

    public ProductModelDeserializer() {
        this(null);
    }

    protected ProductModelDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ProductModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        final int id = jsonNode.get("product_id").asInt();
        final String name = jsonNode.get("translations").get("pl_PL").get("name").asText();

        final JsonNode stockNode = jsonNode.get("stock");
        final int stock = stockNode.get("stock").asInt();
        final boolean active = parseActive(stockNode.get("active"));
        final int warnLevel = parseWarnLevel(stockNode.get("warn_level"));

        return new ProductModel(id, name, stock, active, warnLevel);
    }

    private boolean parseActive(JsonNode activeNode){
        return activeNode.asText().equals("1");
    }

    private int parseWarnLevel(JsonNode warnLvlNode){
        return warnLvlNode.isNull() ? 1 : warnLvlNode.asInt();
    }
}