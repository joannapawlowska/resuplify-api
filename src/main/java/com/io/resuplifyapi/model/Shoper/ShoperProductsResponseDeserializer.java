package com.io.resuplifyapi.model.Shoper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;

public class ShoperProductsResponseDeserializer extends StdDeserializer<ShoperProductsResponse> {

    public ShoperProductsResponseDeserializer() {
        this(null);
    }

    protected ShoperProductsResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ShoperProductsResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        final JsonNode productsNode = jsonNode.get("body").get("list");
        final CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, ShoperProduct.class);
        return new ShoperProductsResponse(mapper.reader(collectionType).readValue(productsNode));
    }
}