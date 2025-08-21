package com.example.vulndemo.lib;

import com.google.common.collect.ImmutableList;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.commons.collections.map.HashedMap;

import java.util.List;
import java.util.Map;

public class LegacyLibUsage {

    public int sampleGuavaListSize() {
        List<String> list = ImmutableList.of("a", "b", "c");
        return list.size();
    }

    public String buildHttpClientInfo() {
        HttpClient client = HttpClients.createDefault();
        return client.getClass().getName();
    }

    @SuppressWarnings("unchecked")
    public String sampleCommonsCollectionsMapType() {
        Map<String, String> m = new HashedMap();
        m.put("k", "v");
        return m.getClass().getSimpleName();
    }
}
