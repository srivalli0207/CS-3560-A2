package a2;

import java.util.List;

public interface TwitterUpdateListener 
{
    void onNewsFeedUpdated(String userId, List<String> updatedNewsFeed);
}
