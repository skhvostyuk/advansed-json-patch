/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 * Changed by Sergey Khvostyuk (https://github.com/skhvostyuk)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of this file and of both licenses is available at the root of this
 * project or, if you have the jar distribution, in directory META-INF/, under
 * the names LGPL-3.0.txt and ASL-2.0.txt respectively.
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.jsonpatch.advansed.mergepatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.JsonNumEquals;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public final class SerializationTest {
    private static final JsonNumEquals EQUIVALENCE = JsonNumEquals.getInstance();

    private final ObjectMapper mapper = JacksonUtils.newMapper();
    private final JsonNode nonObjectTestData;
    private final JsonNode objectTestData;

    public SerializationTest()
            throws IOException {
        final String resource1 = "/jsonpatch/mergepatch/serdeser-nonobject.json";
        final String resource2 = "/jsonpatch/mergepatch/serdeser-object.json";
        nonObjectTestData = JsonLoader.fromResource(resource1);
        objectTestData = JsonLoader.fromResource(resource2);
    }

    @Test
    public void nonObjectSerDeserWorksCorrectly() throws IOException {
        final List<Object[]> list = Lists.newArrayList();

        for (final JsonNode node : nonObjectTestData)
            list.add(new Object[]{node});

        for (Object[] objects : list) {
            nonObjectSerDeserWorksCorrectly((JsonNode) objects[0]);
        }
    }


    public void nonObjectSerDeserWorksCorrectly(final JsonNode input)
            throws IOException {
        final String in = input.toString();
        final JsonMergePatch deserialized
                = mapper.readValue(in, JsonMergePatch.class);

        assertNotNull(deserialized);
        assertSame(deserialized.getClass(), NonObjectMergePatch.class);

        final String out = mapper.writeValueAsString(deserialized);
        final JsonNode serialized = JacksonUtils.getReader().readTree(out);

        assertTrue(EQUIVALENCE.equivalent(input, serialized));
    }

    @Test
    public void objectSerDeserWorksCorrectly() throws IOException {
        final List<Object[]> list = Lists.newArrayList();

        for (final JsonNode node : objectTestData)
            list.add(new Object[]{node});

        for (Object[] objects : list) {
            objectSerDeserWorksCorrectly((JsonNode) objects[0]);
        }

    }

    public void objectSerDeserWorksCorrectly(final JsonNode input) throws IOException {
        final String in = input.toString();
        final JsonMergePatch deserialized
                = mapper.readValue(in, JsonMergePatch.class);

        assertNotNull(deserialized);
        assertSame(deserialized.getClass(), ObjectMergePatch.class);

        final String out = mapper.writeValueAsString(deserialized);
        final JsonNode serialized = JacksonUtils.getReader().readTree(out);

        assertTrue(EQUIVALENCE.equivalent(input, serialized));
    }
}
