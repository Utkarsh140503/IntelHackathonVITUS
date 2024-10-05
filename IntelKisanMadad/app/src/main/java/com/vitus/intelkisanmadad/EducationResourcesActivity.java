package com.vitus.intelkisanmadad;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EducationResourcesActivity extends AppCompatActivity {

    private RecyclerView youtubeRecyclerViewEnglish, youtubeRecyclerViewHindi, youtubeRecyclerViewTamil, youtubeRecyclerViewTelugu, webinarRecyclerView;
    private RecyclerView blogRecyclerViewEnglish, blogRecyclerViewHindi, blogRecyclerViewTamil, blogRecyclerViewTelugu;

    private List<String> youtubeLinksEnglish = new ArrayList<>();
    private List<String> youtubeLinksHindi = new ArrayList<>();
    private List<String> youtubeLinksTamil = new ArrayList<>();
    private List<String> youtubeLinksTelugu = new ArrayList<>();
    private List<Webinar> webinarList;

    private String[] blogLinksEnglish = {
            "https://www.farmers.gov/blog",
            "https://mahadhan.co.in/knowledge-centre/blogs/?_page=3"
    };
    private String[] blogLinksHindi = {
            "https://hindi.krishijagran.com/blog/",
            "https://www.agricultureinformation.com/category/hindi/"
    };
    private String[] blogLinksTamil = {
            "https://www.tamilagri.com/blog",
            "https://www.agricultureindia.in/tamil-agriculture-blog/"
    };
    private String[] blogLinksTelugu = {
            "https://telugu.agricultureinformation.com/",
            "https://www.teluguagriculture.com/blog"
    };

    private static final String YOUTUBE_API_KEY = "AIzaSyBf4yqwxzvjvxGWE_rdBbKYPTa9DVg1VMA";
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static final String TAG = "YouTubeAPI";
    private static String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_resources);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }
        userId = getIntent().getStringExtra("userid");

        setupRecyclerViews();
        setupViewMoreButtons();

        // Fetch YouTube videos
        fetchYouTubeVideos("Farming Blogs in English", youtubeLinksEnglish, youtubeRecyclerViewEnglish);
        fetchYouTubeVideos("Farming Blogs in Hindi", youtubeLinksHindi, youtubeRecyclerViewHindi);
        fetchYouTubeVideos("Farming Blogs in Tamil", youtubeLinksTamil, youtubeRecyclerViewTamil);
        fetchYouTubeVideos("Farming Blogs in Telugu", youtubeLinksTelugu, youtubeRecyclerViewTelugu);

        // Setup Blog Adapters
        setupBlogAdapter(blogRecyclerViewEnglish, blogLinksEnglish);
        setupBlogAdapter(blogRecyclerViewHindi, blogLinksHindi);
        setupBlogAdapter(blogRecyclerViewTamil, blogLinksTamil);
        setupBlogAdapter(blogRecyclerViewTelugu, blogLinksTelugu);
    }

    private void setupRecyclerViews() {
        youtubeRecyclerViewEnglish = findViewById(R.id.youtubeRecyclerView_english);
        youtubeRecyclerViewHindi = findViewById(R.id.youtubeRecyclerView_hindi);
        youtubeRecyclerViewTamil = findViewById(R.id.youtubeRecyclerView_tamil);
        youtubeRecyclerViewTelugu = findViewById(R.id.youtubeRecyclerView_telugu);
        webinarRecyclerView = findViewById(R.id.webinarRecyclerView);
        webinarList = new ArrayList<>();

        // Populate two webinars
        webinarList.add(new Webinar("Smart Farming Webinar", "10th Sept 2024", "10:00 AM", "Delhi", R.drawable.webinar1));
        webinarList.add(new Webinar("AgriTech Innovations", "15th Sept 2024", "2:00 PM", "Mumbai", R.drawable.webinar2));

        // Setup RecyclerView
        WebinarAdapter webinarAdapter = new WebinarAdapter(webinarList, userId);
        webinarRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        webinarRecyclerView.setAdapter(webinarAdapter);

        blogRecyclerViewEnglish = findViewById(R.id.blogRecyclerView_english);
        blogRecyclerViewHindi = findViewById(R.id.blogRecyclerView_hindi);
        blogRecyclerViewTamil = findViewById(R.id.blogRecyclerView_tamil);
        blogRecyclerViewTelugu = findViewById(R.id.blogRecyclerView_telugu);
    }

    private void setupViewMoreButtons() {
        setupViewMoreYouTubeButton(findViewById(R.id.viewMoreYtVideos_english), "Farming Vlogs in English");
        setupViewMoreYouTubeButton(findViewById(R.id.viewMoreYtVideos_hindi), "Farming Vlogs in Hindi");
        setupViewMoreYouTubeButton(findViewById(R.id.viewMoreYtVideos_tamil), "Farming Vlogs in Tamil");
        setupViewMoreYouTubeButton(findViewById(R.id.viewMoreYtVideos_telugu), "Farming Vlogs in Telugu");

        setupViewMoreBlogsButton(findViewById(R.id.viewMoreBlogs_english), "Farming Blogs in English");
        setupViewMoreBlogsButton(findViewById(R.id.viewMoreBlogs_hindi), "Farming Blogs in Hindi");
        setupViewMoreBlogsButton(findViewById(R.id.viewMoreBlogs_tamil), "Farming Blogs in Tamil");
        setupViewMoreBlogsButton(findViewById(R.id.viewMoreBlogs_telugu), "Farming Blogs in Telugu");
    }

    private void setupViewMoreYouTubeButton(TextView viewMoreButton, String query) {
        viewMoreButton.setOnClickListener(v -> {
            Intent youtubeIntent = new Intent(Intent.ACTION_SEARCH);
            youtubeIntent.setPackage("com.google.android.youtube");
            youtubeIntent.putExtra("query", query);
            youtubeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(youtubeIntent);
            } catch (Exception e) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/results?search_query=" + query.replace(" ", "+")));
                startActivity(browserIntent);
            }
        });
    }

    private void setupViewMoreBlogsButton(TextView viewMoreButton, String query) {
        viewMoreButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/search?q=" + query.replace(" ", "+")));
            startActivity(browserIntent);
        });
    }

    private void fetchYouTubeVideos(String query, List<String> youtubeLinks, RecyclerView recyclerView) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YouTubeApiService apiService = retrofit.create(YouTubeApiService.class);
        Call<YouTubeResponse> call = apiService.searchYouTubeVideos(
                "snippet", query, "video", YOUTUBE_API_KEY, "date", 10);

        call.enqueue(new Callback<YouTubeResponse>() {
            @Override
            public void onResponse(Call<YouTubeResponse> call, Response<YouTubeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<YouTubeVideoItem> videoItems = response.body().items;
                    for (YouTubeVideoItem item : videoItems) {
                        youtubeLinks.add("https://www.youtube.com/watch?v=" + item.id.videoId);
                    }
                    setupYouTubeAdapter(youtubeLinks, recyclerView);
                } else {
                    Log.e(TAG, "API response unsuccessful: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<YouTubeResponse> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }

    private void setupYouTubeAdapter(List<String> youtubeLinks, RecyclerView recyclerView) {
        YouTubeAdapter youtubeAdapter = new YouTubeAdapter(youtubeLinks.toArray(new String[0]));
        recyclerView.setAdapter(youtubeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupBlogAdapter(RecyclerView recyclerView, String[] blogLinks) {
        BlogAdapter blogAdapter = new BlogAdapter(blogLinks);
        recyclerView.setAdapter(blogAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
}
