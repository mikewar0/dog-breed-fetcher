package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;
    private final BreedFetcher delegate;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.delegate = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedFetcher.BreedNotFoundException{
        if (cache.containsKey(breed)) {
            return new ArrayList<> (cache.get(breed));
        }
            callsMade ++;
            List<String> subBreeds = delegate.getSubBreeds(breed);
            cache.put(breed, subBreeds);
            return new ArrayList<>(cache.get(breed));
    }
    public int getCallsMade() {
        return callsMade;
    }
}