package com.kenyanewstv;

import java.util.ArrayList;

public class TVAdapter {

    private static final String BASE_CHANNEL_URL = "https://www.youtube.com/feeds/videos.xml?channel_id=";

    public static ArrayList<TVContainer> initializeTVContainers() {
        ArrayList<TVContainer> tvContainers = new ArrayList<>();
        tvContainers.add(new TVContainer("NTV News", BASE_CHANNEL_URL + "UCqBJ47FjJcl61fmSbcadAVg", new int[]{0xF11B95C9, 0xF172BBF8}, ""));
        tvContainers.add(new TVContainer("KTN News", BASE_CHANNEL_URL + "UCKVsdeoHExltrWMuK0hOWmg", new int[]{0xF1005988, 0xF16FCAFB}, ""));
        tvContainers.add(new TVContainer("K24 News", BASE_CHANNEL_URL + "UCt3SE-Mvs3WwP7UW-PiFdqQ", new int[]{0xF1FE7E01, 0xF1FEB166}, ""));
        tvContainers.add(new TVContainer("Citizen TV", BASE_CHANNEL_URL + "UChBQgieUidXV1CmDxSdRm3g", new int[]{0xF1F68220, 0xF1FAB77E}, ""));
        tvContainers.add(new TVContainer("KTN TV", BASE_CHANNEL_URL + "UCkWr5PLM8hp8M4WNIkjpKsQ", new int[]{0xF1005988, 0xF16FCAFB}, ""));
        tvContainers.add(new TVContainer("KBC News", BASE_CHANNEL_URL + "UCypNjM5hP1qcUqQZe57jNfg", new int[]{0xF1870700, 0xF1B77A66}, ""));
        return tvContainers;
    }
}
