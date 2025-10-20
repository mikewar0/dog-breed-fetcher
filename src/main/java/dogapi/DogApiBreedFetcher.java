package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     *
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedFetcher.BreedNotFoundException{
        String url = "https://dog.ceo/api/breed/";
        url = url + breed + "/list";
        Request request = new Request.Builder()
                .url(url)
                .build();
        List<String> subBreeds = new ArrayList<>();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()){
                throw new BreedFetcher.BreedNotFoundException("HTTP" + response.code());
            }
            ResponseBody json = response.body();
            if (json == null){
                throw new BreedFetcher.BreedNotFoundException("Empty Response Body");
            }
            JSONObject obj = new JSONObject(json.string());
            String status = obj.getString("status");
            if (!status.equals("success")) {
                throw new BreedFetcher.BreedNotFoundException("Status not sucessful");
            }
            JSONArray arr = obj.getJSONArray("message");
            for (int i = 0; i < arr.length(); i++) {
                subBreeds.add(arr.getString(i));
            }
            return subBreeds;
        } catch (IOException e) {
            throw new BreedFetcher.BreedNotFoundException(e.getMessage());
        }
    }
}