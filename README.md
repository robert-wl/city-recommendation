# Lacak.io Coding Challenge

Rest API endpoint that provides auto-complete suggestions for large cities.
The deployed API can be accessed at

- [https://city-recommendation-60265293204.asia-southeast2.run.app](https://city-recommendation-60265293204.asia-southeast2.run.app)

### API Documentation

The Swagger UI can be accessed at `/swagger`  
The Api documentation can be accessed at `/api-docs`

### Endpoint Details

- The endpoint is exposed at `/v1/suggestions`

- The partial (or complete) search term is passed as a querystring parameter `q`
- The caller's location can optionally be supplied via querystring parameters `latitude` and `longitude` to help improve
  relative scores
- The scoring algorithm can be optionally supplied via querystring parameter `algorithm`.
    - 0 for Levenshtein distance
    - 1 for Jaro-Winkler distance
    - 2 for Jaccard distance (default)
- The minimum population can be optionally supplied via querystring parameter `minPopulation` to filter out cities with
  population less than the specified value
- The maximum population can be optionally supplied via querystring parameter `maxPopulation` to filter out cities with
  population greater than the specified value
- The pagination parameters can be optionally supplied via querystring parameters `page` and `pageSize` to paginate the
  results

- The endpoint returns a JSON response with an array of scored suggested matches
    - The suggestions are sorted by descending score
    - Each suggestion has a score between 0 and 1 (inclusive) indicating confidence in the suggestion (1 is most
      confident)
    - Each suggestion has a name which can be used to disambiguate between similarly named locations
    - Each suggestion has a latitude and longitude

### Architecture and Design

The application uses a layered architecture with the following components:

1. Controller
2. Service
3. Repository

The application does not use any database and the data is loaded from a TSV file into memory. An Q-Gram/N-Gram index is
used
as a kind of in-memory 'database' to speed up the search process. In the implementation, it indexes the city names and
alternative names.

The application uses dependency injections on almost every component to make it easier to test. The *lombok* package is
used to reduce boilerplate code.
The dependencies are injected through the constructor, which may not be visible due to the usage of
*lombok's* `@AllArgsConstructor` Annotations

### Key Features

1. **Fuzzy search using Levenshtein distance, Jaro-Winkler distance, and Jaccard distance**
    - These algorithms are used to calculate the similarity between the search query and the city names and to get the
      similarity scores for each results. User can choose the algorithm to be used for the search.
2. **Haversine distance calculation for longitude and latitude**
    - This algorithm is used to calculate the distance between the user's location and the city's location to get the
      relative scores for each results. The relative scores are calculated based on the distance between the user's
      longitude and latitude and the city's longitude and latitude.
3. **QGram/NGram indexing for faster data searching**
    - Due to the large number of cities and the heavy computation required for the fuzzy search algorithms, the cities
      are indexed using QGram/NGram indexing to speed up the search process. The QGram/NGram indexing is used to create
      an index of the city names and to search for the city names based on the search query.
4. **Data caching for faster data retrieval**
    - Requests made to the API are cached to speed up the data retrieval process. The data is cached for a certain
      period of time to reduce the load on the server and to improve the performance of the API.
5. **Rate limiting for API security**
    - Rate limiting is implemented to prevent abuse of the API and to ensure that the API is used responsibly. The rate
      limiting is used to limit the number of requests that can be made to the API within a certain period of time.
6. **Unit tests**
    - Unit tests are implemented to test the functionality of the API and to ensure that the API works as expected.
7. **Dockerized application**
    - The application is dockerized to make it easy to deploy and run the application in any environment.
      The `Dockerfile`
      can be found in the root directory of the project.
8. **CI/CD pipeline**
    - The application is deployed using Google Cloud Run and the CI/CD pipeline is implemented using GitHub Actions.
      The workflow file can be found in the `.github/workflows` directory. Any changes made to the main branch will be
      automatically deployed to Google Cloud Run.

## Sample responses

    GET /v1/suggestions?q=beac

```json
{
  "suggestions": [
    {
      "name": "Beacon",
      "latitude": 41.50482,
      "longitude": -73.96958,
      "score": 0.6
    },
    {
      "name": "Beachwood",
      "latitude": 41.4645,
      "longitude": -81.50873,
      "score": 0.4
    },
    {
      "name": "Beachwood",
      "latitude": 39.93901,
      "longitude": -74.19292,
      "score": 0.4
    },
    {
      "name": "Beach Park",
      "latitude": 42.42224,
      "longitude": -87.8573,
      "score": 0.333333333333334
    },
    {
      "name": "Beaconsfield",
      "latitude": 45.43341,
      "longitude": -73.86586,
      "score": 0.236363636363636
    },
    {
      "name": "Beacon Square",
      "latitude": 28.20862,
      "longitude": -82.75538,
      "score": 0.236363636363636
    }
  ]
}
```

    GET /v1/suggestions?q=rawrrawrrawrrwar

```json
{
  "suggestions": []
}
```

    GET /v1/suggestions

```json
{
  "status_code": 400,
  "error": "Bad Request",
  "message": {
    "q": "Search query must not be null or empty"
  }
```

## References

- Geonames provides city lists Canada and the USA http://download.geonames.org/export/dump/readme.txt

## Getting Started

Begin by forking this repo and cloning your fork. GitHub has apps for [Mac](http://mac.github.com/) and
[Windows](http://windows.github.com/) that make this easier.
