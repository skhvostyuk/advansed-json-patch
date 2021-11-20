package com.github.jsonpatch.advansed;

import com.fasterxml.jackson.databind.JsonNode;

public interface Patch {

    JsonNode apply(JsonNode node) throws JsonPatchException;
}
