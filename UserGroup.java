package a2;

import java.util.*;

public class UserGroup
{
    private String groupId;
    private List<Object> members;

    public UserGroup(String groupId) 
    {
        this.groupId = groupId;
        members = new ArrayList<>();
    }

    public String getGroupId() 
    {
        return groupId;
    }

    public List<Object> getMembers() 
    {
        return members;
    }

    public void addMember(Object member) 
    {
        if (!members.contains(member)) 
        {
            if (member instanceof User) 
            {
                User user = (User) member;
                if (!isUserInAnyGroup(user)) 
                {
                    members.add(user);
                }
                else 
                {
                    System.out.println("User " + user.getUserId() + " is already in another group.");
                }
            } 
            else if (member instanceof UserGroup) 
            {
                UserGroup group = (UserGroup) member;
                if (!isGroupInAnyGroup(group)) 
                {
                    members.add(group);
                } 
                else 
                {
                    System.out.println("Group " + group.getGroupId() + " is already in another group.");
                }
            }
        }
    }

    // Helper method to check if a user is already in any group
    private boolean isUserInAnyGroup(User user) 
    {
        for (Object member : members) 
        {
            if (member instanceof User && ((User) member).getUserId().equals(user.getUserId())) 
            {
                return true;
            } 
            else if (member instanceof UserGroup && ((UserGroup) member).isUserInAnyGroup(user)) 
            {
                return true;
            }
        }
        return false;
    }

    private boolean isGroupInAnyGroup(UserGroup group) 
    {
        for (Object member : members) 
        {
            if (member instanceof UserGroup && ((UserGroup) member).getGroupId().equals(group.getGroupId())) 
            {
                return true;
            } 
            else if (member instanceof UserGroup && ((UserGroup) member).isGroupInAnyGroup(group)) 
            {
                return true;
            }
        }
        return false;
    }

	public boolean isGroupInAnyGroup(User user) 
	{
		// TODO Auto-generated method stub
		return false;
	}
}
