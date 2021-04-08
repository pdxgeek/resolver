package com.gonzobeans;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Resolver {

    private static final int CACHE_SIZE = 4096;
    private final Map<String, ResolveCollection> resolvers;
    private final LoadingCache<String, Function<String,String>> cache;
    public Resolver() {
        this.resolvers = new LinkedHashMap<>();
        this.cache = Caffeine.newBuilder().maximumSize(CACHE_SIZE).build(this::resolveInput);
    }

    public void addItem(String prefix, String matcher, Function<String, String> function) {

       if (resolvers.containsKey(prefix)) {
           resolvers.get(prefix).addResolveItem(prefix + matcher, function);
       } else {
           var resolver = new ResolveCollection(prefix);
           resolver.addResolveItem(prefix + matcher, function);
           resolvers.put(prefix, resolver);
       }
    }

    public Function<String, String> resolve(String input) {
        return cache.get(input);
    }

    private Function<String, String> resolveInput(String input) {
        for(var resolver : resolvers.values()) {
            if (resolver.resolves(input)) {
                return resolver.resolve(input);
            }
        }
        throw new ResolverException("No match found.");
    }

    private class ResolveCollection {
        private final String prefix;
        private final Pattern prefixPattern;
        private final ArrayList<ResolveItem> resolveItems;

        public ResolveCollection(String prefix) {
            this.prefix = prefix;
            this.prefixPattern = Pattern.compile(prefix + ".*");
            this.resolveItems = new ArrayList<>();
        }

        public boolean resolves(String input) {
            return prefixPattern.matcher(input).matches();
        }

        public Function<String, String> resolve(String input) {
            for (var resolveItem : resolveItems) {
                if (resolveItem.resolves(getSuffix(input))) {
                    return resolveItem.getFinalizer();
                }
            }
            throw new ResolverException("No match found.");
        }

        public void addResolveItem(String input, Function<String, String> function) {
            var resolveItem = new ResolveItem(getSuffix(input), function);
            resolveItems.add(resolveItem);
        }

        private String getSuffix(String input) {
            if (input.startsWith(prefix)) {
                return input.substring(prefix.length());
            } else {
                throw new ResolverException("Invalid Input: " + input);
            }
        }
    }

    private class ResolveItem {
        private final String matcher;
        private final Pattern matcherPattern;
        private final Function<String, String> finalizer;

        public ResolveItem(String matcher, Function<String, String> finalizer) {
            this.matcher = matcher;
            this.matcherPattern = Pattern.compile(matcher);
            this.finalizer = finalizer;
        }

        public String getMatcher() {
            return matcher;
        }

        public boolean resolves(String input) {
            return matcherPattern.matcher(input).matches();
        }

        public Function<String, String> getFinalizer() {
            return finalizer;
        }
    }
}
