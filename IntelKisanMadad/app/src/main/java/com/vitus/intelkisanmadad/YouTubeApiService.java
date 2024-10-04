package com.vitus.intelkisanmadad;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeApiService {
    @GET("search")
    Call<YouTubeResponse> searchYouTubeVideos(
            @Query("part") String part,
            @Query("q") String query,
            @Query("type") String type,
            @Query("key") String apiKey,
            @Query("order") String order,     // Sorting by date (latest videos)
            @Query("maxResults") int maxResults // Limiting results to 10 videos
    );
}
