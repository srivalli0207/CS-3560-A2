package a2;

import java.util.ArrayList;
import java.util.List;

public class User 
{

    private String userId;
    private List<String> followers;
    private List<String> following;
    private List<String> newsFeed;
    private int tweetCount;

    public User(String userId) 
    {
        this.userId = userId;
        followers = new ArrayList<>();
        following = new ArrayList<>();
        newsFeed = new ArrayList<>();
        tweetCount = 0;
    }

    public String getUserId() 
    {
        return userId;
    }

    public void postTweet(String message, TwitterService twitterService) 
    {
        newsFeed.add(message);
        tweetCount++; // Increment tweet count
        // Update followers' news feeds
        twitterService.updateFollowersNewsFeed(userId, message);
    }
    
    public int getTweetCount() 
    {
        return tweetCount;
    }

    public void addFollower(String followerId) 
    {
        followers.add(followerId);
    }

    public void addFollowing(String followingId) 
    {
        following.add(followingId);
    }

    public List<String> getFollowers()
    {
        return followers;
    }

    public List<String> getFollowing() 
    {
        return following;
    }

    public List<String> getNewsFeed() 
    {
        return newsFeed;
    }

    public void addToNewsFeed(String message) 
    {
        newsFeed.add(message);
    }
}
