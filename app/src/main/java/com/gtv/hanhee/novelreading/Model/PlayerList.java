package com.gtv.hanhee.novelreading.Model;

import java.util.List;

public class PlayerList {


    public PlayerListResult result;

    public static class PlayerListResult {
        public Creator creator;

        public List<Track> tracks;
    }

    public static class Creator {
        public String signature;
        public String avatarUrl;
    }

    public static class Track {
        public int id;
        public String name;
    }
}
