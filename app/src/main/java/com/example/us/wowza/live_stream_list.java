package com.example.us.wowza;

import com.google.gson.annotations.SerializedName;

public class live_stream_list {

    @SerializedName("live_streams")
    private live_streams live_streams;

    public void setLive_streams(live_stream_list.live_streams live_streams) {
        this.live_streams = live_streams;
    }

    public live_stream_list.live_streams getLive_streams() {
        return live_streams;
    }

    public class live_streams {

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("updated_at")
        private String updated_at;

        private String user_name;
        private int user_id;
        private String user_img;

        public void setId(String id) {
            this.id = id;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getCreated_at() {
            return created_at;
        }


    }



}
