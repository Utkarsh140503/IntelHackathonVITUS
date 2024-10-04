package com.vitus.intelkisanmadad;

import com.google.gson.annotations.SerializedName;

public class YouTubeVideoItem {
    @SerializedName("id")
    public VideoId id;

    @SerializedName("snippet")
    public Snippet snippet;

    public static class VideoId {
        @SerializedName("videoId")
        public String videoId;
    }

    public static class Snippet {
        @SerializedName("title")
        public String title;

        @SerializedName("thumbnails")
        public Thumbnails thumbnails;

        public static class Thumbnails {
            @SerializedName("medium")
            public Thumbnail medium;

            public static class Thumbnail {
                @SerializedName("url")
                public String url;
            }
        }
    }
}
