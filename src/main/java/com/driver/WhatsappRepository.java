package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashMap<String,User> userMap;

    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMap=new HashMap<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    public String createUser(String name,String mobile) throws Exception
    {
        if(userMap.containsKey(mobile))
            throw new Exception("User already exists");
        else {
            userMap.put(mobile, new User(name, mobile));
            return "SUCCESS";
        }
    }
    public Group createGroup(List<User> users){
        if(users.size()==2)
        {
            String groupName=users.get(1).getName();
            Group group=new Group(groupName,2);
            groupUserMap.put(group,users);
            return group;
        }
        this.customGroupCount++;
        String groupName="Group "+ this.customGroupCount;
        Group group=new Group(groupName,users.size());
        groupUserMap.put(group,users);
        adminMap.put(group,users.get(0));
        return group;

    }
    public int createMessage(String content){
        this.messageId++;
        Message message=new Message(messageId,content,new Date());
        return messageId;

    }
    public boolean checkSender( User sender, Group group)
    {
        List<User> users=groupUserMap.get(group);
        for(User user:users)
        {
            if(user.equals(sender))
            {
                return true;
            }
        }
        return false;
    }
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!groupUserMap.containsKey(group))
            throw new Exception("Group does not exist");
        if(!checkSender(sender,group))
        throw new Exception("You are not allowed to send message");
        List<Message> messages=new ArrayList<>();
        if(groupMessageMap.containsKey(group))
            messages=groupMessageMap.get(group);
        messages.add(message);
        groupMessageMap.put(group,messages);
        return messages.size();

    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        if(!groupUserMap.containsKey(group))
            throw new Exception("Group does not exist");

        if(!adminMap.get(group).equals(approver))
            throw new Exception("Approver does not have rights");

        if(!checkSender(user,group))
            throw new Exception("User is not a participant");

         adminMap.put(group,user);

        return "SUCCESS";

    }
//    public int removeUser(User user){
//
//    }
//    public String findMessage(Date start, Date end, int K){
//
//    }
}
