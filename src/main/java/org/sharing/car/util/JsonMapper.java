/*
 *    Copyright 2021 Bhaskar Gogoi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.sharing.car.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.sharing.car.domainvalue.GeoCoordinate;
import org.sharing.car.dto.DriverDTO;
import org.sharing.car.exception.InvalidJsonException;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JsonMapper {

/*
    public static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper.writeValueAsString(obj);
    }

    public static <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new StdDateFormat());
        return objectMapper.readValue(json, clazz);
    }
*/

    public static <T> T mapListFromJson(String json, TypeReference<List<DriverDTO>> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return (T) objectMapper.readValue(json, clazz);
    }

    public static String serialize(Object obj) {
        String json = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException jpe) {
            log.info(jpe.getOriginalMessage());
            throw new InvalidJsonException(jpe);
        }

        return json;
    }

    public static Object deserialize(Class clazz, String json) {
        Object obj = null;
        try {
            obj = new ObjectMapper().registerModule(new JavaTimeModule()).registerModule(new ParameterNamesModule())
                    .registerModule(new SimpleModule().addDeserializer(GeoCoordinate.class, new GeoCoordinateDerializer()))
                    .registerModule(new Jdk8Module()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .readerFor(clazz).readValue(json);
        } catch (JsonProcessingException jpe) {
            log.info(jpe.getOriginalMessage());
            throw new InvalidJsonException(jpe);
        }

        return obj;
    }

    public static JsonNode parse(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonNode rootNode = null;

        try {
            JsonParser parser = factory.createParser(json);

            rootNode = mapper.readTree(parser);
        } catch (IOException ioe) {
            log.info(ioe.getMessage());
            throw new InvalidJsonException(ioe);
        }

        return rootNode;
    }

}
