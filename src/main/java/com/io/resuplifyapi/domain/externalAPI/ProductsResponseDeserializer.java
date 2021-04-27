package com.io.resuplifyapi.domain.externalAPI;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductsResponseDeserializer extends StdDeserializer<ProductsResponse> {

    public ProductsResponseDeserializer() {
        this(null);
    }

    protected ProductsResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ProductsResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode productsNode = jsonNode.get("body").get("list");
        CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, ProductModel.class);
        ProductsResponse response;

        try{
            response = new ProductsResponse(mapper.readerFor(collectionType).readValue(productsNode));

        }catch(IllegalArgumentException e){
            response = new ProductsResponse(new ArrayList<>());
        }

        return response;
    }
}